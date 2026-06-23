/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 생성자로 인증서를 받아서 필요한 정보를 제공함
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

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.initech.cryptox.util.Hex;
import com.initech.ocspgd.OCSPGD;
import com.initech.ocspgd.OCSPGDSearchRespCodeType;
import com.initech.ocspgd.util.Code51DataParser;
import com.initech.ocspgd.util.InquiryMsg;
import com.initech.oppra.IniOPPRA;
import com.initech.oppra.util.JohoeiDataParser;
import com.initech.oppra.util.OppraSendDataParser;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.svc.shared.components.cert.dao.InfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.InfoResult;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;
import com.wizvera.crypto.CertUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CertificateHelper {
	private X509Certificate cert;

	@Getter
	private String issuerDN; // CN=yessignCA-Test Class 5,OU=AccreditedCA,O=yessign,C=kr
	@Getter
	private String subjectDN; // c=kr,o=yessign,ou=personal4IB,ou=KFB,cn=업무테스트()002304120250902123000004
	@Getter
	private String subjectCN; // 업무테스트()002304120250902123000004
	@Getter
	private String issuerO; // yessign
	@Getter
	private String issuerBank; // KFB
	@Getter
	private String policyName; // personal4IB

	@SuppressWarnings("unused")
	private CertificateHelper() {
	}

	public CertificateHelper(X509Certificate cert) {
		this.cert = cert;
		this.issuerDN = CertUtil.getIssuerDN(cert, false, false); // 인증서 발급기관 DN정보(인증서,대문자,cn부터): X509기본설정
		this.subjectDN = CertUtil.getSubjectDN(cert, true, true); // 인증서 발급주체 DN정보(인증서,소문자,역순): 고객사별로 확인필요:INITECH호환(새마을금고)
		this.subjectCN = CertUtil.getSubjectEntry(cert, CertUtil.NAME_CN, 0); // 인증서 발급주체 CN정보(인증서,엔트리,순번)
		this.issuerO = CertUtil.getSubjectEntry(cert, CertUtil.NAME_O, 0); // 인증서 발급기관명(타기관 구분용)(EX:yessign,KICA,SignKorea,NCASign,TradeSign,CrossCert)
		this.issuerBank = CertUtil.getSubjectEntry(cert, CertUtil.NAME_OU, 0); // 인증서 발급은행 코드명(EX : KFB)
		this.policyName = CertUtil.getSubjectEntry(cert, CertUtil.NAME_OU, 1); // 인증서 정책명(EX : personalF)
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" CertificateHelper \r\n");
		sb.append("issuerDN=[" + issuerDN + "] \r\n");
		sb.append("subjectDN=[" + subjectDN + "] \r\n");
		sb.append("subjectCN=[" + subjectCN + "] \r\n");
		sb.append("issuerO=[" + issuerO + "] \r\n");
		sb.append("issuerBank=[" + issuerBank + "] \r\n");
		sb.append("issuerCode=[" + getIssuerCode() + "] \r\n");
		sb.append("policyName=[" + policyName + "] \r\n");
		sb.append("serial=[" + getSerial() + "] \r\n");
		sb.append("policyOid=[" + CertUtil.getCertificatePolicyOID(cert) + "] \r\n");
		sb.append("policyCode=[" + getPolicyCode() + "] \r\n");
		sb.append("만료일=[" + getExpireDate() + "] \r\n");
		return sb.toString();
	}

	/**
	 * VID검사
	 * vidRandom : signVerifierResult.getVidRandom()
	 *
	 * @param ci
	 * @param vidRandom
	 * @return
	 */
//	@SuppressWarnings("static-access")
//	public boolean checkVID(String ci, String vidRandom) {
//		try {
//			VIDChecker checker = new VIDChecker();
//			return checker.checkVID(cert, ci, vidRandom.getBytes());
//		} catch (Exception e) {
//			return false;
//		}
//	}

	/**
	 * 발급자 코드를 얻어오는 함수
	 * 01 : 금결원(yessign)
	 * 02 : 무역정보통신(TradeSign)
	 * 03 : 전자인증(CrossCert)
	 * 04 : 코스콤 증권전산원(SignKorea)
	 * 05 : 한국정보인증(SignGate)
	 * 06 : 한국전산원(NCA)
	 *
	 * @return
	 */
	public String getIssuerCode() {
		String result = "";
		if (issuerDN.indexOf("yessign") >= 0) {
			result = "01";
		} else if (issuerDN.indexOf("TradeSign") >= 0) {
			result = "02";
		} else if (issuerDN.indexOf("CrossCert") >= 0) {
			result = "03";
		} else if (issuerDN.indexOf("SignKorea") >= 0) {
			result = "04";
		} else if (issuerDN.indexOf("SignGate") >= 0) {
			result = "05";
		} else if (issuerDN.indexOf("NCA") >= 0) {
			result = "06";
		}
		return result;
	}

	/**
	 * 인증서 시리얼번호 획득 (10진수) 456789
	 *
	 * @return
	 */
	public String getSerialDecimal() {
		return CertUtil.getSerialDecimal(cert);
	}

	/**
	 * 인증서 시리얼번호 획득 (16진수) 0A10
	 *
	 * @return
	 */
	public String getSerialHex() {
//		return CertUtil.getSerialHex(cert);
		return Hex.dumpHex(cert.getSerialNumber().toByteArray());
	}

	/**
	 * 인증서 시리얼번호 획득 (12자리) 000000456789
	 *
	 * @return
	 */
	public String getSerial() {
		return StringUtils.leftPad(CertUtil.getSerialDecimal(cert), 12, '0');
	}

	/**
	 * oid 획득 1.2.410.200005.1.1.1.10
	 *
	 * @return
	 */
	public String getCertificatePolicyOID() {
		return CertUtil.getCertificatePolicyOID(cert);
	}

	/**
	 * 은행코드 획득 KFB:023
	 *
	 * @return
	 */
	public String getIssuerBankCode() {
		int idx = subjectCN.lastIndexOf(")");
		if (idx != -1) {
			return subjectCN.substring(idx + 2, idx + 5);
		}
		return null;
	}

	/**
	 * 금결원 인증서면 true
	 *
	 * @return
	 */
	public boolean isYessign() {
		return issuerDN.indexOf("yessign") >= 0;
	}

	/**
	 * 당행발급 인증서면 true
	 *
	 * @return
	 */
	public boolean isKFB() {
		return subjectDN.indexOf("OU=KFB") != -1 || subjectDN.indexOf("ou=KFB") != -1;
	}

	/**
	 * 상호연동인증서면 true
	 *
	 * @return
	 */
	public boolean isCrossCertificate() {
		return this.getPolicyCode() != null;
	}

	/**
	 * 금융인증서면 true
	 *
	 * @return
	 */
	public boolean isFincert() {
		return subjectDN.indexOf("ou=personalF") != -1 || subjectDN.indexOf("OU=personalF") != -1;
	}

	/**
	 * 인증서 만료일자 yyyyMMdd
	 *
	 * @return
	 */
	public String getExpireDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(cert.getNotAfter());
	}

	/**
	 * OID policy code 조회
	 * 01 범용
	 * 04 개인
	 * 05 기업
	 * 16 금융인증서
	 *
	 * @return
	 */
	public String getPolicyCode() {
		return CRT_OID_MAP.get(this.getCertificatePolicyOID());
	}

	public boolean checkOID() {
		String oid = this.getPolicyCode();
		return oid != null && !"".equals(oid);
	}

	/**
	 * 인증서로 DB 조회
	 *
	 * @param dataType
	 * @return
	 */
	public HashMap<String, Object> getUserInfoByScbDB(String dataType) {
		boolean isValid = false;
		HashMap<String, Object> userInfo = new HashMap<String, Object>();

		LdapInfoDao ldapInfoDao = RuntimeContext.getBean(LdapInfoDao.class);
		InfoDao infoDao = RuntimeContext.getBean(InfoDao.class);

		String resultCode = null;
		String resultMsg = null;

		if (this.isYessign() || this.isKFB()) {
			// 금결원인증서 or 당행인증서 LDAP_INFO 조회
			List<LdapInfoResult> ldapInfoList = ldapInfoDao.selectOppraLdapInfo(this.getSerial());
			for (LdapInfoResult ldapInfo : ldapInfoList) {
				String raFlag = ldapInfo.getRaflag();
				String status = ldapInfo.getStatus();

				if (raFlag == null || raFlag.length() == 0) {
					if (this.isKFB()) {
						resultCode = "INI938";
						resultMsg = "고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)";
						throw new PRCServiceException(resultCode, resultMsg);
					} else {
						resultCode = "INI602";
						resultMsg = "고객님이 타행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)";
						throw new PRCServiceException(resultCode, resultMsg);
					}
				}

				// 인증서의 상태가 유효하지 않을 경우
				if ("1".equals(raFlag) && "S".equals(status)) {
					resultCode = "INI958";
					resultMsg = "고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.";
					throw new PRCServiceException(resultCode, resultMsg);
				} else if ("1".equals(raFlag) && !"V".equals(status)) {
					resultCode = "INI939";
					resultMsg = "인증서가 유효하지 않습니다.";
					throw new PRCServiceException(resultCode, resultMsg);
				}

				userInfo.put("USERID", ldapInfo.getUserid());
				userInfo.put("CID", ldapInfo.getCid());
				userInfo.put("EXPIREDATE", String.valueOf(ldapInfo.getExpiredate()));
				userInfo.put("ISSUEDATE", String.valueOf(ldapInfo.getIssuedate()));
				userInfo.put("MAIL", ldapInfo.getMail());
				userInfo.put("SERIAL", ldapInfo.getSerial());
				userInfo.put("POLICY", ldapInfo.getPolicy());

				isValid = true;
			} // end-for

			if (this.isYessign() && !this.isKFB()) {
				isValid = chkOPPRA();
			}

		} else {
			// 타기관인증서 INFO 테이블 조회
			List<InfoResult> infoList = infoDao.selectSerialInfo(this.getSerialHex(), this.getCertificatePolicyOID());
			for (InfoResult info : infoList) {
				userInfo.put("USERID", info.getUserid());
				userInfo.put("CID", info.getIdnum());
				userInfo.put("SERIAL", info.getSerial());
				userInfo.put("ISSUER", info.getIssuer());
				userInfo.put("EXPIRED", String.valueOf(info.getExpired()));
				userInfo.put("REGISTERD", String.valueOf(info.getRegisterd()));
				userInfo.put("OID", info.getOid());
				userInfo.put("CERT", info.getCert());
			}
			isValid = chkOCSP();
		}

		if (!isValid || (userInfo == null || userInfo.get("USERID") == null)) {
			if (this.isKFB()) {
				if (this.isFincert()) {
					resultCode = "INI303";
					resultMsg = "고객님의 금융인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.";
				} else {
					resultCode = "INI603";
					resultMsg = "고객님의 공동인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.";
				}
				throw new PRCServiceException(resultCode, resultMsg);
			}

			if (!"NS".equals(dataType) && !"OC".equals(dataType) && !"MD".equals(dataType)) {
				if (this.isFincert()) {
					resultCode = "INI310";
					resultMsg = "선택하신 금융인증서는 타기관인증서 등록 후 이용할 수 있습니다.\n" + "타기관인증서 등록 화면으로 이동하시겠습니까?\n";
				} else {
					resultCode = "INI800";
					resultMsg = "타행 타기관 인증서 등록이 필요합니다.";
				}
				throw new PRCServiceException(resultCode, resultMsg);
			}
		}

		return userInfo;
	}

	/**
	 * LDAP 및 DB연동하여 사용자 조회 및 유효한 인증서 체크 유효하면 true
	 * 금결원&&당행 = LDAP
	 * 금결원&&타행 = OPPRA
	 * 타기관 = OCSP
	 *
	 * @return
	 */
	public boolean chkCertStatus() throws PRCServiceException {
		boolean isValid = false;

		if (this.isYessign() || this.isKFB()) {
			// 금결원 OR 당행발급

			if (this.isYessign() && this.isKFB()) {
				// 금결원 && 당행 = LDAP 조회
				LdapInfoDao dao = RuntimeContext.getBean(LdapInfoDao.class);
				List<LdapInfoResult> ldapInfoList = dao.selectOppraLdapInfo(this.getSerial());
				for (LdapInfoResult ldapInfo : ldapInfoList) {
					String raFlag = ldapInfo.getRaflag(); // 당행1, 타행 0
					String status = ldapInfo.getStatus(); // 정상V, 효력정지S

					// 당행발급인증서 raFlag없으면 오류
					if (this.isKFB() && StringUtils.isEmpty(raFlag)) {
						throw new PRCServiceException("INI938", "고객님이 당행에서 발급 받은 인증서 정보를 확인해 주세요(LDAP_INFO 테이블 조회결과 무)");
					}

					// 유효하지 않은 인증서
					if ("1".equals(raFlag) && "S".equals(status)) {
						throw new PRCServiceException("INI958", "고객님이 제출한 당행에서 발급된 인증서는 현재 효력 정지되어 있어요. 인터넷뱅킹을 통해 효력 회복하시면 사용할 수 있어요.");
					} else if ("1".equals(raFlag) && !"V".equals(status)) {
						throw new PRCServiceException("INI939", "인증서가 유효하지 않습니다.");
					}
					isValid = true;
				}

			} else {
				// 금결원 && 타행 = OPPRA 조회
				isValid = this.chkOPPRA();
			}

		} else {
			// 타기관 = OCSP
			isValid = this.chkOCSP();
		}

		if (!isValid) {
			if (this.isFincert()) {
				throw new PRCServiceException("INI303", "고객님의 금융인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.");
			} else {
				throw new PRCServiceException("INI603", "고객님의 공동인증서는 타기관인증서 등록이 되지 않았거나 폐기된 인증서에요. 확인 후 이용해 주세요.");
			}
		}

		return isValid;
	}

	/**
	 * OPPRA 50번 조회
	 *
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean chkOPPRA() {
		boolean isValid = false;
		IniOPPRA oppra = null;
		String resultCode = "";
		String resultMsg = "";
		String certStatusCheck = "";
		String certResultCode = "";

		try {

			if (this.isYessign() && !this.isKFB()) {

				Map map = new HashMap();
				map.put("SERVICEPROVIDER", "01");
//				map.put("CACODE", this.getIssuerBankCode());
				map.put("CACODE", this.getIssuerCode());
				map.put("RegCodeValue", CertConfig.RegCodeValue);
				map.put("MANAGERID", CertConfig.ADID);
				map.put("CERTCODE", this.getPolicyCode());
				map.put("SEARCHCODE", "1");
				map.put("SEARCHCONTENTS", this.getSerial());
				map.put("REQRECORDSTARTNO", "000");
				map.put("REQRECORDNO", "001");

				OppraSendDataParser oppraSendDataParser = new OppraSendDataParser(50, map);
				log.debug("========================================");
				log.debug("OPPRA >> map : [" + map + "]");
				log.debug("OPPRA >> Request Msg:[" + oppraSendDataParser.getSendLastData() + "]");
				log.debug("========================================");

				/*
				 * OPPRA서버 연동.
				 */
//				oppra = new IniOPPRA(map, CertConfig.INITECH_PROPERTIES_OPPRA_PATH);
				oppra = new IniOPPRA(CertConfig.OPPRAIP, CertConfig.OPPRAPORT);
				oppra.initialize();
				oppra.setCharEncoding("EUC-KR");
				oppra.requestRAW(oppraSendDataParser.getSendLastData());

				/*
				 * OPPRA서버 데이터 조회.
				 */
				JohoeiDataParser jdp = new JohoeiDataParser(oppra.getResCommonPart(), oppra.getResDataPart());
				jdp.setRecord(jdp.getRecords()[0]);

				/*
				 * 조회 결과 사용자 정보 세팅.
				 */
				log.debug("======================================== ");
				log.debug("(●'◡'●) getCertStatus ### 1. [" + jdp.getCertStatus() + "]");
				log.debug("(●'◡'●) getCode ### 2. [" + jdp.getCode() + "]");
				log.debug("(●'◡'●) getMsg ### 3. [" + jdp.getMsg() + "]");
				log.debug("(●'◡'●) getNotAfter ### 4. [" + jdp.getNotAfter() + "]");
				log.debug("(●'◡'●) getUserID  ### 5. [" + jdp.getUserID() + "]");
				log.debug("(●'◡'●) getInitechResMsg ### 6. [" + jdp.getInitechResMsg() + "]");
				log.debug("(●'◡'●) getInitechResCode ### 7. [" + jdp.getInitechResCode() + "]");
				log.debug("(●'◡'●) getResMsg ### 8. [" + jdp.getResMsg() + "]");
				log.debug("(●'◡'●) getResCode ### 9. [" + jdp.getResCode() + "]");
				log.debug("(●'◡'●) getTotalRecordNum ### 10. [" + jdp.getTotalRecordNum() + "]");
				log.debug("(●'◡'●) getCertPolicy ### 11. [" + jdp.getCertPolicy() + "]");
				log.debug("(●'◡'●) getResLen ### 12. [" + jdp.getResLen() + "]");
				log.debug("(●'◡'●) getCertSerial ### 13. [" + jdp.getCertSerial() + "]");
				log.debug("(●'◡'●) getCurrentRecorNum ### 14. [" + jdp.getCurrentRecorNum() + "]");
				log.debug("(●'◡'●) getJuminID ### 15. [" + jdp.getJuminID() + "]");
				log.debug("(●'◡'●) getSubjectDN ### 16. [" + jdp.getSubjectDN() + "]");
				log.debug("========================================");

				certStatusCheck = jdp.getCertStatus(); // RA조회시 인증서 상태 코드
				certResultCode = jdp.getResCode(); // RA조회시 인증서 조회결과 코드

				log.debug("(●'◡'●) certStatusCheck : [" + certStatusCheck + "]");
				log.debug("(●'◡'●) certResultCode : [" + certResultCode + "]");
				if ("000".equals(certResultCode) && "10".equals(certStatusCheck)) {
					isValid = true;
				} else {
					throw new Exception("");
				}

			} else {
				isValid = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (certStatusCheck.equals("30")) {
				resultCode = "INI968";
				resultMsg = "현재 로그인 시도한 인증서는 타행에서 발급되었거나 폐기된 인증서 입니다. 인증서 상태를 확인해 주세요.";
				throw new PRCServiceException(resultCode, resultMsg);
			} else if (certStatusCheck.equals("40")) {
				resultCode = "INI969";
				resultMsg = "현재 로그인 시도한 인증서는 타행에서 발급되었거나 효력정지된 인증서 입니다. 발급받으신 은행에서 효력회복후 사용 가능 합니다.";
				throw new PRCServiceException(resultCode, resultMsg);
			} else {
				resultCode = "INI856";
				resultMsg = "전자서명 인증과정에서 실패하였습니다. ID, PASSWORD 방식으로 로그인하여 주십시요.";
				throw new PRCServiceException(resultCode, resultMsg);
			}
		} finally {
			if (oppra != null)
				try {
					oppra.close();
				} catch (Exception ignore) {
				}
		}

		return isValid;
	}

	/**
	 * OCSP 50번 조회
	 *
	 * @return
	 */
	public boolean chkOCSP() {

		boolean isValid = false;
		OCSPGD ocspgd = null;
		String resultCode = "";
		String resultMsg = "";
		String certStatusCheck = "";
		String certResultCode = "";

		String data = null;
		String resCode = null; // 응답코드
		String resMsg = null; // 응답메시지
		String resStatus = null; // 인증서상태
		try {
			String inmsg = InquiryMsg.createStatusCheckMsg(cert);
			log.debug("inmsg :: {}",inmsg);

			inmsg = chkIssuerDN(inmsg);
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(CertConfig.RegCodeValue).append("$").append(CertConfig.ADID).append("$").append(inmsg);

			/*
			 * OCSPGD서버 연동.
			 */
//			ocspgd = new OCSPGD(CertConfig.INITECH_PROPERTIES_OCSPGD_PATH);
			ocspgd = new OCSPGD(CertConfig.OCSPGD_IP, CertConfig.OCSPGD_PORT);
			ocspgd.initialize();

			/*
			 * OCSP서버 데이터 조회.
			 */
			ocspgd.requestRAW(50, sendMsg.toString());

			data = ocspgd.getResponseDataPart();
			resCode = Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ResCode);
			resMsg = Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ResMsg);
			resStatus = Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.Status);

			certStatusCheck = resStatus; // OCSP조회시 인증서 상태 코드
			certResultCode = resCode; // OCSP조회시 인증서 조회결과 코드

			log.debug("========================================");
			log.debug("OCSP 응답코드:[" + resCode + "]");
			log.debug("OCSP 응답메시지:[" + resMsg + "]");
			log.debug("OCSP 상태:[" + resStatus + "]");

			log.debug("전문길이:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.RecordLen) + "]");
			log.debug("응답메시지코드:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ResCode) + "]");
			log.debug("응답메시지:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ResMsg) + "]");
			log.debug("은행코드:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.RegOrgCode) + "]");
			log.debug("조회일자:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ProduceAt) + "]");
			log.debug("DN값:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.IssuerDN) + "]");
			log.debug("6:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.SerialNum) + "]");
			log.debug("인증서상태 코드:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.Status) + "]");
			log.debug("8:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.Revoked) + "]");
			log.debug("인증서 상태 메시지:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.RevokedReason) + "]");
			log.debug("조회일자:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.ThisUpdate) + "]");
			log.debug("11:[" + Code51DataParser.getMsg(data, OCSPGDSearchRespCodeType.NextUpdate) + "]");
			log.debug("========================================");

			if (certResultCode.equals("000") && certStatusCheck.equals("10")) {
				isValid = true;
			} else {
				throw new Exception("");
			}

		} catch (Exception e) {
			if (certStatusCheck.equals("30") && certResultCode.equals("000")) {
				resultCode = "INI978";
				resultMsg = "고객님이 제출한 타기관에서 발급한 인증서는 현재 폐기되어 있어요. 확인 후 이용해 주세요.";
				throw new PRCServiceException(resultCode, resultMsg);
			} else if (certStatusCheck.equals("40") && certResultCode.equals("000")) {
				resultCode = "INI979";
				resultMsg = "고객님이 제출한 타기관에서 발급한 인증서는 현재 효력 정지되어 있어요. 발급한 기관에서 효력 회복하시면 사용할 수 있어요.";
				throw new PRCServiceException(resultCode, resultMsg);
			} else if (certResultCode.equals("120")) {
				resultCode = "INI120";
				resultMsg = "고객께서 제출하신 인증서는 은행 거래에 사용할 수 없는 인증서 입니다. 올바른 인증서인지 확인해주세요. (OID 부적합)";
				throw new PRCServiceException(resultCode, resultMsg);
			} else if (certResultCode != "" && certStatusCheck != "") {
				resultCode = "INI999";
				resultMsg = "알수없는에러가 발생하였습니다. 에러코드 : " + certResultCode + "  인증서 상태 : " + certStatusCheck;
				throw new PRCServiceException(resultCode, resultMsg);
			} else {
				resultCode = "INI856";
				resultMsg = "전자서명 인증과정에서 실패하였습니다. ID, PASSWORD 방식으로 로그인하여 주십시요.";
				throw new PRCServiceException(resultCode, resultMsg);
			}
		} finally {
			if (ocspgd != null)
				try {
					ocspgd.close();
				} catch (Exception ignore) {
				}
		}

		return isValid;
	}

	public String chkIssuerDN(String inmsg) {
		String input = inmsg;
		boolean isOK = (input.startsWith("cn=") || input.startsWith("CN="));

		if (!isOK) {
			StringBuffer returnVal = new StringBuffer();
			String[] inputArray = input.split("\\$");

			for (int i = 0; inputArray.length > i; i++) {
				if (i == 0) {
					String[] inputArray2 = inputArray[i].split(",");
					String returnVal2 = "";

					// 배열에 큰값부터 출력
					for (int j = inputArray2.length; j > 0; j--) {
						// 배열이 0인경우 추가하지않음
						if (j - 1 == 0) {
							returnVal2 += inputArray2[j - 1];
						} else {
							returnVal2 += inputArray2[j - 1] + ",";
						}
					}

					// 역순정렬값 저장
					inputArray[i] = returnVal2;
				}

				// split값 뒤에 $ 추가
				returnVal.append(inputArray[i]).append("$");
			}

			input = returnVal.toString();
		}
		return input;
	}

	/**
	 * oid값에 따른 custGubun(발급자구분(1:개인,2:기업))값 세팅
	 *
	 * @param oid
	 * @return
	 */
	public static String getCrtCustGubun(String oid) {
		return CRT_POLICY_CUST_GUBUN_MAP.get(CRT_OID_MAP.get(oid));
	}

	/**
	 * 여기 없는 공인인증서는 받지 않음
	 */
	private static final Map<String, String> CRT_OID_MAP = new HashMap<>();
	static {
		CRT_OID_MAP.put("1.2.410.200005.1.1.1.10", "16"); // 금결원 금융인증서
		CRT_OID_MAP.put("1.2.410.200005.1.1.1", "01"); // 금결원 범용 개인
		CRT_OID_MAP.put("1.2.410.200005.1.1.4", "04"); // 금결원 은행/보험용 개인
		CRT_OID_MAP.put("1.2.410.200005.1.1.5", "05"); // 금결원 범용 기업
		CRT_OID_MAP.put("1.2.410.200005.1.1.2", "02"); // 금결원 금융거래용 기업
		CRT_OID_MAP.put("1.2.410.200004.5.2.1.2", "01"); // 한국정보인증 1등급(범용) 개인
		CRT_OID_MAP.put("1.2.410.200004.5.2.1.7.1", "04"); // 한국정보인증
		CRT_OID_MAP.put("1.2.410.200004.5.2.1.1", "05"); // 한국정보인증 1등급(범용) 기업
		CRT_OID_MAP.put("1.2.410.200004.5.1.1.5", "01"); // 한국증권전산 범용 개인
		CRT_OID_MAP.put("1.2.410.200004.5.1.1.7", "05"); // 한국증권전산 범용 기업
		CRT_OID_MAP.put("1.2.410.200004.5.4.1.1", "01"); // 한국전자인증 범용 개인
		CRT_OID_MAP.put("1.2.410.200004.5.4.1.101", "04"); // 한국전자인증 은행/보험용 개인
		CRT_OID_MAP.put("1.2.410.200004.5.4.1.2", "02"); // 한국전자인증 범용 기업(입찰용)
		CRT_OID_MAP.put("1.2.410.200012.1.1.1", "01"); // 한국무역정보통신 범용 개인
		CRT_OID_MAP.put("1.2.410.200012.1.1.101", "04"); // 한국무역정보통신 은행거래용/보험용 개인인증서
		CRT_OID_MAP.put("1.2.410.200012.1.1.3", "05"); // 한국무역정보통신 범용 기업
		CRT_OID_MAP.put("1.2.410.200004.5.3.1.1", "05"); // 한국전산원 기관용
		CRT_OID_MAP.put("1.2.410.200004.5.3.1.2", "05"); // 한국전산원 법인용
		CRT_OID_MAP.put("1.2.410.200004.5.3.1.9", "01"); // 한국전산원 개인용
		CRT_OID_MAP.put("1.2.410.200004.2.201", "04"); // 한국정보인증 UCPID통합테스트??
		CRT_OID_MAP.put("1.2.410.200004.2.212", "04");
		CRT_OID_MAP.put("1.2.410.200005.1.1.4.8", "84");
	}

	/**
	 * certPolicy에 따른 custGubun(발급자구분(1:개인,2:기업))
	 */
	private static final Map<String, String> CRT_POLICY_CUST_GUBUN_MAP = new HashMap<>();
	static {
		CRT_POLICY_CUST_GUBUN_MAP.put("01", "1");
		CRT_POLICY_CUST_GUBUN_MAP.put("02", "2");
		CRT_POLICY_CUST_GUBUN_MAP.put("03", "1");
		CRT_POLICY_CUST_GUBUN_MAP.put("04", "1");
		CRT_POLICY_CUST_GUBUN_MAP.put("05", "2");
		CRT_POLICY_CUST_GUBUN_MAP.put("68", "2");
		CRT_POLICY_CUST_GUBUN_MAP.put("16", "1");
		CRT_POLICY_CUST_GUBUN_MAP.put("84", "1");
	}

}