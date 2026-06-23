package com.scbank.process.api.svc.shared.components.fido;

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
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRAIllegalFormatException;
import com.initech.oppra.IniOPPRAReadException;
import com.initech.oppra.util.OppraMessageDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.KftcCertComponent;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
public class FidoHttpComponentTest {
	
	@InjectMocks FidoHttpComponent fidoHttpComponent;
	
	@Mock private ISessionContextManager sessionManager;
	@Mock private LdapInfoDao ldapInfoDao; 
	@Mock private ICodeManager commonCode;
	@Mock private HttpClient.Builder builder;
	@Mock private HttpClient httpClient;
	@Mock private CertUtils certUtils;
	@Mock private KftcCertComponent kftcCertComponent;
	@Mock private FidoSequenceGenerator fidoSequenceGenerator;
	MockedStatic<PropertiesUtils> mockPropertiesUtils;
	MockedStatic<RuntimeContext> mockRuntimeContext;
	MockedStatic<HttpClient> mockHttpClient;
	
	@BeforeEach
	void setUp() {
		mockHttpClient = Mockito.mockStatic(HttpClient.class);
		mockHttpClient.when(() -> HttpClient.newBuilder()).thenReturn(builder);
		when(builder.sslContext(any())).thenReturn(builder);
		when(builder.build()).thenReturn(httpClient);
		
		mockPropertiesUtils = Mockito.mockStatic(PropertiesUtils.class);
		mockRuntimeContext = Mockito.mockStatic(RuntimeContext.class);
		mockPropertiesUtils.when(()-> PropertiesUtils.getString("FIDO_SERVER_TIMEOUT", "10000")).thenReturn("10000");
		mockPropertiesUtils.when(()-> PropertiesUtils.getString("FIDO_SVC_ID", "SVC01SCBANK000000000")).thenReturn("SVC01SCBANK000000000");
		mockPropertiesUtils.when(()-> PropertiesUtils.getString("FIDO_SITE_ID", "SIT01SCBANK000000000")).thenReturn("SIT01SCBANK000000000");
		
	}
	
	@AfterEach
	void tearDown() {
		mockPropertiesUtils.close();
		mockRuntimeContext.close();
		mockHttpClient.close();
	}
	
	@DisplayName("FIDO 지원 여부 체크 (device)")
	@Test
	void allowedAuthnrTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"aaidAllowList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"정상처리 되었습니다.\"}");

		assertNotNull(fidoHttpComponent.allowedAuthnr(""));
	}
	
	@DisplayName("FIDO 등록상태 조회")
	@Test
	void checkRegisteredStatusTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119604\",\"aaid\":\"0012#0021\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"subjectDn\":\"CN=업무테스트()002334220260424123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		
		Map certCodeHashMap = new HashMap<String, String>();
		certCodeHashMap.put("BIO", "Y");
		certCodeHashMap.put("PIN", "Y");
		
		assertNotNull(fidoHttpComponent.checkRegisteredStatus("", "", "", "", Map.of("CERTCODE_HASHMAP", certCodeHashMap)));
	}
	
	@DisplayName("FIDO 등록상태 조회-오류")
	@Test
	void checkRegisteredStatusTest2() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119604\",\"aaid\":\"0012#0021\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"subjectDn\":\"CN=업무테스트()002334220260424123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		
		assertThrows(PRCServiceException.class, ()-> fidoHttpComponent.checkRegisteredStatus("", "", "", "", null));
	}
	
	@DisplayName("FIDO 등록상태 조회-faceId")
	@Test
	void checkRegisteredStatusTest3() throws Exception {
		when(sessionManager.getGlobalValue(eq("faceDevice"), eq(String.class))).thenReturn("Y");
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119604\",\"aaid\":\"0012#0021\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"subjectDn\":\"CN=업무테스트()002334220260424123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		
		Map certCodeHashMap = new HashMap<String, String>();
		certCodeHashMap.put("FACEID", "Y");
		certCodeHashMap.put("PIN", "Y");
		
		assertNotNull(fidoHttpComponent.checkRegisteredStatus("", "", "", "", Map.of("CERTCODE_HASHMAP", certCodeHashMap)));
	}
	
	@DisplayName("FIDO 등록상태 조회-홍채")
	@Test
	void checkRegisteredStatusTest4() throws Exception {
		when(sessionManager.getGlobalValue(eq("faceDevice"), eq(String.class))).thenReturn("Y");
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119604\",\"aaid\":\"0012#0021\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"subjectDn\":\"CN=업무테스트()002334220260424123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"vendorId\":\"0012\",\"verificationType\":\"4096\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		
		Map certCodeHashMap = new HashMap<String, String>();
		certCodeHashMap.put("BIONIC", "Y");
		
		assertNotNull(fidoHttpComponent.checkRegisteredStatus("", "", "", "", Map.of("CERTCODE_HASHMAP", certCodeHashMap)));
	}
	
	@DisplayName("FIDO 인증서 발급")
	@ParameterizedTest
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
			"8007076999994",
			})
	void requestIssueCertTest(String PerBusNo) throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestIssueCert("", "", "", "", "", ""));
	}
	
	@DisplayName("FIDO 인증서 발급-오류")
	@Test
	void requestIssueCertTest2() throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn("8007072999994");
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		assertThrows(PRCServiceException.class, ()-> fidoHttpComponent.requestIssueCert("", "", "", "", "", ""));
	}
	
	
	@DisplayName("FIDO 서비스 가입-로그인")
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
			"8007076999994",
			})
	@ParameterizedTest
	void requestServiceRegistTest(String PerBusNo) throws Exception {
		when(sessionManager.isLogin()).thenReturn(true);
		when(sessionManager.getGlobalValue(eq("HPOne"), eq(String.class))).thenReturn("010");
		when(sessionManager.getGlobalValue(eq("HPTwo"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getGlobalValue(eq("HPThree"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getLoginValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestServiceRegist("", "", "", ""));
	}
	
	@DisplayName("FIDO 서비스 가입-비로그인")
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
			"8007076999994",
	})
	@ParameterizedTest
	void requestServiceRegistTest2(String PerBusNo) throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue(eq("HPOne"), eq(String.class))).thenReturn("010");
		when(sessionManager.getGlobalValue(eq("HPTwo"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getGlobalValue(eq("HPThree"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestServiceRegist("", "", "", ""));
	}
	
	@DisplayName("FIDO 서비스 가입-오류")
	@ValueSource(strings = { 
			"8007071999994",
	})
	@ParameterizedTest
	void requestServiceRegistTest3(String PerBusNo) throws Exception {
		when(sessionManager.isLogin()).thenReturn(false);
		when(sessionManager.getGlobalValue(eq("HPOne"), eq(String.class))).thenReturn("010");
		when(sessionManager.getGlobalValue(eq("HPTwo"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getGlobalValue(eq("HPThree"), eq(String.class))).thenReturn("1111");
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		assertThrows(PRCServiceException.class, ()->fidoHttpComponent.requestServiceRegist("", "", "", ""));
	}
	

	@DisplayName("FIDO 인증서 해지")
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
			"8007076999994",
			})
	@ParameterizedTest
	void requestRevokeCertTest(String PerBusNo) throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestRevokeCert("", "", "", ""));
	}
	
	@DisplayName("FIDO 인증서 해지-오류")
	@ValueSource(strings = { 
			"8007071999994",
	})
	@ParameterizedTest
	void requestRevokeCertTest2(String PerBusNo) throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestRevokeCert("", "", "", ""));
	}
	
	
	@DisplayName("FIDO 서비스 해지")
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
			})
	@ParameterizedTest
	void requestServiceReleaseTest(String PerBusNo) throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestServiceRelease("", "", "", ""));
	}
	
	@DisplayName("FIDO 서비스 해지-오류")
	@ValueSource(strings = { 
			"8007071999994",
			"8007072999994",
			"8007073999994",
			"8007074999994",
			"8007075999994",
			"8007076999994",
	})
	@ParameterizedTest
	void requestServiceReleaseTest2(String PerBusNo) throws Exception {
		when(sessionManager.getGlobalValue(eq("PerBusNo"), eq(String.class))).thenReturn(PerBusNo);
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestServiceRelease("", "", "", ""));
	}
	
	@DisplayName("FIDO 거래결과확인")
	@ValueSource(strings = { 
			"8007071999994",
			})
	@ParameterizedTest
	void trResultConfirmTest(String PerBusNo) throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.trResultConfirm(""));
	}
	
	@DisplayName("FIDO 거래결과확인-오류")
	@ValueSource(strings = { 
			"8007071999994",
	})
	@ParameterizedTest
	void trResultConfirmTest2(String PerBusNo) throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"trId\":\"12345\"},\"resultCode\":\"999\",\"resultMsg\":\"success\"}");
		assertThrows(PRCServiceException.class, ()->fidoHttpComponent.trResultConfirm(""));
	}
	
	@DisplayName("FIDO 등록상태 조회2")
	@ValueSource(strings = { 
			"8007071999994",
			})
	@ParameterizedTest
	void checkRegisteredStatus2Test(String PerBusNo) throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.checkRegisteredStatus2("", "", "", "", null));
	}
	
	@DisplayName("디지털인증서 발급")
	@ValueSource(strings = { 
			"000$정상처리되었습니다.$1952608$77465613442455031832$20260116103825$000001$",
			"999$Error occurred during processing.$410$이미 유효한 인증서가 존재합니다.$LOANTE80$000005086112$ $ $"
	})
	@ParameterizedTest
	void getRaCertTest(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(eq(20), anyString())).thenReturn(oppraRes);
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");

		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenReturn(oppra);
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn("000");
				});
			) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertNotNull(fidoHttpComponent.getRaCert(""));
		}
	}
	
	@DisplayName("디지털인증서 발급 - 오류응답")
	@ValueSource(strings = { 
			"820",
			"216",
			"922",
			"926",
			"927",
			"217",
			"214",
			"210",
			"211",
			"999",
			"",
	})
	@ParameterizedTest
	void getRaCertTest3(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(anyInt(), anyString())).thenReturn(oppraRes);
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");
		
		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(certUtils.getOppra()).thenReturn(oppra);
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doNothing().when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn(oppraRes);
				});
				) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertThrows(PRCServiceException.class, () -> fidoHttpComponent.getRaCert(""));
		}
	}
	
	
	@DisplayName("디지털인증서 발급 - 예외발생")
	@ValueSource(strings = { 
			"000",
	})
	@ParameterizedTest
	void getRaCertTest4(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(anyInt(), anyString())).thenThrow(new IniOPPRAReadException("999"));
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");
		
		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(certUtils.getOppra()).thenReturn(oppra);
		
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doThrow(new IniOPPRAReadException("999")).when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn(oppraRes);
				});
				) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertThrows(PRCServiceException.class, () -> fidoHttpComponent.getRaCert(""));
		}
	}
	
	@DisplayName("디지털인증서 발급 - 예외발생")
	@ValueSource(strings = { 
			"000",
	})
	@ParameterizedTest
	void getRaCertTest5(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(anyInt(), anyString())).thenThrow(new IniOPPRAIllegalFormatException("999"));
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");
		
		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(certUtils.getOppra()).thenReturn(oppra);
		
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doThrow(new IniOPPRAReadException("999")).when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn(oppraRes);
				});
				) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertThrows(PRCServiceException.class, () -> fidoHttpComponent.getRaCert(""));
		}
	}
	
	@DisplayName("디지털인증서 발급 - 예외발생")
	@ValueSource(strings = { 
			"000",
	})
	@ParameterizedTest
	void getRaCertTest6(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(anyInt(), anyString())).thenReturn(oppraRes);
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");
		
		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenThrow(new PRCServiceException(""));
		when(certUtils.getOppra()).thenReturn(oppra);
		
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doThrow(new IniOPPRAReadException("999")).when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn(oppraRes);
				});
				) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertThrows(PRCServiceException.class, () -> fidoHttpComponent.getRaCert(""));
		}
	}
	
	@DisplayName("디지털인증서 발급 - 예외발생")
	@ValueSource(strings = { 
			"000",
	})
	@ParameterizedTest
	void getRaCertTest7(String oppraRes) throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		when(oppra.requestRAW(anyInt(), anyString())).thenReturn(oppraRes);
		when(oppra.getResCommonPart()).thenReturn("");
		when(oppra.getResDataPart()).thenReturn("");
		
		when(sessionManager.getGlobalValue(eq("certIssueSession"), eq(Map.class))).thenReturn(new HashMap<String, Object>());
		when(certUtils.getRegOppra()).thenReturn(oppra);
		when(certUtils.getOppra()).thenReturn(oppra);
		
		try (MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(
				OppraMessageDataParser.class, (mock, context) -> {
					doThrow(new Exception()).when(mock).setLoopData(anyString(), anyString());
					when(mock.getResCode()).thenReturn(oppraRes);
				});
				) {
			HttpResponse<String> response = mock(HttpResponse.class);
			when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
			when(response.statusCode()).thenReturn(200);
			when(response.body()).thenReturn("{\"resultData\":{\"registeredProtocolFamily\":[\"uaf\"],\"registeredList\":[{\"loginId\":\"LNTEST30\",\"deviceList\":[{\"aaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"-wVCjUY_TIAz3VBaN81iiK_hnuIcBeRHjAttYYTj7C8\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"certList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"deviceId\":\"NWM4NDVjY2EtYWJjOC0zMGMxLTkxNDktOGYzNzNjZDI0OWMy\"}]}],\"regYn\":\"Y\",\"acceptedAaidList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
			assertThrows(PRCServiceException.class, () -> fidoHttpComponent.getRaCert(""));
		}
	}
	
	
	@DisplayName("디지털인증서 폐기")
	@Test
	void RACertDeleteTest() throws Exception {
		when(certUtils.getSerialNumByRegnum(any(), any())).thenReturn("1234567");
		when(kftcCertComponent.changeStatusCert(any(), any(), eq("30"))).thenAnswer(o-> {
			Map<String, String> deleteCertReturn = new HashMap<String, String>();
			return deleteCertReturn;
		});
		assertNotNull(fidoHttpComponent.RACertDelete("", ""));
	}
	@DisplayName("디지털인증서 폐기-오류")
	@Test
	void RACertDeleteTest1() throws Exception {
		when(certUtils.getSerialNumByRegnum(any(), any())).thenThrow(new PRCServiceException(""));
		when(kftcCertComponent.changeStatusCert(any(), any(), eq("30"))).thenAnswer(o-> {
			Map<String, String> deleteCertReturn = new HashMap<String, String>();
			return deleteCertReturn;
		});
		assertThrows(PRCServiceException.class, ()->fidoHttpComponent.RACertDelete("", ""));
	}
	
	@DisplayName("디지털인증서 폐기-오류")
	@Test
	void RACertDeleteTest2() throws Exception {
		when(certUtils.getSerialNumByRegnum(any(), any())).thenReturn("1234567");
		when(kftcCertComponent.changeStatusCert(any(), any(), eq("30"))).thenAnswer(o-> {
			return null;
		});
		assertThrows(PRCServiceException.class, ()->fidoHttpComponent.RACertDelete("", ""));
	}
	
	@DisplayName("FIDO svcTrID 생성")
	@Test
	void getFidoSvctrIdTest() throws Exception {
		when(fidoSequenceGenerator.next()).thenReturn(1L);
		assertNotNull(fidoHttpComponent.getFidoSvctrId());
	}
	
	
	@DisplayName("등록허용 인증장치 목록 조회")
	@Test
	void allowedAuthnrJSONObjectTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"aaidAllowList\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"}]},\"resultCode\":\"000\",\"resultMsg\":\"정상처리 되었습니다.\"}");
		assertNotNull(fidoHttpComponent.allowedAuthnr(new JSONObject()));
	}
	
	@DisplayName("등록 상태 조회")
	@Test
	void checkRegisteredStatusJSONObjectTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119602\",\"aaid\":\"0012#0003\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344220260422123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"},{\"issueType\":\"03\",\"notAfter\":\"20260626235959\",\"serialNumber\":\"5119604\",\"aaid\":\"0012#0021\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"oid\":\"1.2.410.200005.1.1.4.3\",\"subjectDn\":\"CN=업무테스트()002334220260424123000001, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"keyId\":\"-adlzKydNAh8jg-jplL-OtHJRyQnRRpur62LhxvvTPU\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"keyId\":\"gymUakGHvdoqiv5xD9TtU0a0SfmUw2wlq-eGKPedH5s\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.checkRegisteredStatus(new JSONObject()));
	}
	
	@DisplayName("바이오 인증서 서명")
	@Test
	void requestP7SignTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"resultCode\":\"000\", \"trId\":\"17798461263915014037\", \"resultMsg\":\"success\"}, \"resultCode\":\"000\", \"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestP7Sign(new JSONObject("{\"issueType\":\"03\",\"bizReqType\":\"app\",\"signReqDt\":1779846126211,\"loginId\":\"LOANTE80\",\"tranData\":\"loginYn=Y\",\"appId\":\"ios:bundle-id:com.scbank.ma40\",\"svcTrId\":\"12605271042064000000\",\"deviceId\":\"NTk1MjY2QzItMjFFMS00QjE3LUFGREItNzY1MzQwNDgyODRG\",\"verifyType\":\"16384\",\"command\":\"requestP7Sign\"}")));
	}
	
	@DisplayName("Raon 간편인증(디지털 인증서 연동X)")
	@Test
	void requestServiceAuthTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"resultCode\":\"000\", \"trId\":\"17798461263915014037\", \"resultMsg\":\"success\"}, \"resultCode\":\"000\", \"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestServiceAuth(new JSONObject("{\"issueType\":\"03\",\"bizReqType\":\"app\",\"signReqDt\":1779846126211,\"loginId\":\"LOANTE80\",\"tranData\":\"loginYn=Y\",\"appId\":\"ios:bundle-id:com.scbank.ma40\",\"svcTrId\":\"12605271042064000000\",\"deviceId\":\"NTk1MjY2QzItMjFFMS00QjE3LUFGREItNzY1MzQwNDgyODRG\",\"verifyType\":\"16384\",\"command\":\"requestP7Sign\"}")));
	}
	
	@DisplayName("거래결과 확인")
	@Test
	void trResultConfirmTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"resultCode\":\"000\", \"trId\":\"17798461263915014037\", \"resultMsg\":\"success\"}, \"resultCode\":\"000\", \"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.trResultConfirm(new JSONObject()));
	}
	@DisplayName("거래결과 확인")
	@Test
	void trResultConfirmTest2() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"resultCode\":\"000\", \"trId\":\"17798461263915014037\", \"resultMsg\":\"success\"}, \"resultCode\":\"000\", \"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.trResultConfirm(new JSONObject()));
	}
	
	@DisplayName("FIDO 서비스 해지")
	@Test
	void requestUserMngtinitAuthnrTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"resultCode\":\"000\", \"trId\":\"17798461263915014037\", \"resultMsg\":\"success\"}, \"resultCode\":\"000\", \"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.requestUserMngtinitAuthnr(new JSONObject()));
	}
	
	@DisplayName("나의 등록 인증장치 조회 결과")
	@Test
	void checkRegisteredStatusResultTest() throws Exception {
		HttpResponse<String> response = mock(HttpResponse.class);
		when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn("{\"resultData\":{\"issuedCertList\":[{\"issueType\":\"03\",\"notAfter\":\"20260627235959\",\"serialNumber\":\"5119832\",\"aaid\":\"0012#0012\",\"issuerDn\":\"CN=yessignCA-Test Class 5, OU=AccreditedCA, O=yessign, C=kr\",\"keyId\":\"5WkCygkjccLGKoaXWY45d9pTJfH_UCTFSJEj5y_sQxE\",\"oid\":\"1.2.410.200005.1.1.4.4\",\"subjectDn\":\"CN=업무테스트()002344720251229123000002, OU=KFB, OU=personal4IB, O=yessign, C=kr\"}],\"registeredProtocolFamily\":[\"uaf\"],\"aaidList\":{\"accepted\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0003\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0007\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"},{\"verificationNm\":\"Fingerprint\",\"aaid\":\"0012#0021\",\"vendorId\":\"0012\",\"verificationType\":\"2\",\"vendorNm\":\"RAONSECURE\"}],\"registered\":[{\"verificationNm\":\"Pin\",\"aaid\":\"0012#0012\",\"keyId\":\"5WkCygkjccLGKoaXWY45d9pTJfH_UCTFSJEj5y_sQxE\",\"vendorId\":\"0012\",\"verificationType\":\"16384\",\"vendorNm\":\"RAONSECURE\"}]},\"regYn\":\"Y\"},\"resultCode\":\"000\",\"resultMsg\":\"success\"}");
		assertNotNull(fidoHttpComponent.checkRegisteredStatusResult(new JSONObject()));
	}
	
	
}