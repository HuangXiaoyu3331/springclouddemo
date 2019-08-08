package com.hxy.edge.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 边缘服务控制器
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EdgeController
 * @date 2019年08月08日 16:19:30
 */
@RestController
@RequestMapping("/edge")
public class EdgeController {

    /**
     * 获取每日新闻
     *
     * @return
     */
    @GetMapping("/getNews")
    public String getNews() {
        if (new Random().nextBoolean()) {
            return "本月编程榜排名";
        }
        throw new RuntimeException();
    }

}
