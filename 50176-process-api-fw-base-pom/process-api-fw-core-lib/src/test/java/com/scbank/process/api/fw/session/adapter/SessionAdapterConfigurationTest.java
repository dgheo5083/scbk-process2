package com.scbank.process.api.fw.session.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link SessionAdapterConfiguration} 단위 테스트
 */
@DisplayName("SessionAdapterConfiguration 테스트")
@ExtendWith(MockitoExtension.class)
class SessionAdapterConfigurationTest {

    private SessionAdapterConfiguration configuration;

    @Mock
    private ISessionAdapterProvider sessionAdapterProvider;

    @Mock
    private ISessionMetadataRegistry sessionMetadataRegistry;

    @Mock
    private ISessionAdapter mockAdapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        configuration = new SessionAdapterConfiguration();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("sessionContextFilter 빈 테스트")
    class SessionContextFilterBeanTests {

        @Test
        @DisplayName("SessionContextAdapterFilter 빈을 생성할 수 있다")
        void createSessionContextFilter() {
            // when
            FilterRegistrationBean<SessionContextAdapterFilter> result =
                    configuration.sessionContextFilter(sessionAdapterProvider);

            // then
            assertNotNull(result);
            assertNotNull(result.getFilter());
        }

        @Test
        @DisplayName("필터 순서가 1이다")
        void filterOrderIsOne() {
            // when
            FilterRegistrationBean<SessionContextAdapterFilter> result =
                    configuration.sessionContextFilter(sessionAdapterProvider);

            // then
            assertEquals(1, result.getOrder());
        }

        @Test
        @DisplayName("필터 이름이 sessionContextAdapterFilter이다")
        void filterNameIsCorrect() {
            // when
            FilterRegistrationBean<SessionContextAdapterFilter> result =
                    configuration.sessionContextFilter(sessionAdapterProvider);

            // then
            assertEquals("sessionContextAdapterFilter", result.getFilterName());
        }

        @Test
        @DisplayName("필터가 모든 URL 패턴에 적용된다")
        void filterAppliesToAllUrls() {
            // when
            FilterRegistrationBean<SessionContextAdapterFilter> result =
                    configuration.sessionContextFilter(sessionAdapterProvider);

            // then
            assertTrue(result.getUrlPatterns().contains("/*"));
        }
    }

    @Nested
    @DisplayName("genericSessionAdapter 빈 테스트")
    class GenericSessionAdapterBeanTests {

        @Test
        @DisplayName("GenericSessionAdapter 빈을 생성할 수 있다")
        void createGenericSessionAdapter() {
            // when
            ISessionAdapter result = configuration.genericSessionAdapter(sessionMetadataRegistry);

            // then
            assertNotNull(result);
            assertInstanceOf(GenericSessionAdapter.class, result);
        }
    }

    @Nested
    @DisplayName("ma30SessionAdapter 빈 테스트")
    class Ma30SessionAdapterBeanTests {

        @Test
        @DisplayName("SessionContextAdapter 빈을 생성할 수 있다")
        void createMa30SessionAdapter() {
            // when
            ISessionAdapter result = configuration.ma30SessionAdapter(sessionMetadataRegistry, objectMapper);

            // then
            assertNotNull(result);
            assertInstanceOf(SessionContextAdapter.class, result);
        }
    }

    @Nested
    @DisplayName("sessionAdapterProvider 빈 테스트")
    class SessionAdapterProviderBeanTests {

        @Test
        @DisplayName("SessionAdapterProvider 빈을 생성할 수 있다")
        void createSessionAdapterProvider() {
            // given
            List<ISessionAdapter> adapters = Arrays.asList(mockAdapter);

            // when
            ISessionAdapterProvider result = configuration.sessionAdapterProvider(adapters);

            // then
            assertNotNull(result);
            assertInstanceOf(SessionAdapterProvider.class, result);
        }
    }

    @Nested
    @DisplayName("Configuration 클래스 테스트")
    class ConfigurationClassTests {

        @Test
        @DisplayName("Configuration 인스턴스를 생성할 수 있다")
        void createInstance() {
            // when
            SessionAdapterConfiguration config = new SessionAdapterConfiguration();

            // then
            assertNotNull(config);
        }
    }
}
