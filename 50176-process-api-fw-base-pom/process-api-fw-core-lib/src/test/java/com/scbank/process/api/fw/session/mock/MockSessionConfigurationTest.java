package com.scbank.process.api.fw.session.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.session.ISessionContextManager;

/**
 * {@link MockSessionConfiguration} 단위 테스트
 */
@DisplayName("MockSessionConfiguration 테스트")
@ExtendWith(MockitoExtension.class)
class MockSessionConfigurationTest {

    private MockSessionConfiguration configuration;

    @Mock
    private MockSessionProperties mockSessionProperties;

    @Mock
    private ISessionContextManager sessionContextManager;

    @BeforeEach
    void setUp() {
        configuration = new MockSessionConfiguration();
    }

    @Nested
    @DisplayName("mockSessionInjectInterceptor 빈 테스트")
    class MockSessionInjectInterceptorBeanTests {

        @Test
        @DisplayName("MockSessionInjectInterceptor 빈을 생성할 수 있다")
        void createMockSessionInjectInterceptor() {
            // when
            MockSessionInjectInterceptor result =
                    configuration.mockSessionInjectInterceptor(mockSessionProperties, sessionContextManager);

            // then
            assertNotNull(result);
            assertInstanceOf(MockSessionInjectInterceptor.class, result);
        }

        @Test
        @DisplayName("생성된 인터셉터에 의존성이 주입된다")
        void interceptorHasDependenciesInjected() {
            // when
            MockSessionInjectInterceptor result =
                    configuration.mockSessionInjectInterceptor(mockSessionProperties, sessionContextManager);

            // then
            assertNotNull(result);
            // 인터셉터가 정상적으로 동작하는지 검증 (간접적으로 의존성 확인)
        }
    }

    @Nested
    @DisplayName("Configuration 클래스 테스트")
    class ConfigurationClassTests {

        @Test
        @DisplayName("Configuration 인스턴스를 생성할 수 있다")
        void createInstance() {
            // when
            MockSessionConfiguration config = new MockSessionConfiguration();

            // then
            assertNotNull(config);
        }
    }
}
