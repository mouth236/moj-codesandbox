package com.mouth.mojcodesandboox;

import com.mouth.mojcodesandboox.model.ExecuteCodeReponse;
import com.mouth.mojcodesandboox.model.ExecuteCodeRequest;
import org.springframework.stereotype.Component;

/**
 * @ClassName JavaNativeCodeSandbox
 * @Description java原生代码沙箱实现（直接复用模板方法）
 * @date 2023/11/23 23:04
 * @Version 1.0
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    @Override
    public ExecuteCodeReponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
