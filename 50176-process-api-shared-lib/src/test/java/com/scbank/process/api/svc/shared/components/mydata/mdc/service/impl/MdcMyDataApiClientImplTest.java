package com.scbank.process.api.svc.shared.components.mydata.mdc.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.mdc.service.MdcMyDataAuthManager;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpRequestEntity;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataApiClientImplTest {

	@Mock private BaseHttpClient httpClient;
	@Mock private MdcMyDataAuthManager myDataAuthManager;

	private MdcMyDataApiClientImpl client;

	@BeforeEach
	void setUp() {
		client = new MdcMyDataApiClientImpl(httpClient, myDataAuthManager);
		client.initPostConstruct();
	}

	@Test
	@DisplayName("getAuth - PostConstruct로 초기화된 인증정보 반환")
	public void getAuthTest() {
		assertNotNull(client.getAuth());
	}

	@Test
	@DisplayName("init - 유효한 토큰 조회 시 자기 자신 반환")
	public void initSuccessTest() {
		when(myDataAuthManager.getSelectToken()).thenReturn(new MdcMyDataAuthVo());

		assertNotNull(client.init());
	}

	@Test
	@DisplayName("validationOAuth - 토큰 조회 실패 시 MYDATA-MA004")
	public void validationOAuthFailTest() {
		when(myDataAuthManager.getSelectToken()).thenThrow(new RuntimeException("db error"));

		assertThrows(PRCServiceException.class, () -> client.validationOAuth());
	}

	@Test
	@DisplayName("execute - 미지원 METHOD 시 PRCServiceException")
	public void executeUnsupportedMethodTest() {
		MyDataHttpRequestEntity entity = mock(MyDataHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("PUT");
		when(entity.getInterceptor()).thenReturn(null);

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
