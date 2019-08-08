package com.hxy.order.server.controller;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.core.SystemError;
import com.hxy.edge.client.EdgeClient;
import com.hxy.product.client.ProductClient;
import com.hxy.product.client.vo.resquest.ProductResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping("/hello")
    public String list() {
        return productClient.hello() + ":" + env;
    }

    @GetMapping("/get")
    public ApiResponse get(Long id) {
        return productClient.get(id);
    }

    @PostMapping("/addProduct")
    public ApiResponse add(@Valid @RequestBody ProductResVo productResVo) {
        return productClient.add(productResVo);
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

}