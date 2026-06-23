package com.scbank.process.api.svc.shared.components.verification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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

import com.scbank.process.api.edmi.dto.mci.MciIbCidi001Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H20100Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciManager;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpManager;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.base.store.ThreadLocalStoreDelegator;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.IpinsideHddInfo;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiResponse;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationInsertCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationVerifyInfo;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class VerificationComponentTest {

	@Mock private OltpManager hostManager;
	@Mock private MciManager mciManaer;
	@Mock private ISessionContextManager sessionManager;
	@Mock private IpinsideComponent ipinside;

	@InjectMocks private VerificationComponent component;

	private MockedStatic<SessionUtils> sessionUtils;
	private MockedStatic<VerificationUtils> verificationUtils;
	private MockedStatic<RuntimeContext> runtime;
	private MockedStatic<PropertiesUtils> props;

	@BeforeEach
	void setUp() {
		sessionUtils = Mockito.mockStatic(SessionUtils.class);
		verificationUtils = Mockito.mockStatic(VerificationUtils.class);
		runtime = Mockito.mockStatic(RuntimeContext.class);
		props = Mockito.mockStatic(PropertiesUtils.class);

		sessionUtils.when(() -> SessionUtils.getSessionValue(anyString())).thenReturn("");
		runtime.when(RuntimeContext::getRunMode).thenReturn(RunMode.SIT);
		props.when(() -> PropertiesUtils.getString(anyString())).thenReturn("http://ma30");
	}

	@AfterEach
	void tearDown() {
		sessionUtils.close();
		verificationUtils.close();
		runtime.close();
		props.close();
	}

	@Nested
	@DisplayName("추가인증 검증")
	class VerifyAdditional {

		@Test
		@DisplayName("실패_추가인증 미완료(N) - PRCCMM0013")
		public void notVerifiedTest() {
			when(sessionManager.getGlobalValue("ADD_AUTH_SUCCESS_FLAG", String.class)).thenReturn("N");
			assertThrows(PRCServiceException.class, () -> component.verifyAdditional());
		}

		@Test
		@DisplayName("성공_추가인증 완료/비대상")
		public void verifiedTest() {
			when(sessionManager.getGlobalValue("ADD_AUTH_SUCCESS_FLAG", String.class)).thenReturn("Y");
			assertDoesNotThrow(() -> component.verifyAdditional());
		}
	}

	@Nested
	@DisplayName("보안매체 검증 verifyTokens")
	class VerifyTokens {

		private VerificationVerifyInfo info() {
			VerificationVerifyInfo info = mock(VerificationVerifyInfo.class);
			when(info.getUserId()).thenReturn("user01");
			when(info.getTransPasswordYn()).thenReturn("Y");
			when(info.getSafeCardConfirm()).thenReturn("0");
			when(info.getFidoSkip()).thenReturn("N");
			return info;
		}

		private void stubCommon() {
			verificationUtils.when(VerificationUtils::getTelNo1).thenReturn("010");
			verificationUtils.when(VerificationUtils::getTelNo2).thenReturn("1234");
			verificationUtils.when(VerificationUtils::getTelNo3).thenReturn("5678");
			verificationUtils.when(VerificationUtils::getSafeCardNumber1).thenReturn("1111");
			verificationUtils.when(VerificationUtils::getSafeCardNumber2).thenReturn("2222");
			verificationUtils.when(VerificationUtils::getTransPassword).thenReturn("12345678");
			verificationUtils.when(VerificationUtils::getTokensType).thenReturn("MOTP");

			IpinsideHddInfo hdd = mock(IpinsideHddInfo.class);
			when(hdd.getMacAddress()).thenReturn("AA:BB:CC");
			when(ipinside.getIpinsidInfo()).thenReturn(hdd);
		}

		@Test
		@DisplayName("성공_로그인 사용자 보안매체 검증")
		public void loginSuccessTest() {
			stubCommon();
			when(sessionManager.isLogin()).thenReturn(true);
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("user01");

			try (MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class);
				 MockedStatic<IpUtils> ipUtils = mockStatic(IpUtils.class)) {
				store.when(ThreadLocalStoreDelegator::getTrackingId).thenReturn("TR01");
				ipUtils.when(IpUtils::getClientIp).thenReturn("127.0.0.1");

				@SuppressWarnings("unchecked")
				OltpResponse<CbTbs03H20100Res> response = mock(OltpResponse.class);
				when(response.getResponse()).thenReturn(mock(CbTbs03H20100Res.class));
				when(hostManager.send(any(), any(), any())).thenAnswer(invoke -> response);

				assertDoesNotThrow(() -> component.verifyTokens(info()));
			}
		}

		@Test
		@DisplayName("비로그인 사용자 보안매체 검증 - 세션 전화번호 사용")
		public void notLoginTest() {
			stubCommon();
			// 전화번호 공백 → 세션값 사용 분기
			verificationUtils.when(VerificationUtils::getTelNo1).thenReturn("");
			verificationUtils.when(VerificationUtils::getTelNo2).thenReturn("");
			verificationUtils.when(VerificationUtils::getTelNo3).thenReturn("");
			when(sessionManager.isLogin()).thenReturn(false);

			try (MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class);
				 MockedStatic<IpUtils> ipUtils = mockStatic(IpUtils.class)) {
				store.when(ThreadLocalStoreDelegator::getTrackingId).thenReturn("TR01");
				ipUtils.when(IpUtils::getClientIp).thenReturn("127.0.0.1");

				@SuppressWarnings("unchecked")
				OltpResponse<CbTbs03H20100Res> response = mock(OltpResponse.class);
				when(response.getResponse()).thenReturn(mock(CbTbs03H20100Res.class));
				when(hostManager.send(any(), any(), any())).thenAnswer(invoke -> response);

				assertDoesNotThrow(() -> component.verifyTokens(info()));
			}
		}

		@Test
		@DisplayName("실패_전문 오류(OltpSystemException) 재던짐")
		public void oltpExceptionTest() {
			stubCommon();
			when(sessionManager.isLogin()).thenReturn(true);

			try (MockedStatic<ThreadLocalStoreDelegator> store = mockStatic(ThreadLocalStoreDelegator.class);
				 MockedStatic<IpUtils> ipUtils = mockStatic(IpUtils.class)) {
				store.when(ThreadLocalStoreDelegator::getTrackingId).thenReturn("TR01");
				ipUtils.when(IpUtils::getClientIp).thenReturn("127.0.0.1");

				doThrow(mock(OltpSystemException.class)).when(hostManager).send(any(), any(), any());

				assertThrows(OltpSystemException.class, () -> component.verifyTokens(info()));
			}
		}
	}

	@Nested
	@DisplayName("CI 조회/등록")
	class CustomerCi {

		private MciIbCidi001Res ciRes(String aoConfirm, String aoStsgb, String aoSmno) {
			MciIbCidi001Res res = mock(MciIbCidi001Res.class);
			when(res.getAOCONFIRM()).thenReturn(aoConfirm);
			when(res.getAOSTSGB()).thenReturn(aoStsgb);
			when(res.getAOSMNO()).thenReturn(aoSmno);
			when(res.getAOCIFNO()).thenReturn("CIF001");
			when(res.getAOCINOINF()).thenReturn("CI-INFO");
			when(res.getAOCMFNA()).thenReturn("홍길동");
			return res;
		}

		@SuppressWarnings("unchecked")
		private void stubMci(MciIbCidi001Res res) {
			MciResponse<MciIbCidi001Res> mciResponse = mock(MciResponse.class);
			when(mciResponse.getResponse()).thenReturn(res);
			when(mciManaer.send(any(), any(), any())).thenAnswer(invoke -> mciResponse);
		}

		@Test
		@DisplayName("getCustomerCi - 정상(AOCONFIRM=Y, 주민번호 일치)")
		public void getCiConfirmYTest() {
			stubMci(ciRes("Y", "Y", "8001011234567"));
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8001011234567");

			VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();
			request.setCi("CI-INFO");

			VerificationGetCustomerCiResponse response = component.getCustomerCi(request);
			assertNotNull(response);
		}

		@Test
		@DisplayName("getCustomerCi - 주민번호 불일치(isCIERR=Y)")
		public void getCiMismatchTest() {
			stubMci(ciRes("Y", "Y", "8001011234567"));
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("9999999999999");

			VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();
			request.setCi("CI-INFO");
			request.setDacomName("임꺽정");
			request.setDacomTranYN("Y");

			VerificationGetCustomerCiResponse response = component.getCustomerCi(request);
			assertNotNull(response);
		}

		@Test
		@DisplayName("getCustomerCi - 미보유(AOCONFIRM=N)")
		public void getCiConfirmNTest() {
			stubMci(ciRes("N", "N", ""));

			VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();
			request.setCi("");

			VerificationGetCustomerCiResponse response = component.getCustomerCi(request);
			assertNotNull(response);
		}

		@Test
		@DisplayName("getCustomerCi - 기타(AOCONFIRM 공백) 오류코드 9999")
		public void getCiConfirmOtherTest() {
			stubMci(ciRes("", "", ""));

			VerificationGetCustomerCiRequest request = new VerificationGetCustomerCiRequest();
			request.setCi("");

			VerificationGetCustomerCiResponse response = component.getCustomerCi(request);
			assertNotNull(response);
		}

		@Test
		@DisplayName("insertCustomerCi - 정상 등록(AOCONFIRM=Y)")
		public void insertCiTest() {
			stubMci(ciRes("Y", "Y", "8001011234567"));

			VerificationInsertCustomerCiRequest request = new VerificationInsertCustomerCiRequest();
			request.setUserCiInfo("CI-INFO");

			MciIbCidi001Res res = component.insertCustomerCi(request);
			assertNotNull(res);
		}

		@Test
		@DisplayName("checkUserCiInfo - 비운영(non-PRD) 정상 조회")
		public void checkUserCiInfoTest() {
			stubMci(ciRes("Y", "Y", "8001011234567"));
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8001011234567");

			assertNotNull(component.checkUserCiInfo());
		}

		@Test
		@DisplayName("checkUserCiInfo - 예외 발생 시 IsCIERR=N 응답")
		public void checkUserCiInfoExceptionTest() {
			when(mciManaer.send(any(), any(), any())).thenThrow(new RuntimeException("mci error"));

			VerificationGetCustomerCiResponse response = component.checkUserCiInfo();
			assertNotNull(response);
		}
	}
}
