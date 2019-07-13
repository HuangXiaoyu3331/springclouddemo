package com.hxy.order.server.controller;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.core.SystemError;
import com.hxy.common.exception.AppException;
import com.hxy.product.client.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: OrderController
 * @date 2019年07月01日 14:17:33
 */
@RestController
@RequestMapping("/order")
//自动刷新配置需要加上该注解
@RefreshScope
public class OrderController {

    @Autowired
    private ProductClient productClient;
    @Value("${env}")
    private String env;

    @GetMapping("/list")
    public String list() {
        return productClient.hello() + ":" + env;
    }

    @GetMapping("/add")
    public ApiResponse add(){
        return productClient.add();
    }

    @GetMapping("/getProductList")
    public ApiResponse getProductList(int pageNo,int pageSize){
        return productClient.list(pageNo,pageSize);
    }

}