package com.mouth.mojcodesandboox.unsafe;

/**
 * @ClassName SleepError
 * @Description 无线睡眠
 * @date 2023/11/21 16:49
 * @Version 1.0
 */
public class SleepError {

    public static void main(String[] args) throws InterruptedException {
        long ONE_HOUR = 60 * 60 * 1000L;
        Thread.sleep(ONE_HOUR);
        System.out.println("睡完了");
    }
}
