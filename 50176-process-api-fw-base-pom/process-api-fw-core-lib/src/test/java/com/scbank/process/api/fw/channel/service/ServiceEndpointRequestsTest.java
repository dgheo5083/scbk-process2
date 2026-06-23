package com.scbank.process.api.fw.channel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties.ServiceMappingConfig;

/**
 * ServiceEndpointRequests Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceEndpointRequestsTest {

    private ServiceEndpointRequests serviceEndpointRequests;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private ServiceProperties serviceProperties1;

    @Mock
    private ServiceProperties serviceProperties2;

    @Mock
    private ServiceMappingConfig serviceMappingConfig1;

    @Mock
    private ServiceMappingConfig serviceMappingConfig2;

    @BeforeEach
    void setUp() {
        serviceEndpointRequests = new ServiceEndpointRequests(channelProperties);
    }

    @Nested
    @DisplayName("enabledServiceEndpointUrls tests")
    class EnabledServiceEndpointUrlsTests {

        @Test
        @DisplayName("Should return empty list when properties is null")
        void shouldReturnEmptyListWhenPropertiesIsNull() {
            serviceEndpointRequests = new ServiceEndpointRequests(null);

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when service list is null")
        void shouldReturnEmptyListWhenServiceListIsNull() {
            when(channelProperties.getService()).thenReturn(null);

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return only enabled service URLs")
        void shouldReturnOnlyEnabledServiceUrls() {
            when(channelProperties.getService()).thenReturn(Arrays.asList(serviceProperties1, serviceProperties2));
            when(serviceProperties1.enabled()).thenReturn(true);
            when(serviceProperties1.basePath()).thenReturn("/api/common");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/login", "/logout"));

            when(serviceProperties2.enabled()).thenReturn(false);

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertEquals(2, result.size());
            assertTrue(result.contains("/api/common/login"));
            assertTrue(result.contains("/api/common/logout"));
        }

        @Test
        @DisplayName("Should combine basePath and URL patterns")
        void shouldCombineBasePathAndUrlPatterns() {
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties1));
            when(serviceProperties1.enabled()).thenReturn(true);
            when(serviceProperties1.basePath()).thenReturn("/api/auth");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/verify", "/token/**"));

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertEquals(2, result.size());
            assertTrue(result.contains("/api/auth/verify"));
            assertTrue(result.contains("/api/auth/token/**"));
        }

        @Test
        @DisplayName("Should handle empty basePath")
        void shouldHandleEmptyBasePath() {
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties1));
            when(serviceProperties1.enabled()).thenReturn(true);
            when(serviceProperties1.basePath()).thenReturn("");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/health", "/status"));

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertEquals(2, result.size());
            assertTrue(result.contains("/health"));
            assertTrue(result.contains("/status"));
        }

        @Test
        @DisplayName("Should handle null basePath")
        void shouldHandleNullBasePath() {
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties1));
            when(serviceProperties1.enabled()).thenReturn(true);
            when(serviceProperties1.basePath()).thenReturn(null);
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/api/test"));

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertEquals(1, result.size());
            assertTrue(result.contains("/api/test"));
        }
    }

    @Nested
    @DisplayName("serviceEndpointUrls tests")
    class ServiceEndpointUrlsTests {

        @Test
        @DisplayName("Should return empty list when properties is null")
        void shouldReturnEmptyListWhenPropertiesIsNull() {
            serviceEndpointRequests = new ServiceEndpointRequests(null);

            List<String> result = serviceEndpointRequests.serviceEndpointUrls();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty list when service list is null")
        void shouldReturnEmptyListWhenServiceListIsNull() {
            when(channelProperties.getService()).thenReturn(null);

            List<String> result = serviceEndpointRequests.serviceEndpointUrls();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return all service URLs regardless of enabled status")
        void shouldReturnAllServiceUrlsRegardlessOfEnabledStatus() {
            when(channelProperties.getService()).thenReturn(Arrays.asList(serviceProperties1, serviceProperties2));

            when(serviceProperties1.basePath()).thenReturn("/api/enabled");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/test1"));

            when(serviceProperties2.basePath()).thenReturn("/api/disabled");
            when(serviceProperties2.serviceMapping()).thenReturn(serviceMappingConfig2);
            when(serviceMappingConfig2.allowedUrlPatterns()).thenReturn(Arrays.asList("/test2"));

            List<String> result = serviceEndpointRequests.serviceEndpointUrls();

            assertEquals(2, result.size());
            assertTrue(result.contains("/api/enabled/test1"));
            assertTrue(result.contains("/api/disabled/test2"));
        }

        @Test
        @DisplayName("Should handle blank basePath")
        void shouldHandleBlankBasePath() {
            when(channelProperties.getService()).thenReturn(Collections.singletonList(serviceProperties1));
            when(serviceProperties1.basePath()).thenReturn("   ");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/endpoint"));

            List<String> result = serviceEndpointRequests.serviceEndpointUrls();

            assertEquals(1, result.size());
            assertTrue(result.contains("/endpoint"));
        }
    }

    @Nested
    @DisplayName("Multiple services tests")
    class MultipleServicesTests {

        @Test
        @DisplayName("Should aggregate URLs from multiple services")
        void shouldAggregateUrlsFromMultipleServices() {
            when(channelProperties.getService()).thenReturn(Arrays.asList(serviceProperties1, serviceProperties2));

            when(serviceProperties1.enabled()).thenReturn(true);
            when(serviceProperties1.basePath()).thenReturn("/api/service1");
            when(serviceProperties1.serviceMapping()).thenReturn(serviceMappingConfig1);
            when(serviceMappingConfig1.allowedUrlPatterns()).thenReturn(Arrays.asList("/action1", "/action2"));

            when(serviceProperties2.enabled()).thenReturn(true);
            when(serviceProperties2.basePath()).thenReturn("/api/service2");
            when(serviceProperties2.serviceMapping()).thenReturn(serviceMappingConfig2);
            when(serviceMappingConfig2.allowedUrlPatterns()).thenReturn(Arrays.asList("/action3"));

            List<String> result = serviceEndpointRequests.enabledServiceEndpointUrls();

            assertEquals(3, result.size());
            assertTrue(result.contains("/api/service1/action1"));
            assertTrue(result.contains("/api/service1/action2"));
            assertTrue(result.contains("/api/service2/action3"));
        }
    }
}
