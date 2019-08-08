package com.hxy.product.server.handler;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.core.SystemError;
import com.hxy.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认异常拦截
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DefaultExceptionHandler
 * @date 2019年07月15日 18:02:58
 */
//@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    /**
     * 400 参数解析失败异常拦截
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse messageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.error("参数解析失败", e);
        return ApiResponse.createByError(SystemError.PARSE_PARAMS_FAIL);
    }

    /**
     * 405 请求方法不支持异常拦截
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse methodNotFoundExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持", e);
        return ApiResponse.createByError(SystemError.METHOD_NOT_SUPPORTED);
    }

    /**
     * 415 请求媒体类型不支持异常拦截
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResponse mediaTypeNotSupportedExceptionHandler(Exception e) {
        log.error("不支持当前媒体类型", e);
        return ApiResponse.createByError(SystemError.CURRENT_MEDIA_TYPE_NOT_SUPPORTED);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ApiResponse handleBindException(Exception e) {
        List<FieldError> errorList = new ArrayList<>();
        if (e instanceof BindException) {
            errorList = ((BindException) e).getFieldErrors();
        } else if (e instanceof MethodArgumentNotValidException) {
            errorList = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        }
        if (errorList.size() > 0) {
            return ApiResponse.createByErrorCodeMessage(SystemError.PARSE_PARAMS_FAIL.getErrorCode(),
                    "参数:" + errorList.get(0).getField() + "的值:" + errorList.get(0).getDefaultMessage() + "无效");
        }
        return ApiResponse.createByError(SystemError.PARSE_PARAMS_FAIL);
    }

    /**
     * 拦截所有异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse processException(Exception e) {
        if (e instanceof AppException) {
            return ApiResponse.createByError(((AppException) e).getBaseError());
        } else {
            return ApiResponse.createByError(SystemError.SYSTEM_ERROR);
        }
    }

}
