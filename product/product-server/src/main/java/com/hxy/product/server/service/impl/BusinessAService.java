package com.hxy.product.server.service.impl;

import com.hxy.product.server.service.BackendAConnector;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BusinessAService
 * @date 2019年08月01日 18:48:35
 */
@Service("businessAService")
public class BusinessAService {

    @Autowired
    private BackendAConnector backendAConnector;

    public String failure() {
        return backendAConnector.failure();
    }
    public String success() {
        return backendAConnector.success();
    }

    public String ignore() {
        return backendAConnector.ignoreException();
    }

    public Try<String> methodWithRecovery() {
        return Try.of(backendAConnector::failure)
                .recover((throwable) -> recovery());
    }

    private String recovery() {
        return "Hello world from recovery";
    }

}
