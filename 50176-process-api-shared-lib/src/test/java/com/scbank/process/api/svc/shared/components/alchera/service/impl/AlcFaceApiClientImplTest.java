package com.scbank.process.api.svc.shared.components.alchera.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
public class AlcFaceApiClientImplTest {

	@Mock private BaseHttpClient httpClient;

	@InjectMocks private AlcFaceApiClientImpl client;

	private AlcHttpRequestEntity entity(String method, JSONObject body) {
		AlcHttpRequestEntity entity = mock(AlcHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn(method);
		when(entity.getUrl()).thenReturn("http://alchera/face");
		when(entity.getBodyParameters()).thenReturn(body);
		return entity;
	}

	@Test
	@DisplayName("execute - 미지원 METHOD(GET) 시 ERR_MA005")
	public void unsupportedMethodTest() {
		assertThrows(PRCServiceException.class, () -> client.execute(entity("GET", new JSONObject())));
	}

	@Test
	@DisplayName("execute - 지원하지 않는 apiCode 시 ERR_MA009")
	public void unknownApiCodeTest() {
		JSONObject body = new JSONObject().put("apiCode", "unknown");
		assertThrows(PRCServiceException.class, () -> client.execute(entity("POST", body)));
	}

	@Test
	@DisplayName("execute - liveness 파일 처리 오류 시 PRCServiceException")
	public void livenessTest() {
		JSONObject body = new JSONObject().put("apiCode", "liveness");
		assertThrows(PRCServiceException.class, () -> client.execute(entity("POST", body)));
	}

	@Test
	@DisplayName("execute - croppedface 파일 처리 오류 시 PRCServiceException")
	public void croppedfaceTest() {
		JSONObject body = new JSONObject().put("apiCode", "croppedface");
		assertThrows(PRCServiceException.class, () -> client.execute(entity("POST", body)));
	}
}
