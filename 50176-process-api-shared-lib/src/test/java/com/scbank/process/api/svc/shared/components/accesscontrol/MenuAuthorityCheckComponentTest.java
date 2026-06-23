package com.scbank.process.api.svc.shared.components.accesscontrol;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.accesscontrol.dto.MenuAuthorityCheckRequest;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuAuthorityCheckComponentTest {

	@Mock private ISessionContextManager sessionContextManager;
	@Mock private ServiceTimeCheckComponent serviceTimeCheckComponent;

	@InjectMocks private MenuAuthorityCheckComponent component;

	private MockedStatic<ServiceContextHolder> holder;

	@BeforeEach
	void setUp() {
		holder = Mockito.mockStatic(ServiceContextHolder.class);
		holder.when(ServiceContextHolder::getContext).thenReturn(null);
	}

	@AfterEach
	void tearDown() {
		holder.close();
	}

	private MenuAuthorityCheckRequest req(String acType) {
		return MenuAuthorityCheckRequest.builder().menuId("M001").acType(acType).forceCheckCode("").build();
	}

	@Nested
	@DisplayName("checkAuthority - 접근제어")
	class CheckAuthority {

		@Test
		@DisplayName("성공_접근제어 타입 없음(이용시간 체크만 수행)")
		public void noAcTypeTest() {
			when(serviceTimeCheckComponent.isCheckIgnoreMenuId("")).thenReturn(false);
			when(serviceTimeCheckComponent.isCheckIgnoreRequestUri(anyString())).thenReturn(false);

			assertDoesNotThrow(() -> component.checkAuthority(req("")));
			verify(serviceTimeCheckComponent, times(1)).checkServiceTime(org.mockito.ArgumentMatchers.any());
		}

		@Test
		@DisplayName("이용시간 체크 무시 대상이면 checkServiceTime 미호출")
		public void ignoreServiceTimeTest() {
			when(serviceTimeCheckComponent.isCheckIgnoreMenuId("M001")).thenReturn(true);
			when(serviceTimeCheckComponent.isCheckIgnoreRequestUri("")).thenReturn(true);

			assertDoesNotThrow(() -> component.checkAuthority(req("")));
			verify(serviceTimeCheckComponent, org.mockito.Mockito.never()).checkServiceTime(org.mockito.ArgumentMatchers.any());
		}

		@Test
		@DisplayName("실패_로그인 필요(00, 미로그인) PRCLGN0015")
		public void needLoginTest() {
			when(sessionContextManager.isLogin()).thenReturn(false);

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("00")));
			assertTrue(ex.getErrorCode().contains("PRCLGN0015"));
		}

		@Test
		@DisplayName("실패_조회사용자 이체성거래 접근(02) PRCCMM0049")
		public void inquiryUserTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("SafeCardState", String.class)).thenReturn("0");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("02")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0049"));
		}

		@Test
		@DisplayName("성공_조회사용자 아님(02, SafeCardState != 0)")
		public void inquiryUserOkTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("SafeCardState", String.class)).thenReturn("1");

			assertDoesNotThrow(() -> component.checkAuthority(req("02")));
		}

		@Test
		@DisplayName("실패_인증서 로그인 필요(05) PRCCMM0052")
		public void certLoginNeedTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getGlobalValue("deviceCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getLoginValue("ConnectType", String.class)).thenReturn("2");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("05")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0052"));
		}

		@Test
		@DisplayName("실패_단말지정 서비스(04, 로그인) PRCCMM0045")
		public void pcFixLoginTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("PcFixValue", String.class)).thenReturn("1");
			when(sessionContextManager.getLoginValue("CustGubun", String.class)).thenReturn("1");
			when(sessionContextManager.getLoginValue("OtherPCYes", String.class)).thenReturn("N");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("04")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0045"));
		}

		@Test
		@DisplayName("실패_단말지정 서비스(04, 미로그인 globalValue) PRCCMM0045")
		public void pcFixNotLoginTest() {
			when(sessionContextManager.isLogin()).thenReturn(false);
			when(sessionContextManager.getGlobalValue("PcFixValue", String.class)).thenReturn("1");
			when(sessionContextManager.getGlobalValue("CustGubun", String.class)).thenReturn("1");
			when(sessionContextManager.getGlobalValue("OtherPCYes", String.class)).thenReturn("N");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("04")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0045"));
		}

		@Test
		@DisplayName("실패_보안매체 상태 이상(01) PRCCMM0048")
		public void userTransStateTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("UserTransStateMsg", String.class)).thenReturn("분실신고 상태입니다");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("01")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0048"));
		}

//		@Test
//		@DisplayName("실패_모바일OTP 기기변경(01) PRCCMM0057")
//		public void motpDeviceChangeTest() {
//			when(sessionContextManager.isLogin()).thenReturn(true);
//			when(sessionContextManager.getLoginValue("UserTransStateMsg", String.class)).thenReturn("");
//			when(sessionContextManager.getLoginValue("MOTP_DEVICE_CHANGE_TYPE", String.class)).thenReturn("02");
//
//			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("01")));
//			assertTrue(ex.getErrorCode().contains("PRCCMM0057"));
//		}

		@Test
		@DisplayName("실패_금융인증서 발급 필요(03, connectType 2) PRCCMM0050")
		public void finCertNeedTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getGlobalValue("deviceCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getGlobalValue("finCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getLoginValue("ConnectType", String.class)).thenReturn("2");

			PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("03")));
			assertTrue(ex.getErrorCode().contains("PRCCMM0050"));
		}

		@Test
		@DisplayName("실패_핀테크 연계메뉴 아님(03, connectType D) PRCCMM0050")
		public void fintechMenuTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getGlobalValue("deviceCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getGlobalValue("finCertExistYn", String.class)).thenReturn("E");
			when(sessionContextManager.getLoginValue("ConnectType", String.class)).thenReturn("D");

			try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
				codeUtils.when(() -> CodeUtils.getCodes("FINTECH_LINKAGE_MENU")).thenReturn(Collections.emptyList());

				PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("03")));
				assertTrue(ex.getErrorCode().contains("PRCCMM0050"));
			}
		}

		@Test
		@DisplayName("성공_핀테크 연계메뉴(03, connectType F)")
		public void fintechMenuOkTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getGlobalValue("deviceCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getGlobalValue("finCertExistYn", String.class)).thenReturn("N");
			when(sessionContextManager.getLoginValue("ConnectType", String.class)).thenReturn("F");

			ICodeItemInfo item = mock(ICodeItemInfo.class);
			when(item.getKey()).thenReturn("M001");

			try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
				codeUtils.when(() -> CodeUtils.getCodes("FINTECH_LINKAGE_MENU")).thenReturn(List.of(item));

				assertDoesNotThrow(() -> component.checkAuthority(req("03")));
			}
		}

		@Test
		@DisplayName("실패_개인정보 노출자 차단(YOLRSOTGB=1) PRCCMMERRCIL001")
		public void infoLeakBlockTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("YOLRSOTGB", String.class)).thenReturn("1");

			ICodeItemInfo item = mock(ICodeItemInfo.class);
			when(item.getValue()).thenReturn("M001");

			try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
				codeUtils.when(() -> CodeUtils.getCodes("CSINFOLEAK_BLKMNU")).thenReturn(List.of(item));

				PRCServiceException ex = assertThrows(PRCServiceException.class, () -> component.checkAuthority(req("")));
				assertTrue(ex.getErrorCode().contains("PRCCMMERRCIL001"));
			}
		}

		@Test
		@DisplayName("성공_개인정보 노출자(YOLRSOTGB=1)이나 차단메뉴 목록 비어있음")
		public void infoLeakEmptyListTest() {
			when(sessionContextManager.isLogin()).thenReturn(true);
			when(sessionContextManager.getLoginValue("YOLRSOTGB", String.class)).thenReturn("1");

			try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class)) {
				codeUtils.when(() -> CodeUtils.getCodes("CSINFOLEAK_BLKMNU")).thenReturn(Collections.emptyList());

				assertDoesNotThrow(() -> component.checkAuthority(req("")));
			}
		}
	}
}
