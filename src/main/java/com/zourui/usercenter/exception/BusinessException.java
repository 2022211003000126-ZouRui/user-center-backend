package com.zourui.usercenter.exception;
import com.zourui.usercenter.common.ErrorCode;

/**
 * 自定义异常类
 */
//继承RuntimeException后就不用try catch捕获了
    //给原本的异常扩充了两个字段，并且提供了几个构造函数，支持传递ErrorCode
public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;
    public BusinessException(String message,int code, String description) {
        super(message); //把message传递给父类
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //把message传递给父类
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage()); //把message传递给父类
        this.code = errorCode.getCode();
        this.description = description;
    }
    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}
