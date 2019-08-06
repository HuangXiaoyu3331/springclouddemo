package com.hxy.product.server.dao;

import com.hxy.product.server.bean.model.ProductModel;

import java.util.List;

/**
 * 商品mapper
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductClient
 * @date 2019年07月13日 14:15:30
 */
public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductModel record);

    int insertSelective(ProductModel record);

    ProductModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductModel record);

    int updateByPrimaryKey(ProductModel record);

    List<ProductModel> selectAll();
}