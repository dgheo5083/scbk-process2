package com.scbank.process.api.svc.common.service.certification;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89200Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89200Res;
import com.scbank.process.api.edmi.dto.oltp.CbSms01S00200Req;
import com.scbank.process.api.edmi.dto.oltp.CbSms01S00200Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.dao.FidoCertificateInfoDao;
import com.scbank.process.api.svc.common.dao.PinUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupCertListDataParameter;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtParameter;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertForceRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertForceRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctAuthorizeDigitalcertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctConfirmDigitalcertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctConfirmDigitalcertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctConfirmDigitalcertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctConfirmDigitalcertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctGetDigitalcertRegStatusRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctGetDigitalcertRegStatusResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctGetDigitalcertStatusRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctGetDigitalcertStatusResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertIssueSecurityRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertIssueSecurityResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertIssueUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertIssueUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertRevokeSecurityRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertRevokeSecurityResponse;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertRevokeUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.digital.CrtDctValidateDigitalcertRevokeUserResponse;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.fido.FidoHttpComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name="공통-인증센터-디지털인증서", url="/certification/digital")
public class CertificationDigitalService {

	// 세션
	private final ISessionContextManager sessionManager;

	// 전문
	private final HostClient hostClient;

	// 인증센터 공통
	private final CertUtils certUtils;
	private final FidoHttpComponent fidoHttpComponent;
	private final CertificationSharedComponent certificationSharedComponent;

	// DBIO
	private final FidoCertificateInfoDao fidoCertificateInfoDao;
	private final PinUsrMgtDao pinUsrMgtDao;

	// 공통코드
	private final ICodeManager codeManager;

	// IPINSIDE
	private final IpinsideComponent ipinsideComponent;

	/************************************************************************************************************************************************
	 * 디지털인증서 관리
	 * getDigitalcertStatus : 디지털인증서 조회(관리)
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/getDigitalcertStatus", name = "디지털인증서 조회(관리)")
	public CrtDctGetDigitalcertStatusResponse getDigitalcertStatus(CrtDctGetDigitalcertStatusRequest request) {
		// MA3CRTMNG004_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());
		String appId = request.getFidoAppId();
		String deviceId = request.getFidoDeviceid();
		String faceDevice = request.getFaceDevice();

		String userId = sessionManager.getLoginValue("UserID", String.class);

		CrtDctGetDigitalcertStatusResponse response = new CrtDctGetDigitalcertStatusResponse();

		if (userId != null && !"".equals(userId)) {

			try {
				sessionManager.setGlobalValue("faceDevice", faceDevice);
				Map<String, Object> input = new HashMap<String, Object>();
				input.put("ISSUE_FACEID", request.getIssueFaceid());
				input.put("ISSUE_PIN", request.getIssuePin());
				input.put("ISSUE_BIO", request.getIssueBio());
				Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(userId, deviceId, appId, "", input);
				response.setRegYn("Y");
				response.setIssuedCertListSize((int) fidoResultMap.get("issuedCertListSize"));
				response.setIssuedCertListJsonString((String) fidoResultMap.get("issuedCertListJSONString"));

			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			} finally {
				sessionManager.removeGlobalValue("faceDevice");
			}

		} else{
			response.setRegYn("N");
		}

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 디지털인증서 발급/재발급
	 * validateDigitalcertIssueUser : 디지털인증서 발급 본인확인
	 * validateDigitalcertIssueSecurity : 디지털인증서 발급 보안매체 검증
	 * getDigitalcertRegStatus : 디지털인증서 등록상태 조회
	 * authorizeDigitalcertForceRevoke : 디지털인증서 강제폐기
	 * authorizeDigitalcertIssue : 디지털인증서 발급
	 * confirmDigitalcertIssue : 디지털인증서 발급 완료
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateDigitalcertIssueUser", name = "디지털인증서 발급 본인확인")
	public CrtDctValidateDigitalcertIssueUserResponse validateDigitalcertIssueUser(CrtDctValidateDigitalcertIssueUserRequest request) {
		// MA3CRTMNG001_201S, MA3CRTMNG001_302S, MA3CRTMNG001_501S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("certIssueSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		CrtDctValidateDigitalcertIssueUserResponse response = new CrtDctValidateDigitalcertIssueUserResponse();

		// 입력값 보관
		sessionManager.setGlobalValue("appId", request.getAppId());
		sessionManager.setGlobalValue("deviceId", request.getDeviceId());
		sessionManager.setGlobalValue("os", request.getOs());
		sessionManager.setGlobalValue("spass", request.getSpass());
		sessionManager.setGlobalValue("fingerprintDevice", request.getFingerprintDevice());
		sessionManager.setGlobalValue("fingerprintEnrolled", request.getFingerprintEnrolled());
		sessionManager.setGlobalValue("irisDevice", request.getIrisDevice());
		sessionManager.setGlobalValue("irisEnrolled", request.getIrisEnrolled());
		sessionManager.setGlobalValue("faceDevice", request.getFaceDevice());
		sessionManager.setGlobalValue("faceEnrolled", request.getFaceEnrolled());
		sessionManager.setGlobalValue("pinEnabled", request.getPinEnabled());
		sessionManager.setGlobalValue("lockYN", request.getLockYN());

		// 개인정보 노출자 조회
		String ssn1 = request.getCustJumin1();
		String ssn2 = request.getCustJumin2();
		String perBusNo = ssn1 + ssn2;

		Map<String, String> H504Map = certificationSharedComponent.getYoLRSOTGB(perBusNo);
		String YOLRSOTGB = H504Map.get("YOLRSOTGB");

		// 개인정보노출등록구분 값이 1이면 거래불가
		if ("1".equals(YOLRSOTGB)) {
			response.setYoLRSOTGB("1");
			return response;

		} else {
			response.setYoLRSOTGB("3");
		}

		String userId = sessionManager.isLogin() ? sessionManager.getGlobalValue("UserID", String.class) : "FIRST999";
		String phoneNo = sessionManager.getGlobalValue("phoneNo", String.class);
		String accountNum = StringUtils.nvl(request.getAcctNum(), "").replaceAll("-", "");
		String accountPw = request.getAcctBb();

		// 아이디찾기 전문 처리
		OltpRequestOptions h892Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
		// 공통부 세팅
		h892Options.setImsTranCd("TI1IBK01");
		h892Options.setInClassCd("H892");
		h892Options.setSvcCd("892");
		//개별부 세팅
		CbIbk01H89200Req h892Req = new CbIbk01H89200Req();
		h892Req.setUserID(userId); // 이용자 ID
		h892Req.setE2ERegNum(perBusNo); // 주민사업자번호
		h892Req.setActType("2"); // 처리구분(예비처리) 1:고객정보조회(본처리) 2:계좌검증 3:계좌미검증 4:법인ID검증 5:신용카드조회회원
		h892Req.setAcctNum(accountNum); //본인계좌번호
		h892Req.setAcctPassword(accountPw); //본인 계좌비밀번호
		CbIbk01H89200Res h892Res = this.hostClient.sendOltp(h892Options, h892Req, CbIbk01H89200Res.class).getResponse();

		// 실명증표구분 값이 3이면 발급 불가
		String YOCFJMGB = h892Res.getYOCFJMGB();
		if ("3".equals(YOCFJMGB)) {
			response.setYoCFJMGB("3");
			return response;

		} else {
			response.setYoCFJMGB(YOCFJMGB);
		}

		// 본인인증 데이터 보관
		sessionManager.setGlobalValue("UserID", h892Res.getUserID2());
		sessionManager.setGlobalValue("DeptPersonName", h892Res.getCustName());
		sessionManager.setGlobalValue("PerBusNo", perBusNo);
		// 이제 필요 없을듯
//		sessionManager.setGlobalValue("SafeCardKind", ti1ibk01H892Res.getSafeCardKind()); // 보안카드종류
//		sessionManager.setGlobalValue("SaupjaNo", perBusNo); // 개인사업자번호
//
//		// 보안매체 인증용
//		sessionManager.setLoginValue("UserID", ti1ibk01H892Res.getUserID2());
//		sessionManager.setLoginValue("SafeCardKind", ti1ibk01H892Res.getSafeCardKind()); // 보안카드종류
//		sessionManager.setLoginValue("accountNum", accountNum);
//		sessionManager.setLoginValue("accountPW", accountPw);

		// 본인확인 전문 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		// 공통부 세팅
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01H00600Req h006Req = new CbIbk01H00600Req();
		h006Req.setUserID(h892Res.getUserID2()); // CB_IBK01_H892 응답 사용
		h006Req.setRegNo(perBusNo);
		h006Req.setJuminSaupjaNo(perBusNo);
		h006Req.setCertTranCode("1");
		h006Req.setChuryGubun("N");
		h006Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		h006Req.setCustGubun("1");
		CbIbk01H00600Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00600Res.class).getResponse();

		if("3".equals(h006Res.getPersonOrCompanyCode())){
			throw new PRCServiceException("PRCCRT0001" ,"사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		if("13".equals(h006Res.getYOIDGB()) || "14".equals(h006Res.getYOIDGB())) {
			throw new PRCServiceException("PRCCRT0001" ,"사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		if(sessionManager.isLogin()) {
			sessionManager.setLoginValue("TelCode", h006Res.getTelCode());
			sessionManager.setLoginValue("SafeCardState", "1");
			sessionManager.setLoginValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
			sessionManager.setLoginValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
			sessionManager.setLoginValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
			sessionManager.setLoginValue("SafeCardIndex1", h006Res.getSafeCardIndex1());
			sessionManager.setLoginValue("SafeCardIndex2", h006Res.getSafeCardIndex2());
			sessionManager.setLoginValue("SafeCardIndex3", h006Res.getSafeCardIndex3());
			sessionManager.setLoginValue("TSPassword", h006Res.getTSPassword());
			sessionManager.setLoginValue("InforGubun", h006Res.getInforGubun());
		}
		sessionManager.setGlobalValue("UserID", h006Res.getUserID());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());
		sessionManager.setGlobalValue("TelCode", h006Res.getTelCode());
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("TransPWUseYN", h006Res.getYOISYN());
		sessionManager.setGlobalValue("SafeCardKind", h006Res.getSafeCardKind());
		sessionManager.setGlobalValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardIndex1", h006Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardIndex2", h006Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardIndex3", h006Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", h006Res.getSecurityIndex());
		sessionManager.setGlobalValue("SafeCardINDEX2", h006Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SecurityIndex", h006Res.getSecurityIndex());
		sessionManager.setGlobalValue("SecurityIndex2", h006Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SmartOTP", h006Res.getSmartOTP());
		sessionManager.setGlobalValue("TSPassword", h006Res.getTSPassword());
		sessionManager.setGlobalValue("InforGubun", h006Res.getInforGubun());
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		sessionManager.setGlobalValue("HPOne", telCode[0]);
		sessionManager.setGlobalValue("HPTwo", telCode[1]);
		sessionManager.setGlobalValue("HPThree", telCode[2]);

		String emailAddr = h006Res.getEmailAddr();
		if(emailAddr.indexOf("@") != -1) {
			String[] emailArr = emailAddr.split("@");
			sessionManager.setGlobalValue("H006_EmailAddrF", emailArr[0]);
			sessionManager.setGlobalValue("H006_EmailAddrE", emailArr[1]);
		}

		Map<String, Object> certIssueSession = new HashMap<String, Object>();
		certIssueSession.put("CN_NAME", certUtils.byteStringConvert(h892Res.getCustName(), 9) + "()");
		certIssueSession.put("SafeCardKind", h892Res.getSafeCardKind()); // 보안카드종류
		certIssueSession.put("DeptPersonName", h892Res.getCustName());
		certIssueSession.put("UserID", h892Res.getUserID2());
		certIssueSession.put("SaupjaNo", perBusNo); // 개인사업자번호
		certIssueSession.put("TelCode", phoneNo);
		certIssueSession.put("RegNo", perBusNo);
		certIssueSession.put("PerBusNo", perBusNo);
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/validateDigitalcertIssueSecurity", name = "디지털인증서 발급 보안매체 검증")
	public CrtDctValidateDigitalcertIssueSecurityResponse validateDigitalcertIssueSecurity(CrtDctValidateDigitalcertIssueSecurityRequest request) {
		// MA3CRTMNG001_502S, MA3CRTMNG001_503S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String safeCardKind = StringUtils.defaultIfBlank(sessionManager.getGlobalValue("SafeCardKind", String.class), "0");

		// SafeCardKind > 0:미발급, 1:안전카드 , 2:구OTP, 3:OTP(SmartOTP > 0:스마트OTP, M:모바일OTP)
		if("0".equals(safeCardKind)) {
			throw new PRCServiceException("PRCMNG30007", "보안카드 또는 OTP 정보가 존재하지 않습니다.");
		}

		String userId = sessionManager.getGlobalValue("UserID", String.class);
		String regNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		String infoGubun = sessionManager.getGlobalValue("InforGubun", String.class);

		// 본인확인 전문 처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setSSRAcctNum(sessionManager.getGlobalValue("accountNum", String.class)); // 계좌번호
		d006Req.setAcctPasswd(sessionManager.getGlobalValue("accountPW", String.class)); // 비밀번호
		d006Req.setTSPassword(sessionManager.getGlobalValue("TSPassword", String.class));
		d006Req.setTelCode(sessionManager.getGlobalValue("PHONE_NO", String.class));
		d006Req.setCAGubun("8"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		d006Req.setRAGubun("7"); // 인증서종류 > 7:블록체인
		d006Req.setSafeCardKind(safeCardKind);
		d006Req.setInforGubun(infoGubun);
		d006Req.setJuminSaupjaNo(regNo);
		d006Req.setCertTranCode("1");
		d006Req.setJoinLoad("000");
		d006Req.setCustGubun("1");
		d006Req.setUserID(userId);
		d006Req.setRegNo(regNo);

		log.debug("safeCardKind={}", safeCardKind);
		if ("1".equals(safeCardKind)) {
			d006Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1());
			d006Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq1());
			d006Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq1());

			d006Req.setSafeCardIndex1(sessionManager.getGlobalValue("SafeCardIndex1", String.class));
			d006Req.setSafeCardIndex2(sessionManager.getGlobalValue("SafeCardIndex2", String.class));
			d006Req.setSafeCardIndex3(sessionManager.getGlobalValue("SafeCardIndex3", String.class));

			d006Req.setSecurityIndex(sessionManager.getGlobalValue("SecurityIndex", String.class));
			d006Req.setSecurityIndex2(sessionManager.getGlobalValue("SecurityIndex2", String.class));

			d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());
			d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2());

		} else {
			d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());
			d006Req.setSecurityValue2(" ");
			d006Req.setSecurityIndex(sessionManager.getGlobalValue("SecurityIndex", String.class));
			d006Req.setSecurityIndex2(sessionManager.getGlobalValue("SecurityIndex2", String.class));

			d006Req.setSafeCardValue1(" ");
			d006Req.setSafeCardValue2(" ");
			d006Req.setSafeCardValue3(" ");

			d006Req.setSafeCardIndex1(" ");
			d006Req.setSafeCardIndex2(" ");
			d006Req.setSafeCardIndex3(" ");

		}
		log.debug("ti1ibk01H006Req={}", d006Req.toString());


        if ("3".equals(safeCardKind)) {
        	this.hostClient.sendOltpWithSecure(d006Options, d006Req, CbIbk01D00600Res.class);

        } else {
        	CbIbk01D00600Res d006Res = this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class).getResponse();

    		sessionManager.setGlobalValue("BLOCKCHAIN_TRAN_CHECK", "");
        	sessionManager.setGlobalValue("addCertTranInfoList", "");

        	if (sessionManager.isLogin()) {
        		sessionManager.setLoginValue("SafeCardKind", d006Res.getSafeCardKind());
        		sessionManager.setLoginValue("SafeCardINDEX", d006Res.getSecurityIndex());
        		sessionManager.setLoginValue("SafeCardINDEX2", d006Res.getSecurityIndex2());
        	} else {
        		sessionManager.setGlobalValue("SafeCardKind", d006Res.getSafeCardKind());
        		sessionManager.setGlobalValue("SafeCardINDEX", d006Res.getSecurityIndex());
        		sessionManager.setGlobalValue("SafeCardINDEX2", d006Res.getSecurityIndex2());
        	}
        }
    	// 보안매체 검증 끝

    	// 이메일,휴대폰번호 가지고 정보입력 페이지로 간다
		CrtDctValidateDigitalcertIssueSecurityResponse response = new CrtDctValidateDigitalcertIssueSecurityResponse();
		response.setTeleOne(sessionManager.getGlobalValue("HPOne", String.class));
		response.setTeleTwo(sessionManager.getGlobalValue("HPTwo", String.class));
		response.setTeleThree(sessionManager.getGlobalValue("HPThree", String.class));
		response.setEmailAddrF(sessionManager.getGlobalValue("H006_EmailAddrF", String.class));
		response.setEmailAddrE(sessionManager.getGlobalValue("H006_EmailAddrE", String.class));

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/getDigitalcertRegStatus", name = "디지털인증서 등록상태 조회")
	public CrtDctGetDigitalcertRegStatusResponse getDigitalcertRegStatus(CrtDctGetDigitalcertRegStatusRequest request) {
		// MA3CRTMNG001_601S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String certCode = StringUtils.nvl(request.getCertCode(), "");
		String refresh = StringUtils.nvl(request.getRefresh(), "");
		String first = StringUtils.nvl(request.getFirst(), "");

		if("".equals(first)) {
			sessionManager.setGlobalValue("Birth", sessionManager.getGlobalValue("PerBusNo", String.class).substring(0, 6));
			sessionManager.setGlobalValue("TeleOne", request.getTelNo1());
			sessionManager.setGlobalValue("TeleTwo", request.getTelNo2());
			sessionManager.setGlobalValue("TeleThree", request.getTelNo3());
			sessionManager.setGlobalValue("EmailAddr", request.getEmail1()+"@"+request.getEmail2());
		}

		Map<String, String> certCodeHashMap = null;
		if(!"".equals(refresh)) {

			if (sessionManager.getGlobalValue("CERTCODE", Map.class) != null) {
				certCodeHashMap = sessionManager.getGlobalValue("CERTCODE", Map.class);
			} else {
				certCodeHashMap = new HashMap<String, String>();
			}

			if(!"".equals(certCode)) {
				certCodeHashMap.put(certCode, "Y");
			}
			sessionManager.setGlobalValue("CERTCODE", certCodeHashMap);

		} else {
			sessionManager.removeGlobalValue("CERTCODE");

		}

		String userId = sessionManager.isLogin() ? sessionManager.getLoginValue("UserID", String.class) : sessionManager.getGlobalValue("UserID", String.class);
		String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
		String appId = sessionManager.getGlobalValue("appId", String.class);
		String verifyType = "";

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("CERTCODE_HASHMAP", certCodeHashMap);
		input.put("ISSUE_BIO", request.getIssueBio());
		input.put("ISSUE_FACEID", request.getIssueFaceid());
		input.put("ISSUE_PIN", request.getIssuePin());
		Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(userId, deviceId, appId, verifyType, input);
		log.debug("fidoResultMap={}", fidoResultMap.toString());

		CrtDctGetDigitalcertRegStatusResponse response = new CrtDctGetDigitalcertRegStatusResponse();
		response.setBirth(sessionManager.getGlobalValue("Birth", String.class));
		response.setTeleOne(sessionManager.getGlobalValue("TeleOne", String.class));
		response.setTeleTwo(sessionManager.getGlobalValue("TeleTwo", String.class));
		response.setTeleThree(sessionManager.getGlobalValue("TeleThree", String.class));
		response.setAcceptedSize((int) fidoResultMap.get("acceptedSize"));
		response.setAcceptedJsonString((String) fidoResultMap.get("acceptedJSONString"));
		response.setRegisteredSize((int) fidoResultMap.get("registeredSize"));
		response.setRegisteredJsonString((String) fidoResultMap.get("registeredJSONString"));
		response.setIssuedCertListSize((int) fidoResultMap.get("issuedCertListSize"));
		response.setIssuedCertListJsonString((String) fidoResultMap.get("issuedCertListJSONString"));
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeDigitalcertForceRevoke", name = "디지털인증서 강제폐기")
	public CrtDctAuthorizeDigitalcertForceRevokeResponse authorizeDigitalcertForceRevoke(CrtDctAuthorizeDigitalcertForceRevokeRequest request) {
		// MA3CRTMNG001_602S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String verifyType = request.getVerifyType(); // 인증서 구분
		String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		String userId = sessionManager.getGlobalValue("UserID", String.class);

		String certPolicy = "";

		if("16384".equals(verifyType)) { // PIN
			certPolicy = "44";
		}
		if("2".equals(verifyType)) { // 지문
			certPolicy = "34";
		}

		// 디지털인증서 폐기
		Map<String, Object> resultRACertDeleteMap = fidoHttpComponent.RACertDelete(certPolicy, perBusNo);

		log.debug("resultRACertDeleteMap={}", resultRACertDeleteMap.toString());

		// 1단말 정책 적용 여부
		String IS_DISTCERT_ONEDEVICE = PropertiesUtils.getString("IS_DISTCERT_ONEDEVICE", "");
		log.debug("IS_DISTCERT_ONEDEVICE={}", IS_DISTCERT_ONEDEVICE);
		if("Y".equals(IS_DISTCERT_ONEDEVICE) && !"".equals(verifyType)) {
			try {
				String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
				String appId = sessionManager.getGlobalValue("appId", String.class);

				Map<String, Object> fidoRegistMap = fidoHttpComponent.checkRegisteredStatus2(userId, deviceId, appId, "", null);

				JSONArray registeredJsonArray = (JSONArray) fidoRegistMap.getOrDefault("registeredJsonArray", new JSONArray());

				for(int i=0; i<registeredJsonArray.length(); i++) {
					JSONObject obj = (JSONObject) registeredJsonArray.get(i);

					String tmpVerifyType = obj.optString("verificationType", "");
					String serialNumber = obj.optString("serialNumber", "");
					String tmpDeviceId = obj.optString("deviceId", "");

					if("".equals(serialNumber)) {
						continue;
					}

					// 다른 deviceId는 등록해지
					if(!tmpDeviceId.equals(deviceId) && tmpVerifyType.equals(verifyType)){

						fidoCertificateInfoDao.updateExpiredCertificateIssued(serialNumber);

						Random rand0 = new Random();
						String certify_no = "000"+((rand0.nextInt()%5000)+5000);
						certify_no = certify_no.substring(certify_no.length()-4, certify_no.length());

						String sendDateTime = DateUtils.getCurrentDate("yyyyMMddHHmmss");
						FidoBackupCertListDataParameter parameter = new FidoBackupCertListDataParameter();
						parameter.setHistId(sendDateTime+""+certify_no);
						parameter.setAuthDt(sendDateTime);
						parameter.setUserId(userId);
						parameter.setTeleType("N");
						parameter.setHpNum("C");
						parameter.setResltCd(verifyType);

						fidoCertificateInfoDao.insertFidoBackupCertListData(parameter);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}

		// 응답 반환
		CrtDctAuthorizeDigitalcertForceRevokeResponse response = new CrtDctAuthorizeDigitalcertForceRevokeResponse();
		response.setResCode((String)resultRACertDeleteMap.get("resCode"));
		response.setResMsg((String)resultRACertDeleteMap.get("resMsg"));
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeDigitalcertIssue", name = "디지털인증서 발급")
	public CrtDctAuthorizeDigitalcertIssueResponse authorizeDigitalcertIssue(CrtDctAuthorizeDigitalcertIssueRequest request) {
		// MA3CRTMNG001_603S

		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String verifyType = request.getVerifyType();
		String issueType = request.getIssueType();

		String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
		String appId = sessionManager.getGlobalValue("appId", String.class);
		String userId = sessionManager.getGlobalValue("UserID", String.class);
		String teleOne = sessionManager.getGlobalValue("TeleOne", String.class);
		String teleTwo = sessionManager.getGlobalValue("TeleTwo", String.class);
		String teleThree = sessionManager.getGlobalValue("TeleThree", String.class);
		String emailAddr = sessionManager.getGlobalValue("EmailAddr", String.class);

		Map<String,Object> certIssueSession = certUtils.getCertSession("certIssueSession");
		certIssueSession.put("PHONE", teleOne+teleTwo+teleThree);
		certIssueSession.put("EMAIL", emailAddr);

		String certPolicy = "";
		if("16384".equals(verifyType)) { // PIN
			certPolicy = "44";
		}
		if("2".equals(verifyType)) { // 지문
			certPolicy = "34";
		}

		// 인증서 발급
		Map<String, Object> resultRAMap = fidoHttpComponent.getRaCert(certPolicy);

		log.debug("resultRAMap={}", resultRAMap.toString());

		String refNum = (String) resultRAMap.get("refNum");
		String appCode = (String) resultRAMap.get("appCode");

		// FIDO 등록
		Map<String, Object> returnFIDOMap = fidoHttpComponent.requestIssueCert(refNum, appCode, deviceId, appId, verifyType, issueType);
		sessionManager.setGlobalValue("svcTrId", returnFIDOMap.get("svcTrId")); // 다음스탭 거래결과 확인용

		log.debug("returnFIDOMap={}", returnFIDOMap.toString());

		// 응답 반환
		CrtDctAuthorizeDigitalcertIssueResponse response = new CrtDctAuthorizeDigitalcertIssueResponse();
		response.setTrId((String) returnFIDOMap.get("trId"));
		response.setUserId(userId);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/confirmDigitalcertIssue", name = "디지털인증서 발급 완료")
	public CrtDctConfirmDigitalcertIssueResponse confirmDigitalcertIssue(CrtDctConfirmDigitalcertIssueRequest request) {
		// MA3CRTMNG001_604S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		log.debug("FIDO 거래결과확인 통신 시작");
		// fido 거래결과 확인
		Map<String, Object> returnFIDOMap = fidoHttpComponent.trResultConfirm(sessionManager.getGlobalValue("svcTrId", String.class));
		CrtDctConfirmDigitalcertIssueResponse response = new CrtDctConfirmDigitalcertIssueResponse();
		response.setResultCode((String) returnFIDOMap.get("resultCode")); // 화면에서 오류코드 제어할것임
		response.setResultMsg((String) returnFIDOMap.get("resultMsg"));
		response.setResultData((String) returnFIDOMap.get("resultData"));
		log.debug("FIDO 거래결과확인 통신 끝");

		log.debug("FIDO 발급결과 디비 적재 시작");
		// 발급결과 DB 적재
		String pinDeviceId = "";
		String osVersion = PRCSharedUtils.getOsVersion();
		String ipinsideHdd = PRCSharedUtils.getIpinsideHdd();
		if(!"".equals(osVersion) && osVersion.indexOf(".") > -1) {
			osVersion = osVersion.substring(0, osVersion.indexOf("."));
		}
		try {
			if(PRCSharedUtils.isAndroid() && Integer.parseInt(osVersion) > 5) {
				// 안드로이드이면서 OSVersion이 6.XX이상이면서 MAC && IMEI 값으로 PIN TABLE 조회할 경우
				// 15자리 초과하는 경우 Android ID 아니면 IMEI
				Map<String, String> deviceData = CommonBizUtils.getDataMap(ipinsideHdd);
				if(StringUtils.isNotEmpty(deviceData.get(CommonBizConstants.IPINSIDE_ANDROID_ID))) {
					//Android ID인 경우
					pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_ANDROID_ID);
					response.setUpdatedAndroidIdYn("Y");
				} else if(StringUtils.isNotEmpty(deviceData.get(CommonBizConstants.IPINSIDE_IMEI))) {
					//IMEI인 경우
					pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_IMEI);
					response.setUpdatedAndroidIdYn("N");
				}
			} else {
				pinDeviceId = ipinsideComponent.simpleDataDecode();
			}
		} catch (Exception e) {
			pinDeviceId = ipinsideComponent.simpleDataDecode();
		}

		int cnt = pinUsrMgtDao.selectCountPinUsrMgtByDeviceId(pinDeviceId);

		PinUsrMgtParameter parameter = new PinUsrMgtParameter();
		parameter.setDeviceId(pinDeviceId); // 사용자 기기 id
		parameter.setDeviceType(""); // 사용자 기기 type
		parameter.setJoinFlag("Y"); // 가입유무
		parameter.setDeviceIdUpdate(""); // 사용자 기기 id update여부
		parameter.setPopupStatus(""); // 가입 팝업 유무
		parameter.setUserBankingId(sessionManager.getGlobalValue("UserID", String.class));
		parameter.setNickName(sessionManager.getGlobalValue("DeptPersonName", String.class));

		if (cnt > 0) {
			pinUsrMgtDao.updatePinUsrMgt(parameter);
		} else {
			pinUsrMgtDao.insertPinUsrMgt(parameter);
		}
		log.debug("FIDO 발급결과 디비 적재 끝");

		// 결과 SMS 발송
		try {
			String verifyType = request.getVerifyType();
			String SMSMsg = codeManager.getCodeItem("MNG", "MNG009005");
			String custName = sessionManager.getGlobalValue("DeptPersonName", String.class);
			String verifyTypeName = "";
			if ("16384".equals(verifyType)) {
				verifyTypeName = codeManager.getCodeItem("MNG", "MNG009001");

			} else if("2".equals(verifyType) || "2048".equals(verifyType) ) {
				verifyTypeName = codeManager.getCodeItem("MNG", "MNG009002");

			} else if("4096".equals(verifyType)) {
				verifyTypeName = codeManager.getCodeItem("MNG", "MNG009003");

			} else if("256".equals(verifyType)) {
				verifyTypeName = codeManager.getCodeItem("MNG", "MNG009004");

			}

			custName = custName.trim().length()>4 ? custName.trim().substring(0,4) : custName.trim(); //이름4자리이상 처리.
			String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
			SMSMsg = SMSMsg.replaceAll("~~CustName~~", smsCustName);
			SMSMsg = SMSMsg.replaceAll("~~verifyTypeName~~", verifyTypeName);

			log.debug("SMSMsg={}", SMSMsg);

			OltpRequestOptions s002Options = this.hostClient.getOltpRequestOptions("CB_SMS01_S002");
			// 공통부 세팅
			s002Options.setImsTranCd("TI1SMS01");
			s002Options.setInClassCd("S002");
			s002Options.setSvcCd("911");
			s002Options.setJobTp("GY");
			//개별부 세팅
			CbSms01S00200Req s002Req = new CbSms01S00200Req();
			s002Req.setUserID(sessionManager.getGlobalValue("UserID", String.class));
			s002Req.setCustomGB("IB9");
			s002Req.setTransGB("009"); // 거래구분(TransGB)>>001 : 공인인증서(재발급), 002:고객정보관리변경, 003:고객휴대폰번호변경, 004:고객이메일변경, 005:이체한도변경(감액), 006:출금계좌등록, 007:출금계좌해지, 008:블록체인, 009:디지털인증서
			s002Req.setJuminNumgb(""); // 개인,개인사업자 "", 법인 "2"
			s002Req.setPerBusNo(sessionManager.getGlobalValue("PerBusNo", String.class));
			s002Req.setJoribGB("Y");
			s002Req.setJoribMsg(StringUtils.toFullChar(SMSMsg));
			s002Req.setTSPassword("99999999");
			this.hostClient.sendOltp(s002Options, s002Req, CbSms01S00200Res.class);

		} catch (Exception e) {
			log.error("sms발송실패");
			log.error(e.getMessage());
		}

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

	/************************************************************************************************************************************************
	 * 디지털인증서 폐기
	 * validateDigitalcertRevokeUser : 디지털인증서 해지 본인확인
	 * validateDigitalcertRevokeSecurity : 디지털인증서 해지 보안매체 검증
	 * authorizeDigitalcertRevoke : 디지털인증서 해지
	 * confirmDigitalcertRevoke : 디지털인증서 해지 완료
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateDigitalcertRevokeUser", name = "디지털인증서 해지 본인확인")
	public CrtDctValidateDigitalcertRevokeUserResponse validateDigitalcertRevokeUser(CrtDctValidateDigitalcertRevokeUserRequest request) {
		// MA3CRTMNG002_201S, MA3CRTMNG002_202S, MA3CRTMNG002_301S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		/**
		 * 1. 입력값 보관
		 */
		sessionManager.setGlobalValue("appId", request.getAppId());
		sessionManager.setGlobalValue("deviceId", request.getDeviceId());
		sessionManager.setGlobalValue("os", request.getOs());
		sessionManager.setGlobalValue("spass", request.getSpass());
		sessionManager.setGlobalValue("fingerprintDevice", request.getFingerprintDevice());
		sessionManager.setGlobalValue("fingerprintEnrolled", request.getFingerprintEnrolled());
		sessionManager.setGlobalValue("faceDevice", request.getFaceDevice());
		sessionManager.setGlobalValue("faceEnrolled", request.getFaceEnrolled());

		/**
		 * 2. 본인확인
		 */
		String ssn1 = request.getCustJumin1();
		String ssn2 = request.getCustJumin2();
		String perBusNo = ssn1 + ssn2;

		String accountNum = request.getAccountNum().replaceAll("-", "");
		String accountPw = request.getAccountBb();

		String userId = sessionManager.isLogin()?sessionManager.getGlobalValue("UserID", String.class):"FIRST999";
		// 아이디찾기 전문처리
		OltpRequestOptions h892Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
		h892Options.setImsTranCd("TI1IBK01");
		h892Options.setInClassCd("H892");
		h892Options.setSvcCd("892");
		//개별부 세팅
		CbIbk01H89200Req h892Req = new CbIbk01H89200Req();
		h892Req.setActType("2"); // 처리구분 ( 예비처리 ) 1: 고객정보조회( 본처리 ) 2: 계좌검증 3: 계좌미검증 4: 법인 ID 검증 5: 신용카드조회회원
		h892Req.setUserID(userId); // 아이디
		h892Req.setE2ERegNum(perBusNo);
		h892Req.setAcctNum(accountNum);
		h892Req.setAcctPassword(accountPw);
		CbIbk01H89200Res h892Res = this.hostClient.sendOltp(h892Options, h892Req, CbIbk01H89200Res.class).getResponse();

		String userId2 = h892Res.getUserID2();
		String deptPersonName = certUtils.byteStringConvert(h892Res.getCustName(), 9)+"()";

		// 본인인증결과 세션에 보관
		sessionManager.setGlobalValue("UserID", userId2);
		sessionManager.setGlobalValue("DeptPersonName", h892Res.getCustName());
		sessionManager.setGlobalValue("PerBusNo", perBusNo);
		sessionManager.setGlobalValue("SafeCardKind", h892Res.getSafeCardKind()); // 보안카드종류
		sessionManager.setGlobalValue("SaupjaNo", perBusNo); // 개인사업자번호

		Map<String,Object> certIssueSession = new HashMap<String, Object>();
		certIssueSession.put("UserID", userId2);
        certIssueSession.put("DeptPersonName", h892Res.getCustName());
        certIssueSession.put("TelCode", ""); // 이거 안쓰는디
        certIssueSession.put("RegNo", perBusNo);
        certIssueSession.put("SafeCardKind", h892Res.getSafeCardKind()); // 보안카드종류
        certIssueSession.put("SaupjaNo", perBusNo); // 개인사업자번호
        certIssueSession.put("CN_NAME", deptPersonName);

        sessionManager.setGlobalValue("certIssueSession", certIssueSession);

        /**
		 * 3. 보안매체용 세션 저장
		 */
        // 본인확인 전문 처리
        OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
 		// 공통부 세팅
        h006Options.setImsTranCd("TI1IBK01");
        h006Options.setInClassCd("H006");
        h006Options.setSvcCd("006");
 		//개별부 세팅
 		CbIbk01H00600Req h006Req = new CbIbk01H00600Req();
 		h006Req.setUserID(h892Res.getUserID2()); // CB_IBK01_H892 응답 사용
 		h006Req.setRegNo(perBusNo);
 		h006Req.setJuminSaupjaNo(perBusNo);
 		h006Req.setCertTranCode("1");
 		h006Req.setChuryGubun("N");
 		h006Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
 		h006Req.setCustGubun("1");
 		CbIbk01H00600Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00600Res.class).getResponse();

 		if(sessionManager.isLogin()) {
			sessionManager.setLoginValue("TelCode", h006Res.getTelCode());
			sessionManager.setLoginValue("SafeCardState", "1");
			sessionManager.setLoginValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
			sessionManager.setLoginValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
			sessionManager.setLoginValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
			sessionManager.setLoginValue("SafeCardIndex1", h006Res.getSafeCardIndex1());
			sessionManager.setLoginValue("SafeCardIndex2", h006Res.getSafeCardIndex2());
			sessionManager.setLoginValue("SafeCardIndex3", h006Res.getSafeCardIndex3());
			sessionManager.setLoginValue("TSPassword", h006Res.getTSPassword());
			sessionManager.setLoginValue("InforGubun", h006Res.getInforGubun());
		}
		sessionManager.setGlobalValue("UserID", h006Res.getUserID());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());
		sessionManager.setGlobalValue("TelCode", h006Res.getTelCode());
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("TransPWUseYN", h006Res.getYOISYN());
		sessionManager.setGlobalValue("SafeCardKind", h006Res.getSafeCardKind());
		sessionManager.setGlobalValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardIndex1", h006Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardIndex2", h006Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardIndex3", h006Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", h006Res.getSecurityIndex());
		sessionManager.setGlobalValue("SafeCardINDEX2", h006Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SecurityIndex", h006Res.getSecurityIndex());
		sessionManager.setGlobalValue("SecurityIndex2", h006Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SmartOTP", h006Res.getSmartOTP());
		sessionManager.setGlobalValue("TSPassword", h006Res.getTSPassword());
		sessionManager.setGlobalValue("InforGubun", h006Res.getInforGubun());
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		sessionManager.setGlobalValue("TeleOne", telCode[0]);
		sessionManager.setGlobalValue("TeleTwo", telCode[1]);
		sessionManager.setGlobalValue("TeleThree", telCode[2]);
//		sessionManager.setGlobalValue("phoneNo", telCode[3]);

		// 해지 본인확인에서는 불필요
//		String emailAddr = h006Res.getEmailAddr();
//		if(emailAddr.indexOf("@") != -1) {
//			String[] emailArr = emailAddr.split("@");
//			sessionManager.setGlobalValue("H006_EmailAddrF", emailArr[0]);
//			sessionManager.setGlobalValue("H006_EmailAddrE", emailArr[1]);
//		}

		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());

        /**
		 * 4. 보유 디지털 인증서 조회
		 */
        String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
		String appId = sessionManager.getGlobalValue("appId", String.class);
		String verifyType = "";
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("ISSUE_FACEID", request.getIssueFaceid());
		input.put("ISSUE_PIN", request.getIssuePin());
		input.put("ISSUE_BIO", request.getIssueBio());
        Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(userId2, deviceId, appId, verifyType, input);

        /**
         * 5. 응답 반환
         */
        CrtDctValidateDigitalcertRevokeUserResponse response = new CrtDctValidateDigitalcertRevokeUserResponse();
        response.setIssuedCertListJsonString((String) fidoResultMap.get("issuedCertListJSONString"));
        response.setIssuedCertListSize((int) fidoResultMap.get("issuedCertListSize"));

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/validateDigitalcertRevokeSecurity", name = "디지털인증서 해지 보안매체 검증")
	public CrtDctValidateDigitalcertRevokeSecurityResponse validateDigitalcertRevokeSecurity(CrtDctValidateDigitalcertRevokeSecurityRequest request) {
		// MA3CRTMNG002_302S, MA3CRTMNG002_401S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String safeCardKind = StringUtils.nvl(sessionManager.getGlobalValue("SafeCardKind", String.class), "0");

		// SafeCardKind > 0:미발급, 1:안전카드 , 2:구OTP, 3:OTP(SmartOTP > 0:스마트OTP, M:모바일OTP)
		if("0".equals(safeCardKind)) {
			throw new PRCServiceException("PRCMNG30007", "보안카드 또는 OTP 정보가 존재하지 않습니다.");
		}

		String userId = sessionManager.getGlobalValue("UserID", String.class);
		String regNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		String infoGubun = sessionManager.getGlobalValue("InforGubun", String.class);
		String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
		String appId = sessionManager.getGlobalValue("appId", String.class);
		String verifyType = "";

		// 본인확인 전문 처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setSSRAcctNum(sessionManager.getGlobalValue("accountNum", String.class)); // 계좌번호
		d006Req.setAcctPasswd(sessionManager.getGlobalValue("accountPW", String.class)); // 비밀번호
		d006Req.setTSPassword(sessionManager.getGlobalValue("TSPassword", String.class));
		d006Req.setTelCode(sessionManager.getGlobalValue("PHONE_NO", String.class));
		d006Req.setCAGubun("8"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		d006Req.setRAGubun("7"); // 인증서종류 > 7:블록체인
		d006Req.setSafeCardKind(safeCardKind);
		d006Req.setInforGubun(infoGubun);
		d006Req.setJuminSaupjaNo(regNo);
		d006Req.setCertTranCode("1");
		d006Req.setJoinLoad("000");
		d006Req.setCustGubun("1");
		d006Req.setUserID(userId);
		d006Req.setRegNo(regNo);

		log.debug("safeCardKind={}", safeCardKind);
		if ("1".equals(safeCardKind)) {
			d006Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1());
			d006Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2());
			d006Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3());

			d006Req.setSafeCardIndex1(sessionManager.getGlobalValue("SafeCardIndex1", String.class));
			d006Req.setSafeCardIndex2(sessionManager.getGlobalValue("SafeCardIndex2", String.class));
			d006Req.setSafeCardIndex3(sessionManager.getGlobalValue("SafeCardIndex3", String.class));

			d006Req.setSecurityIndex(sessionManager.getGlobalValue("SecurityIndex", String.class));
			d006Req.setSecurityIndex2(sessionManager.getGlobalValue("SecurityIndex2", String.class));

			d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());
			d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2());

		} else {
			d006Req.setSafeCardValue1(" ");
			d006Req.setSafeCardValue2(" ");
			d006Req.setSafeCardValue3(" ");

			d006Req.setSafeCardIndex1(" ");
			d006Req.setSafeCardIndex2(" ");
			d006Req.setSafeCardIndex3(" ");

			d006Req.setSecurityIndex(sessionManager.getGlobalValue("SecurityIndex", String.class));
			d006Req.setSecurityIndex2(sessionManager.getGlobalValue("SecurityIndex2", String.class));

			d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());
			d006Req.setSecurityValue2(" ");
		}

		log.debug("ti1ibk01H006Req={}", d006Req.toString());

		OltpResponse<CbIbk01D00600Res> hostResponse = null;
        if ("3".equals(safeCardKind)) {
        	hostResponse = this.hostClient.sendOltpWithSecure(d006Options, d006Req, CbIbk01D00600Res.class);

        } else {
        	hostResponse = this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

        	CbIbk01D00600Res d006Res = hostResponse.getResponse();

    		sessionManager.setGlobalValue("BLOCKCHAIN_TRAN_CHECK", "");
        	sessionManager.setGlobalValue("addCertTranInfoList", "");

        	if (sessionManager.isLogin()) {
        		sessionManager.setLoginValue("SafeCardKind", d006Res.getSafeCardKind());
        		sessionManager.setLoginValue("SafeCardINDEX", d006Res.getSecurityIndex());
        		sessionManager.setLoginValue("SafeCardINDEX2", d006Res.getSecurityIndex2());
        	} else {
        		sessionManager.setGlobalValue("SafeCardKind", d006Res.getSafeCardKind());
        		sessionManager.setGlobalValue("SafeCardINDEX", d006Res.getSecurityIndex());
        		sessionManager.setGlobalValue("SafeCardINDEX2", d006Res.getSecurityIndex2());
        	}
        }

        // 해지할 인증서 조회
        Map<String, Object> input = new HashMap<String, Object>();
		input.put("ISSUE_FACEID", request.getIssueFaceid());
		input.put("ISSUE_PIN", request.getIssuePin());
		input.put("ISSUE_BIO", request.getIssueBio());
        Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(userId, deviceId, appId, verifyType, input);

        // 응답 반환
        CrtDctValidateDigitalcertRevokeSecurityResponse response = new CrtDctValidateDigitalcertRevokeSecurityResponse();
        response.setAcceptedSize((int) fidoResultMap.get("acceptedSize"));
        response.setAcceptedJsonString((String) fidoResultMap.get("acceptedJSONString"));
        response.setIssuedCertListSize((int) fidoResultMap.get("issuedCertListSize"));
        response.setIssuedCertListJsonString((String) fidoResultMap.get("issuedCertListJSONString"));
        response.setRegisteredSize((int) fidoResultMap.get("registeredSize"));
        response.setRegisteredJsonString((String) fidoResultMap.get("registeredJSONString"));

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeDigitalcertRevoke", name = "디지털인증서 해지")
	public CrtDctAuthorizeDigitalcertRevokeResponse authorizeDigitalcertRevoke(CrtDctAuthorizeDigitalcertRevokeRequest request) {
		// MA3CRTMNG002_402S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String verifyType = request.getVerifyType();
		String issueType = request.getIssueType();

		String userId = sessionManager.getGlobalValue("UserID", String.class);
		String deviceId = sessionManager.getGlobalValue("deviceId", String.class);
		String appId = sessionManager.getGlobalValue("appId", String.class);

		// FIDO 인증서 삭제
		Map<String, Object> resultRequestRevokeCertMap = fidoHttpComponent.requestRevokeCert(deviceId, appId, verifyType, issueType);

		// 응답 반환
		CrtDctAuthorizeDigitalcertRevokeResponse response = new CrtDctAuthorizeDigitalcertRevokeResponse();
		if("000".equals(resultRequestRevokeCertMap.getOrDefault("resultCode", ""))) {
			response.setTrId((String) resultRequestRevokeCertMap.get("trId"));
		}
		response.setResultCode((String) resultRequestRevokeCertMap.get("resultCode"));
		response.setResultMsg((String) resultRequestRevokeCertMap.get("resultMsg"));
		response.setUserId(userId);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/confirmDigitalcertRevoke", name = "디지털인증서 해지 완료")
	public CrtDctConfirmDigitalcertRevokeResponse confirmDigitalcertRevoke(CrtDctConfirmDigitalcertRevokeRequest request) {
		// MA3CRTMNG002_403S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String verifyType = request.getVerifyType();

		String userId = sessionManager.getGlobalValue("UserID", String.class);
		String perBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
		String custName = sessionManager.getGlobalValue("DeptPersonName", String.class);

		String certPolicy = "";
		String verifyTypeName = "";

		if ("16384".equals(verifyType)) {
			certPolicy = "44"; // PIN

		} else if("2".equals(verifyType)) {
			certPolicy = "34"; // 지문, faceid

		} else if("4096".equals(verifyType)) {
			certPolicy = "24"; // 홍채

		}

		// 인증서 폐기
		fidoHttpComponent.RACertDelete(certPolicy, perBusNo);

		// 인증서 발급 결과 전문 송신
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID(userId);
		d006Req.setRegNo(perBusNo);
		d006Req.setJuminSaupjaNo(perBusNo);
		d006Req.setTelCode(sessionManager.getGlobalValue("phoneNo", String.class));// 이용자전화번호
		d006Req.setTSPassword("99999999"); // 이용자비밀번호
		d006Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		d006Req.setRAGubun("1"); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		d006Req.setIssueBank("023"); // 인증서발급은행(은행코드)
		d006Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

		// 결과 SMS 발송
		try {
			String SMSMsg = codeManager.getCodeItem("MNG", "MNG009006");

			custName = custName.trim().length()>4 ? custName.trim().substring(0,4) : custName.trim(); //이름4자리이상 처리.
			String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");

			SMSMsg = SMSMsg.replaceAll("~~CustName~~", smsCustName);
			SMSMsg = SMSMsg.replaceAll("~~verifyTypeName~~", verifyTypeName);
			SMSMsg = SMSMsg.length()>80 ? SMSMsg.substring(0,80) : SMSMsg; //80자 처리.

			log.debug("SMSMsg={}", SMSMsg);
			OltpRequestOptions s002Options = this.hostClient.getOltpRequestOptions("CB_SMS01_S002");
			// 공통부 세팅
			s002Options.setImsTranCd("TI1SMS01");
			s002Options.setInClassCd("S002");
			s002Options.setSvcCd("911");
			s002Options.setJobTp("GY");
			//개별부 세팅
			CbSms01S00200Req s002Req = new CbSms01S00200Req();
			s002Req.setUserID(sessionManager.getGlobalValue("UserID", String.class));
			s002Req.setCustomGB("IB9");
			s002Req.setTransGB("009"); // 거래구분(TransGB)>>001 : 공인인증서(재발급), 002:고객정보관리변경, 003:고객휴대폰번호변경, 004:고객이메일변경, 005:이체한도변경(감액), 006:출금계좌등록, 007:출금계좌해지, 008:블록체인, 009:디지털인증서
			s002Req.setJuminNumgb(""); // 개인,개인사업자 "", 법인 "2"
			s002Req.setPerBusNo(sessionManager.getGlobalValue("PerBusNo", String.class));
			s002Req.setJoribGB("Y");
			s002Req.setJoribMsg(StringUtils.toFullChar(SMSMsg));
			s002Req.setTSPassword("99999999");
			this.hostClient.sendOltp(s002Options, s002Req, CbSms01S00200Res.class);
		} catch (Exception e) {
			log.error("sms발송실패");
			log.error(e.getMessage());
		}

		return new CrtDctConfirmDigitalcertRevokeResponse();
	}
}