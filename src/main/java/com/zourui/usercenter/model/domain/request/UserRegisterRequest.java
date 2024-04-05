package com.zourui.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    //防止序列化过程中出现冲突
    @Serial
    private static final long serialVersionUID = -574318972994458639L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
