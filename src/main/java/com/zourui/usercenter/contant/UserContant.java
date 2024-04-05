package com.zourui.usercenter.contant;

/**
 * 用户常量
 */
public interface UserContant {
    String USER_LOGIN_STATE = "userLoginState"; //用户登录态的键
    /**
     * 0 普通用户
     * 1 管理员
     */
    int DEFAULT_ROLE=0;
    int ADMIN_ROLE=1;
}
