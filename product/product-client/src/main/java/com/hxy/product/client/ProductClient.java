package com.hxy.product.client;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.resquest.ProductResVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    ApiResponse add(ProductResVo productResVo);

    // 使用Feign的时候，必须要加@RequestParam注解
    @GetMapping("/list")
    ApiResponse list(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize);
}