/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 공동인증서로 서명된 문서 검증 공통 컴포넌트
*  작   성  자 : 오은진
*  작   성  일 : 2025.11.07
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2025.11.07/오은진)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.wizvera.WizveraConfig;
import com.wizvera.crypto.CertUtil;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SharedComponent(name = "공동인증서 전자서명인증서 검증", description = "공동인증서 전자서명인증서 검증", author = "오은진")
@RequiredArgsConstructor
public class CertVerifyComponent{

	private final ISessionContextManager SessionManager;

	@Value("${delfino.config.path:}")
	private String configPath;

	/**
	 * 전자서명 검증
	 *
	 * @param dataType(L : 로그인, S : 전자서명, A : 인증서검증값 추출, C : ID/PW 로그인시 인증서 데이터 검증, NS : NTB 전자서명 처리)
	 * @param signData
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "전자서명 검증", description = "전자서명 검증", author = "오은진")
	public HashMap<String, Object> verify(String dataType, String signData, String vidRandom) throws Exception {
		log.debug("<<<<<<<<<<<<<<<<< 공동인증서 전자서명 검증 시작 dataType={}, signData={}", dataType, signData);

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}

		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userID = null;
		String cid = null;
		String subject = null;
		String issuer = null;
		String issuer_code = null;
		SignVerifier signVerifier = null; // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		String orgData = "";
		X509Certificate cert = null;

		// ======================================================
		// 1. 인증서 데이터 추출
		// ======================================================
		if (signData == null || signData.equalsIgnoreCase(""))
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");

		try {
			if(PRCSharedUtils.isSB()) {
				signData = java.net.URLDecoder.decode(signData);
				vidRandom = java.net.URLDecoder.decode(vidRandom);
			}
			signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE); // 서명데이터만검증
			cert = signVerifierResult.getSignerCertificate(); // 서명인증서 획득

			log.debug("<<<<<<<<<<<<<<<<< 서명인증서 cert :: {}", cert);
		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		// ======================================================
		// 2. 인증서의 사용자 정보를 얻어 옴.
		// ======================================================
		if (cert == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// 획득한 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(cert);
		log.debug(certHelper.toString());
		subject = certHelper.getSubjectDN(); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요:INITECH호환(새마을금고)
		issuer = certHelper.getIssuerDN(); // 인증서 발급기관 DN정보(인증서,대문자,cn부터): X509기본설정
		issuer_code = certHelper.getIssuerCode(); // 발급자코드

		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급주체 DN정보 subject :: {}", subject);
		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급기관 DN정보 issuer :: {}", issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급자코드 issuer_code :: {}", issuer_code);

		if (issuer_code == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// ======================================================
		// [선처리]5. OID체크 (OID검증을 통한 인증서 용도 체크)
		// ======================================================
		String CertOID = CertUtil.getCertificatePolicyOID(cert);
		try {
			String checkOID = certHelper.getPolicyCode();
			if (certHelper.checkOID() == true) {
				userInfo.put("OID", CertOID);

				log.debug("<<<<<<<<<<<<<<<<< OID 확인 성공 UserCertOID :: {}", CertOID);
				log.debug("<<<<<<<<<<<<<<<<< OID CA :: {}", checkOID);
			} else {
				log.debug("<<<<<<<<<<<<<<<<< OID 확인 실패 UserCertOID :: {}", CertOID);
				throw new PRCServiceException("INI935", "은행 업무용 인증서를 사용하시기 바랍니다. (증권업무용 인증서 사용 불가) ");
			}
		} catch (Exception oidexc) {
			if (!oidexc.getMessage().startsWith("INI")) {
				throw new PRCServiceException("INI935", "은행 업무용 인증서를 사용하시기 바랍니다. (증권업무용 인증서 사용 불가)");
			} else {
				throw new PRCServiceException("INI935", "은행 업무용 인증서를 사용하시기 바랍니다. (증권업무용 인증서 사용 불가)" + oidexc.getMessage());
			}
		}

		// ======================================================
		// ※ 예외 : 인증서 등록시 필요한 정보를 전달한다.
		// ======================================================
		if ("A".equalsIgnoreCase(dataType)) {
			SessionManager.setGlobalValue("_shttp_client_vid_random_", vidRandom);

			resultMap.put("_shttp_client_vid_random_", vidRandom);
			resultMap.put("userCert", CertUtils.getB64FromCertification(cert));

			log.debug("<<<<<<<<<<<<<<<<< certVerify resultMap ::: {}", resultMap);
			return resultMap;
		}

		// ======================================================
		// 3. DB에 등록된 사용자 정보를 조회 한다.
		// ======================================================
		userInfo = certHelper.getUserInfoByScbDB(dataType);
		userID = (String) userInfo.get("USERID");
		cid = (String) userInfo.get("CID");
		log.debug("<<<<<<<<<<<<<<<<< userInfo : {}", userInfo);
		log.debug("<<<<<<<<<<<<<<<<< DB조회 USERID : {}", userID);
		log.debug("<<<<<<<<<<<<<<<<< DB조회 CID : {}", cid);

		// ======================================================
		// 4. VID체크 (본인확인체크).
		// ======================================================
		String juminNO = (String) userInfo.get("CID"); // DB에서 조회
		boolean checkVidResult = false;
		if (dataType != null && !(dataType.toUpperCase()).equalsIgnoreCase("NS")) {
			try {
				log.debug("<<<<<<<<<<<<<<<<< vidRandom : {}",vidRandom);
				log.debug("<<<<<<<<<<<<<<<<< signVerifierResult.isVerifiableVid() : {}",signVerifierResult.isVerifiableVid());

				checkVidResult = signVerifierResult.verifyVid(juminNO, vidRandom);
				log.debug("<<<<<<<<<<<<<<<<< 본인확인체크 결과값 checkVidResult : {}", checkVidResult);
			} catch (Exception e) {
				log.error("CertVerifyImplService.verify checkVID Exception {} - {}", e.getMessage(), e);
				throw new PRCServiceException("INI101", "본인확인에 실패했습니다.");
			}

			if (checkVidResult) {
				log.debug("본인확인 성공");
			} else {
				log.debug("본인확인 실패");
				throw new PRCServiceException("INI101", "본인확인에 실패했습니다.");
			}
		}

		// ======================================================
		// 6. 인증서 유효성 검증.
		// ======================================================
		String _userID = SessionManager.getLoginValue("UserID", String.class);
		String D756_UserID = SessionManager.getGlobalValue("D756_UserID", String.class);
		String session_userID = null;
		if (_userID != null || D756_UserID != null) {
			if (userID.length() > 10 || (_userID != null && _userID.length() > 10) || (D756_UserID != null && D756_UserID.length() > 10)) {
				throw new PRCServiceException("INI857", "WEBBANK 가입 고객은 인터넷 뱅킹거래가 불가합니다. WEBBANK를 통해 거래해 주시기 바랍니다.");
			}

			// D756 기본계좌변경 진행 시 login처리가되지않아 글로벌세션값을 바라보게 수정
			session_userID = D756_UserID == null ? _userID : D756_UserID;

			if (session_userID != null && !"".equals(session_userID)) {
				if (!session_userID.equalsIgnoreCase(userID)) { // 세션에 있는 UserID 와 공인인증서의 userID 를 비교
					throw new PRCServiceException("INI604", "로그인한 사용자와 제출된 사용자의 아이디가 일치하지 않습니다.");
				}
			}
		}

		// ======================================================
		// 7. 서명검증. :: 거래가 서명일경우만 실행.
		// ======================================================
		if ("S".equalsIgnoreCase(dataType)) {
			String LoginUserID = SessionManager.getLoginValue("UserID", String.class);
			if (LoginUserID != null && !userID.equalsIgnoreCase(LoginUserID)) {
				throw new PRCServiceException("INI100", "본인확인에 실패했습니다. 로그인한 사용자와 제출한 인증서 사용자가 다릅니다.");
			}

			String NTBSignSSN = SessionManager.getGlobalValue("NOTLOGINSSN", String.class); // NTB 전자서명시 본인인증 체크 로직 추가
			if (NTBSignSSN != null) {
				boolean checkVidResult_NTB = false;
				try {
					checkVidResult = signVerifierResult.verifyVid(juminNO, vidRandom);
					checkVidResult_NTB = signVerifierResult.verifyVid(NTBSignSSN, vidRandom);
					log.debug("<<<<<<<<<<<<<<<<< checkVidResult_NTB : {}", checkVidResult_NTB);

					if (!checkVidResult_NTB) {
						log.error("CertVerifyImplService.verify NTB checkVidResult false {}", NTBSignSSN.trim().length());
						throw new PRCServiceException("", "fail NTB checkVid");
					}
				} catch (Exception e) {
					log.error("CertVerifyImplService.verify NTB checkVID Exception {} - {}", e.getMessage(), e);
					throw new PRCServiceException("INI201", "본인확인에 실패했습니다. 본인 인증서를 제출하여 주십시오.");
				}
			}

			orgData = signVerifierResult.getSignedRawData(); // 서명 원본 데이터
			log.debug("<<<<<<<<<<<<<<<<< 서명문서원본={}", orgData);
			orgData = java.net.URLDecoder.decode(signVerifierResult.getSignedRawData()); // 서명 원본데이터 저장
			log.debug("<<<<<<<<<<<<<<<<< 서명문서해독={}", orgData);

			SessionManager.setGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME, orgData);
			SessionManager.setGlobalValue(PRCSharedConstants.SIGN_DATA_NAME, signData);

			log.debug("<<<<<<<<<<<<<<<<< 공동인증서 서명검증 완료");
		}

		// ======================================================
		// 8. ID PW 로그인하여 본인확인하기
		// ======================================================
		if ("C".equalsIgnoreCase(dataType)) {
			String LoginUserID = SessionManager.getLoginValue("UserID", String.class);
			if (!userID.equalsIgnoreCase(LoginUserID)) {
				throw new PRCServiceException("INI101", "본인확인에 실패했습니다. 로그인한 사용자와 제출한 인증서 사용자가 다릅니다.");
			}
		}

		String certSerial = certHelper.getSerial(); // 인증서시리얼(10진수) , 12자리로 변환
		String certSerialHex = certHelper.getSerialHex(); // 인증서시리얼(16진수) 0A10
		java.text.DateFormat myDate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm ss");
		java.util.Date NotBefore = cert.getNotBefore(); // 인증서 발급일
		java.util.Date NotAfter = cert.getNotAfter(); // 인증서 만기일
		java.util.Date currentTime = new java.util.Date();// 현재 시간
		int dateformat = (int) ((NotAfter.getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000));

		log.debug("<<<<<<<<<<<<<<<<< 로그인한 ID : " + session_userID);
		log.debug("<<<<<<<<<<<<<<<<< 공인인증서 ID : " + userID);
		log.debug("<<<<<<<<<<<<<<<<< 주민등록번호 : " + cid);
		log.debug("<<<<<<<<<<<<<<<<< 발급자 : " + issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급대상 : " + subject);
		log.debug("<<<<<<<<<<<<<<<<< 발급일 : " + myDate.format(NotBefore));
		log.debug("<<<<<<<<<<<<<<<<< 만료일 : " + myDate.format(NotAfter));
		log.debug("<<<<<<<<<<<<<<<<< Serial : " + certSerial);
		log.debug("<<<<<<<<<<<<<<<<< 현재시간 : " + myDate.format(currentTime));
		log.debug("<<<<<<<<<<<<<<<<< 고객님의 인증서는 {}일 후에 만료가 됩니다.", dateformat);

		// ======================================================
		// # 사용자 정보를 세션에 저장함.
		// ======================================================
		SessionManager.setGlobalValue("session_cid", cid);
		SessionManager.setGlobalValue("cid", cid);
		SessionManager.setGlobalValue("session_UserID", userID);
		SessionManager.setGlobalValue("certID", userID);
		SessionManager.setGlobalValue("uid", userID);
		SessionManager.setGlobalValue("policy", StringUtils.nvl((String) userInfo.get("POLICY"), ""));
		SessionManager.setGlobalValue("serial", userInfo.get("SERIAL"));
		SessionManager.setGlobalValue("subjectDN", subject);
		SessionManager.setGlobalValue("issuerDN", issuer);
		SessionManager.setGlobalValue("certType", issuer_code);
		SessionManager.setGlobalValue("serialSession", certSerialHex);
		// SessionManager.setGlobalObject("issueDate", cert.getNotBefore());
		// SessionManager.setGlobalObject("expireDate", cert.getNotAfter());
		SessionManager.setGlobalValue("CertOID", certHelper.getCertificatePolicyOID());
		SessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "0000");

		// ======================================================
		// 9. 응답데이터 생성
		// ======================================================
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		resultMap.put("userCert", CertUtils.getB64FromCertification(cert));
		log.debug("<<<<<<<<<<<<<<<<< 인증서 검증 완료하였습니다.");

		return resultMap;
	}

	/**
	 * 비대면용 인증서 검증
	 * @param signData
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "비대면용 인증서 검증", description = "비대면용 인증서 검증", author = "오은진")
	public HashMap<String, Object> selfVerify(String signData, String vidRandom) throws Exception {

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userID = null;
		String cid = null;
		String subject = null;
		String issuer = null;
		String issuer_code = null;
		SignVerifier signVerifier = null; // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		X509Certificate cert = null;
		String ConnectType = ""; // 1: cert, 2: id/pw, 8:BlockChainLogin 뱅크사인, 9:디지털인증

		// ======================================================
		// 1. 인증서 데이터 추출
		// ======================================================
		if (signData == null || signData.equalsIgnoreCase(""))
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");

		try {
			if(PRCSharedUtils.isSB()) {
				signData = java.net.URLDecoder.decode(signData);
				vidRandom = java.net.URLDecoder.decode(vidRandom);
			}
			signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
			ConnectType = SessionManager.getLoginValue("ConnectType", String.class);

			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE); // 서명데이터만검증
			cert = signVerifierResult.getSignerCertificate(); // 서명인증서 획득

			log.debug("<<<<<<<<<<<<<<<<< 서명인증서 cert :: {}", cert);
		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		// ======================================================
		// 2. 인증서의 사용자 정보를 얻어 옴.
		// ======================================================
		if (cert == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// 획득한 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(cert);
		log.debug(certHelper.toString());
		subject = certHelper.getSubjectDN(); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요:INITECH호환(새마을금고)
		issuer = certHelper.getIssuerDN(); // 인증서 발급기관 DN정보(인증서,대문자,cn부터): X509기본설정
		issuer_code = certHelper.getIssuerCode(); // 발급자코드

		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급주체 DN정보 subject :: {}", subject);
		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급기관 DN정보 issuer :: {}", issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급자코드 issuer_code :: {}", issuer_code);

		if (issuer_code == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// ======================================================
		// 3. VID체크 (본인확인체크).
		// ======================================================
		String SSN = StringUtils.isEmpty(SessionManager.getLoginValue("PerBusNo", String.class))
				? SessionManager.getGlobalValue("PerBusNo", String.class)
				: SessionManager.getLoginValue("PerBusNo", String.class);
		boolean checkVidResult = false;

		if (StringUtils.isEmpty(SSN))
			throw new PRCServiceException("9999", "로그인이나 본인인증 후 사용 가능합니다");

		ConnectType = SessionManager.getLoginValue("ConnectType", String.class);

		if ("9".equals(ConnectType)) { // 디지털인증서에는 주민번호 암호화 값인 VID값을 추출할수없는 구조이기때문에(ViD 값이 없다고함) CheckVID 로직 SKIP
			checkVidResult = true;
		} else {
			// 로컬인경우 CheckVID 로직 SKIP
			if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
				checkVidResult = true;
			} else {

				try {
					log.debug("<<<<<<<<<<<<<<<<< vidRandom : {}", vidRandom);

					checkVidResult = signVerifierResult.verifyVid(SSN, vidRandom);
					log.debug("<<<<<<<<<<<<<<<<< 본인확인체크 결과값 checkVidResult : {}", checkVidResult);
					log.debug("<<<<<<<<<<<<<<<<< checkVidResult : {}", checkVidResult);
					if (!checkVidResult) {
						log.debug("<<<<<<<<<<<<<<<<< checkVidResult false2");
						SessionManager.setGlobalValue("idVerify", "N");
					} else {
						SessionManager.setGlobalValue("idVerify", "Y");
					}
				} catch (Exception e) {
					SessionManager.setGlobalValue("idVerify", "N");
					log.debug("<<<<<<<<<<<<<<<<< 본인확인에 실패했습니다.!! >> VID Result	:[FALSE]");
					log.error("CertVerifyImplService.selfVerify checkVID Exception {} - {}", e.getMessage(), e);
					throw new PRCServiceException("INI101", "본인확인에 실패했습니다.!!");
				}
			}
		}

		if (checkVidResult) {
			log.debug("본인확인 성공");
		} else {
			log.debug("본인확인 실패");
			throw new PRCServiceException("INI101", "본인확인에 실패했습니다.");
		}

		String certSerial = certHelper.getSerial(); // 인증서시리얼(10진수) , 12자리로 변환
		java.text.DateFormat myDate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm ss");
		java.util.Date NotBefore = cert.getNotBefore(); // 인증서 발급일
		java.util.Date NotAfter = cert.getNotAfter(); // 인증서 만기일
		java.util.Date currentTime = new java.util.Date();// 현재 시간
		int dateformat = (int) ((NotAfter.getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000));

		log.debug("<<<<<<<<<<<<<<<<< 공인인증서 ID :: {} ", userID);
		log.debug("<<<<<<<<<<<<<<<<< 주민등록번호 :: {} ", cid);
		log.debug("<<<<<<<<<<<<<<<<< 발급자 :: {} ", issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급대상 :: {} ", subject);
		log.debug("<<<<<<<<<<<<<<<<< 발급일 :: {} ", myDate.format(NotBefore));
		log.debug("<<<<<<<<<<<<<<<<< 만료일 :: {} ", myDate.format(NotAfter));
		log.debug("<<<<<<<<<<<<<<<<< Serial :: {} ", certSerial);
		log.debug("<<<<<<<<<<<<<<<<< 현재시간 :: {} ", myDate.format(currentTime));
		log.debug("<<<<<<<<<<<<<<<<< 고객님의 인증서는 {}일 후에 만료가 됩니다.", dateformat);

		// ======================================================
		// 9. 응답데이터 생성
		// ======================================================
		log.debug("<<<<<<<<<<<<<<<<< 인증서 검증 완료하였습니다.");
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		resultMap.put("userCert", CertUtils.getB64FromCertification(cert));

		return resultMap;

	}

	/**
	 * 미등록인증서 유효성검증
	 * 1. X509Certificate Data check
	 * > 인증서정보를 뽑아오지못하면 비정상케이스로판별
	 * 2. 인증서 vid check
	 * > 서명한인증서와 세션의 주민번호가 동일사용자인지 판별
	 * 3. OPPRA/OCSPGD check
	 * > 타기관미등록인증서도 허용하기때문에 LDAP 조회는 skip
	 * > 당행 금결원인증서의경우만 LDAP 조회
	 * > OPPRA/OCSPGD 통신 후 상태코드(status)를 받아와 정상("10") 여부 판별
	 * @throws Exception
	 */
	@ComponentOperation(name = "미등록인증서 유효성검증", description = "미등록인증서 유효성검증", author = "오은진")
	public HashMap<String, Object> edcfVerify(String signData, String vidRandom) throws Exception {

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String userID = null;
		String cid = null;
		String subject = null;
		String issuer = null;
		String issuer_code = null;
		SignVerifier signVerifier = null; // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		X509Certificate cert = null;

		// ======================================================
		// 1. 인증서 데이터 추출
		// ======================================================
		if (signData == null || signData.equalsIgnoreCase(""))
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");

		try {
			if(PRCSharedUtils.isSB()) {
				signData = java.net.URLDecoder.decode(signData);
				vidRandom = java.net.URLDecoder.decode(vidRandom);
			}
			signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE); // 서명데이터만검증
			cert = signVerifierResult.getSignerCertificate(); // 서명인증서 획득

			log.debug("<<<<<<<<<<<<<<<<< 서명인증서 cert :: {}", cert);
		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		// ======================================================
		// 2. 인증서의 사용자 정보를 얻어 옴.
		// ======================================================
		if (cert == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// 획득한 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(cert);
		log.debug(certHelper.toString());
		subject = certHelper.getSubjectDN(); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요:INITECH호환(새마을금고)
		issuer = certHelper.getIssuerDN(); // 인증서 발급기관 DN정보(인증서,대문자,cn부터): X509기본설정
		issuer_code = certHelper.getIssuerCode(); // 발급자코드

		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급주체 DN정보 subject :: {}", subject);
		log.debug("<<<<<<<<<<<<<<<<< 인증서 발급기관 DN정보 issuer :: {}", issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급자코드 issuer_code :: {}", issuer_code);

		if (issuer_code == null)
			throw new PRCServiceException("INI922", "이 페이지는 인증서가 필요합니다.");

		// ======================================================
		// 3. VID체크 (본인확인체크).
		// ======================================================
		String SSN = StringUtils.isEmpty(SessionManager.getLoginValue("PerBusNo", String.class))
				? SessionManager.getGlobalValue("PerBusNo", String.class)
				: SessionManager.getLoginValue("PerBusNo", String.class);
		boolean checkVidResult = false;

		if (StringUtils.isEmpty(SSN))
			throw new PRCServiceException("9999", "로그인이나 본인인증 후 사용 가능합니다");

		try {
			log.debug("<<<<<<<<<<<<<<<<< vidRandom : {}", vidRandom);

			checkVidResult = signVerifierResult.verifyVid(SSN, vidRandom);
			log.debug("<<<<<<<<<<<<<<<<< 본인확인체크 결과값 checkVidResult : {}", checkVidResult);
			log.debug("<<<<<<<<<<<<<<<<< checkVidResult : {}", checkVidResult);
		} catch (Exception e) {
			log.debug("<<<<<<<<<<<<<<<<< 본인확인에 실패했습니다.!! >> VID Result	:[FALSE]");
			log.error("CertVerifyImplService.selfVerify checkVID Exception {} - {}", e.getMessage(), e);
			throw new PRCServiceException("INI101", "본인확인에 실패했습니다.!!");
		}

		String certSerial = certHelper.getSerial(); // 인증서시리얼(10진수) , 12자리로 변환
		java.text.DateFormat myDate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm ss");
		java.util.Date NotBefore = cert.getNotBefore(); // 인증서 발급일
		java.util.Date NotAfter = cert.getNotAfter(); // 인증서 만기일
		java.util.Date currentTime = new java.util.Date();// 현재 시간
		int dateformat = (int) ((NotAfter.getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000));

		log.debug("<<<<<<<<<<<<<<<<< 공인인증서 ID : " + userID);
		log.debug("<<<<<<<<<<<<<<<<< 주민등록번호 : " + cid);
		log.debug("<<<<<<<<<<<<<<<<< 발급자 : " + issuer);
		log.debug("<<<<<<<<<<<<<<<<< 발급대상 : " + subject);
		log.debug("<<<<<<<<<<<<<<<<< 발급일 : " + myDate.format(NotBefore));
		log.debug("<<<<<<<<<<<<<<<<< 만료일 : " + myDate.format(NotAfter));
		log.debug("<<<<<<<<<<<<<<<<< Serial : " + certSerial);
		log.debug("<<<<<<<<<<<<<<<<< 현재시간 : " + myDate.format(currentTime));
		log.debug("<<<<<<<<<<<<<<<<< 고객님의 인증서는 {}일 후에 만료가 됩니다.", dateformat);

		/*
		 * OPPRA/OCSPGD check
		 * - 금결원 당행인증서 : LDAP_INFO 조회
		 * - 금결원 타행인증서 : OPPRA 통신 후 status 확인
		 * - 타기관인증서 : OCSPGD 통신 후 status 확인
		 */
		boolean isCertValidity = certHelper.chkCertStatus();
		log.debug("<<<<<<<<<<<<<<<<< OPPRA/OCSPGD check {}", isCertValidity);

		// ======================================================
		// 9. 응답데이터 생성
		// ======================================================
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		resultMap.put("userCert", CertUtils.getB64FromCertification(cert));
		log.debug("<<<<<<<<<<<<<<<<< 인증서 검증 완료하였습니다.");

		return resultMap;
	}

	/**
	 * 제출한 공동인증서 검증
	 * @param signData
	 * @return
	 */
	@ComponentOperation(name = "제출한 공동인증서 검증", description = "제출한 공동인증서 검증", author = "오은진")
	public HashMap<String, Object> submitCertVerify(String signData) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}

		SignVerifier signVerifier = null; // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		X509Certificate userCert = null;

		try {
			if(PRCSharedUtils.isSB()) {
				signData = java.net.URLDecoder.decode(signData);
			}
			signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE); // 서명데이터만검증
			userCert = signVerifierResult.getSignerCertificate(); // 서명인증서 획득

			resultMap.put("signVerifierResult", signVerifierResult);
			resultMap.put("userCert", CertUtils.getB64FromCertification(userCert));
		} catch (Exception e) {
			throw new PRCServiceException("PRCCRT9001", "제출하신 공인인증서의 검증에 실패 하였습니다. 인증서를 다시한번 확인해 주시기 바랍니다.");
		}

		return resultMap;
	}

	/**
	 * 제출한 공동인증서 본인 검증
	 * @param verifyMap
	 * @param regNo
	 * @param saupjaNo
	 * @param vidRandom
	 * @throws Exception
	 */
	@ComponentOperation(name = "제출한 공동인증서 본인 검증", description = "제출한 공동인증서 본인 검증", author = "오은진")
	public void vidVerify(Map<String, Object> verifyMap, String regNo, String saupjaNo, String vidRandom) throws Exception {
		SignVerifierResult signVerifierResult = (SignVerifierResult) verifyMap.get("signVerifierResult");
		if(PRCSharedUtils.isSB()) {
			vidRandom = java.net.URLDecoder.decode(vidRandom);
		}
		if (!signVerifierResult.verifyVid(regNo, vidRandom)) {
			if (saupjaNo != null && !"".equals(saupjaNo.trim())) {
				if (!signVerifierResult.verifyVid(saupjaNo, vidRandom)) {
					throw new PRCServiceException("PRCCRT9000", "제출하신 공인인증서는 본인의 인증서가 아니므로 등록하실수 없습니다.");
				}
			} else {
				throw new PRCServiceException("PRCCRT9000", "제출하신 공인인증서는 본인의 인증서가 아니므로 등록하실수 없습니다");
			}
		}
	}
}
