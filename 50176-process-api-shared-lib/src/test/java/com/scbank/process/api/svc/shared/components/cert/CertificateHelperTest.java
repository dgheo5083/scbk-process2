package com.scbank.process.api.svc.shared.components.cert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.initech.ocspgd.OCSPGD;
import com.initech.ocspgd.OCSPGDSearchRespCodeType;
import com.initech.ocspgd.util.Code51DataParser;
import com.initech.ocspgd.util.InquiryMsg;
import com.initech.oppra.IniOPPRA;
import com.initech.oppra.util.JohoeiDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.dao.InfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.InfoResult;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;
import com.wizvera.crypto.CertUtil;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT) // level가장관대하나 권장하지는 않습니다.
@Slf4j
public class CertificateHelperTest {

	@Mock
	private ISessionContextManager sessionManager;

	@Mock private LdapInfoDao ldapInfoDao; 
	@Mock private InfoDao infoDao; 
	
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

	@DisplayName("getIssuerCodeTest")
	@ParameterizedTest
	@ValueSource(strings = { "CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr", "CN=TradeSign-Test Class 5",
			"CN=CrossCert-Test Class 5", "CN=SignKorea-Test Class 5", "CN=SignGate-Test Class 5", "CN=NCA-Test Class 5",
			"TEST" })
	void getIssuerCodeTest(String value) throws Exception {
		try (MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);) {
			mockCertUtil.when(() -> CertUtil.getCertificatePolicyOID(any())).thenReturn(null);
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");

			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);

			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", value);
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", "c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004");
			log.debug(certificateHelper.toString());
			certificateHelper.isYessign();
			certificateHelper.isKFB();
			certificateHelper.isCrossCertificate();
			certificateHelper.isFincert();
			certificateHelper.checkOID();

		}
	}

	@DisplayName("getIssuerCodeTest")
	@ParameterizedTest
	@ValueSource(strings = { "TEST" })
	void getIssuerCodeTest2(String value) throws Exception {
		try (MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);) {

			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);

			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", value);
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", "TEST");
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			log.debug(certificateHelper.toString());
			certificateHelper.isYessign();
			certificateHelper.isKFB();
			certificateHelper.isCrossCertificate();
			certificateHelper.isFincert();
			certificateHelper.checkOID();
		}
	}

	@Test
	@DisplayName("getSerialTest")
	void getSerialTest() throws Exception {
		try (MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);) {

			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			certificateHelper.getSerialDecimal();
			certificateHelper.getSerialHex();

		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "업무테스트()002304120250902123000004", "TEST" })
	@DisplayName("getIssuerBankCodeTest")
	void getIssuerBankCodeTest(String value) throws Exception {
		try (MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);) {

			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);

			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectCN", value);

			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			certificateHelper.getIssuerBankCode();

		}
	}
	
	@DisplayName("인증서로 DB 조회 - 정상")
	@ParameterizedTest
	@CsvSource({
			"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'"
	})
	void getUserInfoByScbDBTest1(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("0");
			ldapInfoResult.setStatus("V");
			ldapInfoResult.setUserid("LOANTE80");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid("LOANTE80");
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
					(mock, context) -> {
						when(mock.getResponseDataPart()).thenReturn("");
					});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
					(mock, context) -> {
						when(mock.requestRAW(any())).thenReturn("");
					});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
					(mock, context) -> {
						when(mock.getRecords()).thenReturn(new String[]{""});
						doNothing().when(mock).setRecord(any());
						when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
						when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
					});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
//			assertThrows(PRCServiceException.class, () -> {
//				certificateHelper.getUserInfoByScbDB("");
//			});
			assertNotNull(certificateHelper.getUserInfoByScbDB("LO"));;
		}
	}
	
	@DisplayName("인증서로 DB 조회 - 고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
//		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'"
	})
	void getUserInfoByScbDBTest2(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("");
			ldapInfoResult.setStatus("V");
			ldapInfoResult.setUserid("LOANTE80");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid("LOANTE80");
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.getUserInfoByScbDB("");
			});
		}
	}
	
	@DisplayName("인증서로 DB 조회 - 고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
//		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'"
	})
	void getUserInfoByScbDBTest3(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("S");
			ldapInfoResult.setUserid("LOANTE80");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid("LOANTE80");
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.getUserInfoByScbDB("");
			});
		}
	}
	
	@DisplayName("인증서로 DB 조회 - INI939 인증서가 유효하지 않습니다.")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
//		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002304120250902123000004'"
	})
	void getUserInfoByScbDBTest4(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("");
			ldapInfoResult.setUserid("LOANTE80");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid("LOANTE80");
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.getUserInfoByScbDB("");
			});
		}
	}
	
	@DisplayName("인증서로 DB 조회 - 고객님의 금융인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personal4IB,OU=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,OU=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,ou=personalF,OU=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=yessign,OU=personalF,ou=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,OU=personalF,ou=TEST,cn=업무테스트()002304120250902123000004'"
	})
	void getUserInfoByScbDBTest5(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("V");
			ldapInfoResult.setUserid(null);
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid(null);
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.getUserInfoByScbDB("");
			});
		}
	}
	
	@DisplayName("인증서로 DB 조회 - 그냥 넘어가짐")
	@ParameterizedTest
	@CsvSource({
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004', 'MD'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,OU=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004', 'NS'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,OU=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004', 'OC'"
	})
	void getUserInfoByScbDBTest6(String issuerDn, String subjectDN, String dataType) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("V");
			ldapInfoResult.setUserid(null);
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		when(infoDao.selectSerialInfo(any(), any())).thenAnswer(i -> {
			InfoResult infoResult = new InfoResult();
			infoResult.setUserid(null);
			List<InfoResult> infoList = new ArrayList<InfoResult>();
			infoList.add(infoResult);
			return infoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertNotNull(certificateHelper.getUserInfoByScbDB(dataType));
		}
	}
	
	
	@DisplayName("LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크 유효하면 true")
	@ParameterizedTest
	@CsvSource({
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=TEST,ou=personal4IB,ou=TEST,cn=업무테스트()002104120250902123000004'",
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
		"'CN=TESTCA-Test Class 5,OU=AccreditedCA,O=TEST,C=kr', 'c=kr,o=TEST,OU=personalF,ou=TEST,cn=업무테스트()002304120250902123000004'",
	})
	void chkCertStatusTest1(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("V");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertEquals(true, certificateHelper.chkCertStatus());
		}
		
	}
	
	@DisplayName("고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
	})
	void chkCertStatusTest2(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag(null);
			ldapInfoResult.setStatus("V");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.chkCertStatus();
			});
		}
	}
	
	@DisplayName("고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
	})
	void chkCertStatusTest3(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("S");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.chkCertStatus();
			});
		}
	}
	
	@DisplayName("INI939 증서가 유효하지 않습니다.")
	@ParameterizedTest
	@CsvSource({
		"'CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr', 'c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004'",
	})
	void chkCertStatusTest4(String issuerDn, String subjectDN) throws Exception {
		
		when(ldapInfoDao.selectOppraLdapInfo(any())).thenAnswer(i -> {
			LdapInfoResult ldapInfoResult = new LdapInfoResult();
			ldapInfoResult.setRaflag("1");
			ldapInfoResult.setStatus("");
			List<LdapInfoResult> ldapInfoList = new ArrayList<LdapInfoResult>();
			ldapInfoList.add(ldapInfoResult);
			return ldapInfoList;
		});
		
		try (
				MockedStatic<CertUtil> mockCertUtil = mockStatic(CertUtil.class);
				MockedStatic<RuntimeContext> mockRuntimeContext = mockStatic(RuntimeContext.class);
				MockedStatic<InquiryMsg> mockInquiryMsg = mockStatic(InquiryMsg.class);
				MockedStatic<Code51DataParser> mockCode51DataParser = mockStatic(Code51DataParser.class);
				MockedConstruction<OCSPGD> mockOCSPGD = mockConstruction(OCSPGD.class,
						(mock, context) -> {
							when(mock.getResponseDataPart()).thenReturn("");
						});
				MockedConstruction<IniOPPRA> mockIniOPPRA = mockConstruction(IniOPPRA.class,
						(mock, context) -> {
							when(mock.requestRAW(any())).thenReturn("");
						});
				MockedConstruction<JohoeiDataParser> mockJohoeiDataParser = mockConstruction(JohoeiDataParser.class,
						(mock, context) -> {
							when(mock.getRecords()).thenReturn(new String[]{""});
							doNothing().when(mock).setRecord(any());
							when(mock.getCertStatus()).thenReturn("10"); // RA조회시 인증서 상태 코드
							when(mock.getResCode()).thenReturn("000"); // RA조회시 인증서 조회결과 코드
						});
				) {
			
			mockRuntimeContext.when(() -> RuntimeContext.getBean(LdapInfoDao.class)).thenReturn(ldapInfoDao);
			mockRuntimeContext.when(() -> RuntimeContext.getBean(InfoDao.class)).thenReturn(infoDao);
			
			mockCertUtil.when(() -> CertUtil.getIssuerDN(any(), eq(false), eq(false))).thenReturn("issuerDN");
			
			mockInquiryMsg.when(() -> InquiryMsg.createStatusCheckMsg(dummyCert)).thenReturn("C=KR,O=KICA,OU=AccreditedCA,CN=signGATE FTCA07$F70194915F29B64CA172CA11490B5CAFCFD87E0C$00B824$http://211.35.96.115:9020/OCSPServer$1.2.410.200004.5.2.1.2$");
			
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.ResCode))).thenReturn("000");
			mockCode51DataParser.when(() -> Code51DataParser.getMsg(any(), eq(OCSPGDSearchRespCodeType.Status))).thenReturn("10");
			
			CertificateHelper certificateHelper = new CertificateHelper(dummyCert);
			
			// 멤버변수 강제 변경
			ReflectionTestUtils.setField(certificateHelper, "subjectDN", subjectDN); 
			ReflectionTestUtils.setField(certificateHelper, "issuerDN", issuerDn);
			
			assertThrows(PRCServiceException.class, () -> {
				certificateHelper.chkCertStatus();
			});
		}
	}
	
	
	
	
}