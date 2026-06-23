package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsAuthVo;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.obs.utils.ObsUniqueUtils;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcObsApiClientTest {

	@Mock private BaseHttpClient httpClient;
	@Mock private KftcObsTokenManager kftcObsTokenManager;

	private KftcObsApiClient client;

	@BeforeEach
	void setUp() throws Exception {
		client = new KftcObsApiClient(httpClient, kftcObsTokenManager);
		client.afterPropertiesSet();
	}

	@Test
	@DisplayName("getAuth - 초기화된 인증정보 반환")
	public void getAuthTest() {
		assertNotNull(client.getAuth());
	}

	@Test
	@DisplayName("init - 토큰 검증 후 자기 자신 반환")
	public void initTest() {
		when(kftcObsTokenManager.getToken()).thenReturn(new KftcObsAuthVo());
		assertNotNull(client.init());
	}

	@Test
	@DisplayName("validationOAuth - 토큰 관리자 오류 시 KFTC-MA004")
	public void validationOAuthFailTest() {
		when(kftcObsTokenManager.getToken()).thenThrow(new RuntimeException("token error"));
		assertThrows(PRCServiceException.class, () -> client.validationOAuth());
	}

	@Test
	@DisplayName("randomToBankTranId - 거래ID 생성")
	public void randomToBankTranIdTest() {
		try (MockedStatic<ObsUniqueUtils> unique = mockStatic(ObsUniqueUtils.class)) {
			unique.when(ObsUniqueUtils::randomIdByPidToString9).thenReturn("123456789");
			assertNotNull(client.randomToBankTranId());
		}
	}

	@Test
	@DisplayName("execute - 미지원 METHOD 시 PRCServiceException")
	public void executeUnsupportedTest() {
		ObsHttpRequestEntity entity = mock(ObsHttpRequestEntity.class);
		when(entity.getMethod()).thenReturn("DELETE");
		when(entity.getInterceptor()).thenReturn(null);
		assertThrows(PRCServiceException.class, () -> client.execute(entity));
	}

	@Test
	@DisplayName("convertParameter - JSONObject를 NameValuePair 목록으로 변환")
	public void convertParameterTest() {
		JSONObject params = new JSONObject();
		params.put("k1", "v1");
		assertEquals(1, client.convertParameter(params).size());
	}
}
