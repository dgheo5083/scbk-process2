package com.scbank.process.api.svc.shared.components.frs.kftc.service;

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
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.frs.kftc.model.KftcFrsAuthVo;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.frs.util.FrsUniqueUtils;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcFrsApiClientTest {

	@Mock private BaseHttpClient httpClient;
	@Mock private KftcFrsTokenManager kftcFrsTokenManager;

	private KftcFrsApiClient client;

	@BeforeEach
	void setUp() throws Exception {
		client = new KftcFrsApiClient(httpClient, kftcFrsTokenManager);
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
		when(kftcFrsTokenManager.getToken()).thenReturn(new KftcFrsAuthVo());
		assertNotNull(client.init());
	}

	@Test
	@DisplayName("validationOAuth - 토큰 관리자 오류 시 KFTC-MA004")
	public void validationOAuthFailTest() {
		when(kftcFrsTokenManager.getToken()).thenThrow(new RuntimeException("token error"));
		assertThrows(PRCServiceException.class, () -> client.validationOAuth());
	}

	@Test
	@DisplayName("generateTranId - 거래고유번호 생성")
	public void generateTranIdTest() {
		try (MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class);
			 MockedStatic<FrsUniqueUtils> unique = mockStatic(FrsUniqueUtils.class)) {
			dateUtils.when(() -> DateUtils.getCurrentDate(anyString())).thenReturn("260601");
			unique.when(FrsUniqueUtils::randomIdByPidToString11).thenReturn("ABCDE123456");

			assertNotNull(client.generateTranId());
		}
	}

	@Test
	@DisplayName("execute - 미지원 METHOD 시 PRCServiceException")
	public void executeUnsupportedTest() {
		FrsHttpRequestEntity entity = mock(FrsHttpRequestEntity.class);
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
