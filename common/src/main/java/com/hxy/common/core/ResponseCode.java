package com.hxy.common.core;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ResponseCode
 * @date 2019年07月01日 19:03:55
 */
public enum ResponseCode {
    //请求成功状态码
    SUCCESS(200, "SUCCESS"),
    //请求失败状态码
    ERROR(500, "ERROR");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 描述
     */
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
