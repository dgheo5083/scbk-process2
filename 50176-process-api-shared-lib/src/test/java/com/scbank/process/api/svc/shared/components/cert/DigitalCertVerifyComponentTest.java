package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
public class DigitalCertVerifyComponentTest {

	@InjectMocks
	DigitalCertVerifyComponent digitalCertVerifyComponent;
	@Mock
	private ISessionContextManager sessionManager;
	
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
	
	@Test
	@DisplayName("전자서명 검증")
	void verifyTest1() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

		String signData = "1234";
		String dataType = "LO";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.verifyVid(any(), eq(null))).thenReturn(true); // VID 검사 여부
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
		when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

		try (
				// new SignVerifier()
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
				// new CertificateHelper()
				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerCode()).thenReturn("023");
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.1.10");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);) {

			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			Map<String, Object> result = digitalCertVerifyComponent.verify(dataType, signData);
			assertEquals("0000", result.get("returnCode"));
		}
	}
	
	@Test
	@DisplayName("전자서명 검증 - 정상적인 디지털 인증서가 아닙니다.(Code:001)")
	void verifyTest2() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");
		
		String signData = "1234";
		String dataType = "LO";
		
		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.verifyVid(any(), eq(null))).thenReturn(true); // VID 검사 여부
		when(signVerifierResult.getSignerCertificate()).thenReturn(null); // 서명한인증서 꺼냄
		when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터
		
		try (
				// new SignVerifier()
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
							.thenReturn(signVerifierResult);
						});
				// new CertificateHelper()
				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerCode()).thenReturn("023");
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.1.10");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			
			assertThrows(PRCServiceException.class, () -> {
				digitalCertVerifyComponent.verify(dataType, signData);
			});
		}
	}
	
	@Test
	@DisplayName("전자서명 검증 - 정상적인 디지털 인증서가 아닙니다.(Code:002)")
	void verifyTest3() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");
		
		String signData = "1234";
		String dataType = "LO";
		
		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.verifyVid(any(), eq(null))).thenReturn(true); // VID 검사 여부
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
		when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터
		
		try (
				// new SignVerifier()
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
							.thenReturn(signVerifierResult);
						});
				// new CertificateHelper()
				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerCode()).thenReturn("");
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.1.10");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);
			
			assertThrows(PRCServiceException.class, () -> {
				digitalCertVerifyComponent.verify(dataType, signData);
			});
		}
	}
	
	@Test
	@DisplayName("전자서명 검증")
	void verifyTest4() throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue("PerBusNo", String.class)).thenReturn("8007071999994");

		String signData = "1234";
		String dataType = "LOO";

		// SignVerifier 결과
		SignVerifierResult signVerifierResult = Mockito.mock(SignVerifierResult.class);
		when(signVerifierResult.verifyVid(any(), eq(null))).thenReturn(true); // VID 검사 여부
		when(signVerifierResult.getSignerCertificate()).thenReturn(dummyCert); // 서명한인증서 꺼냄
		when(signVerifierResult.getSignedRawData()).thenReturn("TEST=TEST"); // 서명 원본 데이터

		try (
				// new SignVerifier()
				MockedConstruction<SignVerifier> mockSignVerifier = mockConstruction(SignVerifier.class,
						(mock, context) -> {
							when(mock.verifyPKCS7(anyString(), eq(SignVerifier.CERT_STATUS_CHECK_TYPE_NONE)))
									.thenReturn(signVerifierResult);
						});
				// new CertificateHelper()
				MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
						(mock, context) -> {
							when(mock.getIssuerCode()).thenReturn("023");
							when(mock.getIssuerDN()).thenReturn("");
							when(mock.getIssuerO()).thenReturn("yessign");
							when(mock.getCertificatePolicyOID()).thenReturn("1.2.410.200005.1.1.1.10");
							when(mock.getSubjectCN()).thenReturn("업무테스트()00231111111111");
						});
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);) {

			mockRuntimeContext.when(() -> RuntimeContext.getRunMode()).thenReturn(RunMode.PRD);

			Map<String, Object> result = digitalCertVerifyComponent.verify(dataType, signData);
			assertEquals("0000", result.get("returnCode"));
		}
	}
	
	
	
	
	
	
	
	
	
	
}