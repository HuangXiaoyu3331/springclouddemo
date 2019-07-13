package com.hxy.product.server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: TestAspect
 * @date 2019年07月04日 14:33:24
 */
@Aspect
@Component
@Slf4j
public class TestAspect {

    @Pointcut("execution(public * com.hxy.product.server.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void before() {
        log.info("进入切面");
    }

}
