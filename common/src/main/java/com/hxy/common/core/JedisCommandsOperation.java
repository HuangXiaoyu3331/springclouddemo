package com.hxy.common.core;

import redis.clients.jedis.JedisCommands;

/**
 * redis 操作接口定义
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: JedisCommandsOperation
 * @date 2019年07月23日 12:47:08
 */
public interface JedisCommandsOperation<T> {

    /**
     * jedis 操作
     *
     * @param commands jedis操作命令
     * @return
     */
    T doOperation(JedisCommands commands);

}
