package com.zourui.usercenter.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    //成功
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }
    //失败
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(),message,description);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage());
    }
    public static <T> BaseResponse<T> error(int code,String message,String description){
        return new BaseResponse(code,null,message,description);
    }
}
