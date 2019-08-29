package com.hxy.order.server.controller;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.error.SystemError;
import com.hxy.edge.client.EdgeClient;
import com.hxy.product.client.ProductClient;
import com.hxy.product.client.vo.response.ProductRepVo;
import com.hxy.product.client.vo.resquest.ProductReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private EdgeClient edgeClient;

    @Value("${env}")
    private String env;

    @GetMapping("/get")
    public ApiResponse get(Long id) {
        return productClient.get(id);
    }

    @PostMapping("/addProduct")
    public ApiResponse add(@Valid @RequestBody ProductReqVo productReqVo) {
        return productClient.add(productReqVo);
    }

    @GetMapping("/getProductList")
    public ApiResponse getProductList(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return productClient.list(pageNo, pageSize);
    }

    @GetMapping("/getProductAndNews")
    public ApiResponse getProductAndNews(Long id) {
        ApiResponse response = productClient.get(id);
        if (response.isSuccess()) {
            String news = edgeClient.getNews();
            Map<String, Object> resultMap = new HashMap<>(2);
            resultMap.put("news", news);
            resultMap.put("data", response.getData());
            return ApiResponse.createBySuccess(resultMap);
        }
        return ApiResponse.createByError(SystemError.SYSTEM_ERROR);
    }

    @GetMapping("/internalTest")
    public ApiResponse internalTest(Long id) {
        ProductRepVo response = productClient.internalTest(id);
        return null;
    }

}