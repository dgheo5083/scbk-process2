package com.scbank.process.api.svc.common.service.certification;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D98000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D98000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89201Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89201Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.dao.LdapInfoDao;
import com.scbank.process.api.svc.common.dao.Ma3CertUserMgtDao;
import com.scbank.process.api.svc.common.dao.PinUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.CertUserMgtParameter;
import com.scbank.process.api.svc.common.dao.dto.DeviceCountResult;
import com.scbank.process.api.svc.common.dao.dto.LdapInfoResult;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtByDeviceResult;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFidoCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFidoCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertRenewRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertRenewResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeFincertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertDelRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertDelResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertRegRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertRegResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctAuthorizeOtherFincertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFidoResultRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFidoResultResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFincertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFincertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFincertRenewRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctConfirmFincertRenewResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoResultRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoResultResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoTridRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFidoTridResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFincertRefNumRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFincertRefNumResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFincertStatusRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetFincertStatusResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetPcFixRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctGetPcFixResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctListFidoAvailableRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctListFidoAvailableResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFidoCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFidoCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRenewRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRenewResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRevokeAcctRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRevokeAcctResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRevokeUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateFincertRevokeUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertDelUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertDelUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertRegCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertRegCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertRegUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.financial.CrtFctValidateOtherFincertRegUserResponse;
import com.scbank.process.api.svc.shared.components.cert.CertConfig;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.CertificateHelper;
import com.scbank.process.api.svc.shared.components.cert.FinCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.KftcCertComponent;
import com.scbank.process.api.svc.shared.components.cert.dto.OppraCertInfo;
import com.scbank.process.api.svc.shared.components.fido.FidoHttpComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.EmailUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name="공통-인증센터-금융인증서", url="/certification/financial")
public class CertificationFinancialService {

	// 세션
	private final ISessionContextManager sessionManager;

	// 전문
	private final HostClient hostClient;

	// 인증센터 공통
	private final CertUtils certUtils;
	private final KftcCertComponent kftcCertComponent;
	private final FidoHttpComponent fidoHttpComponent;
	private final FinCertVerifyComponent finCertVerifyComponent;
	private final CertificationSharedComponent certificationSharedComponent;

	private final SmsComponent smsComponent;

	// DBIO
	private final LdapInfoDao ldapInfoDao;
	private final Ma3CertUserMgtDao ma3CertUserMgtDao;
	private final PinUsrMgtDao pinUsrMgtDao;

	// IPINSIDE
	private final IpinsideComponent ipinsideComponent;

	/************************************************************************************************************************************************
	 * 금융인증서 발급/재발급
	 * validateFincertIssue : 금융인증서 발급 본인확인
	 * getFincertRefNum : 금융인증서 참조번호 발급
	 * authorizeOtherFincertRevoke : 타행금융인증서 강제폐기
	 * authorizeFincertIssue : 금융인증서 발급
	 * confirmFincertIssue : 금융인증서 발급완료 통지
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateFincertIssue", name = "금융인증서 발급 본인확인")
	public CrtFctValidateFincertIssueResponse validateFincertIssue(CrtFctValidateFincertIssueRequest request) {
		// MA3CRTFCA001_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());
		CrtFctValidateFincertIssueResponse response = new CrtFctValidateFincertIssueResponse();

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("certIssueSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인 된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 본인확인 전문 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		// 공통부 세팅
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");

		//개별부 세팅
		CbIbk01H00601Req h006Req = new CbIbk01H00601Req();
		h006Req.setAcctPasswd(request.getAcctBb()); //계좌비밀번호
		h006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증, 9:금융인증서)
		h006Req.setCertTranCode("1"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		h006Req.setCustJumin1(request.getCustJumin1()); //실명번호1
		h006Req.setCustJumin2(request.getCustJumin2()); //실명번호2
		h006Req.setSSRAcctNum(request.getAcctNum()); //계좌번호
		h006Req.setSimplifyGB("1"); //인증서간소화 체크
		CbIbk01H00601Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00601Res.class).getResponse();

		// 개인정보노출자 판단
		Map<String, String> h504Res = certificationSharedComponent.getYoLRSOTGB(h006Res.getRegNo());
		String YOLRSOTGB = h504Res.get("YOLRSOTGB");
		if("1".equals(YOLRSOTGB)) {
			sessionManager.removeGlobalValue("certIssueSession");
			sessionManager.removeGlobalValue("PerBusNo");
			sessionManager.removeGlobalValue("CustName");
			response.setYoLRSOTGB(YOLRSOTGB);
			return response;
		}

		// 전화번호
		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		String HPOne = telCode[0];
		String HPTwo = telCode[1];
		String HPThree = telCode[2];
		String phoneNo = telCode[3];
		sessionManager.setGlobalValue("HPOne", HPOne);
		sessionManager.setGlobalValue("HPTwo", HPTwo);
		sessionManager.setGlobalValue("HPThree", HPThree);
		sessionManager.setGlobalValue("phoneNo", phoneNo);

		// 이메일
		String email = h006Res.getEmailAddr();
		String email1 = "";
		String email2 = "";
		if(email.indexOf("@")>0) {
			email1 = email.split("@")[0];
			email2 = email.split("@")[1];
		}

		// 사용자 확인
		String personOrCompanyCode = h006Res.getPersonOrCompanyCode();
		String userId = h006Res.getUserID();
		String YOIDGB = h006Res.getYOIDGB();

		if("3".equals(personOrCompanyCode)) {
			throw new PRCServiceException("PRCFCA01002" ,"사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}
		if(!"1".equals(personOrCompanyCode) && !"2".equals(personOrCompanyCode)) {
			throw new PRCServiceException("PRCFCA01001", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}
		if("13".equals(YOIDGB) || "14".equals(YOIDGB)){
			throw new PRCServiceException("PRCFCA01003" ,"사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		// RA사용자 조회
		String oppUserId = CertConfig.RegCodeValue + userId;
		String deptPersonName = certUtils.byteStringConvert(h006Res.getDeptPersonName(), 9) + "()";

		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		//본인 인증한 데이터 세션에 담는다
		Map<String,Object> certIssueSession = new HashMap<String, Object>();
		certIssueSession.put("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		certIssueSession.put("DeptPersonName", h006Res.getDeptPersonName());
		certIssueSession.put("SecurityIndex2", h006Res.getSecurityIndex2());
		certIssueSession.put("SafeCardIndex1", h006Res.getSafeCardIndex1());
		certIssueSession.put("SafeCardIndex2", h006Res.getSafeCardIndex2());
		certIssueSession.put("SafeCardIndex3", h006Res.getSafeCardIndex3());
		certIssueSession.put("SecurityIndex", h006Res.getSecurityIndex());
		certIssueSession.put("SafeCardKind", h006Res.getSafeCardKind());
		certIssueSession.put("AuthSVC4CERT", h006Res.getAuthSVC4CERT());
		certIssueSession.put("TSPassword", h006Res.getTSPassword());
		certIssueSession.put("AcctPasswd", h006Req.getAcctPasswd());
		certIssueSession.put("InforGubun", h006Req.getInforGubun());
		certIssueSession.put("EmailAddr", h006Res.getEmailAddr());
		certIssueSession.put("SmartOTP", h006Res.getSmartOTP());
		certIssueSession.put("SaupjaNo", h006Res.getSaupjaNo());
		certIssueSession.put("EngName", h006Req.getEngName());
		certIssueSession.put("ZipCode", h006Res.getZipCode());
		certIssueSession.put("Address", h006Res.getAddress());
		certIssueSession.put("UserID", h006Res.getUserID());
		certIssueSession.put("RegNo", h006Res.getRegNo());
		certIssueSession.put("oppraResult", oppraResult); //RA정보
		certIssueSession.put("YOAGREEGB", h006Res.getYOAGREEGB());

		sessionManager.setGlobalValue("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());
		sessionManager.setGlobalValue("UserID", h006Res.getUserID());
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		// 응답객체 생성
		response.setYoAGREEGB(h006Res.getYOAGREEGB()); // 온라인 발급 사전동의여부
		response.setYoCFJMGB(h006Res.getYOCFJMGB()); // 조합번호여부 3=차단
		response.setAcctNum(h006Req.getSSRAcctNum()); // 계좌번호
		response.setYoLRSOTGB(YOLRSOTGB); // 개인정보 노출 여부 1=차단
		response.setHpOne(HPOne);
		response.setHpTwo(HPTwo);
		response.setHpThree(HPThree);
		response.setPhoneNo(phoneNo);
		response.setEmail1(email1);
		response.setEmail2(email2);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/getFincertRefNum", name = "금융인증서 참조번호 발급")
	public CrtFctGetFincertRefNumResponse getFincertRefNum(CrtFctGetFincertRefNumRequest request) {
		// MA3CRTFCA001_301S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		Map<String,Object> certIssueSession = certUtils.getCertSession("certIssueSession");

		String email = request.getEmail1()+"@"+request.getEmail2();
		if("@".equals(email)){
			email = (String) certIssueSession.get("EmailAddr");
		}
		String authHpNum1 = request.getTel1();
		String authHpNum2 = request.getTel2();
		String authHpNum3 = request.getTel3();
		String phoneNum = authHpNum1 +""+ authHpNum2 +""+ authHpNum3;

		sessionManager.setGlobalValue("HPOne", authHpNum1);
		sessionManager.setGlobalValue("HPTwo", authHpNum2);
		sessionManager.setGlobalValue("HPThree", authHpNum3);
		sessionManager.setGlobalValue("TeleOne", authHpNum1);
		sessionManager.setGlobalValue("TeleTwo", authHpNum2);
		sessionManager.setGlobalValue("TeleThree", authHpNum3);
		sessionManager.setGlobalValue("phoneNo", phoneNum);

		// 발급인가 예비거래
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID((String) certIssueSession.get("UserID")); //사용자아이디
		d006Req.setRegNo((String) certIssueSession.get("RegNo")); //실명번호
		d006Req.setTSPassword((String) certIssueSession.get("TSPassword")); //통신비밀번호
		d006Req.setJuminSaupjaNo((String) certIssueSession.get("RegNo")); //주민사업자번호
		d006Req.setCertTranCode("1"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("S"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인,9:공동인증서)
		d006Req.setEmailAddr(email); //이메일
		d006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		d006Req.setRAGubun("6"); // 인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))

		CbIbk01D00600Res d006Res = this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class).getResponse();

		String issueGubun = d006Res.getIssueGubun();
		String userId = (String) certIssueSession.get("UserID");
		String personOrCompanyCode = (String) certIssueSession.get("PersonOrCompanyCode");

		String oppUserid ="";
		String deptPersonName = "";
		if("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode)){
			oppUserid = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(d006Res.getDeptPersonName(),9)+"()";
		} else {
			throw new PRCServiceException("PRCFCA01017", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		String issuerType = "p_personal"; //개인사용자
		if ("2".equals(certIssueSession.get("PersonOrCompanyCode"))) { //개인 사업자
			issuerType = "pc_personal";
		}

		Map<String,Object > oppraResult = (Map<String, Object>) certIssueSession.get("oppraResult");
		if(oppraResult == null) { // 타행인증서 강제폐기에서 이걸 지움
			oppraResult = certUtils.getOppResult(userId, oppUserid, deptPersonName);
		}

		String ExpireDate = d006Res.getExpireDate(); //만료일
		String SSRGubun = d006Res.getSSRGubun(); //수수료구분
		String is_c_Financial = (String) oppraResult.get("IS_C_FINANCIAL");
		String c_fStatus = (String) oppraResult.get("C_FINANCIAL_STATUS");
		String bStatus	= (String) oppraResult.get("BUSINESS_STATUS");

		String IS_P_FINANCIAL_FINANCIAL = (String) oppraResult.get("IS_P_FINANCIAL_FINANCIAL");
		String P_FINANCIAL_FINANCIAL_STATUS = (String) oppraResult.get("P_FINANCIAL_FINANCIAL_STATUS");
		String P_FINANCIAL_FINANCIAL_RESCODE = (String) oppraResult.get("P_FINANCIAL_FINANCIAL_RESCODE");
		String P_FINANCIAL_FINANCIAL_ISSUERORG = (String) oppraResult.get("P_FINANCIAL_FINANCIAL_ISSUERORG");
		String P_FINANCIAL_FINANCIAL_SERIAL= (String) oppraResult.get("P_FINANCIAL_FINANCIAL_SERIAL");
		String P_FINANCIAL_FINANCIAL_RAFLAG= (String) oppraResult.get("P_FINANCIAL_FINANCIAL_RAFLAG");

		log.debug("🍜IS_P_FINANCIAL_FINANCIAL :"+IS_P_FINANCIAL_FINANCIAL);
		log.debug("🍜P_FINANCIAL_FINANCIAL_STATUS :"+P_FINANCIAL_FINANCIAL_STATUS);
		log.debug("🍜P_FINANCIAL_FINANCIAL_RESCODE :"+P_FINANCIAL_FINANCIAL_RESCODE);
		log.debug("🍜P_FINANCIAL_FINANCIAL_ISSUERORG :"+P_FINANCIAL_FINANCIAL_ISSUERORG);
		log.debug("🍜P_FINANCIAL_FINANCIAL_SERIAL :"+P_FINANCIAL_FINANCIAL_SERIAL);
		log.debug("🍜P_FINANCIAL_FINANCIAL_RAFLAG :"+P_FINANCIAL_FINANCIAL_RAFLAG);
		log.debug("🍜IssueGubun host :"+issueGubun);

		/*
		 * 해당이용자의 해당 인증정책에 대해 효력정지된 상태이거나, 개인이 타행에 유효한 인증서가 발급되어 있다면 인증서발급불가!!!
		 * 신규인 경우만 유효하거나 폐지된 인증서 있는 경우 에러 처리함.
		 */
		if("0".equals(P_FINANCIAL_FINANCIAL_RAFLAG) || "INVALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
			issueGubun = "1";

			if("p_personal".equals(issuerType) && "YES".equals(IS_P_FINANCIAL_FINANCIAL) && "VALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
				log.debug("유효한 개인 금융 용도제한용 금융인증서를 발급받은 상태");

				HashMap<String, String> checkCertMap = null;
				try {
					checkCertMap = certUtils.checkCertStatus(P_FINANCIAL_FINANCIAL_SERIAL, "16");

				} catch (Exception e) {
					e.printStackTrace();
					throw new PRCServiceException("PRCFCA01004", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.[01]");
				}

				if("FALSE".equals(checkCertMap.get("SUCCESS"))){
					throw new PRCServiceException("PRCFCA01005", "이미 발급하신 금융인증서가 존재합니다.신규/재발급 시 기존 인증서는 폐기되어 다른 기관에서도 이용이 불가합니다.<br>기존 금융인증서를 폐기 후 재발급을 진행하시겠습니까?");
				}

				ExpireDate = checkCertMap.get("ExpireDate");
				issueGubun = checkCertMap.get("IssueGubun");
				SSRGubun = checkCertMap.get("SSRGubun");

			} else if("p_personal".equals(issuerType) && "YES".equals(IS_P_FINANCIAL_FINANCIAL) && "SUSPEND".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
				log.debug("효력정지된 인증서가 존재");
				throw new PRCServiceException("PRCFCA01006", "고객께서는 이미 유효한 개인 금융인증서를 발급받았으며, 현재 효력정지된 상태입니다.");

			} else if("pc_personal".equals(issuerType) && "YES".equals(IS_P_FINANCIAL_FINANCIAL) && "VALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
				log.debug("개인사업자의 금융 용도제한용 유효한 인증서가 존재");

				HashMap<String, String> checkCertMap = null;
				try {
					checkCertMap = certUtils.checkCertStatus(P_FINANCIAL_FINANCIAL_SERIAL, "16");

				} catch (Exception e) {
					e.printStackTrace();
					throw new PRCServiceException("PRCFCA01007", "인증 등록기관의 시스템 점검중입니다. 인증 등록기관 관리자에게 문의하시기 바랍니다.[02]");
				}

				ExpireDate = checkCertMap.get("ExpireDate");
				issueGubun = checkCertMap.get("IssueGubun");
				SSRGubun = checkCertMap.get("SSRGubun");
			}

		} else {
			issueGubun = "2";
			// 재발급을 없애는 테스트
			throw new PRCServiceException("PRCFCA01005", "이미 발급하신 금융인증서가 존재합니다.신규/재발급 시 기존 인증서는 폐기되어 다른 기관에서도 이용이 불가합니다.<br>기존 금융인증서를 폐기 후 재발급을 진행하시겠습니까?");
		}
		// 참조번호, 인가 코드 센터로부터 받아옴 시작
		// 등록메시지 작성
		HashMap<String,Object> raMap = new HashMap<String,Object>();
		raMap.put("MANAGERID", CertConfig.ADID); // 운영자아아디. OPTIONAL 속성
		raMap.put("USERCODE", "1"); //가입자구분코드 "1" 개인   "2" 기업
		raMap.put("OU_NAME", ""); // 개인은 법인명이 OPTIONAL
		raMap.put("CN_NAME", oppraResult.get("DeptPersonDetailName")); // 개인명, 법인/단체 세부명. "()"가 뒤에 포함되어야함. ex:) "홍길동()" 기업일 경우 "이니텍(INI)" ()안에 법인명이 들어감
		raMap.put("IDNO", certIssueSession.get("RegNo")); //주민(사업자)등록번호
		raMap.put("USERID", certIssueSession.get("UserID")); //사이트아이디
		raMap.put("SERVICEPROVIDER", "01"); //01:금결원OPP(또는 범용게이트웨이), 02:타기관직접연동
		raMap.put("CACODE", "01"); //01:금결원 02:SignKorea 03:전자인증
		raMap.put("CERTCODE", "16"); //01:개인범용  02:기업범용  04:개인(은행/보험)  05:기업범용 16:금융인증
		raMap.put("EMAIL", email); //이메일
		raMap.put("HANDPHONE", phoneNum); //휴대폰번호
		raMap.put("FAX", "02-2140-3699"); //팩스
		raMap.put("POSTCODE", certIssueSession.get("ZipCode")); //우편번호
		raMap.put("POSTADDR", "1");
		raMap.put("PHONE", ""); //전화
		raMap.put("STATISTICSCODE", "000386"); //for Advance version
		raMap.put("RESERVATION5", "0");

		// 인증서 발급/재발급
		Map<String, String> issueMap = kftcCertComponent.issueCert(issueGubun, raMap);

		// issue실패하면 다 throw했기때문에 참조번호인가코드 무조건 있다
		String refNum = issueMap.get("refNum");
		String appCode = issueMap.get("appCode");

		// 거래 후 추가 로 세션에 올린다
		String issueDate = DateUtils.getCurrentDate();
		String newExpireDate = certUtils.setExpireDate(issueDate, CertConfig.TermGijun, CertConfig.fincertTermPeriod);

		certIssueSession.put("RefNum", refNum);
		certIssueSession.put("AppCode", appCode);
		certIssueSession.put("issuerType", issuerType);
		certIssueSession.put("IS_P_FINANCIAL_FINANCIAL", IS_P_FINANCIAL_FINANCIAL);
		certIssueSession.put("P_FINANCIAL_FINANCIAL_STATUS", P_FINANCIAL_FINANCIAL_STATUS);
		certIssueSession.put("is_p_Financial", IS_P_FINANCIAL_FINANCIAL);
		certIssueSession.put("p_fStatus", IS_P_FINANCIAL_FINANCIAL);
		certIssueSession.put("is_c_Financial", is_c_Financial);
		certIssueSession.put("c_fStatus", c_fStatus);
		certIssueSession.put("bStatus", bStatus);
		certIssueSession.put("YESSIGN_CAIP", CertConfig.YESSIGN_CAIP);
		certIssueSession.put("YESSIGN_CAPORT", CertConfig.YESSIGN_CAPORT);
		certIssueSession.put("YESSIGN_KEYBOARD_SECURE_PROVIDER", CertConfig.YESSIGN_KEYBOARD_SECURE_PROVIDER);
		certIssueSession.put("YESSIGN_CANAME", CertConfig.YESSIGN_CA_NAME);
		certIssueSession.put("issueDate", issueDate);

		if ("1".equals(issueGubun) && "1".equals(SSRGubun)) { // IssueGubun = '1' and SSRGubun ='1' ==> 신규 및 수수료징구
			certIssueSession.put("ChuryGubun", "S");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", newExpireDate);
			certIssueSession.put("IssueType", "20");	//발급

		} else if("1".equals(issueGubun) && "2".equals(SSRGubun)) { // IssueGubun = '1' and SSRGubun ='2' ==> 신규 및 수수료미징구
			certIssueSession.put("ChuryGubun", "C");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", newExpireDate);
			certIssueSession.put("IssueType", "20");	//발급

		} else if("2".equals(issueGubun) && "2".equals(SSRGubun)) { // IssueGubun = '2' and SSRGubun ='2' ==> 재발급 및 수수료미징구
			certIssueSession.put("ChuryGubun", "C");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", ExpireDate);	// 재발급시는 원래 만료일을 그대로 사용.
			certIssueSession.put("IssueType", "25");	//재발급

		} else {
			throw new PRCServiceException("발급정보 오류");
		}
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		//추가인증 세션값 세팅
		sessionManager.setGlobalValue("RefNum", refNum);
		sessionManager.setGlobalValue("AppCode",  appCode);
		sessionManager.setGlobalValue("UserID", (String)certIssueSession.get("UserID"));
		sessionManager.setGlobalValue("TeleOne", request.getTel1());
		sessionManager.setGlobalValue("TeleTwo", request.getTel2());
		sessionManager.setGlobalValue("TeleThree", request.getTel3());
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", String.valueOf(certIssueSession.get("SafeCardKind")));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(certIssueSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(certIssueSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(certIssueSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", String.valueOf(certIssueSession.get("SecurityIndex")));
		sessionManager.setGlobalValue("SafeCardINDEX2", String.valueOf(certIssueSession.get("SecurityIndex2")));
		sessionManager.setGlobalValue("SmartOTP", String.valueOf(certIssueSession.get("SmartOTP")));
		sessionManager.setGlobalValue("PageGubun", String.valueOf(d006Req.getPageGubun()));
		sessionManager.setGlobalValue("TSPassword", String.valueOf(certIssueSession.get("TSPassword")));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 응답반환
		CrtFctGetFincertRefNumResponse response = new CrtFctGetFincertRefNumResponse();
		response.setRefNum(refNum);
		response.setAppCode(appCode);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeOtherFincertRevoke", name = "타행금융인증서 강제폐기")
	public CrtFctAuthorizeOtherFincertRevokeResponse authorizeOtherFincertRevoke(CrtFctAuthorizeOtherFincertRevokeRequest request) {
		// MA3CRTFCA001_802S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		// 폐기할 금융인증서 씨리얼번호 획득
		Map<String, String> searchCertMap = kftcCertComponent.searchCert("16", sessionManager.getGlobalValue("PerBusNo", String.class));

		sessionManager.setGlobalValue("SerialNum", searchCertMap.get("CERTSERIAL"));
		sessionManager.setGlobalValue("ExpireDate", searchCertMap.get("EVDATE").substring(0,8));

		// 타행발급 금융인증서 삭제
		Map<String, String> opprartn = null;
		try {
			opprartn = kftcCertComponent.changeStatusCert(sessionManager.getGlobalValue("SerialNum", String.class), "16", "30");
			if (opprartn == null) {
				throw new PRCServiceException("PRCFCA01022", "등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 인증기관 관리자에게 문의하시기 바랍니다.");
			}
		} catch (Exception e) {
			throw new PRCServiceException("PRCFCA01023", "등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 인증기관 관리자에게 문의하시기 바랍니다.");
		}

		// 본인확인 전문 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		// 공통부 세팅
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("D006");
		h006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setRegNo(sessionManager.getGlobalValue("PerBusNo", String.class)); //실명번호
		d006Req.setUserID(sessionManager.getGlobalValue("UserID", String.class)); //사용자아이디
		d006Req.setTSPassword("99999999"); //통신비밀번호
		d006Req.setCertTranCode("2"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("U"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인,9:공동인증서)
		d006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		d006Req.setIssueBank("023");
		d006Req.setJuminSaupjaNo(sessionManager.getGlobalValue("PerBusNo", String.class)); //주민사업자번호
		d006Req.setExpireDate(sessionManager.getGlobalValue("ExpireDate", String.class));
		d006Req.setRAGubun("6"); // 인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))

		this.hostClient.sendOltp(h006Options, d006Req, CbIbk01D00600Res.class);

		// 세션에 oppraResult 정보 초기화.
		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");
		log.debug("certIssueSession : " + certIssueSession.toString());
		certIssueSession.put("oppraResult", null);
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		CrtFctAuthorizeOtherFincertRevokeResponse response = new CrtFctAuthorizeOtherFincertRevokeResponse();

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeFincertIssue", name = "금융인증서 발급")
	public CrtFctAuthorizeFincertIssueResponse authorizeFincertIssue(CrtFctAuthorizeFincertIssueRequest request) {
		// MA3CRTFCA001_401S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");

		String teleOne = sessionManager.getGlobalValue("TeleOne", String.class);
		String teleTwo = sessionManager.getGlobalValue("TeleTwo", String.class);
		String teleThree = sessionManager.getGlobalValue("TeleThree", String.class);

		// 보안매체 확인 시작
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");

		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID((String) certIssueSession.get("UserID"));	//사용자아이디
		d006Req.setTSPassword((String) certIssueSession.get("TSPassword"));	//통신비밀번호
		d006Req.setRegNo((String) certIssueSession.get("RegNo"));	//실명번호
		d006Req.setCertTranCode("1");	//거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("N");	//처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
        d006Req.setCAGubun("2");	//발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인,9:공동인증서)
        d006Req.setRAGubun("6");	//인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))
        d006Req.setCustGubun("1");	//발급자구분(1:개인,2:기업)
        d006Req.setEmailAddr((String) certIssueSession.get("EmailAddr"));	//이메일
        d006Req.setJoinLoad("000");	//가입경로
        d006Req.setInforGubun((String) certIssueSession.get("InforGubun"));	//정보코드
        d006Req.setTelCode(teleOne+teleTwo+teleThree);	//전화번호

        String safeCardKind 	= (String) certIssueSession.get("SafeCardKind");
        d006Req.setSafeCardKind(safeCardKind);	//보안카드종류

        d006Req.setSafeCardValue1(" ");	//안전카드일련번호 사용자입력값1
        d006Req.setSafeCardValue2(" ");	//안전카드일련번호 사용자입력값2
        d006Req.setSafeCardValue3(" ");	//안전카드일련번호 사용자입력값3
        d006Req.setSafeCardIndex1(" ");	//안전카드일련번호 위치1
        d006Req.setSafeCardIndex2(" ");	//안전카드일련번호 위치2
        d006Req.setSafeCardIndex3(" ");	//안전카드일련번호 위치3
        d006Req.setSecurityIndex((String) certIssueSession.get("SecurityIndex")); //안전카드 INDEX
        d006Req.setSecurityIndex2((String) certIssueSession.get("SecurityIndex2")); //안전카드 INDEX2

        d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());	//안전카드앞자리 OR OTP 입력값
        d006Req.setSecurityValue2(" ");	//안전카드값 2

        if("1".equals(safeCardKind)) {
        	d006Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1());	//안전카드일련번호 사용자입력값1
            d006Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2());	//안전카드일련번호 사용자입력값2
            d006Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3());	//안전카드일련번호 사용자입력값3
            d006Req.setSafeCardIndex1((String) certIssueSession.get("SafeCardIndex1"));//안전카드일련번호 위치1
            d006Req.setSafeCardIndex2((String) certIssueSession.get("SafeCardIndex2"));//안전카드일련번호 위치2
            d006Req.setSafeCardIndex3((String) certIssueSession.get("SafeCardIndex3"));//안전카드일련번호 위치3
	        d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2());	//안전카드값2
        }

        if("3".equals(safeCardKind)) {
        	this.hostClient.sendOltpWithSecure(d006Options, d006Req, CbIbk01D00600Res.class);
        } else {
        	this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);
        }

		// 보안매체 검사, 요청한 참조번호 인가코드 확인 후 리턴함
		CrtFctAuthorizeFincertIssueResponse response = new CrtFctAuthorizeFincertIssueResponse();
		if(request.getRefNum() != null && request.getRefNum().equals(certIssueSession.get("RefNum"))) {
			response.setRefNum((String) certIssueSession.get("RefNum"));
		}
		if(request.getAppCode() != null && request.getAppCode().equals(certIssueSession.get("AppCode"))) {
			response.setAppCode((String) certIssueSession.get("AppCode"));
		}
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/confirmFincertIssue", name = "금융인증서 발급완료 통지")
	public CrtFctConfirmFincertIssueResponse confirmFincertIssue(CrtFctConfirmFincertIssueRequest request) {
		// MA3CRTFCA001_501S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");

		// 발급완료 전문처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");

		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID((String) certIssueSession.get("UserID"));	//사용자아이디
		d006Req.setRegNo((String) certIssueSession.get("RegNo")); // 실명번호
		d006Req.setTSPassword((String) certIssueSession.get("TSPassword")); // 통신비밀번호
		d006Req.setCertTranCode("1"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인,9:금융인증서)
		d006Req.setEmailAddr((String) certIssueSession.get("EmailAddr")); // email
		d006Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		d006Req.setRAGubun("6"); // 인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))
		d006Req.setJuminSaupjaNo((String) certIssueSession.get("RegNo")); // 주민사업자번호
		d006Req.setIssueBank("023"); // 인증서발급은행(은행코드)
		d006Req.setIssueDate((String) certIssueSession.get("IssueDate")); // 인증서유효기간시작일
		d006Req.setExpireDate((String) certIssueSession.get("ExpireDate")); // 인증서유효기간종료일
		String onAgreeChk = StringUtils.nvl(request.getOnAgreeChk(), ""); // 온라인발급사전동의여부
		if(!"".equals(onAgreeChk)) {
			d006Req.setYIAGREE(onAgreeChk);
		}

		this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

		// 메일발송
		String regNo = (String) certIssueSession.get("RegNo");
		String email = (String) certIssueSession.get("EmailAddr");
		String deptPersonName = (String) certIssueSession.get("DeptPersonName");
		String custName = "";
		if (deptPersonName.length() > 0 && email.length() > 0 && regNo.length() > 0) {
			log.debug("메일발송");
			EmailUtils.sendCompleteMail("71", regNo, deptPersonName, email.trim());
		}

		// SMS발송
		if (deptPersonName.length() > 4) {
			custName = deptPersonName.substring(0, 4);
		} else {
			custName = deptPersonName;
		}
		String userId = (String) certIssueSession.get("UserID");
		String tsPassword = (String) certIssueSession.get("TSPassword");
		String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
		String smsMsg = smsCustName + "님 인증서 발급완료. 본인요청 아닐 시 신고요망";
//		if (smsMsg.length() > 80) {
//			smsMsg = smsMsg.substring(0, 80);
//		}
		smsComponent.sendCompleteSMS(userId, tsPassword, regNo, smsMsg);

		// 세션 삭제
		sessionManager.removeGlobalValue("certIssueSession");

		// 추가인증 세션 삭제
//		sessionManager.removeGlobalValue("UserID");
		sessionManager.removeGlobalValue("TeleOne");
		sessionManager.removeGlobalValue("TeleTwo");
		sessionManager.removeGlobalValue("TeleThree");
		sessionManager.removeGlobalValue("TransPWUseYN");
		sessionManager.removeGlobalValue("SafeCardState");
		sessionManager.removeGlobalValue("SafeCardKind");
		sessionManager.removeGlobalValue("SafeCardINDEX");
		sessionManager.removeGlobalValue("SafeCardINDEX2");
		sessionManager.removeGlobalValue("SmartOTP");
		sessionManager.removeGlobalValue("PageGubun");

		// 응답생성
		CrtFctConfirmFincertIssueResponse response = new CrtFctConfirmFincertIssueResponse();
		response.setExpireDate((String) certIssueSession.get("ExpireDate")); // 인증서 만료일
		response.setCertSeqNum(request.getCertSeqNum());
		response.setSimpleKeyToken(request.getSimpleKeyToken());

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 타행 금융인증서 등록
	 * validateOtherFincertRegUser : 타행금융인증서 등록 본인확인
	 * validateOtherFincertRegCertificate : 타행금융인증서 유효성 검사
	 * authorizeOtherFincertReg : 타행금융인증서 등록
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateOtherFincertRegUser", name = "타행금융인증서 등록 본인확인")
	public CrtFctValidateOtherFincertRegUserResponse validateOtherFincertRegUser(CrtFctValidateOtherFincertRegUserRequest request) {
		// MA3CRTFCA002_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctValidateOtherFincertRegUserResponse response = new CrtFctValidateOtherFincertRegUserResponse();

		// 인증서 등록 세션 초기화
		sessionManager.removeGlobalValue("certRegSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인 된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 아이디찾기 전문처리
		OltpRequestOptions h892Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
		h892Options.setImsTranCd("TI1IBK01");
		h892Options.setInClassCd("H892");
		h892Options.setSvcCd("892");
		//개별부 세팅
		CbIbk01H89201Req h892Req = new CbIbk01H89201Req();
		h892Req.setUserID("FIRST999"); // 아이디
		h892Req.setCustJumin1(request.getCustJumin1()); // 실명번호
		h892Req.setCustJumin2(request.getCustJumin2()); // 실명번호
		h892Req.setActType("1"); // 처리구분 1:고객정보조회(예비처리), 2:계좌검증(본처리), 3:계좌미검증(본처리)
		h892Req.setAcctNum("");

		CbIbk01H89201Res h892Res = this.hostClient.sendOltp(h892Options, h892Req, CbIbk01H89201Res.class).getResponse();

		if("4".equals(h892Res.getUserType()) || "5".equals(h892Res.getUserType())) {
			throw new PRCServiceException("PRCFCA03001", "인터넷뱅킹 미가입 고객입니다.");
		}

		// 본인확인 전문처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01H00601Req h006Req = new CbIbk01H00601Req();
		h006Req.setUserID(h892Res.getUserID2()); // TI1IBK01_H892_01의 응답
		h006Req.setCustJumin1(request.getCustJumin1());
		h006Req.setCustJumin2(request.getCustJumin2());
		h006Req.setCertTranCode("6"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		h006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증 9:금융인증서
		h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		h006Req.setSimplifyGB("2");
		h006Req.setYIGSGB("1");

		CbIbk01H00600Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00600Res.class).getResponse();

		// 개인정보노출자 판단
		Map<String, String> h504Res = certificationSharedComponent.getYoLRSOTGB(h006Res.getRegNo());
		String YOLRSOTGB = h504Res.get("YOLRSOTGB");
		if("1".equals(YOLRSOTGB)) {
			sessionManager.removeGlobalValue("certRegSession");
			sessionManager.removeGlobalValue("PerBusNo");
			sessionManager.removeGlobalValue("CustName");
			response.setYoLRSOTGB(YOLRSOTGB);
			return response;
		}

		// 전화번호
		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		String HPOne = telCode[0];
		String HPTwo = telCode[1];
		String HPThree = telCode[2];
		String phoneNo = telCode[3];
		sessionManager.setGlobalValue("HPOne", HPOne);
		sessionManager.setGlobalValue("HPTwo", HPTwo);
		sessionManager.setGlobalValue("HPThree", HPThree);
		sessionManager.setGlobalValue("phoneNo", phoneNo);

		String personOrCompanyCode = h006Res.getPersonOrCompanyCode();
		String userId = h006Res.getUserID();
		String yoIDGB = h006Res.getYOIDGB();
		String oppUserId = "";
		String deptPersonName = "";

		if("3".equals(personOrCompanyCode)) {
//			throw new PRCServiceException("PRCFCA02003", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
			throw new PRCServiceException("PRCFCA02002", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}
		if("13".equals(personOrCompanyCode) || "13".equals(yoIDGB)) {
//			throw new PRCServiceException("PRCFCA02004", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
			throw new PRCServiceException("PRCFCA02002", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}
		//개인사업자는 오류
		if("13".equals(yoIDGB) || "14".equals(yoIDGB)){
			throw new PRCServiceException("PRCFCA02002", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}
		// 올바른 사용자 임.
		if("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode)) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(h006Res.getDeptPersonName(), 9) + "()";
		} else {
			throw new PRCServiceException("PRCFCA02001", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		//RA사용자 조회
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		// TODO 단말기 지정 서비스
//		ipinsideService.getPcFixValueThrow(UserID, serviceContext.request().getAttribute("IPINSIDE_AX"), serviceContext.request().getAttribute("IPINSIDE_HDD"));

		//본인 인증한 데이터 세션에 담는다
		Map<String,Object> certRegSession = new HashMap<String, Object>();
		certRegSession.put("UserID", h006Res.getUserID());
		certRegSession.put("TSPassword", h006Res.getTSPassword());
		certRegSession.put("RegNo", h006Res.getRegNo());
//		certRegSession.put("AcctPasswd", );
		certRegSession.put("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		certRegSession.put("SafeCardKind", h006Res.getSafeCardKind());
		certRegSession.put("SmartOTP", h006Res.getSmartOTP());
		certRegSession.put("EmailAddr", h006Res.getEmailAddr());
		certRegSession.put("DeptPersonName", h006Res.getDeptPersonName());
		certRegSession.put("SaupjaNo", h006Res.getSaupjaNo());
//		certRegSession.put("EngName", );
		certRegSession.put("ZipCode", h006Res.getZipCode());
		certRegSession.put("Address", h006Res.getAddress());
		certRegSession.put("SecurityIndex", h006Res.getSecurityIndex());
		certRegSession.put("SecurityIndex2", h006Res.getSecurityIndex2());
		certRegSession.put("SafeCardIndex1", h006Res.getSafeCardIndex1());
		certRegSession.put("SafeCardIndex2", h006Res.getSafeCardIndex2());
		certRegSession.put("SafeCardIndex3", h006Res.getSafeCardIndex3());
		certRegSession.put("AuthSVC4CERT", h006Res.getAuthSVC4CERT());
		certRegSession.put("InforGubun", h006Res.getInforGubun());
		certRegSession.put("CustGubun", h006Res.getCustGubun());
		certRegSession.put("TelCode", h006Res.getTelCode());

		certRegSession.put("oppraResult", oppraResult);	//RA정보

		sessionManager.setGlobalValue("certRegSession", certRegSession);
		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());

		response.setYoLRSOTGB(YOLRSOTGB);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/validateOtherFincertRegCertificate", name = "타행금융인증서 유효성 검사")
	public CrtFctValidateOtherFincertRegCertificateResponse validateOtherFincertRegCertificate(CrtFctValidateOtherFincertRegCertificateRequest request) {
		// MA3CRTFCA002_201S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctValidateOtherFincertRegCertificateResponse response = new CrtFctValidateOtherFincertRegCertificateResponse();

		String pkcs7SignedData = request.getPkcs7SignedData();
		String certSeqNum = request.getCertSeqNum();
		String simpleKeyToken = request.getSimpleKeyToken();

		// 인증서 유효성 검사
		Map<String, Object> certInfo = finCertVerifyComponent.verify("OC", pkcs7SignedData);
		String CertSubjectDN = (String) certInfo.get("CertSubjectDN");

		// 발급기관 확인
		String orgCode = certUtils.getOrgCode(CertSubjectDN);
//		if("KFB".equals(orgCode)) {
		if(CertConfig.RegCodeValue.equals(orgCode)) {
			throw new PRCServiceException("PRCFCA02007", "SC은행에서 발급받으신 금융인증서로 타행 금융인증서 사용신청을 하실 수 없습니다. 자세한 내용은 인증등록기관 관리자에게 문의하시기 바랍니다.");
		}

		// 개인용 금융인증서 확인
		if(CertSubjectDN.indexOf("OU=personalF") != -1 || CertSubjectDN.indexOf("ou=personalF") != -1) {
			log.debug("개인 금융인증서를 제출함");
		}else{
			throw new PRCServiceException("PRCFCA02006", "금융인증서 정보 조회 중 오류가 발생하였습니다. 다시 시도하여 주시기 바랍니다.");
		};

		// 본인확인 세션정보에 인증서정보 추가
		Map<String,Object> certRegSession = certUtils.getCertSession("certRegSession");
		certRegSession.put("simpleKeyToken", simpleKeyToken);
		certRegSession.put("CAGubun", "2");
		certRegSession.put("IssueDate", "0");
		certRegSession.put("ExpireDate", "0");
		certRegSession.put("IssueBank", (String) certInfo.get("CertBankCode"));
		certRegSession.put("userSerialStr", (String) certInfo.get("CertSerial"));
		certRegSession.put("userCert", certInfo);
		certRegSession.put("certSeqNum", certSeqNum);
		sessionManager.setGlobalValue("certRegSession", certRegSession);

		//추가인증 세션값 세팅
		String telCode = (String) certRegSession.get("TelCode");
		String[] telCodeArr = certUtils.phoneNumberPartArray(telCode);
		sessionManager.setGlobalValue("UserID", String.valueOf(certRegSession.get("UserID")));
		sessionManager.setGlobalValue("CustName", String.valueOf(certRegSession.get("DeptPersonName")));
		sessionManager.setGlobalValue("TeleOne", telCodeArr[0]);
		sessionManager.setGlobalValue("TeleTwo", telCodeArr[1]);
		sessionManager.setGlobalValue("TeleThree", telCodeArr[2]);
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", String.valueOf(certRegSession.get("SafeCardKind")));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(certRegSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(certRegSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(certRegSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", String.valueOf(certRegSession.get("SecurityIndex")));
		sessionManager.setGlobalValue("SafeCardINDEX2", String.valueOf(certRegSession.get("SecurityIndex2")));
		sessionManager.setGlobalValue("SmartOTP", String.valueOf(certRegSession.get("SmartOTP")));
		sessionManager.setGlobalValue("TSPassword", String.valueOf(certRegSession.get("TSPassword")));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeOtherFincertReg", name = "타행금융인증서 등록")
	public CrtFctAuthorizeOtherFincertRegResponse authorizeOtherFincertReg(CrtFctAuthorizeOtherFincertRegRequest request) {
		// MA3CRTFCA002_401S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctAuthorizeOtherFincertRegResponse response = new CrtFctAuthorizeOtherFincertRegResponse();

		Map<String,Object> certRegSession = certUtils.getCertSession("certRegSession");

		// 보안매체 검증처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
        d006Req.setUserID((String)certRegSession.get("UserID"));  	//사용자아이디
        d006Req.setTSPassword((String)certRegSession.get("TSPassword"));  	//통신비밀번호
        d006Req.setRegNo((String)certRegSession.get("RegNo"));  	//실명번호
        d006Req.setJuminSaupjaNo((String)certRegSession.get("RegNo"));  	//주민사업자번호
        d006Req.setCertTranCode("6");  	//거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
        d006Req.setChuryGubun("N");  	//처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
        d006Req.setCAGubun((String)certRegSession.get("CAGubun"));  	//발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
        d006Req.setRAGubun("6");	//인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))
        d006Req.setCustGubun("1");    //발급자구분(1:개인,2:기업)
        d006Req.setEmailAddr((String)certRegSession.get("EmailAddr"));    //이메일
        d006Req.setJoinLoad("3");    //가입경로
//        ti1ibk01H006Req.setPersonOrCompanyCode((String)certRegSession.get("PersonOrCompanyCode"));    //개인법인 구분코드
        d006Req.setInforGubun((String)certRegSession.get("InforGubun"));    //정보구분
        d006Req.setSafeCardKind((String)certRegSession.get("SafeCardKind"));    //보안카드종류
        d006Req.setYIJMGB("1");    //개인
        d006Req.setIssueDate("0");    //발급일
        d006Req.setExpireDate("0");    //만료일
        d006Req.setYIGSGB((String)certRegSession.get("PersonOrCompanyCode"));    //인증서간소발급자

        String safeCardKind = (String) certRegSession.get("SafeCardKind");
        d006Req.setSafeCardKind(safeCardKind);    //보안카드 종류
        d006Req.setSafeCardValue1(" ");    //안전카드일련번호 사용자입력값1
        d006Req.setSafeCardValue2(" ");    //안전카드일련번호 사용자입력값2
        d006Req.setSafeCardValue3(" ");    //안전카드일련번호 사용자입력값3
        d006Req.setSafeCardIndex1(" ");    //안전카드일련번호 위치1
        d006Req.setSafeCardIndex2(" ");    //안전카드일련번호 위치2
        d006Req.setSafeCardIndex3(" ");    //안전카드일련번호 위치3
        d006Req.setSecurityIndex((String)certRegSession.get("SecurityIndex"));    //안전카드 INDEX
        d006Req.setSecurityIndex2((String)certRegSession.get("SecurityIndex2"));    //안전카드 INDEX2
        d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1());    //안전카드값
    	d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2());    //안전카드값2

    	if ("1".equals(safeCardKind)) {
            d006Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1());    //안전카드일련번호 사용자입력값1
            d006Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2());    //안전카드일련번호 사용자입력값2
            d006Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3());    //안전카드일련번호 사용자입력값3
            d006Req.setSafeCardIndex1((String) certRegSession.get("SafeCardIndex1"));  //안전카드일련번호 위치1
            d006Req.setSafeCardIndex2((String) certRegSession.get("SafeCardIndex2"));    //안전카드일련번호 위치2
            d006Req.setSafeCardIndex3((String) certRegSession.get("SafeCardIndex3"));    //안전카드일련번호 위치3
	        d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2());    //안전카드값2
    	}

        if("3".equals(safeCardKind)) {
        	this.hostClient.sendOltpWithSecure(d006Options, d006Req, CbIbk01D00600Res.class);
        } else {
        	this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);
        }

        // 타행 금융인증서 등록처리
        Map<String, Object> certInfo = (HashMap<String, Object>) certRegSession.get("userCert");
        String userIssuerDN = (String) certInfo.get("CertIssuerDN");
		String CertPolicy = (String) certInfo.get("CertPolicy");
		String serialValue = (String) certInfo.get("CertSerial");
		String UserID = (String) certRegSession.get("UserID");
		log.debug("🍜 userIssuerDN={}, CertPolicy={}, serialValue={}, UserID={}", userIssuerDN, CertPolicy, serialValue, UserID);

		if(userIssuerDN.indexOf("yessignCA") == -1) {
			// 금결원 인증서가 아님
			throw new PRCServiceException("PRCFCA02014","타행 금융인증서 등록 중 오류가 발생하였습니다. 다시 시도하여 주시기 바랍니다.");
		}

		if(!"1.2.410.200005.1.1.1.10".equals(certInfo.get("CertOID"))){
			// 금융인증서 OID가 아님
			throw new PRCServiceException("PRCFCA02014","타행 금융인증서 등록 중 오류가 발생하였습니다. 다시 시도하여 주시기 바랍니다.");
		}

		// 타기관인증서 등록
		Map<String, String> rtnMap = kftcCertComponent.regOtherCert(serialValue, CertPolicy, UserID);

		String certStatus = rtnMap.getOrDefault("CodeData", "");

		// 90번전문 처리결과
		if("10".equals(certStatus)) { // 정상
			//DB처리
			if(serialValue != null && !"".equals(serialValue) && UserID != null && !"".equals(UserID)) {
				ldapInfoDao.updateUseridBySerial(serialValue, UserID);
			}
		} else if("30".equals(certStatus)) { // 폐기상태
			throw new PRCServiceException("PRCFCA02017", "고객께서 제출하신 금융인증서는 폐기된 인증서 이므로 타행등록을 하실 수 없습니다.");

		} else if("40".equals(certStatus)) { // 효력정지상태
			throw new PRCServiceException("PRCFCA02018","고객께서 요청하신 금융인증서는 효력정지된 인증서이므로 먼저 인증서를 발급받았던 기관(은행)에서 효력회복을 해야합니다. 자세한 내용은 인증기관 관리자에게 문의하시기 바랍니다.");
		}

		// 등록완료 전문처리
		OltpRequestOptions d006Options2 = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options2.setImsTranCd("TI1IBK01");
		d006Options2.setInClassCd("D006");
		d006Options2.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req2 = new CbIbk01D00600Req();
		d006Req2.setUserID(String.valueOf(certRegSession.get("UserID"))); //사용자아이디
		d006Req2.setTSPassword(String.valueOf(certRegSession.get("TSPassword"))); //통신비밀번호
		d006Req2.setRegNo(String.valueOf(certRegSession.get("RegNo"))); //실명번호
		d006Req2.setChuryGubun("U"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req2.setCAGubun(String.valueOf(certRegSession.get("CAGubun")));
		d006Req2.setRAGubun("6"); //금융인증서
		d006Req2.setCertTranCode("6"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req2.setCustGubun("1");
		d006Req2.setYIGSGB(String.valueOf(certRegSession.get("PersonOrCompanyCode")));
		d006Req2.setInforGubun(String.valueOf(certRegSession.get("InforGubun")));
		d006Req2.setJuminSaupjaNo(String.valueOf(certRegSession.get("RegNo")));//실명번호
		d006Req2.setIssueDate(String.valueOf(certRegSession.get("IssueDate"))); //등록일
		d006Req2.setExpireDate(String.valueOf(certRegSession.get("ExpireDate"))); //만료일
		d006Req2.setIssueBank(String.valueOf(certRegSession.get("IssueBank"))); //발급은행코드

		this.hostClient.sendOltp(d006Options2, d006Req2, CbIbk01D00600Res.class);

		// SMS발송
		String smsCustName = certRegSession.get("DeptPersonName") + "";
		smsCustName = BizCommonUtils.getMaskCustData(smsCustName, "01");
		String SMSMsg = smsCustName + "님 금융인증서 타기관 등록완료. 본인요청 아닐 시 신고요망";

		HashMap<String, String> sendSMS = new HashMap<String, String>();
		sendSMS.put("UserID", (String) certRegSession.get("UserID"));
		sendSMS.put("RegNo", (String) certRegSession.get("RegNo"));
		sendSMS.put("TransGB", "010");
		sendSMS.put("SMSMsg", SMSMsg);
		smsComponent.sendCompleteSMS(sendSMS);

		// 응답 반환
		response.setCertSeqNum((String) certRegSession.get("certSeqNum"));
		response.setSimpleKeyToken((String) certRegSession.get("simpleKeyToken"));

		//인증서 등록 세션 초기화
		sessionManager.removeGlobalValue("certRegSession");

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 타행 금융인증서 등록 해제
	 * validateOtherFincertDelUser : 타행금융인증서 해제 본인확인
	 * authorizeOtherFincertDel : 타행금융인증서 해제
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateOtherFincertDelUser", name = "타행금융인증서 해제 본인확인")
	public CrtFctValidateOtherFincertDelUserResponse validateOtherFincertDelUser(CrtFctValidateOtherFincertDelUserRequest request) {
		// MA3CRTFCA003_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctValidateOtherFincertDelUserResponse response = new CrtFctValidateOtherFincertDelUserResponse();

		//인증서 해지 세션 초기화
		sessionManager.removeGlobalValue("certReleaseSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		//로그인 된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 아이디찾기 전문 처리
		OltpRequestOptions h892Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
		h892Options.setImsTranCd("TI1IBK01");
		h892Options.setInClassCd("H892");
		h892Options.setSvcCd("892");
		//개별부 세팅
		CbIbk01H89201Req h892Req = new CbIbk01H89201Req();
		h892Req.setUserID("FIRST999"); // 아이디
		h892Req.setCustJumin1(request.getCustJumin1()); // 실명번호
		h892Req.setCustJumin2(request.getCustJumin2()); // 실명번호
		h892Req.setActType("1"); // 처리구분 1:고객정보조회(예비처리), 2:계좌검증(본처리), 3:계좌미검증(본처리)
		h892Req.setAcctNum("");

		CbIbk01H89201Res h892Res = this.hostClient.sendOltp(h892Options, h892Req, CbIbk01H89201Res.class).getResponse();

		if ("4".equals(h892Res.getUserType()) || "5".equals(h892Res.getUserType())) {
			throw new PRCServiceException("PRCFCA03001", "인터넷뱅킹 미가입 고객입니다.");
		}

		// 본인확인 전문 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01H00601Req h006Req = new CbIbk01H00601Req();
		h006Req.setUserID(h892Res.getUserID2()); // TI1IBK01_H892_01 의 응답
		h006Req.setCustJumin1(request.getCustJumin1());
		h006Req.setCustJumin2(request.getCustJumin2());
		h006Req.setSimplifyGB("2"); //인증서간소화 체크
		h006Req.setCertTranCode("6"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		h006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증 9:금융인증서
		h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		h006Req.setYIGSGB("1"); //인증서간소발급자

		CbIbk01H00600Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00600Res.class).getResponse();

		// 이용자 검사
		String personOrCompanyCode = h006Res.getPersonOrCompanyCode();
		String userId = h006Res.getUserID();
		String yoIDGB = h006Res.getYOIDGB();

		String oppUserId = "";
		String deptPersonName = "";

		if("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode)) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(h006Res.getDeptPersonName(), 9) + "()";

		} else {
			throw new PRCServiceException("PRCFCA03002", "잘못된 사용자");
		}

		//개인사업자는 오류
		if ("13".equals(yoIDGB) || "14".equals(yoIDGB)) {
			throw new PRCServiceException("PRCFCA03003", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}

		//RA사용자 조회
		Map<String,Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		//본인 인증한 데이터 세션에 담는다
		Map<String,Object> certReleaseSession = new HashMap<String, Object>();
		certReleaseSession.put("UserID", h006Res.getUserID());
		certReleaseSession.put("TSPassword", h006Res.getTSPassword());
		certReleaseSession.put("RegNo", h006Res.getRegNo());
//		certReleaseSession.put("AcctPasswd");
		certReleaseSession.put("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		certReleaseSession.put("SafeCardKind", h006Res.getSafeCardKind());
		certReleaseSession.put("SmartOTP", h006Res.getSmartOTP());
		certReleaseSession.put("EmailAddr", h006Res.getEmailAddr());
		certReleaseSession.put("DeptPersonName", h006Res.getDeptPersonName());
		certReleaseSession.put("SaupjaNo", h006Res.getSaupjaNo());
//		certReleaseSession.put("EngName", );
		certReleaseSession.put("ZipCode", h006Res.getZipCode());
		certReleaseSession.put("Address", h006Res.getAddress());
		certReleaseSession.put("SecurityIndex", h006Res.getSecurityIndex());
		certReleaseSession.put("SecurityIndex2", h006Res.getSecurityIndex2());
		certReleaseSession.put("SafeCardIndex1", h006Res.getSafeCardIndex1());
		certReleaseSession.put("SafeCardIndex2", h006Res.getSafeCardIndex2());
		certReleaseSession.put("SafeCardIndex3", h006Res.getSafeCardIndex3());
		certReleaseSession.put("AuthSVC4CERT", h006Res.getAuthSVC4CERT());
		certReleaseSession.put("InforGubun", h006Res.getInforGubun());
		certReleaseSession.put("CustGubun", h006Res.getCustGubun());
		certReleaseSession.put("TelCode", h006Res.getTelCode());

		certReleaseSession.put("oppraResult", oppraResult);	//RA정보
		sessionManager.setGlobalValue("certReleaseSession", certReleaseSession);
		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());
		sessionManager.setGlobalValue("UserID", h006Res.getUserID());

		//추가인증 세션값 세팅
		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		String HPOne = telCode[0];
		String HPTwo = telCode[1];
		String HPThree = telCode[2];
		String phoneNo = telCode[3];
		sessionManager.setGlobalValue("HPOne", HPOne);
		sessionManager.setGlobalValue("HPTwo", HPTwo);
		sessionManager.setGlobalValue("HPThree", HPThree);
		sessionManager.setGlobalValue("phoneNo", phoneNo);
		sessionManager.setGlobalValue("UserID", (String)certReleaseSession.get("UserID"));
		sessionManager.setGlobalValue("CustName", (String)certReleaseSession.get("DeptPersonName"));
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", String.valueOf(certReleaseSession.get("SafeCardKind")));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(certReleaseSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(certReleaseSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(certReleaseSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", String.valueOf(certReleaseSession.get("SecurityIndex")));
		sessionManager.setGlobalValue("SafeCardINDEX2", String.valueOf(certReleaseSession.get("SecurityIndex2")));
		sessionManager.setGlobalValue("SmartOTP", String.valueOf(certReleaseSession.get("SmartOTP")));
		sessionManager.setGlobalValue("TSPassword", String.valueOf(certReleaseSession.get("TSPassword")));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 인증서목록 조회
		HashMap<String, String> searchByUser = new HashMap<>();
		searchByUser.put("CertInquiryType", "F");
		List<OppraCertInfo> oppraCertList = certUtils.searchByUserID(h006Res.getUserID(), h006Res.getRegNo(), searchByUser);

		// 응답 반환
		response.setCustName(h006Res.getDeptPersonName().trim());
		response.setCertList(oppraCertList.stream().map(cert -> {cert.setJuminSaupjaNo(""); return cert;}).toList()); // 주민번호 제거

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeOtherFincertDel", name = "타행금융인증서 해제")
	public CrtFctAuthorizeOtherFincertDelResponse authorizeOtherFincertDel(CrtFctAuthorizeOtherFincertDelRequest request) {
		// MA3CRTFCA003_301S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		Map<String, Object> certReleaseSession = certUtils.getCertSession("certReleaseSession");

		// 보안매체 처리
		OltpRequestOptions d980Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D980");
		d980Options.setImsTranCd("TI1IBK01");
		d980Options.setInClassCd("D980");
		d980Options.setSvcCd("980");
		// 개별부 세팅
		CbIbk01D98000Req d980Req = new CbIbk01D98000Req();
		d980Req.setUserID((String) certReleaseSession.get("UserID")); // 사용자아이디
		d980Req.setTSPassword((String) certReleaseSession.get("TSPassword")); // 통신비밀번호
		d980Req.setRegNo((String) certReleaseSession.get("RegNo")); // 실명번호
		d980Req.setJuminSaupjaNo((String) certReleaseSession.get("RegNo")); // 주민사업자번호
		d980Req.setSafeCardKind((String) certReleaseSession.get("SafeCardKind")); // 보안카드종류
		d980Req.setYIGSGB((String) certReleaseSession.get("PersonOrCompanyCode")); // 인증서간소발급자
		d980Req.setCertTranCode("D"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d980Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d980Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인 9:금융인증서)
		d980Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		d980Req.setInforGubun("2"); // 정보구분
		d980Req.setIssueDate("0"); // 발급일
		d980Req.setExpireDate("0"); // 만료일
		String safeCardKind = (String) certReleaseSession.get("SafeCardKind");
		d980Req.setSafeCardKind(safeCardKind); // 보안카드 종류
		d980Req.setSecurityIndex((String) certReleaseSession.get("SecurityIndex")); // 안전카드 INDEX
		d980Req.setSecurityIndex2((String) certReleaseSession.get("SecurityIndex2")); // 안전카드 INDEX2
		d980Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드값
		d980Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드값2

		if ("3".equals(safeCardKind)) {
			this.hostClient.sendOltpWithSecure(d980Options, d980Req, CbIbk01D98000Res.class);
		} else {
			this.hostClient.sendOltp(d980Options, d980Req, CbIbk01D98000Res.class);
		}

		// RA 해제 처리
		String userId = (String) certReleaseSession.get("UserID");
		String regNO = (String) certReleaseSession.get("RegNo");
		String certserial = request.getCertserial();
		String certPolicyCode = request.getCertPolicyCode();

		// 내인증서 맞는지 확인 안하나..?
		HashMap<String, String> searchByUser = new HashMap<>();
		searchByUser.put("CertInquiryType", "F");
		List<OppraCertInfo> oppraCertList = certUtils.searchByUserID(userId, regNO, searchByUser);

		for (OppraCertInfo certInfo : oppraCertList) {
			if (certserial.equals(certInfo.getCertserial()) && certPolicyCode.equals(certInfo.getCertPolicyCode())) {
				kftcCertComponent.revokeOtherCert(certserial, certPolicyCode);
				break;
			}
		}

		CrtFctAuthorizeOtherFincertDelResponse response = new CrtFctAuthorizeOtherFincertDelResponse();
		// 인증서 사용자 관리 테이블 업데이트
		response.setSetDeviceStorageYn("N");

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
				} else if(StringUtils.isNotEmpty(deviceData.get(CommonBizConstants.IPINSIDE_IMEI))) {
					//IMEI인 경우
					pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_IMEI);
				}
			} else {
				pinDeviceId = ipinsideComponent.simpleDataDecode();
			}
		} catch (Exception e) {
			pinDeviceId = ipinsideComponent.simpleDataDecode();
		}

		// 인증서사용자관리테이블 처리
		DeviceCountResult dbResult = ma3CertUserMgtDao.selectCountDevice(userId);
		if(dbResult != null && dbResult.getCnt() > 0) {
			ma3CertUserMgtDao.updateJoinFlg("-", userId); // deviceId 초기화
			if(pinDeviceId.equals(dbResult.getDeviceId())){
				log.debug("현재 단말기의 DEVICE ID와 DB에 등록된 생체인증등록 단말기의 정보가 같을때 초기화");
				response.setSetDeviceStorageYn("Y");
			}
		}

		// 업무 세션 초기화
		sessionManager.removeGlobalValue("certReleaseSession");

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 금융인증서 갱신
	 * validateFincertRenew : 금융인증서 갱신 인증서 유효성 검사
	 * authorizeFincertRenew : 금융인증서 갱신
	 * confirmFincertRenew : 금융인증서 갱신완료 통지
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateFincertRenew", name = "금융인증서 갱신 인증서 유효성 검사")
	public CrtFctValidateFincertRenewResponse validateFincertRenew(CrtFctValidateFincertRenewRequest request) {
		// MA3CRTFCA005_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		// 인증서 갱신 세션 초기화
		sessionManager.removeGlobalValue("certRenewSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		if(sessionManager.isLogin()) {
			sessionManager.logout();
		}

		String pkcs7SignedData = request.getPkcs7SignedData();

		// 제출한인증서 유효성 검사
		Map<String, Object> certVerify = finCertVerifyComponent.verify("L", pkcs7SignedData);

		sessionManager.setGlobalValue("PerBusNo", sessionManager.getGlobalValue("cid", String.class)); // ???

		// 본인확인 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");
		// 개별부 세팅
		CbIbk01H00600Req h006Req = new CbIbk01H00600Req();
		/*
		 * 왜 여기만 세션 키가 다르지?
		 */
		h006Req.setJuminSaupjaNo(sessionManager.getGlobalValue("cid", String.class));
		h006Req.setRegNo(sessionManager.getGlobalValue("cid", String.class));
		h006Req.setUserID(sessionManager.getGlobalValue("uid", String.class));
		h006Req.setTSPassword("99999999");
		h006Req.setCertTranCode("3"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		h006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		h006Req.setYIJMGB("1"); //인증서사용자구분
		CbIbk01H00600Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00600Res.class).getResponse();

		String personOrCompanyCode = h006Res.getPersonOrCompanyCode();
		String userId = h006Res.getUserID();
		String oppUserId = "";
		String deptPersonName = "";

		if("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode)){
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(h006Res.getDeptPersonName(), 9) + "()";

		} else if("3".equals(personOrCompanyCode)) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// RA사용자 조회
		Map<String,Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		// 본인 인증한 데이터 세션에 담는다
		Map<String,Object> certRenewSession = new HashMap<String, Object>();
		certRenewSession.put("UserID", h006Res.getUserID());
		certRenewSession.put("TSPassword", h006Res.getTSPassword());
		certRenewSession.put("RegNo", h006Res.getRegNo());
		certRenewSession.put("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		certRenewSession.put("SafeCardKind", h006Res.getSafeCardKind());
		certRenewSession.put("SmartOTP", h006Res.getSmartOTP());
		certRenewSession.put("EmailAddr", h006Res.getEmailAddr());
		certRenewSession.put("DeptPersonName", h006Res.getDeptPersonName());
		certRenewSession.put("SaupjaNo", h006Res.getSaupjaNo());
		certRenewSession.put("ZipCode", h006Res.getZipCode());
		certRenewSession.put("Address", h006Res.getAddress());
		certRenewSession.put("SecurityIndex", h006Res.getSecurityIndex());
		certRenewSession.put("SecurityIndex2", h006Res.getSecurityIndex2());
		certRenewSession.put("SafeCardIndex1", h006Res.getSafeCardIndex1());
		certRenewSession.put("SafeCardIndex2", h006Res.getSafeCardIndex2());
		certRenewSession.put("SafeCardIndex3", h006Res.getSafeCardIndex3());
		certRenewSession.put("AuthSVC4CERT", h006Res.getAuthSVC4CERT());
		certRenewSession.put("InforGubun", h006Res.getInforGubun());
		certRenewSession.put("YOAGREEGB", h006Res.getYOAGREEGB()); //인증서발급동의여부
		certRenewSession.put("YOAGIL", h006Res.getYOAGIL()); //인증서발급동의거부일
		certRenewSession.put("TelCode", h006Res.getTelCode());
		certRenewSession.put("oppraResult", oppraResult);	//RA정보
		certRenewSession.put("userCert", certVerify.get("userCert")); //인증서 정보
		sessionManager.setGlobalValue("certRenewSession", certRenewSession);
		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());
		sessionManager.setGlobalValue("renewCertSerial", request.getRenewCertSerial());
		sessionManager.setGlobalValue("renewSimpleKeyToken", request.getRenewSimpleKeyToken());

		// 추가인증 세션값 세팅
		String[] telCode = certUtils.phoneNumberPartArray(h006Res.getTelCode());
		sessionManager.setGlobalValue("HPOne", telCode[0]);
		sessionManager.setGlobalValue("HPTwo", telCode[1]);
		sessionManager.setGlobalValue("HPThree", telCode[2]);
		sessionManager.setGlobalValue("phoneNo", telCode[3]);
		sessionManager.setGlobalValue("UserID", (String) certRenewSession.get("UserID"));
		sessionManager.setGlobalValue("InforGubun", h006Res.getInforGubun());
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", (String) certRenewSession.get("SafeCardKind"));
		sessionManager.setGlobalValue("SafeCardSeq1", h006Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", h006Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", h006Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", (String) certRenewSession.get("SecurityIndex"));
		sessionManager.setGlobalValue("SafeCardINDEX2", (String) certRenewSession.get("SecurityIndex2"));
		sessionManager.setGlobalValue("SmartOTP", (String) certRenewSession.get("SmartOTP"));
		sessionManager.setGlobalValue("TSPassword", (String) certRenewSession.get("TSPassword"));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 응답 반환
		CrtFctValidateFincertRenewResponse response = new CrtFctValidateFincertRenewResponse();
		response.setYoAGREEGB(h006Res.getYOAGREEGB()); // 인증서발급동의여부
		response.setYoCFJMGB(h006Res.getYOCFJMGB()); // 조합번호여부 3=차단

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeFincertRenew", name = "금융인증서 갱신")
	public CrtFctAuthorizeFincertRenewResponse authorizeFincertRenew(CrtFctAuthorizeFincertRenewRequest request) throws Exception {
		// MA3CRTFCA005_301S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctAuthorizeFincertRenewResponse response = new CrtFctAuthorizeFincertRenewResponse();

		// 1단계에서 제출했던 정보
		Map<String,Object> certRenewSession = certUtils.getCertSession("certRenewSession");

//		X509Certificate userCert = (X509Certificate) certRenewSession.get("userCert");
		X509Certificate userCert = certUtils.getCertificationFromB64((String) certRenewSession.get("userCert"));
		String userId = (String) certRenewSession.get("UserID");

		log.debug("X509Certificate={}", userCert);

		// 인증서 정보 추출 클래스 생성
		CertificateHelper certificateHelper = new CertificateHelper(userCert);

		String issuerDn = certificateHelper.getIssuerDN();
		String issuerO = certificateHelper.getIssuerO();
		String subjectCn = certificateHelper.getSubjectCN();
		String subjectDn = certificateHelper.getSubjectDN();
		String oid = certificateHelper.getCertificatePolicyOID();
		String serial = certificateHelper.getSerial();
		String policyCode = "";
		String raGubun = "";
		String custGubun = "";

		if("1.2.410.200005.1.1.1.10".equals(oid)) { // 금융인증서
			policyCode = "16";
			raGubun = "6";
			custGubun = "1";
		}

		log.debug("certificateHelper.getIssuerBankCode()={}", certificateHelper.getIssuerBankCode());
		log.debug("issuerDn={}", issuerDn);
		log.debug("subjectCn={}", subjectCn);
		log.debug("subjectDn={}", subjectDn);
		log.debug("oid={}", oid);
		log.debug("policyCode={}", policyCode);

		// 제출한 인증서가 사설인증서임
		if(CertConfig.kfbDN1.equals(issuerDn) || CertConfig.kfbDN2.equals(issuerDn)) {
			throw new PRCServiceException("PRCCRT20831", "SC은행 사설인증서는 공인인증센터에서 유효기간 연장을 할 수 없습니다. SC은행인증센터에서 재발급 받으시기 바랍니다.");
		}

		// 제출한 인증서가 금결원 인증서가 아님
		if(!"yessign".equals(issuerO)){
			throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 갱신이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 갱신을 하시기 바랍니다.");
		}

		// SC제일은행에서 발급한 인증서가 아님
		String orgCode = certUtils.getOrgCode(subjectDn);
		if(orgCode == null || !CertConfig.RegCodeValue.equals(orgCode)){
			throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 갱신이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 갱신을 하시기 바랍니다.");
		}

		// 인증서 시리얼번호로 당행인증서 DB조회
		LdapInfoResult ldapInfoResult = ldapInfoDao.selectLdapInfoBySerial(serial);
		String certStatus = ldapInfoResult.getStatus();
		String certUserId = ldapInfoResult.getUserid();

		// LDAP 조회결과 효력정지된 인증서임
		if(certStatus != null && !"V".equals(certStatus)) {
			throw new PRCServiceException("PRCCRT20830", "고객의 인증서는 효력정지된 인증서이므로 유효기간 연장을 할 수 없습니다.");
		}

		// LDAP_INFO 테이블의 USERID 를 지우는 경우가 있나보다...
		if((certUserId == null || "".equals(certUserId)) && (orgCode == null || CertConfig.RegCodeValue.equals(orgCode))) {
			throw new PRCServiceException("PRCCRT30115", "고객께서 제출하신 금융인증서는 SC은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
		}

		// 1단계떄 아이디와 다름
		if(!userId.equals(certUserId)) {
			throw new PRCServiceException("PRCCRT20614", "고객께서 제출하신 인증서는 본인의 인증서가 아니므로 갱신하실수 없습니다.인증서를 다시 한번 확인하시기 바랍니다.");
		}

		// 보안매체 검증 처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		// 개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID((String) certRenewSession.get("UserID"));
		d006Req.setTSPassword((String) certRenewSession.get("TSPassword"));
		d006Req.setRegNo((String) certRenewSession.get("RegNo"));
		d006Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		d006Req.setEmailAddr((String) certRenewSession.get("EmailAddr"));
		d006Req.setSafeCardKind((String) certRenewSession.get("SafeCardKind")); //보안카드종류
		d006Req.setCertTranCode("3"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("S"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		d006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		d006Req.setJoinLoad("000"); //가입경로
		d006Req.setYIJMGB("1"); // 개인

		d006Req.setRAGubun(raGubun);
		d006Req.setCustGubun(custGubun);

		String safeCardKind = (String) certRenewSession.get("SafeCardKind");
		d006Req.setSafeCardKind(safeCardKind); //보안카드 종류
		d006Req.setSafeCardValue1(" "); //안전카드일련번호 사용자입력값1
		d006Req.setSafeCardValue2(" "); //안전카드일련번호 사용자입력값2
		d006Req.setSafeCardValue3(" "); //안전카드일련번호 사용자입력값3
		d006Req.setSafeCardIndex1(" "); //안전카드일련번호 위치1
		d006Req.setSafeCardIndex2(" "); //안전카드일련번호 위치2
		d006Req.setSafeCardIndex3(" "); //안전카드일련번호 위치3
		d006Req.setSecurityIndex((String) certRenewSession.get("SecurityIndex")); //안전카드 INDEX
		d006Req.setSecurityIndex2((String) certRenewSession.get("SecurityIndex2")); //안전카드 INDEX

		d006Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); //안전카드값
		d006Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); //안전카드값2

        if("3".equals(safeCardKind)) {
        	this.hostClient.sendOltpWithSecure(d006Options, d006Req, CbIbk01D00600Res.class);
        } else {
        	this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);
        }

        // RA 갱신 인가
        String oppUserId = CertConfig.RegCodeValue + userId;
        String juminNo = (String)certRenewSession.get("RegNo");
        String emailAddr = (String)certRenewSession.get("EmailAddr");
        String telenum = (String)certRenewSession.get("phoneNo");
        String telenumCell = "";
        String telenumHome = "";
        String telenumOffice = "";

        if (telenum != null && telenum != "") {
        	if(telenum.startsWith("010") || telenum.startsWith("011") || telenum.startsWith("016") || telenum.startsWith("017") || telenum.startsWith("018") || telenum.startsWith("019")) {
        		telenumCell = telenum;
        	}else {
        		telenumHome = telenum;
        	}
		}

        // 갱신전문발송
        Map<String, String> renewResult = kftcCertComponent.renewCert(custGubun, oppUserId, juminNo, policyCode, emailAddr, telenumCell, telenumHome, telenumOffice);
        String resCode = StringUtils.nvl(renewResult.get("ResCode"), "");

        if("413".equals(resCode) || "412".equals(resCode)) {
        	throw new PRCServiceException("PRCCRT20834", "고객의 인증서는 현재 폐기되었거나 효력정지인 상태이므로 갱신 불가합니다.");
        }

        if("921".equals(resCode)) {
        	throw new PRCServiceException("PRCCRT20832", "가입자 갱신 등록은 30 일 전부터 허용됩니다.");
        }

        if(!"000".equals(resCode)) {
        	throw new PRCServiceException("PRCCRT20835", "갱신 처리중 오류가 발생하였습니다. 자세한 사항은 고객서비스센터(1588-1599)로 문의 하시기 바랍니다.");
        }

        /****************************************************************/
		/* 인증서의 유효기간 시작일과 만료일자를 생성해주는 루틴이다.      */
		/* 갱신된 인증서의 만료일은 갱신전 만료일 + 1일부터 1년후이다.   */
		/* 예) 원래   만료일 : 2003.12.01                               */
		/*     갱신후 만료일 : 2004.12.02                               */
		/* 테스트계는 갱신후 만료일은 원 만료일의 달수 + 1 이다.        */
		/* 예) 원래   만료일 : 2003.12.01                               */
		/*     갱신후 만료일 : 2004.01.01                               */
		/* 이거 계산해 주는 루틴을 js파일로 만들도록 하자.              */
		/* 필요한것이                                                   */
		/* input값은  날짜 , 구분(Y:년, M:월), 증가치(1)                */
		/* output값은 날짜                                              */
		/* Inserted by Kim Dong Hyuk on 2003.12.25                      */
		/****************************************************************/
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		String ExpireDate = simpledateformat.format(userCert.getNotAfter());

		String IssueDate = DateUtils.getCurrentDate();
		String oldExpireDate = certUtils.getFutureDate(ExpireDate,1);
		String newExpireDate = certUtils.setExpireDate(oldExpireDate,  CertConfig.TermGijun,  CertConfig.fincertTermPeriod);

		log.debug("newExpireDate : {}", newExpireDate);

		certRenewSession.put("IssueDate", IssueDate);
		certRenewSession.put("RAGubun", raGubun);
		certRenewSession.put("newExpireDate", newExpireDate);
		certRenewSession.put("CustGubun", custGubun);
		sessionManager.setGlobalValue("certRenewSession", certRenewSession);

		response.setSimpleKeyToken(sessionManager.getGlobalValue("simpleKeyToken", String.class));
		response.setCertSeqNum(sessionManager.getGlobalValue("renewCertSerial", String.class));

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/confirmFincertRenew", name = "금융인증서 갱신완료 통지")
	public CrtFctConfirmFincertRenewResponse confirmFincertRenew(CrtFctConfirmFincertRenewRequest request) {
		// MA3CRTFCA005_401S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		// 1단계에 저장한거
		Map<String,Object> certRenewSession = certUtils.getCertSession("certRenewSession");

		CrtFctConfirmFincertRenewResponse response = new CrtFctConfirmFincertRenewResponse();

		// HOST 갱신전문발송
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setUserID((String) certRenewSession.get("UserID"));
		d006Req.setTSPassword((String) certRenewSession.get("TSPassword"));
		d006Req.setRegNo((String) certRenewSession.get("RegNo"));
		d006Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		d006Req.setEmailAddr((String) certRenewSession.get("EmailAddr"));
		d006Req.setCertTranCode("3"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		d006Req.setChuryGubun("U"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		d006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		d006Req.setYIJMGB("1"); //개인
		d006Req.setRAGubun("6");
		d006Req.setIssueBank("023"); //발급은행
		d006Req.setIssueDate((String) certRenewSession.get("IssueDate"));
		d006Req.setExpireDate((String) certRenewSession.get("newExpireDate"));
		d006Req.setPageGubun("023"); // ??
		d006Req.setInforGubun((String) certRenewSession.get("InforGubun")); //정보구분
		String onAgreeChk = StringUtils.nvl(request.getOnAgreeChk(), "");
		if(!"".equals(onAgreeChk)) { // 온라인발급사전동의여부
			d006Req.setYIAGREE(onAgreeChk);
		}
		this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

		String deptPersonName = String.valueOf(certRenewSession.get("DeptPersonName")).trim();
		String userId = String.valueOf(certRenewSession.get("UserID")).trim();
		String regNo = String.valueOf(certRenewSession.get("RegNo")).trim();
		String custName = "";
		String smsMsg = "";

		if(deptPersonName.length() > 4) {
			custName = deptPersonName.substring(0, 4);
		} else {
			custName = deptPersonName;
		}

		// SMS 고객명 마스킹
		String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
		smsMsg = smsCustName + "님 인증서가 갱신되었어요.";
//		if(smsMsg.length() > 80) {
//			smsMsg = smsMsg.substring(0, 80);
//		}

		Map<String, String> sendSmsData = new HashMap<String, String>();
		sendSmsData.put("UserID", userId);
		sendSmsData.put("RegNo", regNo);
		sendSmsData.put("SMSMsg", smsMsg);
		sendSmsData.put("TransGB", "010");
		smsComponent.sendCompleteSMS(sendSmsData);

		//추가인증 세션 삭제
		sessionManager.removeGlobalValue("TeleOne");
		sessionManager.removeGlobalValue("TeleTwo");
		sessionManager.removeGlobalValue("TeleThree");
		sessionManager.removeGlobalValue("TransPWUseYN");
		sessionManager.removeGlobalValue("SafeCardState");
		sessionManager.removeGlobalValue("SafeCardKind");
		sessionManager.removeGlobalValue("SafeCardINDEX");
		sessionManager.removeGlobalValue("SafeCardINDEX2");
		sessionManager.removeGlobalValue("SmartOTP");
		sessionManager.removeGlobalValue("PageGubun");

		//업무세션 삭제처리
		sessionManager.removeGlobalValue("certRenewSession");

		// 금융인증서 생체인증등록여부 조회
		int cnt = ma3CertUserMgtDao.countCertUserMgt(userId);

		if(cnt > 0) {
			ma3CertUserMgtDao.updateCertUserMgtByRenew(userId);
			response.setFinCertFidoReg("Y");

		} else {
			response.setFinCertFidoReg("N");
		}

		response.setCertSeqNum(request.getRenewCertSeqNum());
		response.setSimpleKeyToken(request.getRenewSimpleKeyToken());
		response.setNewExpireDate(d006Req.getExpireDate());

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 금융인증서 폐기
	 * validateFincertRevokeUser : 금융인증서 폐기 본인확인
	 * validateFincertRevokeAcct : 금융인증서 폐기 계좌확인
	 * authorizeFincertRevoke : 금융인증서 폐기
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/validateFincertRevokeUser", name = "금융인증서 폐기 본인확인")
	public CrtFctValidateFincertRevokeUserResponse validateFincertRevokeUser(CrtFctValidateFincertRevokeUserRequest request) {
		// MA3CRTFCA004_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctValidateFincertRevokeUserResponse response = new CrtFctValidateFincertRevokeUserResponse();

		//인증서 폐기 세션 초기화
		sessionManager.removeGlobalValue("certDisposalSession");

		//로그인 된 경우 로그아웃 처리
		if(sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 본인확인 전문 처리
		OltpRequestOptions h006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");
		h006Options.setImsTranCd("TI1IBK01");
		h006Options.setInClassCd("H006");
		h006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01H00601Req h006Req = new CbIbk01H00601Req();
		h006Req.setCustJumin1(request.getCustJumin1()); //실명번호1
		h006Req.setCustJumin2(request.getCustJumin2()); //실명번호2
		h006Req.setUserID(request.getUserId());
		h006Req.setCertTranCode("2"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		h006Req.setChuryGubun("N"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		h006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		h006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)

		CbIbk01H00601Res h006Res = this.hostClient.sendOltp(h006Options, h006Req, CbIbk01H00601Res.class).getResponse();

		//본인 인증한 데이터 세션에 담는다
		Map<String,Object> certDisposalSession = new HashMap<String, Object>();
		certDisposalSession.put("UserID", h006Res.getUserID());
		certDisposalSession.put("TSPassword", h006Res.getTSPassword());
		certDisposalSession.put("RegNo", h006Res.getRegNo());
		certDisposalSession.put("PersonOrCompanyCode", h006Res.getPersonOrCompanyCode());
		certDisposalSession.put("SafeCardKind", h006Res.getSafeCardKind());
		certDisposalSession.put("SmartOTP", h006Res.getSmartOTP());
		certDisposalSession.put("EmailAddr", h006Res.getEmailAddr());
		certDisposalSession.put("DeptPersonName", h006Res.getDeptPersonName());
		certDisposalSession.put("SaupjaNo", h006Res.getSaupjaNo());
		certDisposalSession.put("ZipCode", h006Res.getZipCode());
		certDisposalSession.put("Address", h006Res.getAddress());
		certDisposalSession.put("SecurityIndex", h006Res.getSecurityIndex());
		certDisposalSession.put("SecurityIndex2", h006Res.getSecurityIndex2());
		certDisposalSession.put("SafeCardIndex1", h006Res.getSafeCardIndex1());
		certDisposalSession.put("SafeCardIndex2", h006Res.getSafeCardIndex2());
		certDisposalSession.put("SafeCardIndex3", h006Res.getSafeCardIndex3());
		certDisposalSession.put("AuthSVC4CERT", h006Res.getAuthSVC4CERT());
		certDisposalSession.put("YOIDGB", h006Res.getYOIDGB());	//이용등급
		certDisposalSession.put("InforGubun", h006Res.getInforGubun());

		sessionManager.setGlobalValue("UserID", h006Res.getUserID());
		sessionManager.setGlobalValue("PerBusNo", h006Res.getRegNo());
		sessionManager.setGlobalValue("CustName", h006Res.getDeptPersonName());

		sessionManager.setGlobalValue("certDisposalSession", certDisposalSession);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/validateFincertRevokeAcct", name = "금융인증서 폐기 계좌확인")
	public CrtFctValidateFincertRevokeAcctResponse validateFincertRevokeAcct(CrtFctValidateFincertRevokeAcctRequest request) {
		// MA3CRTFCA004_201S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctValidateFincertRevokeAcctResponse response = new CrtFctValidateFincertRevokeAcctResponse();

		Map<String, Object> certDisposalSession = certUtils.getCertSession("certDisposalSession");

		// 본인확인 처리
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		// 개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setSSRAcctNum(request.getAccountNum()); // 계좌번호
		d006Req.setAcctPasswd(request.getAccountBb()); // 계좌비밀번호
		d006Req.setRegNo((String) certDisposalSession.get("RegNo"));
		d006Req.setUserID((String) certDisposalSession.get("UserID"));
		d006Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		d006Req.setChuryGubun("C"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		d006Req.setRAGubun("6"); // 인증서종류(RA)(1:금융용,2:범용,6:금융인증서,7:블록체인,8:사설,9:타기관(RA틀림))
		d006Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)

		this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

		String personOrCompanyCode = (String) certDisposalSession.get("PersonOrCompanyCode");
		String userId = (String) certDisposalSession.get("UserID");
		String regNo = (String) certDisposalSession.get("RegNo");
		String oppUserId = "";
		String deptPersonName = "";

		if ("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode) || "3".equals(personOrCompanyCode)) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert((String)certDisposalSession.get("DeptPersonName"), 9);
			response.setDeptPersonName(deptPersonName);
			deptPersonName = deptPersonName+ "()";
		} else {
			throw new PRCServiceException("PRCFCA04001", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// 당행 인증서 조회
		List<LdapInfoResult> ldapInfoList = ldapInfoDao.selectForDelete("16", userId);
		log.debug("ldapInfoList={}", ldapInfoList.toString());

		// certList가 있으면 당행발급 인증서 삭제하는거고
		// certList 없이 certStatus 10이면 타행에 유효한 인증서 있으니 거기서 폐기하라고 안내함
		if (ldapInfoList.size() > 0) {
			response.setCertList(ldapInfoList.stream().map(o -> {
				CrtFctValidateFincertRevokeAcctResponse.CertListRecord record = new CrtFctValidateFincertRevokeAcctResponse.CertListRecord();
				record.setExpiredate(o.getExpiredatestr());
				record.setIssuedate(o.getIssuedatestr());
				record.setIssueEndDate(o.getExpiredatestr());
				record.setPolicy(o.getPolicy());
				record.setCustname(o.getCustname());
				record.setRaflag(o.getRaflag());
				record.setCid(o.getCid().substring(0, 6)+"-*******");
				record.setStatus(o.getStatus());
				record.setDn(o.getDn());
				record.setSerial(o.getSerial());
				record.setCn(o.getCn());
				record.setBankName("023"); // 당행발급인증서임
				return record;
			}).toList());

		} else {

			// LDAP_INFO 조회 안되면 RA 조회함
			Map<String, String> searchCertMap = kftcCertComponent.searchCert("16", regNo);
			if ("000".equals(searchCertMap.get("ResCode"))) {
				log.debug(" CERTPOLICY :[{}]", searchCertMap.get("CERTPOLICY"));
				log.debug(" EVDATE :[{}]", searchCertMap.get("EVDATE"));
				log.debug(" CERTSTATUS :[{}]", searchCertMap.get("CERTSTATUS"));
				log.debug(" CERTSERIAL :[{}]", searchCertMap.get("CERTSERIAL"));
				response.setCertStatus(searchCertMap.get("CERTSTATUS"));
			}
			Map<String, String> searchOptionMap = new HashMap<String, String>();
			searchOptionMap.put("CertInquiryType", "F"); // 금융인증서 조회
			searchOptionMap.put("WorkType", "RV"); // 폐기 진행중
			List<OppraCertInfo> OppraCertInfoList = certUtils.searchByUserID(oppUserId, regNo, searchOptionMap);
			response.setCertList(OppraCertInfoList.stream().map(o -> {
				CrtFctValidateFincertRevokeAcctResponse.CertListRecord record = new CrtFctValidateFincertRevokeAcctResponse.CertListRecord();
				record.setExpiredate(o.getIssueEndDate());
				record.setSerial(o.getCertserial());
//				record.setBankName(o.getIssueBankName()); // 이건 타기관이라고 나옴
				record.setBankName(o.getIssueBank()); // 이건 023이라고 나옴
				record.setIssueEndDate(o.getIssueEndDate());
				record.setPolicy(o.getCertPolicyCode());
				record.setRaflag("0");
				record.setCid(regNo.substring(0, 6)+"-*******");
				return record;
			}).toList());
		}

		String delUserId = userId.concat("__DELETECERT__USER_ID");

		Map<String, Object> oppraResult = certUtils.getOppResult(delUserId, oppUserId, deptPersonName);

		certDisposalSession.put("oppraResult", oppraResult);

		sessionManager.setGlobalValue("certDisposalSession", certDisposalSession);

		response.setCodeValue(CertConfig.RegCodeValue); // 0023

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeFincertRevoke", name = "금융인증서 폐기")
	public CrtFctAuthorizeFincertRevokeResponse authorizeFincertRevoke(CrtFctAuthorizeFincertRevokeRequest request) {
		// MA3CRTFCA004_301S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctAuthorizeFincertRevokeResponse response = new CrtFctAuthorizeFincertRevokeResponse();

		Map<String,Object> certDisposalSession = certUtils.getCertSession("certDisposalSession");

		Map<String, Object> oppraResult = (Map<String, Object>) certDisposalSession.get("oppraResult");

		String sCertPolicy = "PERSONAL_FINANCIAL";

		String serialValue = "";
		String PersonOrCompanyCode = String.valueOf(certDisposalSession.get("PersonOrCompanyCode"));

		String IS_P_FINANCIAL_FINANCIAL = String.valueOf(oppraResult.get("IS_P_FINANCIAL_FINANCIAL"));
		String P_FINANCIAL_FINANCIAL_STATUS = String.valueOf(oppraResult.get("P_FINANCIAL_FINANCIAL_STATUS"));
		String P_FINANCIAL_FINANCIAL_SERIAL= String.valueOf(oppraResult.get("P_FINANCIAL_FINANCIAL_SERIAL"));

		String issuerType = "p_personal";	//개인사용자

		if ("2".equals(certDisposalSession.get("PersonOrCompanyCode"))) {	//개인 사업자
			issuerType = "pc_personal";	//개인사용자
		}

		if ("1".equals(PersonOrCompanyCode) && "PERSONAL_FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL) && "INVALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
			// 개인 고객의 금융인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCFCA04006", "고객(개인)의 금융인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기 할 수 없습니다.");

		} else if ("1".equals(PersonOrCompanyCode) && "PERSONAL_FINANCIAL".equals(sCertPolicy) && "NO".equals(IS_P_FINANCIAL_FINANCIAL) ) {
			// 개인 고객이 금융용 인증서를 받지 않은 상태에서 폐지할려고 함.
			throw new PRCServiceException("PRCFCA04007", "고객(개인)께서는 금융인증서를 받지 않은 상태이므로 폐기 할 수 없습니다.");

		} else if ("1".equals(PersonOrCompanyCode) && "PERSONAL_FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && ("VALID".equals(P_FINANCIAL_FINANCIAL_STATUS) || "SUSPEND".equals(P_FINANCIAL_FINANCIAL_STATUS))) {
			// 개인 고객이 금융용 인증서가 유효하거나 효력정지인 상태에서  폐기 하려하면 정상 폐기 처리.
			serialValue = P_FINANCIAL_FINANCIAL_SERIAL;

//		} else if ("1".equals(PersonOrCompanyCode) && "FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && "INVALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
//			// 개인 고객의 범용 인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
//			throw new PRCServiceException("PRCFCA04008", "고객(개인)의 범용 인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기 할 수 없습니다.");
//
//		} else if ("1".equals(PersonOrCompanyCode) && "FINANCIAL".equals(sCertPolicy) && "NO".equals(IS_P_FINANCIAL_FINANCIAL) ) {
//			// 개인 고객이 범용 인증서를 받지 않은 상태에서 폐지할려고 함.
//			throw new PRCServiceException("PRCFCA04009", " 고객(개인)께서는 범용 인증서를 받지 않은 상태이므로 폐기 할 수 없습니다.");
//
//		} else if ("1".equals(PersonOrCompanyCode) && "FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && ("VALID".equals(P_FINANCIAL_FINANCIAL_STATUS) || "SUSPEND".equals(P_FINANCIAL_FINANCIAL_STATUS))) {
//			// 개인 고객이 범용 인증서가 유효하거나 효력정지인 상태에서  폐기 하려하면 정상 폐기 처리.
//			serialValue = P_FINANCIAL_FINANCIAL_SERIAL;
//
		} else if (issuerType.equals("pc_personal") && "PERSONAL_FINANCIAL".equals(sCertPolicy) && "NO".equals(IS_P_FINANCIAL_FINANCIAL) ) {
			// 개인사업자의 개인 금융인증서가 존재하지 않음.
			throw new PRCServiceException("PRCFCA04010", "고객(개인사업자)의 개인 금융인증서가 존재하지 않으므로 폐기 할 수 없습니다.");

		} else if (issuerType.equals("pc_personal") && "PERSONAL_FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && "INVALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
			// 개인사업자의 개인 금융인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCFCA04011", "고객(개인사업자)의 개인 금융인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기할 수 없습니다.");

		} else if (issuerType.equals("pc_personal") && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && ("VALID".equals(P_FINANCIAL_FINANCIAL_STATUS) || "SUSPEND".equals(P_FINANCIAL_FINANCIAL_STATUS))) {
			// 개인사업자의 개인 금융용 금융인증서가 유효하거나 효력정지인 상태에서 폐기하려면 정상 폐기 처리
			serialValue = P_FINANCIAL_FINANCIAL_SERIAL;

//		} else if (issuerType.equals("pc_personal") && "FINANCIAL".equals(sCertPolicy) && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && "INVALID".equals(P_FINANCIAL_FINANCIAL_STATUS)) {
//			// 개인사업자의 개인 범용 인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
//			throw new PRCServiceException("PRCFCA04012", " 고객(개인사업자)의 개인 범용 인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기할 수 없습니다.");

//		} else if (issuerType.equals("pc_personal") && "YES".equals(IS_P_FINANCIAL_FINANCIAL)  && ("VALID".equals(P_FINANCIAL_FINANCIAL_STATUS) || "SUSPEND".equals(P_FINANCIAL_FINANCIAL_STATUS))) {
//			// 개인사업자의 개인 범용 금융인증서가 유효하거나 효력정지인 상태에서 폐기하려면 정상 폐기 처리
//			serialValue = P_FINANCIAL_FINANCIAL_SERIAL;
		}

		// HOST 폐기전문 발송
		OltpRequestOptions d006Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");
		d006Options.setImsTranCd("TI1IBK01");
		d006Options.setInClassCd("D006");
		d006Options.setSvcCd("006");
		//개별부 세팅
		CbIbk01D00600Req d006Req = new CbIbk01D00600Req();
		d006Req.setIssueDate(request.getCertIssueDate()); //발급일
		d006Req.setExpireDate(request.getCertExpireDate()); //만료일
		d006Req.setRegNo((String) certDisposalSession.get("RegNo"));
		d006Req.setUserID((String) certDisposalSession.get("UserID"));
		d006Req.setTSPassword((String) certDisposalSession.get("TSPassword"));
		d006Req.setJuminSaupjaNo((String) certDisposalSession.get("RegNo"));
		d006Req.setCertTranCode("2"); //거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		d006Req.setChuryGubun("U"); //처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		d006Req.setCAGubun("2"); //발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		d006Req.setCustGubun("1"); //발급자구분(1:개인,2:기업)
		d006Req.setIssueBank("023"); //인증서발급은행(은행코드)
		d006Req.setRAGubun("6");

		this.hostClient.sendOltp(d006Options, d006Req, CbIbk01D00600Res.class);

		// 인증서 사용자 관리 테이블 업데이트
		response.setSetDeviceStorageYn("N");

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
				} else if(StringUtils.isNotEmpty(deviceData.get(CommonBizConstants.IPINSIDE_IMEI))) {
					//IMEI인 경우
					pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_IMEI);
				}
			} else {
				pinDeviceId = ipinsideComponent.simpleDataDecode();
			}
		} catch (Exception e) {
			pinDeviceId = ipinsideComponent.simpleDataDecode();
		}

		// 인증서사용자관리테이블 처리
		DeviceCountResult dbResult = ma3CertUserMgtDao.selectCountDevice((String) certDisposalSession.get("UserID"));
		if(dbResult != null && dbResult.getCnt() > 0) {
			ma3CertUserMgtDao.updateJoinFlg("-", (String) certDisposalSession.get("UserID")); // deviceId 초기화
			if(pinDeviceId.equals(dbResult.getDeviceId())){
				log.debug("현재 단말기의 DEVICE ID와 DB에 등록된 생체인증등록 단말기의 정보가 같을때 초기화");
				response.setSetDeviceStorageYn("Y");
			}
		}

		// RA 폐기상태 변경
		kftcCertComponent.changeStatusCert(serialValue, "16", "30");

		// 폐기결과 SMS 발송
		String custName = StringUtils.defaultIfBlank((String)certDisposalSession.get("DeptPersonName"), "").trim();
		String regNo = StringUtils.defaultIfBlank((String)certDisposalSession.get("RegNo"), "").trim();
		String userId = StringUtils.defaultIfBlank((String)certDisposalSession.get("UserID"), "").trim();
		String smsMsg = "";

		if(custName.length()>4) custName = custName.substring(0,4);

		smsMsg = BizCommonUtils.getMaskCustData(custName, "01") + "님 금융인증서 폐기완료. 본인요청 아닐 시 신고요망";

//		if(smsMsg.length()>80) {
//			smsMsg = smsMsg.substring(0, 80);
//		}

		log.debug("smsMsg={}", smsMsg);

		Map<String, String> sendSmsMap = new HashMap<String, String>();
		sendSmsMap.put("UserID", userId);
		sendSmsMap.put("RegNo", regNo);
		sendSmsMap.put("SMSMsg", smsMsg);
		sendSmsMap.put("TransGB", "010"); //001공인인증서(재발급)  002고객정보관리변경  003고객휴대폰번호변경  004고객이메일변경  005이체한도변경(감액) 006출금계좌 등록 007출금계좌 해지 008블록체인 009디지털인증서 010금융인증서 011토스인증서 012카카오인증서

		smsComponent.sendCompleteSMS(sendSmsMap);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	/************************************************************************************************************************************************
	 * 금융인증서 생체인증
	 * getFincertStatus : 금융인증서 보유여부 조회
	 * listFidoAvailable : 금융인증서 생체인증 등록허용 장치 목록 조회
	 * getFidoCertificate : 금융인증서 생체인증 인증서 등록상태 조회
	 * authorizeFidoCertificate : 금융인증서 생체인증 가입
	 * getFidoResult : 금융인증서 생체인증 거래결과 조회
	 * confirmFidoResult : 금융인증서 생체인증 정보 저장
	 * getFidoTrid : 금융인증서 생체인증 TRID 조회
	 * validateFidoCertificate : 금융인증서 생체인증 인증서 유효성 검사
	 ************************************************************************************************************************************************/
	@ServiceEndpoint(url = "/getFincertStatus", name = "금융인증서 보유여부 조회")
	public CrtFctGetFincertStatusResponse getFincertStatus(CrtFctGetFincertStatusRequest request) {
		// MA3CRTFCA008_102S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String userId = sessionManager.getLoginValue("UserID", String.class);

		int cnt = ldapInfoDao.selectFincertCount(userId);

		CrtFctGetFincertStatusResponse response = new CrtFctGetFincertStatusResponse();
		response.setRegCertYn(cnt > 0 ? "Y" : "N");

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/listFidoAvailable", name = "금융인증서 생체인증 등록허용 장치 목록 조회")
	public CrtFctListFidoAvailableResponse listFidoAvailable(CrtFctListFidoAvailableRequest request) {
		// MA3CRTFCA001_602S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctListFidoAvailableResponse response = new CrtFctListFidoAvailableResponse();
		response.setAaidFinger(fidoHttpComponent.allowedAuthnr("2")); // 지문,FACEID
		response.setAaidPin(fidoHttpComponent.allowedAuthnr("16384")); // PIN

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/getFidoCertificate", name = "금융인증서 생체인증 인증서 등록상태 조회")
	public CrtFctGetFidoCertificateResponse getFidoCertificate(CrtFctGetFidoCertificateRequest request) {
		// MA3CRTFCA001_601S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());
		
		CrtFctGetFidoCertificateResponse response = new CrtFctGetFidoCertificateResponse();
		
		sessionManager.setGlobalValue("appId", StringUtils.defaultIfBlank(request.getAppId(),""));
		sessionManager.setGlobalValue("deviceId", StringUtils.defaultIfBlank(request.getDeviceId(),""));
		sessionManager.setGlobalValue("certSeqNum", StringUtils.defaultIfBlank(request.getCertSeqNum(),""));
		sessionManager.setGlobalValue("simpleKeyToken", StringUtils.defaultIfBlank(request.getSimpleKeyToken(),""));

		sessionManager.setGlobalValue("os", StringUtils.defaultIfBlank(request.getOs(),""));
		sessionManager.setGlobalValue("spass", StringUtils.defaultIfBlank(request.getSpass(),""));
		sessionManager.setGlobalValue("fingerprintDevice", StringUtils.defaultIfBlank(request.getFingerprintDevice(),""));
		sessionManager.setGlobalValue("fingerprintEnrolled", StringUtils.defaultIfBlank(request.getFingerprintEnrolled(),""));
		sessionManager.setGlobalValue("irisDevice", StringUtils.defaultIfBlank(request.getIrisDevice(),""));
		sessionManager.setGlobalValue("irisEnrolled", StringUtils.defaultIfBlank(request.getIrisEnrolled(),""));
		sessionManager.setGlobalValue("faceDevice", StringUtils.defaultIfBlank(request.getFaceDevice(),""));
		sessionManager.setGlobalValue("faceEnrolled", StringUtils.defaultIfBlank(request.getFaceEnrolled(),""));
		sessionManager.setGlobalValue("pinEnabled", StringUtils.defaultIfBlank(request.getPinEnabled(),""));
		sessionManager.setGlobalValue("lockYN", StringUtils.defaultIfBlank(request.getLockYN(),""));

		// 등록상태 조회
		String userId = sessionManager.isLogin() ? sessionManager.getLoginValue("UserID", String.class) : sessionManager.getGlobalValue("UserID", String.class);
		String deviceId = StringUtils.defaultIfBlank(request.getDeviceId(),"");
		String appId = StringUtils.defaultIfBlank(request.getAppId(),"");
		String verifyType = "";

		log.debug("getFidoCertificate appId={}", appId);
		log.debug("getFidoCertificate deviceId={}", deviceId);

		Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(userId, deviceId, appId, verifyType, null);

		String regYn = "N";
		if (fidoResultMap != null) {
			regYn = (String) fidoResultMap.get("regYn");
			
			response.setAcceptedSize((int) fidoResultMap.getOrDefault("acceptedSize", 0));
			response.setAcceptedJsonString((String) fidoResultMap.get("acceptedJSONString"));

			response.setRegisteredSize((int) fidoResultMap.getOrDefault("registeredSize", 0));
			response.setRegisteredJsonString((String) fidoResultMap.get("registeredJSONString"));

			response.setIssuedCertListSize((int) fidoResultMap.getOrDefault("issuedCertListSize", 0));
			response.setIssuedCertListJsonString((String) fidoResultMap.get("issuedCertListJSONString"));
		}
		log.debug("getFidoCertificate regYn={}", regYn);

		response.setRegYn(regYn);

		response.setCertSeqNum(request.getCertSeqNum());
		response.setDeviceId(deviceId);
		response.setAppId(appId);
		response.setSimpleKeyToken(request.getSimpleKeyToken());

		response.setRenewBio(StringUtils.defaultIfBlank(request.getRenewBio(), "N"));

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

	@ServiceEndpoint(url = "/authorizeFidoCertificate", name = "금융인증서 생체인증 가입")
	public CrtFctAuthorizeFidoCertificateResponse authorizeFidoCertificate(CrtFctAuthorizeFidoCertificateRequest request) {
		// MA3CRTFCA001_603S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String deviceId = "";
		String appId  = "";
		String verifyType = request.getVerifyType();
		String issueType = request.getIssueType();
		String userId = "";

		if (sessionManager.isLogin()) {
			log.debug("로그인 상태임");
			userId = sessionManager.getLoginValue("UserID", String.class);
			deviceId = StringUtils.nvl(request.getDeviceId(), "");
			appId = StringUtils.nvl(request.getAppId(), "");

			sessionManager.setGlobalValue("deviceId", deviceId);
			sessionManager.setGlobalValue("appId", appId);

		} else {
			deviceId = sessionManager.getGlobalValue("deviceId", String.class);
			userId = sessionManager.getGlobalValue("UserID", String.class);
			appId = sessionManager.getGlobalValue("appId", String.class);
		}

		log.debug("🌷 금융인증서 발급/재발급 간편인증 서비스 가입 deviceId={}", deviceId);
		log.debug("🌷 금융인증서 발급/재발급 간편인증 서비스 가입 appId={}", appId);
		log.debug("🌷 금융인증서 발급/재발급 간편인증 서비스 가입 verifyType={}", verifyType);
		log.debug("🌷 금융인증서 발급/재발급 간편인증 서비스 가입 issueType={}", issueType);
		log.debug("🌷 금융인증서 발급/재발급 간편인증 서비스 가입 userId={}", userId);

		// FIDO 서비스 가입
		Map<String, Object> resultServiceRegistMap = (Map<String, Object>)	fidoHttpComponent.requestServiceRegist(deviceId, appId, verifyType, issueType);

		String resCode = (String)resultServiceRegistMap.get("resultCode");
		String resMsg = (String)resultServiceRegistMap.get("resultMsg");
		String trId = (String)resultServiceRegistMap.get("trId");
		String svcTrId = (String)resultServiceRegistMap.get("svcTrId");

		sessionManager.setGlobalValue("svcTrId", svcTrId);

		// 응답 반환
		CrtFctAuthorizeFidoCertificateResponse response = new CrtFctAuthorizeFidoCertificateResponse();
		response.setResCode(resCode);
		response.setAppId(appId);
		response.setDeviceId(deviceId);
		response.setResMsg(resMsg);
		response.setSvcTrId(svcTrId);
		response.setTrId(trId);
		response.setUserId(userId);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/getFidoResult", name = "금융인증서 생체인증 거래결과 조회")
	public CrtFctGetFidoResultResponse getFidoResult(CrtFctGetFidoResultRequest request) {
		// MA3CRTFCA001_604S finCertSdk_TrResultConfirm_YN=N
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String svcTrId = request.getSvcTrId();

		if(svcTrId == null || "".equals(svcTrId)) {
			svcTrId = sessionManager.getGlobalValue(svcTrId, String.class);
			log.debug("요청에 svcTrId가 없어서 세션에서 찾았습니다 [{}]", svcTrId);
		}

		// 거래결과 조회
		Map<String, Object> returnFIDOMap = fidoHttpComponent.trResultConfirm(svcTrId);

		// 응답 반환
		CrtFctGetFidoResultResponse response = new CrtFctGetFidoResultResponse();
		response.setResultCode((String) returnFIDOMap.get("resultCode"));
		response.setResultMsg((String) returnFIDOMap.get("resultMsg"));
		response.setResultData((String) returnFIDOMap.get("resultData"));
		response.setTrResultConfirmYn("N");

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/confirmFidoResult", name = "금융인증서 생체인증 정보 저장")
	public CrtFctConfirmFidoResultResponse confirmFidoResult(CrtFctConfirmFidoResultRequest request) {
		// MA3CRTFCA001_604S finCertSdk_TrResultConfirm_YN=Y
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		CrtFctConfirmFidoResultResponse response = new CrtFctConfirmFidoResultResponse();

		String certSeqNum = "";
		String simpleKeyToken = "";
		String userId = "";

		/* 세션에 없으면 input에 있는 값 꺼내쓰기 */
		if (sessionManager.isLogin()) {
			userId = sessionManager.getLoginValue("UserID", String.class);
			certSeqNum = StringUtils.defaultIfEmpty(request.getCertSeqNum(),"");
			simpleKeyToken = StringUtils.defaultIfEmpty(request.getSimpleKeyToken(), "");
		} else {
			userId = sessionManager.getGlobalValue("UserID", String.class);
			certSeqNum = sessionManager.getGlobalValue("certSeqNum", String.class);
			simpleKeyToken = sessionManager.getGlobalValue("simpleKeyToken", String.class);
		}

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

		int deviceCount = ma3CertUserMgtDao.countCertUserMgtBtDeviceId(pinDeviceId);

		if(deviceCount > 0) {
			// device id에 등록된 유저를 검색했을때 검색결과 있을경우
			// 현재 device에 등록된 user중 현재등록하려는 user를 제외한 user들의 등록여부 N으로 초기화 및 device id "-"으로 초기화
			ma3CertUserMgtDao.updateJoinFlgByDeviceId(pinDeviceId, userId);
		}

		// user Id에 다른 device id가 걸려있는지 검색
		DeviceCountResult deviceCountResult = ma3CertUserMgtDao.selectCountDevice(userId);

		CertUserMgtParameter certUserMgtParameter = new CertUserMgtParameter();
		certUserMgtParameter.setDeviceId(pinDeviceId); // DEVICE_ID
		certUserMgtParameter.setUserId(userId); // USER_ID
		certUserMgtParameter.setJoinFlg("Y"); // 가입 유무
		certUserMgtParameter.setConnectType("C"); // 접속방법 B (금융인증서-PIN), C (금융인증서-BIO), D (핀테크-토스), F (핀테크-카카오)
		certUserMgtParameter.setFinCertSeqNum(certSeqNum); // 인증시리얼번호
		certUserMgtParameter.setFinCertSmpKeyTkn(simpleKeyToken); // 간편인증키

		if (deviceCountResult != null) {
			int userCnt = deviceCountResult.getCnt();
			if (userCnt > 0) {
				// 등록하려는 userid 에 다른 device id가 걸려있을 경우 해당 userid update
				ma3CertUserMgtDao.updateCertUserMgt(certUserMgtParameter);
			}
		} else {
			// 등록하려는 userid로 검색결과가 없을경우 insert
			ma3CertUserMgtDao.insertCertUserMgt(certUserMgtParameter);
		}

		return response;
	}

	@ServiceEndpoint(url = "/getFidoTrid", name = "금융인증서 생체인증 TRID 조회")
	public CrtFctGetFidoTridResponse getFidoTrid(CrtFctGetFidoTridRequest request) {
		// MA3CMMINF006_100S command=requestServiceAuth
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		// DEVICEID로 BANKINGID 찾기
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
				} else if(StringUtils.isNotEmpty(deviceData.get(CommonBizConstants.IPINSIDE_IMEI))) {
					//IMEI인 경우
					pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_IMEI);
				}
			} else {
				pinDeviceId = ipinsideComponent.simpleDataDecode();
			}
		} catch (Exception e) {
			pinDeviceId = ipinsideComponent.simpleDataDecode();
		}

		PinUsrMgtByDeviceResult pinUsrMgtByDeviceResult = pinUsrMgtDao.selectPinUsrMgtByDeviceId(pinDeviceId);

		String userBankingID = "";
		if(pinUsrMgtByDeviceResult != null) {
			userBankingID = StringUtils.defaultIfEmpty(pinUsrMgtByDeviceResult.getUserBankingId(), "");
		}
		if("".equals(userBankingID)) {
			userBankingID = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class) , StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("UserID", String.class), ""));
		}

		// FIDO요청JSON생성
		String fidoSvctrId = request.getFidoSvctrId();
		if(fidoSvctrId == null || "".equals(fidoSvctrId)) {
			fidoSvctrId = fidoHttpComponent.getFidoSvctrId(); // 없으면 신규 채번함
		}
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("bizReqType", "app");
		jsonParam.put("signReqDt", System.currentTimeMillis());
		jsonParam.put("svcTrId", fidoSvctrId);
		jsonParam.put("appId", request.getFidoAppId());
		jsonParam.put("deviceId", request.getFidoDeviceid());
		jsonParam.put("verifyType", request.getVerifyType());
		jsonParam.put("issueType", "4096".equals(request.getVerifyType()) ? "01" : "03"); //01:S-PASS, 03:바이오인증서
		jsonParam.put("loginId", userBankingID);

		Map<String, Object> resultMap = fidoHttpComponent.requestServiceAuth(jsonParam);

		// 응답 반환
		CrtFctGetFidoTridResponse response = new CrtFctGetFidoTridResponse();
		response.setResultCode(StringUtils.nvl((String) resultMap.get("resultCode"), ""));
		response.setResultMsg(StringUtils.nvl((String) resultMap.get("resultMsg"), ""));
		response.setTrId(StringUtils.nvl((String) resultMap.get("trId"), ""));
		response.setSvcTrId(fidoSvctrId);
		response.setUserBankingId(userBankingID);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/validateFidoCertificate", name = "금융인증서 생체인증 인증서 유효성 검사")
	public CrtFctValidateFidoCertificateResponse validateFidoCertificate(CrtFctValidateFidoCertificateRequest request) throws Exception {
		// MA3CRTFCA008_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String pkcs7SignedData = request.getPkcs7SignedData();
		String certSeqNum = request.getCertSeqNum();
		String simpleKeyToken = request.getSimpleKeyToken();

		// 서명값 검증
		finCertVerifyComponent.verify("BR", pkcs7SignedData);

		sessionManager.setLoginValue("certSeqNum", certSeqNum);
		sessionManager.setLoginValue("simpleKeyToken", simpleKeyToken);

		// 응답 반환
		CrtFctValidateFidoCertificateResponse response = new CrtFctValidateFidoCertificateResponse();
		response.setCertSeqNum(certSeqNum);
		response.setSimpleKeyToken(simpleKeyToken);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/getPcFix", name = "비로그인 단말기지정 서비스 조회")
	public CrtFctGetPcFixResponse getPcFix(CrtFctGetPcFixRequest request) {
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String userId = request.getUserId();
		if(userId == null || "".equals(userId.trim())) {
			userId = sessionManager.isLogin()? sessionManager.getLoginValue("UserID", String.class) : sessionManager.getGlobalValue("UserID", String.class);
		}

		ipinsideComponent.getPcFixValueThrow(userId);

		CrtFctGetPcFixResponse response = new CrtFctGetPcFixResponse();
		response.setOtherPcYes(StringUtils.nvl(sessionManager.getGlobalValue("OtherPCYes", String.class), ""));
		response.setPcFixValue(StringUtils.nvl(sessionManager.getGlobalValue("PcFixValue", String.class), ""));

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

}