package com.xazhao.common;


import com.xazhao.entity.user.UserInfo;

/**
 * @ClassName CurrentUser.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Version 2024.0.1
 * @Description
 */

public class CurrentUser {

    public CurrentUser() {}

    /**
     * 模拟获取当前登录用户
     *
     * @return 用户信息
     */
    public static UserInfo getCurrentUser() {

        return new UserInfo();
    }

    /**
     * 根据用户ID查询用户，模拟获取当前登录用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public static UserInfo getCurrentUser(String userId) {

        return new UserInfo();
    }
}
