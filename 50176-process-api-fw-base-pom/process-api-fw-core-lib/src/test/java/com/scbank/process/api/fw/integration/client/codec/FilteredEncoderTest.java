package com.scbank.process.api.fw.integration.client.codec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.RequestTemplate;
import feign.codec.Encoder;

/**
 * FilteredEncoder Test Class
 */
@ExtendWith(MockitoExtension.class)
class FilteredEncoderTest {

    @Mock
    private Encoder mockEncoder;

    @Mock
    private FeignFilterChain mockFilterChain;

    @Mock
    private RequestTemplate mockTemplate;

    @Mock
    private Type mockBodyType;

    private FilteredEncoder filteredEncoder;

    @BeforeEach
    void setUp() {
        filteredEncoder = new FilteredEncoder(mockEncoder, mockFilterChain);
    }

    @AfterEach
    void tearDown() {
        FeignFilterContextHolder.clear();
    }

    @Nested
    @DisplayName("encode tests")
    class EncodeTests {

        @Test
        @DisplayName("Should encode first then apply filter chain")
        void shouldEncodeFirstThenApplyFilterChain() {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);

            Object requestObject = new Object();

            filteredEncoder.encode(requestObject, mockBodyType, mockTemplate);

            InOrder inOrder = inOrder(mockEncoder, mockFilterChain);
            inOrder.verify(mockEncoder).encode(requestObject, mockBodyType, mockTemplate);
            inOrder.verify(mockFilterChain).applyBeforeRequest(mockTemplate, ctx);
        }

        @Test
        @DisplayName("Should use context from holder")
        void shouldUseContextFromHolder() {
            FeignFilterContext ctx = new FeignFilterContext();
            ctx.setSystemId("MCI");
            FeignFilterContextHolder.set(ctx);

            Object requestObject = "test request";

            filteredEncoder.encode(requestObject, mockBodyType, mockTemplate);

            verify(mockFilterChain).applyBeforeRequest(mockTemplate, ctx);
        }

        @Test
        @DisplayName("Should delegate encoding to wrapped encoder")
        void shouldDelegateEncodingToWrappedEncoder() {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);

            String requestObject = "test object";

            filteredEncoder.encode(requestObject, mockBodyType, mockTemplate);

            verify(mockEncoder).encode(requestObject, mockBodyType, mockTemplate);
        }

        @Test
        @DisplayName("Should handle null context from holder")
        void shouldHandleNullContextFromHolder() {
            FeignFilterContextHolder.clear();

            Object requestObject = new Object();

            assertDoesNotThrow(() ->
                    filteredEncoder.encode(requestObject, mockBodyType, mockTemplate));

            verify(mockEncoder).encode(requestObject, mockBodyType, mockTemplate);
            verify(mockFilterChain).applyBeforeRequest(eq(mockTemplate), isNull());
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement Encoder interface")
        void shouldImplementEncoderInterface() {
            assertTrue(filteredEncoder instanceof Encoder);
        }
    }
}
