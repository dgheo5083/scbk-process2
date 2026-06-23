package com.scbank.process.api.svc.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ParameterInfo;

/**
 * {@link ServiceContextUtils} 서비스 파라미터 조회 커버리지 테스트.
 */
@DisplayName("ServiceContextUtils")
class ServiceContextUtilsTest {

	private IServiceContext ctxWithParams(List<ParameterInfo> params) {
		IServiceContext sCtx = mock(IServiceContext.class);
		ServiceDefinitionMetadata def = mock(ServiceDefinitionMetadata.class);
		when(sCtx.serviceDefinition()).thenReturn(def);
		when(def.getParameters()).thenReturn(params);
		return sCtx;
	}

	@DisplayName("getServiceParameterMap(ctx) : serviceDefinition 이 null 이면 빈 맵")
	@Test
	void getAll_nullDefinition() {
		IServiceContext sCtx = mock(IServiceContext.class);
		when(sCtx.serviceDefinition()).thenReturn(null);

		assertTrue(ServiceContextUtils.getServiceParameterMap(sCtx).isEmpty());
	}

	@DisplayName("getServiceParameterMap(ctx) : 파라미터가 비어있으면 빈 맵")
	@Test
	void getAll_emptyParameters() {
		IServiceContext sCtx = ctxWithParams(List.of());

		assertTrue(ServiceContextUtils.getServiceParameterMap(sCtx).isEmpty());
	}

	@DisplayName("getServiceParameterMap(ctx) : 전체 파라미터 맵 반환")
	@Test
	void getAll_returnsMap() {
		IServiceContext sCtx = ctxWithParams(List.of(
				new ParameterInfo("k1", "v1"),
				new ParameterInfo("k2", "v2")));

		Map<String, String> result = ServiceContextUtils.getServiceParameterMap(sCtx);

		assertEquals(2, result.size());
		assertEquals("v1", result.get("k1"));
		assertEquals("v2", result.get("k2"));
	}

	@DisplayName("getServiceParameterMap(ctx, names) : names 가 null 이면 빈 맵")
	@Test
	void getFiltered_nullNames() {
		IServiceContext sCtx = mock(IServiceContext.class);

		assertTrue(ServiceContextUtils.getServiceParameterMap(sCtx, (String[]) null).isEmpty());
	}

	@DisplayName("getServiceParameterMap(ctx, names) : names 가 비어있으면 빈 맵")
	@Test
	void getFiltered_emptyNames() {
		IServiceContext sCtx = mock(IServiceContext.class);

		assertTrue(ServiceContextUtils.getServiceParameterMap(sCtx, new String[] {}).isEmpty());
	}

	@DisplayName("getServiceParameterMap(ctx, names) : 전체 파라미터가 비어있으면 빈 맵")
	@Test
	void getFiltered_emptyServiceParameters() {
		IServiceContext sCtx = ctxWithParams(List.of());

		assertTrue(ServiceContextUtils.getServiceParameterMap(sCtx, "k1").isEmpty());
	}

	@DisplayName("getServiceParameterMap(ctx, names) : 지정한 이름만 필터링한다")
	@Test
	void getFiltered_filtersByName() {
		IServiceContext sCtx = ctxWithParams(List.of(
				new ParameterInfo("k1", "v1"),
				new ParameterInfo("k2", "v2"),
				new ParameterInfo("k3", "v3")));

		Map<String, String> result = ServiceContextUtils.getServiceParameterMap(sCtx, "k1", "k3");

		assertEquals(2, result.size());
		assertEquals("v1", result.get("k1"));
		assertEquals("v3", result.get("k3"));
		assertTrue(result.get("k2") == null);
	}
}
