package com.mouth.mojcodesandboox;


import com.mouth.mojcodesandboox.model.ExecuteCodeReponse;
import com.mouth.mojcodesandboox.model.ExecuteCodeRequest;

/**
 * @ClassName CodeSandbox
 * @Description TODO
 * @date 2023/11/20 21:01
 * @Version 1.0
 */
public interface CodeSandbox {

    ExecuteCodeReponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
