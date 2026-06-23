package com.scbank.process.api.svc.shared.components.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnRequest;
import com.scbank.process.api.svc.shared.components.auth.dto.NonFaceAuthCompleteYnResponse;
import com.scbank.process.api.svc.shared.dao.NfTradinfoMgtDao;
import com.scbank.process.api.svc.shared.dao.dto.NfAuthCompleteYnResult;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthVerifyComponentTest {

	@Mock private NfTradinfoMgtDao nfTradinfoMgtDao;

	@InjectMocks private AuthVerifyComponent component;

	@Nested
	@DisplayName("비대면 인증 완료 여부 조회")
	class GetNonFaceAuthCompleteYn {

//		@Test
//		@DisplayName("성공_조회결과 존재 시 응답 매핑")
//		public void resultExistsTest() {
//			NonFaceAuthCompleteYnRequest input = mock(NonFaceAuthCompleteYnRequest.class);
//			when(input.getCustNo()).thenReturn("C001");
//			when(input.getTradNo()).thenReturn("T001");
//
//			NfAuthCompleteYnResult daoResult = mock(NfAuthCompleteYnResult.class);
//			when(daoResult.getAuthntIndCd()).thenReturn("1");
//			when(daoResult.getCddReqCd()).thenReturn("2");
//			when(daoResult.getDocEvdcCd()).thenReturn("3");
//			when(daoResult.getIdCardCd()).thenReturn("4");
//			when(nfTradinfoMgtDao.selectNfAuthCompleteYn(any())).thenReturn(daoResult);
//
//			NonFaceAuthCompleteYnResponse response = component.getNonFaceAuthCompleteYn(input);
//
//			assertNotNull(response);
//		}
//
//		@Test
//		@DisplayName("조회결과 없음 + custNo/tradNo는 세션값 사용")
//		public void resultNullUseSessionTest() {
//			NonFaceAuthCompleteYnRequest input = mock(NonFaceAuthCompleteYnRequest.class);
//			when(input.getCustNo()).thenReturn("");
//			when(input.getTradNo()).thenReturn("");
//			when(nfTradinfoMgtDao.selectNfAuthCompleteYn(any())).thenReturn(null);
//
//			try (MockedStatic<SessionUtils> session = mockStatic(SessionUtils.class)) {
//				session.when(() -> SessionUtils.getSessionValue("CUST_NO")).thenReturn("SC001");
//				session.when(() -> SessionUtils.getSessionValue("TRAD_NO")).thenReturn("ST001");
//				session.when(() -> SessionUtils.getSessionValue(anyString())).thenReturn("");
//
//				NonFaceAuthCompleteYnResponse response = component.getNonFaceAuthCompleteYn(input);
//
//				assertNotNull(response);
//			}
//		}
	}
}
