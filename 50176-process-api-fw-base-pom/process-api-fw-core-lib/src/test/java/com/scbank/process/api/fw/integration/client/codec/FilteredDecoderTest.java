package com.scbank.process.api.fw.integration.client.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.integration.client.filter.FeignFilterChain;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext;
import com.scbank.process.api.fw.integration.client.filter.FeignFilterContext.FeignFilterContextHolder;

import feign.Request;
import feign.Response;
import feign.codec.Decoder;

/**
 * FilteredDecoder Test Class
 */
@ExtendWith(MockitoExtension.class)
class FilteredDecoderTest {

    @Mock
    private Decoder mockDecoder;

    @Mock
    private FeignFilterChain mockFilterChain;

    @Mock
    private Type mockType;

    private FilteredDecoder filteredDecoder;

    @BeforeEach
    void setUp() {
        filteredDecoder = new FilteredDecoder(mockDecoder, mockFilterChain);
    }

    @AfterEach
    void tearDown() {
        FeignFilterContextHolder.clear();
    }

    @Nested
    @DisplayName("decode tests")
    class DecodeTests {

        @Test
        @DisplayName("Should apply filter chain and then decode")
        void shouldApplyFilterChainAndThenDecode() throws Exception {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);

            Request mockRequest = mock(Request.class);
            Map<String, java.util.Collection<String>> headers = new HashMap<>();
            Response originalResponse = Response.builder()
                    .status(200)
                    .reason("OK")
                    .request(mockRequest)
                    .headers(headers)
                    .body(new ByteArrayInputStream("test".getBytes()), 4)
                    .build();

            when(mockFilterChain.applyAfterResponse(any(Response.class), eq(ctx)))
                    .thenReturn(originalResponse);
            when(mockDecoder.decode(any(Response.class), eq(mockType)))
                    .thenReturn("decoded result");

            Object result = filteredDecoder.decode(originalResponse, mockType);

            assertEquals("decoded result", result);
            verify(mockFilterChain).applyAfterResponse(any(Response.class), eq(ctx));
            verify(mockDecoder).decode(any(Response.class), eq(mockType));
        }

        @Test
        @DisplayName("Should pass filtered response to decoder")
        void shouldPassFilteredResponseToDecoder() throws Exception {
            FeignFilterContext ctx = new FeignFilterContext();
            FeignFilterContextHolder.set(ctx);

            Request mockRequest = mock(Request.class);
            Map<String, java.util.Collection<String>> headers = new HashMap<>();
            Response originalResponse = Response.builder()
                    .status(200)
                    .reason("OK")
                    .request(mockRequest)
                    .headers(headers)
                    .body(new ByteArrayInputStream("original".getBytes()), 8)
                    .build();

            Response filteredResponse = Response.builder()
                    .status(200)
                    .reason("OK")
                    .request(mockRequest)
                    .headers(headers)
                    .body(new ByteArrayInputStream("filtered".getBytes()), 8)
                    .build();

            when(mockFilterChain.applyAfterResponse(any(Response.class), eq(ctx)))
                    .thenReturn(filteredResponse);
            when(mockDecoder.decode(eq(filteredResponse), eq(mockType)))
                    .thenReturn("filtered result");

            Object result = filteredDecoder.decode(originalResponse, mockType);

            assertEquals("filtered result", result);
        }

        @Test
        @DisplayName("Should use context from holder")
        void shouldUseContextFromHolder() throws Exception {
            FeignFilterContext ctx = new FeignFilterContext();
            ctx.setSystemId("MCI");
            FeignFilterContextHolder.set(ctx);

            Request mockRequest = mock(Request.class);
            Map<String, java.util.Collection<String>> headers = new HashMap<>();
            Response response = Response.builder()
                    .status(200)
                    .reason("OK")
                    .request(mockRequest)
                    .headers(headers)
                    .body(new ByteArrayInputStream("test".getBytes()), 4)
                    .build();

            when(mockFilterChain.applyAfterResponse(any(Response.class), eq(ctx)))
                    .thenReturn(response);
            when(mockDecoder.decode(any(Response.class), eq(mockType)))
                    .thenReturn("result");

            filteredDecoder.decode(response, mockType);

            verify(mockFilterChain).applyAfterResponse(any(Response.class), eq(ctx));
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement Decoder interface")
        void shouldImplementDecoderInterface() {
            assertTrue(filteredDecoder instanceof Decoder);
        }
    }
}
