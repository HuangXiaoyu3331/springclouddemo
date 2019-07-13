package com.hxy.product.server.dozer;

import com.hxy.product.server.bean.model.ProductModel;
import lombok.Data;

import java.util.List;

/**
 * sourceDeepObject 深复制源数据对象
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: SrcDeepObject
 * @date 2019年07月13日 12:56:20
 */
@Data
public class SrcDeepObject {

    private String name;
    private ProductModel srcProductModel;
    private List<ProductModel> srcProductModelList;

}
