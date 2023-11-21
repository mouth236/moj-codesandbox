package com.mouth.moj.judge.codesandbox.model;

import lombok.Data;

/**
 * @ClassName JudgeInfo
 * @Description 题目判题信息
 * @date 2023/11/17 22:38
 * @Version 1.0
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;
    /**
     * 消耗内存（kb）
     */
    private Long memory;
    /**
     * 花费时间（kb）
     */
    private Long time;
}
