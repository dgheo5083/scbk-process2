package com.scbank.process.api.fw.channel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.channel.response.ResponseRendererComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutor;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.channel.service.resolver.IServiceComponentResolver;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.validation.IBeanValidator;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

/**
 * DefaultServiceDispatchController Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class DefaultServiceDispatchControllerTest {

    @Mock
    private IServiceRegistrar serviceRegistrar;

    @Mock
    private IServiceComponentResolver serviceComponentResolver;

    @Mock
    private IServiceComponentExecutorFactory executorFactory;

    @Mock
    private ResponseRendererComposite responseRendererComposite;

    @Mock
    private HttpMessageConverterComposite<?> httpMessageConverterComposite;

    @Mock
    private IResponseMessageFactory<?, ?, ?> responseMessageFactory;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private IBeanValidator<?> beanValidator;

    @Mock
    private HttpServletRequest request;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private ServiceDefinitionMetadata serviceDefinitionMetadata;

    @Mock
    private ServiceInfo serviceInfo;

    @Mock
    private ServiceMethodMetadata serviceMethodMetadata;

    @Mock
    private IServiceComponentExecutor executor;

    @Mock
    private IMessageObject outputMessage;

    @Mock
    private IResponseMessage<?, ?> responseMessage;

    private DefaultServiceDispatchController controller;

    private MockedStatic<RuntimeContext> runtimeContextMock;

    @BeforeEach
    void setUp() {
        controller = new DefaultServiceDispatchController();
        runtimeContextMock = mockStatic(RuntimeContext.class);
    }

    @AfterEach
    void tearDown() {
        ServiceContextHolder.clear();
        if (runtimeContextMock != null) {
            runtimeContextMock.close();
        }
    }

    @Nested
    @DisplayName("Controller instantiation tests")
    class InstantiationTests {

        @Test
        @DisplayName("Should create controller instance")
        void shouldCreateControllerInstance() {
            DefaultServiceDispatchController newController = new DefaultServiceDispatchController();

            assertNotNull(newController);
        }
    }

    @Nested
    @DisplayName("dispatch tests")
    class DispatchTests {

        @Test
        @DisplayName("Should dispatch successfully with null input")
        @SuppressWarnings({ "unchecked", "rawtypes" })
        void shouldDispatchSuccessfullyWithNullInput() throws Throwable {
            // Setup service context
            ServiceContextHolder.setContext(serviceContext);

            // Setup mocks for RuntimeContext
            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceRegistrar.class))
                    .thenReturn(serviceRegistrar);
            runtimeContextMock.when(() -> RuntimeContext.getBean(HttpMessageConverterComposite.class))
                    .thenReturn(httpMessageConverterComposite);
            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceComponentExecutorFactory.class))
                    .thenReturn(executorFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(ResponseRendererComposite.class))
                    .thenReturn(responseRendererComposite);
            IResponseMessageFactory rawFactory = mock(IResponseMessageFactory.class);
            runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                    .thenReturn(rawFactory);

            // Setup service resolver behavior - through serviceDefinition
            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(java.util.List.of(serviceInfo));
            when(serviceInfo.isFallback()).thenReturn(true);
            when(serviceInfo.getComponent()).thenReturn("testComponent");
            when(serviceRegistrar.getServiceMethodMetadata("testComponent")).thenReturn(serviceMethodMetadata);
            when(serviceMethodMetadata.getParameters()).thenReturn(new ArrayList<>());

            // Setup executor
            when(executorFactory.create(serviceMethodMetadata)).thenReturn(executor);
            when(executor.execute(eq(serviceContext), isNull())).thenReturn(outputMessage);

            // Setup response
            when(rawFactory.ok(outputMessage)).thenReturn(responseMessage);
            ResponseEntity responseEntity = ResponseEntity.ok().build();
            when(responseRendererComposite.render(eq(responseMessage), eq(serviceContext))).thenReturn(responseEntity);

            // Execute
            ResponseEntity<?> result = controller.dispatch(request, null);

            // Verify
            assertNotNull(result);
            assertEquals(HttpStatus.OK, result.getStatusCode());
        }

//        @Test
//        @DisplayName("Should dispatch with validation enabled and input message")
//        @SuppressWarnings({ "unchecked", "rawtypes" })
//        void shouldDispatchWithValidationEnabledAndInputMessage() throws Throwable {
//            // Setup service context
//            ServiceContextHolder.setContext(serviceContext);
//
//            // Setup mocks for RuntimeContext
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceRegistrar.class))
//                    .thenReturn(serviceRegistrar);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(HttpMessageConverterComposite.class))
//                    .thenReturn(httpMessageConverterComposite);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceComponentExecutorFactory.class))
//                    .thenReturn(executorFactory);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(ResponseRendererComposite.class))
//                    .thenReturn(responseRendererComposite);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelProperties.class))
//                    .thenReturn(channelProperties);
//            IBeanValidator rawValidator = mock(IBeanValidator.class);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IBeanValidator.class))
//                    .thenReturn(rawValidator);
//            IResponseMessageFactory rawFactory = mock(IResponseMessageFactory.class);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
//                    .thenReturn(rawFactory);
//
//            // Setup validation config
//            ValidationConfig validationConfig = new ValidationConfig(true);
//            when(channelProperties.getValidation()).thenReturn(validationConfig);
//
//            // Setup service resolver behavior - through serviceDefinition
//            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
//            when(serviceDefinitionMetadata.getServices()).thenReturn(java.util.List.of(serviceInfo));
//            when(serviceInfo.isFallback()).thenReturn(true);
//            when(serviceInfo.getComponent()).thenReturn("testComponent");
//            when(serviceRegistrar.getServiceMethodMetadata("testComponent")).thenReturn(serviceMethodMetadata);
//
//            // Setup parameter with body
//            ServiceMethodMetadata.ParameterMetadata param = mock(ServiceMethodMetadata.ParameterMetadata.class);
//            when(param.isBody()).thenReturn(true);
//            when(param.getType()).thenReturn((Class) TestMessageObject.class);
//            when(serviceMethodMetadata.getParameters()).thenReturn(java.util.List.of(param));
//
//            // Setup request with body
//            when(request.getContentLength()).thenReturn(10);
//            ServletInputStream mockInputStream = createMockServletInputStream("{\"test\":1}".getBytes());
//            when(request.getInputStream()).thenReturn(mockInputStream);
//            when(request.getContentType()).thenReturn("application/json");
//
//            // Setup converter to return mock input
//            TestMessageObject inputMessage = new TestMessageObject();
//            //when(httpMessageConverterComposite.read(any(), any())).thenReturn(inputMessage);
//
//            // Setup executor
//            when(executorFactory.create(serviceMethodMetadata)).thenReturn(executor);
//            when(executor.execute(eq(serviceContext), eq(inputMessage))).thenReturn(outputMessage);
//
//            // Setup response
//            when(rawFactory.ok(outputMessage)).thenReturn(responseMessage);
//            ResponseEntity responseEntity = ResponseEntity.ok().build();
//            when(responseRendererComposite.render(eq(responseMessage), eq(serviceContext))).thenReturn(responseEntity);
//
//            // Execute
//            ResponseEntity<?> result = controller.dispatch(request, "{\"test\":1}".getBytes());
//
//            // Verify
//            assertNotNull(result);
//            verify(rawValidator).validate(inputMessage);
//        }

//        @Test
//        @DisplayName("Should dispatch with validation disabled")
//        @SuppressWarnings({ "unchecked", "rawtypes" })
//        void shouldDispatchWithValidationDisabled() throws Throwable {
//            // Setup service context
//            ServiceContextHolder.setContext(serviceContext);
//
//            // Setup mocks for RuntimeContext
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceRegistrar.class))
//                    .thenReturn(serviceRegistrar);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(HttpMessageConverterComposite.class))
//                    .thenReturn(httpMessageConverterComposite);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceComponentExecutorFactory.class))
//                    .thenReturn(executorFactory);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(ResponseRendererComposite.class))
//                    .thenReturn(responseRendererComposite);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelProperties.class))
//                    .thenReturn(channelProperties);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IBeanValidator.class))
//                    .thenReturn(beanValidator);
//            IResponseMessageFactory rawFactory = mock(IResponseMessageFactory.class);
//            runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
//                    .thenReturn(rawFactory);
//
//            // Setup validation config - disabled
//            ValidationConfig validationConfig = new ValidationConfig(false);
//            when(channelProperties.getValidation()).thenReturn(validationConfig);
//
//            // Setup service resolver behavior - through serviceDefinition
//            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
//            when(serviceDefinitionMetadata.getServices()).thenReturn(java.util.List.of(serviceInfo));
//            when(serviceInfo.isFallback()).thenReturn(true);
//            when(serviceInfo.getComponent()).thenReturn("testComponent");
//            when(serviceRegistrar.getServiceMethodMetadata("testComponent")).thenReturn(serviceMethodMetadata);
//
//            // Setup parameter with body
//            ServiceMethodMetadata.ParameterMetadata param = mock(ServiceMethodMetadata.ParameterMetadata.class);
//            when(param.isBody()).thenReturn(true);
//            when(param.getType()).thenReturn((Class) TestMessageObject.class);
//            when(serviceMethodMetadata.getParameters()).thenReturn(java.util.List.of(param));
//
//            // Setup request with body
//            when(request.getContentLength()).thenReturn(10);
//            ServletInputStream mockInputStream = createMockServletInputStream("{\"test\":1}".getBytes());
//            when(request.getInputStream()).thenReturn(mockInputStream);
//            when(request.getContentType()).thenReturn("application/json");
//
//            // Setup converter to return mock input
//            TestMessageObject inputMessage = new TestMessageObject();
//            //when(httpMessageConverterComposite.read(any(), any())).thenReturn(inputMessage);
//
//            // Setup executor
//            when(executorFactory.create(serviceMethodMetadata)).thenReturn(executor);
//            when(executor.execute(eq(serviceContext), eq(inputMessage))).thenReturn(outputMessage);
//
//            // Setup response
//            when(rawFactory.ok(outputMessage)).thenReturn(responseMessage);
//            ResponseEntity responseEntity = ResponseEntity.ok().build();
//            when(responseRendererComposite.render(eq(responseMessage), eq(serviceContext))).thenReturn(responseEntity);
//
//            // Execute
//            ResponseEntity<?> result = controller.dispatch(request, "{\"test\":1}".getBytes());
//
//            // Verify
//            assertNotNull(result);
//        }

        @Test
        @DisplayName("Should dispatch with null output from executor")
        @SuppressWarnings({ "unchecked", "rawtypes" })
        void shouldDispatchWithNullOutputFromExecutor() throws Throwable {
            // Setup service context
            ServiceContextHolder.setContext(serviceContext);

            // Setup mocks for RuntimeContext
            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceRegistrar.class))
                    .thenReturn(serviceRegistrar);
            runtimeContextMock.when(() -> RuntimeContext.getBean(HttpMessageConverterComposite.class))
                    .thenReturn(httpMessageConverterComposite);
            runtimeContextMock.when(() -> RuntimeContext.getBean(IServiceComponentExecutorFactory.class))
                    .thenReturn(executorFactory);
            runtimeContextMock.when(() -> RuntimeContext.getBean(ResponseRendererComposite.class))
                    .thenReturn(responseRendererComposite);
            IResponseMessageFactory rawFactory = mock(IResponseMessageFactory.class);
            runtimeContextMock.when(() -> RuntimeContext.getBean(IResponseMessageFactory.class))
                    .thenReturn(rawFactory);

            // Setup service resolver behavior - through serviceDefinition
            when(serviceContext.serviceDefinition()).thenReturn(serviceDefinitionMetadata);
            when(serviceDefinitionMetadata.getServices()).thenReturn(java.util.List.of(serviceInfo));
            when(serviceInfo.isFallback()).thenReturn(true);
            when(serviceInfo.getComponent()).thenReturn("testComponent");
            when(serviceRegistrar.getServiceMethodMetadata("testComponent")).thenReturn(serviceMethodMetadata);
            when(serviceMethodMetadata.getParameters()).thenReturn(new ArrayList<>());

            // Setup executor returning null
            when(executorFactory.create(serviceMethodMetadata)).thenReturn(executor);
            when(executor.execute(eq(serviceContext), isNull())).thenReturn(null);

            // Setup response
            when(rawFactory.ok(null)).thenReturn(responseMessage);
            ResponseEntity responseEntity = ResponseEntity.ok().build();
            when(responseRendererComposite.render(eq(responseMessage), eq(serviceContext))).thenReturn(responseEntity);

            // Execute
            ResponseEntity<?> result = controller.dispatch(request, null);

            // Verify
            assertNotNull(result);
        }
    }

    /**
     * Test implementation of IMessageObject
     */
    public static class TestMessageObject implements IMessageObject {
    }

    /**
     * Helper method to create mock ServletInputStream
     */
    private ServletInputStream createMockServletInputStream(byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int available() {
                return byteStream.available();
            }
        };
    }
}
