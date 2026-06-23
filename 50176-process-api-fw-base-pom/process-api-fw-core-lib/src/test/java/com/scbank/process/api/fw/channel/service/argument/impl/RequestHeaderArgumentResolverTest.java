package com.scbank.process.api.fw.channel.service.argument.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ValueConstants;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;

import jakarta.servlet.http.HttpServletRequest;

/**
 * RequestHeaderArgumentResolver Test Class
 */
@ExtendWith(MockitoExtension.class)
class RequestHeaderArgumentResolverTest {

    @Mock
    private ParameterMetadata parameterMetadata;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private HttpServletRequest request;

    private RequestHeaderArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new RequestHeaderArgumentResolver();
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should return true when parameter has RequestHeader annotation")
        void shouldReturnTrueWhenHasRequestHeaderAnnotation() {
            when(parameterMetadata.hasAnnotation(RequestHeader.class)).thenReturn(true);

            boolean result = resolver.supports(parameterMetadata);

            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when parameter does not have RequestHeader annotation")
        void shouldReturnFalseWhenNoRequestHeaderAnnotation() {
            when(parameterMetadata.hasAnnotation(RequestHeader.class)).thenReturn(false);

            boolean result = resolver.supports(parameterMetadata);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("resolveArgument tests")
    class ResolveArgumentTests {

        @Test
        @DisplayName("Should resolve header value by annotation name")
        void shouldResolveHeaderValueByAnnotationName() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Custom-Header", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Custom-Header")).thenReturn("HeaderValue");
            when(parameterMetadata.getType()).thenReturn((Class) String.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals("HeaderValue", result);
        }

        @Test
        @DisplayName("Should resolve header value by annotation value when name is empty")
        void shouldResolveHeaderValueByAnnotationValue() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("", "X-Value-Header", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Value-Header")).thenReturn("ValueHeader");
            when(parameterMetadata.getType()).thenReturn((Class) String.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals("ValueHeader", result);
        }

        @Test
        @DisplayName("Should use parameter name when annotation name and value are empty")
        void shouldUseParameterNameWhenAnnotationEmpty() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(parameterMetadata.getName()).thenReturn("myHeader");
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("myHeader")).thenReturn("MyHeaderValue");
            when(parameterMetadata.getType()).thenReturn((Class) String.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals("MyHeaderValue", result);
        }

        @Test
        @DisplayName("Should return default value when header is missing and default is set")
        void shouldReturnDefaultValueWhenHeaderMissing() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Missing", "", "", false, "defaultValue");
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Missing")).thenReturn(null);
            when(parameterMetadata.getType()).thenReturn((Class) String.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals("defaultValue", result);
        }

        @Test
        @DisplayName("Should return null when header is missing and not required")
        void shouldReturnNullWhenHeaderMissingAndNotRequired() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Optional", "", "", false, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Optional")).thenReturn(null);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertNull(result);
        }

        @Test
        @DisplayName("Should throw exception when required header is missing")
        void shouldThrowExceptionWhenRequiredHeaderMissing() {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Required", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Required")).thenReturn(null);

            assertThrows(IllegalArgumentException.class,
                    () -> resolver.resolveArgument(parameterMetadata, serviceContext, null));
        }

        @Test
        @DisplayName("Should convert to Integer type")
        void shouldConvertToIntegerType() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Int", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Int")).thenReturn("42");
            when(parameterMetadata.getType()).thenReturn((Class) Integer.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals(42, result);
        }

        @Test
        @DisplayName("Should convert to primitive int type")
        void shouldConvertToPrimitiveIntType() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Int", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Int")).thenReturn("100");
            when(parameterMetadata.getType()).thenReturn((Class) int.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals(100, result);
        }

        @Test
        @DisplayName("Should convert to Long type")
        void shouldConvertToLongType() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Long", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Long")).thenReturn("9999999999");
            when(parameterMetadata.getType()).thenReturn((Class) Long.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals(9999999999L, result);
        }

        @Test
        @DisplayName("Should convert to Boolean type")
        void shouldConvertToBooleanType() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Bool", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Bool")).thenReturn("true");
            when(parameterMetadata.getType()).thenReturn((Class) Boolean.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals(true, result);
        }

        @Test
        @DisplayName("Should convert to Double type")
        void shouldConvertToDoubleType() throws Exception {
            RequestHeader annotation = createRequestHeaderAnnotation("X-Double", "", "", true, ValueConstants.DEFAULT_NONE);
            when(parameterMetadata.getAnnotation(RequestHeader.class)).thenReturn(annotation);
            when(serviceContext.request()).thenReturn(request);
            when(request.getHeader("X-Double")).thenReturn("3.14");
            when(parameterMetadata.getType()).thenReturn((Class) Double.class);

            Object result = resolver.resolveArgument(parameterMetadata, serviceContext, null);

            assertEquals(3.14, result);
        }
    }

    /**
     * Helper method to create RequestHeader annotation mock
     */
    private RequestHeader createRequestHeaderAnnotation(String name, String value, String defaultValue,
                                                         boolean required, String defaultValueConstant) {
        return new RequestHeader() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestHeader.class;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public boolean required() {
                return required;
            }

            @Override
            public String defaultValue() {
                return defaultValueConstant;
            }
        };
    }
}
