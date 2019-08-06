package com.hxy.product.server.controller;

import com.hxy.product.server.service.impl.BusinessAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BackendAController
 * @date 2019年08月01日 18:46:49
 */
@RestController
@RequestMapping("/backendA")
public class BackendAController {

    @Autowired
    private BusinessAService businessAService;

    @GetMapping("/failure")
    public String failure(){
        return businessAService.failure();
    }

    @GetMapping("/success")
    public String success(){
        return businessAService.success();
    }

    @GetMapping("/ignore")
    public String ignore(){
        return businessAService.ignore();
    }

    @GetMapping("/recover")
    public String methodWithRecovery(){
        return businessAService.methodWithRecovery().get();
    }

}
