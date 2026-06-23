package com.scbank.process.api.fw.integration.client.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import feign.FeignException.FeignClientException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;

/**
 * FeignFilterChain Test Class
 */
@ExtendWith(MockitoExtension.class)
class FeignFilterChainTest {

	@Mock
	private FeignFilter mockFilter1;

	@Mock
	private FeignFilter mockFilter2;

	@Mock
	private RequestTemplate mockTemplate;

	@Mock
	private FeignFilterContext mockContext;

	private FeignFilterChain filterChain;

	@BeforeEach
	void setUp() {
		filterChain = new FeignFilterChain(List.of(mockFilter1, mockFilter2));
	}

	@Nested
	@DisplayName("constructor tests")
	class ConstructorTests {

		@Test
		@DisplayName("Should create chain with filters")
		void shouldCreateChainWithFilters() {
			FeignFilterChain chain = new FeignFilterChain(List.of(mockFilter1));
			assertNotNull(chain);
		}

		@Test
		@DisplayName("Should create chain with empty list")
		void shouldCreateChainWithEmptyList() {
			FeignFilterChain chain = new FeignFilterChain(List.of());
			assertNotNull(chain);
		}
	}

	@Nested
	@DisplayName("applyOnTemplate tests")
	class ApplyOnTemplateTests {

		@Test
		@DisplayName("Should call onTemplate on all filters")
		void shouldCallOnTemplateOnAllFilters() {
			filterChain.applyOnTemplate(mockTemplate, mockContext);

			verify(mockFilter1).onTemplate(mockTemplate, mockContext);
			verify(mockFilter2).onTemplate(mockTemplate, mockContext);
		}

		@Test
		@DisplayName("Should call filters in order")
		void shouldCallFiltersInOrder() {
			filterChain.applyOnTemplate(mockTemplate, mockContext);

			InOrder inOrder = inOrder(mockFilter1, mockFilter2);
			inOrder.verify(mockFilter1).onTemplate(mockTemplate, mockContext);
			inOrder.verify(mockFilter2).onTemplate(mockTemplate, mockContext);
		}

		@Test
		@DisplayName("Should handle empty filter list")
		void shouldHandleEmptyFilterList() {
			FeignFilterChain emptyChain = new FeignFilterChain(List.of());

			assertDoesNotThrow(() -> emptyChain.applyOnTemplate(mockTemplate, mockContext));
		}
	}

	@Nested
	@DisplayName("applyBeforeRequest tests")
	class ApplyBeforeRequestTests {

		@Test
		@DisplayName("Should call beforeExecute on all filters")
		void shouldCallBeforeExecuteOnAllFilters() {
			when(mockFilter1.beforeExecute(mockTemplate, mockContext)).thenReturn(mockTemplate);
			when(mockFilter2.beforeExecute(mockTemplate, mockContext)).thenReturn(mockTemplate);

			RequestTemplate result = filterChain.applyBeforeRequest(mockTemplate, mockContext);

			assertNotNull(result);
			verify(mockFilter1).beforeExecute(mockTemplate, mockContext);
			verify(mockFilter2).beforeExecute(any(RequestTemplate.class), eq(mockContext));
		}

		@Test
		@DisplayName("Should return transformed template")
		void shouldReturnTransformedTemplate() {
			RequestTemplate transformedTemplate = mock(RequestTemplate.class);
			when(mockFilter1.beforeExecute(mockTemplate, mockContext)).thenReturn(transformedTemplate);
			when(mockFilter2.beforeExecute(transformedTemplate, mockContext)).thenReturn(transformedTemplate);

			RequestTemplate result = filterChain.applyBeforeRequest(mockTemplate, mockContext);

			assertEquals(transformedTemplate, result);
		}
	}

	@Nested
	@DisplayName("applyOnError tests")
	class ApplyOnErrorTests {

		@Test
		@DisplayName("Should call onError on all filters")
		void shouldCallOnErrorOnAllFilters() {
			IOException exception = new IOException("Test error");

			filterChain.applyOnError(exception, mockContext);

			verify(mockFilter1).onError(exception, mockContext);
			verify(mockFilter2).onError(exception, mockContext);
		}

		@Test
		@DisplayName("Should return original exception")
		void shouldReturnOriginalException() {
			IOException exception = new IOException("Test error");

			IOException result = filterChain.applyOnError(exception, mockContext);

			assertSame(exception, result);
		}
	}

	@Nested
	@DisplayName("rebody static method tests")
	class RebodyTests {

		@Test
		@DisplayName("Should create new response with updated body")
		void shouldCreateNewResponseWithUpdatedBody() {
			byte[] newBody = "new body content".getBytes();
			Map<String, Collection<String>> headers = new HashMap<>();
			headers.put("Content-Type", List.of("text/plain"));

			Request mockRequest = mock(Request.class);
			Response originalResponse = Response.builder().status(200).reason("OK").request(mockRequest)
					.headers(headers).body(new ByteArrayInputStream("original".getBytes()), 8).build();

			Response result = FeignFilterChain.rebody(originalResponse, newBody, "application/json");

			assertNotNull(result);
			assertEquals(200, result.status());
			assertEquals("OK", result.reason());
		}

		@Test
		@DisplayName("Should update Content-Length header")
		void shouldUpdateContentLengthHeader() {
			byte[] newBody = "test body".getBytes();
			Map<String, Collection<String>> headers = new HashMap<>();

			Request mockRequest = mock(Request.class);
			Response originalResponse = Response.builder().status(200).reason("OK").request(mockRequest)
					.headers(headers).body(new ByteArrayInputStream(new byte[0]), 0).build();

			Response result = FeignFilterChain.rebody(originalResponse, newBody, "text/plain");

			assertTrue(result.headers().containsKey("Content-Length"));
			assertEquals(String.valueOf(newBody.length), result.headers().get("Content-Length").iterator().next());
		}

		@Test
		@DisplayName("Should handle null content type")
		void shouldHandleNullContentType() {
			byte[] newBody = "body".getBytes();
			Map<String, Collection<String>> headers = new HashMap<>();

			Request mockRequest = mock(Request.class);
			Response originalResponse = Response.builder().status(200).reason("OK").request(mockRequest)
					.headers(headers).body(new ByteArrayInputStream(new byte[0]), 0).build();

			Response result = FeignFilterChain.rebody(originalResponse, newBody, null);

			assertNotNull(result);
			assertFalse(result.headers().containsKey("Content-Type"));
		}
	}

	@Test
	void applyOnTemplate_callsAllFiltersInOrder() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilter f2 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);

		FeignFilterChain chain = new FeignFilterChain(List.of(f1, f2));
		RequestTemplate template = new RequestTemplate();

		chain.applyOnTemplate(template, ctx);

		InOrder inOrder = inOrder(f1, f2);
		inOrder.verify(f1).onTemplate(same(template), same(ctx));
		inOrder.verify(f2).onTemplate(same(template), same(ctx));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void applyBeforeRequest_chainsReturnedTemplates() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilter f2 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);

		RequestTemplate t0 = new RequestTemplate();
		RequestTemplate t1 = new RequestTemplate();
		RequestTemplate t2 = new RequestTemplate();

		when(f1.beforeExecute(same(t0), same(ctx))).thenReturn(t1);
		when(f2.beforeExecute(same(t1), same(ctx))).thenReturn(t2);

		FeignFilterChain chain = new FeignFilterChain(List.of(f1, f2));

		RequestTemplate result = chain.applyBeforeRequest(t0, ctx);

		assertSame(t2, result);

		InOrder inOrder = inOrder(f1, f2);
		inOrder.verify(f1).beforeExecute(same(t0), same(ctx));
		inOrder.verify(f2).beforeExecute(same(t1), same(ctx));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void applyOnError_callsAllFiltersAndReturnsSameException() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilter f2 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);

		FeignFilterChain chain = new FeignFilterChain(List.of(f1, f2));
		IOException ex = new IOException("boom");

		IOException result = chain.applyOnError(ex, ctx);

		assertSame(ex, result);

		InOrder inOrder = inOrder(f1, f2);
		inOrder.verify(f1).onError(same(ex), same(ctx));
		inOrder.verify(f2).onError(same(ex), same(ctx));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	void applyAfterResponse_whenCtxHasNoBytes_readsBody_setsCtx_thenRunsFiltersReverse_andrebodiesLatest()
			throws Exception {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilter f2 = mock(FeignFilter.class);

		FeignFilterContext ctx = mock(FeignFilterContext.class);
		AtomicReference<byte[]> bytesRef = new AtomicReference<>(null);
		AtomicReference<String> ctRef = new AtomicReference<>(null);

		when(ctx.getResponseBytese()).thenAnswer(inv -> bytesRef.get());
		when(ctx.getResponseContentType()).thenAnswer(inv -> ctRef.get());

		doAnswer(inv -> {
			bytesRef.set(inv.getArgument(0));
			return null;
		}).when(ctx).setResponseBytese(any(byte[].class));

		doAnswer(inv -> {
			ctRef.set(inv.getArgument(0));
			return null;
		}).when(ctx).setResponseContentType(anyString());
		
		Response initial = responseWithBody("abc".getBytes(), "application/json");
		
		when(f2.afterResponse(any(Response.class), same(ctx)))
			.thenAnswer(inv -> responseWithBody("xyz".getBytes(), "application/json"));
		
		when(f1.afterResponse(any(Response.class), same(ctx)))
		.thenAnswer(inv -> inv.getArgument(0));
		
		FeignFilterChain chain = new FeignFilterChain(List.of(f1, f2));
		
		Response out = chain.applyAfterResponse(initial, ctx);
		
		assertArrayEquals("abc".getBytes(), bytesRef.get());
		assertEquals("application/json", ctRef.get());
		
		byte[] outBytes = Util.toByteArray(Objects.requireNonNull(out.body()).asInputStream());
		assertArrayEquals("xyz".getBytes(), outBytes);
		
		InOrder inOrder = inOrder(f1, f2);
		inOrder.verify(f2).afterResponse(any(Response.class), same(ctx));
		inOrder.verify(f1).afterResponse(any(Response.class), same(ctx));
	}
	
//	@Test
//	void applyAfterResponse_whenFilterReturnsNullBody_skipsSecondRebodyBranch() throws Exception {
//		FeignFilter f1 = mock(FeignFilter.class);
//		FeignFilterContext ctx = mock(FeignFilterContext.class);
//		
//		Response initial = responseWithBody("abc".getBytes(), "application/json");
//		
//		when(f1.afterResponse(any(Response.class), same(ctx)))
//			.thenAnswer(inv -> Response.builder()
//					.status(200)
//					.reason("OK")
//					.request(initial.request())
//					.headers(initial.headers())
//					.body((Response.Body)null)
//					.build());
//		
//		FeignFilterChain chain = new FeignFilterChain(List.of(f1));
//		
//		Response out = chain.applyAfterResponse(initial, ctx);
//		
//		assertNull(out.body());
//		verify(f1).afterResponse(any(Response.class), same(ctx));
//	}
	
	@Test
	void applyAfterResponse_rethrowsFeignClientException() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);
		when(ctx.getResponseBytese()).thenReturn("abc".getBytes());
		
		Response initial = responseWithBody("abc".getBytes(), "application/json");
		
		FeignClientException ex = mock(FeignClientException.class);
		when(f1.afterResponse(any(Response.class), same(ctx))).thenThrow(ex);
		
		FeignFilterChain chain = new FeignFilterChain(List.of(f1));
		
		FeignClientException thrown = assertThrows(FeignClientException.class, () -> chain.applyAfterResponse(initial, ctx));
		assertSame(ex, thrown);
	}
	
	@Test
	void applyAfterResponse_rethrowsFrameworkRuntimeException() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);
		when(ctx.getResponseBytese()).thenReturn("abc".getBytes());
		
		Response initial = responseWithBody("abc".getBytes(), "application/json");
		
		FrameworkRuntimeException ex = mock(FrameworkRuntimeException.class);
		when(f1.afterResponse(any(Response.class), same(ctx))).thenThrow(ex);
		
		FeignFilterChain chain = new FeignFilterChain(List.of(f1));
		
		FrameworkRuntimeException thrown = assertThrows(FrameworkRuntimeException.class, () -> chain.applyAfterResponse(initial, ctx));
		assertSame(ex, thrown);
	}
	
	@Test
	void applyAfterResponse_wrapOtherExceptionAsFrameworkRuntimeExceptionInternalError() {
		FeignFilter f1 = mock(FeignFilter.class);
		FeignFilterContext ctx = mock(FeignFilterContext.class);
		when(ctx.getResponseBytese()).thenReturn("abc".getBytes());
		
		Response initial = responseWithBody("abc".getBytes(), "application/json");
		
		RuntimeException boom = new RuntimeException("boom");
		when(f1.afterResponse(any(Response.class), same(ctx))).thenThrow(boom);
		
		FeignFilterChain chain = new FeignFilterChain(List.of(f1));
		
		FrameworkRuntimeException thrown = assertThrows(FrameworkRuntimeException.class, () -> chain.applyAfterResponse(initial, ctx));
		assertSame(boom, thrown.getCause());
	}

	private static Response responseWithBody(byte[] body, String contentType) {
		Map<String, Collection<String>> headers = new LinkedHashMap<>();
		if (contentType != null) {
			headers.put("Content-Type", List.of(contentType));
		}

		Request req = Request.create(Request.HttpMethod.GET, "http:/localhost/test", Collections.emptyMap(), null,
				StandardCharsets.UTF_8, new RequestTemplate());
		
		return Response.builder()
				.status(200)
				.reason("OK")
				.request(req)
				.headers(headers)
				.body(new String(body), StandardCharsets.UTF_8)
				.build();
	}
}
