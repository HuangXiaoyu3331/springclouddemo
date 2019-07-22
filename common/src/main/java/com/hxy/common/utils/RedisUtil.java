package com.hxy.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * redis 工具类，支持集群跟单机模式
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: RedisUtil
 * @date 2019年07月16日 12:01:16
 */
@Slf4j
@Data
public class RedisUtil {

    private JedisPool jedisPool;
    private JedisCluster jedisCluster;

    public String get(String key) {
        if (jedisCluster != null) {
            return jedisCluster.get(key);
        }
        return jedisPool.getResource().get(key);
    }

}
