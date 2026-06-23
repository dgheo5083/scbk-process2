package com.scbank.process.api.fw.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.scbank.process.api.fw.base.channel.filters.AuthFilter;
import com.scbank.process.api.fw.base.channel.filters.ContextFilter;
import com.scbank.process.api.fw.base.properties.AuthFilterProperty;

@Configuration
public class AuthConfig {

    /**
     * 프로세스API 요청 인증 필터
     * 
     * @param property                인증필터 프로퍼티
     * @return
     */
    @Bean
    @Order(-99)
    @ConditionalOnProperty(name = { "csl.filter.auth.enabled" }, matchIfMissing = true, havingValue = "true")
    FilterRegistrationBean<AuthFilter> authFilter(AuthFilterProperty property) {
        FilterRegistrationBean<AuthFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new AuthFilter(property));
        filterBean.setName("authFilter");
        filterBean.addUrlPatterns("/*");

        return filterBean;
    }

    /**
     * 프로세스API 요청 컨텍스트 필터
     * 
     * @param serviceEndpointRequests 프로세스API 서비스 엔드포인트 경로
     * @return
     */
    @Bean
    @Order(-98)
    @ConditionalOnProperty(name = { "csl.filter.context.enabled" }, matchIfMissing = true, havingValue = "true")
    FilterRegistrationBean<ContextFilter> contextFilter() {
        FilterRegistrationBean<ContextFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new ContextFilter());
        filterBean.setName("contextFilter");
        filterBean.addUrlPatterns("/*");

        return filterBean;
    }
}
