package com.scbank.process.api.fw.channel.service.argument;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;

/**
 * ServiceMethodArgumentResolverComposite Test Class
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceMethodArgumentResolverCompositeTest {

    @Mock
    private IServiceMethodArgumentResolver resolver1;

    @Mock
    private IServiceMethodArgumentResolver resolver2;

    @Mock
    private IServiceContext serviceContext;

    @Mock
    private ParameterMetadata param1;

    @Mock
    private ParameterMetadata param2;

    @Mock
    private ParameterMetadata param3;

    private ServiceMethodArgumentResolverComposite composite;

    @BeforeEach
    void setUp() {
        composite = new ServiceMethodArgumentResolverComposite(Arrays.asList(resolver1, resolver2));
    }

    @Nested
    @DisplayName("resolveArguments tests")
    class ResolveArgumentsTests {

        @Test
        @DisplayName("Should resolve arguments using matching resolver")
        void shouldResolveArgumentsUsingMatchingResolver() throws Exception {
            Object inputObject = new Object();
            Object resolvedArg1 = "resolved1";
            Object resolvedArg2 = "resolved2";

            when(param1.getName()).thenReturn("param1");
            when(param2.getName()).thenReturn("param2");
            when(resolver1.supports(param1)).thenReturn(true);
            when(resolver1.supports(param2)).thenReturn(false);
            when(resolver2.supports(param2)).thenReturn(true);
            when(resolver1.resolveArgument(param1, serviceContext, inputObject)).thenReturn(resolvedArg1);
            when(resolver2.resolveArgument(param2, serviceContext, inputObject)).thenReturn(resolvedArg2);

            List<ParameterMetadata> parameters = Arrays.asList(param1, param2);
            Object[] result = composite.resolveArguments(parameters, serviceContext, inputObject);

            assertEquals(2, result.length);
            assertEquals(resolvedArg1, result[0]);
            assertEquals(resolvedArg2, result[1]);
        }

        @Test
        @DisplayName("Should return null for unsupported parameter")
        void shouldReturnNullForUnsupportedParameter() throws Exception {
            when(param1.getName()).thenReturn("param1");
            when(resolver1.supports(param1)).thenReturn(false);
            when(resolver2.supports(param1)).thenReturn(false);

            List<ParameterMetadata> parameters = List.of(param1);
            Object[] result = composite.resolveArguments(parameters, serviceContext, null);

            assertEquals(1, result.length);
            assertNull(result[0]);
        }

        @Test
        @DisplayName("Should use first matching resolver")
        void shouldUseFirstMatchingResolver() throws Exception {
            Object resolvedArg = "first resolver";

            when(param1.getName()).thenReturn("param1");
            when(resolver1.supports(param1)).thenReturn(true);
            when(resolver1.resolveArgument(param1, serviceContext, null)).thenReturn(resolvedArg);

            List<ParameterMetadata> parameters = List.of(param1);
            Object[] result = composite.resolveArguments(parameters, serviceContext, null);

            assertEquals(resolvedArg, result[0]);
            verify(resolver2, never()).supports(any());
        }

        @Test
        @DisplayName("Should handle empty parameter list")
        void shouldHandleEmptyParameterList() throws Exception {
            Object[] result = composite.resolveArguments(Collections.emptyList(), serviceContext, null);

            assertEquals(0, result.length);
        }

        @Test
        @DisplayName("Should handle multiple parameters")
        void shouldHandleMultipleParameters() throws Exception {
            Object input = "input";
            Object arg1 = "arg1";
            Object arg2 = "arg2";
            Object arg3 = "arg3";

            when(param1.getName()).thenReturn("p1");
            when(param2.getName()).thenReturn("p2");
            when(param3.getName()).thenReturn("p3");

            when(resolver1.supports(param1)).thenReturn(true);
            when(resolver1.supports(param2)).thenReturn(true);
            when(resolver1.supports(param3)).thenReturn(true);

            when(resolver1.resolveArgument(param1, serviceContext, input)).thenReturn(arg1);
            when(resolver1.resolveArgument(param2, serviceContext, input)).thenReturn(arg2);
            when(resolver1.resolveArgument(param3, serviceContext, input)).thenReturn(arg3);

            List<ParameterMetadata> parameters = Arrays.asList(param1, param2, param3);
            Object[] result = composite.resolveArguments(parameters, serviceContext, input);

            assertEquals(3, result.length);
            assertEquals(arg1, result[0]);
            assertEquals(arg2, result[1]);
            assertEquals(arg3, result[2]);
        }

        @Test
        @DisplayName("Should pass null input to resolver")
        void shouldPassNullInputToResolver() throws Exception {
            when(param1.getName()).thenReturn("param1");
            when(resolver1.supports(param1)).thenReturn(true);
            when(resolver1.resolveArgument(param1, serviceContext, null)).thenReturn("resolved");

            List<ParameterMetadata> parameters = List.of(param1);
            Object[] result = composite.resolveArguments(parameters, serviceContext, null);

            verify(resolver1).resolveArgument(param1, serviceContext, null);
            assertEquals("resolved", result[0]);
        }
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create composite with empty resolver list")
        void shouldCreateWithEmptyResolverList() throws Exception {
            ServiceMethodArgumentResolverComposite emptyComposite =
                    new ServiceMethodArgumentResolverComposite(Collections.emptyList());

            when(param1.getName()).thenReturn("param1");
            Object[] result = emptyComposite.resolveArguments(List.of(param1), serviceContext, null);

            assertEquals(1, result.length);
            assertNull(result[0]);
        }

        @Test
        @DisplayName("Should create composite with single resolver")
        void shouldCreateWithSingleResolver() throws Exception {
            ServiceMethodArgumentResolverComposite singleComposite =
                    new ServiceMethodArgumentResolverComposite(List.of(resolver1));

            when(param1.getName()).thenReturn("param1");
            when(resolver1.supports(param1)).thenReturn(true);
            when(resolver1.resolveArgument(param1, serviceContext, null)).thenReturn("value");

            Object[] result = singleComposite.resolveArguments(List.of(param1), serviceContext, null);

            assertEquals("value", result[0]);
        }
    }
}
