/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 인증서 공통 유틸 (인증서를 직접넣을거면 CertificateHelper 클래스 사용)
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import com.initech.oppra.IniOPPRA;
import com.initech.oppra.IniOPPRAIllegalFormatException;
import com.initech.oppra.IniOPPRAReadException;
import com.initech.oppra.IniOPPRASystemException;
import com.initech.oppra.util.JohoeiDataParser;
import com.initech.oppra.util.OppraMessageDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;
import com.scbank.process.api.svc.shared.components.cert.dto.OppraCertInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "인증서 유틸", description = "인증서 유틸", author = "이완주")
@RequiredArgsConstructor
public class CertUtils {

	private final LdapInfoDao ldapInfoDao;
	private final ICodeManager commonCode;
	private final ISessionContextManager sessionManager;
	
	/**
	 * 인증센터 2단계부터 세션에서 값을 꺼낼때 검사하는 용도
	 * 이 오류코드를 받으면 1스탭이나 메인으로 돌려보내야함
	 * @param key
	 * @return
	 */
	@ComponentOperation(name = "getCertSession", description = "안전한 세션값 꺼내기", author = "이완주")
	public Map<String, Object> getCertSession(String key) {
		Map<String,Object> certSession = (Map<String, Object>) sessionManager.getGlobalValue(key, Map.class);
		
		if(certSession == null) {
			throw new PRCServiceException("TEST9999", "세션이 유효하지 않습니다");
		}

		return certSession;
	}

	/**
	 * 금결원 인증서 발급 정보 조회
	 * @param userId
	 * @param oppUserid
	 * @param deptPersonName
	 * @return
	 */
	@ComponentOperation(name = "getOppResult", description = "금결원 인증서 발급 정보 조회", author = "이완주")
	public Map<String, Object> getOppResult(String userId, String oppUserid, String deptPersonName) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("OPPUserID", oppUserid);
		result.put("DeptPersonDetailName", deptPersonName);

		// 개인-범용(구 공인인증서)
		result.put("IS_P_FINANCIAL", "NO");
		result.put("P_FINANCIAL_STATUS", "INVALID");
		result.put("P_FINANCIAL_ISSUERORG", "UNKNOWN");
		result.put("P_FINANCIAL_SERIAL", "UNKNOWN");

		// 개인-금융(구 공인인증서)
		result.put("IS_P_FINANCIAL_RESTRICT", "NO");
		result.put("P_FINANCIAL_RESTRICT_STATUS", "INVALID");
		result.put("P_FINANCIAL_RESTRICT_ISSUERORG", "UNKNOWN");
		result.put("P_FINANCIAL_RESTRICT_SERIAL", "UNKNOWN");

		// 개인-금융인증서
		result.put("IS_P_FINANCIAL_FINANCIAL", "NO");
		result.put("P_FINANCIAL_FINANCIAL_STATUS", "INVALID");
		result.put("P_FINANCIAL_FINANCIAL_ISSUERORG", "UNKNOWN");
		result.put("P_FINANCIAL_FINANCIAL_SERIAL", "UNKNOWN");
		result.put("P_FINANCIAL_FINANCIAL_RAFLAG", "UNKNOWN");

		// 기업-금융(구 공인인증서)
		result.put("IS_C_FINANCIAL", "NO");
		result.put("C_FINANCIAL_STATUS", "INVALID");
		result.put("C_FINANCIAL_ISSUERORG", "UNKNOWN");
		result.put("C_FINANCIAL_SERIAL", "UNKNOWN");

		// 기업-범용(구 공인인증서)
		result.put("IS_BUSINESS", "NO");
		result.put("BUSINESS_STATUS", "INVALID");
		result.put("BUSINESS_SERIAL", "UNKNOWN");
		result.put("BUSINESS_ISSUERORG", "UNKNOWN");

		// 기업 세금계산서(구 공인인증서)
		result.put("IS_ESERO_RESTRICT", "NO");
		result.put("ESERO_RESTRICT_STATUS", "INVALID");
		result.put("ESERO_RESTRICT_ISSUERORG", "UNKNOWN");
		result.put("ESERO_RESTRICT_SERIAL", "UNKNOWN");

		// 삭제업무에서 왔는지 검사
		boolean delCert = false;
		if (userId != null && userId.indexOf("__DELETECERT__USER_ID") > -1) {
			delCert = true;
			userId = userId.split("__DELETECERT__USER_ID")[0];
		}

		// LDAP_INFO테이블을 userId로 조회하여 금결원 인증서 목록 조회함
		List<LdapInfoResult> ldapInfoOutputDtoList = ldapInfoDao.selectOppraLdapInfoUserid(userId);

		// LDAP_INFO 조회데이터 가공
		List<Map<String, String>> ldapInfoResultList = new ArrayList<Map<String, String>>();
		for (LdapInfoResult ldapInfo : ldapInfoOutputDtoList) {

			HashMap<String, String> ldapInfoResult = new HashMap<String, String>();

			String policy = ldapInfo.getPolicy();
			if ("34".equals(policy) || "44".equals(policy))
				continue;

			String subjectDN = "";
			String subjectPolicy = "";
			String userClass = "";
			String issuerOrg = "";
			String raFlag = "0";
			String combFlag = "0";
			String serial = "";
			try {
				// 개발서버에서 인증서 읽지 못하는문제
				if (delCert || "20".equals(HexFormat.of().formatHex((byte[]) ldapInfo.getUsercertificate()))) {
					serial = ldapInfo.getSerial();
					subjectDN = ldapInfo.getDn();
				} else {
					java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
					X509Certificate x509certificate = (X509Certificate) cf.generateCertificate(new java.io.ByteArrayInputStream(ldapInfo.getUsercertificate()));
					subjectDN = x509certificate.getSubjectX500Principal().getName().replaceAll(" ", "");
					serial = x509certificate.getSerialNumber().toString(10);
				}
			} catch (Exception exception) {
				log.error(exception.getMessage());
				// 인증서바이트를 읽지 못했을때 처리
				serial = ldapInfo.getSerial();
				subjectDN = ldapInfo.getDn();
			}

			// 인증서 종별
			if (subjectDN.indexOf("ou=corporation4EC") != -1 || subjectDN.indexOf("OU=corporation4EC") != -1) {
				subjectPolicy = "BUSINESS";
				userClass = "CORPORATION";

			} else {
				subjectPolicy = "FINANCIAL";
				if (subjectDN.indexOf("ou=corporation") != -1 || subjectDN.indexOf("OU=corporation") != -1) {
					userClass = "CORPORATION";
				} else if (subjectDN.indexOf("ou=personal4IB") != -1 || subjectDN.indexOf("OU=personal4IB") != -1) {
					userClass = "PERSONAL_RESTRICT";
				} else if (subjectDN.indexOf("ou=xUse4Esero") != -1 || subjectDN.indexOf("OU=xUse4Esero") != -1) {
					userClass = "ESERO_RESTRICT";
				} else if (subjectDN.indexOf("ou=personalF") != -1 || subjectDN.indexOf("OU=personalF") != -1) {
					userClass = "PERSONAL_FINANCIAL";
				} else {
					userClass = "PERSONAL";
				}
			}

			// 발급기관
			StringTokenizer stringtokenizer = new StringTokenizer(subjectDN, "OU=");
			while (stringtokenizer.hasMoreTokens()) {
				String temp = stringtokenizer.nextToken();
				for (StringTokenizer st = new StringTokenizer(temp, ","); st.hasMoreTokens();) {
					String substr = st.nextToken();
					if ("KFB".equals(substr)) {
						issuerOrg = "KFB";
						raFlag = "1";
					}
				}
			}

			// 조합번호용
			if ("84".equals(policy) && "PERSONAL_RESTRICT".equals(userClass)) {
				combFlag = "1";
			}

			ldapInfoResult.put("userId", ldapInfo.getUserid());
			ldapInfoResult.put("serial", serial);
			ldapInfoResult.put("subjectPolicy", subjectPolicy);
			ldapInfoResult.put("userClass", userClass);
			ldapInfoResult.put("issuerOrg", issuerOrg);
			ldapInfoResult.put("raFlag", raFlag);
			ldapInfoResult.put("combFlag", combFlag);
			ldapInfoResultList.add(ldapInfoResult);
		} // LDAP_INFO 반복문 끝

		// LDAP_INFO 조회결과로 RA조회
		List<Map<String, String>> oppraResultList = new ArrayList<Map<String, String>>();
		for (Map<String, String> ldapInfoResult : ldapInfoResultList) {

			Map<String, String> oppraResult = new HashMap<String, String>();

			String subjectPolicy = ldapInfoResult.get("subjectPolicy");
			String userClass = ldapInfoResult.get("userClass");
			String requestRAW = "";

			// 전문 조립
			if ("BUSINESS".equals(subjectPolicy)) {
				if ("CORPORATION".equals(userClass)) {
					requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$05$1$" + ldapInfoResult.get("serial") + "$000$001$";
				} else {
					requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$01$1$" + ldapInfoResult.get("serial") + "$000$001$";
				}
			} else if (userClass.equals("CORPORATION")) {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$02$1$" + ldapInfoResult.get("serial") + "$000$001$";
			} else if (userClass.equals("PERSONAL")) {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$01$1$" + ldapInfoResult.get("serial") + "$000$001$";
			} else if (userClass.equals("ESERO_RESTRICT")) {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$68$1$" + ldapInfoResult.get("serial") + "$000$001$";
			} else if (userClass.equals("PERSONAL_FINANCIAL")) {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$16$1$" + ldapInfoResult.get("serial") + "$000$001$";
			} else if ("1".equals(ldapInfoResult.get("combFlag"))) {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$84$1$" + ldapInfoResult.get("serial") + "$000$001$";
			} else {
				requestRAW = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$04$1$" + ldapInfoResult.get("serial") + "$000$001$";
			}

			IniOPPRA oppra = null;
			try {
				// 정보조회
				oppra = this.getOppra();
				oppra.requestRAW(50, requestRAW);
				String resCommonPart = oppra.getResCommonPart();
				String resDataPart = oppra.getResDataPart();
				JohoeiDataParser johoeidataparser = new JohoeiDataParser(resCommonPart, resDataPart);
				String[] records = johoeidataparser.getRecords();
				johoeidataparser.setRecord(records[0]);
				oppraResult.put("resCode", johoeidataparser.getResCode());
				oppraResult.put("resMsg", johoeidataparser.getResMsg());
				oppraResult.put("regOrgCode", johoeidataparser.getRegOrgCode());
				oppraResult.put("serial", johoeidataparser.getCertSerial());
				oppraResult.put("status", johoeidataparser.getCertStatus());
				oppraResultList.add(oppraResult);
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				this.closeOppra(oppra);
			}
		} // OPPRA 반복문 끝

		// 반환데이터 생성 실패
		if (oppraResultList.size() != ldapInfoResultList.size() || oppraResultList.size() == 0) {
			// 초기값으로 리턴함
			return result;
		}

		// 반환데이터 생성 시작
		int maxSize = oppraResultList.size();
		for (int i = 0; i < maxSize; i++) {
			Map<String, String> ldapInfoResult = ldapInfoResultList.get(i);
			Map<String, String> oppraResult = oppraResultList.get(i);

			log.debug("userId={} [{} of {}]", userId, i+1, maxSize);
			log.debug("subjectPolicy = {}", ldapInfoResult.get("subjectPolicy"));
			log.debug("userClass = {}", ldapInfoResult.get("userClass"));
			log.debug("raFlag = {}", ldapInfoResult.get("raFlag"));
			log.debug("status = {}", oppraResult.get("status"));
			
			if ("BUSINESS".equals(ldapInfoResult.get("subjectPolicy"))) {
				result.put("IS_BUSINESS", "YES");
				if ("10".equals(oppraResult.get("status"))) {
					result.put("BUSINESS_STATUS", "VALID");
					result.put("BUSINESS_ISSUERORG", ldapInfoResult.get("issuerOrg"));
					result.put("BUSINESS_SERIAL", ldapInfoResult.get("serial"));
				}
				if ("40".equals(oppraResult.get("status"))) {
					result.put("BUSINESS_STATUS", "SUSPEND");
					result.put("BUSINESS_ISSUERORG", ldapInfoResult.get("issuerOrg"));
					result.put("BUSINESS_SERIAL", ldapInfoResult.get("serial"));
				}
			}

			if ("FINANCIAL".equals(ldapInfoResult.get("subjectPolicy"))) {
				if ("CORPORATION".equals(ldapInfoResult.get("userClass"))) {
					result.put("IS_C_FINANCIAL", "YES");
					if ("10".equals(oppraResult.get("status"))) {
						result.put("C_FINANCIAL_STATUS", "VALID");
						result.put("C_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("C_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
					}
					if ("40".equals(oppraResult.get("status"))) {
						result.put("C_FINANCIAL_STATUS", "SUSPEND");
						result.put("C_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("C_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
					}

				} else if ("PERSONAL".equals(ldapInfoResult.get("userClass"))) {
					result.put("IS_P_FINANCIAL", "YES");
					if ("10".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_STATUS", "VALID");
						result.put("P_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
					}
					if ("40".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_STATUS", "SUSPEND");
						result.put("P_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
					}

				} else if ("ESERO_RESTRICT".equals(ldapInfoResult.get("userClass"))) {
					result.put("IS_ESERO_RESTRICT", "YES");
					if ("10".equals(oppraResult.get("status"))) {
						result.put("ESERO_RESTRICT_STATUS", "VALID");
						result.put("ESERO_RESTRICT_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("ESERO_RESTRICT_SERIAL", ldapInfoResult.get("serial"));
					}
					if ("40".equals(oppraResult.get("status"))) {
						result.put("ESERO_RESTRICT_STATUS", "SUSPEND");
						result.put("ESERO_RESTRICT_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("ESERO_RESTRICT_SERIAL", ldapInfoResult.get("serial"));
					}

				} else if ("PERSONAL_FINANCIAL".equals(ldapInfoResult.get("userClass"))) {
					result.put("IS_P_FINANCIAL_FINANCIAL", "YES");
					if ("10".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_FINANCIAL_STATUS", "VALID");
						result.put("P_FINANCIAL_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
						result.put("P_FINANCIAL_FINANCIAL_RAFLAG", ldapInfoResult.get("raFlag"));
					}
					if ("40".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_FINANCIAL_STATUS", "SUSPEND");
						result.put("P_FINANCIAL_FINANCIAL_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_FINANCIAL_SERIAL", ldapInfoResult.get("serial"));
					}

				} else {
					result.put("IS_P_FINANCIAL_RESTRICT", "YES");
					if ("10".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_RESTRICT_STATUS", "VALID");
						result.put("P_FINANCIAL_RESTRICT_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_RESTRICT_SERIAL", ldapInfoResult.get("serial"));
					}
					if ("40".equals(oppraResult.get("status"))) {
						result.put("P_FINANCIAL_RESTRICT_STATUS", "SUSPEND");
						result.put("P_FINANCIAL_RESTRICT_ISSUERORG", ldapInfoResult.get("issuerOrg"));
						result.put("P_FINANCIAL_RESTRICT_SERIAL", ldapInfoResult.get("serial"));
					}
				}
			}
		} // 정제 반복문 끝

		log.debug("★개인범용인증서 보유 IS_P_FINANCIAL : [{}]", result.get("IS_P_FINANCIAL"));
		log.debug("★개인범용인증서 상태 P_FINANCIAL_STATUS  : [{}]", result.get("P_FINANCIAL_STATUS"));
		log.debug("★개인범용인증서발급기관 P_FINANCIAL_ISSUERORG  : [{}]", result.get("P_FINANCIAL_ISSUERORG"));
		log.debug("★개인범용인증서 시리얼 P_FINANCIAL_SERIAL  : [{}]", result.get("P_FINANCIAL_SERIAL"));
		log.debug("○개인공동인증서 보유 IS_P_FINANCIAL_RESTRICT  : [{}]", result.get("IS_P_FINANCIAL_RESTRICT"));
		log.debug("○개인공동인증서 상태 P_FINANCIAL_RESTRICT_STATUS  : [{}]", result.get("P_FINANCIAL_RESTRICT_STATUS"));
		log.debug("○개인공동인증서발급기관 P_FINANCIAL_RESTRICT_ISSUERORG  : [{}]", result.get("P_FINANCIAL_RESTRICT_ISSUERORG"));
		log.debug("○개인공동인증서 시리얼 P_FINANCIAL_RESTRICT_SERIAL  : [{}]", result.get("P_FINANCIAL_RESTRICT_SERIAL"));
		log.debug("●개인금융인증서 보유 IS_P_FINANCIAL_FINANCIAL  : [{}]", result.get("IS_P_FINANCIAL_FINANCIAL"));
		log.debug("●개인금융인증서 상태 P_FINANCIAL_FINANCIAL_STATUS  : [{}]", result.get("P_FINANCIAL_FINANCIAL_STATUS"));
		log.debug("●개인금융인증서발급기관 P_FINANCIAL_FINANCIAL_ISSUERORG  : [{}]", result.get("P_FINANCIAL_FINANCIAL_ISSUERORG"));
		log.debug("●개인금융인증서 시리얼 P_FINANCIAL_FINANCIAL_SERIAL  : [{}]", result.get("P_FINANCIAL_FINANCIAL_SERIAL"));
		log.debug("●개인금융인증서당타행 P_FINANCIAL_FINANCIAL_RAFLAG  : [{}]", result.get("P_FINANCIAL_FINANCIAL_RAFLAG"));
		log.debug("□기업공동인증서 보유 IS_C_FINANCIAL  : [{}]", result.get("IS_C_FINANCIAL"));
		log.debug("□기업공동인증서 상태 C_FINANCIAL_STATUS  : [{}]", result.get("C_FINANCIAL_STATUS"));
		log.debug("□기업공동인증서발급기관 C_FINANCIAL_ISSUERORG  : [{}]", result.get("C_FINANCIAL_ISSUERORG"));
		log.debug("□기업공동인증서시리얼 C_FINANCIAL_SERIAL  : [{}]", result.get("C_FINANCIAL_SERIAL"));
		log.debug("§기업범용인증서 보유 IS_BUSINESS  : [{}]", result.get("IS_BUSINESS"));
		log.debug("§기업범용인증서 상태 BUSINESS_STATUS  : [{}]", result.get("BUSINESS_STATUS"));
		log.debug("§기업범용인증서발급기관 BUSINESS_ISSUERORG  : [{}]", result.get("BUSINESS_ISSUERORG"));
		log.debug("§기업범용인증서 시리얼 BUSINESS_SERIAL  : [{}]", result.get("BUSINESS_SERIAL"));
		log.debug("♥세금계산서인증서 보유 IS_ESERO_RESTRICT  : [{}]", result.get("IS_ESERO_RESTRICT"));
		log.debug("♥세금계산서인증서상태 ESERO_RESTRICT_STATUS  : [{}]", result.get("ESERO_RESTRICT_STATUS"));
		log.debug("♥세금계산서인증서발급기관 ESERO_RESTRICT_ISSUERORG  : [{}]", result.get("ESERO_RESTRICT_ISSUERORG"));
		log.debug("♥세금계산서인증서 시리얼 ESERO_RESTRICT_SERIAL  : [{}]", result.get("ESERO_RESTRICT_SERIAL"));

		return result;
	}

	@ComponentOperation(name = "getOrgCode", description = "OrgCode 조회", author = "이완주")
	public String getOrgCode(String subjectDn) {
		String s1 = "KFB";
		String s2 = CertConfig.RegCodeValue;
		String s3 = "";
		StringTokenizer stringtokenizer = new StringTokenizer(subjectDn, "OU=");
		while (stringtokenizer.hasMoreTokens()) {
			String s4 = stringtokenizer.nextToken();
			StringTokenizer stringtokenizer1 = new StringTokenizer(s4, ",");
			if (!stringtokenizer1.hasMoreTokens())
				continue;
			String s5 = stringtokenizer1.nextToken();
			if (s5 == null) {
				s3 = "ORGCODE_3000";
				continue;
			}
			if (s5.equals("")) {
				s3 = "ORGCODE_3000";
				continue;
			}
			if (s5.equals(s1)) {
				s3 = s2;
				break;
			}
			s3 = s5;
		}
		return s3;
	}

	@ComponentOperation(name = "getRegOppra", description = "IniOPPRA 반환", author = "이완주")
	public IniOPPRA getRegOppra() {
		log.debug("oppra 연결시도.. {}:{}", CertConfig.OPPRAIP, CertConfig.OPPRAPORT);
		
		IniOPPRA oppra = new IniOPPRA(CertConfig.OPPRAIP, CertConfig.OPPRAPORT);

		try {
			oppra.setCharEncoding("EUC-KR");
			oppra.initialize();
			// oppra.setVersion("4"); getOppra()랑 차이가 있네..? 뭔가 다른가..?
		} catch (IniOPPRASystemException e) {
			log.error(e.getMessage());
			throw new PRCServiceException(e);
		}

		return oppra;
	}

	@ComponentOperation(name = "getOppra", description = "IniOPPRA 반환", author = "이완주")
	public IniOPPRA getOppra() {
		log.debug("oppra 연결시도.. {}:{}", CertConfig.OPPRAIP, CertConfig.OPPRAPORT);

		IniOPPRA oppra = new IniOPPRA(CertConfig.OPPRAIP, CertConfig.OPPRAPORT);

		try {
			oppra.setCharEncoding("EUC-KR");
			oppra.initialize();
			oppra.setVersion("4");
		} catch (IniOPPRASystemException e) {
			throw new PRCServiceException(e);
		}

		return oppra;
	}

	@ComponentOperation(name = "closeOppra", description = "OPPRA 닫기", author = "이완주")
	public void closeOppra(IniOPPRA oppra) {
		try {
			if (oppra != null) {
				oppra.close();
				oppra = null;
			}
		} catch (IniOPPRASystemException e) {
			throw new PRCServiceException(e);
		}
	}

	@ComponentOperation(name = "getOPPRASerial", description = "사용자 serialNumber 조회", author = "이완주")
	public String getOPPRASerial(X509Certificate userCert) {
		String serialNumber = userCert.getSerialNumber().toString(10);
		return this.getOPPRASerial(serialNumber);
	}

	@ComponentOperation(name = "getOPPRASerial", description = "사용자 serialNumber 자릿수에 맞게 반환", author = "이완주")
	public String getOPPRASerial(String serialNumber) {
		return StringUtils.leftPad(serialNumber, 12, '0');
	}

//	/**
//	 * 발급자 코드를 얻어오는 함수 01 : 금결원(yessign) 02 : 무역정보통신(TradeSign) 03 : 전자인증(CrossCert)
//	 * 04 : 코스콤 증권전산원(SignKorea) 05 : 한국정보인증(SignGate) 06 : 한국전산원(NCA)
//	 *
//	 * @param issuer
//	 * @return
//	 */
//	@ComponentOperation(name = "makeIssuerCode", description = "발급자 코드 생성", author = "이완주")
//	public String makeIssuerCode(String issuer) {
//		String result = "";
//		if (issuer.indexOf("yessign") >= 0) {
//			result = "01";
//		} else if (issuer.indexOf("TradeSign") >= 0) {
//			result = "02";
//		} else if (issuer.indexOf("CrossCert") >= 0) {
//			result = "03";
//		} else if (issuer.indexOf("SignKorea") >= 0) {
//			result = "04";
//		} else if (issuer.indexOf("SignGate") >= 0) {
//			result = "05";
//		} else if (issuer.indexOf("NCA") >= 0) {
//			result = "06";
//		}
//		return result;
//	}

	/**
	 * 주민번호로 CA조회
	 *
	 * @param certPolicy
	 * @param regNum
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "getSerialNumByRegnum", description = "주민번호로 CA조회", author = "이완주")
	public String getSerialNumByRegnum(String certPolicy, String regNum) {

		String serialNum = "";
		IniOPPRA oppra = null;
		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append(certPolicy).append("$3$").append(regNum).append("$000$001$");

			oppra = this.getOppra();
			oppra.requestRAW(50, sendMsg.toString());

			String commonPart = "";
			String dataPart = "";
			dataPart = oppra.getResDataPart();
			commonPart = oppra.getResCommonPart();
			dataPart = oppra.getResDataPart();
			OppraMessageDataParser odp = new OppraMessageDataParser(50);
			odp.setLoopData(commonPart, dataPart);

			for (int i = 0; i < odp.getLoopSize(); i++) {
				odp.setRecord(i);
				if ("000".equals(odp.getResCode())) {
					serialNum = odp.getCodeData("CERTSERIAL");
				}
			}
		} catch (IniOPPRAReadException | IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException(e);
		} finally {
			this.closeOppra(oppra);
		}
		return serialNum;
	}

	/**
	 * 아이디로 CA조회
	 *
	 * @param certPolicy
	 * @param regNum
	 * @return
	 * @throws Exception
	 */
	@ComponentOperation(name = "getSerialNumByUserID", description = "아이디로 CA조회", author = "오은진")
	public String getSerialNumByUserID(String certPolicy, String UserID) {

		String serialNum = "";
		IniOPPRA oppra = null;
		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append(certPolicy).append("$2$").append(CertConfig.RegCodeValue).append(UserID).append("$000$001$");

			oppra = this.getOppra();
			oppra.requestRAW(50, sendMsg.toString());

			JohoeiDataParser jdp = new JohoeiDataParser(oppra.getResCommonPart(), oppra.getResDataPart());

			String[] records = jdp.getRecords();
			jdp.setRecord(records[0]);

			serialNum = jdp.getCertSerial();
		} catch (IniOPPRAReadException | IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException(e);
		} finally {
			this.closeOppra(oppra);
		}
		return serialNum;
	}


	@ComponentOperation(name = "formatString", description = "문자열 format", author = "이완주")
	static public String formatString(String source) {
		String str = "000000000000" + source;
		return str.substring(str.length() - 12);
	}

	/**
	 * 호스트로부터 내려온 전자 스페이스를 처리하는 메소드
	 *
	 * @param deptPersonName
	 * @param maxLength
	 * @return
	 */
	@ComponentOperation(name = "byteStringConvert", description = "호스트로부터 내려온 전자 스페이스를 처리하는 메소드", author = "이완주")
	public String byteStringConvert(String deptPersonName, int maxLength) {

		if (deptPersonName == null) {
			return "";
		} else if (deptPersonName.equals("")) {
			return deptPersonName;
		}

		StringBuffer strBuf = new StringBuffer();
		char c = 0;
		for (int i = 0; i < deptPersonName.length(); i++) {
			c = deptPersonName.charAt(i);

			// 영문이거나 특수 문자 일경우.
			if (c >= 0xff01 && c <= 0xff5e) {
				c -= 0xfee0;
			} else if (c == 0x3000) {
				c = 0x20;
			}

			// 문자열 버퍼에 변환된 문자를 쌓는다
			strBuf.append(c);
		}

		deptPersonName = strBuf.toString(); // 특수문자 제거 처리

		byte[] bufCustName = null;

		try {

			bufCustName = (deptPersonName.trim()).getBytes("EUC-KR");
		} catch (Exception e) {
			return deptPersonName;
		}

		int endPos = bufCustName.length;
		int startPos = 0;
		int i;

		for (i = 0; i < endPos; i++) {
			if (bufCustName[i] != -95)
				break;
		}

		if (i == endPos) {
			deptPersonName = " ";
		} else {

			if (i % 2 == 1) {
				startPos = i - 1;
			} else {
				startPos = i;
			}
		}

		if (endPos > 0) {
			for (i = endPos - 1; i > startPos; i--) {
				if (bufCustName[i] != -95)
					break;
				else
					endPos--;
			}

			if (endPos % 2 == 1) {
				endPos += 1;
			}
			/*
			 * "청담교회 예수장로회"와 같이 전각 사이에 반각 스페이스가 있을 경우 Index Out Exception를 방지하기 위해서 아래의 로직을
			 * 삽입함. 전체 싸이즈가 홀수라는 것은 중간에 반각 데이터가 존재하고 그 반각은 스페이스임을 말함 왜냐하면 스페이스일 경우에는 반각으로
			 * 변경하는 로직 프로웍스에 존재함. 2009년 12월 29일 JIP
			 */
			if (bufCustName.length % 2 == 1) {
				endPos--;
			}
		}

		if (endPos > startPos) {
			try {
				deptPersonName = new String(bufCustName, startPos, endPos, "EUC-KR");
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			deptPersonName = " ";
		}

		/* 앞뒤 전자 스페이스 제거 끝 */

		/* 전자 문자 바꾸기 시작 */
		StringBuffer _ret = new StringBuffer();
		String result = "";
		int _i = 0;
		if (deptPersonName != null) {
			int ch;
			for (_i = 0; _i < deptPersonName.length(); _i++) {
				ch = (int) deptPersonName.charAt(_i);
				// 이미 위에서 다 바뀌었기때문에 여기 오지 못함
				_ret.append((char)ch);
			}
			result = _ret.toString().trim();
		}

		// 결제원에서 DN값 생성시 길이 초과나는 오류 방지하기 위해 이름을 9자리에서 자른다.
		if (result.length() >= (maxLength + 2)) {
			result = result.substring(0, maxLength);
		}

		return result;
	}

	/**
	 * CA에 등록된 금융결제원 인증서 조회
	 * ※ 타행발급된거만 찾는것임 ※
	 * @param UserID
	 * @param RegNo
	 * @param PersonOrCompanyCode
	 * @param input
	 * @return
	 * @throws PRCServiceException
	 */
	@ComponentOperation(name = "searchByUserID", description = "CA에 등록된 금융결제원 인증서 조회", author = "이완주")
	public List<OppraCertInfo> searchByUserID(String UserID, String RegNo, Map<String, String> input) {
		if(input == null) input = new HashMap<String, String>();
		String certInquiryType = StringUtils.defaultIfEmpty(input.get("CertInquiryType"), "C"); // C : 기존 공인인증서 F : 금융인증서
		String workType = StringUtils.defaultIfEmpty(input.get("WorkType"), "OC"); // OC : 타기관인증서해지 RV : 인증서 폐기
		String YOCFJMGB = StringUtils.defaultIfEmpty(input.get("YOCFJMGB"), "1"); // 1 : 주민번호 3 : 여권조합번호

		log.debug("YOCFJMGB : " + YOCFJMGB);
		log.debug("CertInquiryType : " + certInquiryType);
		log.debug("WorkType : " + workType);

		List<Map<String, String>> certRASearchList = new ArrayList<Map<String, String>>();

		// 개인 금융 인증서
		String RA_USER_SEARCH_PERSONAL_FINCERT = "1|6|16";
		List<String> RA_USER_SEARCH_PERSONAL_FINCERT_ARRAY = Arrays.asList(RA_USER_SEARCH_PERSONAL_FINCERT.split("[|]"));

		// 개인 범용 인증서
		String RA_USER_SEARCH_PERSONAL_UNIVERSAL = "1|2|01";
		List<String> RA_USER_SEARCH_PERSONAL_UNIVERSAL_ARRAY = Arrays.asList(RA_USER_SEARCH_PERSONAL_UNIVERSAL.split("[|]"));

		// 개인 공동 인증서
		String RA_USER_SEARCH_PERSONAL_COMMON = "1|1|04";
		List<String> RA_USER_SEARCH_PERSONAL_COMMON_ARRAY = Arrays.asList(RA_USER_SEARCH_PERSONAL_COMMON.split("[|]"));

		// 조합번호용 인증서
		String RA_USER_SEARCH_PERSONAL_COMMON_C = "1|1|84";
		List<String> RA_USER_SEARCH_PERSONAL_COMMON_C_ARRAY = Arrays.asList(RA_USER_SEARCH_PERSONAL_COMMON_C.split("[|]"));

		List<List<String>> certInquiryType1 = new CopyOnWriteArrayList<>(); // 금융인증서
		certInquiryType1.add(RA_USER_SEARCH_PERSONAL_FINCERT_ARRAY);

		List<List<String>> certInquiryType2 = new CopyOnWriteArrayList<>(); // 공동인증서 , 범용인증서
		certInquiryType2.add(RA_USER_SEARCH_PERSONAL_UNIVERSAL_ARRAY); // 범용인증서
		certInquiryType2.add(RA_USER_SEARCH_PERSONAL_COMMON_ARRAY); // 공동인증서
		certInquiryType2.add(RA_USER_SEARCH_PERSONAL_COMMON_C_ARRAY); // 공동인증서

		if ("F".equals(certInquiryType)) {
			Map<String, String> subMap = new HashMap<>();
			List<String> subArray = certInquiryType1.get(0);
			subMap.put("CustGubun", subArray.get(0));
			subMap.put("RAGubun", subArray.get(1));
			subMap.put("CertPolicy", subArray.get(2)); // 개인금융용인증서
			certRASearchList.add(subMap);
		} else {
			for (int i = 0; i < certInquiryType2.size(); i++) { // 개인 범용인증서 와 공동인증서를 데이터 적재
				Map<String, String> subMap = new HashMap<>();
				List<String> subArray = certInquiryType2.get(i);
				subMap.put("CustGubun", subArray.get(0));
				subMap.put("RAGubun", subArray.get(1));
				subMap.put("CertPolicy", subArray.get(2));
				certRASearchList.add(subMap);
			}
		}

		List<OppraCertInfo> ldapList = new ArrayList<OppraCertInfo>();
		IniOPPRA oppra = null;

		try {
			for (int i = 0; i < certRASearchList.size(); i++) {
				String custGubun = certRASearchList.get(i).get("CustGubun");
				String raGubun = certRASearchList.get(i).get("RAGubun");
				String certPolicy = certRASearchList.get(i).get("CertPolicy");

				oppra = null;
				OppraMessageDataParser odp = null;
				oppra = this.getOppra();

				String msg = CertConfig.RegCodeValue + "$" + CertConfig.ADID + "$" + certPolicy + "$3$" + RegNo + "$000$020$";
				log.debug("##cert certlist :msg=[" + msg + "]");

				oppra.requestRAW(50, msg);

				String commonPart = "";
				String dataPart = "";
				dataPart = oppra.getResDataPart();
				commonPart = oppra.getResCommonPart();
				dataPart = oppra.getResDataPart();
				odp = new OppraMessageDataParser(50);
				odp.setLoopData(commonPart, dataPart);

				for (int j = 0; j < odp.getLoopSize(); j++) {
					odp.setRecord(j);
					if ("000".equals(odp.getResCode()) && !"023".equals(odp.getCodeData("USERID").substring(1, 4))) {
						OppraCertInfo info = new OppraCertInfo();
						String CAGubun = "2";
						info.setTranType("2"); // TranType(1:HOST, 2:LDAP, 3:INFO)
						info.setCertserial(odp.getCodeData("CERTSERIAL"));
						info.setIssueBank(odp.getCodeData("USERID").substring(1, 4));
						info.setJuminSaupjaNo(odp.getCodeData("REGNO"));
						info.setOid("");
						info.setCustGubun(custGubun);
						info.setRaGubun(raGubun);
						info.setIssueEndDate( odp.getCodeData("EVDATE").substring(0, 8));
						info.setCaGubun(CAGubun);
						info.setDeptPersonName(odp.getCodeData("ORGNAME"));
						info.setIssueBankName(this.IssueBankName(CAGubun, odp.getCodeData("USERID").substring(1, 4)));
						info.setRaName(this.getRAName(raGubun, custGubun));
						// 조합번호용 인증서
						if ("84".equals(certPolicy)) {
							info.setCertPolicyCode("84");
						} else {
							info.setCertPolicyCode(this.getCertPolicyCode("2", custGubun, raGubun));
						}

						// 당행 등록된 인증서만 보여줌
						List<LdapInfoResult> ldapInfoOutputList = ldapInfoDao.selectOppraLdapInfo(odp.getCodeData("CERTSERIAL"));
						if (ldapInfoOutputList.size() > 0) {
							ldapList.add(info);
						} else { // 사이즈가 없고 , 인증서 폐기거래인데 타행 금융인증서가 존재한다..이럴경우에도 LIST에 담아준다.(CertMap 전체를 담아주자)
							if (workType.equals("RV") && certPolicy.equals("16")) {
								ldapList.add(info);
								log.debug("ldap_info 결과가 없고 인증서 폐기거래면서 금융인증서이다... ldapList : [" + ldapList.toString() + "]");
							}
						}

					}
				} // for j
			} // for i

		} catch (Exception e) {
			log.debug("//cert ldap인증서 목록 조회 오류 [" + "Exception" + "]");
			throw new PRCServiceException(e);
		} finally {
			closeOppra(oppra);
		}

		log.debug("ldapList : " + ldapList.toString());

		return ldapList;
	}

	@ComponentOperation(name = "getCertPolicyCode", description = "certPolicyCode 조회", author = "이완주")
	public String getCertPolicyCode(String caGubunValue, String custGubunValue, String raGubunValue) {
		String certPolicyCode = "";
		String certPolicyCodeHanGul = "";

		if ("1".equals(custGubunValue) && "1".equals(raGubunValue)) {
			certPolicyCode = "04";
			certPolicyCodeHanGul = "개인공동";
		} else if ("1".equals(custGubunValue) && "2".equals(raGubunValue)) {
			certPolicyCode = "01";
			certPolicyCodeHanGul = "개인범용";
		} else if ("2".equals(custGubunValue) && "1".equals(raGubunValue)) {
			certPolicyCode = "02";
			certPolicyCodeHanGul = "기업금융";
		} else if ("2".equals(custGubunValue) && "2".equals(raGubunValue)) {
			certPolicyCode = "05";
			certPolicyCodeHanGul = "기업범용";
		} else if ("2".equals(custGubunValue) && "3".equals(raGubunValue)) {
			certPolicyCode = "68";
			certPolicyCodeHanGul = "전자세금계산서";
		} else if ("1".equals(custGubunValue) && "6".equals(raGubunValue)) {
			certPolicyCode = "16";
			certPolicyCodeHanGul = "개인금융";
		}

		log.debug("getCertPolicyCode in=[{},{},{}] out=[{},{}]", caGubunValue, custGubunValue, raGubunValue, certPolicyCode, certPolicyCodeHanGul);

		return certPolicyCode;
	}

	@ComponentOperation(name = "getRAName", description = "인증서 종류(RA) 조회", author = "이완주")
	public String getRAName(String raGubun, String custGubun) {
		String name = "";

		if ("1".equals(custGubun) && "1".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009001"); // name = "개인공동";

		} else if ("1".equals(custGubun) && "2".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009002"); // name = "개인범용";

		} else if ("2".equals(custGubun) && "1".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009001"); // name = "기업금융";

		} else if ("2".equals(custGubun) && "2".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009002"); // name = "기업범용";

		} else if ("2".equals(custGubun) && "3".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009003"); // name = "전자세금계산서"

		} else if ("1".equals(custGubun) && "6".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009012"); // name = "개인금융"
		}
		if ("9".equals(raGubun)) {
			name = commonCode.getCodeItem("CRT", "CRT009004");
		}
		log.debug("RAName: " + name);

		log.debug("########################RAName Check Start##############################");
		for (int i = 0; i < 11; i++) {
			String index = i < 10 ? "0" + i : "" + i;
			log.debug("CODE Name i[" + i + "] value["+ commonCode.getCodeItem("CRT", "CRT0090" + index) + "]");
		}
		log.debug("########################RAName Check END##############################");
		return name;
	}

	@ComponentOperation(name = "IssueBankName", description = "인증서 발급 은행명 반환", author = "이완주")
	public String IssueBankName(String caGubun, String issueBank) {
		String bankName = "";
		/*
		 * 1:스탠다드차타드은행(사설) 2:금융결제원 - 각은행이름 3:한국정보인증 4:한국증권전산 5:한국전산원 6:한국전자인증 7:한국무역정보통신
		 */
		if(caGubun.length()==1) {
			caGubun = StringUtils.leftPad(caGubun, 2, '0');
		}

		if("01".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009005");

		} else if("02".equals(caGubun)) {
			if ("099".equals(issueBank)) {
				bankName = commonCode.getCodeItem("CRT", "CRT009004");
			} else {
				bankName = commonCode.getCodeItem("CRT", "CRT009004");
			}

		} else if("03".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009007");

		} else if("04".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009008");

		} else if("05".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009009");

		} else if("06".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009010");

		} else if("07".equals(caGubun)) {
			bankName = commonCode.getCodeItem("CRT", "CRT009011");

		} else {
			bankName = "미확인(" + issueBank + ")";
		}

		return bankName;
	}

	@ComponentOperation(name = "전화번호 파싱", description = "전화번호 파싱", author = "오은진")
	public String[] phoneNumberPartArray(String phoneNum) {
		log.debug("phoneNumberPartArray 시작");

		String[] phoneNumArr = {"", "", "", ""};	// phoneNo1, phoneNo2, phoneNo3, phoneNum

		log.debug("phoneNum ::: {}", phoneNum);
		if (phoneNum != null && !phoneNum.equals("")) {
			phoneNum = phoneNum.replaceAll(" ", "");
			if(phoneNum.length() < 9) {
				phoneNumArr[0] = "";
				phoneNumArr[1] = "";
				phoneNumArr[2] = "";
				phoneNumArr[3] = "";
			} else if (phoneNum.startsWith("010") || phoneNum.startsWith("011") || phoneNum.startsWith("016") || phoneNum.startsWith("018") || phoneNum.startsWith("019")) {
				if (phoneNum.length() == 12) {
					// 자릿수 : 4 4 4
					phoneNumArr[0] = phoneNum.substring(0, 4).trim();
					phoneNumArr[1] = phoneNum.substring(4, 8).trim();
					phoneNumArr[2] = phoneNum.substring(8, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				} else if (phoneNum.length() == 11) {
					// 자릿수 : 3 4 4
					phoneNumArr[0] = phoneNum.substring(0, 3).trim();
					phoneNumArr[1] = phoneNum.substring(3, 7).trim();
					phoneNumArr[2] = phoneNum.substring(7, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				} else if (phoneNum.length() == 10) {
					// 자릿수 : 3 3 4
					phoneNumArr[0] = phoneNum.substring(0, 3).trim();
					phoneNumArr[1] = phoneNum.substring(3, 6).trim();
					phoneNumArr[2] = phoneNum.substring(6, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				}
			} else if(phoneNum.startsWith("02")) {
				if (phoneNum.length() == 10) {
					// 자릿수 : 2 4 4
					phoneNumArr[0] = phoneNum.substring(0, 2).trim();
					phoneNumArr[1] = phoneNum.substring(2, 6).trim();
					phoneNumArr[2] = phoneNum.substring(6, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				} else if (phoneNum.length() == 9) {
					// 자릿수 : 2 3 4
					phoneNumArr[0] = phoneNum.substring(0, 2).trim();
					phoneNumArr[1] = phoneNum.substring(2, 5).trim();
					phoneNumArr[2] = phoneNum.substring(5, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				}
			} else {
				if (phoneNum.length() == 11) {
					// 자릿수 : 3 4 4
					phoneNumArr[0] = phoneNum.substring(0, 3).trim();
					phoneNumArr[1] = phoneNum.substring(3, 7).trim();
					phoneNumArr[2] = phoneNum.substring(7, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				} else if (phoneNum.length() == 10) {
					// 자릿수 : 3 3 4
					phoneNumArr[0] = phoneNum.substring(0, 3).trim();
					phoneNumArr[1] = phoneNum.substring(3, 6).trim();
					phoneNumArr[2] = phoneNum.substring(6, phoneNum.length()).trim();
					phoneNumArr[3] = phoneNumArr[0] + phoneNumArr[1] + phoneNumArr[2];
				}
			}
		}

		log.debug("phoneNumArr[0]{}", phoneNumArr[0]);
		log.debug("phoneNumArr[1]{}", phoneNumArr[1]);
		log.debug("phoneNumArr[2]{}", phoneNumArr[2]);
		log.debug("phoneNumArr[3]{}", phoneNumArr[3]);
		log.debug("phoneNumberPartArray 종료");
		return phoneNumArr;
	}

	@ComponentOperation(name = "씨리얼번호로 CA조회", description = "씨리얼번호로 CA조회", author = "이완주")
	public HashMap<String, String> checkCertStatus(String userSerial, String certGubun) {
		HashMap<String, String> rtn = new HashMap<String, String>();

		IniOPPRA oppra = null;

		try {
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID);
			sendMsg.append("$").append(certGubun).append("$1$").append(formatString(userSerial)).append("$000$001$");

			oppra = this.getOppra();
			oppra.requestRAW(50, sendMsg.toString());

			JohoeiDataParser jdp = new JohoeiDataParser(oppra.getResCommonPart(), oppra.getResDataPart());

			String[] records = jdp.getRecords();
			jdp.setRecord(records[0]);

			rtn.put("ExpireDate", jdp.getNotAfter().substring(0,8));	// 만료일.

//			log.debug("##cetrutil {}", records.toString());
//			log.debug("##cetrutil CertStatus["+jdp.getCertStatus()+"], RegCode["+jdp.getUserID().substring(0, 4)+"]");

			if ("10".equals(jdp.getCertStatus()) && CertConfig.RegCodeValue.equals(jdp.getUserID().substring(0, 4))) {
				log.debug("##cetrutil 인증서 상태가 유효하고 SC은행 인증서");
				rtn.put("IssueGubun", "2");
				rtn.put("SSRGubun", "2");
				rtn.put("SUCCESS", "TRUE");
			} else {
				log.debug("##cetrutil 상태가 유효하고 SC은행 인증서가 아닐때");
				rtn.put("SUCCESS", "FALSE");
			}

		} catch (IniOPPRAReadException | IniOPPRAIllegalFormatException e) {
			throw new PRCServiceException(e);
		} finally {
			this.closeOppra(oppra);
		}

		return rtn;
	}

	@ComponentOperation(name = "인증서만료일계산", description = "인증서만료일계산", author = "이완주")
	public String setExpireDate(String indate, String gijun, int period) {
		// 연도 체크
	    String yy = indate.substring(0,4);
	    String mm = indate.substring(4,6);
	    String dd = indate.substring(6,8);

	    // 계산하기 위해 정수로 변환
	    int i_yy = Integer.parseInt(yy);
	    int i_mm = Integer.parseInt(mm);
	    int i_dd = Integer.parseInt(dd);

	    // 결과값을 보관하기 위한 변수
	    String result_yy = "";
	    String result_mm = "";
	    String result_dd = "";


	    // 먼저 기간이 년 단위일때 처리
	    if ( gijun.equals( "Y" )) {
			i_yy = i_yy + period;
			result_yy = Integer.toString(i_yy);

			result_mm = mm;
			result_dd = dd;

			// 해당월이 2월 29일이면 윤년 검사를 해서 아니면 28일로 바꾼다.
			if ( mm == "02" && dd == "29" ) {
			    if ( ((i_yy % 4) == 0 && (i_yy % 100) != 0) || (i_yy % 400) == 0 ) {
					//alert("윤년");
			    } else {
					//alert("비윤년");
					result_dd = "28";
			    }
			}
		}

		// 기간이 월 단위일때 처리
	    if ( gijun.equals( "M") ) {

	        i_mm = i_mm + 1;

	        // 월이 증가한결과 년도가 바뀌게 될때
	        if ( i_mm > 12 ) {
	            i_yy = i_yy + 1;
	            i_mm = i_mm -12;
	        }

	        result_yy = Integer.toString(i_yy);
	        result_mm = Integer.toString(i_mm);
	        result_dd = Integer.toString(i_dd);

	        if ( result_mm.length()  == 1 ) {
				//alert("mm 길이가 1");
				result_mm = "0" + result_mm;
	        }

	        // 해당월이 2월 29일이면 윤년 검사를 해서 아니면 28일로 바꾼다.
	        if ( i_mm == 2 && i_dd >= 29 ) {
	            if ( ((i_yy % 4) == 0 && (i_yy % 100) != 0) || (i_yy % 400) == 0 ) {
					result_dd = "29";
	            } else {
					result_dd = "28";
	            }
	        }

			// 해당월이 30일까지인데 해당일자가 31일 이면 30일로 바꾼다.
	        if ( i_mm == 2 || i_mm == 4 || i_mm == 6 || i_mm == 9 || i_mm == 11 ) {
				if ( result_dd == "31" )
					result_dd = "30";
	        }

			if (result_dd.length() == 1 )
			  result_dd = "0" + result_dd;
		}

		String calculateDate = result_yy + result_mm + result_dd;

		// 기간이 일 단위일때 처리
	    if ( gijun.equals( "D" )) {
			calculateDate = getFutureDate(indate,period);
	    }

	    return calculateDate;
	}

	@ComponentOperation(name = "인증서만료일계산2", description = "인증서만료일계산2", author = "이완주")
	public String getFutureDate(String strToday, int d) {
		//연도 체크
	    String result_yy = strToday.substring(0,4);
	    String result_mm = strToday.substring(4,6);
	    String result_dd = strToday.substring(6,8);

	    int yy = Integer.parseInt(result_yy);
	    int mm = Integer.parseInt(result_mm);
	    int dd = Integer.parseInt(result_dd);

	    dd = dd+d;
	    int day = calFinalDay(yy, mm);

	    while ( dd > day ) {
			dd = dd - day ;

			if (mm == 12) {
				yy = yy+1;
				mm = 1;
			} else {
				mm = mm+1;
			}
			day = calFinalDay(yy,mm);
	    }

		result_yy = Integer.toString(yy);
		result_mm = Integer.toString(mm);
		result_dd = Integer.toString(dd);

		if ( result_mm.length() == 1 ) {
			result_mm = "0" + result_mm;
		}

		if ( result_dd.length() == 1 ) {
			result_dd = "0" + result_dd;
		}

		String someday = result_yy+result_mm+result_dd;

		return someday;
	}

	private int calFinalDay(int y, int m) {
		int total_days;

		if ( m == 2 ) {
			if ( ((y%4 == 0) && (y%100 != 0)) || (y%400 == 0) ) {
				total_days = 29;
			} else {
				total_days = 28;
			}
		} else if ( (m == 4) || (m == 6) || (m == 9) || (m == 11) ) {
			total_days = 30;
		} else {
			total_days = 31;
		}

		return total_days;
	}

	
	/**
	 * 인증서를 세션에 넣을때 변환하는 용도
	 * @param cert
	 * @return
	 * @throws CertificateEncodingException
	 */
	@ComponentOperation(name = "인증서를 BASE64인코딩 문자열로 반환", description = "인증서를 BASE64인코딩 문자열로 반환", author = "이완주")
	public static String getB64FromCertification(X509Certificate cert) throws CertificateEncodingException {
		return Base64.getEncoder().encodeToString(cert.getEncoded());
	}
	
	/**
	 * 인증서를 세션에서 꺼내고 변환하는 용도
	 * @param encodedCert
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 */
	@ComponentOperation(name = "BASE64문자열을 x509인증서로 반환", description = "BASE64문자열을 x509인증서로 반환", author = "이완주")
	public static X509Certificate getCertificationFromB64(String encodedCert) throws CertificateException, IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedCert);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		try(ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes)) {
			return (X509Certificate) cf.generateCertificate(bais);
		}
	}
	
	/**
	 * Edoc용 LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크
	 * 1. LDAP 또는 DB 조회하여 사용자 정보 읽어옴.
	 * 2. RA 또는 OCSP 연동 하여 유효한 인증서 검증. 
	 * @param cert
	 * @param dataType
	 * @return
	 */
	@ComponentOperation(name = "Edoc용 LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크", description = "Edoc용 LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크", author = "이완주")
	public Map<String, Object> getUserInfoByScbDBEdoc(X509Certificate cert, String dataType) {
		log.debug("dataType = {}, cert = {}", dataType, cert);
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		boolean isCertValidity = false;
		
		// 생성자로 인증서를 받아서 필요한 정보를 제공함
		CertificateHelper certHelper = new CertificateHelper(cert);
		
		if(certHelper.isYessign() || certHelper.isKFB()) {
			List<LdapInfoResult> resultSet = ldapInfoDao.selectOppraLdapInfo(certHelper.getSerial());
			
			for(LdapInfoResult ldapInfo: resultSet) {
				log.debug("ldapInfo={}", ldapInfo.toString());
				
				if(ldapInfo.getRaflag() == null || ldapInfo.getRaflag().length() == 0) {
					if(certHelper.isKFB()) {
						throw new PRCServiceException("INI938", "고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)");
						
					} else {
						throw new PRCServiceException("INI602", "고객님이 타행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)");
						
					}
				}
				
				// 당행 금결원 인증서 상태 검사
				if("1".equals(ldapInfo.getRaflag())) {
					if("S".equals(ldapInfo.getStatus())) {
						throw new PRCServiceException("INI958", "고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.");
						
					} else if (!"V".equals(ldapInfo.getStatus())) {
						throw new PRCServiceException("INI939", "인증서가 유효하지 않습니다.");
						
					}
				}
										
				//사용자 정보 세팅.
				userInfo.put("USERID",  	(String) ldapInfo.getUserid());
				userInfo.put("CID",  		(String) ldapInfo.getCid());
				userInfo.put("EXPIREDATE",  String.valueOf(ldapInfo.getExpiredate()));
				userInfo.put("ISSUEDATE",	String.valueOf(ldapInfo.getIssuedate()));
				userInfo.put("MAIL",  		(String) ldapInfo.getMail());
				userInfo.put("POLICY",  	(String) ldapInfo.getPolicy());
				userInfo.put("SERIAL",  	(String) ldapInfo.getSerial());
				isCertValidity = true;
			}
			
			// 타행 금결원 인증서 상태 검사
			if(certHelper.isYessign() && !certHelper.isKFB()) { 
				isCertValidity = certHelper.chkOPPRA();
			}
			
		} else {
			// 타기관 인증서 OCSP
			isCertValidity = certHelper.chkOCSP();
		}
		
		if (!isCertValidity) {
			throw new PRCServiceException("INI603", "고객님의 공동인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.");
		}
		
		return userInfo;
	}

}