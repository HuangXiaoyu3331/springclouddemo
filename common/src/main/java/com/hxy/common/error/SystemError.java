package com.hxy.common.error;

/**
 * 系统异常枚举
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: SystemError
 * @date 2019年07月01日 19:16:50
 */
public enum SystemError implements BaseError {
    /**
     * 系统错误
     */
    SYSTEM_ERROR(100000, "系统错误"),
    /**
     * 参数解析失败
     */
    PARSE_PARAMS_FAIL(100001, "参数解析失败"),
    /**
     * 请求方法不支持
     */
    METHOD_NOT_SUPPORTED(100002, "请求方法不支持"),
    /**
     * 不支持当前媒体类型
     */
    CURRENT_MEDIA_TYPE_NOT_SUPPORTED(100003, "不支持当前媒体类型"),
    /**
     * 服务限流
     */
    CURRENT_LIMITING(100004, "服务限流"),

    /**
     * feign解析提供方异常失败
     */
    FEIGN_ERROR_DECODER_FAIL(1000005, "feign解析提供方异常失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(100006, "参数错误"),

    /**
     *
     */
    SERVICE_NOT_FOUND(100007, "服务不存在"),
    ;

    private Integer errorCode;
    private String errorMsg;

    SystemError(Integer errorCode, String errorMsg) {
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
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
