package com.hxy.product.server;

import io.vavr.collection.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: CircuitBreakerTest
 * @date 2019年08月01日 18:57:30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CircuitBreakerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldOpenAndCloseBackendACircuitBreaker() throws InterruptedException {
        // When
        Stream.rangeClosed(1, 5).forEach((count) -> produceFailure(count));

        // When
//        Stream.rangeClosed(1,3).forEach((count) -> produceSuccess());
    }

    @Test
    public void shouldOpenAndCloseBackendBCircuitBreaker() throws InterruptedException {
        // When
        Stream.rangeClosed(1, 10).forEach((count) -> produceFailure(count));

        // When
//        Stream.rangeClosed(1, 3).forEach((count) -> produceSuccess());
    }

    private void produceFailure(int count) {
        log.info("执行第{}个请求------------------------------------------------", count);
        try {
            mvc.perform(MockMvcRequestBuilders.get("/backendA/failure")
                    .characterEncoding("utf-8")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void produceSuccess() {

    }

}
