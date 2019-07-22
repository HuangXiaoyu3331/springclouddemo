package com.hxy.product.server.config;

import com.hxy.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: RedisClusterUtilConfig
 * @date 2019年07月22日 14:56:54
 */
@Slf4j
@Configuration
public class RedisConfig {

    // 连接超时时间
    @Value("${spring.redis.timeout:3000}")
    private int timeout;

    // 返回值的超时时间
    @Value("${spring.redis.so-timeout:3000}")
    private int soTimeout;

    // 节点信息
    @Value("#{'${spring.redis.cluster.nodes}'.split(',')}")
    private List<String> clusterNodes;

    // 最大重定向次数
    @Value("${spring.redis.cluster.max-redirects:3}")
    private Integer maxRedirects;

    // 最大连接数
    @Value("${spring.redis.jedis.pool.max-active:8}")
    private int maxActive;

    // 最大空闲连接数
    @Value("${spring.redis.jedis.pool.max-idle:8}")
    private int maxIdle;

    // 最小空闲连接数
    @Value("${spring.redis.jedis.pool.min-idle:0}")
    private int minIdle;

    // 连接池最大阻塞等待时间（使用负值表示没有限制）
    @Value("${spring.redis.jedis.pool.max-wait:10000}")
    private Long maxWait;

    // 密码
    @Value("${spring.redis.password:}")
    private String password;

    // 数据库
    @Value("${spring.redis.database:0}")
    private int database;

    // 最大重试次数
    @Value("${spring.redis.maxAttempts:3}")
    private int maxAttempts;

    private JedisPoolConfig getJedisPoolConfig() {
        log.info("初始化 JedisPoolConfig，maxActive:{},maxIdle:{},minIdle:{},maxWait:{}", maxActive, maxIdle, minIdle, maxWait);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }

    /**
     * 获取 jedis 连接池实例
     */
    private JedisPool getJedisPool() {
        log.info("获取 jedis pool 实例,address:{},timeout:{},database:{}", clusterNodes.get(0), timeout, database);
        return new JedisPool(getJedisPoolConfig(),
                ":".split(clusterNodes.get(0))[0],
                Integer.valueOf(":".split(clusterNodes.get(0))[1]),
                timeout,
                password,
                database);
    }

    /**
     * 获取 jedis cluster 实例
     */
    private JedisCluster getJedisCluster() {
        Set<HostAndPort> hostAndPortSet = new HashSet<>();
        clusterNodes.forEach(node ->hostAndPortSet.add(new HostAndPort(node.split(":")[0], Integer.valueOf(node.split(":")[1]))));
        log.info("获取 jedis cluster 实例,nodes:{},timeout:{},soTimeout:{},maxAttempts:{}", clusterNodes, timeout, soTimeout, maxAttempts);
        if (StringUtils.isNotBlank(password)) {
            return new JedisCluster(hostAndPortSet, timeout, soTimeout, maxAttempts, password, getJedisPoolConfig());
        }
        return new JedisCluster(hostAndPortSet, timeout, soTimeout, maxAttempts, getJedisPoolConfig());
    }

    /**
     * 注入 RedisUtil
     */
    @Bean
    public RedisUtil redisUtil() {
        RedisUtil redisUtil = new RedisUtil();
        if (clusterNodes.size() == 1) {
            log.info("注入redis单机工具类");
            redisUtil.setJedisPool(getJedisPool());
        } else {
            log.info("注入redis集群工具类");
            redisUtil.setJedisCluster(getJedisCluster());
        }
        return redisUtil;
    }

}
