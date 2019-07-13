package com.hxy.common.core;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: SystemError
 * @date 2019年07月01日 19:16:50
 */
public enum SystemError implements BaseError {
    SYSTEM_ERROR(500, "系统错误"),
    ;

    SystemError(Integer errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private Integer errorCode;
    private String errorMsg;

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
