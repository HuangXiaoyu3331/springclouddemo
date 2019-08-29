package com.hxy.common.core;

import com.hxy.common.error.BaseError;
import lombok.Data;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ErrorMsg
 * @date 2019年07月01日 19:25:49
 */
@Data
public class ErrorMsg {

    private Integer errorCode;
    private String errorMsg;

    public ErrorMsg(BaseError error){
        this.errorCode = error.getErrorCode();
        this.errorMsg = error.getErrorMsg();
    }
}
