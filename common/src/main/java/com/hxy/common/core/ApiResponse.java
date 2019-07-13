package com.hxy.common.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ApiResponse
 * @date 2019年07月01日 18:53:49
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    /**
     * 请求状态
     */
    private int status;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private T data;

    /**
     * 无参构造函数，反序列化的时候，需要无参构造函数，不然报会 MismatchedInputException 异常
     */
    private ApiResponse() {
    }

    private ApiResponse(int status) {
        this.status = status;
    }

    private ApiResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ApiResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ApiResponse(BaseError baseError) {
        this.status = baseError.getErrorCode();
        this.msg = baseError.getErrorMsg();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * getXXX()、setXXX()、isXXX()开头的都会被序列化成字段
     *
     * @return
     * @ JsonIgnore是忽略该方法，使该方法不序列化为字段
     * 详情可看jackson源码
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    /**
     * 请求成功
     */
    public static <T> ApiResponse<T> createBySuccess() {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode());
    }

    /**
     * 请求成功，带提示信息
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createBySuccessMessage(String msg) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 请求成功，带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createBySuccess(T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    /**
     * 请求成功，带提示信息、数据
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createBySuccess(String msg, T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 请求失败
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createByError() {
        return new ApiResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    /**
     * 请求失败，带提示信息
     *
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createByErrorMessage(String errorMsg) {
        return new ApiResponse<>(ResponseCode.ERROR.getCode(), errorMsg);
    }

    /**
     * 请求失败，带失败状态码跟描述
     *
     * @param errorCode
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createByErrorCodeMessage(int errorCode, String errorMsg) {
        return new ApiResponse<>(errorCode, errorMsg);
    }

    /**
     * 请求失败
     *
     * @param error 错误枚举
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> createByError(BaseError error) {
        return new ApiResponse<>(error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiResponse<?> response = (ApiResponse<?>) o;
        return status == response.status &&
                Objects.equals(msg, response.msg) &&
                Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, msg, data);
    }
}

