package com.hxy.product.server;

import com.hxy.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void set() {
        String statusA = redisUtil.set("a", "a");
        assertThat(statusA, is(equalTo("OK")));
        String statusB = redisUtil.set("b", "b");
        assertThat(statusB, is(equalTo("OK")));
    }

    @Test
    public void get() {
        String value = redisUtil.get("a");
        assertThat(value, is(equalTo("a")));
    }

    @Test
    public void expire() {
        Long status = redisUtil.expire("a", 20);
        assertThat(status, is(equalTo(1L)));
    }

    @Test
    public void del() {
        Long delCount = redisUtil.del("a");
        assertThat(delCount, is(equalTo(1L)));
    }

    @Test
    public void append() {
        Long length = redisUtil.append("b", "b");
        assertThat(length, is(equalTo(2L)));
    }

    @Test
    public void mget() {
        List<String> list = redisUtil.mget("a", "b");
        log.info(list.toString());
    }
}
