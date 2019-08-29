package com.hxy.product.server.controller;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.response.ProductRepVo;
import com.hxy.product.client.vo.resquest.ProductReqVo;
import com.hxy.product.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: HelloWorldController
 * @date 2019年07月01日 13:56:26
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 根据商品id获取商品
     *
     * @param id 商品id
     * @return ApiResponse
     */
    @GetMapping("/get")
    public ApiResponse get(Long id) {
        return ApiResponse.createBySuccess(productService.get(id));
    }

    @PostMapping("/add")
    public ApiResponse add(@Valid @RequestBody ProductReqVo productModel) {
        return productService.add(productModel);
    }

    @GetMapping("/list")
    public ApiResponse list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return productService.list(pageNo, pageSize);
    }

    /**
     * 对内接口测试，根据id获取商品
     *
     * @param id 商品id
     * @return ProductModel
     */
    @GetMapping("/internalTest")
    public ProductRepVo internalTest(Long id) {
        return productService.get(id);
    }
}
