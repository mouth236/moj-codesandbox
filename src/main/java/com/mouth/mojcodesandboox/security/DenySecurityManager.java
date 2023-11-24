package com.mouth.mojcodesandboox.security;

import java.security.Permission;

/**
 * @ClassName DenySecurityManager
 * @Description 禁用所有权限安全管理器
 * @date 2023/11/21 19:02
 * @Version 1.0
 */
public class DenySecurityManager extends SecurityManager{
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限异常："+perm.toString());
    }
}
