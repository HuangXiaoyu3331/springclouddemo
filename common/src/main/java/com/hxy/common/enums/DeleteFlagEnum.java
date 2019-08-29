package com.hxy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DeleteFlagEnum
 * @date 2019年08月28日 17:29:58
 */
@AllArgsConstructor
@Getter
public enum DeleteFlagEnum {

    /**
     * 未删除标志
     */
    NO(0, "未删除"),
    /**
     * 删除标志
     */
    YES(1, "已删除"),
    ;

    private Integer code;
    private String msg;
}
