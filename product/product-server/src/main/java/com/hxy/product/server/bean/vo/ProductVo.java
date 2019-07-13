package com.hxy.product.server.bean.vo;

import lombok.Data;
import lombok.ToString;


/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductVo
 * @date 2019年07月12日 17:58:09
 */
@Data
@ToString
public class ProductVo {

    public Long id;
    private String name;
    private String createTime;

}
