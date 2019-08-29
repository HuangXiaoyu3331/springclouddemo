package com.hxy.common.error;

/**
 * 商品服务异常枚举
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductError
 * @date 2019年08月19日 16:22:48
 */
public enum ProductError implements BaseError {
    /**
     * 商品不存在
     */
    PRODUCT_NOT_EXITS(200000, "商品不存在"),
    ;

    private Integer errorCode;
    private String errorMsg;

    ProductError(Integer errorCode, String errorMsg) {
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
        this.errorMsg = msg;
    }
}
