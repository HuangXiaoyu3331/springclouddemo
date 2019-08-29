package com.hxy.common.annotation;

import com.hxy.common.handler.DefaultExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EnableExceptionHandler
 * @date 2019年08月22日 17:07:54
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {DefaultExceptionHandler.class})
public @interface EnableExceptionHandler {
}
