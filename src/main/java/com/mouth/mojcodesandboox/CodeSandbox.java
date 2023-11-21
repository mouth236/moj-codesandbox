package com.mouth.moj.judge.codesandbox;

import com.mouth.moj.judge.codesandbox.model.ExecuteCodeReponse;
import com.mouth.moj.judge.codesandbox.model.ExecuteCodeRequest;

/**
 * @ClassName CodeSandbox
 * @Description TODO
 * @date 2023/11/20 21:01
 * @Version 1.0
 */
public interface CodeSandbox {

    ExecuteCodeReponse exexuteCode(ExecuteCodeRequest executeCodeRequest);
}
