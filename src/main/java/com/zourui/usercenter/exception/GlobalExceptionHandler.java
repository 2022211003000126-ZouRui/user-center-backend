package com.zourui.usercenter.exception;

import com.zourui.usercenter.common.BaseResponse;
import com.zourui.usercenter.common.ErrorCode;
import com.zourui.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice    //aop切面技术
@Slf4j    //日志注解
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)  //表示这个方法只捕获这个异常类
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);      //只要后端出现运行时异常都会打日志
        return ResultUtils.error(e.getCode(), e.getMessage(),e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)  //表示这个方法只捕获这个异常类
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);      //只要后端出现运行时异常都会打日志
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(),"");
    }
}
