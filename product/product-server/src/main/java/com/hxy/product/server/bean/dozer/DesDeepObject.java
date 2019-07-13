package com.hxy.product.server.bean.dozer;

import com.hxy.product.server.bean.vo.ProductVo;
import com.hxy.product.server.bean.vo.ProductVo2;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * destinationDeepObject,深复制目标对象
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DesDeepObject
 * @date 2019年07月13日 12:57:00
 */
@Data
@ToString
public class DesDeepObject {

    private String desName;
    private ProductVo2 desProductVo2;
    private List<ProductVo2> desProductVo2List;

}
