package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.InputStream;
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
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRASystemException;
import com.initech.oppra.util.JohoeiDataParser;
import com.initech.oppra.util.OppraMessageDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
public class CertUtilsTest {
	
	@InjectMocks CertUtils certUtils;
	
	@Mock
	private ISessionContextManager sessionManager;

	@Mock private LdapInfoDao ldapInfoDao; 
	@Mock private ICodeManager commonCode;
	
	MockedStatic<PropertiesUtils> mockPropertiesUtils;
	
	@BeforeEach
	void setUp() {
		mockPropertiesUtils = Mockito.mockStatic(PropertiesUtils.class);
	}
	
	@AfterEach
	void tearDown() {
		mockPropertiesUtils.close();
	}
	
	X509Certificate dummyCert = new X509Certificate() {
		public X500Principal getSubjectX500Principal() {
	        return new X500Principal("CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr");
	    }
		
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
			return new BigInteger("1233457");
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

	@DisplayName("안전한 세션값 꺼내기")
	@ParameterizedTest
	@ValueSource(strings = { "certIssueSession", "certDisposalSession" })
	void getCertSessionTest(String value) throws Exception {
		if("certIssueSession".equals(value)) {
			when(sessionManager.getGlobalValue(any(), eq(Map.class))).thenReturn(Map.of("", ""));
			assertNotNull(certUtils.getCertSession(value));
			
		} else {
			when(sessionManager.getGlobalValue(any(), eq(Map.class))).thenReturn(null);
			assertThrows(PRCServiceException.class, () -> {
				certUtils.getCertSession(value);
			});
		}
	}
	
	@DisplayName("금결원 인증서 발급 정보 조회")
	@ParameterizedTest
	@CsvSource({
		"LOANTE80 , 0023LOANTE80 , 업무테스트",
		"LOANTE82 , 0023LOANTE82 , 업무테스트",
		"NAMJ420 , 0023NAMJ420 , 업무테스트",
		"NAMJ422 , 0023NAMJ422 , 업무테스트",
		"KIMJ420 , 0023KIMJ420 , 업무테스트",
		"LEEJ420 , 0023LEEJ420 , 업무테스트",
		"LEEJ422 , 0023LEEJ422 , 업무테스트",
		"NAMJ420__DELETECERT__USER_ID , 0023NAMJ420 , 업무테스트",
		"NAMJ422__DELETECERT__USER_ID , 0023NAMJ422 , 업무테스트",
		"OHJ420 , 0023OHJ420 , 업무테스트",
		"OHJ422 , 0023OHJ420 , 업무테스트",
		"PARCKJ420 , 0023PARCKJ420 , 업무테스트",
		"CHOIJ420 , 0023PARCKJ420 , 업무테스트",
		})
	void getOppResult(String userId, String oppUserid, String deptPersonName) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfoUserid(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setSerial("12345");
			ldapInfoResult.setUserid(userId);

			if("LOANTE80".equals(userId) || "LOANTE82".equals(userId)) {
				ldapInfoResult.setPolicy("84");
				ldapInfoResult.setUsercertificate("20".getBytes());
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=personal4IB,o=yessign,c=kr");
				
			} else if("NAMJ420__DELETECERT__USER_ID".equals(userId) || "NAMJ422__DELETECERT__USER_ID".equals(userId)) {
				ldapInfoResult.setUserid("NAMJ420");
				ldapInfoResult.setPolicy("84");
				ldapInfoResult.setUsercertificate("20".getBytes());
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=corporation4EC,o=yessign,c=kr");
				
			} else if("KIMJ420".equals(userId)) {
				ldapInfoResult.setPolicy("01");
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=corporation,o=yessign,c=kr");
				
			} else if("LEEJ420".equals(userId) || "LEEJ422".equals(userId)) {
				ldapInfoResult.setPolicy("01");
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=xUse4Esero,o=yessign,c=kr");
				
			} else if("OHJ420".equals(userId) || "OHJ422".equals(userId)) {
				ldapInfoResult.setPolicy("84");
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=personal4IB,o=yessign,c=kr");
				
			} else if("PARCKJ420".equals(userId)) {
				ldapInfoResult.setPolicy("16");
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=personal4IB,o=yessign,c=kr");
				
			} else {
				ldapInfoResult.setPolicy("84");
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=personalF,o=yessign,c=kr");
				
			}
			
			LdapInfoResult ldapInfoResult2 = new LdapInfoResult();
			ldapInfoResult2.setPolicy("34");
			LdapInfoResult ldapInfoResult3 = new LdapInfoResult();
			ldapInfoResult3.setPolicy("44");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			ldapInfoList.add(ldapInfoResult2);
			ldapInfoList.add(ldapInfoResult3);
			return ldapInfoList;
		});
		
		try (
				MockedStatic<LoggerFactory> mockLoggerFactory = mockStatic(LoggerFactory.class);
				MockedStatic<CertificateFactory> mockCertificateFactory = mockStatic(CertificateFactory.class);
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
					(mock, context) -> {
						when(mock.requestRAW(any())).thenReturn("");
					});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
					(mock, context) -> {
						when(mock.getRecords()).thenReturn(new String[]{""});
						doNothing().when(mock).setRecord(any());
						if("NAMJ420__DELETECERT__USER_ID".equals(userId) || "LOANTE80".equals(userId) || "NAMJ422".equals(userId) || "LEEJ420".equals(userId) || "OHJ420".equals(userId) ) {
							when(mock.getCertStatus()).thenReturn("40"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						} else {
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드	
						}
						
					});
				) {
			Logger mockLogger = mock(Logger.class);
			when(mockLogger.isDebugEnabled()).thenReturn(true);
			mockLoggerFactory.when(() -> LoggerFactory.getLogger(any(Class.class))).thenReturn(mockLogger);
			
			CertificateFactory certificateFactory = Mockito.mock(CertificateFactory.class);
			mockCertificateFactory.when(() -> CertificateFactory.getInstance("X.509")).thenReturn(certificateFactory);
			
			when(certificateFactory.generateCertificate(any(InputStream.class))).thenReturn(dummyCert);
			assertNotNull(certUtils.getOppResult(userId, oppUserid, deptPersonName));
		}
	}
	
	
	@DisplayName("OrgCode 조회")
	@ParameterizedTest
	@ValueSource(strings = { "c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004", "", "OU=, ou=" })
	void getOrgCodeTest(String value) throws Exception {
		assertNotNull(certUtils.getOrgCode(value));
	}
	
	@DisplayName("IniOPPRA 반환")
	@Test
	void getRegOppraTest() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			
		})) {
			assertNotNull(certUtils.getRegOppra());
		}
	}
	
	@DisplayName("IniOPPRA 반환오류")
	@Test
	void getRegOppraTest2() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			doThrow(new IniOPPRASystemException()).when(mock).initialize();
		})) {
			assertThrows(PRCServiceException.class, () -> {
				certUtils.getRegOppra();
			});
		}
	}
	
	@DisplayName("IniOPPRA 반환")
	@Test
	void getOppraTest() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			
		})) {
			assertNotNull(certUtils.getOppra());
		}
	}
	
	@DisplayName("IniOPPRA 반환오류")
	@Test
	void getOppraTest2() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			doThrow(new IniOPPRASystemException()).when(mock).initialize();
		})) {
			assertThrows(PRCServiceException.class, () -> {
				certUtils.getOppra();
			});
		}
	}
	
	@DisplayName("OPPRA 닫기")
	@Test
	void closeOppraTest() throws Exception {
		certUtils.closeOppra(null);
	}
	
	@DisplayName("OPPRA 닫기 오류")
	@Test
	void closeOppraTest2() throws Exception {
		IniOPPRA oppra = mock(IniOPPRA.class);
		doThrow(new IniOPPRASystemException()).when(oppra).close();
		assertThrows(PRCServiceException.class, () -> {
			certUtils.closeOppra(oppra);
		});
	}
	
	@DisplayName("사용자 serialNumber 조회")
	@Test
	void getOPPRASerialTest() throws Exception {
		certUtils.getOPPRASerial(dummyCert);
	}
	
	@DisplayName("주민번호로 CA조회")
	@Test
	void getSerialNumByRegnumTest() throws Exception {
		try (
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
					when(mock.requestRAW(any())).thenReturn("000$정상처리되었습니다.$0023$kfbadmin0309$44$000005117574$002344720251229123000002$cn=업무테스트()002344720251229123000002,ou=KFB,ou=personal4IB,o=yessign,c=kr$*************$20260515000000$20260615235959$10$001$001$000$정상처리되었습니다.$ $ $");
				});
				MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getLoopSize()).thenReturn(2);
					when(mock.getResCode()).thenReturn("000");
					when(mock.getCodeData(any())).thenReturn("123456");
				});
				) {
			certUtils.getSerialNumByRegnum("", "");
		}
	}
	
	@DisplayName("주민번호로 CA조회-조회결과없음")
	@Test
	void getSerialNumByRegnumTest2() throws Exception {
		try (
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
					when(mock.requestRAW(any())).thenReturn("");
				});
				MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
					when(mock.getLoopSize()).thenReturn(2);
					when(mock.getResCode()).thenReturn("999");
					when(mock.getCodeData(any())).thenReturn("123456");
				});
				) {
			certUtils.getSerialNumByRegnum("", "");
		}
	}
	
	@DisplayName("아이디로 CA조회")
	@Test
	void getSerialNumByUserIDTest() throws Exception {
		try (
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
					when(mock.requestRAW(any())).thenReturn("");
				});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class, (mock, context) -> {
					when(mock.getRecords()).thenReturn(new String[] {"1"});
				});
				) {
			certUtils.getSerialNumByUserID("", "");
		}
	}
	
	
	@DisplayName("문자열 format")
	@Test
	void formatStringTest() throws Exception {
		assertEquals("000000000001", certUtils.formatString("1"));
	}
	
	@DisplayName("호스트로부터 내려온 전자 스페이스를 처리하는 메소드")
	@ParameterizedTest
	@ValueSource(strings = { 
			"！＠＃＄％＾＆＊（）＿＋－＝［］｛",
			"ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ",
			"ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ",
			"ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ",
			"１２３４５６７８９０",
			":　;　'　,　.　/　~　`",
			"　",
			"　　",
			})
	void byteStringConvertTest(String deptPersonName) throws Exception {
		assertEquals("", certUtils.byteStringConvert(null, 80));
		assertEquals("", certUtils.byteStringConvert("", 26));
		certUtils.byteStringConvert(deptPersonName, 10);
	}
	
	
	
	@DisplayName("CA에 등록된 금융결제원 인증서 조회 - 금융인증서")
	@Test
	void searchByUserIDTest() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			when(mock.requestRAW(any())).thenReturn("");
		});
		MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
			doNothing().when(mock).setLoopData(any(), any());
			when(mock.getLoopSize()).thenReturn(1);
			when(mock.getResCode()).thenReturn("000");
			when(mock.getCodeData("USERID")).thenReturn("LOANTE80");
			when(mock.getCodeData("EVDATE")).thenReturn("20260526");
		});
		) {
			Map<String, String> inputMap = Map.of("CertInquiryType", "F");
			certUtils.searchByUserID("", "", inputMap);
		}
	}
	
	@DisplayName("CA에 등록된 금융결제원 인증서 조회 - 공동인증서")
	@Test
	void searchByUserIDTest2() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			when(mock.requestRAW(any())).thenReturn("");
		});
		MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
			doNothing().when(mock).setLoopData(any(), any());
			when(mock.getLoopSize()).thenReturn(1);
			when(mock.getResCode()).thenReturn("000");
			when(mock.getCodeData("USERID")).thenReturn("LOANTE80");
			when(mock.getCodeData("EVDATE")).thenReturn("20260526");
		});
		) {
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfoResult = new LdapInfoResult();
				ldapInfoResult.setSerial("12345");
				ldapInfoResult.setPolicy("84");
				ldapInfoResult.setUsercertificate("20".getBytes());
				ldapInfoResult.setDn("cn=업무테스트()002304020260318123000002,ou=KFB,ou=personal4IB,o=yessign,c=kr");
				
				LdapInfoResult ldapInfoResult2 = new LdapInfoResult();
				ldapInfoResult2.setPolicy("34");
				LdapInfoResult ldapInfoResult3 = new LdapInfoResult();
				ldapInfoResult3.setPolicy("44");
				List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
				ldapInfoList.add(ldapInfoResult);
				ldapInfoList.add(ldapInfoResult2);
				ldapInfoList.add(ldapInfoResult3);
				return ldapInfoList;
			});
			certUtils.searchByUserID("", "", null);
		}
	}
	
	@DisplayName("CA에 등록된 금융결제원 인증서 조회 - 금융인증서폐기")
	@Test
	void searchByUserIDTest3() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			when(mock.requestRAW(any())).thenReturn("");
		});
		MockedConstruction<OppraMessageDataParser> mockOppraMessageDataParser = mockConstruction(OppraMessageDataParser.class, (mock, context) -> {
			doNothing().when(mock).setLoopData(any(), any());
			when(mock.getLoopSize()).thenReturn(1);
			when(mock.getResCode()).thenReturn("000");
			when(mock.getCodeData("USERID")).thenReturn("LOANTE80");
			when(mock.getCodeData("EVDATE")).thenReturn("20260526");
		});
				) {
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
				return ldapInfoList;
			});
			Map<String, String> inputMap = Map.of("CertInquiryType", "F", "WorkType","RV");
			certUtils.searchByUserID("", "", inputMap);
		}
	}
	
	@DisplayName("certPolicyCode 조회")
	@ParameterizedTest
	@CsvSource({
		"1 , 1 , 1",
		"1 , 1 , 2",
		"2 , 2 , 1",
		"2 , 2 , 2",
		"2 , 2 , 3",
		"2 , 1 , 6",
		})
	void getCertPolicyCodeTest(String caGubunValue, String custGubunValue, String raGubunValue) throws Exception {
		assertNotNull(certUtils.getCertPolicyCode(caGubunValue, custGubunValue, raGubunValue));
	}
	
	@DisplayName("인증서 종류(RA) 조회")
	@ParameterizedTest
	@CsvSource({
		"'1', '1'",
		"'1', '2'",
		"'2', '1'",
		"'2', '2'",
		"'2', '3'",
		"'1', '6'",
		"'1', '7'",
		"'2', '9'",
	})
	void getRANameTest(String CustGubun, String RAGubun) throws Exception {
		certUtils.getRAName(RAGubun, CustGubun);
	}
	
	@DisplayName("인증서 발급 은행명 반환")
	@ParameterizedTest
	@CsvSource({
		"'01', '099'",
		"'02', '011'",
		"'03', '023'",
		"'04', '2'",
		"'05', '3'",
		"'06', '6'",
		"'07', '7'",
	})
	void IssueBankNameTest(String CaGubun, String IssueBank) throws Exception {
		certUtils.IssueBankName(CaGubun, IssueBank);
	}
	
	@DisplayName("전화번호 파싱")
	@ParameterizedTest
	@ValueSource(strings = {
			"01011112222",
			"010 11112222",
			"010",
			"0212341234",
			"021231234",
			"03112341234",
			"0311231234",
			"0111231234",
			"0161231234",
			"0181231234",
			"0191231234",
			"",
			})
	void phoneNumberPartArrayTest(String phoneNum) throws Exception {
		certUtils.phoneNumberPartArray(phoneNum);
		certUtils.phoneNumberPartArray(null);
	}
	
	
	@DisplayName("씨리얼번호로 CA조회")
	@Test
	void checkCertStatusTest() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			when(mock.requestRAW(any())).thenReturn("");
		});
		MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class, (mock, context) -> {
			when(mock.getRecords()).thenReturn(new String[] {"1"});
			doNothing().when(mock).setRecord(any());
			when(mock.getNotAfter()).thenReturn("20260526");
			when(mock.getCertStatus()).thenReturn("10");
			when(mock.getUserID()).thenReturn("0023");
		});
		) {
			certUtils.checkCertStatus("", "");
		}
	}
	
	@DisplayName("씨리얼번호로 CA조회")
	@Test
	void checkCertStatusTest2() throws Exception {
		try (MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class, (mock, context) -> {
			when(mock.requestRAW(any())).thenReturn("");
		});
		MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class, (mock, context) -> {
			when(mock.getRecords()).thenReturn(new String[] {"1"});
			doNothing().when(mock).setRecord(any());
			when(mock.getNotAfter()).thenReturn("20260526");
			when(mock.getCertStatus()).thenReturn("20");
			when(mock.getUserID()).thenReturn("0000");
		});
				) {
			certUtils.checkCertStatus("", "");
		}
	}
	
	
	@DisplayName("인증서만료일계산")
	@ParameterizedTest
	@CsvSource({
		"'20260101', '20270101', 'Y'",
		"'20260228', '20270228', 'Y'",
		"'20261111', '20271111', 'Y'",
		"'20260101', '20260201', 'M'",
		"'20260229', '20260329', 'M'",
		"'20261231', '20270131', 'M'",
		"'20240229', '20240329', 'M'",
		"'20261231', '20270101', 'D'",
		"'20270101', '20270102', 'D'",
		"'20270228', '20270301', 'D'",
		"'20260228', '20260301', 'D'",
		"'20240229', '20240301', 'D'",
		"'20261111', '20261112', 'D'",
	})
	void setExpireDateTest(String indate, String expireDate, String gijun) throws Exception {
		assertEquals(expireDate, certUtils.setExpireDate(indate, gijun, 1));
	}
	
	@DisplayName("BASE64문자열을 x509인증서로 반환")
	@Test
	void getCertificationFromB64Test() throws Exception {
		try (MockedStatic<CertificateFactory> mockCertificateFactory = mockStatic(CertificateFactory.class);) {
			CertificateFactory certificateFactory = Mockito.mock(CertificateFactory.class);
			mockCertificateFactory.when(() -> CertificateFactory.getInstance("X.509")).thenReturn(certificateFactory);
			when(certificateFactory.generateCertificate(any(InputStream.class))).thenReturn(dummyCert);
			certUtils.getCertificationFromB64("");
		}
	}
	
	@DisplayName("Edoc용 LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크")
	@Test
	void getUserInfoByScbDBEdocTest() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(true);
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("1");
				ldapInfo.setStatus("V");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertNotNull(certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)")
	@Test
	void getUserInfoByScbDBEdocTest2() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(true);
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("");
				ldapInfo.setStatus("V");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertThrows(PRCServiceException.class, ()-> certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("고객님이 타행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)")
	@Test
	void getUserInfoByScbDBEdocTest3() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(false);
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("");
				ldapInfo.setStatus("V");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertThrows(PRCServiceException.class, ()-> certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.")
	@Test
	void getUserInfoByScbDBEdocTest4() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(false);
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("1");
				ldapInfo.setStatus("S");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertThrows(PRCServiceException.class, ()-> certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("인증서가 유효하지 않습니다.")
	@Test
	void getUserInfoByScbDBEdocTest5() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(false);
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("1");
				ldapInfo.setStatus("");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertThrows(PRCServiceException.class, ()-> certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("고객님의 공동인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.")
	@Test
	void getUserInfoByScbDBEdocTest6() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(true);
					when(mock.isKFB()).thenReturn(false);
					when(mock.chkOPPRA()).thenReturn(false);
					
				});) {
			
			when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
				LdapInfoResult ldapInfo = new LdapInfoResult();
				ldapInfo.setRaflag("1");
				ldapInfo.setStatus("V");
				
				List<LdapInfoResult> list = new ArrayList<LdapInfoResult>();
				list.add(ldapInfo);
				
				return list;
			});
			assertThrows(PRCServiceException.class, ()-> certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
	@DisplayName("타기관 인증서 OCSP")
	@Test
	void getUserInfoByScbDBEdocTest7() throws Exception {
		try (MockedConstruction<CertificateHelper> mockCertificateHelper = mockConstruction(CertificateHelper.class,
				(mock, context) -> {
					when(mock.getSerial()).thenReturn("");
					when(mock.isYessign()).thenReturn(false);
					when(mock.isKFB()).thenReturn(false);
					when(mock.chkOCSP()).thenReturn(true);

				});) {
			assertNotNull(certUtils.getUserInfoByScbDBEdoc(dummyCert, ""));
		}
	}
	
}