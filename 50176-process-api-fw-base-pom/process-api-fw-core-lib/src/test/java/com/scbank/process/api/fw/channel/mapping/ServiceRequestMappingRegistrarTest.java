package com.scbank.process.api.fw.channel.mapping;

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
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.ServiceProperties.ServiceMappingConfig;
import com.scbank.process.api.fw.channel.controller.DefaultServiceDispatchController;

/**
 * ServiceRequestMappingRegistrar Test Class
 */
@ExtendWith(MockitoExtension.class)
class ServiceRequestMappingRegistrarTest {

    @Mock
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ServiceProperties serviceProperties;

    @Mock
    private ServiceMappingConfig serviceMappingConfig;

    private ServiceRequestMappingRegistrar registrar;

    @BeforeEach
    void setUp() {
        registrar = new ServiceRequestMappingRegistrar(requestMappingHandlerMapping, channelProperties);
        registrar.setApplicationContext(applicationContext);
    }

    @Nested
    @DisplayName("init tests")
    class InitTests {

        @Test
        @DisplayName("Should handle null channel properties")
        void shouldHandleNullChannelProperties() {
            ServiceRequestMappingRegistrar nullPropsRegistrar =
                    new ServiceRequestMappingRegistrar(requestMappingHandlerMapping, null);

            assertDoesNotThrow(() -> nullPropsRegistrar.init());
        }

        @Test
        @DisplayName("Should handle null service list")
        void shouldHandleNullServiceList() {
            when(channelProperties.getService()).thenReturn(null);

            assertDoesNotThrow(() -> registrar.init());
        }

        @Test
        @DisplayName("Should handle empty service list")
        void shouldHandleEmptyServiceList() {
            when(channelProperties.getService()).thenReturn(Collections.emptyList());

            assertDoesNotThrow(() -> registrar.init());
        }

        @Test
        @DisplayName("Should skip disabled services")
        void shouldSkipDisabledServices() {
            when(channelProperties.getService()).thenReturn(List.of(serviceProperties));
            when(serviceProperties.enabled()).thenReturn(false);

            registrar.init();

            verify(serviceProperties).enabled();
            verify(applicationContext, never()).getBean(any(Class.class));
        }

        @Test
        @DisplayName("Should process enabled services")
        void shouldProcessEnabledServices() {
            DefaultServiceDispatchController controller = mock(DefaultServiceDispatchController.class);

            when(channelProperties.getService()).thenReturn(List.of(serviceProperties));
            doReturn(DefaultServiceDispatchController.class).when(channelProperties).getDefaultControllerClass();
            when(channelProperties.getDefaultControllerMethod()).thenReturn("dispatch");
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceId()).thenReturn("TEST_SERVICE");
            when(serviceProperties.basePath()).thenReturn("/api/test");
            when(serviceProperties.controllerClass()).thenReturn(null);
            when(serviceProperties.controllerMethod()).thenReturn(null);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(List.of("/**"));
            when(serviceMappingConfig.allowedContentTypes()).thenReturn(List.of("application/json"));
            when(serviceMappingConfig.allowedMethods()).thenReturn(List.of("POST"));
            when(applicationContext.getBean(DefaultServiceDispatchController.class)).thenReturn(controller);

            registrar.init();

            verify(serviceProperties).enabled();
            verify(applicationContext).getBean(DefaultServiceDispatchController.class);
        }
    }

    @Nested
    @DisplayName("afterSingletonsInstantiated tests")
    class AfterSingletonsInstantiatedTests {

        @Test
        @DisplayName("Should call init method")
        void shouldCallInitMethod() {
            when(channelProperties.getService()).thenReturn(null);

            assertDoesNotThrow(() -> registrar.afterSingletonsInstantiated());
        }
    }

    @Nested
    @DisplayName("setApplicationContext tests")
    class SetApplicationContextTests {

        @Test
        @DisplayName("Should set application context")
        void shouldSetApplicationContext() {
            ApplicationContext newContext = mock(ApplicationContext.class);

            assertDoesNotThrow(() -> registrar.setApplicationContext(newContext));
        }
    }

    @Nested
    @DisplayName("Service registration tests")
    class ServiceRegistrationTests {

        @Test
        @DisplayName("Should use default controller class when not specified")
        void shouldUseDefaultControllerClassWhenNotSpecified() {
            DefaultServiceDispatchController controller = mock(DefaultServiceDispatchController.class);

            when(channelProperties.getService()).thenReturn(List.of(serviceProperties));
            doReturn(DefaultServiceDispatchController.class).when(channelProperties).getDefaultControllerClass();
            when(channelProperties.getDefaultControllerMethod()).thenReturn("dispatch");
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceId()).thenReturn("TEST");
            when(serviceProperties.basePath()).thenReturn("/api");
            when(serviceProperties.controllerClass()).thenReturn(null);
            when(serviceProperties.controllerMethod()).thenReturn(null);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(List.of("/test"));
            when(serviceMappingConfig.allowedContentTypes()).thenReturn(List.of("application/json"));
            when(serviceMappingConfig.allowedMethods()).thenReturn(List.of("GET"));
            when(applicationContext.getBean(DefaultServiceDispatchController.class)).thenReturn(controller);

            registrar.init();

            verify(applicationContext).getBean(DefaultServiceDispatchController.class);
        }

        @Test
        @DisplayName("Should handle multiple URL patterns")
        void shouldHandleMultipleUrlPatterns() {
            DefaultServiceDispatchController controller = mock(DefaultServiceDispatchController.class);

            when(channelProperties.getService()).thenReturn(List.of(serviceProperties));
            doReturn(DefaultServiceDispatchController.class).when(channelProperties).getDefaultControllerClass();
            when(channelProperties.getDefaultControllerMethod()).thenReturn("dispatch");
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceId()).thenReturn("MULTI");
            when(serviceProperties.basePath()).thenReturn("/api");
            when(serviceProperties.controllerClass()).thenReturn(null);
            when(serviceProperties.controllerMethod()).thenReturn(null);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(Arrays.asList("/users/**", "/orders/**", "/products/**"));
            when(serviceMappingConfig.allowedContentTypes()).thenReturn(List.of("application/json"));
            when(serviceMappingConfig.allowedMethods()).thenReturn(List.of("GET", "POST"));
            when(applicationContext.getBean(DefaultServiceDispatchController.class)).thenReturn(controller);

            assertDoesNotThrow(() -> registrar.init());
        }

        @Test
        @DisplayName("Should handle empty base path")
        void shouldHandleEmptyBasePath() {
            DefaultServiceDispatchController controller = mock(DefaultServiceDispatchController.class);

            when(channelProperties.getService()).thenReturn(List.of(serviceProperties));
            doReturn(DefaultServiceDispatchController.class).when(channelProperties).getDefaultControllerClass();
            when(channelProperties.getDefaultControllerMethod()).thenReturn("dispatch");
            when(serviceProperties.enabled()).thenReturn(true);
            when(serviceProperties.serviceId()).thenReturn("NO_BASE");
            when(serviceProperties.basePath()).thenReturn("");
            when(serviceProperties.controllerClass()).thenReturn(null);
            when(serviceProperties.controllerMethod()).thenReturn(null);
            when(serviceProperties.serviceMapping()).thenReturn(serviceMappingConfig);
            when(serviceMappingConfig.allowedUrlPatterns()).thenReturn(List.of("/direct/**"));
            when(serviceMappingConfig.allowedContentTypes()).thenReturn(List.of("application/json"));
            when(serviceMappingConfig.allowedMethods()).thenReturn(List.of("POST"));
            when(applicationContext.getBean(DefaultServiceDispatchController.class)).thenReturn(controller);

            assertDoesNotThrow(() -> registrar.init());
        }
    }
}
