package com.mouth.mojcodesandboox.unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MemoryError
 * @Description 无限占用空间 （浪费系统内存）
 * @date 2023/11/21 17:03
 * @Version 1.0
 */
public class MemoryError {

    public static void main(String[] args) {
        List<byte[]> bytes = new ArrayList<>();
        while(true){
            bytes.add(new byte[10000]);
        }
    }
}
