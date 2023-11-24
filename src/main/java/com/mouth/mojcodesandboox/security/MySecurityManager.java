package com.mouth.mojcodesandboox.security;

import java.security.Permission;

/**
 * @ClassName MySecurityManager
 * @Description TODO
 * @date 2023/11/21 19:09
 * @Version 1.0
 */
public class MySecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
        //super.checkPermission(perm);
    }

    //检测程序是否允许执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("权限异常" + cmd);
    }

    //检测程序是否允许读文件
    @Override
    public void checkRead(String file) {
//        throw new SecurityException("权限异常" + file);
    }

    //检测程序是否允许写文件
    @Override
    public void checkWrite(String file) {
//        throw new SecurityException("权限异常" + file);
    }

    //检测程序是否允许删除文件
    @Override
    public void checkDelete(String file) {
//        throw new SecurityException("权限异常" + file);
    }

    //检测程序是否允许网络连接
    @Override
    public void checkConnect(String host, int port) {
//        throw new SecurityException("权限异常" + host +":" + port);
    }
}
