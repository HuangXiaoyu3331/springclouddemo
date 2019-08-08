package com.hxy.product.client;

import com.hxy.common.core.ApiResponse;
import com.hxy.product.client.vo.resquest.ProductResVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
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
@FeignClient(value = "product", path = "/product", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/get")
    ApiResponse get(@RequestParam Long id);

    @GetMapping("/add")
    ApiResponse add(ProductResVo productResVo);

    /**
     * 使用Feign的时候，必须要加@RequestParam注解
     */
    @GetMapping("/list")
    ApiResponse list(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize);

}
@Slf4j
@Component
class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable throwable) {
        log.error("product服务降级:", throwable);
        return new ProductClient() {
            @Override
            public String hello() {
                return null;
            }

            @Override
            public ApiResponse get(Long id) {
                return ApiResponse.createBySuccess();
            }

            @Override
            public ApiResponse add(ProductResVo productResVo) {
                return null;
            }

            @Override
            public ApiResponse list(int pageNo, int pageSize) {
                return null;
            }
        };
    }
}