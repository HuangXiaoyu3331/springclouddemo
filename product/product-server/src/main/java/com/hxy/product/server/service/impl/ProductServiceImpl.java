package com.hxy.product.server.service.impl;

import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hxy.common.core.ApiResponse;
import com.hxy.common.core.SystemError;
import com.hxy.common.utils.DozerUtil;
import com.hxy.product.server.bean.model.ProductModel;
import com.hxy.product.server.bean.vo.ProductVo;
import com.hxy.product.server.dao.ProductMapper;
import com.hxy.product.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductServiceImpl
 * @date 2019年07月11日 18:57:14
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private Mapper dozerMapper;

    @Override
    public ApiResponse add(ProductModel model) {
        int resultCount = productMapper.insert(model);
        if (resultCount > 0) {
            return ApiResponse.createBySuccess();
        }
        return ApiResponse.createByError(SystemError.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse list(int pageNo, int pageSize) {
        // 开启分页查询
        PageHelper.startPage(pageNo, pageSize);
        List<ProductModel> productList = productMapper.selectAll();
        List<ProductVo> productVoList = DozerUtil.mapList(dozerMapper, productList, ProductVo.class);
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productVoList);
        return ApiResponse.createBySuccess(pageResult);
    }

    @Override
    public ApiResponse get(Long id) {
        ProductModel productModel = productMapper.selectByPrimaryKey(id);
        if (productModel == null) {
            return ApiResponse.createBySuccess("商品不存在");
        }
        return ApiResponse.createBySuccess(productModel);
    }
}