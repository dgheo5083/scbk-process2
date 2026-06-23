package com.scbank.process.api.fw.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.integration.cfg.AbstractIntegrationRequestOptions;
import com.scbank.process.api.fw.integration.cfg.IntegrationRequestOptions;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodec;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodecRegistry;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.context.IntegrationContextHolder;
import com.scbank.process.api.fw.integration.exception.IntegrationException;
import com.scbank.process.api.fw.integration.exception.IntegrationSystemException;
import com.scbank.process.api.fw.integration.exception.IntegrationTimeoutException;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorChain;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorRegistry;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.integration.request.IntegrationRequestHeaderBuilder;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.MessageFormat;

import feign.Request;
import feign.Request.HttpMethod;
import feign.RetryableException;

/**
 * IntegrationManager Test Class
 */
@ExtendWith(MockitoExtension.class)
class IntegrationManagerTest {

    @Mock
    private IntegrationSystemConfig mockSystemConfig;

    @Mock
    private IntegrationRequestHeaderBuilder<?, ?> mockHeaderBuilder;

    @Mock
    private IntegrationResponseHandler<?, ?, ?> mockResponseHandler;

    @Mock
    private IntegrationClientCodecRegistry mockCodecRegistry;

    @Mock
    private IntegrationClientCodec mockCodec;

    @Mock
    private IntegrationInterceptorRegistry mockInterceptorRegistry;

    @Mock
    private IntegrationInterceptorChain mockInterceptorChain;

    @Mock
    private IntegrationMessageContextCreator mockMessageContextCreator;

    @Mock
    private MessageContext mockMessageContext;

    @Mock
    private IntegrationRequest<? extends IMessageObject, ? extends IMessageObject> mockRequest;

    @Mock
    private IntegrationContext mockContext;

    private TestIntegrationManager manager;

    /**
     * Concrete test implementation of IntegrationManager
     */
    private static class TestIntegrationManager extends IntegrationManager {
        private final String systemId;

        @SuppressWarnings("rawtypes")
        public TestIntegrationManager(IntegrationSystemConfig systemConfig,
                IntegrationRequestHeaderBuilder requestHeaderBuilder,
                IntegrationResponseHandler responseHandler,
                String systemId) {
            super(systemConfig, requestHeaderBuilder, responseHandler);
            this.systemId = systemId;
        }

        @Override
        protected String getSystemId() {
            return systemId;
        }

        // Expose protected methods for testing
        public IntegrationClientCodecRegistry testGetIntegrationClientCodecRegistry() {
            return getIntegrationClientCodecRegistry();
        }

        public IntegrationInterceptorRegistry testGetIntegrationInterceptorRegistry() {
            return getIntegrationInterceptorRegistry();
        }

        public IntegrationClientCodec testGetIntegrationClientCodec() {
            return getIntegrationClientCodec();
        }

        public IntegrationInterceptorChain testGetInterceptorChain() {
            return getInterceptorChain();
        }

        public IntegrationContext testCreateContext(IntegrationRequestOptions cfg) {
            return createContext(cfg);
        }

        public MessageContext testCreateMessageContext(IntegrationContext context) {
            return createMessageContext(context);
        }

        public boolean testIsSimulationMode(IntegrationRequestOptions cfg) {
            return isSimulationMode(cfg);
        }

        @SuppressWarnings({ "rawtypes" })
        public void testHandleException(IntegrationContext context, IntegrationRequest request,
                IntegrationInterceptorChain interceptorChain, Throwable e) {
            handleException(context, request, interceptorChain, e);
        }
    }

    @BeforeEach
    void setUp() {
        manager = new TestIntegrationManager(mockSystemConfig, mockHeaderBuilder, mockResponseHandler, "TEST");
    }

    @AfterEach
    void tearDown() {
        IntegrationContextHolder.clear();
    }

    @Nested
    @DisplayName("getSystemId tests")
    class GetSystemIdTests {

        @Test
        @DisplayName("Should return system id")
        void shouldReturnSystemId() {
            assertEquals("TEST", manager.getSystemId());
        }
    }

    @Nested
    @DisplayName("getIntegrationClientCodecRegistry tests")
    class GetCodecRegistryTests {

        @Test
        @DisplayName("Should get codec registry from RuntimeContext when null")
        void shouldGetCodecRegistryFromRuntimeContext() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationClientCodecRegistry.class))
                        .thenReturn(mockCodecRegistry);

                IntegrationClientCodecRegistry registry = manager.testGetIntegrationClientCodecRegistry();

                assertNotNull(registry);
                assertEquals(mockCodecRegistry, registry);
            }
        }

        @Test
        @DisplayName("Should return cached codec registry on second call")
        void shouldReturnCachedCodecRegistry() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationClientCodecRegistry.class))
                        .thenReturn(mockCodecRegistry);

                manager.testGetIntegrationClientCodecRegistry();
                IntegrationClientCodecRegistry registry = manager.testGetIntegrationClientCodecRegistry();

                assertEquals(mockCodecRegistry, registry);
                mockedRuntime.verify(() -> RuntimeContext.getBean(IntegrationClientCodecRegistry.class), times(1));
            }
        }
    }

    @Nested
    @DisplayName("getIntegrationInterceptorRegistry tests")
    class GetInterceptorRegistryTests {

        @Test
        @DisplayName("Should get interceptor registry from RuntimeContext when null")
        void shouldGetInterceptorRegistryFromRuntimeContext() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationInterceptorRegistry.class))
                        .thenReturn(mockInterceptorRegistry);

                IntegrationInterceptorRegistry registry = manager.testGetIntegrationInterceptorRegistry();

                assertNotNull(registry);
                assertEquals(mockInterceptorRegistry, registry);
            }
        }
    }

    @Nested
    @DisplayName("getIntegrationClientCodec tests")
    class GetCodecTests {

        @Test
        @DisplayName("Should get codec for system format")
        void shouldGetCodecForSystemFormat() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationClientCodecRegistry.class))
                        .thenReturn(mockCodecRegistry);
                when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);
                when(mockCodecRegistry.getCodec(MessageFormat.XML)).thenReturn(mockCodec);

                IntegrationClientCodec codec = manager.testGetIntegrationClientCodec();

                assertNotNull(codec);
                assertEquals(mockCodec, codec);
            }
        }
    }

    @Nested
    @DisplayName("getInterceptorChain tests")
    class GetInterceptorChainTests {

        @Test
        @DisplayName("Should return null when interceptor registry is null")
        void shouldReturnNullWhenRegistryIsNull() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationInterceptorRegistry.class))
                        .thenReturn(null);

                IntegrationInterceptorChain chain = manager.testGetInterceptorChain();

                assertNull(chain);
            }
        }

        @Test
        @DisplayName("Should resolve interceptor chain from registry")
        void shouldResolveInterceptorChainFromRegistry() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationInterceptorRegistry.class))
                        .thenReturn(mockInterceptorRegistry);
                List<String> interceptorNames = List.of("interceptor1");
                when(mockSystemConfig.interceptors()).thenReturn(interceptorNames);
                when(mockInterceptorRegistry.resolve(interceptorNames)).thenReturn(mockInterceptorChain);

                IntegrationInterceptorChain chain = manager.testGetInterceptorChain();

                assertNotNull(chain);
                assertEquals(mockInterceptorChain, chain);
            }
        }
    }

    @Nested
    @DisplayName("createContext tests")
    class CreateContextTests {

        @Test
        @DisplayName("Should create context with system id and interface id")
        void shouldCreateContextWithSystemIdAndInterfaceId() {
            Map<String, Object> systemProps = new HashMap<>();
            systemProps.put("key1", "value1");

            when(mockSystemConfig.properties()).thenReturn(systemProps);
            when(mockSystemConfig.charset()).thenReturn("UTF-8");
            when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);

            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setInterfaceId("IF001");

            LocaleContextHolder.setLocale(Locale.KOREA);

            IntegrationContext context = manager.testCreateContext(cfg);

            assertNotNull(context);
            assertEquals("TEST", context.getSystemId());
            assertEquals("IF001", context.getInterfaceId());
            assertEquals("UTF-8", context.getCharset());
            assertEquals(Locale.KOREA, context.getLocale());
            assertNotNull(context.getAttribute("key1"));
        }

        @Test
        @DisplayName("Should merge config attributes with system properties")
        void shouldMergeConfigAttributesWithSystemProperties() {
            Map<String, Object> systemProps = new HashMap<>();
            systemProps.put("system", "value");

            when(mockSystemConfig.properties()).thenReturn(systemProps);
            when(mockSystemConfig.charset()).thenReturn("UTF-8");
            when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);

            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setInterfaceId("IF001");
            Map<String, Object> cfgAttrs = new HashMap<>();
            cfgAttrs.put("custom", "customValue");
            cfg.setAttributes(cfgAttrs);

            IntegrationContext context = manager.testCreateContext(cfg);

            assertEquals("value", context.getAttribute("system"));
            assertEquals("customValue", context.getAttribute("custom"));
        }

        @Test
        @DisplayName("Should set context in holder")
        void shouldSetContextInHolder() {
            Map<String, Object> systemProps = new HashMap<>();
            when(mockSystemConfig.properties()).thenReturn(systemProps);
            when(mockSystemConfig.charset()).thenReturn("UTF-8");
            when(mockSystemConfig.format()).thenReturn(MessageFormat.XML);

            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setInterfaceId("IF001");

            IntegrationContext context = manager.testCreateContext(cfg);

            assertNotNull(IntegrationContextHolder.get());
            assertEquals(context, IntegrationContextHolder.get());
        }
    }

    @Nested
    @DisplayName("createMessageContext tests")
    class CreateMessageContextTests {

        @Test
        @DisplayName("Should create message context and set in integration context")
        void shouldCreateMessageContextAndSetInIntegrationContext() {
            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IntegrationMessageContextCreator.class))
                        .thenReturn(mockMessageContextCreator);
                when(mockMessageContextCreator.create(mockSystemConfig)).thenReturn(mockMessageContext);

                IntegrationContext context = IntegrationContext.builder()
                        .systemId("TEST")
                        .attributes(new HashMap<>())
                        .build();

                MessageContext result = manager.testCreateMessageContext(context);

                assertNotNull(result);
                assertEquals(mockMessageContext, result);
                assertNotNull(context.getAttribute("_MESSAGE_CTX_"));
            }
        }
    }

    @Nested
    @DisplayName("isSimulationMode tests")
    class IsSimulationModeTests {

        @Test
        @DisplayName("Should return true when cfg simulation mode is true")
        void shouldReturnTrueWhenCfgSimulationModeIsTrue() {
            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setSimulationMode(true);

            boolean result = manager.testIsSimulationMode(cfg);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return true when system config simulation is enabled")
        void shouldReturnTrueWhenSystemConfigSimulationEnabled() {
            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setSimulationMode(false);

            IntegrationProperties.SimulationConfig simConfig =
                    new IntegrationProperties.SimulationConfig(true, "classpath:/sim/");
            when(mockSystemConfig.simulation()).thenReturn(simConfig);

            boolean result = manager.testIsSimulationMode(cfg);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when simulation is not enabled")
        void shouldReturnFalseWhenSimulationNotEnabled() {
            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setSimulationMode(false);

            IntegrationProperties.SimulationConfig simConfig =
                    new IntegrationProperties.SimulationConfig(false, null);
            when(mockSystemConfig.simulation()).thenReturn(simConfig);

            boolean result = manager.testIsSimulationMode(cfg);

            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when simulation config is null")
        void shouldReturnFalseWhenSimulationConfigIsNull() {
            AbstractIntegrationRequestOptions cfg = new AbstractIntegrationRequestOptions() {
            };
            cfg.setSimulationMode(false);

            when(mockSystemConfig.simulation()).thenReturn(null);

            boolean result = manager.testIsSimulationMode(cfg);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("handleException tests")
    class HandleExceptionTests {

        @Test
        @DisplayName("Should do nothing when exception is null")
        void shouldDoNothingWhenExceptionIsNull() {
            assertDoesNotThrow(() ->
                    manager.testHandleException(mockContext, mockRequest, mockInterceptorChain, null));
        }

        @Test
        @DisplayName("Should throw IntegrationSystemException for ConnectException")
        void shouldThrowSystemExceptionForConnectException() {
            ConnectException connectException = new ConnectException("Connection refused");
            Request request = Request.create(HttpMethod.POST, "http://test", Map.of(), null, null, null);
            RetryableException retryable = new RetryableException(-1, "Retry", HttpMethod.POST, connectException,
                    (Long) null, request);

            assertThrows(IntegrationSystemException.class, () ->
                    manager.testHandleException(mockContext, mockRequest, mockInterceptorChain, retryable));
        }

        @Test
        @DisplayName("Should throw IntegrationTimeoutException for SocketTimeoutException")
        void shouldThrowTimeoutExceptionForSocketTimeoutException() {
            SocketTimeoutException socketTimeout = new SocketTimeoutException("Timeout");
            Request request = Request.create(HttpMethod.POST, "http://test", Map.of(), null, null, null);
            RetryableException retryable = new RetryableException(-1, "Retry", HttpMethod.POST, socketTimeout,
                    (Long) null, request);

            assertThrows(IntegrationTimeoutException.class, () ->
                    manager.testHandleException(mockContext, mockRequest, mockInterceptorChain, retryable));
        }

        @Test
        @DisplayName("Should rethrow IntegrationException as-is")
        void shouldRethrowIntegrationExceptionAsIs() {
            IntegrationException integrationEx = new IntegrationException("Test error");

            assertThrows(IntegrationException.class, () ->
                    manager.testHandleException(mockContext, mockRequest, mockInterceptorChain, integrationEx));
        }

        @Test
        @DisplayName("Should throw IntegrationSystemException for generic exception")
        void shouldThrowSystemExceptionForGenericException() {
            RuntimeException genericEx = new RuntimeException("Generic error");

            assertThrows(IntegrationSystemException.class, () ->
                    manager.testHandleException(mockContext, mockRequest, mockInterceptorChain, genericEx));
        }
    }
    
    @Test
    void createRequestOptions_shouldCreateFeignOptionsFormIntegrationOptions() {
    	
    	IntegrationRequestOptions integrationRequestOptions = mock(IntegrationRequestOptions.class);
    	
    	when(integrationRequestOptions.getConnectTimeout()).thenReturn(1500L);
    	when(integrationRequestOptions.getReadTimeout()).thenReturn(4500L);
    	
    	TestIntegrationManager manager = new TestIntegrationManager(mockSystemConfig, mockHeaderBuilder, mockResponseHandler, "TEST");
    	
    	Request.Options feignOptions = manager.createRequestOptions(integrationRequestOptions);
    	
    	assertNotNull(feignOptions);
    	
    	assertEquals(1500, feignOptions.connectTimeoutMillis());
    	assertEquals(4500, feignOptions.readTimeoutMillis());
    	assertFalse(feignOptions.isFollowRedirects());
    	
    	verify(integrationRequestOptions, times(1)).getConnectTimeout();
    	verify(integrationRequestOptions, times(1)).getReadTimeout();
    	verifyNoMoreInteractions(integrationRequestOptions);
    }
}
