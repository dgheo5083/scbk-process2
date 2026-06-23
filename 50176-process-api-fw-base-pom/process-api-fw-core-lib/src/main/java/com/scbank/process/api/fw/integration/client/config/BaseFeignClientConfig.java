package com.scbank.process.api.fw.integration.client.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.scbank.process.api.fw.integration.client.filter.FeignFilter;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.interceptors.FilteredRequestInterceptor;
import com.scbank.process.api.fw.integration.client.invocation.FeignClientInvocationHandlerFactory;

import feign.InvocationHandlerFactory;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * FeignClient Base Configuration
 * 
 * @author sungdon.choi
 */
@Slf4j
public abstract class BaseFeignClientConfig {

    /**
     * 
     * @param filters
     * @return
     */
    @Bean
    FeignFilterChain feignFilterChain(List<FeignFilter> filters) {
        return new FeignFilterChain(filters);
    }

    /**
     * 템플릿 단계 훅 처리 인터셉터 빈
     * 
     * @param filterChain
     * @return
     */
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    RequestInterceptor filteredRequestInterceptor(FeignFilterChain filterChain) {
        return new FilteredRequestInterceptor(filterChain);
    }

    @Bean
    InvocationHandlerFactory invocationHandlerFactory() {
        return new FeignClientInvocationHandlerFactory();
    }
}
