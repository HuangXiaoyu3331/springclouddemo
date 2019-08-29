package com.hxy.product.server.service;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.response.ProductRepVo;
import com.hxy.product.client.vo.resquest.ProductReqVo;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductService
 * @date 2019年07月11日 18:56:54
 */
public interface ProductService {

    ApiResponse add(ProductReqVo model);

    ApiResponse list(int pageNo, int pageSize);

    /**
     * 根据id获取商品
     *
     * @param id 商品id
     * @return ProductModel
     */
    ProductRepVo get(Long id);
}
