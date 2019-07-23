package com.hxy.common.utils;

import com.hxy.common.core.JedisCommandsOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.util.JedisClusterCRC16;

import java.util.List;


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

    /**
     * 执行 redis 操作
     */
    private <T> T excute(JedisCommandsOperation<T> operation) {
        Jedis jedis = null;
        try {
            if (jedisCluster == null) {
                jedis = getJedis();
                if (jedis != null) {
                    return operation.doOperation(jedis);
                }
                return null;
            }
            return operation.doOperation(jedisCluster);
        } catch (Exception e) {
            log.error("执行redis操作异常", e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }



    /**
     * 获取 Jedis 实例
     */
    private Jedis getJedis() {
        if (jedisPool != null) {
            try {
                return jedisPool.getResource();
            } catch (Exception e) {
                log.error("获取 Jedis 实例异常", e);
            }
        }
        return null;
    }

    /**
     * 获取指定key的value
     *
     * @param key redis key
     * @return redis value
     */
    public String get(String key) {
        return excute(commands -> commands.get(key));
    }

    /**
     * 向redis中存入key和value，如果key已经存在，则覆盖
     *
     * @param key   redis key
     * @param value redis value
     * @return 成功返回OK，失败返回0
     */
    public String set(String key, String value) {
        return excute(commands -> commands.set(key, value));
    }

    /**
     * 设置key的过期时间，单位:秒
     *
     * @param key     redis key
     * @param seconds 过期时间
     * @return 成功返回1，失败返回0(redis 2.1.3及以前版本,当键的过期时间已经设置了，再次设置会返回失败，2.1.3以后的版本则会覆盖过期时间)
     */
    public Long expire(String key, int seconds) {
        return excute(commands -> commands.expire(key, seconds));
    }

    /**
     * 删除指定key
     *
     * @param key redis key
     * @return 成功删除的key的个数
     */
    public Long del(String key) {
        return excute(commands -> commands.del(key));
    }

    /**
     * 向key指定的value后面追加字符串
     *
     * @param key       redis key
     * @param appendStr 追加的字符串
     * @return 成功返回添加后的value的长度，失败返回添加的appendStr的长度，异常返回OL
     */
    public Long append(String key, String appendStr) {
        return excute(commands -> commands.append(key, appendStr));
    }

    /**
     * 判断key是否存在
     *
     * @param key redis key
     * @return 存在返回true，不存在返回false
     */
    public Boolean exists(String key) {
        return excute(commands -> commands.exists(key));
    }

    /**
     * 向redis中存入key和value，当key存在则不存入
     *
     * @param key   redis key
     * @param value redis value
     * @return 成功返回1，如果存在/异常返回0
     */
    public Long setnx(String key, String value) {
        return excute(commands -> commands.setnx(key, value));
    }

    /**
     * 向redis中存入key和value，并设置key的过期时间
     *
     * @param key    redis key
     * @param value  redis value
     * @param second 过期时间，单位:秒
     * @return 成功返回OK，失败/异常返回null
     */
    public String setex(String key, String value, int second) {
        return excute(commands -> commands.setex(key, second, value));
    }

    /**
     * 覆盖key指定的value值，从偏移量offset开始
     *
     * @param key    redis key
     * @param value  redis value
     * @param offset 偏移量
     * @return 成功返回替换后的value长度
     */
    public Long setrange(String key, String value, int offset) {
        return excute(commands -> commands.setrange(key, offset, value));
    }

//    public List<String> mget(String... keys) {
//        return excute(commands -> {
//            if (commands instanceof Jedis) {
//                return ((Jedis) commands).mget(keys);
//            } else if (commands instanceof JedisCluster) {
//                return ((JedisCluster) commands).mget(keys);
//            }
//            return null;
//        });
//    }

}
