package com.scbank.process.api.fw.channel.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.context.handler.DefaultServiceContextHandler;
import com.scbank.process.api.fw.channel.context.handler.IServiceContextHandler;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.interceptor.IRequestInterceptor;
import com.scbank.process.api.fw.channel.interceptor.IRequestInterceptorRegistrar;
import com.scbank.process.api.fw.channel.interceptor.RequestInterceptorRegistrar;
import com.scbank.process.api.fw.channel.mapping.ServiceRequestMappingRegistrar;
import com.scbank.process.api.fw.channel.service.ServiceEndpointRequests;
import com.scbank.process.api.fw.channel.service.ServiceIdResolver;
import com.scbank.process.api.fw.channel.service.argument.ServiceMethodArgumentResolverComposite;
import com.scbank.process.api.fw.channel.service.executor.IServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.executor.impl.DefaultServiceComponentExecutorFactory;
import com.scbank.process.api.fw.channel.service.interceptor.IServiceInterceptor;
import com.scbank.process.api.fw.channel.service.interceptor.ServiceInterceptorComposite;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;
import com.scbank.process.api.fw.channel.service.registry.impl.ServiceRegistrar;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;

/**
 * ChannelServiceConfigurations Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class ChannelServiceConfigurationsTest {

    private ChannelServiceConfigurations configurations;

    @Mock
    private ChannelProperties channelProperties;

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
    private ServiceEndpointRequests serviceEndpointRequests;

    @Mock
    private IServiceContextHandler serviceContextHandler;

    @Mock
    private ObjectProvider<IRequestInterceptor> interceptorsProvider;

    @Mock
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @BeforeEach
    void setUp() {
        configurations = new ChannelServiceConfigurations();
    }

    @Nested
    @DisplayName("ServiceIdResolver tests")
    class ServiceIdResolverTests {

        @Test
        @DisplayName("Should create ServiceIdResolver")
        void shouldCreateServiceIdResolver() {
            ServiceIdResolver resolver = configurations.serviceIdResolver(channelProperties);

            assertNotNull(resolver);
        }
    }

    @Nested
    @DisplayName("ServiceContextHandler tests")
    class ServiceContextHandlerTests {

        @Test
        @DisplayName("Should create DefaultServiceContextHandler")
        void shouldCreateServiceContextHandler() {
            IServiceContextHandler handler = configurations.serviceContextHandler(
                    identifyGenerator,
                    deviceResolver,
                    serviceRegistrar,
                    localeResolver,
                    serviceIdResolver,
                    sessionContextResolver);

            assertNotNull(handler);
            assertTrue(handler instanceof DefaultServiceContextHandler);
        }
    }

    @Nested
    @DisplayName("ServiceRequestInterceptor tests")
    class ServiceRequestInterceptorTests {

        @Test
        @DisplayName("Should create MappedInterceptor for service requests")
        void shouldCreateServiceRequestInterceptor() {
            when(serviceEndpointRequests.enabledServiceEndpointUrls()).thenReturn(List.of("/api/**"));

            MappedInterceptor interceptor = configurations.serviceRequestInterceptor(
                    serviceEndpointRequests,
                    serviceContextHandler,
                    interceptorsProvider);

            assertNotNull(interceptor);
        }

        @Test
        @DisplayName("Should create MappedInterceptor with multiple URL patterns")
        void shouldCreateServiceRequestInterceptorWithMultiplePatterns() {
            when(serviceEndpointRequests.enabledServiceEndpointUrls())
                    .thenReturn(List.of("/api/v1/**", "/api/v2/**", "/services/**"));

            MappedInterceptor interceptor = configurations.serviceRequestInterceptor(
                    serviceEndpointRequests,
                    serviceContextHandler,
                    interceptorsProvider);

            assertNotNull(interceptor);
        }

        @Test
        @DisplayName("Should create MappedInterceptor with empty URL list")
        void shouldCreateServiceRequestInterceptorWithEmptyUrls() {
            when(serviceEndpointRequests.enabledServiceEndpointUrls()).thenReturn(List.of());

            MappedInterceptor interceptor = configurations.serviceRequestInterceptor(
                    serviceEndpointRequests,
                    serviceContextHandler,
                    interceptorsProvider);

            assertNotNull(interceptor);
        }
    }

    @Nested
    @DisplayName("ServiceRegistrar tests")
    class ServiceRegistrarTests {

        @Test
        @DisplayName("Should create ServiceRegistrar")
        void shouldCreateServiceRegistrar() {
            IServiceRegistrar registrar = configurations.serviceRegistrar();

            assertNotNull(registrar);
            assertTrue(registrar instanceof ServiceRegistrar);
        }
    }

    @Nested
    @DisplayName("ServiceRequestMappingRegistrar tests")
    class ServiceRequestMappingRegistrarTests {

        @Test
        @DisplayName("Should create ServiceRequestMappingRegistrar")
        void shouldCreateServiceRequestMappingRegistrar() {
            ServiceRequestMappingRegistrar registrar = configurations.serviceRequestMappingRegistrar(
                    requestMappingHandlerMapping,
                    channelProperties);

            assertNotNull(registrar);
        }
    }

    @Nested
    @DisplayName("RequestInterceptorRegistrar tests")
    class RequestInterceptorRegistrarTests {

        @Test
        @DisplayName("Should create RequestInterceptorRegistrar")
        void shouldCreateRequestInterceptorRegistrar() {
            IRequestInterceptorRegistrar registrar = configurations.requestInterceptorRegistrar();

            assertNotNull(registrar);
            assertTrue(registrar instanceof RequestInterceptorRegistrar);
        }
    }

    @Nested
    @DisplayName("ServiceEndpointRequests tests")
    class ServiceEndpointRequestsTests {

        @Test
        @DisplayName("Should create ServiceEndpointRequests")
        void shouldCreateServiceEndpointRequests() {
            ServiceEndpointRequests requests = configurations.serviceEndpointRequests(channelProperties);

            assertNotNull(requests);
        }
    }

    @Nested
    @DisplayName("ServiceInterceptorComposite tests")
    class ServiceInterceptorCompositeTests {

        @Test
        @DisplayName("Should create ServiceInterceptorComposite with interceptors")
        void shouldCreateServiceInterceptorComposite() {
            List<IServiceInterceptor> interceptors = new ArrayList<>();
            interceptors.add(mock(IServiceInterceptor.class));

            ServiceInterceptorComposite composite = configurations.serviceInterceptorComposite(interceptors);

            assertNotNull(composite);
        }

        @Test
        @DisplayName("Should create ServiceInterceptorComposite with empty list")
        void shouldCreateServiceInterceptorCompositeWithEmptyList() {
            ServiceInterceptorComposite composite = configurations.serviceInterceptorComposite(List.of());

            assertNotNull(composite);
        }

        @Test
        @DisplayName("Should create ServiceInterceptorComposite with multiple interceptors")
        void shouldCreateServiceInterceptorCompositeWithMultiple() {
            List<IServiceInterceptor> interceptors = List.of(
                    mock(IServiceInterceptor.class),
                    mock(IServiceInterceptor.class),
                    mock(IServiceInterceptor.class));

            ServiceInterceptorComposite composite = configurations.serviceInterceptorComposite(interceptors);

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("ServiceMethodArgumentResolverComposite tests")
    class ServiceMethodArgumentResolverCompositeTests {

        @Test
        @DisplayName("Should create ServiceMethodArgumentResolverComposite with default resolvers")
        void shouldCreateServiceMethodArgumentResolverComposite() {
            ServiceMethodArgumentResolverComposite composite = configurations.serviceMethodArgumentResolverComposite();

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("ServiceComponentExecutorFactory tests")
    class ServiceComponentExecutorFactoryTests {

        @Test
        @DisplayName("Should create DefaultServiceComponentExecutorFactory")
        void shouldCreateServiceComponentExecutorFactory() {
            ServiceMethodArgumentResolverComposite argumentResolverComposite =
                    configurations.serviceMethodArgumentResolverComposite();
            ServiceInterceptorComposite interceptorComposite =
                    configurations.serviceInterceptorComposite(List.of());

            IServiceComponentExecutorFactory factory = configurations.serviceComponentExecutorFactory(
                    argumentResolverComposite,
                    interceptorComposite);

            assertNotNull(factory);
            assertTrue(factory instanceof DefaultServiceComponentExecutorFactory);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should create all service-related beans successfully")
        void shouldCreateAllServiceBeansSuccessfully() {
            // Service ID Resolver
            ServiceIdResolver idResolver = configurations.serviceIdResolver(channelProperties);
            assertNotNull(idResolver);

            // Service Registrar
            IServiceRegistrar registrar = configurations.serviceRegistrar();
            assertNotNull(registrar);

            // Interceptor Registrar
            IRequestInterceptorRegistrar interceptorRegistrar = configurations.requestInterceptorRegistrar();
            assertNotNull(interceptorRegistrar);

            // Argument Resolver Composite
            ServiceMethodArgumentResolverComposite argResolver = configurations.serviceMethodArgumentResolverComposite();
            assertNotNull(argResolver);

            // Service Interceptor Composite
            ServiceInterceptorComposite serviceInterceptorComposite = configurations.serviceInterceptorComposite(List.of());
            assertNotNull(serviceInterceptorComposite);

            // Executor Factory
            IServiceComponentExecutorFactory executorFactory = configurations.serviceComponentExecutorFactory(
                    argResolver, serviceInterceptorComposite);
            assertNotNull(executorFactory);
        }

        @Test
        @DisplayName("Should create context handler with all dependencies")
        void shouldCreateContextHandlerWithAllDependencies() {
            ServiceIdResolver idResolver = configurations.serviceIdResolver(channelProperties);
            IServiceRegistrar registrar = configurations.serviceRegistrar();

            IServiceContextHandler handler = configurations.serviceContextHandler(
                    identifyGenerator,
                    deviceResolver,
                    registrar,
                    localeResolver,
                    idResolver,
                    sessionContextResolver);

            assertNotNull(handler);
        }
    }
}
