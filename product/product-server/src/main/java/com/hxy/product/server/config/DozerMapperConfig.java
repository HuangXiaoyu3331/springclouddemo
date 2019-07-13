package com.hxy.product.server.config;

import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * DozerMapperConfig class
 * 注入DozerBeanMapperFactoryBean
 *
 * @author hxy
 * @date 2019/1/16
 */
@Configuration
public class DozerMapperConfig {

    @Bean
    public DozerBeanMapperFactoryBean dozerMapper(@Value("classpath:mapping/*.xml") Resource[] resources) throws IOException {
        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(resources);
        return dozerBeanMapperFactoryBean;
    }

}
