package com.hxy.product.server.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.resquest.ProductResVo;
import com.hxy.product.server.bean.model.ProductModel;
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
     * blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/get")
    public ApiResponse get(Long id) {
        return productService.get(id);
    }

    @PostMapping("/add")
    public ApiResponse add(@Valid @RequestBody ProductResVo productModel) {
        return productService.add(productModel);
    }

    @GetMapping("/list")
    public ApiResponse list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return productService.list(pageNo, pageSize);
    }
}
