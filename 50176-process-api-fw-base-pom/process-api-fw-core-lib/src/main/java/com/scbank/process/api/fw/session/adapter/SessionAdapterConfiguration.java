package com.scbank.process.api.fw.session.adapter;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(name = "csl.session.adapter.enabled", havingValue = "true")
public class SessionAdapterConfiguration {

    @Value("${csl.session.adapter.rule-path}")
    private String rulePath;

    @Bean
    FilterRegistrationBean<SessionContextAdapterFilter> sessionContextFilter(
            ISessionAdapterProvider sessionAdapterProvider) {
        FilterRegistrationBean<SessionContextAdapterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SessionContextAdapterFilter(sessionAdapterProvider));
        registration.setOrder(1); // 낮을수록 먼저 실행
        registration.addUrlPatterns("/*"); // 적용할 URL 패턴
        registration.setName("sessionContextAdapterFilter");
        return registration;
    }

    /**
     * 세션 adapter 룰 메타데이터 레지스트리
     * 
     * @return
     * @throws Exception
     */
    @Bean
    ISessionMetadataRegistry sessionMetadataRegistry() throws Exception {
        InputStream is = new ClassPathResource(rulePath).getInputStream();
        return new JsonSessionMetadataRegistry(is);
    }

    @Bean
    ISessionAdapter genericSessionAdapter(ISessionMetadataRegistry registry) {
        return new GenericSessionAdapter(registry);
    }

    @Bean
    ISessionAdapter ma30SessionAdapter(ISessionMetadataRegistry registry, ObjectMapper objectMapper) {
        return new SessionContextAdapter(registry, objectMapper);
    }

    @Bean
    ISessionAdapterProvider sessionAdapterProvider(List<ISessionAdapter> adapters) {
        return new SessionAdapterProvider(adapters);
    }
}
