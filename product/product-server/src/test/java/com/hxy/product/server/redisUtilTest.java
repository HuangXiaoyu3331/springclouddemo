//package com.hxy.product.server;
//
//import com.hxy.common.utils.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.assertThat;
//
///**
// * @author 黄晓宇
// * @version v1.0
// * @ClassName: redisUtilTest
// * @date 2019年07月22日 11:29:53
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class redisUtilTest {
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Before
//    public void set() {
//        String statusA = redisUtil.set("a", "a");
//        assertThat(statusA, is(equalTo("OK")));
//        String statusB = redisUtil.set("b", "b");
//        assertThat(statusB, is(equalTo("OK")));
//    }
//
//    @Test
//    public void get() {
//        String value = redisUtil.get("a");
//        assertThat(value, is(equalTo("a")));
//    }
//
//    @Test
//    public void expire() {
//        Long status = redisUtil.expire("a", 20);
//        assertThat(status, is(equalTo(1L)));
//    }
//
//    @Test
//    public void del() {
//        Long delCount = redisUtil.del("a");
//        assertThat(delCount, is(equalTo(1L)));
//    }
//
//    @Test
//    public void ttl() {
//        Long expirationTime = redisUtil.ttl("a");
//        assertThat(expirationTime, is(greaterThan(0L)));
//        expirationTime = redisUtil.ttl("b");
//        assertThat(expirationTime, is(equalTo(-1L)));
//    }
//
//    @Test
//    public void test() {
//        Set<String> keys = redisUtil.keys("*");
//        assertThat(keys.size(), is(greaterThan(0)));
//    }
//
//    @Test
//    public void append() {
//        Long length = redisUtil.append("b", "b");
//        assertThat(length, is(equalTo(2L)));
//    }
//
//    @Test
//    public void exists() {
//        Boolean flag = redisUtil.exists("a");
//        assertThat(flag, is(equalTo(true)));
//    }
//
//    @Test
//    public void setnx() {
//        Long count = redisUtil.setnx("c", "c");
//        assertThat(count, is(equalTo(1L)));
//        count = redisUtil.setnx("c", "c");
//        assertThat(count, is(equalTo(0L)));
//    }
//}
