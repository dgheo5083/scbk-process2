package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;
import com.scbank.process.api.svc.shared.dao.Ma3MydataApiTokenDao;
import com.scbank.process.api.svc.shared.dao.dto.Ma3MydataApiTokenResult;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MdcMyDataAuthManagerTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS) private MdcMyDataAuthClient myDataAuthClient;
	@Mock private Ma3MydataApiTokenDao mydataApiTokenDao;

	@InjectMocks private MdcMyDataAuthManager authManager;

	private MyDataHttpJsonObject tokenResult() {
		MyDataHttpJsonObject result = mock(MyDataHttpJsonObject.class);
		when(result.getAsString(anyString(), anyString())).thenReturn("");
		when(result.getAsString(eq("expiresIn"), anyString())).thenReturn("3600");
		when(result.getAsJsonObject()).thenReturn(new JSONObject());
		return result;
	}

	@Nested
	@DisplayName("토큰 발급/변경")
	class ChangeToken {

//		@Test
//		@DisplayName("changeToken - KONG 토큰 발급 후 활성화")
//		public void changeTokenTest() {
//			when(myDataAuthClient.getToken().throwException().getBody()).thenReturn(tokenResult());
//
//			assertNotNull(authManager.changeToken());
//			verify(mydataApiTokenDao).disabledToken("mobile");
//			verify(mydataApiTokenDao).enabledToken(org.mockito.ArgumentMatchers.any());
//		}

//		@Test
//		@DisplayName("changeInternetToken - 인터넷 토큰 발급 후 활성화")
//		public void changeInternetTokenTest() {
//			when(myDataAuthClient.getInternetToken().throwException().getBody()).thenReturn(tokenResult());
//
//			assertNotNull(authManager.changeInternetToken());
//			verify(mydataApiTokenDao).disabledToken("internet");
//		}

//		@Test
//		@DisplayName("getToken - DB 토큰 없으면 신규 발급(changeToken)")
//		public void getTokenNoDbTest() {
//			when(mydataApiTokenDao.selectToken("mobile")).thenReturn(null);
//			when(myDataAuthClient.getToken().throwException().getBody()).thenReturn(tokenResult());
//
//			assertNotNull(authManager.getToken());
//		}
//
//		@Test
//		@DisplayName("getToken - DB 토큰 만료 시 신규 발급")
//		public void getTokenExpiredTest() {
//			Ma3MydataApiTokenResult dbResult = mock(Ma3MydataApiTokenResult.class);
//			when(dbResult.getAccessToken()).thenReturn("{}");
//			when(mydataApiTokenDao.selectToken("mobile")).thenReturn(dbResult);
//			when(myDataAuthClient.getToken().throwException().getBody()).thenReturn(tokenResult());
//
//			assertNotNull(authManager.getToken());
//		}
	}

	@Nested
	@DisplayName("토큰 조회/활성화/폐기")
	class TokenLifecycle {

		@Test
		@DisplayName("getSelectToken - DB 토큰 없으면 예외")
		public void getSelectTokenNoneTest() {
			when(mydataApiTokenDao.selectToken("mobile")).thenReturn(null);
			assertThrows(PRCServiceException.class, () -> authManager.getSelectToken());
		}

		@Test
		@DisplayName("getSelectToken - DB 토큰 존재 시 VO 반환")
		public void getSelectTokenExistsTest() {
			Ma3MydataApiTokenResult dbResult = mock(Ma3MydataApiTokenResult.class);
			when(dbResult.getAccessToken()).thenReturn("{}");
			when(mydataApiTokenDao.selectToken("mobile")).thenReturn(dbResult);

			assertNotNull(authManager.getSelectToken());
		}

		@Test
		@DisplayName("enableToken / disableToken")
		public void enableDisableTest() {
			authManager.enableToken("20260601", tokenResult());
			verify(mydataApiTokenDao).enabledToken(org.mockito.ArgumentMatchers.any());

			authManager.disableToken();
			verify(mydataApiTokenDao).disabledToken("mobile");
		}

		@Test
		@DisplayName("enableInternetToken / disableInternetToken")
		public void enableDisableInternetTest() {
			authManager.enableInternetToken("20260601", tokenResult());
			authManager.disableInternetToken();
			verify(mydataApiTokenDao).disabledToken("internet");
		}
	}
}
