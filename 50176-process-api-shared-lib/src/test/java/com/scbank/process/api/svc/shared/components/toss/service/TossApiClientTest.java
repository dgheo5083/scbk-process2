package com.scbank.process.api.svc.shared.components.toss.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.toss.model.TossHttpRequestEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class TossApiClientTest {

	@Mock private BaseHttpClient httpClient;

	private TossApiClient client;

	@org.junit.jupiter.api.BeforeEach
	void setUp() {
		client = new TossApiClient(httpClient);
	}

	@Test
	@DisplayName("init - 자기 자신 반환")
	public void initTest() {
		assertNotNull(client.init());
	}

	@Test
	@DisplayName("execute - 미지원 METHOD 시 PRCServiceException")
	public void executeUnsupportedTest() {
		TossHttpRequestEntity entity = mock(TossHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("PUT");

		assertThrows(PRCServiceException.class, () -> client.execute(entity));
	}

	@Test
	@DisplayName("execute - GET 통신 실패 시 MA999")
	public void executeGetFailTest() {
		TossHttpRequestEntity entity = mock(TossHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("GET");
		when(entity.getUrl()).thenReturn("http://toss/api");
		when(entity.getBodyParameters()).thenReturn(new JSONObject());
		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("toss fail"));

		assertThrows(PRCServiceException.class, () -> client.execute(entity));
	}

	@Test
	@DisplayName("convertParameter - JSONObject를 NameValuePair 목록으로 변환")
	public void convertParameterTest() {
		JSONObject params = new JSONObject();
		params.put("a", "1");
		params.put("b", "2");

		assertEquals(2, client.convertParameter(params).size());
	}
}
