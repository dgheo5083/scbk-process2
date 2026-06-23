package com.scbank.process.api.fw.session.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.scbank.process.api.fw.core.runtime.conditional.ConditionalOnRunMode;
import com.scbank.process.api.fw.session.ISessionContextManager;

@Configuration
@ConditionalOnProperty(prefix = "mock.session", name = "enabled", havingValue = "true")
@ConditionalOnRunMode(value = { "local" })
public class MockSessionConfiguration {

    /**
     * 로컬환경 Mock 세션 처리 인터셉터
     * 
     * @param mockSessionProperties Mock 세션 프로퍼티 빈
     * @param sessionContextManager 세션 컨텍스트 매니저 빈
     * @return
     */
    @Bean
    @Order(0)
    MockSessionInjectInterceptor mockSessionInjectInterceptor(MockSessionProperties mockSessionProperties,
            ISessionContextManager sessionContextManager) {
        return new MockSessionInjectInterceptor(mockSessionProperties, sessionContextManager);
    }
}
