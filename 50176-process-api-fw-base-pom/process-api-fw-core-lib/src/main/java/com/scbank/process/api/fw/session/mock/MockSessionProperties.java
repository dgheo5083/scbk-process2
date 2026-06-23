package com.scbank.process.api.fw.session.mock;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

/**
 * Mock 세션 프로퍼티
 * 기능 활성화 여부
 * 로그인/글로벌 세션 키/값을 관리한다.
 */
@Getter
@Setter
@Configuration
@PropertySource(value = "classpath:mock-session.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "mock.session")
public class MockSessionProperties {

    private boolean enabled;

    private LoginConfig login = new LoginConfig();

    private Map<String, Object> loginSession;

    private Map<String, Object> globalSession;

    @Getter
    @Setter
    public static class LoginConfig {
        private boolean enabled;
        private String userId;
    }
}
