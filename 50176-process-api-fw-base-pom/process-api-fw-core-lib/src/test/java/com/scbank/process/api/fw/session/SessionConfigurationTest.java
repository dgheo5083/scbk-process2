package com.scbank.process.api.fw.session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.session.impl.DefaultSessionContextManager;
import com.scbank.process.api.fw.session.impl.DefaultSessionKeyValidator;

import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionListener;

/**
 * {@link SessionConfiguration} 단위 테스트
 */
@DisplayName("SessionConfiguration 테스트")
@ExtendWith(MockitoExtension.class)
class SessionConfigurationTest {

    private SessionConfiguration configuration;

    @Mock
    private SessionProperties sessionProperties;

    @BeforeEach
    void setUp() {
        configuration = new SessionConfiguration();
    }

    @Nested
    @DisplayName("sessionKeyValidator 빈 테스트")
    class SessionKeyValidatorBeanTests {

        @Test
        @DisplayName("SessionKeyValidator 빈을 생성할 수 있다")
        void createSessionKeyValidator() {
            // given
            when(sessionProperties.isEnabled()).thenReturn(true);
            Map<SessionScope, List<String>> allowedKeys = new HashMap<>();
            allowedKeys.put(SessionScope.GLOBAL, Arrays.asList("traceId"));
            allowedKeys.put(SessionScope.LOGIN, Arrays.asList("userId"));
            when(sessionProperties.getAllowedKeys()).thenReturn(allowedKeys);

            // when
            ISessionKeyValidator result = configuration.sessionKeyValidator(sessionProperties);

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("SessionKeyValidator 빈이 DefaultSessionKeyValidator 타입이다")
        void sessionKeyValidatorIsDefaultType() {
            // given
            when(sessionProperties.isEnabled()).thenReturn(false);
            when(sessionProperties.getAllowedKeys()).thenReturn(new HashMap<>());

            // when
            ISessionKeyValidator result = configuration.sessionKeyValidator(sessionProperties);

            // then
            assertInstanceOf(DefaultSessionKeyValidator.class, result);
        }
    }

    @Nested
    @DisplayName("sessionManager 빈 테스트")
    class SessionManagerBeanTests {

        @Test
        @DisplayName("SessionManager 빈을 생성할 수 있다")
        void createSessionManager() {
            // when
            ISessionContextManager result = configuration.sessionManager();

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("SessionManager 빈이 DefaultSessionContextManager 타입이다")
        void sessionManagerIsDefaultType() {
            // when
            ISessionContextManager result = configuration.sessionManager();

            // then
            assertInstanceOf(DefaultSessionContextManager.class, result);
        }
    }

    @Nested
    @DisplayName("httpSessionListener 빈 테스트")
    class HttpSessionListenerBeanTests {

        @Test
        @DisplayName("HttpSessionListener 빈을 생성할 수 있다")
        void createHttpSessionListener() {
            // when
            HttpSessionListener result = configuration.httpSessionListener();

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("HttpSessionListener 인터페이스를 구현한다")
        void implementsHttpSessionListener() {
            // when
            HttpSessionListener result = configuration.httpSessionListener();

            // then
            assertInstanceOf(HttpSessionListener.class, result);
        }
    }

    @Nested
    @DisplayName("httpSessionAttributeListener 빈 테스트")
    class HttpSessionAttributeListenerBeanTests {

        @Test
        @DisplayName("HttpSessionAttributeListener 빈을 생성할 수 있다")
        void createHttpSessionAttributeListener() {
            // when
            HttpSessionAttributeListener result = configuration.httpSessionAttributeListener();

            // then
            assertNotNull(result);
        }

        @Test
        @DisplayName("HttpSessionAttributeListener 인터페이스를 구현한다")
        void implementsHttpSessionAttributeListener() {
            // when
            HttpSessionAttributeListener result = configuration.httpSessionAttributeListener();

            // then
            assertInstanceOf(HttpSessionAttributeListener.class, result);
        }
    }

    @Nested
    @DisplayName("Configuration 클래스 테스트")
    class ConfigurationClassTests {

        @Test
        @DisplayName("Configuration 인스턴스를 생성할 수 있다")
        void createInstance() {
            // when
            SessionConfiguration config = new SessionConfiguration();

            // then
            assertNotNull(config);
        }
    }
}
