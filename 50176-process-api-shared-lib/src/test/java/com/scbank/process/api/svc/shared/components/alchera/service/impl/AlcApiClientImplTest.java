package com.scbank.process.api.svc.shared.components.alchera.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.alchera.model.AlcHttpRequestEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class AlcApiClientImplTest {

	@Mock private BaseHttpClient httpClient;

	@InjectMocks private AlcApiClientImpl client;

	@Test
	@DisplayName("execute - 미지원 METHOD(GET) 시 ERR_MA005")
	public void unsupportedMethodTest() {
		AlcHttpRequestEntity entity = mock(AlcHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("GET");

		assertThrows(PRCServiceException.class, () -> client.execute(entity));
	}

	@Test
	@DisplayName("execute - POST 통신 실패 시 PRCServiceException")
	public void postFailTest() {
		AlcHttpRequestEntity entity = mock(AlcHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("POST");
		when(entity.getUrl()).thenReturn("http://alchera/api");
		when(entity.getBodyParameters()).thenReturn(new JSONObject());
		when(httpClient.execute(any(ClassicHttpRequest.class))).thenThrow(new RuntimeException("alc fail"));

		assertThrows(PRCServiceException.class, () -> client.execute(entity));
	}
}
