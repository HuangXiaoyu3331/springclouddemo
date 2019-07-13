package com.hxy.product.server.bean.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductVo2
 * @date 2019年07月13日 11:25:07
 */
@Data
@ToString
public class ProductVo2 {

    private Long productId;
    private String name;
    private BigDecimal productPrice;
    private String productCreateTime;

}
