package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.dao.FaceRecApiTokenDao;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecTokenInfo;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class KftcFrsTokenManagerTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS) private KftcFrsAuthClient kftcFrsAuthClient;
	@Mock private FaceRecApiTokenDao faceRecApiTokenDao;
	@Mock private SmsComponent smsComponent;

	@InjectMocks private KftcFrsTokenManager tokenManager;

	private FrsHttpJsonObject tokenBody() {
		FrsHttpJsonObject body = mock(FrsHttpJsonObject.class);
		when(body.getAsLong(anyString(), anyLong())).thenReturn(3600L);
		when(body.getAsJsonObject()).thenReturn(new JSONObject());
		when(body.getAsJsonString()).thenReturn("{}");
		return body;
	}

//	@Nested
//	@DisplayName("토큰 획득/재발급")
//	class GetChange {
//
//		@Test
//		@DisplayName("getToken - DB 토큰 없으면 재발급")
//		public void getTokenNoDbTest() {
//			when(faceRecApiTokenDao.selectToken()).thenReturn(null);
//			when(kftcFrsAuthClient.getToken().getBody()).thenReturn(tokenBody());
//
//			try (MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class);
//				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
//				dateUtils.when(() -> DateUtils.getDate(any(Date.class), anyString())).thenReturn("20270601");
//				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("");
//				assertNotNull(tokenManager.getToken());
//			}
//		}
//
//		@Test
//		@DisplayName("getToken - DB 토큰 만료 시 재발급")
//		public void getTokenExpiredTest() {
//			FaceRecTokenInfo dbResult = mock(FaceRecTokenInfo.class);
//			when(dbResult.getData()).thenReturn("{}");
//			when(faceRecApiTokenDao.selectToken()).thenReturn(dbResult);
//			when(kftcFrsAuthClient.getToken().getBody()).thenReturn(tokenBody());
//
//			try (MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class);
//				 MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
//				dateUtils.when(() -> DateUtils.getDate(any(Date.class), anyString())).thenReturn("20270601");
//				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("");
//				assertNotNull(tokenManager.getToken());
//			}
//		}
//
//		@Test
//		@DisplayName("changeToken - 발급 실패 시 SMS 실패 안내 후 VO 반환")
//		public void changeTokenFailTest() {
//			when(kftcFrsAuthClient.getToken().getBody()).thenThrow(new RuntimeException("fail"));
//
//			try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
//				props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("");
//				assertNotNull(tokenManager.changeToken());
//			}
//		}
//	}

	@Nested
	@DisplayName("토큰 활성화/무효화")
	class EnableDisable {

		@Test
		@DisplayName("enableToken - 활성화 DAO 호출")
		public void enableTokenTest() {
			tokenManager.enableToken("20270601", "{}");
			verify(faceRecApiTokenDao).enabledToken(any());
		}

		@Test
		@DisplayName("enableToken - DAO 오류 시 PRCServiceException")
		public void enableTokenFailTest() {
			doThrow(new RuntimeException("db error")).when(faceRecApiTokenDao).enabledToken(any());
			assertThrows(PRCServiceException.class, () -> tokenManager.enableToken("20270601", "{}"));
		}

		@Test
		@DisplayName("disableToken - 무효화 DAO 호출")
		public void disableTokenTest() {
			tokenManager.disableToken();
			verify(faceRecApiTokenDao).disabledToken();
		}
	}
}
