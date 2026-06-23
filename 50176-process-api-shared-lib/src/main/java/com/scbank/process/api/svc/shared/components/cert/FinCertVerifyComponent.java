/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 금융인증서로 서명된 문서 검증 공통 컴포넌트
*  작   성  자 : 이완주
*  작   성  일 : 2025.11.07
*  관련 테이블 :
*  관련 전문   :
* Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
* 변경이력 (버전/변경일시/작성자)
* <pre>
* 최초작성 (1.0/2025.11.07/이완주)
* </pre>
******************************************************************************/
package com.scbank.process.api.svc.shared.components.cert;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.wizvera.WizveraConfig;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.DetachedSignVerifier;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "금융인증서 전자서명인증서 검증", description = "금융인증서 전자서명인증서 검증", author = "이완주")
public class FinCertVerifyComponent {

	private final ISessionContextManager sessionManager;
	
	// delfino.properties
	@Value("${delfino.config.path:}")
	private String configPath;

	/**
	 * 금융인증서 서명데이터 검증
	 *
	 * @param dataType (L : 로그인, S : 전자서명, A : 인증서검증값 추출, C : ID/PW 로그인시 인증서 데이터 검증, NS : NTB 전자서명 처리)
	 * @param signData
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "서명데이터 검증", description = "서명데이터 검증", author = "이완주")
	public HashMap<String, Object> verify(String dataType, String signData) {
		log.debug("(●'◡'●) 금융인증서 전자서명 검증 시작 dataType={}, signData={}", dataType, signData);
		
		if (dataType == null || "".equals(dataType) || signData == null || "".equals(signData)) {
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");
		}
		
		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}
		
		SignVerifier signVerifier = new SignVerifier(delfinoConfig);  //서명검증 객체
		SignVerifierResult signVerifierResult = null;
		X509Certificate userCert = null;

		try {
			if (dataType.matches("^(?:OC|RN|S|BR|LO|MD)$")) { // 타기관인증서 등록 , 인증서갱신 , 전자서명 , 생체인증등록 , 대출거래시 VID 체크한다.
				// ======================================================
				// 1. VID 검사
				// ======================================================
				String perBusNo = sessionManager.isLogin() ? sessionManager.getLoginValue("PerBusNo", String.class) : sessionManager.getGlobalValue("PerBusNo", String.class); // 주민등록번호
				if (perBusNo == null)
					throw new PRCServiceException("INI399", "VID 체크시 주민등록번호가 없습니다.");

				// ======================================================
				// 2-1. 서명값 검사
				// ======================================================

				signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
				if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
					boolean isVerify = signVerifierResult.verifyVid(perBusNo, null);
					if (!isVerify)
						throw new PRCServiceException("FINERR0604", "본인 인증서가 아닙니다.");
				}

			} else if (dataType.matches("^(?:PDF)$")) { // 원문없는 PDF 서명 --> 사용하지않음.
				// ======================================================
				// 2-2. 서명값 검사
				// ======================================================
				WizveraConfig wizveraConfig = WizveraConfig.getDefaultConfiguration();
				DetachedSignVerifier detachedSignVerifier = new DetachedSignVerifier(wizveraConfig);
				signVerifierResult = detachedSignVerifier.verifyPKCS7(signData,SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);

			} else { // 서명데이터만 검증
				// ======================================================
				// 2-3. 서명값 검사
				// ======================================================
				signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
			}

		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		// ======================================================
		// 3. 서명한 인증서 획득
		// ======================================================
		userCert = signVerifierResult.getSignerCertificate(); // 서명인증서 획득
		if (userCert == null)
			throw new PRCServiceException("INI301", "정상적인 인증서가 아닙니다.(Code:001)");

		// 3에서 얻은 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(userCert);
		log.debug(certHelper.toString());
		String issuerDN = certHelper.getIssuerDN(); // 인증서 발급기관 DN정보(인증서,대문자,cn부터): X509기본설정
		String subjectDN = certHelper.getSubjectDN(); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요:INITECH호환(새마을금고)
		String subjectCN = certHelper.getSubjectCN(); // 인증서 발급주체 CN정보(인증서,엔트리,순번)
		String issuerO = certHelper.getIssuerO(); // 인증서 발급기관명(타기관 구분용)(EX :yessign,KICA,SignKorea,NCASign,TradeSign,CrossCert)
		String issuerBank = certHelper.getIssuerBank(); // 인증서 발급은행 코드명(EX : KFB)
		String PolicyName = certHelper.getPolicyName(); // 인증서 정책명(EX : personalF)
		String issuerCode = certHelper.getIssuerCode(); // 발급자코드

		if ("".equals(issuerCode)) throw new PRCServiceException("INI302", "정상적인 인증서가 아닙니다.(Code:002)");

		// ======================================================
		// 4. 서명한 인증서 유효성 검사
		// ======================================================
		Map<String, Object> userInfo = new HashMap<String, Object>();
		String userId = "";
		String cid = "";
		if (!"LO".equals(dataType)) {

			if ("MD".equals(dataType)) {
				/**
				 * OPPRA/OCSPGD check
				 * - 금결원 당행인증서 : LDAP_INFO 조회
				 * - 금결원 타행인증서 : OPPRA 통신 후 status 확인
				 * - 타기관인증서 : OCSPGD 통신 후 status 확인
				 */
				certHelper.chkCertStatus();

			} else {
				// DB에 등록된 사용자 정보를 조회 한다.
				userInfo = certHelper.getUserInfoByScbDB(dataType);
				userId = StringUtils.nvl((String) userInfo.get("USERID"), "");
				cid = StringUtils.nvl((String) userInfo.get("CID"), "");

				// 로그인 정보와 비교
				String _userID = StringUtils.nvl(sessionManager.getLoginValue("UserID", String.class), "");
				String D756_UserID = StringUtils.nvl(sessionManager.getGlobalValue("D756_UserID", String.class), "");
				String sessionUserId = null;
				if (userId.length() > 10 || (_userID != null && _userID.length() > 10) || (D756_UserID != null && D756_UserID.length() > 10)) {
					log.error("userId={}, D756_UserID={}", userId, D756_UserID);
					throw new PRCServiceException("INI303", "WEBBANK 가입 고객은 인터넷 뱅킹거래가 불가합니다. WEBBANK를 통해 거래해 주시기 바랍니다.");
				}
				// D756 기본계좌변경 진행 시 login처리가되지않아 글로벌세션값을 바라보게 수정
				sessionUserId = "".equals(D756_UserID) ? _userID : D756_UserID;

				if (sessionUserId != null && !"".equals(sessionUserId)) {
					if (!sessionUserId.equalsIgnoreCase(userId)) {
						log.error("userId={}, sessionUserId={}", userId, sessionUserId);
						throw new PRCServiceException("INI304", "로그인한 사용자와 제출된 사용자의 아이디가 일치하지 않습니다.");
					}
				}
			}
		}

		// 이거 왜 또 있는거지 오류코드는 다르긴 하네
		if ("S".equalsIgnoreCase(dataType)) {
			String LoginUserID = sessionManager.getLoginValue("UserID", String.class);
			if (LoginUserID != null && !userId.equalsIgnoreCase(LoginUserID)) {
				log.error("userId={}, LoginUserID={}", userId, LoginUserID);
				throw new PRCServiceException("INI100", "본인확인에 실패했습니다. 로그인한 사용자와 제출한 인증서 사용자가 다릅니다.");
			}
		}

		// ======================================================
		// 5. 전자서명 or 대출거래시 서명문서 원본 확인
		// ======================================================
		if (dataType.matches("^(?:S|LO)$")) {
			String orgData = signVerifierResult.getSignedRawData(); // 서명 원본 데이터
			log.debug("서명문서원본={}", orgData);
			orgData = URLDecoder.decode(signVerifierResult.getSignedRawData());
			log.debug("서명문서해독={}", orgData);

			sessionManager.setGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME, orgData);
			sessionManager.setGlobalValue(PRCSharedConstants.SIGN_DATA_NAME, signData); 

			log.debug("(●'◡'●) 금융인증서 서명검증 완료");
		}

		// 이건 왜 또있지..? 오류코드가 다르긴 한데...
		if ("C".equalsIgnoreCase(dataType)) {
			String LoginUserID = sessionManager.getLoginValue("UserID", String.class);
			if (!userId.equalsIgnoreCase(LoginUserID)) {
				log.error("userId={}, LoginUserID={}", userId, LoginUserID);
				throw new PRCServiceException("INI101", "본인확인에 실패했습니다. 로그인한 사용자와 제출한 인증서 사용자가 다릅니다.");
			}
		}

		String certSerial = certHelper.getSerial(); // 인증서시리얼(10진수) , 12자리로 변환
		String certSerialHex = certHelper.getSerialHex(); // 인증서시리얼(16진수) 0A10

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm ss");
		String certBefore = sdf.format(userCert.getNotBefore()); // 인증서유효기간:시작일
		String certAfter = sdf.format(userCert.getNotAfter()); // 인증서유효기간:종료일

		Date currentTime = new Date();// 현재 시간
		int dateformat = (int) ((userCert.getNotAfter().getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000));

		String certPolicy = "";
		String certOID = certHelper.getCertificatePolicyOID();

		if (certOID.equals("1.2.410.200005.1.1.1.10")) {
			certPolicy = "16";
		}

		// 금결원일경우 은행코드 추출
		String bankCode = "";
		if ("yessign".equals(issuerO)) {
			int idx = subjectCN.lastIndexOf(")");
			if (idx != -1)
				bankCode = subjectCN.substring(idx + 2, idx + 5);
		}

		sessionManager.setGlobalValue("session_cid", cid);
		sessionManager.setGlobalValue("cid", cid);
		sessionManager.setGlobalValue("session_UserID", userId);
		sessionManager.setGlobalValue("certID", userId);
		sessionManager.setGlobalValue("uid", userId);
		sessionManager.setGlobalValue("policy", StringUtils.defaultIfEmpty((String)userInfo.get("POLICY"), ""));
		sessionManager.setGlobalValue("serial", StringUtils.defaultIfEmpty((String)userInfo.get("SERIAL"), ""));
		sessionManager.setGlobalValue("subjectDN", subjectDN);
		sessionManager.setGlobalValue("issuerDN", issuerDN);
		sessionManager.setGlobalValue("certType", issuerCode);
		sessionManager.setGlobalValue("serialSession", certSerialHex);
		// FIXME Date 세션적재?? 쓰는곳이 없는거같은데
		// SessionManager.setGlobalObject("issueDate", userCert.getNotBefore());
		// SessionManager.setGlobalObject("expireDate", userCert.getNotAfter());
		sessionManager.setGlobalValue("CertOID", certOID);

		sessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "0000");

		log.debug("(●'◡'●) 인증서발급기관 공인인증서 ID [{}]", sessionManager.getGlobalValue("session_cid", String.class));
		log.debug("(●'◡'●) 인증서발급기관 주민등록번호 [{}]", sessionManager.getGlobalValue("cid", String.class));
		log.debug("(●'◡'●) 인증서발급주체 subjectDN [{}]", sessionManager.getGlobalValue("subjectDN", String.class));
		log.debug("(●'◡'●) 인증서은행 issuerBank [{}]", issuerBank);
		log.debug("(●'◡'●) 인증서은행 PolicyName [{}]", PolicyName);
		log.debug("(●'◡'●) 인증서부가정보 issuerO [{}] subjectCN [{}]", issuerO, subjectCN);
		log.debug("(●'◡'●) 인증서부가정보 bankCode [{}]", bankCode);
		log.debug("(●'◡'●) 인증서일련번호 10진수 [{}] 16진수 [{}]", certSerial, certSerialHex);
		log.debug("(●'◡'●) 인증서유효기간 발급기간 [{}~{}]", certBefore, certAfter);
		log.debug("(●'◡'●) 인증서 정책 식별 ID [{}]", certOID);
		log.debug("(●'◡'●) 현재시간 [{}]", sdf.format(currentTime));
		log.debug("(●'◡'●) 고객님의 인증서는 {}일 후에 만료가 됩니다.", dateformat);

		// ======================================================
		// 6. 응답데이터 생성
		// ======================================================
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		try {
			resultMap.put("userCert", CertUtils.getB64FromCertification(userCert));
		} catch (CertificateEncodingException e) {
			throw new PRCServiceException(e);
		} // 변환해야 세션에 넣기편함
		if ("OC".equals(dataType)) { // 타기관일때 데이터를 추가로 리턴한다.
			resultMap.put("CertPolicy", certPolicy); // 인증서 정책코드 금융인증서 : 16
			resultMap.put("CertBankCode", bankCode); // 인증서 발급 은행 코드 ( TEST : 0099 REAL : 발급은행코드)
			resultMap.put("CertSerial", certSerial); // 인증서 시리얼 넘버 10진수 12자리
			resultMap.put("CertOID", certOID); // 인증서 정책 식별 코드(OID : Object Identifiers)
			resultMap.put("CertCAName", issuerBank); // 인증서 발급 은행 코드명(OU 값 INDEX 1)
			resultMap.put("CertPolicyName", PolicyName); // 인증서 정책 식별명(OU 값 INDEX 0)
			resultMap.put("CertIssuerDN", issuerDN); // 인증서 발급자 DN정보(DN : Distinguished Name)
			resultMap.put("CertSubjectDN", subjectDN); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요: INITECH호환(새마을금고)
			resultMap.put("CertIssueDate", userCert.getNotBefore()); // 인증서 발급날짜
			resultMap.put("CertExpireDate", userCert.getNotAfter()); // 인증서 만료날짜
		}

		log.debug("╰（‵□′）╯ 금융인증서 검증 끝 resultMap={}", resultMap.toString());
		return resultMap;
	}

	/**
	 * 금융인증서 스크래핑용 검증
	 * @param dataType
	 * @param signData
	 * @return
	 * @throws PRCServiceException
	 */
	@ComponentOperation(name = "금융인증서 스크래핑용 검증", description = "금융인증서 스크래핑용 검증", author = "이완주")
	public HashMap<String, Object> finVerify(String dataType, String signData) {
		log.debug("(●'◡'●) 금융인증서 전자서명 검증 시작 dataType={}, signData={}", dataType, signData);
		
		if (dataType == null || "".equals(dataType) || signData == null || "".equals(signData)) {
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");
		}
		if (!"FC".equals(dataType)) {
			throw new PRCServiceException("INI301", "정상적인 인증서가 아닙니다.(Code:001)"); // FC아니면 돌려보내야함 as-is에서도 어차피 NPE라 도달 못함
		}

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}
		SignVerifier signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		X509Certificate userCert = null;

		try {
			String userSSN = sessionManager.getGlobalValue("de_SSN", String.class);
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
			boolean isFCVerify = signVerifierResult.verifyVid(userSSN, null);
			if (!isFCVerify)
				throw new PRCServiceException("FINERR0605", "입력하신 주민등록번호와 인증서 정보가 일치하지 않습니다.");

			String perBusNo = sessionManager.isLogin() ? sessionManager.getLoginValue("PerBusNo", String.class) : sessionManager.getGlobalValue("PerBusNo", String.class); // 주민등록번호
			if (perBusNo == null)
				throw new PRCServiceException("INI399", "VID 체크시 주민등록번호가 없습니다.");
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
			boolean isVerify = signVerifierResult.verifyVid(perBusNo, null);
			if (!isVerify)
				throw new PRCServiceException("FINERR0604", "본인 인증서가 아닙니다.");

		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		userCert = signVerifierResult.getSignerCertificate(); // FC말고 딴거로 들어오면 이거 NPE날텐데...
		if (userCert == null)
			throw new PRCServiceException("INI301", "정상적인 인증서가 아닙니다.(Code:001)");

		// 3에서 얻은 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(userCert);
		String issuerCode = certHelper.getIssuerCode(); // 발급자코드

		if ("".equals(issuerCode))
			throw new PRCServiceException("INI302", "정상적인 인증서가 아닙니다.(Code:002)");

		/**
		 * OPPRA/OCSPGD check
		 * - 금결원 당행인증서 : LDAP_INFO 조회
		 * - 금결원 타행인증서 : OPPRA 통신 후 status 확인
		 * - 타기관인증서 : OCSPGD 통신 후 status 확인
		 */
		certHelper.chkCertStatus();

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		log.debug("╰（‵□′）╯ 금융인증서 검증 끝 resultMap={}", resultMap.toString());

		return resultMap;
	}

	/**
	 * 미등록인증서 유효성검증
	 * @param signData
	 * @return
	 * @throws PRCServiceException
	 */
	@ComponentOperation(name = "미등록인증서 유효성검증", description = "미등록인증서 유효성검증", author = "이완주")
	public HashMap<String, Object> edcfVerify(String signData) {
		log.debug("(●'◡'●) 금융인증서 전자서명 검증 시작 signData={}", signData);
		if (signData == null || "".equals(signData)) {
			throw new PRCServiceException("INI923", "제출된 인증서에서 시리얼 번호, 주체자, 발급자 정보를 얻지 못했습니다.");
		}

		String perBusNo = sessionManager.isLogin() ? sessionManager.getLoginValue("PerBusNo", String.class) : sessionManager.getGlobalValue("PerBusNo", String.class); // 주민등록번호
		if (perBusNo == null) throw new PRCServiceException("INI399", "VID 체크시 주민등록번호가 없습니다.");

		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try{
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException|IllegalStateException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}
		SignVerifier signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
		SignVerifierResult signVerifierResult = null; // 서명검증 결과
		X509Certificate userCert = null;

		try {
			signVerifierResult = signVerifier.verifyPKCS7(signData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
			boolean isVerify = signVerifierResult.verifyVid(perBusNo, null);

			if (!isVerify) {
				throw new PRCServiceException("FINERR0604", "본인 인증서가 아닙니다.");
			}

		} catch (DelfinoServiceException e) {
			if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage());
			} else {
				throw new PRCServiceException("FINERR_" + e.getErrorCode(), e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN));
			}
		}

		userCert = signVerifierResult.getSignerCertificate();
		if (userCert == null) throw new PRCServiceException("INI301", "정상적인 인증서가 아닙니다.(Code:001)");

		// 3에서 얻은 인증서로 도우미 생성
		CertificateHelper certHelper = new CertificateHelper(userCert);

		/**
		 * OPPRA/OCSPGD check
		 * - 금결원 당행인증서 : LDAP_INFO 조회
		 * - 금결원 타행인증서 : OPPRA 통신 후 status 확인
		 * - 타기관인증서 : OCSPGD 통신 후 status 확인
		 */
		certHelper.chkCertStatus();

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("returnCode", "0000");
		resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
		log.debug("╰（‵□′）╯ 금융인증서 검증 끝 resultMap={}", resultMap.toString());

		return resultMap;
	}

}