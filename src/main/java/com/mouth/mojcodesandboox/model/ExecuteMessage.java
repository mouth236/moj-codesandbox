package com.mouth.mojcodesandboox.model;

import lombok.Data;

/**
 * @ClassName ExecuteMessage
 * @Description 进程执行信息
 * @date 2023/11/21 14:57
 * @Version 1.0
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;
    private Long memory;
}
