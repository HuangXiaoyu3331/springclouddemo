package com.hxy.product.server.service;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.server.bean.model.ProductModel;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductService
 * @date 2019年07月11日 18:56:54
 */
public interface ProductService {

    ApiResponse add(ProductModel model);

    ApiResponse list(int pageNo, int pageSize);

    ApiResponse get(Long id);
}
