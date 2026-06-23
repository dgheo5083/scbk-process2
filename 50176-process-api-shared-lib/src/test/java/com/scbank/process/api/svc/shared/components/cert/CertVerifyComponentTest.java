package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.wizvera.crypto.CertUtil;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class CertVerifyComponentTest {

	@Mock private ISessionContextManager sessionManager;

	@InjectMocks private CertVerifyComponent certVerifyComponent;

	MockedStatic<CertUtil> mockCertUtil;
	MockedStatic<PRCSharedUtils> mockPRCSharedUtils;

	@BeforeEach
	void setUp() {
		mockCertUtil = Mockito.mockStatic(CertUtil.class);
		mockPRCSharedUtils = mockStatic(PRCSharedUtils.class);
	}

	@AfterEach
	void tearDown() {
		mockCertUtil.close();
		mockPRCSharedUtils.close();
	}

	@Nested
    @DisplayName("전자서명 검증")
	class verify {

		@Test
		@DisplayName("성공_로그인")
		public void verifyTest() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("");

			String signData = "1234";
			String dataType = "L";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.checkOID()).thenReturn(true);
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
								when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
									HashMap<String, Object> userInfo = new HashMap<String, Object>();
									userInfo.put("USERID", "LOANTE80");
									return userInfo;
								});
							});
				)
			{

				Map<String, Object> result = certVerifyComponent.verify(dataType, signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공_인증서검증값 추출")
		public void verifyTest2() throws Exception {

			String signData = "1234";
			String dataType = "A";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
								.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.checkOID()).thenReturn(true);
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
					)
			{
				when(PRCSharedUtils.isSB()).thenReturn(true);
				Map<String, Object> result = certVerifyComponent.verify(dataType, signData, vidRandom);
				assertEquals(vidRandom, result.get("_shttp_client_vid_random_"));
			}
		}

		@Test
		@DisplayName("성공_전자서명")
		public void verifyTest3() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String dataType = "S";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.checkOID()).thenReturn(true);
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
								when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
									HashMap<String, Object> userInfo = new HashMap<String, Object>();
									userInfo.put("USERID", "LOANTE80");
									return userInfo;
								});
							});
				)
			{
				when(PRCSharedUtils.isSB()).thenReturn(true);
				Map<String, Object> result = certVerifyComponent.verify(dataType, signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공_전자서명2")
		public void verifyTest3_2() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String dataType = "S";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.checkOID()).thenReturn(true);
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
								when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
									HashMap<String, Object> userInfo = new HashMap<String, Object>();
									userInfo.put("USERID", "LOANTE80");
									return userInfo;
								});
							});
				)
			{
				Map<String, Object> result = certVerifyComponent.verify(dataType, signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공_ID/PW 로그인시 인증서 데이터 검증")
		public void verifyTest4() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("");

			String signData = "1234";
			String dataType = "C";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.checkOID()).thenReturn(true);
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
								when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
									HashMap<String, Object> userInfo = new HashMap<String, Object>();
									userInfo.put("USERID", "LOANTE80");
									return userInfo;
								});
							});
				)
			{
				Map<String, Object> result = certVerifyComponent.verify(dataType, signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("실패_제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다")
		public void verifyTest5() throws Exception {
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.verify("", null, "");
			});
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.verify("", "", "");
			});
		}

		@Test
		@DisplayName("실패_이 페이지는 인증서가 필요합니다.")
		public void verifyTest6() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(null); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패2_이 페이지는 인증서가 필요합니다.")
		public void verifyTest7() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.checkOID()).thenReturn(true);
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_은행 업무용 인증서를 사용하시기 바랍니다")
		public void verifyTest8() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.checkOID()).thenReturn(false);
							when(mock.getIssuerCode()).thenReturn("01");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_본인확인에 실패했습니다")
		public void verifyTest9() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.checkOID()).thenReturn(true);
							when(mock.getIssuerCode()).thenReturn("01");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_WEBBANK 가입 고객은 인터넷 뱅킹거래가 불가합니다")
		public void verifyTest10() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE800000");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("LOANTE80");

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.checkOID()).thenReturn(true);
							when(mock.getIssuerCode()).thenReturn("01");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
								HashMap<String, Object> userInfo = new HashMap<String, Object>();
								userInfo.put("USERID", "LOANTE80");
								return userInfo;
							});
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_로그인한 사용자와 제출된 사용자의 아이디가 일치하지 않습니다.")
		public void verifyTest11() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getGlobalValue("D756_UserID", String.class)).thenReturn("TESTTEST");

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.checkOID()).thenReturn(true);
							when(mock.getIssuerCode()).thenReturn("01");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							when(mock.getUserInfoByScbDB(any())).thenAnswer(i -> {
								HashMap<String, Object> userInfo = new HashMap<String, Object>();
								userInfo.put("USERID", "LAMJ420");
								return userInfo;
							});
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.verify("C", "1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}
	}

	@Nested
    @DisplayName("비대면용 인증서 검증")
	class selfVerify {

		@Test
		@DisplayName("성공_LOCAL인 경우 CheckVID 로직 skip")
		public void selfVerifyTest() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("1");
			when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
					MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				)

			{
				when(PRCSharedUtils.isSB()).thenReturn(true);
				mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);
				Map<String, Object> result = certVerifyComponent.selfVerify(signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공_디지털인증서")
		public void selfVerifyTest2() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("9");
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				)

			{
				Map<String, Object> result = certVerifyComponent.selfVerify(signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공")
		public void selfVerifyTest3() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("1");
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
					MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				)

			{
				mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
				Map<String, Object> result = certVerifyComponent.selfVerify(signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("실패_본인확인에 실패했습니다")
		public void selfVerifyTest4() throws Exception {
			when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
			when(sessionManager.getLoginValue("ConnectType", String.class)).thenReturn("1");
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
					MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				)

			{
				mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.selfVerify(signData, vidRandom);
				});
			}
		}

		@Test
		@DisplayName("실패_제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다")
		public void selfVerifyTest5() throws Exception {
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.selfVerify(null, "");
			});
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.selfVerify("", "");
			});
		}

		@Test
		@DisplayName("실패_이 페이지는 인증서가 필요합니다.")
		public void selfVerifyTest6() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(null); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.selfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패2_이 페이지는 인증서가 필요합니다.")
		public void selfVerifyTest7() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.selfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_로그인이나 본인인증 후 사용 가능합니다.")
		public void selfVerifyTest8() throws Exception {
			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				){
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.selfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}
	}

	@Nested
    @DisplayName("미등록인증서 유효성검증")
	class edcfVerify {

		@Test
		@DisplayName("성공")
		public void edcfVerifyTest() throws Exception {
			when(sessionManager.getLoginValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				)

			{
				when(PRCSharedUtils.isSB()).thenReturn(true);
				Map<String, Object> result = certVerifyComponent.edcfVerify(signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("성공2")
		public void edcfVerifyTest2() throws Exception {
			when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

			String signData = "1234";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				)

			{
				Map<String, Object> result = certVerifyComponent.edcfVerify(signData, vidRandom);
				assertEquals("0000", result.get("returnCode"));
			}
		}

		@Test
		@DisplayName("실패_제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.")
		public void edcfVerifyTest3() throws Exception {
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.edcfVerify(null, "");
			});
			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.edcfVerify("", "");
			});
		}

		@Test
		@DisplayName("실패_이 페이지는 인증서가 필요합니다.")
		public void edcfVerifyTest4() throws Exception {
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(null); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
				)

			{
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.edcfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패2_이 페이지는 인증서가 필요합니다.")
		public void edcfVerifyTest5() throws Exception {
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getIssuerCode()).thenReturn("01");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				)

			{
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.edcfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

		@Test
		@DisplayName("실패_로그인이나 본인인증 후 사용 가능합니다.")
		public void edcfVerifyTest6() throws Exception {
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
					MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
							(mock, context) -> {
								when(mock.getIssuerDN()).thenReturn("");
								when(mock.getIssuerO()).thenReturn("yessign");
								when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.4");
								when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
							});
				)

			{
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.edcfVerify("1234", "Agbksnqw=dkang1k6k1");
				});
			}
		}

	}

	@Nested
    @DisplayName("제출한 공동인증서 검증")
	class submitCertVerify {

		@Test
		@DisplayName("성공")
		public void submitCertVerifyTest() throws Exception {

			// SignVerifier 결과
			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(signVerifierResult);
							});
				)

			{
				when(PRCSharedUtils.isSB()).thenReturn(true);
				Map<String, Object> result = certVerifyComponent.submitCertVerify("1234");
				assertEquals(signVerifierResult, result.get("signVerifierResult"));
			}
		}

		@Test
		@DisplayName("실패_제출하신 공인인증서의 검증에 실패 하였습니다")
		public void submitCertVerifyTest2() throws Exception {

			try (
					MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
							(mock, context) -> {
								when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
										.thenReturn(null);
							});
				)

			{
				assertThrows(PRCServiceException.class, () -> {
					certVerifyComponent.submitCertVerify("1234");
				});
			}
		}

	}

	@Nested
    @DisplayName("제출한 공동인증서 본인 검증")
	class vidVerify {

		@Test
		@DisplayName("성공")
		public void vidVerifyTest() throws Exception {

			Map<String, Object> verifyMap = new HashMap();
			String regNo = "8007071999994";
			String saupjaNo = "1101111111";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			verifyMap.put("signVerifierResult", signVerifierResult);

			when(PRCSharedUtils.isSB()).thenReturn(true);
			certVerifyComponent.vidVerify(verifyMap, regNo, saupjaNo, vidRandom);
		}

		@Test
		@DisplayName("실패_제출하신 공인인증서는 본인의 인증서가 아니므로 등록하실수 없습니다")
		public void vidVerifyTest2() throws Exception {

			Map<String, Object> verifyMap = new HashMap();

			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(any(), any())).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			verifyMap.put("signVerifierResult", signVerifierResult);

			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.vidVerify(verifyMap, "8007071999994", null, "Agbksnqw=dkang1k6k1");
			});

			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.vidVerify(verifyMap, "8007071999994", "", "Agbksnqw=dkang1k6k1");
			});
		}

		@Test
		@DisplayName("실패2_제출하신 공인인증서는 본인의 인증서가 아니므로 등록하실수 없습니다")
		public void vidVerifyTest3() throws Exception {

			Map<String, Object> verifyMap = new HashMap();
			String regNo = "8007071999994";
			String saupjaNo = "1101111111";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(regNo, vidRandom)).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.verifyVid(saupjaNo, vidRandom)).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			verifyMap.put("signVerifierResult", signVerifierResult);

			assertThrows(PRCServiceException.class, () -> {
				certVerifyComponent.vidVerify(verifyMap, regNo, saupjaNo, vidRandom);
			});
		}

		@Test
		@DisplayName("성공2")
		public void vidVerifyTest4() throws Exception {

			Map<String, Object> verifyMap = new HashMap();
			String regNo = "8007071999994";
			String saupjaNo = "1101111111";
			String vidRandom = "Agbksnqw=dkang1k6k1";

			SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
			when(signVerifierResult.verifyVid(regNo, vidRandom)).thenReturn(false); // VID 검사 여부
			when(signVerifierResult.verifyVid(saupjaNo, vidRandom)).thenReturn(true); // VID 검사 여부
			when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
			verifyMap.put("signVerifierResult", signVerifierResult);

			certVerifyComponent.vidVerify(verifyMap, regNo, saupjaNo, vidRandom);
		}

	}

	X509Certificate dummyCert = new X509Certificate() {

		@Override
		public boolean hasUnsupportedCriticalExtension() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Set<String> getNonCriticalExtensionOIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getExtensionValue(String oid) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<String> getCriticalExtensionOIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException,
				InvalidKeyException, NoSuchProviderException, SignatureException {
			// TODO Auto-generated method stub

		}

		@Override
		public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
				NoSuchProviderException, SignatureException {
			// TODO Auto-generated method stub

		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PublicKey getPublicKey() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getEncoded() throws CertificateEncodingException {
			return "TEST".getBytes();
		}

		@Override
		public int getVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public byte[] getTBSCertificate() throws CertificateEncodingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean[] getSubjectUniqueID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Principal getSubjectDN() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getSignature() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getSigAlgParams() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getSigAlgOID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getSigAlgName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BigInteger getSerialNumber() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getNotBefore() {
			return new Date();
		}

		@Override
		public Date getNotAfter() {
			return new Date();
		}

		@Override
		public boolean[] getKeyUsage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean[] getIssuerUniqueID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Principal getIssuerDN() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getBasicConstraints() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
			// TODO Auto-generated method stub

		}
	};

}
