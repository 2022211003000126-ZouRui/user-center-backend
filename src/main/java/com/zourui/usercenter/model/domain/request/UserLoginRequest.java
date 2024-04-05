package com.zourui.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8128213025748655183L;
    private String userAccount;
    private String userPassword;
    private String planetCode;
}
