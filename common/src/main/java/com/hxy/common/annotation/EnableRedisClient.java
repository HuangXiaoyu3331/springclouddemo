package com.hxy.common.annotation;

import com.hxy.common.config.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EnableRedisClient
 * @date 2019年08月22日 17:13:04
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {RedisConfig.class})
public @interface EnableRedisClient {
}
