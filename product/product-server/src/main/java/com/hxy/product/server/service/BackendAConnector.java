package com.hxy.product.server.service;


import com.hxy.common.core.SystemError;
import com.hxy.common.exception.AppException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import rx.Observable;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BackendAConnector
 * @date 2019年08月01日 18:52:53
 */
@CircuitBreaker(name = "backendA",fallbackMethod = "fallbackAll")
@Component(value = "backendAConnector")
public class BackendAConnector {

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public String failure() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    public String ignoreException() {
        throw new AppException(SystemError.SYSTEM_ERROR);
    }

    public String success() {
        return "Hello World from backend A";
    }

    public Observable<String> methodWhichReturnsAStream() {
        return Observable.never();
    }

    private String fallback(Exception e) {
        return "fallback";
    }

    private String fallbackAll(Exception e) {
        return "fallbackAll";
    }

}
