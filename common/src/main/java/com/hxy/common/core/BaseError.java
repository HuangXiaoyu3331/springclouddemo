package com.hxy.common.core;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BaseError
 * @date 2019年07月01日 19:15:38
 */
public interface BaseError {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    Integer getErrorCode();

    /**
     * 设置错误码
     *
     * @param errorCode 错误码
     */
    void setErrorCode(Integer errorCode);

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getErrorMsg();

    /**
     * 设置错误信息
     *
     * @param msg 错误信息
     */
    void setErrorMsg(String msg);

}
