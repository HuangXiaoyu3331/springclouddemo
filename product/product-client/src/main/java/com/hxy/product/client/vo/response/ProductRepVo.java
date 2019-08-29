package com.hxy.product.client.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品响应vo
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductRepVo
 * @date 2019年08月19日 16:31:47
 */
@Data
public class ProductRepVo {
    private Long id;
    private String name;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
}
