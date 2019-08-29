package com.hxy.common.error;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UserError
 * @date 2019年08月26日 14:22:35
 */
public enum UserError implements BaseError {

    /**
     * 用户已存在
     */
    USERNAME_ALREADY_EXISTS(300000, "用户已存在"),
    EMAIL_ALREADY_EXISTS(300001, "邮箱已存在"),
    USERNAME_OR_PASSWORD_NOT_EXISTS(300001, "用户名或密码正确"),
    NOT_LOGGED_IN(300002, "用户未登录"),
    ;

    private Integer errorCode;
    private String errorMsg;

    UserError(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

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
    public void setErrorMsg(String msg) {
        this.errorMsg = errorMsg;
    }
}
