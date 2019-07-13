package com.hxy.product.client;

import com.hxy.common.core.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductClient
 * @date 2019年07月01日 14:15:30
 */
@FeignClient(value = "product", path = "/product")
public interface ProductClient {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/get")
    String get(Long id) throws Exception;

    @GetMapping("/add")
    ApiResponse add();

    // 使用Feign的时候，必须要加@RequestParam注解
    @GetMapping("/list")
    ApiResponse list(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize);
}