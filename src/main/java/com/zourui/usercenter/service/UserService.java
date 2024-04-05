package com.zourui.usercenter.service;

import com.zourui.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author www21
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-03-30 09:44:27
 */
public interface UserService extends IService<User> {

    /**
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword,HttpServletRequest request );

    /**
     *
     * @param originUser 原始用户
     * @return  脱敏用户
     */
    User getSafetyUser(User originUser);
    int userLogout(HttpServletRequest request);
}
