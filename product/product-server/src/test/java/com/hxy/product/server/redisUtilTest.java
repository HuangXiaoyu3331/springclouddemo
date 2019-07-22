package com.hxy.product.server;

import com.hxy.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: redisUtilTest
 * @date 2019年07月22日 11:29:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class redisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void set() {
        log.info(redisUtil.get("a"));
    }

}
