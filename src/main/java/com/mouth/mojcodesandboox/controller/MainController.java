package com.mouth.mojcodesandboox.controller;

import com.mouth.mojcodesandboox.JavaNativeCodeSandbox;
import com.mouth.mojcodesandboox.model.ExecuteCodeReponse;
import com.mouth.mojcodesandboox.model.ExecuteCodeRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MainController
 * @Description TODO
 * @date 2023/11/21 12:37
 * @Version 1.0
 */
@RestController("/")
public class MainController {


    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";
    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;
    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/executeCode")
    ExecuteCodeReponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        //基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if(!AUTH_REQUEST_SECRET.equals(authHeader)){
            response.setStatus(403);
            return null;
        }
        if(executeCodeRequest == null ){
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    };
}
