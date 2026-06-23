package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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

import com.ahnlab.v3mobileplus.interfaces.parser.json.JSONObject;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.wizvera.crypto.CertUtil;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.FintechSignVerifier;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;


@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
public class FinTechCertVerifyComponentTest {

	@Mock private ISessionContextManager sessionManager;

	@InjectMocks private FinTechCertVerifyComponent finTechCertVerifyComponent;

	@Test
	@DisplayName("테스트")
	public void test() throws Exception {
		try(MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);){
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);
			finTechCertVerifyComponent.test();
		}

		try(MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);){
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.UAT);
			finTechCertVerifyComponent.test();
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 성공")
	public void signVerify() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("N", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}

	}

	@Test
	@DisplayName("전자서명 검증[단건] 성공2")
	public void signVerify2() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("N", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공")
	public void signVerify3() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}

	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공2")
	public void signVerify4() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공4_2")
	public void signVerify4_2() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"kakao\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("{\"data\":null}"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공4_3")
	public void signVerify4_3() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"kakao\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("{\"data\":{\"content\":\"TEST\"}}"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공3")
	public void signVerify5() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda|r9fa1sdasda|r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult, signVerifierResult, signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 실패_핀테크인증 요청 정보가 잘못되었습니다2")
	public void signVerify5_2() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda\\|r9fa1sdasda|r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult, signVerifierResult, signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 성공4")
	public void signVerify6() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda|r9fa1sdasda|r9fa1sdasda");

		String signData = "r9fa1sdasda|r9fa1sdasda|r9fa1sdasda";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
					when(mock.verifyPKCS7WithoutCertValidation(any())).thenReturn(signVerifierResult);

				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify("Y", signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}
	}

	@Test
	@DisplayName("전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerify7() throws Exception {
		String signData = "r9fa1sdasda|r9fa1sdasda|r9fa1sdasda";

		try(
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", "");
			});
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", null);
			});
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}

	}

	@Test
	@DisplayName("전자서명 검증 실패_본인인증이 정상적으로 처리되지 않았습니다")
	public void signVerify8() throws Exception {
		String signData = "r9fa1sdasda|r9fa1sdasda|r9fa1sdasda";

		try(
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", "");
			});
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", null);
			});
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}

	}

	@Test
	@DisplayName("전자서명검증 실패_전자서명 데이터 처리중 에러가 발생하였습니다")
	public void signVerify9() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}

	}

	@Test
	@DisplayName("전자서명 검증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerify10() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // PKCS7 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn(""); // 서명원문 데이터

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult, signVerifierResult, signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			assertThrows(Exception.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}

	}

	@Test
	@DisplayName("전자서명 검증[다중] 실패_본인인증에 실패했습니다")
	public void signVerify11() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda|r9fa1sdasda|r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(false);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult, signVerifierResult, signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 실패_본인인증에 실패했습니다2")
	public void signVerify12() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda|r9fa1sdasda|r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(false);


		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
				MockedConstruction<FintechSignVerifier> mockFintechSignVerifier = mockConstruction(FintechSignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS(any())).thenAnswer(o -> {
						SignVerifierResult[] multiSignVerifierResult = new SignVerifierResult[] {signVerifierResult, signVerifierResult, signVerifierResult};
						return multiSignVerifierResult;
					});
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechMultiSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[다중] 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerify13() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda|r9fa1sdasda|r9fa1sdasda");

		String signData = "r9fa1sdasda|r9fa1sdasda|r9fa1sdasda";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
					when(mock.verifyPKCS7WithoutCertValidation(any())).thenReturn(signVerifierResult);

				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("Y", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerify14() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn(""); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다2")
	public void signVerify15() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("asdasdasd"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_본인인증에 실패했습니다.")
	public void signVerify16() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(false);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_본인인증이 정상적으로 처리되지 않았습니다.")
	public void signVerify17() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(false);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);

				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}
	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerify18() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}

	}

	@Test
	@DisplayName("전자서명 검증[단건] 실패_정상적인 인증서가 아닙니다")
	public void signVerify20() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("finTechSignPlainText", String.class)).thenReturn("r9fa1sdasda");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(null); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify("N", signData);
			});
		}

	}

	@Test
	@DisplayName("본인인증 검증 성공")
	public void authVerify() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}

	}

	@Test
	@DisplayName("본인인증 검증 성공2")
	public void authVerify2() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(signData);
			assertEquals("0000", resultMap.get("returnCode"));
		}

	}

	@Test
	@DisplayName("본인인증 검증 실패_전자서명 데이터 처리중 에러가 발생하였습니다")
	public void authVerify3() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}

	}

	@Test
	@DisplayName("본인인증 검증 실패_본인인증이 정상적으로 처리되지 않았습니다")
	public void authVerify4() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}
	}

	@Test
	@DisplayName("본인인증 검증 실패_본인인증에 실패했습니다")
	public void authVerify5() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}
	}

	@Test
	@DisplayName("본인인증 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void authVerify6() throws Exception {
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify("");
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(null);
		});
	}

	@Test
	@DisplayName("본인인증 검증 실패_본인인증이 정상적으로 처리되지 않았습니다")
	public void authVerify7() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn(null);

		String signData = "{\"provider\":\"toss\"}";

		try(
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}

	}

	@Test
	@DisplayName("본인인증 검증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void authVerify8() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "{\"provider\":\"toss\"}";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터
		when(signVerifierResult.isVerifiableCi()).thenReturn(true);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedStatic<FintechSignVerifier> fintMockedStatic = Mockito.mockStatic(FintechSignVerifier.class);
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);
			fintMockedStatic.when(() -> FintechSignVerifier.isFintechSignResult(anyString())).thenReturn(true);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}

	}

	@Test
	@DisplayName("본인인증 검증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void authVerify9() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}

	}

	@Test
	@DisplayName("본인인증 검증 실패_전달된 핀테크인증 전자서명 데이터 검증에 실패하였습니다")
	public void authVerify10() throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("USER_CI_INFO", String.class)).thenReturn("12451512");

		String signData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("GF61sdaas12dasda"); // 서명원문 데이터
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명원문 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithoutCertValidation(anyString())).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(signData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 성공[toss]")
	public void authVerifyTest() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 성공[toss]_CI검증 스킵")
	public void authVerifyTest_2() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 성공[kakaotalk]")
	public void authVerifyTest2() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("kakaotalk");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 성공[naver2]")
	public void authVerifyTest3() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("naver2");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 성공[pass]")
	public void authVerifyTest4() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("pass");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			HashMap<String, Object> resultMap = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 실패_전달된 핀테크인증 전자서명 데이터 검증에 실패")
	public void authVerifyTest4_2() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.LOCAL);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			});
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void authVerifyTest5() throws Exception {
		String delfinoNonce = "1q2tg1";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(delfinoNonce, null, vpcgData, userId);
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(delfinoNonce, "", vpcgData, userId);
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(delfinoNonce, "L", vpcgData, userId);
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(delfinoNonce, "A", null, userId);
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.authVerify(delfinoNonce, "A", "", userId);
		});

	}

	@Test
	@DisplayName("핀테크 본인인증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void authVerifyTest6() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			});
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다2")
	public void authVerifyTest6_2() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn(""); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("asfdsad1243"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			});
		}

	}

	@Test
	@DisplayName("핀테크 본인인증 실패_본인인증에 실패했습니다.")
	public void authVerifyTest7() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "A";
		String vpcgData = "1234";
		String userId = "LOANTE80";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(false);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, vpcgData, userId);
			});
		}

	}

	@Test
	@DisplayName("핀테크 로그인인증 성공")
	public void loginVerify() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "L";
		String vpcgData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
			)
		{

			finTechCertVerifyComponent.loginVerify(delfinoNonce, dataType, vpcgData);
			assertNotNull(signVerifierResult);;
		}

	}

	@Test
	@DisplayName("핀테크 로그인인증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void loginVerify2() throws Exception {
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.loginVerify("1q2tg1", "", "1234");
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.loginVerify("1q2tg1", null, "1234");
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.loginVerify("1q2tg1", "A", "1234");
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.loginVerify("1q2tg1", "L", "");
		});
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.loginVerify("1q2tg1", "L", null);
		});
	}

	@Test
	@DisplayName("핀테크 로그인인증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void loginVerify3() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "L";
		String vpcgData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn(""); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.loginVerify(delfinoNonce, dataType, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 로그인인증 실패2_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void loginVerify3_2() throws Exception {
		String delfinoNonce = "1q2tg1";
		String dataType = "L";
		String vpcgData = "1234";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7WithNonce(anyString(), any(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.loginVerify(delfinoNonce, dataType, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 성공[toss]")
	public void signVerifyTest() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 성공[kakaotalk]")
	public void signVerifyTest2() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("kakaotalk");
		when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("kakaotalk");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 성공[naver2]")
	public void signVerifyTest3() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("naver2");
		when(sessionManager.getLoginValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("naver2");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 성공[pass]")
	public void signVerifyTest4() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("pass");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("pass");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			assertEquals("000", resultMap.get("resultCode"));
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn(null);
		assertThrows(PRCServiceException.class, () -> {

			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_2() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("");
		assertThrows(PRCServiceException.class, () -> {

			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_3() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn(null);
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_4() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("");
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_5() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, null);
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_6() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, "");
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_7() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn(null);
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}
	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 요청 정보가 잘못되었습니다")
	public void signVerifyTest5_8() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getLoginValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("");
		assertThrows(PRCServiceException.class, () -> {
			finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
		});
	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerifyTest6() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(""); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패2_핀테크인증 전자서명 데이터가 존재하지 않습니다")
	public void signVerifyTest6_2() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn(null); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패_전달된 핀테크인증 전자서명 데이터 검증에 실패하였습니다")
	public void signVerifyTest7() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("toss");
		when(signVerifierResult.getSignerCertificate()).thenReturn(null);
		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 실패_본인인증에 실패했습니다")
	public void signVerifyTest8() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("123412");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("toss");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("pass");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			assertThrows(PRCServiceException.class, () -> {
				finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			});
		}

	}

	@Test
	@DisplayName("핀테크 전자서명 검증 오류응답")
	public void signVerifyTest9() throws Exception {
		String dataType = "S";
		String verifyData = "LOANTE80";
		String vpcgData = "{\"signData\":\"1234\"}";

		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class)).thenReturn("12451512");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_SERIAL", String.class)).thenReturn("612313");
		when(sessionManager.getGlobalValue("FINCERT_VERIFIER_TYPE", String.class)).thenReturn("pass");
		when(sessionManager.getGlobalValue("UserID", String.class)).thenReturn("LOANTE80");
		when(sessionManager.getGlobalValue("NOTLOGINSSN", String.class)).thenReturn("8007071999994");

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.getPKCS7SignedData()).thenReturn("132fasd1"); // PKCS7 데이터
		when(signVerifierResult.getOriginSignedRawData()).thenReturn("r9fa1sdasda"); // 원본 전자서명 데이터
		when(signVerifierResult.getSignedRawData()).thenReturn("14asfagGA15"); // 원본 전자서명 데이터
		when(signVerifierResult.getProvider()).thenReturn("pass");
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert);
		when(signVerifierResult.verifyCi(any())).thenReturn(true);

		try(
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class, (mock, context) -> {
					when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE))).thenReturn(signVerifierResult);
				});

				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
					(mock, context) -> {
						when(mock.getSerialDecimal()).thenReturn("123412");
						when(mock.getSerialHex()).thenReturn("0A10");
					});
			)
		{
			HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify( dataType, verifyData, vpcgData);
			assertEquals("999", resultMap.get("resultCode"));
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
			return new BigInteger("1231231");
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
