package com.hxy.common.utils;

import com.hxy.common.core.JedisCommandsOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
     * 查询key的过期时间
     *
     * @param key redis key
     * @return 过期时间，永久返回-1
     */
    public Long ttl(String key) {
        return excute(commands -> commands.ttl(key));
    }

    /**
     * 获取所有符合匹配的key，生产环境应该禁用keys *
     *
     * @param pattern 表达式
     * @return 所有匹配的key的集合
     */
    public Set<String> keys(String pattern) {
        return excute(commands -> {
            Set<String> keys = new HashSet<>();
            if (commands instanceof Jedis) {
                return ((Jedis) commands).keys(pattern);
            } else if (commands instanceof JedisCluster) {
                Map<String, JedisPool> clusterNodes = ((JedisCluster) commands).getClusterNodes();
                clusterNodes.values().forEach(pool -> {
                    try (Jedis jedis = pool.getResource()) {
                        keys.addAll(jedis.keys(pattern));
                    } catch (Exception e) {
                        log.error("执行redis操作异常");
                    }
                });
                return keys;
            }
            return null;
        });
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

    /**
     * 更新key的值，并返回旧值
     *
     * @param key   redis key
     * @param value 新值
     * @return 旧值，如果key不存在，则返回null
     */
    public String getSet(String key, String value) {
        return excute(commands -> commands.getSet(key, value));
    }

    /**
     * 截取key指定位置的值
     *
     * @param key         redis key
     * @param startOffset 起始位置，从0开始，负数表示从右边开始截取
     * @param endOffset   结束位置
     * @return 返回截取位置的字符串，没有返回null
     */
    public String getrange(String key, int startOffset, int endOffset) {
        return excute(commands -> commands.getrange(key, startOffset, endOffset));
    }

    /**
     * 对指定key的value做+1操作，value必须为int类型，否则报错
     *
     * @param key redis key
     * @return 加值后的结果，当key不存在时，设置并返回value=1
     */
    public Long incr(String key) {
        return excute(commands -> commands.incr(key));
    }

    /**
     * 通过key给指定的value加值，value必须为int类型，否则报错
     *
     * @param key       redis key
     * @param increment 添加的值
     * @return 加值后的value，如果key不存在，设置并返回value=increment
     */
    public Long incrBy(String key, Long increment) {
        return excute(commands -> commands.incrBy(key, increment));
    }

    /**
     * 对指定的key的value做-1操作，value必须为int类型，否则报错
     *
     * @param key redis key
     * @return 减值后的结果，如果key不存在，设置并返回value=-1
     */
    public Long decr(String key) {
        return excute(commands -> commands.decr(key));
    }

    /**
     * 通过key给指定的value减值，value必须为int类型，否则报错
     *
     * @param key       redis key
     * @param decrement 减少的值
     * @return 减值后的结果，如果key不存在，设置并返回value=-decrement
     */
    public Long decrBy(String key, Long decrement) {
        return excute(commands -> commands.decrBy(key, decrement));
    }

    /**
     * 通过key获取value的长度
     *
     * @param key redis key
     * @return 返回value的长度，key不存在返回0
     */
    public Long strlen(String key) {
        return excute(commands -> commands.strlen(key));
    }

    /**
     * 将哈希表 key 中的字段 field 的值设为 value
     *
     * @param key   redis key
     * @param field hash表中的key
     * @param value 要设置的value
     * @return 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hset(String key, String field, String value) {
        return excute(commands -> commands.hset(key, field, value));
    }

    /**
     * 将哈希表key中的字段field的值设为value，如果不存在则设置，存在则不设置
     *
     * @param key   redis key
     * @param field hash表中的key
     * @param value 要设置的value
     * @return 成功返回1，如果field已存在，返回0
     */
    public Long hsetnx(String key, String field, String value) {
        return excute(commands -> commands.hsetnx(key, field, value));
    }

    /**
     * 获取哈希表key中的字段field的值
     *
     * @param key   redis key
     * @param field hash表中的key
     * @return 返回查询到的值，不存在返回null
     */
    public String hget(String key, String field) {
        return excute(commands -> commands.hget(key, field));
    }

    /**
     * 给哈希表中的key的field字段的值加上给定的value
     *
     * @param key   redis key
     * @param field hash表中的key
     * @param value 增加的值
     * @return 返回增加后的值，不存在设置并返回当前value
     */
    public Long hincrBy(String key, String field, long value) {
        return excute(commands -> commands.hincrBy(key, field, value));
    }

    /**
     * 判断key指定的field是否有value
     *
     * @param key   redis key
     * @param field hash表中的key
     * @return 存在返回true，失败返回false
     */
    public Boolean hexists(String key, String field) {
        return excute(commands -> commands.hexists(key, field));
    }

    /**
     * 通过key返回field的数量
     *
     * @param key redis key
     * @return 指定key里面field的数量
     */
    public Long hlen(String key) {
        return excute(commands -> commands.hlen(key));
    }

    /**
     * 删除指定key的field
     *
     * @param key   redis key
     * @param field hash表的key
     * @return 删除的field数量
     */
    public Long hdel(String key, String... field) {
        return excute(commands -> commands.hdel(key, field));
    }

    /**
     * 查询key的所有field
     *
     * @param key redis key
     * @return 所有field
     */
    public Set<String> hkeys(String key) {
        return excute(commands -> commands.hkeys(key));
    }

    /**
     * 返回该key下的哈希表的所有value
     *
     * @param key redis key
     * @return 所有value
     */
    public List<String> hvals(String key) {
        return excute(commands -> commands.hvals(key));
    }

    /**
     * 查询该key下的所有field-value
     *
     * @param key redis key
     * @return 该hash表的所有field-value
     */
    public Map<String, String> hgetAll(String key) {
        return excute(commands -> commands.hgetAll(key));
    }

    /**
     * 向key存放的list头部添加字符串
     *
     * @param key    redis key
     * @param string 可以是String字符串，也可以是字符串数组
     * @return 添加后的list的value个数
     */
    public Long lpush(String key, String... string) {
        return excute(commands -> commands.lpush(key, string));
    }

    /**
     * 向key存放的list尾部添加字符串
     *
     * @param key    redis key
     * @param string 可以是String字符串，也可以是字符串数组
     * @return 添加后的list的value个数
     */
    public Long rpush(String key, String... string) {
        return excute(commands -> commands.rpush(key, string));
    }

    /**
     * 给List的index位置的元素设置一个新值，如果下表超过list长度，则报错
     *
     * @param key   redis key
     * @param index 下表
     * @param value 新值
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        return excute(commands -> commands.lset(key, index, value));
    }

    /**
     * 删除key下的list，count个与value值相同的元素
     *
     * @param key   redis key
     * @param count 删除的个数
     * @param value 值
     * @return 成功删除的个数
     */
    public Long lrem(String key, Long count, String value) {
        return excute(commands -> commands.lrem(key, count, value));
    }

    /**
     * 对List进行修剪，保留start到end位置的元素，其他元素全部删除，负数表示从最后一个开始
     *
     * @param key   redis key
     * @param start 开始下标
     * @param end   结束下标
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        return excute(commands -> commands.ltrim(key, start, end));
    }

    /**
     * 从list头部删除一个元素并返回
     *
     * @param key redis key
     * @return 删除的元素
     */
    public String lpop(String key) {
        return excute(commands -> commands.lpop(key));
    }

    /**
     * 从list尾部删除一个元素并返回
     *
     * @param key redis key
     * @return 删除的元素
     */
    public String rpop(String key) {
        return excute(commands -> commands.rpop(key));
    }

    /**
     * 获取list指定index的值
     *
     * @param key   redis key
     * @param index 下标
     * @return 获取到的值
     */
    public String lindex(String key, long index) {
        return excute(commands -> commands.lindex(key, index));
    }

    /**
     * 返回key存放的list的长度
     *
     * @param key redis key
     * @return list长度
     */
    public Long llen(String key) {
        return excute(commands -> commands.llen(key));
    }

    /**
     * 通过key获取list指定下标位置的value
     *
     * @param key   redis key
     * @param start 开始位置，负值表示从最后开始
     * @param end   结束位置
     * @return 获取到的value集合
     */
    public List<String> lrange(String key, long start, long end) {
        return excute(commands -> commands.lrange(key, start, end));
    }

    /**
     * 向set中添加value
     *
     * @param key    redis key
     * @param member 可以是String，也可以是String数组
     * @return 添加的值的数量
     */
    public Long sadd(String key, String... member) {
        return excute(commands -> commands.sadd(key, member));
    }

    /**
     * 向set中删除指定value
     *
     * @param key    redis value
     * @param member 可以是String，也可以是String数组
     * @return 删除的值的数量
     */
    public Long srem(String key, String... member) {
        return excute(commands -> commands.srem(key, member));
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key redis key
     * @return 删除的值
     */
    public String spop(String key) {
        return excute(commands -> commands.spop(key));
    }

    /**
     * 获取set中value的个数
     *
     * @param key redis key
     * @return value的个数
     */
    public Long scard(String key) {
        return excute(commands -> commands.scard(key));
    }

    /**
     * 判断值是否是set中的元素
     *
     * @param key    redis key
     * @param member 值
     * @return 是返回true，否则返回false
     */
    public Boolean sismember(String key, String member) {
        return excute(commands -> commands.sismember(key, member));
    }

    /**
     * 返回set中的随机value，不删除改value
     *
     * @param key redis key
     * @return set中随机元素的值
     */
    public String srandmember(String key) {
        return excute(commands -> commands.srandmember(key));
    }

    /**
     * 获取指定set中的所有value
     *
     * @param key redis key
     * @return set集合
     */
    public Set<String> smembers(String key) {
        return excute(commands -> commands.smembers(key));
    }

    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的，
     * 如果该value已经存在则根据score更新元素
     *
     * @param key          redis key
     * @param scoreMembers value-score
     * @return 增加的条数
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return excute(commands -> commands.zadd(key, scoreMembers));
    }

    /**
     * 向有序集合中添加元素
     *
     * @param key    redis key
     * @param score  分数，用于排序
     * @param member 元素的值
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
     */
    public Long zadd(String key, double score, String member) {
        return excute(commands -> commands.zadd(key, score, member));
    }

    /**
     * 删除集合中的元素
     *
     * @param key     redis key
     * @param members 可以是String，也可以是String数组
     * @return 被成功移除的成员的数量，不包括被忽略的成员
     */
    public Long zrem(String key, String... members) {
        return excute(commands -> commands.zrem(key, members));
    }

    /**
     * 增加有序集合中的分数值
     *
     * @param key       redis key
     * @param increment 要增加的分数
     * @param member    成员
     * @return member 成员的新分数值
     */
    public Double zincrby(String key, double increment, String member) {
        return excute(commands -> commands.zincrby(key, increment, member));
    }

    /**
     * 返回zset中value的排名
     *
     * @param key    redis key
     * @param member 成员
     * @return 排名
     */
    public Long zrank(String key, String member) {
        return excute(commands -> commands.zrank(key, member));
    }

    /**
     * Redis Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序
     *
     * @param key    redis key
     * @param member 成员
     * @return 排名
     */
    public Long zrevrank(String key, String member) {
        return excute(commands -> commands.zrank(key, member));
    }

    /**
     * 通过key将获取score从start到end中zset的value
     *
     * @param key   redis key
     * @param start 开始坐标
     * @param end   结束坐标
     * @return 获取到的元素集合
     */
    public Set<String> zrevrange(String key, long start, long end) {
        return excute(commands -> commands.zrevrange(key, start, end));
    }

    /**
     * 获取score为min~max范围内的元素
     *
     * @param key redis key
     * @param max
     * @param min 最低分数
     * @return 获取到的元素集合
     */
    public Set<String> zrangeByScore(String key, String max, String min) {
        return excute(commands -> commands.zrangeByScore(key, max, min));
    }

    /**
     * 获取score为min~max范围内的元素
     *
     * @param key redis key
     * @param min 最低分数
     * @param max 最大分数
     * @return 获取到的元素数量
     */
    public Long zcount(String key, String min, String max) {
        return excute(commands -> commands.zcount(key, min, max));
    }

    /**
     * 通过key返回zset中的元素个数
     *
     * @param key redis key
     * @return zset中的元素个数
     */
    public Long zcard(String key) {
        return excute(commands -> commands.zcard(key));
    }

    /**
     * 获取zset中的元素的分数
     *
     * @param key    redis key
     * @param member 成员
     * @return 分数
     */
    public Double zscore(String key, String member) {
        return excute(commands -> commands.zscore(key, member));
    }

    /**
     * 删除start~end范围内的元素
     *
     * @param key   redis key
     * @param start 开始位置
     * @param end   结束位置
     * @return 删除个数
     */
    public Long zremrangeByRank(String key, long start, long end) {
        return excute(commands -> commands.zremrangeByRank(key, start, end));
    }

    /**
     * 删除min~max分数范围内的元素
     *
     * @param key redis key
     * @param min 最低分数
     * @param max 最高分数
     * @return 删除的个数
     */
    public Long zremrangeByScore(String key, double min, double max) {
        return excute(commands -> commands.zremrangeByScore(key, min, max));
    }

    /**
     * 获取key的value的类型
     *
     * @param key redis key
     * @return value的类型
     */
    public String type(String key) {
        return excute(commands -> commands.type(key));
    }
}
