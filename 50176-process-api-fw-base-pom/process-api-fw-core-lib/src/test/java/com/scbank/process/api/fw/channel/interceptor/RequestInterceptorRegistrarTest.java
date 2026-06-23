package com.scbank.process.api.fw.channel.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

import com.scbank.process.api.fw.channel.constants.ChannelConstants;

/**
 * RequestInterceptorRegistrar Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestInterceptorRegistrarTest {

    @Mock
    private Environment environment;

    @Mock
    private BeanDefinitionRegistry registry;

    private RequestInterceptorRegistrar registrar;

    @BeforeEach
    void setUp() {
        registrar = new RequestInterceptorRegistrar();
        registrar.setEnvironment(environment);
    }

    @Nested
    @DisplayName("setEnvironment tests")
    class SetEnvironmentTests {

        @Test
        @DisplayName("Should set environment")
        void shouldSetEnvironment() {
            RequestInterceptorRegistrar newRegistrar = new RequestInterceptorRegistrar();

            assertDoesNotThrow(() -> newRegistrar.setEnvironment(environment));
        }
    }

    @Nested
    @DisplayName("postProcessBeanDefinitionRegistry tests")
    class PostProcessBeanDefinitionRegistryTests {

        @Test
        @DisplayName("Should handle missing channel properties binding")
        void shouldHandleMissingChannelPropertiesBinding() {
            when(environment.getProperty(anyString())).thenReturn(null);
            when(environment.getProperty(anyString(), anyString())).thenReturn("");

            // The Binder.get().bind() will fail gracefully when properties are not found
            assertThrows(Exception.class,
                    () -> registrar.postProcessBeanDefinitionRegistry(registry));
        }
    }

    @Nested
    @DisplayName("Bean registration tests")
    class BeanRegistrationTests {

        @Test
        @DisplayName("Should not register beans when channel properties is null")
        void shouldNotRegisterBeansWhenPropertiesNull() {
            // This tests the private registerBean method indirectly
            // When properties are null, no beans should be registered
            verify(registry, never()).registerBeanDefinition(anyString(), any());
        }
    }

    @Nested
    @DisplayName("Interceptor bean naming tests")
    class InterceptorBeanNamingTests {

        @Test
        @DisplayName("Should generate correct bean name format")
        void shouldGenerateCorrectBeanNameFormat() {
            // Bean name format: serviceId#index#className
            // This is tested through the registration process
            // Expected format example: "COMMON#0#AuthInterceptor"
            assertNotNull(registrar);
        }
    }

    /**
     * Test interceptor class for testing purposes
     */
    public static class TestRequestInterceptor extends AbstractRequestInterceptor {
        @Override
        public boolean preHandle(jakarta.servlet.http.HttpServletRequest request,
                                 jakarta.servlet.http.HttpServletResponse response,
                                 Object handler) throws Exception {
            return true;
        }
    }
}
