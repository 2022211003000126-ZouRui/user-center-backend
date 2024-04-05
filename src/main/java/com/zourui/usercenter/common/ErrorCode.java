package com.zourui.usercenter.common;

/**
 * 全局错误码
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NO_Login(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","");
    private final int code;     //错误码
    private final String message;   //错误码描述
    private final String description; //详细描述
    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getDescription() {
        return description;
    }

}
