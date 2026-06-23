package com.scbank.process.api.fw.channel.context.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.LocaleResolver;

import com.scbank.process.api.fw.channel.context.DefaultServiceContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.service.ServiceId;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContext;
import com.scbank.process.api.fw.session.ISessionContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * DefaultServiceContextHandler Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceContextHandlerTest {

    @Mock
    private IIdentifyGenerator identifyGenerator;

    @Mock
    private IDeviceResolver deviceResolver;

    @Mock
    private IServiceRegistrar serviceRegistrar;

    @Mock
    private LocaleResolver localeResolver;

    @Mock
    private ServiceIdResolver serviceIdResolver;

    @Mock
    private ISessionContextResolver sessionContextResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private IDevice device;

    @Mock
    private ISessionContext sessionContext;

    @Mock
    private ServiceDefinitionMetadata serviceDefinition;

    private DefaultServiceContextHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DefaultServiceContextHandler(
                identifyGenerator,
                deviceResolver,
                serviceRegistrar,
                localeResolver,
                serviceIdResolver,
                sessionContextResolver);
    }

    @Nested
    @DisplayName("createServiceContext tests")
    class CreateServiceContextTests {

        @Test
        @DisplayName("Should create service context with all components")
        void shouldCreateServiceContext() {
            // Given
            String requestUUID = "test-uuid-123";
            String channelId = "WEB";
            Locale locale = Locale.KOREA;
            ServiceId serviceId = ServiceId.builder().service("TEST_SERVICE").url("/api/test").build();

            when(request.getHeader("channel")).thenReturn(channelId);
            when(request.getHeader("x-tracking-id")).thenReturn(null);
            when(identifyGenerator.generateId()).thenReturn(requestUUID);
            when(deviceResolver.resolve(request)).thenReturn(device);
            when(localeResolver.resolveLocale(request)).thenReturn(locale);
            when(serviceIdResolver.resolve(request)).thenReturn(serviceId);
            when(serviceRegistrar.getServiceDefinition(serviceId)).thenReturn(serviceDefinition);
            when(sessionContextResolver.resolve(request)).thenReturn(sessionContext);
            when(serviceDefinition.getParameters()).thenReturn(List.of());

            // When
            IServiceContext context = handler.createServiceContext(request, response);

            // Then
            assertNotNull(context);
            assertTrue(context instanceof DefaultServiceContext);
            assertEquals(channelId, context.channelId());
            assertEquals(requestUUID, context.requestUId());
            assertEquals(device, context.device());
            assertEquals(locale, context.locale());
            assertEquals(serviceDefinition, context.serviceDefinition());
            assertEquals(sessionContext, context.session());
        }

        @Test
        @DisplayName("Should use requestId from header if present")
        void shouldUseRequestIdFromHeader() {
            // Given
            String headerRequestId = "x-tracking-id";
            ServiceId serviceId = ServiceId.builder().service("TEST_SERVICE").url("/api/test").build();

            when(request.getHeader("channel")).thenReturn("");
            when(request.getHeader("x-tracking-id")).thenReturn(headerRequestId);
            when(deviceResolver.resolve(request)).thenReturn(device);
            when(localeResolver.resolveLocale(request)).thenReturn(Locale.KOREA);
            when(serviceIdResolver.resolve(request)).thenReturn(serviceId);
            when(serviceRegistrar.getServiceDefinition(serviceId)).thenReturn(serviceDefinition);
            when(sessionContextResolver.resolve(request)).thenReturn(sessionContext);
            when(serviceDefinition.getParameters()).thenReturn(List.of());

            // When
            IServiceContext context = handler.createServiceContext(request, response);

            // Then
            assertEquals(headerRequestId, context.requestUId());
            verify(identifyGenerator, never()).generateId();
        }

        @Test
        @DisplayName("Should generate new UUID when requestId header is blank")
        void shouldGenerateUuidWhenHeaderBlank() {
            // Given
            String generatedUUID = "generated-uuid";
            ServiceId serviceId = ServiceId.builder().service("TEST_SERVICE").url("/api/test").build();

            when(request.getHeader("channel")).thenReturn("");
            when(request.getHeader("x-tracking-id")).thenReturn("   ");
            when(identifyGenerator.generateId()).thenReturn(generatedUUID);
            when(deviceResolver.resolve(request)).thenReturn(device);
            when(localeResolver.resolveLocale(request)).thenReturn(Locale.KOREA);
            when(serviceIdResolver.resolve(request)).thenReturn(serviceId);
            when(serviceRegistrar.getServiceDefinition(serviceId)).thenReturn(serviceDefinition);
            when(sessionContextResolver.resolve(request)).thenReturn(sessionContext);
            when(serviceDefinition.getParameters()).thenReturn(List.of());

            // When
            IServiceContext context = handler.createServiceContext(request, response);

            // Then
            assertEquals(generatedUUID, context.requestUId());
            verify(identifyGenerator).generateId();
        }

        @Test
        @DisplayName("Should handle null service definition parameters")
        void shouldHandleNullServiceDefinitionParameters() {
            // Given
            ServiceId serviceId = ServiceId.builder().service("TEST_SERVICE").url("/api/test").build();

            when(request.getHeader("channel")).thenReturn("WEB");
            when(request.getHeader("x-tracking-id")).thenReturn("test-id");
            when(deviceResolver.resolve(request)).thenReturn(device);
            when(localeResolver.resolveLocale(request)).thenReturn(Locale.KOREA);
            when(serviceIdResolver.resolve(request)).thenReturn(serviceId);
            when(serviceRegistrar.getServiceDefinition(serviceId)).thenReturn(serviceDefinition);
            when(sessionContextResolver.resolve(request)).thenReturn(sessionContext);
            when(serviceDefinition.getParameters()).thenReturn(null);

            // When
            IServiceContext context = handler.createServiceContext(request, response);

            // Then
            assertNotNull(context);
            assertNull(context.parameter("anyKey"));
        }
    }
}
