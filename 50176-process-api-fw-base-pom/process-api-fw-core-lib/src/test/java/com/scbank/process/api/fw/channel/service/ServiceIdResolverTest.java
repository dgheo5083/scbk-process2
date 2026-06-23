package com.scbank.process.api.fw.channel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties.ServiceMappingConfig;

import jakarta.servlet.http.HttpServletRequest;

/**
 * ServiceIdResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceIdResolverTest {

    private ServiceIdResolver serviceIdResolver;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private ServiceProperties serviceProperties;

    @Mock
    private ServiceMappingConfig serviceMappingConfig;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        serviceIdResolver = new ServiceIdResolver(channelProperties);
        ReflectionTestUtils.setField(serviceIdResolver, "contextPath", "");
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create resolver with channel properties")
        void shouldCreateResolverWithChannelProperties() {
            assertNotNull(serviceIdResolver);
        }
    }

    @Nested
    @DisplayName("resolve tests")
    class ResolveTests {

        @Test
        @DisplayName("Should resolve ServiceId for matching URI")
        void shouldResolveServiceIdForMatchingUri() {
            when(request.getRequestURI()).thenReturn("/api/common/login/userLogin");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceProperties.serviceId()).thenReturn("COMMON");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**", "/logout/**"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("COMMON", result.getService());
            assertEquals("/api/common/login/userLogin", result.getUrl());
        }

        @Test
        @DisplayName("Should throw when no service matches")
        void shouldThrowWhenNoServiceMatches() {
            when(request.getRequestURI()).thenReturn("/api/unknown/endpoint");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                serviceIdResolver.resolve(request);
            });

            assertTrue(exception.getMessage().contains("서비스 ID를 URI로부터 매핑할 수 없습니다"));
        }

        @Test
        @DisplayName("Should skip disabled services")
        void shouldSkipDisabledServices() {
            when(request.getRequestURI()).thenReturn("/api/common/login");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(false);

            assertThrows(IllegalStateException.class, () -> {
                serviceIdResolver.resolve(request);
            });
        }

        @Test
        @DisplayName("Should skip services without service mapping")
        void shouldSkipServicesWithoutServiceMapping() {
            when(request.getRequestURI()).thenReturn("/api/test");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> {
                serviceIdResolver.resolve(request);
            });
        }

        @Test
        @DisplayName("Should handle context path in URI")
        void shouldHandleContextPathInUri() {
            ReflectionTestUtils.setField(serviceIdResolver, "contextPath", "/app");
            when(request.getRequestURI()).thenReturn("/app/api/common/login");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceProperties.serviceId()).thenReturn("COMMON");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("COMMON", result.getService());
        }
    }

    @Nested
    @DisplayName("Pattern matching tests")
    class PatternMatchingTests {

        @Test
        @DisplayName("Should match wildcard patterns")
        void shouldMatchWildcardPatterns() {
            when(request.getRequestURI()).thenReturn("/api/common/login/deep/nested/path");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceProperties.serviceId()).thenReturn("COMMON");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("COMMON", result.getService());
        }

        @Test
        @DisplayName("Should match exact patterns")
        void shouldMatchExactPatterns() {
            when(request.getRequestURI()).thenReturn("/api/common/health");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceProperties.serviceId()).thenReturn("COMMON");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/health"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("COMMON", result.getService());
        }

        @Test
        @DisplayName("Should handle empty basePath")
        void shouldHandleEmptyBasePath() {
            when(request.getRequestURI()).thenReturn("/api/test");
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("");
            when(serviceProperties.serviceId()).thenReturn("ROOT");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/api/**"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("ROOT", result.getService());
        }
    }

    @Nested
    @DisplayName("Multiple services tests")
    class MultipleServicesTests {

        @Mock
        private ServiceProperties serviceProperties2;

        @Mock
        private ServiceMappingConfig serviceMappingConfig2;

        @Test
        @DisplayName("Should find first matching service")
        void shouldFindFirstMatchingService() {
            when(request.getRequestURI()).thenReturn("/api/auth/login");
            when(channelProperties.getService()).thenReturn(Arrays.asList(serviceProperties, serviceProperties2));

            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceProperties.basePath()).thenReturn("/api/common");
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**"));

            when(serviceProperties2.enabled()).thenReturn(true);
            when(serviceProperties2.serviceMapping()).thenReturn(serviceMappingConfig2);
            when(serviceProperties2.basePath()).thenReturn("/api/auth");
            when(serviceProperties2.serviceId()).thenReturn("AUTH");
            when(serviceMappingConfig2.allowedUrlPatterns()).thenReturn(Arrays.asList("/login/**"));

            ServiceId result = serviceIdResolver.resolve(request);

            assertNotNull(result);
            assertEquals("AUTH", result.getService());
        }
    }
}
