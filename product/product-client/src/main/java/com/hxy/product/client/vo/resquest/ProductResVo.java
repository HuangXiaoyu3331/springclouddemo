package com.hxy.product.client.vo.resquest;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 添加商品vo类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductResVo
 * @date 2019年07月13日 17:17:51
 */
@Data
public class ProductResVo {

    @NotBlank
    private String name;
    @Min(value = 0)
    @Max(value = 10000000)
    private BigDecimal price;

}
