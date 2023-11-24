package com.mouth.mojcodesandboox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.mouth.mojcodesandboox.model.ExecuteCodeReponse;
import com.mouth.mojcodesandboox.model.ExecuteCodeRequest;
import com.mouth.mojcodesandboox.model.ExecuteMessage;
import com.mouth.mojcodesandboox.model.JudgeInfo;
import com.mouth.mojcodesandboox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName JavaCodeSandboxTemplate
 * @Description java代码模板方法的实现
 * @date 2023/11/23 21:00
 * @Version 1.0
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox{

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    private static final long TIME_OUT = 5000L;
//    private static final String SECURITY_MANAGER_PATH = "D:\\maven-workspace\\moj-codesandboox\\src\\main\\resources\\security";
//    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    @Override
    public ExecuteCodeReponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        //System.setSecurityManager(new MySecurityManager());

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        //1.将用户代码保存为文件
        File userCodeFile = saveCodeToFile(code);

        //2.编译代码，得到.class文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);

        //3.执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);

        //4.收集整理输出结果
        ExecuteCodeReponse outputResponse = getOutputResponse(executeMessageList);

        //5.文件清理
        boolean b = deleteFile(userCodeFile);
        if(!b){
            log.error("deleteFile error,userCodeFilePath = {}",userCodeFile.getAbsolutePath());
        }
        return outputResponse;
    }
    /**
     * 1、保存代码为文件
     * @param code
     * @return
     */
    public File saveCodeToFile(String code){
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        //判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //将用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;

    }

    /**
     * 2、编译文件
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile){
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue()!=0){
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
            //return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3、执行代码文件，获得执行结果列表
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile,List<String> inputList){
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            //String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main", userCodeParentPath,SECURITY_MANAGER_PATH,SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                //超时控制
                new Thread(()->{
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时了");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProessAndGetMessage(runProcess, "运行");
                //ExecuteMessage executeMessage = ProcessUtils.runInteractiveProessAndGetMessage(runProcess, "运行", inputArgs);
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                //return getErrorResponse(e);
                throw new RuntimeException("执行错误",e);
            }
        }
        return executeMessageList;
    }

    /**
     * 4、获取输出结果
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeReponse getOutputResponse(List<ExecuteMessage> executeMessageList){
        ExecuteCodeReponse executeCodeReponse = new ExecuteCodeReponse();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if(StrUtil.isNotBlank(errorMessage)){
                executeCodeReponse.setMessage(errorMessage);
                //用户提交的代码在执行中存在错误
                executeCodeReponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time !=null){
                maxTime = Math.max(maxTime,time);
            }
        }
        //正常运行完成
        if (outputList.size() == executeMessageList.size()){
            executeCodeReponse.setStatus(1);
        }
        executeCodeReponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        //要借助第三方库来获取内存占用，非常麻烦
        //judgeInfo.setMemory();
        executeCodeReponse.setJudgeInfo(judgeInfo);
        return executeCodeReponse;
    }

    /**
     * 5、删除文件
     * @param userCodeFile
     * @return
     */
    public boolean deleteFile(File userCodeFile){
        if(userCodeFile.getParentFile()!=null){
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功":"失败"));
            return del;
        }
        return true;
    }
    /**
     * 6、获取错误响应
     * @param e
     * @return
     */
    private ExecuteCodeReponse getErrorResponse(Throwable e){
        ExecuteCodeReponse executeCodeReponse = new ExecuteCodeReponse();
        executeCodeReponse.setOutputList(new ArrayList<>());
        executeCodeReponse.setMessage(e.getMessage());
        //表示代码沙箱错误
        executeCodeReponse.setStatus(2);
        executeCodeReponse.setJudgeInfo(new JudgeInfo());
        return executeCodeReponse;
    }
}
