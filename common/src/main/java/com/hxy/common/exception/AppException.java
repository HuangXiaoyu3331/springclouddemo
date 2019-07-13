package com.hxy.common.exception;

import com.hxy.common.core.BaseError;
import com.hxy.common.core.ErrorMsg;

/**
 * 程序异常类
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: AppException
 * @date 2019年07月01日 19:12:09
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private BaseError baseError;

    public BaseError getBaseError() {
        return baseError;
    }

    public AppException(Exception ex) {
        super(ex.getMessage(), ex);
    }

    public AppException(BaseError error) {
        super(error.getErrorMsg());
        baseError = error;
    }

}
