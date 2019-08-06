package com.hxy.product.client;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.resquest.ProductResVo;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductClient
 * @date 2019年07月01日 14:15:30
 */
//@FeignClient(value = "product", path = "/product")
public interface ProductClient {

    //    @GetMapping("/hello")
    @RequestLine("GET /product/product")
    String hello();

    //    @GetMapping("/get")
    @RequestLine("GET /product/product")
    String get(Long id) throws Exception;

    //    @GetMapping("/add")
    @RequestLine("GET /product/product")
    ApiResponse add(ProductResVo productResVo);

    /**
     * 使用Feign的时候，必须要加@RequestParam注解
     *
     * @param pageNo   页码
     * @param pageSize 每页显示大小
     * @return ApiResponse
     */
//    @GetMapping("/list")
//    @RequestLine("GET /product/product")
//    ApiResponse list(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize);

    @Component
    class ProductClientFallback implements ProductClient {

        private Exception exception;

        public ProductClientFallback(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String hello() {
            return null;
        }

        @Override
        public String get(Long id) throws Exception {
            return null;
        }

        @Override
        public ApiResponse add(ProductResVo productResVo) {
            return null;
        }

//        @Override
//        public ApiResponse list(int pageNo, int pageSize) {
//            return null;
//        }
    }
}