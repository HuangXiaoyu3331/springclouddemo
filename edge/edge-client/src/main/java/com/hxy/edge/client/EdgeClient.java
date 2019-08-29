package com.hxy.edge.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * edge服务feign客户端
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EdgeClient
 * @date 2019年08月08日 16:23:17
 */
@FeignClient(value = "edge", path = "/edge", fallbackFactory = EdgeClientFallbackFactory.class)
public interface EdgeClient {

    /**
     * 获取每日新闻
     * 偶发性抛异常，做降级处理
     *
     * @return 新闻
     */
    @GetMapping("/getNews")
    String getNews();

}

@Slf4j
@Component
class EdgeClientFallbackFactory implements FallbackFactory<EdgeClient> {

    @Override
    public EdgeClient create(Throwable throwable) {
        log.error("edge服务降级", throwable);
        return () -> "今天没有大事发生~";
    }
}
