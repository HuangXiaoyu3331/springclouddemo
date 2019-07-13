package com.hxy.product.server.dao;

import com.hxy.product.server.bean.model.ProductModel;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductModel record);

    int insertSelective(ProductModel record);

    ProductModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductModel record);

    int updateByPrimaryKey(ProductModel record);

    List<ProductModel> selectAll();
}