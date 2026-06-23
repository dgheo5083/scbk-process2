package com.scbank.process.api.svc.common.service.certification;

import java.util.HashMap;
import java.util.Map;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98100Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98100Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctGetUniCertFeeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctGetUniCertFeeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctPayUniCertFeePreRequest;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctPayUniCertFeePreResponse;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctPayUniCertFeeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctPayUniCertFeeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctValidateUniCertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.universal.CrtUctValidateUniCertIssueResponse;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.KicaCertComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.ipinside.dto.IpinsideHddInfo;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name="공통-인증센터-범용인증서", url="/certification/universal")
public class CertificationUniversalService {

	// 전문
	private final HostClient hostClient;

	// 세션
	private final ISessionContextManager sessionManager;

	// 인증센터 공통
	private final CertUtils certUtils;
	private final KicaCertComponent kicaCertComponent;

	private final IpinsideComponent ipinsideComponent;

	@ServiceEndpoint(url = "/validateUniCertIssue", name = "범용인증서 발급 본인확인")
	public CrtUctValidateUniCertIssueResponse validateUniCertIssue(IServiceContext serviceContext, CrtUctValidateUniCertIssueRequest request) {
		// ASIS :: H00607 [CertTask.KICA_reissue_step01(), CertTaskUtil.getBraCertStatus()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 범용인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("uniCertSession");

		// 본인확인 전문 처리
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00600Req cbIbk01H00600Req = new CbIbk01H00600Req();
		cbIbk01H00600Req.setUserID(request.getUserId().toUpperCase());
		cbIbk01H00600Req.setRegNo(request.getCustJumin1() + request.getCustJumin2());
		cbIbk01H00600Req.setCertTranCode("1"); 	//거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		cbIbk01H00600Req.setChuryGubun("N");  		//처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req.setCAGubun("3");			//발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		cbIbk01H00600Req.setRAGubun("9");			//인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01H00600Req.setCustGubun("1");		//발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00600Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00600Req, CbIbk01H00600Res.class);
		CbIbk01H00600Res cbIbk01H00600Res = hostResponse.getResponse();
		log.debug("validateUniCertIssue cbIbk01H006Res :: {}", cbIbk01H00600Res);

		// 한국정보인증. 인증서 발급. 정보인증인증서 정보조회. 계좌인증여부 확인
		String regNo = cbIbk01H00600Res.getRegNo();
		String userId = cbIbk01H00600Res.getUserID().toUpperCase();
		String marketCode = "1";	//SC제일은행 마케팅아이디

		String saupjaNo = cbIbk01H00600Res.getSaupjaNo();
		// 개인사업자인경우 주민번호를 사업자번호로 대치
		String personOrCompanyCode = cbIbk01H00600Res.getPersonOrCompanyCode();
		if(personOrCompanyCode.equals("2")) {
			// 개인사업자인경우 주민번호를 사업자번호로 대치
			regNo = saupjaNo;
			log.debug("validateUniCertIssue JuminSaupjaNo :: {}", regNo);
		}

		log.debug("validateUniCertIssue userId :: {}", userId);
		log.debug("validateUniCertIssue regNo :: {}", regNo);

		// 응답 반환
		CrtUctValidateUniCertIssueResponse response = new CrtUctValidateUniCertIssueResponse();

		// 범용인증서 발급 세션
		Map<String, Object> uniCertSession = new HashMap<>();

		// 인증서 상태 검증 START
		try {
			Map<String, String> rtnMap = kicaCertComponent.getBraCertStatus(marketCode, userId, regNo);
			String isRequestAccountAuth = rtnMap.get("IS_REQUEST_ACCOUNT_AUTH");		//계좌인증여부값

			uniCertSession.put("IS_REQUEST_ACCOUNT_AUTH", isRequestAccountAuth);
		} catch(Exception e) {
			throw new PRCServiceException("PRCCRT70001","RA(CA)서버로 데이터 전송에 실패했습니다(SGE001).다시 시도하십시요");
		}
		// 인증서 상태 검증 END

		// 본인 인증한 데이터 세션에 저장
		uniCertSession.put("UserID", cbIbk01H00600Res.getUserID());
		uniCertSession.put("TSPassword", cbIbk01H00600Res.getTSPassword());
		uniCertSession.put("RegNo", cbIbk01H00600Res.getRegNo());
		uniCertSession.put("PersonOrCompanyCode", cbIbk01H00600Res.getPersonOrCompanyCode());
		uniCertSession.put("InforGubun", cbIbk01H00600Res.getInforGubun());
		uniCertSession.put("SafeCardKind", cbIbk01H00600Res.getSafeCardKind());
		uniCertSession.put("SmartOTP", cbIbk01H00600Res.getSmartOTP());
		uniCertSession.put("DeptPersonName", cbIbk01H00600Res.getDeptPersonName());
		uniCertSession.put("SecurityIndex", cbIbk01H00600Res.getSecurityIndex());
		uniCertSession.put("SecurityIndex2", cbIbk01H00600Res.getSecurityIndex2());
		uniCertSession.put("SafeCardIndex1", cbIbk01H00600Res.getSafeCardIndex1());
		uniCertSession.put("SafeCardIndex2", cbIbk01H00600Res.getSafeCardIndex2());
		uniCertSession.put("SafeCardIndex3", cbIbk01H00600Res.getSafeCardIndex3());
		uniCertSession.put("OTPStatus", cbIbk01H00600Res.getOTPStatus());			//일회용비밀번호상태
		uniCertSession.put("AuthSVC4CERT", cbIbk01H00600Res.getAuthSVC4CERT());	//추가인증(공동인증)
		uniCertSession.put("AuthSVC4TRAN", cbIbk01H00600Res.getAuthSVC4TRAN());	//추가인증(이체)
		uniCertSession.put("TelCode", cbIbk01H00600Res.getTelCode());
		uniCertSession.put("ZipCode", cbIbk01H00600Res.getZipCode());
		uniCertSession.put("Address", cbIbk01H00600Res.getAddress());
		uniCertSession.put("UserStatus", cbIbk01H00600Res.getUserStatus());
		uniCertSession.put("SaupjaNo", cbIbk01H00600Res.getSaupjaNo());
		uniCertSession.put("EmailAddr", cbIbk01H00600Res.getEmailAddr());

		sessionManager.setGlobalValue("uniCertSession", uniCertSession);

		log.debug("uniCertSession :: {}", uniCertSession);

		// 추가인증 세션값 세팅
		sessionManager.setGlobalValue("UserID", (String) uniCertSession.get("UserID"));
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", String.valueOf(uniCertSession.get("SafeCardKind")));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(uniCertSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(uniCertSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(uniCertSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", String.valueOf(uniCertSession.get("SecurityIndex")));
		sessionManager.setGlobalValue("SafeCardINDEX2", String.valueOf(uniCertSession.get("SecurityIndex2")));
		sessionManager.setGlobalValue("SmartOTP", String.valueOf(uniCertSession.get("SmartOTP")));
		sessionManager.setGlobalValue("TSPassword", String.valueOf(uniCertSession.get("TSPassword")));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		response.setYoAGREEGB(cbIbk01H00600Res.getYOAGREEGB());			//온라인 발급 사전동의여부
		response.setYoAGIL(cbIbk01H00600Res.getYOAGIL());				//인증서발급동의거부일
		response.setCustName(cbIbk01H00600Res.getDeptPersonName());		//고객명
		response.setPersonOrCompanyCode(cbIbk01H00600Res.getPersonOrCompanyCode());	//개인법인 구분코드
		response.setTelCode(cbIbk01H00600Res.getTelCode());			//전화번호

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getUniCertFee", name = "범용인증서 발급 수수료 징수 여부 조회")
	public CrtUctGetUniCertFeeResponse getUniCertFee(IServiceContext serviceContext, CrtUctGetUniCertFeeRequest request) throws Exception {
		// ASIS :: H00607 [procIssueFirst(), CertTask.KICA_reissue_step02(), CertTaskUtil.getBraCertStatus(), CertTaskUtil.KICA_CertReIssue]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String,Object> uniCertSession = certUtils.getCertSession("uniCertSession");
		Map<String, Object> oppResult =  new HashMap<>();

		String telCode = (String) uniCertSession.get("TelCode");
		if("".equals(telCode)) {
			telCode = VerificationUtils.getTelNo1()+VerificationUtils.getTelNo2()+VerificationUtils.getTelNo3();
			uniCertSession.put("TelCode", telCode);
			sessionManager.setGlobalValue("uniCertSession", uniCertSession);
		}

		// 보안매체 검증 처리 START
		log.debug("getUniCertFee 보안매체 검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();

		cbIbk01D00600Req.setUserID((String) uniCertSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) uniCertSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setInforGubun((String) uniCertSession.get("InforGubun"));
		cbIbk01D00600Req.setRegNo((String) uniCertSession.get("RegNo"));

		String isRequestAccountAuth = (String) uniCertSession.get("IS_REQUEST_ACCOUNT_AUTH");
		String certTranCode = isRequestAccountAuth.equals("Y") ? "7" : "1";
		String personOrCompanyCode = (String) uniCertSession.get("PersonOrCompanyCode");
		String custGubun = !"2".equals(personOrCompanyCode) ? "1" : "2";

		cbIbk01D00600Req.setCertTranCode(certTranCode);	// 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setCustGubun(custGubun); 		// 발급자구분(1:개인,2:기업)
		cbIbk01D00600Req.setChuryGubun("N");		// 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("3"); 		// 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setRAGubun("9");		//인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req.setJoinLoad("105"); 	// 가입경로
		cbIbk01D00600Req.setYIAGREE("Y");

		cbIbk01D00600Req.setEmailAddr((String) uniCertSession.get("EmailAddr"));
		cbIbk01D00600Req.setTelCode((String) uniCertSession.get("TelCode"));	// 전화번호
		String step3_SafeCardKind = (String) uniCertSession.get("SafeCardKind"); // 보안매체 종류
		cbIbk01D00600Req.setSafeCardKind(step3_SafeCardKind);// 보안카드 종류

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) uniCertSession.get("SecurityIndex"));// 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) uniCertSession.get("SecurityIndex2"));// 안전카드 INDEX2
		cbIbk01D00600Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드 첫번째 입력값 or OTP 입력값
		cbIbk01D00600Req.setSecurityValue2(" "); // 안전카드값2

		log.debug("getUniCertFee step3_SafeCardKind :: {}", step3_SafeCardKind);

		if ("1".equals(step3_SafeCardKind)) {	// 보안매체가 안전카드인 경우
			cbIbk01D00600Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1()); // 안전카드일련번호 사용자입력값1
			cbIbk01D00600Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2()); // 안전카드일련번호 사용자입력값2
			cbIbk01D00600Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3()); // 안전카드일련번호 사용자입력값3
			cbIbk01D00600Req.setSafeCardIndex1((String) uniCertSession.get("SafeCardIndex1")); //안전카드일련번호 위치1
			cbIbk01D00600Req.setSafeCardIndex2((String) uniCertSession.get("SafeCardIndex2")); //안전카드일련번호 위치2
			cbIbk01D00600Req.setSafeCardIndex3((String) uniCertSession.get("SafeCardIndex3")); //안전카드일련번호 위치3
			cbIbk01D00600Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드 두번째 입력값
		}

		if("2".equals(personOrCompanyCode)) {
			cbIbk01D00600Req.setJuminSaupjaNo((String) uniCertSession.get("SaupjaNo"));
		} else {
			cbIbk01D00600Req.setJuminSaupjaNo((String) uniCertSession.get("RegNo"));
		}

		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals(step3_SafeCardKind)) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		CbIbk01D00600Res cbIbk01H00600Res = hostResponse.getResponse();
		log.debug("getUniCertFee 보안매체 검증 END");
		// 보안매체 검증 처리 END

		//한국정보인증. 인증서 발급. OTP 추가 검증 및 정보인증인증서 정보 조회, 수수료 징구여부 확인
		String marketCode = "1";	//SC제일은행 마케팅아이디
		String userId = cbIbk01H00600Res.getUserID();
		String deptPersonName = cbIbk01H00600Res.getDeptPersonName();
		String companyName = request.getCompanyName();
		String engName = request.getEngName();
		String policyId = personOrCompanyCode.equals("1") ? "1 등급 인증서(개인)" : "1 등급 인증서(법인)";
		String regNo = cbIbk01H00600Res.getRegNo();

		// 개인사업자인경우 주민번호를 사업자번호로 대치, 이름도 회사명으로 대치
		if(personOrCompanyCode.equals("2")) {
			regNo = cbIbk01H00600Res.getSaupjaNo();
			deptPersonName = companyName;

			log.debug("getUniCertFee regNo :: {}", regNo);
			log.debug("getUniCertFee deptPersonName :: {}", deptPersonName);
		}

		// 인증서 상태 검증
		Map<String, String> rtnMap = kicaCertComponent.getBraCertStatus(marketCode, userId, regNo);
		String sgSSRGubun = rtnMap.get("SGSSRGubun");
		String currentChkRefNo = rtnMap.get("SGRefNo");
		String currentChkAuthCode = rtnMap.get("SGAuthCode");
		String registerCase = rtnMap.get("RA_REG_CASE");
		String oppStatusCode = rtnMap.get("SGStatusCode");


		oppResult.put("RA_REG_CASE", rtnMap.get("RA_REG_CASE"));		//인증서 상태 [신규:2 ,재발급:8, 사용자 재인가:19]
		oppResult.put("SGSSRGubun", sgSSRGubun);
		oppResult.put("SGAuthCode", currentChkAuthCode);
		oppResult.put("SGRefNo", currentChkRefNo);
		oppResult.put("SGStatusCode", oppStatusCode);

		// 응답 객체
		CrtUctGetUniCertFeeResponse response = new CrtUctGetUniCertFeeResponse();

		//수수료 징구 대상이 아닐경우
		if(!sgSSRGubun.equals("1")) {
			// 참조코드, 인가코드가 없을 경우. 인증서 발급모드
			if(currentChkRefNo.trim().length() == 0 || currentChkAuthCode.trim().length() == 0) {
				log.debug("==========KICA_CertReIssue Start====================================");
				if(Integer.parseInt(registerCase) != 8) {	// IniCertConstants.SG_BRA_USER_KEYREC : 8 (사용자 재등록)
					throw new PRCServiceException("PRCCRT70001", "고객 정보 확인에 실패했습니다(SGE009).문의바랍니다.");
				}
				Map<String, String> sgrartn = null;
				sgrartn = kicaCertComponent.reIssueCert(userId, regNo, oppStatusCode, marketCode, Integer.parseInt(registerCase));
				currentChkAuthCode = sgrartn.get("SGAuthCode");
				currentChkRefNo = sgrartn.get("SGRefNo");

				oppResult.put("SGAuthCode", currentChkAuthCode);
				oppResult.put("SGRefNo", currentChkRefNo);

				log.debug("getUniCertFee currentChkRefNo :: {}", currentChkRefNo);
				log.debug("getUniCertFee currentChkAuthCode :: {}", currentChkAuthCode);
				log.debug("==========KICA_CertReIssueResult====================================");
			}
		} else {
			// 수수료 징구 대상일경우. 수수료 납부 확인 전문을 호스트로 추가로 던진다
			cbIbk01D00600Req.setChuryGubun("S"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
			OltpResponse<CbIbk01D00600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
			CbIbk01D00600Res cbIbk01H0060100Res2 = hostResponse2.getResponse();

			uniCertSession.put("SSRFee", cbIbk01H0060100Res2.getSSRFee());
			uniCertSession.put("SSRVat", cbIbk01H0060100Res2.getSSRVat());

			response.setSsrFee(cbIbk01H0060100Res2.getSSRFee());		//수수료
			response.setSsrVat(cbIbk01H0060100Res2.getSSRVat());		//부가가치세
		}

		uniCertSession.put("CompanyName", request.getCompanyName());
		uniCertSession.put("EngName", request.getEngName());
		uniCertSession.put("oppResult", oppResult);
		sessionManager.setGlobalValue("uniCertSession", uniCertSession);

		response.setSgRefNo((String) oppResult.get("SGRefNo"));			//참조번호
		response.setSgAuthCode((String) oppResult.get("SGAuthCode"));	//인가코드
		response.setSgSsrGubun(sgSSRGubun);								//수수료징구구분
		response.setTelNo(telCode);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	/**
	 * 수수료 납부 구분[payGubun]에 따라 다른 처리
	 * 1 : 당행계좌인출 케이스인 경우 수수료 납부 예비거래
	 * 2 : 한국정보인증결제 케이스인 경우 정보인증에서 수수료 납부 및 RA인증서 발급 거래
	 * @throws Exception
	 */
	@ServiceEndpoint(url = "/payUniCertFeePre", name = "범용인증서 발급 수수료 납부 예비거래")
	public CrtUctPayUniCertFeePreResponse payUniCertFeePre(IServiceContext serviceContext, CrtUctPayUniCertFeePreRequest request) throws Exception {
		// ASIS :: H00607 [procFee(), CertTask.KICA_reissue_step04(), CertTaskUtil.KICA_CertIssue()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String,Object> uniCertSession = certUtils.getCertSession("uniCertSession");
		Map<String, Object> oppResult = (Map<String, Object>) uniCertSession.get("oppResult");

		String personOrCompanyCode = (String) uniCertSession.get("PersonOrCompanyCode");
		String custGubun = personOrCompanyCode.equals("1") ? "1" : "2";
		String payGubun = request.getPayGubun();		// 수수료 납부 구분(1:당행계좌인출, 2:한국정보인증결제)

		// 응답 객체
		CrtUctPayUniCertFeePreResponse response = new CrtUctPayUniCertFeePreResponse();

		// 수수료 납부 구분에 따른 처리 START
		if(payGubun.equals("1")) {
			// 당행계좌인출 START
			log.debug("payUniCertFeePre 당행계좌인출 START");

			// 세금계산서 여부 및 메일발송 전문 처리 START
			log.debug("payUniCertFeePre 세금계산서 여부 및 메일발송 예비거래 START");
			OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H981");

			// 공통부 세팅
			hostRequestOptions.setImsTranCd("TI1IBK01");
			hostRequestOptions.setInClassCd("H981");
			hostRequestOptions.setSvcCd("981");

			// 개별부 세팅
			CbIbk01H98100Req cbIbk01H98100Req = new CbIbk01H98100Req();
			cbIbk01H98100Req.setCustGubun(custGubun); 		// 발급자구분(1:개인,2:기업)
			cbIbk01H98100Req.setCAGubun("3"); 		// 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
			cbIbk01H98100Req.setRAGubun("9");		//인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))

			cbIbk01H98100Req.setUserID((String) uniCertSession.get("UserID"));
			cbIbk01H98100Req.setSafeCardKind((String) uniCertSession.get("SafeCardKind"));
			cbIbk01H98100Req.setSSRFee((String) uniCertSession.get("SSRFee"));
			cbIbk01H98100Req.setSSRVat((String) uniCertSession.get("SSRVat"));
			cbIbk01H98100Req.setRegNo((String) uniCertSession.get("RegNo"));

			if("1".equals(custGubun)) {
				cbIbk01H98100Req.setJuminSaupjaNo((String) uniCertSession.get("RegNo"));
			} else {
				cbIbk01H98100Req.setJuminSaupjaNo((String) uniCertSession.get("SaupjaNo"));
			}

			String[] telCode = certUtils.phoneNumberPartArray((String) uniCertSession.get("TelCode"));
			cbIbk01H98100Req.setTelCode1(telCode[0]);
			cbIbk01H98100Req.setTelCode2(telCode[1]);
			cbIbk01H98100Req.setTelCode3(telCode[2]);
			cbIbk01H98100Req.setTeleOne(telCode[0]);
			cbIbk01H98100Req.setTeleTwo(telCode[1]);
			cbIbk01H98100Req.setTeleThree(telCode[2]);

			cbIbk01H98100Req.setSSRAcctNum(request.getAcctNum());
			cbIbk01H98100Req.setAcctPasswd(request.getAcctBb());

			uniCertSession.put("AcctBb", request.getAcctBb());
			sessionManager.setGlobalValue("uniCertSession", uniCertSession);

			// 전문 전송
			this.hostClient.sendOltp(hostRequestOptions, cbIbk01H98100Req, CbIbk01H98100Res.class);
			log.debug("payUniCertFeePre 세금계산서 여부 및 메일발송 예비거래 END");
			// 세금계산서 여부 및 메일발송 전문 처리 END

			log.debug("payUniCertFeePre 당행계좌인출 END");
			// 당행계좌인출 END
		} else if (payGubun.equals("2")) {
			// 한국정보인증결제 START
			log.debug("payUniCertFeePre 한국정보인증결제 START");

			// 정보인증 전문 처리 START
			log.debug("payUniCertFeePre 한국정보인증 인증서 발급 START");
			OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

			// 공통부 세팅
			hostRequestOptions.setImsTranCd("TI1IBK01");
			hostRequestOptions.setInClassCd("D006");
			hostRequestOptions.setSvcCd("006");

			// 개별부 세팅
			CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
			cbIbk01D00600Req.setCustGubun(custGubun); 		// 발급자구분(1:개인,2:기업)
			cbIbk01D00600Req.setCertTranCode("1");	// 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
			cbIbk01D00600Req.setChuryGubun("S");		// 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
			cbIbk01D00600Req.setCAGubun("3"); 		// 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
			cbIbk01D00600Req.setRAGubun("9");		//인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
			cbIbk01D00600Req.setJoinLoad("105"); 	// 가입경로

			cbIbk01D00600Req.setUserID((String) uniCertSession.get("UserID"));
			cbIbk01D00600Req.setTSPassword((String) uniCertSession.get("TSPassword"));
			cbIbk01D00600Req.setInforGubun((String) uniCertSession.get("InforGubun"));
			cbIbk01D00600Req.setSafeCardKind((String) uniCertSession.get("SafeCardKind"));
			cbIbk01D00600Req.setYIAGREE("Y");
			cbIbk01D00600Req.setRegNo((String) uniCertSession.get("RegNo"));
			cbIbk01D00600Req.setEmailAddr((String) uniCertSession.get("EmailAddr"));

			// 한국정보인증. 인증서발급. 정보인증에서 수수료 납부 및 RA인증서 발급
			log.debug("payUniCertFeePre 한국정보인증 수수료 납부 및 RA 인증서 발급 START");

			cbIbk01D00600Req.setTelCode((String) uniCertSession.get("TelCode"));
			if(personOrCompanyCode.equals("2")) {
				cbIbk01D00600Req.setJuminSaupjaNo((String) uniCertSession.get("SaupjaNo"));
			} else {
				cbIbk01D00600Req.setJuminSaupjaNo((String) uniCertSession.get("RegNo"));
			}

			// 전문 전송
			this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
			log.debug("payUniCertFeePre 한국정보인증 인증서 발급 END");
			// 정보인증 END

//			CertTaskUtil.KICA_CertIssue
			String policyId = "";
			String raRegCase = (String) oppResult.get("RA_REG_CASE");

			Map<String, String> sgrartn = null;
			sgrartn = kicaCertComponent.issueCert(2, uniCertSession, raRegCase, personOrCompanyCode);

			oppResult.put("SGRefNo", sgrartn.get("SGRefNo"));
			oppResult.put("SGAuthCode", sgrartn.get("SGAuthCode"));
			oppResult.put("SGDeptPersonName", sgrartn.get("SGDeptPersonName"));
			oppResult.put("SGSSRGubun", "4");

			response.setSgRefNo((String) oppResult.get("SGRefNo"));
			response.setSgAuthCode((String) oppResult.get("SGAuthCode"));

			log.debug("payUniCertFeePre 한국정보인증 수수료 납부 및 RA 인증서 발급 END");
			log.debug("payUniCertFeePre 한국정보인증결제 END");
			// 한국정보인증결제 END
		}
		// 수수료 납부 구분에 따른 처리 END

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/payUniCertFee", name = "범용인증서 발급 수수료 납부")
	public CrtUctPayUniCertFeeResponse payUniCertFee(IServiceContext serviceContext, CrtUctPayUniCertFeeRequest request) throws Exception {
		// ASIS :: H00607 [transferFee(), CertTask.KICA_reissue_step03()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String,Object> uniCertSession = certUtils.getCertSession("uniCertSession");
		Map<String, Object> oppResult = (Map<String, Object>) uniCertSession.get("oppResult");

		String personOrCompanyCode = (String) uniCertSession.get("PersonOrCompanyCode");
		String custGubun = personOrCompanyCode.equals("1") ? "1" : "2";
		String feePayIssueYn = request.getFeePayIssueYn();		// 계산서 발급 여부

		uniCertSession.put("TypesOfIndustry", request.getTypesOfIndustry());	// 업종
		uniCertSession.put("Item", request.getItem());	// 종목
		uniCertSession.put("ChairManName", request.getChairManName());	// 대표자명

		// 응답 객체
		CrtUctPayUniCertFeeResponse response = new CrtUctPayUniCertFeeResponse();

		// 당행계좌인출 선택 시 수수료 이체 START
		log.debug("payUniCertFee 당행계좌인출 수수료 이체 START");

		// 세금계산서 여부 및 메일발송 전문 처리 START
		log.debug("payUniCertFee 세금계산서 여부 및 메일발송 본거래 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D98A");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D98A");
		hostRequestOptions.setSvcCd("981");

		// 개별부 세팅
		CbIbk01H98100Req cbIbk01H98100Req = new CbIbk01H98100Req();
		cbIbk01H98100Req.setCustGubun(custGubun); 		// 발급자구분(1:개인,2:기업)
		cbIbk01H98100Req.setCAGubun("3"); 		// 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H98100Req.setRAGubun("9");		//인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01H98100Req.setSecurityIndex((String) uniCertSession.get("SecurityIndex"));

		cbIbk01H98100Req.setUserID((String) uniCertSession.get("UserID"));
		cbIbk01H98100Req.setSafeCardKind((String) uniCertSession.get("SafeCardKind"));
		cbIbk01H98100Req.setSSRFee((String) uniCertSession.get("SSRFee"));
		cbIbk01H98100Req.setSSRVat((String) uniCertSession.get("SSRVat"));
		cbIbk01H98100Req.setRegNo((String) uniCertSession.get("RegNo"));

		cbIbk01H98100Req.setTypesOfIndustry((String) uniCertSession.get("TypesOfIndustry"));
		cbIbk01H98100Req.setItem((String) uniCertSession.get("Item"));

		// 세금계산서 발급구분
		if("Y".equals(feePayIssueYn)) {
			cbIbk01H98100Req.setIssueReceipt("1");
		} else {
			cbIbk01H98100Req.setIssueReceipt("0");
		}

		if("1".equals(custGubun)) {
			cbIbk01H98100Req.setJuminSaupjaNo((String) uniCertSession.get("RegNo"));
		} else {
			cbIbk01H98100Req.setJuminSaupjaNo((String) uniCertSession.get("SaupjaNo"));
		}

		String[] telCode = certUtils.phoneNumberPartArray((String) uniCertSession.get("TelCode"));
		cbIbk01H98100Req.setTelCode1(telCode[0]);
		cbIbk01H98100Req.setTelCode2(telCode[1]);
		cbIbk01H98100Req.setTelCode3(telCode[2]);
		cbIbk01H98100Req.setTeleOne(telCode[0]);
		cbIbk01H98100Req.setTeleTwo(telCode[1]);
		cbIbk01H98100Req.setTeleThree(telCode[2]);

		cbIbk01H98100Req.setSSRAcctNum(request.getAcctNum());
		cbIbk01H98100Req.setAcctPasswd((String) uniCertSession.get("AcctBb"));

		IpinsideHddInfo ipinsideHddInfo = new IpinsideHddInfo();

        if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode()) // 로컬 Ipinsid 방화벽 문제로 SKIP
                && !"Y".equals(PropertiesUtils.getString("PARSE_IPINSIDE_SKIP")) // FDS DB 관련 SKIP 여부
        ) {
            // Ipinside mac/ip 정보 조회
            ipinsideHddInfo = ipinsideComponent.getIpinsidInfo();
        }
		String ipinsideIp = StringUtils.defaultIfBlank(PRCSharedUtils.getIpinsideIp(), "");
		String ipUtilIp = StringUtils.defaultIfBlank(IpUtils.getClientIp(), "000.000.000.000");
		String hddMac = StringUtils.defaultIfBlank(ipinsideHddInfo.getMacAddress(), "");
        String ipinsideMac = StringUtils.defaultIfBlank(PRCSharedUtils.getIpinsideMac(), "MACADDRESSNONE");
        String macAddress = !hddMac.isBlank()
                ? hddMac
                : ipinsideMac.replaceAll(":", "").replaceAll("-", "");

        if (macAddress.length() > 20) {
            macAddress = macAddress.substring(0, 20);
        }
        cbIbk01H98100Req.setYIMAC(macAddress);
        cbIbk01H98100Req.setYIIPN(!ipinsideIp.isBlank() ? ipinsideIp : ipUtilIp);

		// 한국정보인증. 인증서 발급. 제일은행 수수료 납부 및 RA인증서 발급 START
		log.debug("payUniCertFee 제일은행 수수료 납부 및 RA 인증서 발급 START");

		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions, cbIbk01H98100Req, CbIbk01H98100Res.class);
		log.debug("payUniCertFee 세금계산서 여부 및 메일발송 본거래 END");
		// 세금계산서 여부 및 메일발송 전문 처리 END

//		CertTaskUtil.KICA_CertIssue
		String policyId = "";
		String raRegCase = (String) oppResult.get("RA_REG_CASE");

		Map<String, String> sgrartn = null;
		sgrartn = kicaCertComponent.issueCert(1, uniCertSession, raRegCase, personOrCompanyCode);

		// 필요없어보임
//		oppResult.put("SGRefNo", sgrartn.get("SGRefNo"));
//		oppResult.put("SGAuthCode", sgrartn.get("SGAuthCode"));
//		oppResult.put("SGDeptPersonName", sgrartn.get("SGDeptPersonName"));
//		oppResult.put("SGSSRGubun", "3");
//		oppResult.put("SSRFee", (String) uniCertSession.get("SSRFee"));
//		oppResult.put("SSRVat", (String) uniCertSession.get("SSRVat"));

		response.setSgRefNo(sgrartn.get("SGRefNo"));
		response.setSgAuthCode(sgrartn.get("SGAuthCode"));

		log.debug("payUniCertFee 제일은행 수수료 납부 및 RA 인증서 발급 END");
		// 한국정보인증. 인증서 발급. 제일은행 수수료 납부 및 RA인증서 발급 END

		log.debug("payUniCertFee 당행계좌인출 수수료 이체 END");
		// 당행계좌인출 선택 시 수수료 이체 END

		// 세션 삭제
		sessionManager.removeGlobalValue("uniCertSession");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

}