package com.mouth.mojcodesandboox.security;

import java.security.Permission;

/**
 * @ClassName DefaultSecurityManager
 * @Description
 * @date 2023/11/21 18:56
 * @Version 1.0
 */
public class DefaultSecurityManager extends SecurityManager {

    //检查所有权限

    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何限制");
        System.out.println(perm);
        super.checkPermission(perm);
    }


}
