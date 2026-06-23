package com.scbank.process.api.svc.common.service.certification;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89201Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89201Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92203Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92203Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98000Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98100Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H98100Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H98200Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H98200Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.dao.LdapInfoDao;
import com.scbank.process.api.svc.common.dao.SmtPhoneBankingVsnMgtDao;
import com.scbank.process.api.svc.common.dao.dto.InfoOtherResult;
import com.scbank.process.api.svc.common.dao.dto.LdapInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SmtPhoneBankingInfoResult;
import com.scbank.process.api.svc.common.service.certification.dto.joint.ChargeInfo;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRenewRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRenewResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRestoreRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRestoreResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertSuspensionRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeCertSuspensionResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeOtherCertDelRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeOtherCertDelResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeOtherCertRegRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeOtherCertRegResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeUniCertRefundRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctAuthorizeUniCertRefundResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctConfirmCertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctConfirmCertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCertAuthPhoneRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCertAuthPhoneResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCertRefNumRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCertRefNumResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCurrentAppVersionRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCurrentAppVersionResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCurrentSecurityStatusRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetCurrentSecurityStatusResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetOtherCertStatusRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetOtherCertStatusResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetUniCertRefundRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctGetUniCertRefundResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctPayCertRenewPreRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctPayCertRenewPreResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctPayCertRenewRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctPayCertRenewResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctRequestCertTaxInvoiceRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctRequestCertTaxInvoiceResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRenewCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRenewCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRenewSecurityRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRenewSecurityResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreAcctRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreAcctResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreSecurityRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreSecurityResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRestoreUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRevokeAcctRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRevokeAcctResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRevokeUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertRevokeUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionAcctRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionAcctResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionSecurityRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionSecurityResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateCertSuspensionUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertDelRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertDelResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertRegCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertRegCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertRegUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateOtherCertRegUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateUniCertRefundRequest;
import com.scbank.process.api.svc.common.service.certification.dto.joint.CrtJctValidateUniCertRefundResponse;
import com.scbank.process.api.svc.common.service.certification.dto.joint.OpprJdp;
import com.scbank.process.api.svc.shared.components.cert.CertConfig;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.CertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.CertificateHelper;
import com.scbank.process.api.svc.shared.components.cert.KftcCertComponent;
import com.scbank.process.api.svc.shared.components.cert.OtherCertComponent;
import com.scbank.process.api.svc.shared.components.cert.dto.OppraCertInfo;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.EmailUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.RandomKeyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-인증센터-공동인증서", url = "/certification/joint")
public class CertificationJointService {

	// 전문
	private final HostClient hostClient;

	// 세션
	private final ISessionContextManager sessionManager;

	// 인증센터 공통
	private final CertUtils certUtils;
	private final OtherCertComponent otherCertComponent;
	private final KftcCertComponent kftcCertComponent;
	private final CertVerifyComponent certVerifyComponent;
	private final CertificationSharedComponent certificationSharedComponent;

	// DBIO
	private final LdapInfoDao ldapInfoDao;
	private final SmtPhoneBankingVsnMgtDao smtPhoneBankingVsnMgtDao;

	// IPINSIDE
	private final IpinsideComponent ipinsideComponent;

	private final SmsComponent smsComponent;

	@ServiceEndpoint(url = "/validateCertIssue", name = "공동인증서 발급 본인확인")
	public CrtJctValidateCertIssueResponse validateCertIssue(IServiceContext serviceContext, CrtJctValidateCertIssueRequest request) {
		// ASIS :: MA3CRTCRT001_101S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");

		// 응답 객체
		CrtJctValidateCertIssueResponse response = new CrtJctValidateCertIssueResponse();

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 본인확인 전문 처리
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
		cbIbk01H00601Req.setCustJumin1((String) certIssueSession.get("CustJumin1"));
		cbIbk01H00601Req.setCustJumin2((String) certIssueSession.get("CustJumin2"));
		cbIbk01H00601Req.setSSRAcctNum(request.getAcctNum());
		cbIbk01H00601Req.setAcctPasswd(request.getAcctBb());
		cbIbk01H00601Req.setSimplifyGB("1"); // 인증서간소화 체크
		cbIbk01H00601Req.setCertTranCode("1"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		cbIbk01H00601Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00601Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		cbIbk01H00601Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00601Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00601Req, CbIbk01H00601Res.class);
		CbIbk01H00601Res cbIbk01H00601Res = hostResponse.getResponse();
		log.debug("validateCertIssue cbIbk01H006Res :: {}", cbIbk01H00601Res);

		// 개인정보 노출자 여부 판단
		log.debug("validateCertIssue 개인정보 노출자 여부 판단 시작");
		Map<String, String> resH504Map = certificationSharedComponent.getYoLRSOTGB(cbIbk01H00601Res.getRegNo());
		String yoLRSOTGB = resH504Map.get("YOLRSOTGB");
		log.debug("validateCertIssue resH504Map :: {}", resH504Map);

		// 개인정보노출등록구분 값이 1이면 거래불가 고객으로 안내페이지로 이동
		if ("1".equalsIgnoreCase(yoLRSOTGB)) {
			sessionManager.removeGlobalValue("certIssueSession");
			sessionManager.removeGlobalValue("PerBusNo");
			sessionManager.removeGlobalValue("CustName");

			response.setYoLRSOTGB(yoLRSOTGB); // 개인정보 노출자 여부 판단값(1:차단)
			return response;
		}
		log.debug("validateCertIssue 개인정보 노출자 여부 판단 종료");

		sessionManager.setGlobalValue("CustName", cbIbk01H00601Res.getDeptPersonName());

		// 전화번호
		String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00601Res.getTelCode());
		String hpOne = telCode[0];
		String hpTwo = telCode[1];
		String hpThree = telCode[2];
		String phoneNo = telCode[3];

		// 이메일
		String email = cbIbk01H00601Res.getEmailAddr();
		String email1 = "";
		String email2 = "";
		if (email.indexOf("@") > 0) {
			email1 = email.split("@")[0];
			email2 = email.split("@")[1];
		}

		String personOrCompanyCode = cbIbk01H00601Res.getPersonOrCompanyCode();
		String userId = cbIbk01H00601Res.getUserID();

		String oppUserId = "";
		String deptPersonName = "";

		// 올바른 사용자
		if ("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode) || "3".equals(personOrCompanyCode)) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H00601Res.getDeptPersonName(), 9);
			deptPersonName = deptPersonName + "()";
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// 조합번호용 인증서
		// 여권조합번호 사용자일 경우 팝업 안내
		if ("3".equals(cbIbk01H00601Res.getYOCFJMGB())) {
			String policy = "04";
			List<LdapInfoResult> certList = ldapInfoDao.selectForDelete(policy, userId);

			// RA서버에서 04 기발급 항목이 있으면 재발급 거래이므로 폐기 안내 (combPopType: 71)
			// 04 기발급 항목이 없으면 신규 거래이므로 조합번호용 공동인증서 발급 안내 (combPopType: 70)
			String combPopType = certList.size() > 0 ? "71" : "70";
			response.setCombPopType(combPopType); // 여권조합번호 사용자일 경우 팝업 안내 타입
			log.debug("validateCertIssue combPopType :: {}", combPopType);
		}

		// RA사용자 조회
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		if ("3".equals(cbIbk01H00601Res.getPersonOrCompanyCode())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		if ("13".equals(cbIbk01H00601Res.getYOIDGB()) || "14".equals(cbIbk01H00601Res.getYOIDGB())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		// 본인 인증한 데이터 세션에 저장
		certIssueSession.put("UserID", cbIbk01H00601Res.getUserID());
		certIssueSession.put("TSPassword", cbIbk01H00601Res.getTSPassword());
		certIssueSession.put("RegNo", cbIbk01H00601Res.getRegNo());
		certIssueSession.put("PersonOrCompanyCode", cbIbk01H00601Res.getPersonOrCompanyCode());
		certIssueSession.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		certIssueSession.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		certIssueSession.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
		certIssueSession.put("DeptPersonName", cbIbk01H00601Res.getDeptPersonName());
		certIssueSession.put("SaupjaNo", cbIbk01H00601Res.getSaupjaNo());
		certIssueSession.put("ZipCode", cbIbk01H00601Res.getZipCode());
		certIssueSession.put("Address", cbIbk01H00601Res.getAddress());
		certIssueSession.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
		certIssueSession.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
		certIssueSession.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
		certIssueSession.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
		certIssueSession.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
		certIssueSession.put("AuthSVC4CERT", cbIbk01H00601Res.getAuthSVC4CERT());
		certIssueSession.put("InforGubun", cbIbk01H00601Res.getInforGubun());
		certIssueSession.put("oppraResult", oppraResult); // RA정보
		certIssueSession.put("RealNameIDType", cbIbk01H00601Res.getYOCFJMGB()); // 실명증표구분 - 1:주민등록번호 2:사업자번호 3:여권번호 4:외국인등록번호 5:임의단체대표자주민번호 6:납세고유번호

		sessionManager.setGlobalValue("certIssueSession", certIssueSession);
		sessionManager.setGlobalValue("PerBusNo", cbIbk01H00601Res.getRegNo());

		// 금결원 범용인증서 재발급 가능 여부 판단(0:불가능, 1:가능)
		String isFinancial = "0";
		String isPFinancial = (String) oppraResult.get("IS_P_FINANCIAL");
		if (("1".equals(personOrCompanyCode) || "2".equals(personOrCompanyCode)) && "YES".equals(isPFinancial)) {
			isFinancial = "1";
		}
		log.debug("validateCertIssue oppraResult :: {}", oppraResult.toString());
		log.debug("validateCertIssue isFinancial :: {}", isFinancial);

		response.setIsFinancial(isFinancial); // 금결원 범용인증서 재발급 가능 여부
		response.setYoAGREEGB(cbIbk01H00601Res.getYOAGREEGB()); // 온라인 발급 사전동의여부
		response.setEmail1(email1);
		response.setEmail2(email2);
		response.setCustName(cbIbk01H00601Res.getDeptPersonName());
		response.setHpOne(hpOne);
		response.setHpTwo(hpTwo);
		response.setHpThree(hpThree);
		response.setPhoneNo(phoneNo);
		response.setEmail1(email1);
		response.setEmail2(email2);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getCertRefNum", name = "공동인증서 참조번호 발급")
	public CrtJctGetCertRefNumResponse getCertRefNum(IServiceContext serviceContext, CrtJctGetCertRefNumRequest request) {
		// ASIS :: MA3CRTCRT001_301S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");
		Map<String, Object> oppraResult = (Map<String, Object>) certIssueSession.get("oppraResult");

		// 발급인가 전문 처리 START
		log.debug("getCertRefNum 발급인가 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certIssueSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setRegNo((String) certIssueSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setTSPassword((String) certIssueSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setEmailAddr((String) certIssueSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setJuminSaupjaNo((String) certIssueSession.get("RegNo")); // 주민사업자번호
		cbIbk01D00600Req.setCertTranCode("1"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("S"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		String raGubun = StringUtils.nvl(request.getRaGubun(), "1");
		cbIbk01D00600Req.setRAGubun(raGubun); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostH006Response.getResponse();
		log.debug("getCertRefNum 발급인가 END");

		String issueGubun = cbIbk01D00600Res.getIssueGubun(); // 발급구분(1:신규, 2:재발급)
		String issuerType = "p_personal"; // 개인사용자
		String userType = "P";
		if ("2".equals(certIssueSession.get("PersonOrCompanyCode"))) { // 개인 사업자
			issuerType = "pc_personal"; // 개인사용자
			userType = "PP";
		}

		String certpolicy = "FINANCIAL_RESTRICT"; // 기업용 : FINANCIAL, BUSINESS, TAX | 개인용 FINANCIAL_RESTRICT, FINANCIAL
		if ("2".equals(raGubun)) {
			certpolicy = "FINANCIAL";
		}

		String certPolicyCode = "04";
		log.debug("getCertRefNum RealNameIDType :: {}", certIssueSession.get("RealNameIDType"));

		/**
		 * 실명증표구분값에 따른 처리 CertPolicyCode (발급자 식별코드) - 84 : 여권번호 사용 외국인 - 04 : 개인
		 */
		certPolicyCode = "3".equals(certIssueSession.get("RealNameIDType")) ? "84" : "04";
		if ("2".equals(raGubun)) {
			certPolicyCode = "01";
		}

		log.debug("getCertRefNum CertPolicyCode :: {}", certPolicyCode);

		String isPFinancial = (String) oppraResult.get("IS_P_FINANCIAL");
		String pFStatus = (String) oppraResult.get("P_FINANCIAL_STATUS");
		String pFinancialSerial = (String) oppraResult.get("P_FINANCIAL_SERIAL");
		String expireDate = (String) cbIbk01D00600Res.getExpireDate(); // 만료일
		String ssrGubun = (String) cbIbk01D00600Res.getSSRGubun(); // 수수료구분
		String isPFinancialRestrict = (String) oppraResult.get("IS_P_FINANCIAL_RESTRICT");
		String pFrStatus = (String) oppraResult.get("P_FINANCIAL_RESTRICT_STATUS");
		String pFinancialRestrictSerial = (String) oppraResult.get("P_FINANCIAL_RESTRICT_SERIAL");
		String PersonOrCompanyCode = (String) certIssueSession.get("PersonOrCompanyCode");
		String isCFinancial = (String) oppraResult.get("IS_C_FINANCIAL");
		String cFStatus = (String) oppraResult.get("C_FINANCIAL_STATUS");
		String bStatus = (String) oppraResult.get("BUSINESS_STATUS");

		/*
		 * 해당이용자의 해당 인증정책에 대해 효력정지된 상태이거나, 개인이 타행에 유효한 인증서가 발급되어 있다면 인증서발급불가 신규인 경우만 유효하거나 폐지된 인증서 있는 경우 에러 처리함.
		 */
		if ("1".equals(issueGubun)) {

			if (issuerType.equals("p_personal") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("VALID") && certpolicy.equals("FINANCIAL_RESTRICT")) {
				// 유효한 개인 금융 용도제한용 공인인증서를 발급받은 상태.
				log.debug("getCertRefNum hostSendAndCheckSSR : 유효한 개인 금융 용도제한용 공인인증서를 발급받은 상태");
				Map<String, String> rtn = null;
				try {
					rtn = certUtils.checkCertStatus(pFinancialRestrictSerial, certPolicyCode);
				} catch (Exception e) {
					e.printStackTrace();
					throw new PRCServiceException("PRCCRT201123", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.[01]");
				}

				if (rtn.get("SUCCESS").equals("FALSE")) {
					throw new PRCServiceException("PRCCRT20136", "고객께서는 이미 유효한 개인 금융 용도제한용 공인인증서를 발급받은 상태입니다.");
				}

				expireDate = rtn.get("ExpireDate"); // 만료일.
				issueGubun = rtn.get("IssueGubun"); // 발급 구분 (1:신규, 2:재발급).
				ssrGubun = rtn.get("SSRGubun"); // 수수료 구분 (1:징구, 2:미징구).

			} else if (issuerType.equals("p_personal") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("SUSPEND") && certpolicy.equals("FINANCIAL_RESTRICT")) {
				// 이미 개인 고객의 금융 용도제한용 효력정지된 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR : 이미 개인 고객의 금융 용도제한용 효력정지된 인증서가 존재");
				throw new PRCServiceException("PRCCRT20137", "고객께서는 이미 유효한 개인 금융 용도제한용 공인인증서를 발급받았으며, 현재 효력정지된 상태입니다.");

			} else if (issuerType.equals("pc_personal") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("VALID") && certpolicy.equals("FINANCIAL_RESTRICT")) {
				// 이미 개인사업자의 금융 용도제한용 유효한 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR : 이미 개인사업자의 금융 용도제한용 유효한 인증서가 존재");
				Map<String, String> rtn = null;
				try {
					rtn = certUtils.checkCertStatus(pFinancialRestrictSerial, certPolicyCode);
				} catch (Exception e) {
					e.printStackTrace();
					throw new PRCServiceException("PRCCRT201123", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.[02]");
				}

				if (rtn.get("SUCCESS").equals("FALSE")) {
					throw new PRCServiceException("PRCCRT20138", "고객께서는 이미 유효한 개인사업자 금융 용도제한용 공인인증서를 발급받은 상태입니다.");
				}

				expireDate = rtn.get("ExpireDate"); // 만료일.
				issueGubun = rtn.get("IssueGubun"); // 발급 구분 (1:신규, 2:재발급).
				ssrGubun = rtn.get("SSRGubun"); // 수수료 구분 (1:징구, 2:미징구).

			} else if (issuerType.equals("pc_personal") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("SUSPEND") && certpolicy.equals("FINANCIAL_RESTRICT")) {
				// 이미 개인사업자의 금융 용도제한용 효력정지된 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR : 이미 개인사업자의 금융 용도제한용 효력정지된 인증서가 존재");
				throw new PRCServiceException("PRCCRT20139", "고객께서는 이미 유효한 개인사업자 금융 용도제한용 공인인증서를 발급받았으며, 현재 효력정지된 상태입니다.");

			} else if (issuerType.equals("p_personal") && isPFinancial.equals("YES") && pFStatus.equals("VALID") && certpolicy.equals("FINANCIAL")) {
				// 유효한 개인 범용 공인인증서를 발급받은 상태.
				Map<String, String> rtn = null;
				try {
					log.debug("getCertRefNum hostSendAndCheckSSR : 유효한 개인 범용 공동인증서를 발급받은 상태. :: {}", pFinancialSerial);
					certPolicyCode = "01";
					rtn = certUtils.checkCertStatus(pFinancialSerial, certPolicyCode);
				} catch (Exception e) {
					throw new PRCServiceException("PRCCRT201123", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.[03]");
				}
				log.debug("rtn.SUCCESS :: {}", rtn.get("SUCCESS"));

				if (rtn.get("SUCCESS").equals("FALSE")) {
					throw new PRCServiceException("PRCCRT20125", "고객께서는 이미 유효한 개인 금융용 공인인증서를 발급받은 상태입니다.");
				}

				expireDate = rtn.get("ExpireDate"); // 만료일.
				issueGubun = rtn.get("IssueGubun"); // 발급 구분 (1:신규, 2:재발급).
				ssrGubun = rtn.get("SSRGubun"); // 수수료 구분 (1:징구, 2:미징구).

			} else if (issuerType.equals("p_personal") && isPFinancial.equals("YES") && pFStatus.equals("SUSPEND") && certpolicy.equals("FINANCIAL")) {
				// 이미 개인 고객의 범용 효력정지된 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR :: 이미 개인 고객의 범용 효력정지된 인증서가 존재.");
				throw new PRCServiceException("PRCCRT20126", "고객께서는 이미 유효한 개인 금융용 공인인증서를 발급받았으며, 현재 효력정지된 상태입니다.");

			} else if (issuerType.equals("pc_personal") && isPFinancial.equals("YES") && pFStatus.equals("VALID") && certpolicy.equals("FINANCIAL")) {
				// 이미 개인사업자의 범용 유효한 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR : 이미 개인사업자의 범용 유효한 인증서가 존재. {}", pFinancialSerial);
				HashMap<String, String> rtn = null;
				try {
					certPolicyCode = "01";
					rtn = certUtils.checkCertStatus(pFinancialSerial, certPolicyCode);
				} catch (Exception e) {
					log.debug("범용인증서 에러 메세지 출력");
					throw new PRCServiceException("PRCCRT201123", "공인인증 등록기관의 시스템 점검중입니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.[04]");
				}
				if (rtn.get("SUCCESS").equals("FALSE")) {
					log.debug("getCertRefNum 타행에서 발급받은 범용인증서 에러 메세지 출력");
					throw new PRCServiceException("PRCCRT20127", "고객께서는 이미 유효한 개인사업자 금융용 공인인증서를 발급받은 상태입니다.");
				}

				expireDate = rtn.get("ExpireDate"); // 만료일.
				issueGubun = rtn.get("IssueGubun"); // 발급 구분 (1:신규, 2:재발급).
				ssrGubun = rtn.get("SSRGubun"); // 수수료 구분 (1:징구, 2:미징구).

			} else if (issuerType.equals("pc_personal") && isPFinancial.equals("YES") && pFStatus.equals("SUSPEND") && certpolicy.equals("FINANCIAL")) {
				// 이미 개인사업자의 범용 효력정지된 인증서가 존재
				log.debug("getCertRefNum hostSendAndCheckSSR : 이미 개인사업자의 범용 효력정지된 인증서가 존재.");
				throw new PRCServiceException("PRCCRT20128", "고객께서는 이미 유효한 개인사업자 금융용 공인인증서를 발급받았으며, 현재 효력정지된 상태입니다.");
			}
		}
		log.debug("getCertRefNum IssueGubun ra :: {}", issueGubun);

		// 참조번호, 인가 코드 센터로부터 받아옴 시작
		// 등록 메세지 작성
		String phoneNum = request.getTel1() + request.getTel2() + request.getTel3();
		String tmpPhoneNum = ""; // 임시 번호
		String phoneNumCell = ""; // 핸드폰번호
		String phoneNumHome = ""; // 자택전화번호
		if (phoneNum != null) {
			tmpPhoneNum = phoneNum.substring(0, 3);
			if (tmpPhoneNum.equals("010") || tmpPhoneNum.equals("011") || tmpPhoneNum.equals("016") || tmpPhoneNum.equals("017") || tmpPhoneNum.equals("018") || tmpPhoneNum.equals("019")) {
				phoneNumCell = phoneNum; // 핸드폰번호
			} else {
				phoneNumHome = phoneNum; // 자택전화번호
			}
		}

		Map<String, Object> raMap = new HashMap<>();
		raMap.put("MANAGERID", CertConfig.ADID); // 운영자아아디. OPTIONAL 속성
		raMap.put("USERCODE", "1"); // 가입자구분코드 (1:개인, 2:기업)
		raMap.put("OU_NAME", ""); // 개인은 법인명이 OPTIONAL
		raMap.put("CN_NAME", oppraResult.get("DeptPersonDetailName")); // 개인명, 법인/단체 세부명 "()"가 뒤에 포함되어야함. ex:) "홍길동()" 기업일 경우 "이니텍(INI)" ()안에 법인명이 들어감
		raMap.put("IDNO", certIssueSession.get("RegNo")); // 주민(사업자)등록번호
		raMap.put("USERID", certIssueSession.get("UserID")); // 사이트아이디
		raMap.put("SERVICEPROVIDER", "01"); // 01:금결원OPP(또는 범용게이트웨이), 02:타기관직접연동
		raMap.put("CACODE", "01"); // 01:금결원 02:SignKorea 03:전자인증
		raMap.put("CERTCODE", certPolicyCode); // 01:개인범용 02:기업범용 04:개인(은행/보험) 05:기업범용 84:조합번호용
		raMap.put("EMAIL", certIssueSession.get("EmailAddr")); // 이메일
		raMap.put("HANDPHONE", phoneNumCell); // 휴대폰번호
		raMap.put("FAX", "02-2140-3699"); // 팩스
		raMap.put("POSTCODE", certIssueSession.get("ZipCode")); // 우편번호
		raMap.put("POSTADDR", "1");
		raMap.put("PHONE", phoneNumHome); // 전화
		raMap.put("STATISTICSCODE", "000386"); // for Advance version
		raMap.put("RESERVATION5", "0");

		log.debug("raMap ::: {}", raMap);

		// 인증서 발급/재발급
		Map<String, String> issueMap = kftcCertComponent.issueCert(issueGubun, raMap);

		// issue 실패하면 다 throw 했기 때문에 참조번호, 인가코드 무조건 있다
		String refNum = issueMap.get("refNum");
		String appCode = issueMap.get("appCode");

		log.debug("getCertRefNum [참조번호] {}", refNum);
		log.debug("getCertRefNum [인가코드] {}", appCode);
		// 참조번호, 인가 코드 센터로부터 받아옴 끝

		// 거래 후 추가 로 세션에 올린다
		String issueDate = DateUtils.getCurrentDate();
		String newExpireDate = certUtils.setExpireDate(issueDate, CertConfig.TermGijun, CertConfig.fincertTermPeriod);

		certIssueSession.put("RefNum", refNum);
		certIssueSession.put("AppCode", appCode);
		certIssueSession.put("issuerType", issuerType);
		certIssueSession.put("is_p_Financial_Restrict", isPFinancialRestrict);
		certIssueSession.put("p_frStatus", pFrStatus);
		certIssueSession.put("is_p_Financial", isPFinancial);
		certIssueSession.put("p_fStatus", pFStatus);
		certIssueSession.put("is_c_Financial", isCFinancial);
		certIssueSession.put("c_fStatus", cFStatus);
		certIssueSession.put("bStatus", bStatus);

		certIssueSession.put("YESSIGN_CAIP", CertConfig.YESSIGN_CAIP);
		certIssueSession.put("YESSIGN_CAPORT", CertConfig.YESSIGN_CAPORT);
		certIssueSession.put("YESSIGN_KEYBOARD_SECURE_PROVIDER", CertConfig.YESSIGN_KEYBOARD_SECURE_PROVIDER);
		certIssueSession.put("YESSIGN_CANAME", CertConfig.YESSIGN_CA_NAME);

		if (issueGubun.equals("1") && ssrGubun.equals("1")) { // IssueGubun = '1' and SSRGubun ='1' ==> 신규 및 수수료징구
			certIssueSession.put("ChuryGubun", "S");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", newExpireDate);
			certIssueSession.put("IssueType", "20"); // 발급
		} else if (issueGubun.equals("1") && ssrGubun.equals("2")) { // IssueGubun = '1' and SSRGubun ='2' ==> 신규 및 수수료미징구
			certIssueSession.put("ChuryGubun", "C");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", newExpireDate);
			certIssueSession.put("IssueType", "20"); // 발급
		} else if (issueGubun.equals("2") && ssrGubun.equals("2")) { // IssueGubun = '2' and SSRGubun ='2' ==> 재발급 및 수수료미징구
			certIssueSession.put("ChuryGubun", "C");
			certIssueSession.put("IssueDate", issueDate);
			certIssueSession.put("ExpireDate", expireDate); // 재발급시는 원래 만료일을 그대로 사용.
			certIssueSession.put("IssueType", "25"); // 재발급
		} else { // 여기는 탈수가 없다. 정보이상하니 에러 뿌려주자.....
			throw new PRCServiceException("발급정보 오류");
		}

		// 추가인증 세션값 세팅
		sessionManager.setGlobalValue("UserID", (String) certIssueSession.get("UserID"));
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
		sessionManager.setGlobalValue("TSPassword", String.valueOf(certIssueSession.get("TSPassword")));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		certIssueSession.put("issueDate", issueDate);
		certIssueSession.put("onAgreeChk", request.getOnAgreeChk());
		certIssueSession.put("RAGubun", raGubun);

		log.debug("getCertRefNum certIssueSession : " + certIssueSession);
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		// 응답반환
		CrtJctGetCertRefNumResponse response = new CrtJctGetCertRefNumResponse();
		response.setRefNum(refNum); // 참조번호
		response.setAppCode(appCode); // 인가코드
		response.setIssueType((String) certIssueSession.get("IssueType")); // 인증서 발급구분(20:신규, 25:재발급)
		response.setYesSignCaIp((String) certIssueSession.get("YESSIGN_CAIP"));
		response.setYesSignCaPort((String) certIssueSession.get("YESSIGN_CAPORT"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/authorizeCertIssue", name = "공동인증서 발급")
	public CrtJctAuthorizeCertIssueResponse authorizeCertIssue(IServiceContext serviceContext, CrtJctAuthorizeCertIssueRequest request) {
		// ASIS :: MA3CRTCRT001_401S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");
		Map<String, Object> oppraResult = (Map<String, Object>) certIssueSession.get("oppraResult");

		String teleOne = sessionManager.getGlobalValue("TeleOne", String.class);
		String teleTwo = sessionManager.getGlobalValue("TeleTwo", String.class);
		String teleThree = sessionManager.getGlobalValue("TeleThree", String.class);

		// 보안매체 검증 처리 START
		log.debug("authorizeCertIssue 보안매체 검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certIssueSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certIssueSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setRegNo((String) certIssueSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setCertTranCode("1"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01D00600Req.setEmailAddr((String) certIssueSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setJoinLoad("000"); // 가입경로
		cbIbk01D00600Req.setInforGubun((String) certIssueSession.get("InforGubun")); // 정보코드
		cbIbk01D00600Req.setSafeCardKind((String) certIssueSession.get("SafeCardKind")); // 보안카드종류
		cbIbk01D00600Req.setTelCode(teleOne + teleTwo + teleThree); // 전화번호

		String step3SafeCardKind = (String) certIssueSession.get("SafeCardKind"); // 보안매체 종류
		cbIbk01D00600Req.setSafeCardKind(step3SafeCardKind);// 보안카드 종류

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) certIssueSession.get("SecurityIndex"));// 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) certIssueSession.get("SecurityIndex2"));// 안전카드 INDEX2
		cbIbk01D00600Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드 첫번째 입력값 or OTP 입력값
		cbIbk01D00600Req.setSecurityValue2(" "); // 안전카드값 두번째 입력값

		log.debug("authorizeCertIssue step3_SafeCardKind :: {}", step3SafeCardKind);

		if ("1".equals(step3SafeCardKind)) {	// 보안매체가 안전카드인 경우
			cbIbk01D00600Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1()); // 안전카드일련번호 사용자입력값1
			cbIbk01D00600Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2()); // 안전카드일련번호 사용자입력값2
			cbIbk01D00600Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3()); // 안전카드일련번호 사용자입력값3
			cbIbk01D00600Req.setSafeCardIndex1((String) certIssueSession.get("SafeCardIndex1")); //안전카드일련번호 위치1
			cbIbk01D00600Req.setSafeCardIndex2((String) certIssueSession.get("SafeCardIndex2")); //안전카드일련번호 위치2
			cbIbk01D00600Req.setSafeCardIndex3((String) certIssueSession.get("SafeCardIndex3")); //안전카드일련번호 위치3
			cbIbk01D00600Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드값 두번째 입력값
		}

		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals(step3SafeCardKind)) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		log.debug("authorizeCertIssue 보안매체 검증 END");
		// 보안매체 검증 처리 END

		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctAuthorizeCertIssueResponse();
	}

	@ServiceEndpoint(url = "/confirmCertIssue", name = "공동인증서 발급 완료")
	public CrtJctConfirmCertIssueResponse confirmCertIssue(IServiceContext serviceContext, CrtJctConfirmCertIssueRequest request) {
		// ASIS :: MA3CRTCRT001_501S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certIssueSession = certUtils.getCertSession("certIssueSession");

		// 발급완료 전문 처리 START
		log.debug("confirmCertIssue 발급완료 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certIssueSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setRegNo((String) certIssueSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setTSPassword((String) certIssueSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setCertTranCode("1"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setEmailAddr((String) certIssueSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01D00600Req.setRAGubun((String) certIssueSession.get("RAGubun")); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req.setJuminSaupjaNo((String) certIssueSession.get("RegNo")); // 주민사업자번호
		cbIbk01D00600Req.setIssueBank("023"); // 인증서발급은행(은행코드)
		cbIbk01D00600Req.setIssueDate((String) certIssueSession.get("IssueDate")); // 인증서유효기간시작일
		cbIbk01D00600Req.setExpireDate((String) certIssueSession.get("ExpireDate")); // 인증서유효기간종료일
		cbIbk01D00600Req.setPageGubun((String) certIssueSession.get("PageGubun")); // 페이지구분(1:한글,2:영문)

		// 사전동의 발급 여부
		if ("Y".equals(String.valueOf(certIssueSession.get("onAgreeChk")))) {
			cbIbk01D00600Req.setYIAGREE("Y");
		}

		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		log.debug("confirmCertIssue 발급완료 END");
		// 발급완료 전문 처리 END

		// 메일발송
		String email = (String) certIssueSession.get("EmailAddr");
		String regNo = (String) certIssueSession.get("RegNo");
		String deptPersonName = (String) certIssueSession.get("DeptPersonName");
		String custName = "";
		if (deptPersonName.length() > 4) {
			custName = deptPersonName.substring(0, 4);
		} else {
			custName = deptPersonName;
		}
		if (deptPersonName.length() > 0 && email.length() > 0 && regNo.length() > 0) {
			log.debug("메일발송");
			EmailUtils.sendCompleteMail("71", regNo, deptPersonName, email.trim());
		}

		// SMS발송
		String userId = (String) certIssueSession.get("UserID");
		String tsPassword = (String) certIssueSession.get("TSPassword");
		String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
		String smsMsg = smsCustName + "님 인증서 발급완료. 본인요청 아닐 시 신고요망";
		smsComponent.sendCompleteSMS(userId, tsPassword, regNo, smsMsg);

		// 세션 삭제
		sessionManager.removeGlobalValue("certIssueSession");

		// 추가인증 세션 삭제
		sessionManager.removeGlobalValue("UserID");
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

		// 응답 반환
		CrtJctConfirmCertIssueResponse response = new CrtJctConfirmCertIssueResponse();
		response.setExpireDate((String) certIssueSession.get("ExpireDate"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertSuspensionUser", name = "인증서 효력정지 본인확인")
	public CrtJctValidateCertSuspensionUserResponse validateCertSuspensionUser(IServiceContext serviceContext, CrtJctValidateCertSuspensionUserRequest request) {
		// ASIS :: H00621 [CertTask.sendHostAndOppResult(), func_CellAuth()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("certSuspensionSession");

		// 응답 객체
		CrtJctValidateCertSuspensionUserResponse response = new CrtJctValidateCertSuspensionUserResponse();

		// 본인확인 전문 처리 START
		log.debug("validateCertSuspensionUser 본인확인 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00600Req cbIbk01H00600Req = new CbIbk01H00600Req();
		cbIbk01H00600Req.setUserID(request.getUserId()); // 사용자아이디
		cbIbk01H00600Req.setRegNo(request.getCustJumin1()+request.getCustJumin2());
		cbIbk01H00600Req.setTSPassword(" "); // 통신비밀번호
		cbIbk01H00600Req.setEmailAddr(" "); // 이메일
		cbIbk01H00600Req.setCertTranCode("4"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00600Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00600Req, CbIbk01H00600Res.class);
		CbIbk01H00600Res cbIbk01H006Res = hostResponse.getResponse();
		log.debug("validateCertIssue cbIbk01H006Res :: {}", cbIbk01H006Res);
		log.debug("validateCertIssue 본인확인 END");
		// 본인확인 전문 처리 END

		String personOrCompanyCode = cbIbk01H006Res.getPersonOrCompanyCode();
		String userId = cbIbk01H006Res.getUserID();
		String oppUserId = "";
		String deptPersonName = cbIbk01H006Res.getDeptPersonName();

		// 올바른 사용자 임.
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H006Res.getDeptPersonName(), 12);
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		response.setCustName(deptPersonName); // 예금주명

		// RA사용자 조회
		deptPersonName = deptPersonName + "()";
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		// 공동인증서 휴대폰인증 서비스 가입 여부 [PC뱅킹 전용]
		response.setYoHpIn(cbIbk01H006Res.getYOHPIN());	// 휴대폰인증 서비스 가입 여부
		if ("Y".equals(cbIbk01H006Res.getYOHPIN())) {
			log.debug("validateCertSuspensionUser 고객정보조회 START");
			OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");

			// 공통부 세팅
			hostRequestOptions2.setImsTranCd("TI1IBK01");
			hostRequestOptions2.setInClassCd("H920");
			hostRequestOptions2.setSvcCd("920");

			// 개별부 세팅
			CbIbk01H92000Req cbIbk01H92000Req = new CbIbk01H92000Req();
			cbIbk01H92000Req.setUserID(userId); // 사용자아이디
			cbIbk01H92000Req.setBKGuBun("1"); // 변경구분
			cbIbk01H92000Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);

			// 전문 전송
			OltpResponse<CbIbk01H92000Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H92000Req, CbIbk01H92000Res.class);
			CbIbk01H92000Res cbIbk01H92000Res = hostResponse2.getResponse();

			response.setHandPhone1(cbIbk01H92000Res.getHandPhone1());
			response.setHandPhone2(cbIbk01H92000Res.getHandPhone2());
			response.setHandPhone3(cbIbk01H92000Res.getHandPhone3());

			log.debug("validateCertSuspensionUser 고객정보조회 END");
		}

		// 본인 인증한 데이터 세션에 저장
		Map<String, Object> certSuspensionSession = new HashMap<>();
		certSuspensionSession.put("UserID", cbIbk01H006Res.getUserID());
		certSuspensionSession.put("TSPassword", cbIbk01H006Res.getTSPassword());
		certSuspensionSession.put("RegNo", cbIbk01H006Res.getRegNo());
		certSuspensionSession.put("PersonOrCompanyCode", cbIbk01H006Res.getPersonOrCompanyCode());
		certSuspensionSession.put("SafeCardKind", cbIbk01H006Res.getSafeCardKind());
		certSuspensionSession.put("SmartOTP", cbIbk01H006Res.getSmartOTP());
		certSuspensionSession.put("EmailAddr", cbIbk01H006Res.getEmailAddr());
		certSuspensionSession.put("DeptPersonName", cbIbk01H006Res.getDeptPersonName());
		certSuspensionSession.put("SaupjaNo", cbIbk01H006Res.getSaupjaNo());
		certSuspensionSession.put("ZipCode", cbIbk01H006Res.getZipCode());
		certSuspensionSession.put("Address", cbIbk01H006Res.getAddress());
		certSuspensionSession.put("SecurityIndex", cbIbk01H006Res.getSecurityIndex());
		certSuspensionSession.put("SecurityIndex2", cbIbk01H006Res.getSecurityIndex2());
		certSuspensionSession.put("SafeCardIndex1", cbIbk01H006Res.getSafeCardIndex1());
		certSuspensionSession.put("SafeCardIndex2", cbIbk01H006Res.getSafeCardIndex2());
		certSuspensionSession.put("SafeCardIndex3", cbIbk01H006Res.getSafeCardIndex3());
		certSuspensionSession.put("AuthSVC4CERT", cbIbk01H006Res.getAuthSVC4CERT());
		certSuspensionSession.put("oppraResult", oppraResult); // RA정보
		certSuspensionSession.put("RealNameIDType", cbIbk01H006Res.getYOCFJMGB()); // 실명증표구분 - 1:주민등록번호 2:사업자번호 3:여권번호 4:외국인등록번호 5:임의단체대표자주민번호 6:납세고유번호
		certSuspensionSession.put("TelCode", cbIbk01H006Res.getTelCode());

		sessionManager.setGlobalValue("certSuspensionSession", certSuspensionSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertSuspensionAcct", name = "인증서 효력정지 계좌 검증")
	public CrtJctValidateCertSuspensionAcctResponse validateCertSuspensionAcct(IServiceContext serviceContext, CrtJctValidateCertSuspensionAcctRequest request) {
		// ASIS :: H00621 [func_H00621011()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certSuspensionSession = certUtils.getCertSession("certSuspensionSession");

		// 계좌검증 전문 처리 START
		log.debug("validateCertSuspensionAcct 계좌검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certSuspensionSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certSuspensionSession.get("TSPassword"));// 통신비밀번호
		cbIbk01D00600Req.setSSRAcctNum(request.getAcctNum()); // 계좌번호
		cbIbk01D00600Req.setAcctPasswd(request.getAcctBb()); // 계좌비밀번호
		cbIbk01D00600Req.setCertTranCode("4"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("C"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setJuminSaupjaNo((String) certSuspensionSession.get("RegNo"));
		cbIbk01D00600Req.setRegNo((String) certSuspensionSession.get("RegNo"));
		cbIbk01D00600Req.setEmailAddr((String) certSuspensionSession.get("EmailAddr"));

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D006Res = hostH006Response.getResponse();
		log.debug("validateCertSuspensionAcct 계좌검증 END");
		// 계좌검증 전문 처리 END

		// 추가인증 세션값 세팅
		sessionManager.setGlobalValue("UserID", (String) certSuspensionSession.get("UserID"));
		sessionManager.setGlobalValue("InforGubun", cbIbk01D006Res.getInforGubun());
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", (String) certSuspensionSession.get("SafeCardKind"));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(certSuspensionSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(certSuspensionSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(certSuspensionSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", (String) certSuspensionSession.get("SecurityIndex"));
		sessionManager.setGlobalValue("SafeCardINDEX2", (String) certSuspensionSession.get("SecurityIndex2"));
		sessionManager.setGlobalValue("SmartOTP", (String) certSuspensionSession.get("SmartOTP"));
		sessionManager.setGlobalValue("TSPassword", (String) certSuspensionSession.get("TSPassword"));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 응답 객체
		CrtJctValidateCertSuspensionAcctResponse response = new CrtJctValidateCertSuspensionAcctResponse();
		response.setSafeCardKind(cbIbk01D006Res.getSafeCardKind());
		response.setSecurityIndex(cbIbk01D006Res.getSecurityIndex());
		response.setSecurityIndex2(cbIbk01D006Res.getSecurityIndex2());
		response.setSmartOTP(cbIbk01D006Res.getSmartOTP());
		response.setInforGubun(cbIbk01D006Res.getInforGubun());

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertSuspensionSecurity", name = "인증서 효력정지 보안매체 검증")
	public CrtJctValidateCertSuspensionSecurityResponse validateCertSuspensionSecurity(IServiceContext serviceContext, CrtJctValidateCertSuspensionSecurityRequest request) {
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certSuspensionSession = certUtils.getCertSession("certSuspensionSession");

		certSuspensionSession.put("SecurityValue", VerificationUtils.getSafeCardNumber1());  // 안전카드 첫번째 입력값 or OTP 입력값
		if("1".equals((String) certSuspensionSession.get("SafeCardKind"))) { // 보안매체가 안전카드인 경우
			certSuspensionSession.put("SafeCardValue1", VerificationUtils.getSafeCardSeq1());  // 안전카드일련번호 사용자입력값1
			certSuspensionSession.put("SafeCardValue2", VerificationUtils.getSafeCardSeq2());  // 안전카드일련번호 사용자입력값2
			certSuspensionSession.put("SafeCardValue3", VerificationUtils.getSafeCardSeq3());  // 안전카드일련번호 사용자입력값3
			certSuspensionSession.put("SecurityValue2", VerificationUtils.getSafeCardNumber2());  // 안전카드 두번째 입력값
		}

		sessionManager.setGlobalValue("certSuspensionSession", certSuspensionSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctValidateCertSuspensionSecurityResponse();
	}

	@ServiceEndpoint(url = "/authorizeCertSuspension", name = "인증서 효력정지 완료")
	public CrtJctAuthorizeCertSuspensionResponse authorizeCertSuspension(IServiceContext serviceContext, CrtJctAuthorizeCertSuspensionRequest request) throws Exception {
		// ASIS :: H00621 [renew_confirm(), CertTask.checkExecuteSuspend(), CertTask.executeSuspend()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certSuspensionSession = certUtils.getCertSession("certSuspensionSession");

		// 효력정지 예비처리 전문 처리 START
		log.debug("authorizeCertSuspension 효력정지 예비처리 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certSuspensionSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certSuspensionSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setJuminSaupjaNo((String) certSuspensionSession.get("RegNo"));
		cbIbk01D00600Req.setRegNo((String) certSuspensionSession.get("RegNo"));
		cbIbk01D00600Req.setCertTranCode("4"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setEmailAddr((String) certSuspensionSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setSafeCardKind((String) certSuspensionSession.get("SafeCardKind"));
		cbIbk01D00600Req.setInforGubun((String) certSuspensionSession.get("InforGubun")); // 정보코드
		cbIbk01D00600Req.setTelCode((String) certSuspensionSession.get("TelCode"));// 전화번호

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) certSuspensionSession.get("SecurityIndex")); // 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) certSuspensionSession.get("SecurityIndex2")); // 안전카드 INDEX2
		cbIbk01D00600Req.setSecurityValue((String) certSuspensionSession.get("SecurityValue")); // 안전카드 첫번째 입력값 or OTP 입력값
		cbIbk01D00600Req.setSecurityValue2(" "); // 안전카드 두번째 입력값

		if("1".equals((String) certSuspensionSession.get("SafeCardKind"))) { // 보안매체가 안전카드인 경우
			cbIbk01D00600Req.setSafeCardValue1((String) certSuspensionSession.get("SafeCardValue1")); // 안전카드일련번호 사용자입력값1
			cbIbk01D00600Req.setSafeCardValue2((String) certSuspensionSession.get("SafeCardValue2")); // 안전카드일련번호 사용자입력값2
			cbIbk01D00600Req.setSafeCardValue3((String) certSuspensionSession.get("SafeCardValue3")); // 안전카드일련번호 사용자입력값3
			cbIbk01D00600Req.setSafeCardIndex1((String) certSuspensionSession.get("SafeCardIndex1")); //안전카드일련번호 위치1
			cbIbk01D00600Req.setSafeCardIndex2((String) certSuspensionSession.get("SafeCardIndex2")); //안전카드일련번호 위치2
			cbIbk01D00600Req.setSafeCardIndex3((String) certSuspensionSession.get("SafeCardIndex3")); //안전카드일련번호 위치3
			cbIbk01D00600Req.setSecurityValue2((String) certSuspensionSession.get("SecurityValue2")); // 안전카드값 두번째 입력값
		}

		String pkcs7SignedData = request.getPkcs7SignedData();
		String vidRandom = request.getVidRandom();

		// 제출한인증서 유효성 검사
		Map<String, Object> certVerify = certVerifyComponent.verify("L", pkcs7SignedData, vidRandom);

		X509Certificate userCert = certUtils.getCertificationFromB64((String) certVerify.get("userCert"));
		String userId = (String) certSuspensionSession.get("UserID");

		log.debug("X509Certificate={}", userCert);

		// 인증서 정보 추출 클래스 생성
		CertificateHelper certificateHelper = new CertificateHelper(userCert);

		String issuerDn = certificateHelper.getIssuerDN();
		String issuerO = certificateHelper.getIssuerO();
		String subjectDn = certificateHelper.getSubjectDN();
		String serial = certificateHelper.getSerial();
		String raGubun = "";
		String custGubun = "";
		String certPolicyCode = certificateHelper.getPolicyCode();
		String certOid = certificateHelper.getCertificatePolicyOID();
		String issueBank = certificateHelper.getIssuerBankCode();

		// 사설인증서를 제출하였을 경우에
		if (issuerDn.equals(CertConfig.kfbDN1) || issuerDn.equals(CertConfig.kfbDN2)) {
			throw new PRCServiceException("PRCCRT20831", "SC은행 사설인증서는 공인인증센터에서 유효기간 연장을 할 수 없습니다. SC은행인증센터에서 재발급 받으시기 바랍니다.");
		}

		if (certificateHelper.isCrossCertificate()) {
			if (certificateHelper.isYessign()) {
				String kindOfCA = "YESSIGNCA";
				boolean isKFB = certificateHelper.isKFB();

				/**********************************/
				/* OID 읽어서 정책코드로 변환. */
				/* 1.2.410.200005.1.1.1 ==> 01 */
				/* 1.2.410.200005.1.1.2 ==> 02 */
				/* 1.2.410.200005.1.1.4 ==> 04 */
				/* 1.2.410.200005.1.1.5 ==> 05 */
				// *********************************/
				// 개인범용 인증서
				if (certOid.equals("1.2.410.200005.1.1.1")) {
					custGubun = "1";
					raGubun = "2";
				}

				// 개인금융 인증서
				if (certOid.equals("1.2.410.200005.1.1.4")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 개인금융 인증서 ( 조합번호 전용 )
				if (certOid.equals("1.2.410.200005.1.1.4.8")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 기업금용 인증서
				if (certOid.equals("1.2.410.200005.1.1.2")) {
					custGubun = "2";
					raGubun = "1";
				}

				if (certOid.equals("1.2.410.200005.1.1.5")) {
					custGubun = "2";
					raGubun = "2";
				}

				if (certOid.equals("1.2.410.200005.1.1.6.8")) {
					custGubun = "2";
					raGubun = "3";
				}

				// 타행인증서를 제출하였을 경우
				if (!isKFB) {
					throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 효력정지가 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 효력정지를 하시기 바랍니다.");
				}

				// 인증서 시리얼번호로 당행인증서 DB조회
				LdapInfoResult ldapInfoResult = ldapInfoDao.selectLdapInfoBySerial(serial);
				String personId = ldapInfoResult.getUserid();
				String email = ldapInfoResult.getMail();
				String certStatus = ldapInfoResult.getStatus();

				if (certStatus != null && !certStatus.equals("V")) {
					throw new PRCServiceException("PRCCRT20315", "고객님의 공인인증서는 이미 폐기 혹은 효력정지되었으므로 효력정지 할 수 없습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
				}

				if (((personId == null) || personId.equals("")) && isKFB) {
					throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
				}

				if (personId != null) {
					if (personId.equals("LDAPDB_5000")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (personId.equals("LDAPDB_5001")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (!personId.equals(userId)) {
						throw new PRCServiceException("PRCCRT20614", "고객께서 제출하신 인증서는 본인의 인증서가 아니므로 효력정지 하실수 없습니다. 인증서를 다시 한번 확인하시기 바랍니다.");
					}
				}
			} else {
				// 고객이 제출한 인증서는 금결원을 제외한 다른 공인인증 기관으로 부터 받은 인증서이다.
				throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 효력정지가 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 효력정지를 하시기 바랍니다.");
			}
		} else {
			// 고객이 제출한 인증서가 SC제일은행 사설 인증서도 아니고 공인인증기관 인증서도 아니다. 인증서의 DN이 올바르지 않거나 다른 은행으로부터 인증서를 받았음.
			throw new PRCServiceException("PRCCRT20550", "고객께서 제출하신 인증서는 상호연동용 인증서가 아니거나, 인증서에 상호연동용임을 확인하는 OID가 존재하지 않습니다.");
		}

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals((String) certSuspensionSession.get("SafeCardKind"))) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		CbIbk01D00600Res cbIbk01D00600Res = hostResponse.getResponse();
		log.debug("authorizeCertSuspension 효력정지 예비처리 END");
		// 효력정지 예비처리 전문 처리 END

		// 효력정지 본거래 전문 처리 START
		log.debug("authorizeCertSuspension 효력정지 본거래 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("D006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req2 = new CbIbk01D00600Req();
		cbIbk01D00600Req2.setUserID((String) certSuspensionSession.get("UserID"));
		cbIbk01D00600Req2.setTSPassword((String) certSuspensionSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req2.setCertTranCode("4"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req2.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req2.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req2.setCustGubun(custGubun); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req2.setRAGubun(raGubun); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req2.setJuminSaupjaNo((String) certSuspensionSession.get("RegNo"));
		cbIbk01D00600Req2.setRegNo((String) certSuspensionSession.get("RegNo"));

		cbIbk01D00600Req2.setExpireDate(certificateHelper.getExpireDate());
		cbIbk01D00600Req2.setIssueBank(issueBank);
		cbIbk01D00600Req2.setTelCode(cbIbk01D00600Res.getTelCode());
		cbIbk01D00600Req2.setInforGubun(cbIbk01D00600Res.getInforGubun());
		cbIbk01D00600Req2.setEmailAddr(cbIbk01D00600Res.getEmailAddr());
		cbIbk01D00600Req2.setSafeCardKind(cbIbk01D00600Res.getSafeCardKind());
		cbIbk01D00600Req2.setSecurityIndex(cbIbk01D00600Res.getSecurityIndex());
		cbIbk01D00600Req2.setSafeCardIndex1(cbIbk01D00600Res.getSafeCardIndex1());
		cbIbk01D00600Req2.setSafeCardIndex2(cbIbk01D00600Res.getSafeCardIndex2());
		cbIbk01D00600Req2.setSafeCardIndex3(cbIbk01D00600Res.getSafeCardIndex3());
		cbIbk01D00600Req2.setSecurityIndex2(cbIbk01D00600Res.getSecurityIndex2());


		String certPolicy = certPolicyCode.equals("68") ? "TAX" : "";
		String personOrCompanyCode = cbIbk01D00600Res.getPersonOrCompanyCode();

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01D00600Req2, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01H00600Res2 = hostResponse2.getResponse();

		log.debug("authorizeCertSuspension certPolicyCode :: {}", certPolicyCode);
		log.debug("authorizeCertSuspension userId :: {}", userId);

		String certSerial = "";
		if ("3".equals(personOrCompanyCode)) {
			certSerial = certUtils.getSerialNumByUserID(certPolicyCode, userId);
			log.debug("authorizeCertSuspension getSerialNumByUserID certSerial :: {}", certSerial);
		} else {
			certSerial = certUtils.getSerialNumByRegnum(certPolicyCode, cbIbk01D00600Req2.getJuminSaupjaNo());
			log.debug("authorizeCertSuspension getSerialNumByRegnum certSerial :: {}", certSerial);
		}

		// 인증서 상태변경 (효력정지 : 40)
		Map<String, String> issueMap = kftcCertComponent.changeStatusCert(certSerial, certPolicyCode, "40");

		String resCode = issueMap.get("ResCode");
		String resMsg = issueMap.get("ResMsg");

		// 응답 객체
		CrtJctAuthorizeCertSuspensionResponse response = new CrtJctAuthorizeCertSuspensionResponse();
		response.setResCode(resCode);
		response.setResMsg(resMsg);

		log.debug("authorizeCertSuspension 효력정지 본거래 END");
		// 효력정지 본거래 전문 처리 END

		// 세션 삭제
		sessionManager.removeGlobalValue("certSuspensionSession");

		// 추가인증 세션 삭제
		sessionManager.removeGlobalValue("UserID");
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

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertRestoreUser", name = "인증서 효력회복 본인확인")
	public CrtJctValidateCertRestoreUserResponse validateCertRestoreUser(IServiceContext serviceContext, CrtJctValidateCertRestoreUserRequest request) {
		// ASIS :: H00622 [CertTask.sendOltpAndOppResult(), func_CellAuth()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("certRestoreSession");

		// 응답 객체
		CrtJctValidateCertRestoreUserResponse response = new CrtJctValidateCertRestoreUserResponse();

		// 본인확인 전문 처리 START
		log.debug("validateCertRestoreUser 본인확인 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00600Req cbIbk01H00600Req = new CbIbk01H00600Req();
		cbIbk01H00600Req.setUserID(request.getUserId()); // 사용자아이디
		cbIbk01H00600Req.setRegNo(request.getCustJumin1()+request.getCustJumin2());
		cbIbk01H00600Req.setTSPassword(" "); // 통신비밀번호
		cbIbk01H00600Req.setEmailAddr(" "); // 이메일
		cbIbk01H00600Req.setCertTranCode("5"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00600Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00600Req, CbIbk01H00600Res.class);
		CbIbk01H00600Res cbIbk01H00600Res = hostResponse.getResponse();
		log.debug("validateCertRestoreUser cbIbk01H006Res :: {}", cbIbk01H00600Res);
		log.debug("validateCertRestoreUser 본인확인 END");
		// 본인확인 전문 처리 END

		String personOrCompanyCode = cbIbk01H00600Res.getPersonOrCompanyCode();
		String userId = cbIbk01H00600Res.getUserID();
		String oppUserId = "";
		String deptPersonName = cbIbk01H00600Res.getDeptPersonName();

		// 올바른 사용자 임.
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H00600Res.getDeptPersonName(), 12);
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		response.setCustName(deptPersonName); // 예금주명

		// RA사용자 조회
		deptPersonName = deptPersonName + "()";
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		// 고객정보조회 전문 처리 START
		log.debug("validateCertSuspensionUser 고객정보조회 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("H920");
		hostRequestOptions2.setSvcCd("920");

		// 개별부 세팅
		CbIbk01H92000Req cbIbk01H920Req = new CbIbk01H92000Req();
		cbIbk01H920Req.setUserID(userId); // 사용자아이디
		cbIbk01H920Req.setBKGuBun("1"); // 변경구분
		cbIbk01H920Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);

		// 전문 전송
		OltpResponse<CbIbk01H92000Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H920Req, CbIbk01H92000Res.class);
		CbIbk01H92000Res cbIbk01H92000Res = hostResponse2.getResponse();

		response.setHandPhone( cbIbk01H92000Res.getHandPhone1() + cbIbk01H92000Res.getHandPhone2() + cbIbk01H92000Res.getHandPhone3());
		response.setHomeTele( cbIbk01H92000Res.getHomeTele1() + cbIbk01H92000Res.getHomeTele2() + cbIbk01H92000Res.getHomeTele3());
		response.setJobTele(cbIbk01H92000Res.getJobTele1() + cbIbk01H92000Res.getJobTele2() + cbIbk01H92000Res.getJobTele3());

		log.debug("validateCertSuspensionUser 고객정보조회 END");
		// 고객정보조회 전문 처리 END

		// 본인 인증한 데이터 세션에 저장
		Map<String, Object> certRestoreSession = new HashMap<>();
		certRestoreSession.put("UserID", cbIbk01H00600Res.getUserID());
		certRestoreSession.put("TSPassword", cbIbk01H00600Res.getTSPassword());
		certRestoreSession.put("RegNo", cbIbk01H00600Res.getRegNo());
		certRestoreSession.put("PersonOrCompanyCode", cbIbk01H00600Res.getPersonOrCompanyCode());
		certRestoreSession.put("SafeCardKind", cbIbk01H00600Res.getSafeCardKind());
		certRestoreSession.put("SmartOTP", cbIbk01H00600Res.getSmartOTP());
		certRestoreSession.put("EmailAddr", cbIbk01H00600Res.getEmailAddr());
		certRestoreSession.put("DeptPersonName", cbIbk01H00600Res.getDeptPersonName());
		certRestoreSession.put("SaupjaNo", cbIbk01H00600Res.getSaupjaNo());
		certRestoreSession.put("ZipCode", cbIbk01H00600Res.getZipCode());
		certRestoreSession.put("Address", cbIbk01H00600Res.getAddress());
		certRestoreSession.put("SecurityIndex", cbIbk01H00600Res.getSecurityIndex());
		certRestoreSession.put("SecurityIndex2", cbIbk01H00600Res.getSecurityIndex2());
		certRestoreSession.put("SafeCardIndex1", cbIbk01H00600Res.getSafeCardIndex1());
		certRestoreSession.put("SafeCardIndex2", cbIbk01H00600Res.getSafeCardIndex2());
		certRestoreSession.put("SafeCardIndex3", cbIbk01H00600Res.getSafeCardIndex3());
		certRestoreSession.put("AuthSVC4CERT", cbIbk01H00600Res.getAuthSVC4CERT());
		certRestoreSession.put("oppraResult", oppraResult); // RA정보
		certRestoreSession.put("RealNameIDType", cbIbk01H00600Res.getYOCFJMGB()); // 실명증표구분 - 1:주민등록번호 2:사업자번호 3:여권번호 4:외국인등록번호 5:임의단체대표자주민번호 6:납세고유번호
		certRestoreSession.put("TelCode", cbIbk01H00600Res.getTelCode());

		sessionManager.setGlobalValue("certRestoreSession", certRestoreSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertRestoreAcct", name = "인증서 효력회복 계좌 검증")
	public CrtJctValidateCertRestoreAcctResponse validateCertRestoreAcct(IServiceContext serviceContext, CrtJctValidateCertRestoreAcctRequest request) {
		// ASIS :: H00622 [func_H00622011]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRestoreSession = certUtils.getCertSession("certRestoreSession");

		// 계좌검증 전문 처리 START
		log.debug("validateCertSuspensionAcct 계좌검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certRestoreSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certRestoreSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setSSRAcctNum(request.getAcctNum()); // 계좌번호
		cbIbk01D00600Req.setAcctPasswd(request.getAcctBb()); // 계좌비밀번호
		cbIbk01D00600Req.setCertTranCode("5"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("C"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setJuminSaupjaNo((String) certRestoreSession.get("RegNo"));
		cbIbk01D00600Req.setRegNo((String) certRestoreSession.get("RegNo"));
		cbIbk01D00600Req.setEmailAddr((String) certRestoreSession.get("EmailAddr"));

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostH006Response.getResponse();
		log.debug("validateCertSuspensionAcct 계좌검증 END");
		// 계좌검증 전문 처리 END

		// 추가인증 세션값 세팅
		sessionManager.setGlobalValue("UserID", (String) certRestoreSession.get("UserID"));
		sessionManager.setGlobalValue("InforGubun", cbIbk01D00600Res.getInforGubun());
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", (String) certRestoreSession.get("SafeCardKind"));
		sessionManager.setGlobalValue("SafeCardSeq1", String.valueOf(certRestoreSession.get("SafeCardIndex1")));
		sessionManager.setGlobalValue("SafeCardSeq2", String.valueOf(certRestoreSession.get("SafeCardIndex2")));
		sessionManager.setGlobalValue("SafeCardSeq3", String.valueOf(certRestoreSession.get("SafeCardIndex3")));
		sessionManager.setGlobalValue("SafeCardINDEX", (String) certRestoreSession.get("SecurityIndex"));
		sessionManager.setGlobalValue("SafeCardINDEX2", (String) certRestoreSession.get("SecurityIndex2"));
		sessionManager.setGlobalValue("SmartOTP", (String) certRestoreSession.get("SmartOTP"));
		sessionManager.setGlobalValue("TSPassword", (String) certRestoreSession.get("TSPassword"));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 응답 객체
		CrtJctValidateCertRestoreAcctResponse response = new CrtJctValidateCertRestoreAcctResponse();
		response.setSafeCardKind(cbIbk01D00600Res.getSafeCardKind());
		response.setSecurityIndex(cbIbk01D00600Res.getSecurityIndex());
		response.setSecurityIndex2(cbIbk01D00600Res.getSecurityIndex2());
		response.setSmartOTP(cbIbk01D00600Res.getSmartOTP());

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertRestoreSecurity", name = "인증서 효력회복 보안매체 검증")
	public CrtJctValidateCertRestoreSecurityResponse validateCertRestoreSecurity(IServiceContext serviceContext, CrtJctValidateCertRestoreSecurityRequest request) {
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRestoreSession = certUtils.getCertSession("certRestoreSession");

		certRestoreSession.put("SecurityValue", VerificationUtils.getSafeCardNumber1());  // 안전카드 첫번째 입력값 or OTP 입력값
		if("1".equals((String) certRestoreSession.get("SafeCardKind"))) { // 보안매체가 안전카드인 경우
			certRestoreSession.put("SafeCardValue1", VerificationUtils.getSafeCardSeq1());  // 안전카드일련번호 사용자입력값1
			certRestoreSession.put("SafeCardValue2", VerificationUtils.getSafeCardSeq2());  // 안전카드일련번호 사용자입력값2
			certRestoreSession.put("SafeCardValue3", VerificationUtils.getSafeCardSeq3());  // 안전카드일련번호 사용자입력값3
			certRestoreSession.put("SecurityValue2", VerificationUtils.getSafeCardNumber2());  // 안전카드 두번째 입력값
		}

		sessionManager.setGlobalValue("certRestoreSession", certRestoreSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctValidateCertRestoreSecurityResponse();
	}

	@ServiceEndpoint(url = "/authorizeCertRestore", name = "인증서 효력회복 완료")
	public CrtJctAuthorizeCertRestoreResponse authorizeCertRestore(IServiceContext serviceContext, CrtJctAuthorizeCertRestoreRequest request) throws Exception {
		// ASIS :: H00622 [renew_confirm(), CertTask.checkExecuteRecover(), CertTask.executeRecover()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRestoreSession = certUtils.getCertSession("certRestoreSession");

		// 효력회복 예비처리 전문 처리 START
		log.debug("authorizeCertRestore 효력회복 예비처리 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certRestoreSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certRestoreSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setJuminSaupjaNo((String) certRestoreSession.get("RegNo"));
		cbIbk01D00600Req.setRegNo((String) certRestoreSession.get("RegNo"));
		cbIbk01D00600Req.setCertTranCode("5"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setEmailAddr((String) certRestoreSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setSafeCardKind((String) certRestoreSession.get("SafeCardKind"));
		cbIbk01D00600Req.setInforGubun((String) certRestoreSession.get("InforGubun")); // 정보코드
		cbIbk01D00600Req.setTelCode((String) certRestoreSession.get("TelCode"));// 전화번호

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) certRestoreSession.get("SecurityIndex")); // 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) certRestoreSession.get("SecurityIndex2")); // 안전카드 INDEX2
		cbIbk01D00600Req.setSecurityValue((String) certRestoreSession.get("SecurityValue")); // 안전카드 첫번째 입력값 or OTP 입력값
		cbIbk01D00600Req.setSecurityValue2(" "); // 안전카드 두번째 입력값

		if("1".equals((String) certRestoreSession.get("SafeCardKind"))) { // 보안매체가 안전카드인 경우
			cbIbk01D00600Req.setSafeCardValue1((String) certRestoreSession.get("SafeCardValue1")); // 안전카드일련번호 사용자입력값1
			cbIbk01D00600Req.setSafeCardValue2((String) certRestoreSession.get("SafeCardValue2")); // 안전카드일련번호 사용자입력값2
			cbIbk01D00600Req.setSafeCardValue3((String) certRestoreSession.get("SafeCardValue3")); // 안전카드일련번호 사용자입력값3
			cbIbk01D00600Req.setSafeCardIndex1((String) certRestoreSession.get("SafeCardIndex1")); //안전카드일련번호 위치1
			cbIbk01D00600Req.setSafeCardIndex2((String) certRestoreSession.get("SafeCardIndex2")); //안전카드일련번호 위치2
			cbIbk01D00600Req.setSafeCardIndex3((String) certRestoreSession.get("SafeCardIndex3")); //안전카드일련번호 위치3
			cbIbk01D00600Req.setSecurityValue2((String) certRestoreSession.get("SecurityValue2")); // 안전카드값 두번째 입력값
		}

		String pkcs7SignedData = request.getPkcs7SignedData();
		String vidRandom = request.getVidRandom();

		// 제출한인증서 유효성 검사
		Map<String, Object> certVerify = certVerifyComponent.verify("A", pkcs7SignedData, vidRandom);

		X509Certificate userCert = certUtils.getCertificationFromB64((String) certVerify.get("userCert"));
		String userId = (String) certRestoreSession.get("UserID");

		log.debug("X509Certificate={}", userCert);

		// 인증서 정보 추출 클래스 생성
		CertificateHelper certificateHelper = new CertificateHelper(userCert);

		String issuerDn = certificateHelper.getIssuerDN();
		String issuerO = certificateHelper.getIssuerO();
		String subjectDn = certificateHelper.getSubjectDN();
		String serial = certificateHelper.getSerial();
		String raGubun = "";
		String custGubun = "";
		String certPolicyCode = certificateHelper.getPolicyCode();
		String certOid = certificateHelper.getCertificatePolicyOID();
		String issueBank = certificateHelper.getIssuerBankCode();

		// 사설인증서를 제출하였을 경우에
		if (issuerDn.equals(CertConfig.kfbDN1) || issuerDn.equals(CertConfig.kfbDN2)) {
			throw new PRCServiceException("PRCCRT20831", "SC은행 사설인증서는 공인인증센터에서 유효기간 연장을 할 수 없습니다. SC은행인증센터에서 재발급 받으시기 바랍니다.");
		}

		if (certificateHelper.isCrossCertificate()) {
			if (certificateHelper.isYessign()) {
				String kindOfCA = "YESSIGNCA";
				boolean isKFB = certificateHelper.isKFB();

				/**********************************/
				/* OID 읽어서 정책코드로 변환. */
				/* 1.2.410.200005.1.1.1 ==> 01 */
				/* 1.2.410.200005.1.1.2 ==> 02 */
				/* 1.2.410.200005.1.1.4 ==> 04 */
				/* 1.2.410.200005.1.1.5 ==> 05 */
				// *********************************/
				// 개인범용 인증서
				if (certOid.equals("1.2.410.200005.1.1.1")) {
					custGubun = "1";
					raGubun = "2";
				}

				// 개인금융 인증서
				if (certOid.equals("1.2.410.200005.1.1.4")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 개인금융 인증서 ( 조합번호 전용 )
				if (certOid.equals("1.2.410.200005.1.1.4.8")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 기업금용 인증서
				if (certOid.equals("1.2.410.200005.1.1.2")) {
					custGubun = "2";
					raGubun = "1";
				}

				if (certOid.equals("1.2.410.200005.1.1.5")) {
					custGubun = "2";
					raGubun = "2";
				}

				if (certOid.equals("1.2.410.200005.1.1.6.8")) {
					custGubun = "2";
					raGubun = "3";
				}

				// 타행인증서를 제출하였을 경우
				if (!isKFB) {
					throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 효력회복이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 효력회복을 하시기 바랍니다.");
				}

				// 인증서 시리얼번호로 당행인증서 DB조회
				LdapInfoResult ldapInfoResult = ldapInfoDao.selectLdapInfoBySerial(serial);
				String personId = ldapInfoResult.getUserid();
				String email = ldapInfoResult.getMail();
				String certStatus = ldapInfoResult.getStatus();

				// 인증서 상태 정상이면 에러발생
				if (certStatus != null && certStatus.equals("V")) {
					throw new PRCServiceException("PRCCRT20411", "고객께서는 이미 인증서를 효력회복 또는 발급을 받으셨습니다. 자세한 내용은 인증 등록기관 관리자에게 문의하시기 바랍니다.");
				}

				if (((personId == null) || personId.equals("")) && isKFB) {
					throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
				}

				if (personId != null) {
					if (personId.equals("LDAPDB_5000")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (personId.equals("LDAPDB_5001")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (!personId.equals(userId)) {
						throw new PRCServiceException("PRCCRT20614", "고객께서 제출하신 인증서는 본인의 인증서가 아니므로 효력회복을 하실수 없습니다.인증서를 다시 한번 확인하시기 바랍니다.");
					}
				}
			} else {
				// 고객이 제출한 인증서는 금결원을 제외한 다른 공인인증 기관으로 부터 받은 인증서이다.
				throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 효력회복이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 효력회복을 하시기 바랍니다.");
			}
		} else {
			// 고객이 제출한 인증서가 SC제일은행 사설 인증서도 아니고 공인인증기관 인증서도 아니다. 인증서의 DN이 올바르지 않거나 다른 은행으로부터 인증서를 받았음.
			throw new PRCServiceException("PRCCRT20550", "고객께서 제출하신 인증서는 상호연동용 인증서가 아니거나, 인증서에 상호연동용임을 확인하는 OID가 존재하지 않습니다.");
		}

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals((String) certRestoreSession.get("SafeCardKind"))) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		CbIbk01D00600Res cbIbk01D00600Res = hostResponse.getResponse();
		log.debug("authorizeCertRestore 효력회복 예비처리 END");
		// 효력회복 예비처리 전문 처리 END

		// 효력회복 본거래 전문 처리 START
		log.debug("authorizeCertRestore 효력회복 본거래 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("D006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req2 = new CbIbk01D00600Req();
		cbIbk01D00600Req2.setUserID((String) certRestoreSession.get("UserID"));
		cbIbk01D00600Req2.setTSPassword((String) certRestoreSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req2.setCertTranCode("5"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req2.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req2.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req2.setCustGubun(custGubun); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req2.setRAGubun(raGubun); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req2.setJuminSaupjaNo((String) certRestoreSession.get("RegNo"));
		cbIbk01D00600Req2.setRegNo((String) certRestoreSession.get("RegNo"));

		cbIbk01D00600Req2.setExpireDate(certificateHelper.getExpireDate());
		cbIbk01D00600Req2.setIssueBank(issueBank);
		cbIbk01D00600Req2.setTelCode(cbIbk01D00600Res.getTelCode());
		cbIbk01D00600Req2.setInforGubun(cbIbk01D00600Res.getInforGubun());
		cbIbk01D00600Req2.setEmailAddr(cbIbk01D00600Res.getEmailAddr());
		cbIbk01D00600Req2.setSafeCardKind(cbIbk01D00600Res.getSafeCardKind());
		cbIbk01D00600Req2.setSecurityIndex(cbIbk01D00600Res.getSecurityIndex());
		cbIbk01D00600Req2.setSafeCardIndex1(cbIbk01D00600Res.getSafeCardIndex1());
		cbIbk01D00600Req2.setSafeCardIndex2(cbIbk01D00600Res.getSafeCardIndex2());
		cbIbk01D00600Req2.setSafeCardIndex3(cbIbk01D00600Res.getSafeCardIndex3());
		cbIbk01D00600Req2.setSecurityIndex2(cbIbk01D00600Res.getSecurityIndex2());

		String certPolicy = certPolicyCode.equals("68") ? "TAX" : "";
		String personOrCompanyCode = cbIbk01D00600Res.getPersonOrCompanyCode();

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01D00600Req2, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01H00600Res2 = hostResponse2.getResponse();

		log.debug("authorizeCertRestore certPolicyCode :: {}", certPolicyCode);
		log.debug("authorizeCertRestore userId :: {}", userId);

		String certSerial = "";
		if ("3".equals(personOrCompanyCode)) {
			certSerial = certUtils.getSerialNumByUserID(certPolicyCode, userId);
			log.debug("authorizeCertRestore getSerialNumByUserID certSerial :: {}", certSerial);
		} else {
			certSerial = certUtils.getSerialNumByRegnum(certPolicyCode, cbIbk01D00600Req2.getJuminSaupjaNo());
			log.debug("authorizeCertRestore getSerialNumByRegnum certSerial :: {}", certSerial);
		}

		// 인증서 상태변경 (효력회복 : 41)
		Map<String, String> issueMap = kftcCertComponent.changeStatusCert(certSerial, certPolicyCode, "41");

		String resCode = issueMap.get("ResCode");
		String resMsg = issueMap.get("ResMsg");

		// 응답 객체
		CrtJctAuthorizeCertRestoreResponse response = new CrtJctAuthorizeCertRestoreResponse();
		response.setResCode(resCode);
		response.setResMsg(resMsg);

		log.debug("authorizeCertRestore 효력회복 본거래 END");
		// 효력회복 본거래 전문 처리 END

		// 세션 삭제
		sessionManager.removeGlobalValue("certRestoreSession");

		// 추가인증 세션 삭제
		sessionManager.removeGlobalValue("UserID");
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

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/requestCertTaxInvoice", name = "세금계산서 신청")
	public CrtJctRequestCertTaxInvoiceResponse requestCertTaxInvoice(IServiceContext serviceContext, CrtJctRequestCertTaxInvoiceRequest request) {
		// ASIS :: H00606 [CertTask.requestTaxAccount()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// String adminEmail = "Joohyun.lee@sc.com";
		// String adminEmail = "ByoJiHye.Choi@sc.comungKwon.Park@sc.com";
		// String adminEmail = "stereok2@gmail.com";
		// String adminEmail = "NaNa.Lee@sc.com";
		// String adminEmail = "PyeoungSeung.Kim@sc.com";
		// String adminEmail = "JiHye.Choi@sc.com";
		// String adminEmail = "JinHee.An@sc.com"; // 2018.08.24
		String adminEmail = "JiYeon.Shim@sc.com"; // 2023.06

		String adminName = "SC은행";

		String accounts = "";
		String subject = "";

		String userID = request.getUserId();
		String emailAddr2 = request.getEmailAddr2();

		String issueDate = request.getIssueDate();

		String saupjaNo1 = request.getSaupjaNo1();
		String saupjaNo2 = request.getSaupjaNo2();
		String saupjaNo3 = request.getSaupjaNo3();
		String saupjaNo = saupjaNo1 + saupjaNo2 + saupjaNo3;

		String companyName = request.getCompanyName();
		String personInCharge = request.getPersonInCharge();

		String telCode1 = request.getTelCode1();
		String telCode2 = request.getTelCode2();
		String telCode3 = request.getTelCode3();
		String companyPhone = telCode1 + telCode2 + telCode3;

		String companyFax1 = request.getCompanyFax1();
		String companyFax2 = request.getCompanyFax2();
		String companyFax3 = request.getCompanyFax3();
		String companyFax = "";

		if (!"".equals(companyFax1) || !"".equals(companyFax2) || !"".equals(companyFax3)) {
			companyFax = companyFax1 + companyFax2 + companyFax3;
		}

		String president = request.getPresident();
		String companyLocation = request.getCompanyLocation();
		String typesOfIndustry = request.getTypesOfIndustry();
		String item = request.getItem();
		String ssrFee = request.getSsrFee();
		String ssrVat = request.getSsrVat();

		subject = "공동인증서발급 " + companyName + " " + saupjaNo;
		accounts = userID + ";" + issueDate + ";" + personInCharge + ";" + emailAddr2 + ";" + companyPhone + ";"
				+ companyFax + ";" + saupjaNo + ";" + companyName + ";" + president + ";" + companyLocation + ";"
				+ typesOfIndustry + ";" + item + ";" + ssrFee + ";" + ssrVat + ";";

		log.debug("requestCertTaxInvoice 메일발송");

		try {
			EmailUtils.sendCompleteMail("190", adminName, adminEmail, subject, accounts);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctRequestCertTaxInvoiceResponse();
	}

	@ServiceEndpoint(url = "/validateCertRenewCertificate", name = "공동인증서 갱신 인증서 유효성 검사")
	public CrtJctValidateCertRenewCertificateResponse validateCertRenewCertificate(IServiceContext serviceContext, CrtJctValidateCertRenewCertificateRequest request) throws Exception {
		// ASIS :: PRCCRTCRT006_101S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 갱신 세션 초기화
		sessionManager.removeGlobalValue("certRenewSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 응답 객체
		CrtJctValidateCertRenewCertificateResponse response = new CrtJctValidateCertRenewCertificateResponse();

		String pkcs7SignedData = request.getPkcs7SignedData();
		String vidRandom = request.getVidRandom();

		// 제출한인증서 유효성 검사
		Map<String, Object> certVerify = certVerifyComponent.verify("L", pkcs7SignedData, vidRandom);

		// 본인확인 전문 처리 START
		log.debug("validateCertRenewCertificate 본인확인 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00600Req cbIbk01H00600Req = new CbIbk01H00600Req();
		cbIbk01H00600Req.setRegNo(sessionManager.getGlobalValue("cid", String.class)); // 실명번호
		cbIbk01H00600Req.setJuminSaupjaNo(sessionManager.getGlobalValue("cid", String.class)); // 주민사업자번호
		cbIbk01H00600Req.setUserID(sessionManager.getGlobalValue("uid", String.class)); // 사용자아이디
		cbIbk01H00600Req.setTSPassword("99999999"); // 통신비밀번호
		cbIbk01H00600Req.setCertTranCode("3"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01H00600Req.setYIJMGB("1"); // 인증서사용자구분

		// 전문 전송
		OltpResponse<CbIbk01H00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00600Req, CbIbk01H00600Res.class);
		CbIbk01H00600Res cbIbk01H00600Res = hostH006Response.getResponse();
		log.debug("validateCertRenewCertificate 본인확인 END");

		String personOrCompanyCode = cbIbk01H00600Res.getPersonOrCompanyCode();
		String userId = cbIbk01H00600Res.getUserID();

		String oppUserId = "";
		String deptPersonName = "";

		// 조합번호용 인증서
		// 여권조합번호 사용자인데 정책구분 04로 기발급된 항목이 있는 경우 폐기안내
		if ("3".equals(cbIbk01H00600Res.getYOCFJMGB())) {
			String policy = "04";
			List<LdapInfoResult> certList = ldapInfoDao.selectForDelete(policy, userId);

			if (certList.size() > 0) {
				String combPopType = "72";
				response.setCombPopType(combPopType);
				log.debug("validateCertRenewCertificate combPopType :: {}", combPopType);
			}
		}

		// 올바른 사용자임
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2") || personOrCompanyCode.equals("3")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H00600Res.getDeptPersonName(), 9);
			deptPersonName = deptPersonName + "()";
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// RA사용자 조회
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		if ("3".equals(cbIbk01H00600Res.getPersonOrCompanyCode())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		if ("13".equals(cbIbk01H00600Res.getYOIDGB()) || "14".equals(cbIbk01H00600Res.getYOIDGB())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		// 공동인증서 휴대폰인증 서비스 가입 여부 [PC뱅킹 전용]
		response.setYoHpIn(cbIbk01H00600Res.getYOHPIN());	// 휴대폰인증 서비스 가입 여부
		if ("Y".equals(cbIbk01H00600Res.getYOHPIN())) {
			log.debug("validateCertRenewCertificate 고객정보조회 START");
			OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");

			// 공통부 세팅
			hostRequestOptions2.setImsTranCd("TI1IBK01");
			hostRequestOptions2.setInClassCd("H920");
			hostRequestOptions2.setSvcCd("920");

			// 개별부 세팅
			CbIbk01H92000Req cbIbk01H92000Req = new CbIbk01H92000Req();
			cbIbk01H92000Req.setUserID(userId); // 사용자아이디
			cbIbk01H92000Req.setBKGuBun("1"); // 변경구분
			cbIbk01H92000Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);

			// 전문 전송
			OltpResponse<CbIbk01H92000Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H92000Req, CbIbk01H92000Res.class);
			CbIbk01H92000Res cbIbk01H92000Res = hostResponse2.getResponse();

			response.setHandPhone1(cbIbk01H92000Res.getHandPhone1());
			response.setHandPhone2(cbIbk01H92000Res.getHandPhone2());
			response.setHandPhone3(cbIbk01H92000Res.getHandPhone3());

			log.debug("validateCertRenewCertificate 고객정보조회 END");
		}

		// 본인 인증한 데이터 세션에 저장
		Map<String, Object> certRenewSession = new HashMap<>();
		certRenewSession.put("UserID", cbIbk01H00600Res.getUserID());
		certRenewSession.put("TSPassword", cbIbk01H00600Res.getTSPassword());
		certRenewSession.put("RegNo", cbIbk01H00600Res.getRegNo());
		certRenewSession.put("PersonOrCompanyCode", cbIbk01H00600Res.getPersonOrCompanyCode());
		certRenewSession.put("SafeCardKind", cbIbk01H00600Res.getSafeCardKind());
		certRenewSession.put("SmartOTP", cbIbk01H00600Res.getSmartOTP());
		certRenewSession.put("EmailAddr", cbIbk01H00600Res.getEmailAddr());
		certRenewSession.put("DeptPersonName", cbIbk01H00600Res.getDeptPersonName());
		certRenewSession.put("SaupjaNo", cbIbk01H00600Res.getSaupjaNo());
		certRenewSession.put("ZipCode", cbIbk01H00600Res.getZipCode());
		certRenewSession.put("Address", cbIbk01H00600Res.getAddress());
		certRenewSession.put("SecurityIndex", cbIbk01H00600Res.getSecurityIndex());
		certRenewSession.put("SecurityIndex2", cbIbk01H00600Res.getSecurityIndex2());
		certRenewSession.put("SafeCardIndex1", cbIbk01H00600Res.getSafeCardIndex1());
		certRenewSession.put("SafeCardIndex2", cbIbk01H00600Res.getSafeCardIndex2());
		certRenewSession.put("SafeCardIndex3", cbIbk01H00600Res.getSafeCardIndex3());
		certRenewSession.put("AuthSVC4CERT", cbIbk01H00600Res.getAuthSVC4CERT());
		certRenewSession.put("InforGubun", cbIbk01H00600Res.getInforGubun());
		certRenewSession.put("YOAGREEGB", cbIbk01H00600Res.getYOAGREEGB()); // 인증서발급동의여부
		certRenewSession.put("YOAGIL", cbIbk01H00600Res.getYOAGIL()); // 인증서발급동의거부일
		certRenewSession.put("TelCode", cbIbk01H00600Res.getTelCode());
		certRenewSession.put("oppraResult", oppraResult); // RA정보
		certRenewSession.put("userCert", certVerify.get("userCert")); // 인증서 정보
		certRenewSession.put("certIndex", StringUtils.nvl(request.getCertIndex(), "")); // 인증서 index
		certRenewSession.put("encData", StringUtils.nvl(request.getEncData(), "")); // 인증서 비밀번호
		certRenewSession.put("YESSIGN_CAIP", CertConfig.YESSIGN_CAIP);
		certRenewSession.put("YESSIGN_CAPORT", CertConfig.YESSIGN_CAPORT);

		sessionManager.setGlobalValue("certRenewSession", certRenewSession);
		sessionManager.setGlobalValue("PerBusNo", cbIbk01H00600Res.getRegNo());
		sessionManager.setGlobalValue("CustName", cbIbk01H00600Res.getDeptPersonName());

		// 추가인증 세션값 세팅
		String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00600Res.getTelCode());
		sessionManager.setGlobalValue("HPOne", telCode[0]);
		sessionManager.setGlobalValue("HPTwo", telCode[1]);
		sessionManager.setGlobalValue("HPThree", telCode[2]);
		sessionManager.setGlobalValue("phoneNo", telCode[3]);
		sessionManager.setGlobalValue("UserID", (String) certRenewSession.get("UserID"));
		sessionManager.setGlobalValue("InforGubun", cbIbk01H00600Res.getInforGubun());
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", (String) certRenewSession.get("SafeCardKind"));
		sessionManager.setGlobalValue("SafeCardSeq1", cbIbk01H00600Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", cbIbk01H00600Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", cbIbk01H00600Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", (String) certRenewSession.get("SecurityIndex"));
		sessionManager.setGlobalValue("SafeCardINDEX2", (String) certRenewSession.get("SecurityIndex2"));
		sessionManager.setGlobalValue("SmartOTP", (String) certRenewSession.get("SmartOTP"));
		sessionManager.setGlobalValue("TSPassword", (String) certRenewSession.get("TSPassword"));
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		response.setYoAGREEGB(cbIbk01H00600Res.getYOAGREEGB()); // 인증서발급동의여부
		response.setYesSignCaIp((String) certRenewSession.get("YESSIGN_CAIP"));
		response.setYesSignCaPort((String) certRenewSession.get("YESSIGN_CAPORT"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertRenewSecurity", name = "공동인증서 갱신 보안매체 검증")
	public CrtJctValidateCertRenewSecurityResponse validateCertRenewSecurity(IServiceContext serviceContext, CrtJctValidateCertRenewSecurityRequest request) throws CertificateException, IOException {
		// ASIS :: MA3CRTCRT006_301S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRenewSession = certUtils.getCertSession("certRenewSession");

		log.debug("validateCertRenewSecurity certRenewSession :: {}", certRenewSession);

//		X509Certificate userCert = (X509Certificate) certRenewSession.get("userCert");
		X509Certificate userCert = certUtils.getCertificationFromB64((String) certRenewSession.get("userCert"));

		log.debug("validateCertRenewSecurity certRenew X509Certificate :: {}", userCert);

		// 인증서 정보 추출 클래스 생성
		CertificateHelper certificateHelper = new CertificateHelper(userCert);

		String issuerDn = certificateHelper.getIssuerDN();
		String subjectDn = certificateHelper.getSubjectDN();
		String certOid = certificateHelper.getCertificatePolicyOID();
		String serial = certificateHelper.getSerial();
		String raGubun = "";
		String custGubun = "";
		String userId = (String) certRenewSession.get("UserID");
		String certPolicyCode = certificateHelper.getPolicyCode();

		// 사설인증서를 제출하였을 경우에
		if (issuerDn.equals(CertConfig.kfbDN1) || issuerDn.equals(CertConfig.kfbDN2)) {
			throw new PRCServiceException("PRCCRT20831", "SC은행 사설인증서는 공인인증센터에서 유효기간 연장을 할 수 없습니다. SC은행인증센터에서 재발급 받으시기 바랍니다.");
		}

		log.debug("##CombCert validateCertRenewSecurity certificateHelper.isCrossCertificate() {}", certificateHelper.isCrossCertificate());
		log.debug("##CombCert validateCertRenewSecurity certificateHelper.isYessign() {}", certificateHelper.isYessign());
		log.debug("##CombCert validateCertRenewSecurity issuerDn {}", issuerDn);
		log.debug("##CombCert validateCertRenewSecurity subjectDn {}", subjectDn);
		log.debug("##CombCert validateCertRenewSecurity certUtils.getOrgCode(subjectDn) {}", certUtils.getOrgCode(subjectDn));
		log.debug("##CombCert validateCertRenewSecurity certOid {}", certOid);

		if (certificateHelper.isCrossCertificate()) {
			if (certificateHelper.isYessign()) {
				String kindOfCA = "YESSIGNCA";
				boolean isKFB = certificateHelper.isKFB();

				/**********************************/
				/* OID 읽어서 정책코드로 변환. */
				/* 1.2.410.200005.1.1.1 ==> 01 */
				/* 1.2.410.200005.1.1.2 ==> 02 */
				/* 1.2.410.200005.1.1.4 ==> 04 */
				/* 1.2.410.200005.1.1.5 ==> 05 */
				// *********************************/
				// 개인범용 인증서
				if (certOid.equals("1.2.410.200005.1.1.1")) {
					custGubun = "1";
					raGubun = "2";
				}

				// 개인금융 인증서
				if (certOid.equals("1.2.410.200005.1.1.4")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 개인금융 인증서 ( 조합번호 전용 )
				if (certOid.equals("1.2.410.200005.1.1.4.8")) {
					custGubun = "1";
					raGubun = "1";
				}

				// 기업금용 인증서
				if (certOid.equals("1.2.410.200005.1.1.2")) {
					custGubun = "2";
					raGubun = "1";
				}

				if (certOid.equals("1.2.410.200005.1.1.5")) {
					custGubun = "2";
					raGubun = "2";
				}

				if (certOid.equals("1.2.410.200005.1.1.6.8")) {
					custGubun = "2";
					raGubun = "3";
				}

				// 타행인증서를 제출하였을 경우
				if (!isKFB) {
					throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 갱신이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 갱신을 하시기 바랍니다.");
				}

				// 인증서 시리얼번호로 당행인증서 DB조회
				LdapInfoResult ldapInfoResult = ldapInfoDao.selectLdapInfoBySerial(serial);
				String personId = ldapInfoResult.getUserid();
				String email = ldapInfoResult.getMail();
				String certStatus = ldapInfoResult.getStatus();

				if (certStatus != null && !certStatus.equals("V")) {
					throw new PRCServiceException("PRCCRT20830", "고객의 인증서는 효력정지된 인증서이므로 유효기간 연장을 할 수 없습니다.");
				}

				if (((personId == null) || personId.equals("")) && isKFB) {
					throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
				}

				if (personId != null) {
					if (personId.equals("LDAPDB_5000")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (personId.equals("LDAPDB_5001")) {
						throw new PRCServiceException("PRCCRT30113", "고객께서 제출하신 공인인증서는 스탠다드차타드은행에서 발급받으셨으나 현재 폐기되었거나 효력정지 중인 인증서입니다. 자세한 내용은 인증센터 관리자에게 문의하시기 바랍니다.");
					} else if (!personId.equals(userId)) {
						throw new PRCServiceException("PRCCRT20614", "고객께서 제출하신 인증서는 본인의 인증서가 아니므로 갱신하실수 없습니다.인증서를 다시 한번 확인하시기 바랍니다.");
					}
				}
			} else {
				// 고객이 제출한 인증서는 금결원을 제외한 다른 공인인증 기관으로 부터 받은 인증서이다.
				throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 갱신이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 갱신을 하시기 바랍니다.");
			}
		} else {
			// 고객이 제출한 인증서가 SC제일은행 사설 인증서도 아니고 공인인증기관 인증서도 아니다.
			// 인증서의 DN이 올바르지 않거나 다른 은행으로부터 인증서를 받았음.
			throw new PRCServiceException("PRCCRT20711", "고객께서 등록하신 타기관인증서는 해당 은행에서만 갱신이 가능합니다. 발급받으신 은행의 인터넷뱅킹 웹사이트에서 공인인증서 갱신을 하시기 바랍니다.");
		}

		// 보안매체 전문 처리 START
		log.debug("validateCertRenewSecurity 보안매체 처리 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certRenewSession.get("UserID"));
		cbIbk01D00600Req.setTSPassword((String) certRenewSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setRegNo((String) certRenewSession.get("RegNo"));
		cbIbk01D00600Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		cbIbk01D00600Req.setCertTranCode("3"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("S"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun(custGubun); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setRAGubun(raGubun); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req.setEmailAddr((String) certRenewSession.get("EmailAddr"));
		cbIbk01D00600Req.setJoinLoad("000"); // 가입경로
		cbIbk01D00600Req.setSafeCardKind((String) certRenewSession.get("SafeCardKind")); // 보안카드종류
		cbIbk01D00600Req.setYIJMGB("1"); // 개인

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) certRenewSession.get("SecurityIndex")); // 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) certRenewSession.get("SecurityIndex2")); // 안전카드 INDEX2

		cbIbk01D00600Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드 첫번째 입력값
		cbIbk01D00600Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드 두번째 입력값

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals((String) certRenewSession.get("SafeCardKind"))) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		CbIbk01D00600Res cbIbk01D00600Res = hostResponse.getResponse();

		log.debug("validateCertRenewSecurity 보안매체 처리 END");
		// 보안매체 전문 처리 END

		// RA갱신인가 처리
		log.debug("validateCertRenewSecurity RA갱신인가 처리");
		String deptPersonName = certUtils.byteStringConvert((String) certRenewSession.get("DeptPersonName"), 9);
		deptPersonName = deptPersonName + "()";
		String personOrCompanyCode = (String) certRenewSession.get("PersonOrCompanyCode");

		log.debug("validateCertRenewSecurity certrenew certPolicyCode :: {}", certPolicyCode);
		log.debug("validateCertRenewSecurity certrenew certOid :: {}", certOid);
		log.debug("validateCertRenewSecurity certrenew personOrCompanyCode :: {}", personOrCompanyCode);

		String oppUserId = CertConfig.RegCodeValue + userId;
		String juminNo = (String) certRenewSession.get("RegNo");
		String emailAddr = (String) certRenewSession.get("EmailAddr");
		String telenum = (String) certRenewSession.get("phoneNo");
		String telenumCell = "";
		String telenumHome = "";
		String telenumOffice = "";

		if (telenum != null && telenum != "") {
			if (telenum.startsWith("010") || telenum.startsWith("011") || telenum.startsWith("016") || telenum.startsWith("017") || telenum.startsWith("018") || telenum.startsWith("019")) {
				telenumCell = telenum;
			} else {
				telenumHome = telenum;
			}
		}

		// 갱신전문발송
		Map<String, String> renewResult = kftcCertComponent.renewCert(custGubun, oppUserId, juminNo, certPolicyCode, emailAddr, telenumCell, telenumHome, telenumOffice);
		String resCode = StringUtils.nvl(renewResult.get("ResCode"), "");

		if ("413".equals(resCode) || "412".equals(resCode)) {
			throw new PRCServiceException("PRCCRT20834", "고객의 인증서는 현재 폐기되었거나 효력정지인 상태이므로 갱신 불가합니다.");
		}

		if ("921".equals(resCode)) {
			throw new PRCServiceException("PRCCRT20832", "가입자 갱신 등록은 30 일 전부터 허용됩니다.");
		}

		if (!"000".equals(resCode)) {
			throw new PRCServiceException("PRCCRT20835", "갱신 처리중 오류가 발생하였습니다. 자세한 사항은 고객서비스센터(1588-1599)로 문의 하시기 바랍니다.");
		}

		/****************************************************************/
		/* 인증서의 유효기간 시작일과 만료일자를 생성해주는 루틴이다. */
		/* 갱신된 인증서의 만료일은 갱신전 만료일 + 1일부터 1년후이다. */
		/* 예) 원래 만료일 : 2003.12.01 */
		/* 갱신후 만료일 : 2004.12.02 */
		/* 테스트계는 갱신후 만료일은 원 만료일의 달수 + 1 이다. */
		/* 예) 원래 만료일 : 2003.12.01 */
		/* 갱신후 만료일 : 2004.01.01 */
		/* 이거 계산해 주는 루틴을 js파일로 만들도록 하자. */
		/* 필요한것이 */
		/* input값은 날짜 , 구분(Y:년, M:월), 증가치(1) */
		/* output값은 날짜 */
		/* Inserted by Kim Dong Hyuk on 2003.12.25 */
		/****************************************************************/
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		String ExpireDate = simpledateformat.format(userCert.getNotAfter());

		String issueDate = DateUtils.getCurrentDate();
		String oldExpireDate = certUtils.getFutureDate(ExpireDate, 1);
		String newExpireDate = certUtils.setExpireDate(oldExpireDate, CertConfig.TermGijun, CertConfig.fincertTermPeriod);

		log.debug("newExpireDate : {}", newExpireDate);

		certRenewSession.put("IssueDate", issueDate);
		certRenewSession.put("RAGubun", raGubun);
		certRenewSession.put("newExpireDate", newExpireDate);
		certRenewSession.put("CustGubun", custGubun);
		certRenewSession.put("onAgreeChk", request.getOnAgreeChk());
		certRenewSession.put("serial", certificateHelper.getSerialDecimal());

		sessionManager.setGlobalValue("certRenewSession", certRenewSession);

		// 응답 객체
		CrtJctValidateCertRenewSecurityResponse response = new CrtJctValidateCertRenewSecurityResponse();
		response.setSsrGubun(cbIbk01D00600Res.getSSRGubun());
		response.setSerial((String) certRenewSession.get("serial"));
		response.setCertIndex((String) certRenewSession.get("certIndex"));
		response.setEncData((String) certRenewSession.get("encData"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/payCertRenewPre", name = "공동인증서 갱신 수수료 납부 예비거래")
	public CrtJctPayCertRenewPreResponse payCertRenewPre(IServiceContext serviceContext, CrtJctPayCertRenewPreRequest request) {
		// ASIS :: MA3CRTCRT006_501S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRenewSession = certUtils.getCertSession("certRenewSession");

		log.debug("payCertRenewPre certRenewSession :: {}", certRenewSession);

		// 세금계산서 여부 및 메일발송 전문 처리 START
		log.debug("payCertRenewPre 세금계산서 여부 및 메일발송 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H981");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H981");
		hostRequestOptions.setSvcCd("981");

		// 개별부 세팅
		CbIbk01H98100Req cbIbk01H98100Req = new CbIbk01H98100Req();
		cbIbk01H98100Req.setUserID((String) certRenewSession.get("UserID"));
		cbIbk01H98100Req.setRegNo((String) certRenewSession.get("RegNo"));
		cbIbk01H98100Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		cbIbk01H98100Req.setCustGubun((String) certRenewSession.get("CustGubun")); // 발급자구분(1:개인,2:기업))
		cbIbk01H98100Req.setRAGubun((String) certRenewSession.get("RAGubun"));
		cbIbk01H98100Req.setSSRFee("4000");
		cbIbk01H98100Req.setSSRVat("400");
		cbIbk01H98100Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H98100Req.setSSRGubun("2");
		cbIbk01H98100Req.setIssueDate((String) certRenewSession.get("IssueDate"));
		cbIbk01H98100Req.setExpireDate((String) certRenewSession.get("ExpireDate"));
		cbIbk01H98100Req.setSSRAcctNum(request.getAcctNum());
		cbIbk01H98100Req.setAcctPasswd(request.getAcctBb());
		cbIbk01H98100Req.setTelCode1(request.getTel1());
		cbIbk01H98100Req.setTelCode2(request.getTel2());
		cbIbk01H98100Req.setTelCode3(request.getTel3());
		cbIbk01H98100Req.setTeleOne(request.getTel1());
		cbIbk01H98100Req.setTeleTwo(request.getTel2());
		cbIbk01H98100Req.setTeleThree(request.getTel3());

		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions, cbIbk01H98100Req, CbIbk01H98100Res.class);
		log.debug("payCertRenewPre 세금계산서 여부 및 메일발송 END");
		// 세금계산서 여부 및 메일발송 전문 처리 END

		CrtJctPayCertRenewPreResponse response = new CrtJctPayCertRenewPreResponse();
		response.setDeptPersonName((String) certRenewSession.get("DeptPersonName"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/payCertRenew", name = "공동인증서 갱신 수수료 납부 본거래")
	public CrtJctPayCertRenewResponse payCertRenew(IServiceContext serviceContext, CrtJctPayCertRenewRequest request) {
		// ASIS :: MA3CRTCRT006_511S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRenewSession = certUtils.getCertSession("certRenewSession");

		log.debug("payCertRenew certRenewSession :: {}", certRenewSession);

		// 세금계산서 여부 및 메일발송 전문 처리 START
		log.debug("payCertRenew 세금계산서 여부 및 메일발송 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D981");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D981");
		hostRequestOptions.setSvcCd("981");

		// 개별부 세팅
		CbIbk01H98100Req cbIbk01H98100Req = new CbIbk01H98100Req();
		cbIbk01H98100Req.setUserID((String) certRenewSession.get("UserID"));
		cbIbk01H98100Req.setRegNo((String) certRenewSession.get("RegNo"));
		cbIbk01H98100Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		cbIbk01H98100Req.setCustGubun((String) certRenewSession.get("CustGubun")); // 발급자구분(1:개인,2:기업))
		cbIbk01H98100Req.setRAGubun((String) certRenewSession.get("RAGubun"));
		cbIbk01H98100Req.setIssueReceipt("0"); // 세금계산서 발급구분
		cbIbk01H98100Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H98100Req.setSSRGubun("2");
		cbIbk01H98100Req.setSSRAcctNum(request.getAcctNum());
		cbIbk01H98100Req.setAcctPasswd(request.getAcctBb());
		cbIbk01H98100Req.setSSRFee("4000");
		cbIbk01H98100Req.setSSRVat("400");
		cbIbk01H98100Req.setTelCode1(request.getTel1());
		cbIbk01H98100Req.setTelCode2(request.getTel2());
		cbIbk01H98100Req.setTelCode3(request.getTel3());
		cbIbk01H98100Req.setTeleOne(request.getTel1());
		cbIbk01H98100Req.setTeleTwo(request.getTel2());
		cbIbk01H98100Req.setTeleThree(request.getTel3());
		cbIbk01H98100Req.setIssueDate((String) certRenewSession.get("IssueDate"));
		cbIbk01H98100Req.setExpireDate((String) certRenewSession.get("ExpireDate"));

		// 전문 전송
		OltpResponse<CbIbk01H98100Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H98100Req, CbIbk01H98100Res.class);
		CbIbk01H98100Res cbIbk01H98100Res = hostResponse.getResponse();
		log.debug("payCertRenew 세금계산서 여부 및 메일발송 END");
		// 세금계산서 여부 및 메일발송 전문 처리 END

		certRenewSession.put("SSRDate", cbIbk01H98100Res.getSSRDate());
		certRenewSession.put("SSRTime", cbIbk01H98100Res.getSSRTime());
		certRenewSession.put("SSRSecq", cbIbk01H98100Res.getSSRSecq());
		certRenewSession.put("SSRAcctNum", cbIbk01H98100Res.getSSRAcctNum());

		sessionManager.setGlobalValue("certRenewSession", certRenewSession);

		// 응답 객체
		CrtJctPayCertRenewResponse response = new CrtJctPayCertRenewResponse();
		response.setSsrDate(cbIbk01H98100Res.getSSRDate());
		response.setSsrTime(cbIbk01H98100Res.getSSRTime());
		response.setSsrSecq(cbIbk01H98100Res.getSSRSecq());
		response.setDeptPersonName(cbIbk01H98100Res.getDeptPersonName());
		response.setAcctNum(cbIbk01H98100Res.getSSRAcctNum());
		response.setSsrFee(cbIbk01H98100Res.getSSRFee());
		response.setSsrVat(cbIbk01H98100Res.getSSRVat());

		response.setYesSignCaIp((String) certRenewSession.get("YESSIGN_CAIP"));
		response.setYesSignCaPort((String) certRenewSession.get("YESSIGN_CAPORT"));
		response.setSerial((String) certRenewSession.get("serial"));
		response.setCertIndex((String) certRenewSession.get("certIndex"));
		response.setEncData((String) certRenewSession.get("encData"));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/authorizeCertRenew", name = "공동인증서 갱신 완료")
	public CrtJctAuthorizeCertRenewResponse authorizeCertRenew(IServiceContext serviceContext, CrtJctAuthorizeCertRenewRequest request) {
		// ASIS :: MA3CRTCRT006_701S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certRenewSession = certUtils.getCertSession("certRenewSession");

		log.debug("authorizeCertRenew certRenewSession :: {}", certRenewSession);

		// 갱신완료 전문 처리 START
		log.debug("authorizeCertRenew 갱신완료 처리 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certRenewSession.get("UserID"));
		cbIbk01D00600Req.setTSPassword((String) certRenewSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setRegNo((String) certRenewSession.get("RegNo"));
		cbIbk01D00600Req.setJuminSaupjaNo((String) certRenewSession.get("RegNo"));
		cbIbk01D00600Req.setCertTranCode("3"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setYIJMGB("1"); // 개인
		cbIbk01D00600Req.setRAGubun((String) certRenewSession.get("RAGubun")); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req.setIssueBank("023"); // 발급은행
		cbIbk01D00600Req.setIssueDate((String) certRenewSession.get("IssueDate"));
		cbIbk01D00600Req.setExpireDate((String) certRenewSession.get("newExpireDate"));
		cbIbk01D00600Req.setPageGubun("023");
		cbIbk01D00600Req.setInforGubun((String) certRenewSession.get("InforGubun"));
		cbIbk01D00600Req.setEmailAddr((String) certRenewSession.get("EmailAddr"));

		// 사전동의 발급 여부
		if ("Y".equals(String.valueOf(certRenewSession.get("onAgreeChk")))) {
			cbIbk01D00600Req.setYIAGREE("Y");
		}

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostResponse.getResponse();

		log.debug("authorizeCertRenew 갱신완료 처리 END");
		// 갱신완료 전문 처리 전문 처리 END

		log.debug("authorizeCertRenew newExpireDate :: {}", certRenewSession.get("newExpireDate"));

		// 응답 객체
		CrtJctAuthorizeCertRenewResponse response = new CrtJctAuthorizeCertRenewResponse();
		response.setExpireDate((String) certRenewSession.get("newExpireDate"));
		response.setDeptPersonName((String) certRenewSession.get("DeptPersonName"));

		// SMS발송
		String userId = (String) certRenewSession.get("UserID");
		String tsPassword = (String) certRenewSession.get("TSPassword");
		String custName = (String) certRenewSession.get("DeptPersonName");
		String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
		String smsMsg = smsCustName + "님 인증서가 갱신되었어요.";

		smsComponent.sendCompleteSMS(userId, tsPassword, (String) certRenewSession.get("RegNo"), smsMsg);

		// 세션 삭제
		sessionManager.removeGlobalValue("certRenewSession");

		log.debug("🍕 {} 종료 🍕", new Object() {
		}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateCertRevokeUser", name = "공동인증서 폐기 본인확인")
	public CrtJctValidateCertRevokeUserResponse validateCertRevokeUser(IServiceContext serviceContext, CrtJctValidateCertRevokeUserRequest request) {
		// ASIS :: MA3CRTCRT007_101S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 폐기 세션 초기화
		sessionManager.removeGlobalValue("certDisposalSession");

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 본인확인 전문 처리 START
		log.debug("validateCertRevokeUser 본인확인 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
		cbIbk01H00601Req.setCustJumin1(request.getCustJumin1());
		cbIbk01H00601Req.setCustJumin2(request.getCustJumin2());
		cbIbk01H00601Req.setUserID(StringUtils.upperCase(request.getUserId()));
		cbIbk01H00601Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		cbIbk01H00601Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00601Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		cbIbk01H00601Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00601Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00601Req, CbIbk01H00601Res.class);
		CbIbk01H00601Res cbIbk01H00601Res = hostResponse.getResponse();
		log.debug("validateCertRevokeUser 본인확인 END");

		// 본인 인증한 데이터 세션에 담는다
		Map<String, Object> certDisposalSession = new HashMap<>();
		certDisposalSession.put("UserID", cbIbk01H00601Res.getUserID());
		certDisposalSession.put("TSPassword", cbIbk01H00601Res.getTSPassword());
		certDisposalSession.put("RegNo", cbIbk01H00601Res.getRegNo());
		certDisposalSession.put("PersonOrCompanyCode", cbIbk01H00601Res.getPersonOrCompanyCode());
		certDisposalSession.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		certDisposalSession.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		certDisposalSession.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
		certDisposalSession.put("DeptPersonName", cbIbk01H00601Res.getDeptPersonName());
		certDisposalSession.put("SaupjaNo", cbIbk01H00601Res.getSaupjaNo());
		certDisposalSession.put("ZipCode", cbIbk01H00601Res.getZipCode());
		certDisposalSession.put("Address", cbIbk01H00601Res.getAddress());
		certDisposalSession.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
		certDisposalSession.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
		certDisposalSession.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
		certDisposalSession.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
		certDisposalSession.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
		certDisposalSession.put("AuthSVC4CERT", cbIbk01H00601Res.getAuthSVC4CERT());
		certDisposalSession.put("YOIDGB", cbIbk01H00601Res.getYOIDGB()); // 이용등급
		certDisposalSession.put("InforGubun", cbIbk01H00601Res.getInforGubun());

		sessionManager.setGlobalValue("certDisposalSession", certDisposalSession);
		log.debug("validateCertRevokeUser certDisposalSession :: {}", certDisposalSession);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctValidateCertRevokeUserResponse();
	}

	@ServiceEndpoint(url = "/validateCertRevokeAcct", name = "공동인증서 폐기 계좌 검증")
	public CrtJctValidateCertRevokeAcctResponse validateCertRevokeAcct(IServiceContext serviceContext, CrtJctValidateCertRevokeAcctRequest request) {
		// ASIS :: MA3CRTCRT007_201S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certDisposalSession = certUtils.getCertSession("certDisposalSession");

		log.debug("validateCertRevokeAcct certDisposalSession :: {}", certDisposalSession);

		// 본인확인 전문 처리 START
		log.debug("validateCertRevokeAcct 계좌검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setRegNo((String) certDisposalSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setUserID((String) certDisposalSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setSSRAcctNum(request.getAcctNum()); // 계좌번호
		cbIbk01D00600Req.setAcctPasswd(request.getAcctBb()); // 계좌비밀번호
		cbIbk01D00600Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("C"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setEmailAddr((String) certDisposalSession.get("EmailAddr"));

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostH006Response.getResponse();
		log.debug("validateCertRevokeAcct 계좌검증 END");

		String personOrCompanyCode = (String) certDisposalSession.get("PersonOrCompanyCode");
		String userId = (String) certDisposalSession.get("UserID");
		String oppUserId = "";
		String deptPersonName = "";

		// 올바른 사용자 임.
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2") || personOrCompanyCode.equals("3")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert((String) certDisposalSession.get("DeptPersonName"), 9);
			deptPersonName = deptPersonName + "()";
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		String policy = "04";
		List<LdapInfoResult> certList = ldapInfoDao.selectForDelete(policy, userId);

		// 조합번호용 인증서 - 2023.05.27 ssh
		// 실명증표구분이 3-여권조합번호 이고, 인증서정책 04로 조회한 항목의 수가 0일 경우,
		// 인증서정책 84로 발급된 항목이 있는지도 체크해야 한다.
		// 조합번호이용고객이 유효한 04 기발급항목이 있을수도 있고, 84로 신규발급이 될 것이기 때문에 추가한 로직임.
		// 추후에는 실명증표구분이 3-여권조합번호일 경우 84로만 체크하면 된다.
		if (certList.size() == 0 && cbIbk01D00600Res.getYOCFJMGB().equals("3")) {
			policy = "84";

			certList = ldapInfoDao.selectForDelete(policy, userId);
		}
		log.debug("validateCertRevokeAcct certList :: {}", certList);

		String delUserId = userId + "__DELETECERT__USER_ID";
		Map<String, Object> oppraResult = certUtils.getOppResult(delUserId, oppUserId, deptPersonName);
		certDisposalSession.put("oppraResult", oppraResult); // RA정보
		sessionManager.setGlobalValue("certDisposalSession", certDisposalSession);

		// 응답 객체
		CrtJctValidateCertRevokeAcctResponse response = new CrtJctValidateCertRevokeAcctResponse();
		response.setCertList(certList.stream().map(cert -> {
			CrtJctValidateCertRevokeAcctResponse.CertListRecord record = new CrtJctValidateCertRevokeAcctResponse.CertListRecord();
			record.setCustname(cert.getCustname());
			record.setExpiredate(cert.getExpiredatestr());
			record.setPolicy(cert.getPolicy());
			record.setIssuedate(cert.getIssuedatestr());
			return record;
		}).toList());

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/authorizeCertRevoke", name = "공동인증서 폐기 완료")
	public CrtJctAuthorizeCertRevokeResponse authorizeCertRevoke(IServiceContext serviceContext, CrtJctAuthorizeCertRevokeRequest request) {
		// ASIS :: MA3CRTCRT007_301S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		Map<String, Object> certDisposalSession = certUtils.getCertSession("certDisposalSession");
		Map<String, Object> oppraResult = (Map<String, Object>) certDisposalSession.get("oppraResult");

		String certPolicy = request.getCertPolicy();
		String custGubun = "1";
		String raGubun = "1";
		String sCertPolicy = "FINANCIAL_RESTRICT";

		if (certPolicy.equals("01")) { // 개인범용
			raGubun = "2";
			sCertPolicy = "FINANCIAL";
		}

		String serialValue = "";
		String ispFinancialRestrict = String.valueOf(oppraResult.get("IS_P_FINANCIAL_RESTRICT"));
		String pFrStatus = String.valueOf(oppraResult.get("P_FINANCIAL_RESTRICT_STATUS"));
		String pFrSerial = String.valueOf(oppraResult.get("P_FINANCIAL_RESTRICT_SERIAL"));
		String isPFinancial = String.valueOf(oppraResult.get("IS_P_FINANCIAL"));
		String pFStatus = String.valueOf(oppraResult.get("P_FINANCIAL_STATUS"));
		String pFSerial = String.valueOf(oppraResult.get("P_FINANCIAL_SERIAL"));
		String isCFinancial = String.valueOf(oppraResult.get("IS_C_FINANCIAL"));
		String cFStatus = String.valueOf(oppraResult.get("C_FINANCIAL_STATUS"));
		String cFSerial = String.valueOf(oppraResult.get("C_FINANCIAL_SERIAL"));
		String isBusiness = String.valueOf(oppraResult.get("IS_BUSINESS"));
		String bStatus = String.valueOf(oppraResult.get("BUSINESS_STATUS"));
		String bSerial = String.valueOf(oppraResult.get("BUSINESS_SERIAL"));
		String isEseroRestrict = String.valueOf(oppraResult.get("IS_ESERO_RESTRICT"));
		String eStatus = String.valueOf(oppraResult.get("ESERO_RESTRICT_STATUS"));
		String eSerial = String.valueOf(oppraResult.get("ESERO_RESTRICT_SERIAL"));
		String personOrCompanyCode = String.valueOf(certDisposalSession.get("PersonOrCompanyCode"));

		String issuerType = "p_personal"; // 개인사용자
		String userType = "P";
		if ("2".equals(certDisposalSession.get("PersonOrCompanyCode"))) { // 개인 사업자
			issuerType = "pc_personal"; // 개인사용자
			userType = "PP";
		}

		if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL_RESTRICT") && ispFinancialRestrict.equals("YES") && pFrStatus.equals("INVALID")) {
			// 개인 고객의 금융용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCCRT20218", "고객님의 공동인증서가 만료되어 폐기되었거나 이미 폐기된 인증서라 폐기할 수 없어요");
		} else if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL_RESTRICT") && ispFinancialRestrict.equals("NO")) {
			// 개인 고객이 금융용 인증서를 받지 않은 상태에서 폐지할려고 함.
			throw new PRCServiceException("PRCCRT20219", "고객(개인)께서는 금융용 인증서를 받지 않은 상태이므로 폐기 할 수 없습니다.");
		} else if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL_RESTRICT") && ispFinancialRestrict.equals("YES") && (pFrStatus.equals("VALID") || pFrStatus.equals("SUSPEND"))) {
			// 개인 고객이 금융용 인증서가 유효하거나 효력정지인 상태에서 폐기 하려하면 정상 폐기 처리.
			serialValue = pFrSerial;
		} else if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("INVALID")) {
			// 개인 고객의 범용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCCRT20248", " 고객(개인)의 범용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기 할 수 없습니다.");
		} else if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL") && isPFinancial.equals("NO")) {
			// 개인 고객이 범용 인증서를 받지 않은 상태에서 폐지할려고 함.
			throw new PRCServiceException("PRCCRT20249", " 고객(개인)께서는 범용 인증서를 받지 않은 상태이므로 폐기 할 수 없습니다.");
		} else if (personOrCompanyCode.equals("1") && sCertPolicy.equals("FINANCIAL") && isPFinancial.equals("YES") && (pFStatus.equals("VALID") || pFStatus.equals("SUSPEND"))) {
			// 개인 고객이 범용 인증서가 유효하거나 효력정지인 상태에서 폐기 하려하면 정상 폐기 처리.
			serialValue = pFSerial;
		} else if (issuerType.equals("pc_personal") && sCertPolicy.equals("FINANCIAL_RESTRICT") && ispFinancialRestrict.equals("NO")) {
			// 개인사업자의 개인 금융용 공인인증서가 존재하지 않음.
			throw new PRCServiceException("PRCCRT20221", " 고객(개인사업자)의 개인 금융용 공인인증서가 존재하지 않으므로 폐기 할 수 없습니다.");
		} else if (issuerType.equals("pc_personal") && sCertPolicy.equals("FINANCIAL_RESTRICT") && ispFinancialRestrict.equals("YES") && pFrStatus.equals("INVALID")) {
			// 개인사업자의 개인 금융용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCCRT20222", "고객(개인사업자)의 개인 금융용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기할 수 없습니다.");
		} else if (issuerType.equals("pc_personal") && ispFinancialRestrict.equals("YES") && (pFrStatus.equals("VALID") || pFrStatus.equals("SUSPEND"))) {
			// 개인사업자의 개인 금융용 공인인증서가 유효하거나 효력정지인 상태에서 폐기하려면 정상 폐기 처리
			serialValue = pFrSerial;
		} else if (issuerType.equals("pc_personal") && sCertPolicy.equals("FINANCIAL") && isPFinancial.equals("NO")) {
			// 개인사업자의 개인범용 인증서 폐기할때 처리
			// 개인사업자의 개인 범용 공인인증서가 존재하지 않음.
			throw new PRCServiceException("PRCCRT20251", " 고객(개인사업자)의 개인 범용 공인인증서가 존재하지 않으므로 폐기 할 수 없습니다.");
		} else if (issuerType.equals("pc_personal") && sCertPolicy.equals("FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("INVALID")) {
			// 개인사업자의 개인 범용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태임.
			throw new PRCServiceException("PRCCRT20252", " 고객(개인사업자)의 개인 범용 공인인증서가 만료되어 폐기되었거나, 이미 폐기된 상태이므로 폐기할 수 없습니다.");
		} else if (issuerType.equals("pc_personal") && isPFinancial.equals("YES") && (pFStatus.equals("VALID") || pFStatus.equals("SUSPEND"))) {
			// 개인사업자의 개인 범용 공인인증서가 유효하거나 효력정지인 상태에서 폐기하려면 정상 폐기 처리
			serialValue = pFSerial;
		}

		// 폐기 전문 처리 START
		log.debug("authorizeCertRevoke 폐기 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setRegNo((String) certDisposalSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setUserID((String) certDisposalSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certDisposalSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setIssueBank("0023"); // 인증서발급은행(은행코드)
		cbIbk01D00600Req.setJuminSaupjaNo((String) certDisposalSession.get("RegNo")); // 주민사업자번호
		cbIbk01D00600Req.setIssueDate(request.getCertIssueDate()); // 발급일
		cbIbk01D00600Req.setExpireDate(request.getCertExpireDate()); // 만료일
		cbIbk01D00600Req.setRAGubun(raGubun);
		cbIbk01D00600Req.setEmailAddr((String) certDisposalSession.get("EmailAddr"));

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostH006Response = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostH006Response.getResponse();
		log.debug("authorizeCertRevoke 폐기 END");
		// 폐기 전문 처리 END

		personOrCompanyCode = cbIbk01D00600Res.getPersonOrCompanyCode();
		String userId = cbIbk01D00600Res.getUserID();
		String oppUserId = "";
		String deptPersonName = cbIbk01D00600Res.getDeptPersonName();

		oppUserId = CertConfig.RegCodeValue + userId;
		deptPersonName = certUtils.byteStringConvert(deptPersonName, 9);
		deptPersonName = deptPersonName + "()";

		Map<String, String> opprartn = null;

		try {
			log.debug("##authorizeCertRevoke certUtils.deleteCert > serialValue= {} || certPolicy -> {}", serialValue, certPolicy);
			opprartn = kftcCertComponent.changeStatusCert(serialValue, certPolicy, "30");
			if (opprartn == null) {
				throw new PRCServiceException("PRCCRT20213", "공인인증 등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}
		} catch (PRCServiceException e) {
			e.printStackTrace();
			throw new PRCServiceException("PRCCRT20213", "공인인증 등록기관 시스템 점검중으로 고객의 인증서를 폐기 할 수 없습니다. 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		}

		// 응답 객체
		CrtJctAuthorizeCertRevokeResponse response = new CrtJctAuthorizeCertRevokeResponse();
		response.setSerialValue(serialValue);

		// 인증서 폐기 SMS발송
		try {
			String custName = String.valueOf(certDisposalSession.get("DeptPersonName")).trim();
			String regNo = String.valueOf(certDisposalSession.get("RegNo")).trim();
			String h006UserId = String.valueOf(certDisposalSession.get("UserID")).trim();
			String tsPassword = String.valueOf(certDisposalSession.get("TSPassword")).trim();
			String emailAddr = String.valueOf(certDisposalSession.get("EmailAddr")).trim();

			if (custName.length() > 4)
				custName = custName.substring(0, 4);

			// SMS 고객명 마스킹 개선
			String smsCustName = BizCommonUtils.getMaskCustData(custName, "01");
			String smsMsg = smsCustName + "님 공동인증서 폐기완료. 본인요청 아닐 시 신고요망";

			// SMS전송
			smsComponent.sendCompleteSMS(h006UserId, tsPassword, regNo, smsMsg);

		} catch (Exception e) {
			// SMS 발송실패 시
			log.error("authorizeCertRevoke 공동인증서폐기_SMS발송오류_start");
			e.printStackTrace();
			log.error("authorizeCertRevoke 공동인증서폐기_SMS발송오류_end");

		}

		// 세션 삭제
		sessionManager.removeGlobalValue("certDisposalSession");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getOtherCertStatus", name = "공동인증서 타기관인증서 조회")
	public CrtJctGetOtherCertStatusResponse getOtherCertStatus(IServiceContext serviceContext, CrtJctGetOtherCertStatusRequest request) {
		// ASIS :: MA3CRTCRT001_102S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("certIssueSession");

		// 본인 인증한 데이터 세션에 저장
		Map<String, Object> certIssueSession = new HashMap<>();
		certIssueSession.put("CustJumin1", request.getCustJumin1());
		certIssueSession.put("CustJumin2", request.getCustJumin2());
		sessionManager.setGlobalValue("certIssueSession", certIssueSession);

		// 응답 객체
		CrtJctGetOtherCertStatusResponse response = new CrtJctGetOtherCertStatusResponse();
		response.setErrorYn("N");

		// 고객확인
		String userId = CommonBizConstants.DEFAULT_USER_ID; // "FIRST999"
		String tsPassword = CommonBizConstants.DEFAULT_TS_PASS_WORD; // "111111"
		String yiJMNO = request.getCustJumin1() + request.getCustJumin2();

		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H92C");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H92C");
		hostRequestOptions.setSvcCd("92C");

		// 개별부 세팅
		CbIbk01H92C00Req cbIbk01H92C00Req = new CbIbk01H92C00Req();
		cbIbk01H92C00Req.setUserID(userId);
		cbIbk01H92C00Req.setTSPassword(tsPassword);
		cbIbk01H92C00Req.setYIJMNO(yiJMNO); // 주민등록번호
		cbIbk01H92C00Req.setYICMFNA(""); // 사용자이름
		cbIbk01H92C00Req.setYIIDGB("2"); // 로그인구분(1:로그인, 2:로그아웃(비로그인))
		cbIbk01H92C00Req.setYIJHGB("3"); // 거래구분(1:당행고객여부조회, 2:당행고객신규, 3:온라인CASA체크, 4:온라인CASA신규

		OltpResponse<CbIbk01H92C00Res> hostResponse = null;
		CbIbk01H92C00Res cbIbk01H92C00Res = null;
		try {
			// 전문 전송
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H92C00Req, CbIbk01H92C00Res.class);
			cbIbk01H92C00Res = hostResponse.getResponse();
			log.debug("getOtherCertStatus hostResponse :: {}", hostResponse);
			log.debug("getOtherCertStatus cbIbk01H92CRes :: {}", cbIbk01H92C00Res);
		} catch (Exception e) {
			response.setErrorYn("Y");
			response.setErrorCode("h92cError");
			return response;
		}

		String ckExist = StringUtils.nvl(cbIbk01H92C00Res.getYOEXIST(), "");
		String ckIbuser = StringUtils.nvl(cbIbk01H92C00Res.getYOIBUSER(), "");
		response.setYoIbUser(cbIbk01H92C00Res.getYOIBUSER());
		if (!ckExist.equals("Y") || !ckIbuser.equals("Y")) { // 당행 전자금융 고객인지만 체크 개인정보 등으로 그외 정보 x
			response.setErrorYn("Y");
			response.setErrorCode("h92cCkError");
			return response;
		}

		// 전문코드 세팅
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("H892");
		hostRequestOptions2.setSvcCd("892");

		// 개별부 세팅
		CbIbk01H89201Req cbIbk01H89201Req = new CbIbk01H89201Req();
		cbIbk01H89201Req.setUserID(userId);
		cbIbk01H89201Req.setCustJumin1(request.getCustJumin1());
		cbIbk01H89201Req.setCustJumin2(request.getCustJumin2());
		cbIbk01H89201Req.setActType("3"); // 처리구분 ( 예비처리 ) 1: 고객정보조회( 본처리 ) 2: 계좌검증 3: 계좌미검증 4: 법인 ID 검증 5: 신용카드조회회원
		cbIbk01H89201Req.setAcctNum("");

		int ckLdapInfoCnt = 0;
		try {
			// 전문 전송
			OltpResponse<CbIbk01H89201Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H89201Req, CbIbk01H89201Res.class);
			CbIbk01H89201Res cbIbk01H89201Res = hostResponse2.getResponse();

			// 당행 인증서가 있는지 확인 있을 경우 금결원 조회 하지 않음
			List<LdapInfoResult> resultGetOppraLdapInfoUserid = ldapInfoDao.selectOppraLdapInfoByUserid(cbIbk01H89201Res.getUserID2());

			for (LdapInfoResult itemList : resultGetOppraLdapInfoUserid) {
				String policy = StringUtils.defaultIfEmpty(itemList.getPolicy(), ""); // 04: 개인 공동
				String raflag = StringUtils.defaultIfEmpty(itemList.getRaflag(), ""); // 1 이면 정상
				if (policy.equals("04") && raflag.equals("1")) { // 04 개인 공동 정상 상태 이면
					ckLdapInfoCnt++;
				}
			}
			log.debug("===================rckLdapInfoCnt {}", ckLdapInfoCnt);
			log.debug("===================rckLdapInfoCnt {}", resultGetOppraLdapInfoUserid.toString());
		} catch (Exception e) {
			// 오류 케이스 잡지 않음 뒤쪽에서 잡히는 걸로..
		}

		if (ckLdapInfoCnt < 1) {

			// 인증서 조회
			Map<String, String> searchCertMap = kftcCertComponent.searchCert("04", yiJMNO);
			response.setOpprRes(searchCertMap.get("opprRes"));

			OpprJdp opprJdp = new OpprJdp();
			opprJdp.setResCode(searchCertMap.get("ResCode"));
			opprJdp.setCkOppr(null);
			opprJdp.setCertStatus(searchCertMap.get("CERTSTATUS"));
			opprJdp.setCertPolicy(searchCertMap.get("CERTPOLICY"));
			response.setOpprJdp(opprJdp);

		}

		response.setCkLdapInfoCnt(String.valueOf(ckLdapInfoCnt));

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/validateOtherCertRegUser", name = "공동인증서 타기관 등록 본인확인")
	public CrtJctValidateOtherCertRegUserResponse validateOtherCertRegUser(IServiceContext serviceContext, CrtJctValidateOtherCertRegUserRequest request) {
		// ASIS :: MA3CRTCRT002_101S + MA3CRTCRT002_301S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 등록 세션 초기화
		sessionManager.removeGlobalValue("certRegSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 아이디 찾기 전문 처리 START
		log.debug("validateOtherCertRegUser 아이디 찾기 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H892");
		hostRequestOptions.setSvcCd("892");

		// 개별부 세팅
		CbIbk01H89201Req cbIbk01H89201Req = new CbIbk01H89201Req();
		cbIbk01H89201Req.setUserID(CommonBizConstants.DEFAULT_USER_ID); // 아이디 "FIRST999"
		cbIbk01H89201Req.setCustJumin1(request.getCustJumin1()); // 실명번호1
		cbIbk01H89201Req.setCustJumin2(request.getCustJumin2()); // 계좌비밀번호
		cbIbk01H89201Req.setActType("1"); // 처리구분. 1:고객정보조회(예비처리), 2:계좌검증(본처리), 3:계좌미검증(본처리)
		cbIbk01H89201Req.setAcctNum(" ");

		// 전문 전송
		OltpResponse<CbIbk01H89201Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H89201Req, CbIbk01H89201Res.class);
		CbIbk01H89201Res cbIbk01H89201Res = hostResponse.getResponse();
		log.debug("validateOtherCertRegUser 아이디 찾기 END");
		// 아이디 찾기 전문 처리 END

		if ("4".equals(cbIbk01H89201Res.getUserType()) || "5".equals(cbIbk01H89201Res.getUserType())) {
			throw new PRCServiceException("PRCCRT0009", "인터넷뱅킹 미가입 고객입니다.");
		}

		// 본인확인 전문 처리 START
		log.debug("validateOtherCertRegUser 본인확인 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("H006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
		cbIbk01H00601Req.setUserID(cbIbk01H89201Res.getUserID2());
		cbIbk01H00601Req.setCustJumin1(request.getCustJumin1());
		cbIbk01H00601Req.setCustJumin2(request.getCustJumin2());
		cbIbk01H00601Req.setSimplifyGB("2"); // 인증서간소화 체크
		cbIbk01H00601Req.setCertTranCode("6"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		cbIbk01H00601Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00601Req.setCAGubun("3"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		cbIbk01H00601Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01H00601Req.setYIGSGB("1"); // 인증서간소발급자

		// 전문 전송
		OltpResponse<CbIbk01H00601Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H00601Req, CbIbk01H00601Res.class);
		CbIbk01H00601Res cbIbk01H00601Res = hostResponse2.getResponse();
		log.debug("validateOtherCertRegUser 본인확인 END");
		// 본인확인 전문 처리 END

		// 응답 객체
		CrtJctValidateOtherCertRegUserResponse resposne = new CrtJctValidateOtherCertRegUserResponse();

		// S-개인정보 노출자 여부판단 로직 추가
		log.debug("validateCertIssue 개인정보 노출자 여부 판단 시작");
		Map<String, String> resH504Map = certificationSharedComponent.getYoLRSOTGB(cbIbk01H00601Res.getRegNo());
		String yoLRSOTGB = resH504Map.get("YOLRSOTGB");
		log.debug("validateCertIssue resH504Map :: {}", resH504Map);

		// 개인정보노출등록구분 값이 1이면 거래불가 고객으로 안내페이지로 이동
		if ("1".equalsIgnoreCase(yoLRSOTGB)) {
			sessionManager.removeGlobalValue("certRegSession");
			sessionManager.removeGlobalValue("PerBusNo");
			sessionManager.removeGlobalValue("CustName");

			resposne.setYoLRSOTGB(yoLRSOTGB); // 개인정보 노출자 여부 판단값(1:차단)
			return resposne;
		}
		log.debug("validateCertIssue 개인정보 노출자 여부 판단 종료");
		// E-개인정보 노출자 여부판단 로직 추가

		String personOrCompanyCode = cbIbk01H00601Res.getPersonOrCompanyCode();
		String userId = cbIbk01H00601Res.getUserID();

		String oppUserId = "";
		String deptPersonName = "";

		// 올바른 사용자
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2") || personOrCompanyCode.equals("3")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H00601Res.getDeptPersonName(), 9);
			deptPersonName = deptPersonName + "()";
		} else {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// RA사용자 조회
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		if ("3".equals(cbIbk01H00601Res.getPersonOrCompanyCode())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		// 개인사업자는 오류
		if ("13".equals(cbIbk01H00601Res.getYOIDGB()) || "14".equals(cbIbk01H00601Res.getYOIDGB())) {
			throw new PRCServiceException("PRCCRT0010", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}

		// 본인 인증한 데이터 세션에 담는다
		Map<String, Object> certRegSession = new HashMap<>();
		certRegSession.put("UserID", cbIbk01H00601Res.getUserID());
		certRegSession.put("TSPassword", cbIbk01H00601Res.getTSPassword());
		certRegSession.put("RegNo", cbIbk01H00601Res.getRegNo());
		certRegSession.put("PersonOrCompanyCode", cbIbk01H00601Res.getPersonOrCompanyCode());
		certRegSession.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		certRegSession.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		certRegSession.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
		certRegSession.put("DeptPersonName", cbIbk01H00601Res.getDeptPersonName());
		certRegSession.put("SaupjaNo", cbIbk01H00601Res.getSaupjaNo());
		certRegSession.put("ZipCode", cbIbk01H00601Res.getZipCode());
		certRegSession.put("Address", cbIbk01H00601Res.getAddress());
		certRegSession.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
		certRegSession.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
		certRegSession.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
		certRegSession.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
		certRegSession.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
		certRegSession.put("AuthSVC4CERT", cbIbk01H00601Res.getAuthSVC4CERT());
		certRegSession.put("InforGubun", cbIbk01H00601Res.getInforGubun());
		certRegSession.put("CustGubun", cbIbk01H00601Res.getCustGubun());
		certRegSession.put("TelCode", cbIbk01H00601Res.getTelCode());
		certRegSession.put("oppraResult", oppraResult); // RA정보

		sessionManager.setGlobalValue("certRegSession", certRegSession);
		sessionManager.setGlobalValue("PerBusNo", cbIbk01H00601Res.getRegNo());
		sessionManager.setGlobalValue("CustName", cbIbk01H00601Res.getDeptPersonName());

		// 추가인증 세션값 세팅
		String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00601Res.getTelCode());
		sessionManager.setGlobalValue("TeleOne", telCode[0]);
		sessionManager.setGlobalValue("TeleTwo", telCode[1]);
		sessionManager.setGlobalValue("TeleThree", telCode[2]);
		sessionManager.setGlobalValue("UserID", (String) certRegSession.get("UserID"));
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		sessionManager.setGlobalValue("SafeCardSeq1", cbIbk01H00601Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", cbIbk01H00601Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", cbIbk01H00601Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", cbIbk01H00601Res.getSecurityIndex());
		sessionManager.setGlobalValue("SafeCardINDEX2", cbIbk01H00601Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		sessionManager.setGlobalValue("TSPassword", cbIbk01H00601Res.getTSPassword());
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return resposne;
	}

	@ServiceEndpoint(url = "/validateOtherCertRegCertificate", name = "공동인증서 타기관 등록 인증서 유효성 검사")
	public CrtJctValidateOtherCertRegCertificateResponse validateOtherCertRegCertificate(IServiceContext serviceContext, CrtJctValidateOtherCertRegCertificateRequest request) throws Exception {
		// ASIS :: MA3CRTCRT002_201S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		String pkcs7SignedData = request.getPkcs7SignedData();
		String vidRandom = request.getVidRandom();

		// vid값 추출
		Map<String, Object> certVerify = certVerifyComponent.verify("A", pkcs7SignedData, vidRandom);
//		vidRandom = (String) certVerify.get("_shttp_client_vid_random_");

		// 인증서 userCert 값 추출
		Map<String, Object> submitCertVerify = certVerifyComponent.submitCertVerify(pkcs7SignedData);
		X509Certificate userCert = certUtils.getCertificationFromB64((String) submitCertVerify.get("userCert"));

		// 인증서 정보 세션
		Map<String, Object> certRegSession = certUtils.getCertSession("certRegSession");

		String deptPersonName = certUtils.byteStringConvert((String) certRegSession.get("DeptPersonName"), 9);
		String personOrCompanyCode = (String) certRegSession.get("PersonOrCompanyCode");
		String userSerialStr = "";

		if (userCert != null) {
			userSerialStr = certUtils.formatString(userCert.getSerialNumber().toString(10));
		}

		if (!personOrCompanyCode.equals("1") && !personOrCompanyCode.equals("2")) {
			throw new PRCServiceException("PRCCRT30114", "고객님의 개인 법인 구분을 알 수 없습니다. 자세한 내용은 관리자에게 문의하시기 바랍니다.");
		}

		// 인증서 정보 추출 클래스 생성
		CertificateHelper certificateHelper = new CertificateHelper(userCert);
		if (certificateHelper.getIssuerBank().equals("KFB")) {
			throw new PRCServiceException("PRCCRT20519", "SC은행에서 발급받으신 사설인증서로 타기관 공인인증서 사용신청을 하실 수 없습니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
		}

		String issuerO = certificateHelper.getIssuerO();
		String certPolicyCode = certificateHelper.getPolicyCode();

		if (issuerO == null)
			issuerO = "";
		String custGubun = (certPolicyCode.equals("01") || certPolicyCode.equals("04")) ? "1" : "2";
		String issueBank = "";
		String caGubun = "";

		/*************************************************/
		/* 인증기관 코드 변환 루틴 */
		/* Inserted by Kim Dong Hyuk on 2004.06.08 */
		/*************************************************/
		if (issuerO.equals("KICA")) {
			caGubun = "3";
		} else if (issuerO.equals("SignKorea")) {
			caGubun = "4";
		} else if (issuerO.equals("NCA")) {
			caGubun = "5";
		} else if (issuerO.equals("CrossCert")) {
			caGubun = "6";
		} else if (issuerO.equals("TradeSign")) {
			caGubun = "7";
		} else {
			caGubun = "2";
			issueBank = certificateHelper.getIssuerBankCode();
			if (issueBank != null && issueBank.length() > 3) {
				/********* 은행코드에서 뒤에 세자리만 사용 *********/
				issueBank = issueBank.substring(1);
			} else {
				issueBank = "";
			}
		}

		String issuerDn = "";
		String subjectDn = "";

		if (userCert != null) {
			issuerDn = certificateHelper.getIssuerDN();
			subjectDn = certificateHelper.getSubjectDN();

		}

		/****************************************************************/
		/* 인증서의 만료일자를 얻어오는 루틴이다. */
		/* Inserted by Kim Dong Hyuk on 2004.06.08 */
		/****************************************************************/
		SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyyMMdd");
		String expireDate = "";
		if (userCert != null)
			expireDate = simpledateFormat.format(userCert.getNotAfter());

		// 인증서 등록일
		String issueDate = DateUtils.getCurrentDate();

		/******************************************
		 * 제출한 인증서가 본인인지 확인
		 *******************************************/
		if (userCert == null) {
			throw new PRCServiceException("PRCCRT9001", "인증서 오류");
		}

		String regNo = (String) certRegSession.get("RegNo");
		String saupjaNo = (String) certRegSession.get("SaupjaNo");

		log.debug("validateOtherCertRegCertificate CERTTASK LOG usercert :: {}", userCert);
		log.debug("validateOtherCertRegCertificate CERTTASK LOG vidRandom :: {}", vidRandom);
		log.debug("validateOtherCertRegCertificate CERTTASK LOG regNo :: {}", regNo);
		log.debug("validateOtherCertRegCertificate CERTTASK LOG saupjaNo :: {}", saupjaNo);

		try {
			certVerifyComponent.vidVerify(submitCertVerify, regNo, saupjaNo, vidRandom);
		} catch (PRCServiceException e) {
			throw new PRCServiceException(e.getErrorCode(), e.getErrorMessage());
		} catch (Exception e) {
			throw new PRCServiceException("PRCCRT9004", "제출하신 인증서의 확인에 실패하였습니다.");
		}

		certRegSession.put("CAGubun", caGubun);
		certRegSession.put("IssueDate", issueDate);
		certRegSession.put("ExpireDate", expireDate);
		certRegSession.put("IssueBank", issueBank);
		certRegSession.put("userSerialStr", userSerialStr);
		certRegSession.put("userCert", (String) submitCertVerify.get("userCert"));
		sessionManager.setGlobalValue("certRegSession", certRegSession);

		// 금결원 공인인증서를 제출했는지를 체크.
		// 금결원 공인인증서는 타 은행에서 발급받은 금결원 공인인증서 등록 페이지에서 등록
		if (issuerDn.indexOf("yessignCA") > -1) {
			String sCode = "NOTEXIST";
			String imsi = null;
			String imsi1 = null;

			StringTokenizer token = new StringTokenizer(subjectDn, "OU=");

			if (!token.hasMoreTokens()) {
				sCode = "NOTEXIST";
			} else {
				while (token.hasMoreTokens()) {
					imsi = token.nextToken();
					StringTokenizer _token = new StringTokenizer(imsi, ",");

					while (_token.hasMoreTokens()) {
						imsi1 = _token.nextToken();

						if (imsi1 == null) {
							sCode = "NOTEXIST";
							break;
						} else if (imsi1.equals("")) {
							sCode = "NOTEXIST";
							break;
						} else if (imsi1.equals(CertConfig.RegBank)) {
							sCode = CertConfig.RegCodeValue;
							break;
						}
					}
				}
			}

			String userCertIssuerOrg = "";
			if ((subjectDn.indexOf("OU=corporation4EC") != -1) || (subjectDn.indexOf("ou=corporation4EC") != -1)) {
				userCertIssuerOrg = "BUSINESS";

			} else {
				if ((subjectDn.indexOf("OU=corporation") != -1) || (subjectDn.indexOf("ou=corporation") != -1)) {
					userCertIssuerOrg = "C_FINANCIAL";

				} else if ((subjectDn.indexOf("OU=personal4IB") != -1) || (subjectDn.indexOf("ou=personal4IB") != -1)) {
					userCertIssuerOrg = "P_FINANCIAL_RESTRICT";

				} else {
					userCertIssuerOrg = "P_FINANCIAL";
				}
			}

			// 해당 인증서가 SC제일은행에서 받은 금결원 인증서일경우 타행 등록을 할 수 없다.
			if (sCode.equals(CertConfig.RegCodeValue)) {
				throw new PRCServiceException("PRCCRT20518", "SC은행에서 발급받으신 공인인증서로 타기관 공인인증서 사용신청을 하실 수 없습니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			}

			/********* 시리얼을 십진수 포맷에 맞게 변경 *********/
			userSerialStr = certificateHelper.getSerialDecimal();
			sCode = sCode.substring(2, 2);

			String userId = (String) certRegSession.get("UserID");
			deptPersonName = (String) certRegSession.get("DeptPersonName");

			Map<String, Object> oppResult = certUtils.getOppResult(userId, userId, deptPersonName);

			String isPFinancial = StringUtils.nvl((String) oppResult.get("IS_P_FINANCIAL"), "");
			String isPFinancialRestrict = StringUtils.nvl((String) oppResult.get("IS_P_FINANCIAL_RESTRICT"), "");
			String isCFinancial = StringUtils.nvl((String) oppResult.get("IS_C_FINANCIAL"), "");
			String isBusiness = StringUtils.nvl((String) oppResult.get("IS_BUSINESS"), "");
			String pFStatus = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_STATUS"), "");
			String pFrStatus = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_RESTRICT_STATUS"), "");
			String cFStatus = StringUtils.nvl((String) oppResult.get("C_FINANCIAL_STATUS"), "");
			String bStatus = StringUtils.nvl((String) oppResult.get("BUSINESS_STATUS"), "");
			String pFSerial = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_SERIAL"), "");
			String pFrSerial = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_RESTRICT_SERIAL"), "");
			String cFSerial = StringUtils.nvl((String) oppResult.get("C_FINANCIAL_SERIAL"), "");
			String bSerial = StringUtils.nvl((String) oppResult.get("BUSINESS_SERIAL"), "");
			String pFIssuerOrg = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_ISSUERORG"), "");
			String pFrIssuerOrg = StringUtils.nvl((String) oppResult.get("P_FINANCIAL_RESTRICT_ISSUERORG"), "");
			String cFIssuerOrg = StringUtils.nvl((String) oppResult.get("C_FINANCIAL_ISSUERORG"), "");
			String bIssuerOrg = StringUtils.nvl((String) oppResult.get("BUSINESS_ISSUERORG"), "");

			log.debug("##cert is_p_Financial :: {} ", isPFinancial);
			log.debug("##cert is_p_Financial_Restrict :: {} ", isPFinancialRestrict);
			log.debug("##cert is_c_Financial :: {} ", isCFinancial);
			log.debug("##cert isBusiness :: {} ", isBusiness);
			log.debug("##cert p_fStatus :: {} ", pFStatus);
			log.debug("##cert p_frStatus :: {} ", pFrStatus);
			log.debug("##cert c_fStatus :: {} ", cFStatus);
			log.debug("##cert bStatus :: {} ", bStatus);
			log.debug("##cert p_fSerial :: {} ", pFSerial);
			log.debug("##cert p_frSerial :: {} ", pFrSerial);
			log.debug("##cert c_fSerial :: {} ", cFSerial);
			log.debug("##cert p_fIssuerOrg :: {} ", pFIssuerOrg);
			log.debug("##cert p_frIssuerOrg :: {} ", pFrIssuerOrg);
			log.debug("##cert c_fIssuerOrg :: {} ", cFIssuerOrg);
			log.debug("##cert bIssuerOrg :: {} ", bIssuerOrg);
			log.debug("##cert PersonOrCompanyCode :: {} ", personOrCompanyCode);

			if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("P_FINANCIAL_RESTRICT") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("VALID")) {
				// 이미 개인 금융용 유효한 인증서가 있다.
				if (pFrIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20520", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 이미 자행에서 발급받은 개인 금융용 유효한 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20521", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 이미 타행에서 발급받은 개인 금융용 유효한 공인인증서가 등록되어있으므로 타행 등록을 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("P_FINANCIAL_RESTRICT") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("SUSPEND")) {
				// 이미 개인 금융용 효력정지된 인증서가 있다.
				if (pFrIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20522", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 이미 자행에서 발급받은 개인 금융용 효력정지된 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20523", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 이미 타행에서 발급받은 개인 금융용 효력정지된 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("P_FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("VALID")) {
				// 이미 개인 범용 유효한 인증서가 있다.
				if (pFIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20560", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 이미 자행에서 발급받은 개인 범용 유효한 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20561", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 이미 타행에서 발급받은 개인 범용 유효한 공인인증서가 등록되어있으므로 타행 등록을 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("P_FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("SUSPEND")) {
				// 이미 개인 범용 효력정지된 인증서가 있다.
				if (pFIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20562", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 이미 자행에서 발급받은 개인 범용 효력정지된 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20563", "고객(개인)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 이미 타행에서 발급받은 개인 범용 효력정지된 공인인증서가 존재하므로 타행 등록을 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("C_FINANCIAL")) {
				// 개인 고객은 타행에서 발급받은 법인 금융용 공인인증서를 등록할 수 없음.
				throw new PRCServiceException("PRCCRT20524", "고객(개인)께서는 타행에서 발급받은 법인 금융용 공인인증서를 등록할 수 없습니다. 개인 금융용 공인인증서만 등록 할 수 있습니다.");
			} else if (personOrCompanyCode.equals("1") && userCertIssuerOrg.equals("BUSINESS")) {
				// 개인 고객은 타행에서 발급받은 법인 전자거래용 공인인증서를 등록할 수 없음.
				throw new PRCServiceException("PRCCRT20525", "고객(개인)께서는 타행에서 발급받은 법인 전자거래용 공인인증서를 등록할 수 없습니다.  개인 금융용 공인인증서만 등록 할 수 있습니다.");
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("P_FINANCIAL_RESTRICT") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("VALID")) {
				// 유효한 개인 금융용 공인인증서가 타행 등록되어 있다.
				if (pFrIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20526", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 자행에서 발급받은 유효한 개인 금융용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20527", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 타행에서 발급받은 유효한 개인 금융용 공인인증서가 등록되어있으므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("P_FINANCIAL_RESTRICT") && isPFinancialRestrict.equals("YES") && pFrStatus.equals("SUSPEND")) {
				// 이미 타행에서 발급 받은 효력정지된 개인 금융용 공인인증서가 타행 등록되어 있다.
				if (pFrIssuerOrg.equals("KFB")) { // 자행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20528", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 자행에서 발급받은 효력정지된 개인 금융용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20529", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 금융용 공인인증서이나, 타행에서 발급받은 효력정지된 개인 금융용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("P_FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("VALID")) {
				// 유효한 개인 범용 공인인증서가 타행 등록되어 있다.
				if (pFIssuerOrg.equals("KFB")) { // 자행에서 발급.
					throw new PRCServiceException("PRCCRT20566", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 자행에서 발급받은 유효한 개인 범용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급.
					throw new PRCServiceException("PRCCRT20567", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 타행에서 발급받은 유효한 개인 범용 공인인증서가 등록되어있으므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("P_FINANCIAL") && isPFinancial.equals("YES") && pFStatus.equals("SUSPEND")) {
				// 이미 타행에서 발급 받은 효력정지된 개인 범용 공인인증서가 타행 등록되어 있다.
				if (pFIssuerOrg.equals("KFB")) { // 자행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20568", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 자행에서 발급받은 효력정지된 개인 범용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20569", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 개인 범용 공인인증서이나, 타행에서 발급받은 효력정지된 개인 범용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("C_FINANCIAL") && isCFinancial.equals("YES") && cFStatus.equals("VALID")) {
				// 이미 타행에서 발급 받은 유효한 법인 금융용 공인인증서가 타행 등록되어 있다.
				if (cFIssuerOrg.equals("KFB")) { // 자행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20530", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 금융용 공인인증서이나, 자행에서 발급받은 유효한 법인 금융용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20531", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 금융용 공인인증서이나, 타행에서 발급받은 유효한 법인 금융용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("C_FINANCIAL") && isCFinancial.equals("YES") && cFStatus.equals("SUSPEND")) {
				// 이미 타행에서 발급 받은 효력정지된 법인 금융용 공인인증서가 타행 등록되어 있다.
				if (cFIssuerOrg.equals("KFB")) { // 자행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20532", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 금융용 공인인증서이나, 자행에서 발급받은 효력정지된 법인 금융용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 효력정지된 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20533", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 금융용 공인인증서이나, 타행에서 발급받은 효력정지된 법인 금융용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("BUSINESS") && isBusiness.equals("YES") && bStatus.equals("VALID")) {
				// 유효한 법인 전자거래용 공인인증서가 등록되어 있다.
				if (bIssuerOrg.equals("KFB")) { // 자행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20534", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 전자거래용 공인인증서이나, 자행에서 발급받은 유효한 법인 전자거래용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20535", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 전자거래용 공인인증서이나, 타행에서 발급받은 유효한 법인 전자거래용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			} else if (personOrCompanyCode.equals("2") && userCertIssuerOrg.equals("BUSINESS") && isBusiness.equals("YES") && bStatus.equals("SUSPEND")) {
				// 효력정지된 법인 전자거래용 공인인증서가 등록되어 있다.
				if (bIssuerOrg.equals("KFB")) { // 자행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20536", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 전자거래용 공인인증서이나, 자행에서 발급받은 효력정지된 법인 전자거래용 공인인증서가 존재하므로 타행등록 할 수 없습니다.");
				} else { // 타행에서 발급받은 유효한 공인인증서가 존재한다.
					throw new PRCServiceException("PRCCRT20537", "고객(개인사업자)께서는 등록하고자 하는 공인인증서가 법인 전자거래용 공인인증서이나, 타행에서 발급받은 효력정지된 법인 전자거래용 공인인증서가 등록되어있므로 타행등록 할 수 없습니다.");
				}
			}
		}

		sessionManager.setGlobalValue("certRegSession", certRegSession);
		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctValidateOtherCertRegCertificateResponse();
	}

	@ServiceEndpoint(url = "/authorizeOtherCertReg", name = "공동인증서 타기관 등록")
	public CrtJctAuthorizeOtherCertRegResponse authorizeOtherCertReg(IServiceContext serviceContext, CrtJctAuthorizeOtherCertRegRequest request) throws CertificateException, IOException {
		// ASIS :: MA3CRTCRT002_401S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 정보 세션
		Map<String, Object> certRegSession = certUtils.getCertSession("certRegSession");

		// 보안매체 검증 처리 START
		log.debug("authorizeOtherCertReg 보안매체 검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) certRegSession.get("UserID")); // 사용자아이디
		cbIbk01D00600Req.setTSPassword((String) certRegSession.get("TSPassword")); // 통신비밀번호
		cbIbk01D00600Req.setRegNo((String) certRegSession.get("RegNo")); // 실명번호
		cbIbk01D00600Req.setJuminSaupjaNo((String) certRegSession.get("RegNo")); // 주민사업자번호
		cbIbk01D00600Req.setCertTranCode("6"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setCAGubun((String) certRegSession.get("CAGubun")); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01D00600Req.setEmailAddr((String) certRegSession.get("EmailAddr")); // 이메일
		cbIbk01D00600Req.setJoinLoad("3"); // 가입경로
		cbIbk01D00600Req.setInforGubun((String) certRegSession.get("InforGubun")); // 정보코드
		cbIbk01D00600Req.setYIJMGB("1"); // 개인
		cbIbk01D00600Req.setIssueDate("0"); // 발급일
		cbIbk01D00600Req.setExpireDate("0"); // 만료일
		cbIbk01D00600Req.setYIGSGB((String) certRegSession.get("PersonOrCompanyCode"));// 인증서간소발급자

		String step3SafeCardKind = (String) certRegSession.get("SafeCardKind"); // 보안매체 종류
		cbIbk01D00600Req.setSafeCardKind(step3SafeCardKind); // 보안카드종류

		cbIbk01D00600Req.setSafeCardValue1(" "); // 안전카드일련번호 사용자입력값1
		cbIbk01D00600Req.setSafeCardValue2(" "); // 안전카드일련번호 사용자입력값2
		cbIbk01D00600Req.setSafeCardValue3(" "); // 안전카드일련번호 사용자입력값3
		cbIbk01D00600Req.setSafeCardIndex1(" "); // 안전카드일련번호 위치1
		cbIbk01D00600Req.setSafeCardIndex2(" "); // 안전카드일련번호 위치2
		cbIbk01D00600Req.setSafeCardIndex3(" "); // 안전카드일련번호 위치3
		cbIbk01D00600Req.setSecurityIndex((String) certRegSession.get("SecurityIndex"));// 안전카드 INDEX
		cbIbk01D00600Req.setSecurityIndex2((String) certRegSession.get("SecurityIndex2"));// 안전카드 INDEX2

		cbIbk01D00600Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드 첫번째 입력값
		cbIbk01D00600Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드 두번째 입력값

		log.debug("authorizeOtherCertReg step3_SafeCardKind :: {}", step3SafeCardKind);

		if ("1".equals(step3SafeCardKind)) {
			cbIbk01D00600Req.setSafeCardValue1(VerificationUtils.getSafeCardSeq1()); // 안전카드일련번호 사용자입력값1
			cbIbk01D00600Req.setSafeCardValue2(VerificationUtils.getSafeCardSeq2()); // 안전카드일련번호 사용자입력값2
			cbIbk01D00600Req.setSafeCardValue3(VerificationUtils.getSafeCardSeq3()); // 안전카드일련번호 사용자입력값3
			cbIbk01D00600Req.setSafeCardIndex1((String) certRegSession.get("SafeCardIndex1")); // 안전카드일련번호 위치1
			cbIbk01D00600Req.setSafeCardIndex2((String) certRegSession.get("SafeCardIndex2")); // 안전카드일련번호 위치2
			cbIbk01D00600Req.setSafeCardIndex3((String) certRegSession.get("SafeCardIndex3")); // 안전카드일련번호 위치3
			cbIbk01D00600Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드값2
		}

		OltpResponse<CbIbk01D00600Res> hostResponse = null;
		if ("3".equals(step3SafeCardKind)) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01D00600Req, CbIbk01D00600Res.class);
		}
		log.debug("authorizeOtherCertReg 보안매체 검증 END");
		// 보안매체 검증 처리 END

		// RA전문 처리 START
//		X509Certificate userCert = (X509Certificate) certRegSession.get("userCert");
		X509Certificate userCert = certUtils.getCertificationFromB64((String) certRegSession.get("userCert"));

		CertificateHelper certificateHelper = new CertificateHelper(userCert);
		log.debug("X509Certificate={}", userCert);

		String raGubun = "";
		String custGubun = "";

		/*
		 * 2010.09.05 요청자 강계환 YIRAGB 타기관인증등록에서는 9로 올렸음 금결원일때는 금융:1 범용:2 YIBANK 무조건 올려야되고,
		 * YICUSTGB 발급자 YICAGB 발급기관
		 */
		String issuerDn = certificateHelper.getIssuerDN();
		log.debug("issuerDn :: {}", issuerDn);

		// 타행 금결원 처리
		if (issuerDn.indexOf("yessignCA") > -1) {
			// 타행 금결원 인증서 등록 START
			log.debug("타행 금결원 인증서 등록 START");
			log.debug("======OtherCert==  타기관 금융결제원 인증서 제출");

			// 고객 인증서 조회
			String certPolicy = certificateHelper.getPolicyCode();
			String serialValue = certificateHelper.getSerial();
			String userId = (String) certRegSession.get("UserID");

			// 타기관 인증서 등록
			Map<String, String> issueMap = kftcCertComponent.regOtherCert(serialValue, certPolicy, userId);

			String resCode = issueMap.get("ResCode");
			String status = issueMap.get("CodeData");

			// 인증서 폐지 상태
			if (status.equals("30")) {
				throw new PRCServiceException("PRCCRT20547", "고객께서 제출하신 공인인증서는 폐기된 인증서 이므로 타행등록을 하실 수 없습니다.");
			} else if (status.equals("40")) { // 인증서 효력정지 상태
				// 효력정지된 인증서이므로 인증서를 발급받았던 기관에서 효력-회복 해야함.
				throw new PRCServiceException("PRCCRT20514", "고객께서 요청하신 공인인증서는 효력정지된 인증서이므로 먼저 인증서를 발급받았던 기관(은행)에서 효력회복을 해야합니다. 자세한 내용은 공인인증 등록기관 관리자에게 문의하시기 바랍니다.");
			} else if (status.equals("10")) { // 인증서 유효상태
				// DB처리
				ldapInfoDao.updateUseridBySerial(serialValue, userId);
			}
			log.debug("타행 금결원 인증서 등록 END");
			// 타행 금결원 인증서 등록 END

			log.debug("==============OtherCert RAGubun start==");

			String certOid = certificateHelper.getCertificatePolicyOID();
			if (certificateHelper.isYessign()) {
				String kindOfCA = "YESSIGNCA";

				/**********************************/
				/* OID 읽어서 정책코드로 변환. */
				/* 1.2.410.200005.1.1.1 ==> 01 */
				/* 1.2.410.200005.1.1.2 ==> 02 */
				/* 1.2.410.200005.1.1.4 ==> 04 */
				/* 1.2.410.200005.1.1.5 ==> 05 */
				// *********************************/
				// 개인범용 인증서
				if (certOid.equals("1.2.410.200005.1.1.1")) {
					custGubun = "1";
					raGubun = "2";
				}
				// 개인금융 인증서
				if (certOid.equals("1.2.410.200005.1.1.4")) {
					custGubun = "1";
					raGubun = "1";
				}
				// 기업금용 인증서
				if (certOid.equals("1.2.410.200005.1.1.2")) {
					custGubun = "2";
					raGubun = "1";
				}
				if (certOid.equals("1.2.410.200005.1.1.5")) {
					custGubun = "2";
					raGubun = "2";
				}
				if (certOid.equals("1.2.410.200005.1.1.6.8")) {
					custGubun = "2";
					raGubun = "3";
				}
				// 조합번호용 인증서 - 2023.05.27 ssh 추가
				if (certOid.equals("1.2.410.200005.1.1.4.8")) {
					custGubun = "1";
					raGubun = "1";
				}
			} else { // 고객이 제출한 인증서는 금결원을 제외한 다른 공인인증 기관으로 부터 받은 인증서이다.
				raGubun = "9";
			}
			log.debug("==============OtherCert raGubun end :: {}", raGubun);
		} else {
			// 타기관 처리
			// 타기관인증(금결원 외) 이므로 무조건 9값세팅 추가 부분 처리 2010.09.13
			raGubun = "9";

			log.debug("타기관 인증서 등록 처리 START");
			String userId = (String) certRegSession.get("UserID");
			String idn = (String) certRegSession.get("RegNo");

			// 타기관(금결원 외) 인증서 등록
			otherCertComponent.issueCert(userCert, userId, idn);
			log.debug("타기관 인증서 등록 처리 END");
		}
		// RA전문 처리 END

		// 타기관 등록 본거래 전문 처리 START
		log.debug("authorizeOtherCertReg 타기관 등록 본거래 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("D006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01H00600Req2 = new CbIbk01D00600Req();
		cbIbk01H00600Req2.setUserID((String) certRegSession.get("UserID")); // 사용자아이디
		cbIbk01H00600Req2.setTSPassword((String) certRegSession.get("TSPassword")); // 통신비밀번호
		cbIbk01H00600Req2.setRegNo((String) certRegSession.get("RegNo")); // 실명번호
		cbIbk01H00600Req2.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req2.setCAGubun((String) certRegSession.get("CAGubun")); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H00600Req2.setRAGubun(raGubun);
		cbIbk01H00600Req2.setCertTranCode("6"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H00600Req2.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01H00600Req2.setYIGSGB((String) certRegSession.get("PersonOrCompanyCode"));
		cbIbk01H00600Req2.setInforGubun((String) certRegSession.get("InforGubun"));
		cbIbk01H00600Req2.setJuminSaupjaNo((String) certRegSession.get("RegNo")); // 주민사업자번호
		cbIbk01H00600Req2.setIssueDate((String) certRegSession.get("IssueDate")); // 발급일
		cbIbk01H00600Req2.setExpireDate((String) certRegSession.get("ExpireDate")); // 만료일
		cbIbk01H00600Req2.setIssueBank((String) certRegSession.get("IssueBank")); // 인증서발급은행(은행코드)
		cbIbk01H00600Req2.setEmailAddr((String) certRegSession.get("EmailAddr"));

		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H00600Req2, CbIbk01D00600Res.class);
		log.debug("authorizeOtherCertReg 타기관 등록 본거래 END");
		// 타기관 등록 본거래 전문 처리 END

		// SMS 발송 START
		String smsCustName = (String) certRegSession.get("DeptPersonName");
		smsCustName = BizCommonUtils.getMaskCustData(smsCustName, "01");

		String smsMsg = smsCustName + "님 인증서 타기관 등록완료. 본인요청 아닐 시 신고요망";
		String userId = (String) certRegSession.get("UserID");
		String tsPassword = (String) certRegSession.get("TSPassword");
		String regNo = (String) certRegSession.get("RegNo");

		smsComponent.sendCompleteSMS(userId, tsPassword, regNo, smsMsg);
		// SMS 발송 END

		// 세션 삭제
		sessionManager.removeGlobalValue("certRegSession");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctAuthorizeOtherCertRegResponse();
	}

	@ServiceEndpoint(url = "/validateOtherCertDel", name = "공동인증서 타기관 해제 본인확인")
	public CrtJctValidateOtherCertDelResponse validateOtherCertDel(IServiceContext serviceContext, CrtJctValidateOtherCertDelRequest request) {
		// ASIS :: MA3CRTCRT003_101S + MA3CRTCRT003_201S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 등록 세션 초기화
		sessionManager.removeGlobalValue("certReleaseSession");
		sessionManager.removeGlobalValue("PerBusNo");
		sessionManager.removeGlobalValue("CustName");

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 아이디 찾기 전문 처리 START
		log.debug("authorizeOtherCertReg 아이디 찾기 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H892");
		hostRequestOptions.setSvcCd("892");

		// 개별부 세팅
		CbIbk01H89201Req cbIbk01H89201Req = new CbIbk01H89201Req();
		cbIbk01H89201Req.setUserID(CommonBizConstants.DEFAULT_USER_ID); // 아이디
		cbIbk01H89201Req.setCustJumin1(request.getCustJumin1()); // 실명번호1
		cbIbk01H89201Req.setCustJumin2(request.getCustJumin2()); // 계좌비밀번호
		cbIbk01H89201Req.setActType("1"); // 처리구분. 1:고객정보조회(예비처리), 2:계좌검증(본처리), 3:계좌미검증(본처리)
		cbIbk01H89201Req.setAcctNum("");

		// 전문 전송
		OltpResponse<CbIbk01H89201Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H89201Req, CbIbk01H89201Res.class);
		CbIbk01H89201Res cbIbk01H89201Res = hostResponse.getResponse();
		log.debug("authorizeOtherCertReg 아이디 찾기 END");
		// 아이디 찾기 전문 처리 END

		if ("4".equals(cbIbk01H89201Res.getUserType()) || "5".equals(cbIbk01H89201Res.getUserType())) {
			throw new PRCServiceException("PRCCRT0009", "인터넷뱅킹 미가입 고객입니다.");
		}

		// 본인확인 전문 처리 START
		log.debug("authorizeOtherCertReg 본인확인 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("H006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
		cbIbk01H00601Req.setUserID(cbIbk01H89201Res.getUserID2());
		cbIbk01H00601Req.setCustJumin1(request.getCustJumin1());
		cbIbk01H00601Req.setCustJumin2(request.getCustJumin2());
		cbIbk01H00601Req.setSimplifyGB("2"); // 인증서간소화 체크
		cbIbk01H00601Req.setCertTranCode("6"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록
		cbIbk01H00601Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00601Req.setCAGubun("3"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증
		cbIbk01H00601Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01H00601Req.setYIGSGB("1"); // 인증서간소발급자

		// 전문 전송
		OltpResponse<CbIbk01H00601Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H00601Req, CbIbk01H00601Res.class);
		CbIbk01H00601Res cbIbk01H00601Res = hostResponse2.getResponse();
		log.debug("authorizeOtherCertReg 본인확인 END");
		// 본인확인 전문 처리 END

		String personOrCompanyCode = cbIbk01H00601Res.getPersonOrCompanyCode();
		String userId = cbIbk01H00601Res.getUserID();

		// 조합번호용 인증서 - 2023.05.27 ssh
		// H006거래를 통해 받아온 실명증표구분으로, 3-조합번호이용고객일 경우 input추가
		// (CertUtil에서 해당 구분값으로 인증서 PolicyCode를 분기하기 위함)
		String yoCFJMGB = cbIbk01H00601Res.getYOCFJMGB();

		String oppUserId = "";
		String deptPersonName = "";

		// 올바른 사용자
		if (personOrCompanyCode.equals("1") || personOrCompanyCode.equals("2") || personOrCompanyCode.equals("3")) {
			oppUserId = CertConfig.RegCodeValue + userId;
			deptPersonName = certUtils.byteStringConvert(cbIbk01H00601Res.getDeptPersonName(), 9);
			deptPersonName = deptPersonName + "()";
		} else {
			throw new PRCServiceException("PRCCRT30114", "잘못된 사용자");
		}

		// RA사용자 조회
		Map<String, Object> oppraResult = certUtils.getOppResult(userId, oppUserId, deptPersonName);

		if ("3".equals(cbIbk01H00601Res.getPersonOrCompanyCode())) {
			throw new PRCServiceException("PRCCRT0001", "사업자 고객은 스마트폰에서 인증서 발급을 하실 수 없습니다.");
		}

		// 개인사업자는 오류
		if ("13".equals(cbIbk01H00601Res.getYOIDGB()) || "14".equals(cbIbk01H00601Res.getYOIDGB())) {
			throw new PRCServiceException("PRCCRT0010", "사업자 고객은 스마트폰에서 타기관 인증서 등록을 하실수 없습니다.");
		}

		// 본인 인증한 데이터 세션에 담는다
		Map<String, Object> certReleaseSession = new HashMap<>();
		certReleaseSession.put("UserID", cbIbk01H00601Res.getUserID());
		certReleaseSession.put("TSPassword", cbIbk01H00601Res.getTSPassword());
		certReleaseSession.put("RegNo", cbIbk01H00601Res.getRegNo());
		certReleaseSession.put("PersonOrCompanyCode", cbIbk01H00601Res.getPersonOrCompanyCode());
		certReleaseSession.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		certReleaseSession.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		certReleaseSession.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
		certReleaseSession.put("DeptPersonName", cbIbk01H00601Res.getDeptPersonName());
		certReleaseSession.put("SaupjaNo", cbIbk01H00601Res.getSaupjaNo());
		certReleaseSession.put("ZipCode", cbIbk01H00601Res.getZipCode());
		certReleaseSession.put("Address", cbIbk01H00601Res.getAddress());
		certReleaseSession.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
		certReleaseSession.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
		certReleaseSession.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
		certReleaseSession.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
		certReleaseSession.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
		certReleaseSession.put("AuthSVC4CERT", cbIbk01H00601Res.getAuthSVC4CERT());
		certReleaseSession.put("InforGubun", cbIbk01H00601Res.getInforGubun());
		certReleaseSession.put("CustGubun", cbIbk01H00601Res.getCustGubun());
		certReleaseSession.put("TelCode", cbIbk01H00601Res.getTelCode());
		certReleaseSession.put("oppraResult", oppraResult); // RA정보

		sessionManager.setGlobalValue("certReleaseSession", certReleaseSession);
		sessionManager.setGlobalValue("PerBusNo", cbIbk01H00601Res.getRegNo());
		sessionManager.setGlobalValue("CustName", cbIbk01H00601Res.getDeptPersonName());

		// 추가인증 세션값 세팅
		String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00601Res.getTelCode());
		sessionManager.setGlobalValue("TeleOne", telCode[0]);
		sessionManager.setGlobalValue("TeleTwo", telCode[1]);
		sessionManager.setGlobalValue("TeleThree", telCode[2]);
		sessionManager.setGlobalValue("UserID", (String) certReleaseSession.get("UserID"));
		sessionManager.setGlobalValue("CustName", (String) certReleaseSession.get("DeptPersonName"));
		sessionManager.setGlobalValue("TransPWUseYN", "1");
		sessionManager.setGlobalValue("SafeCardState", "1");
		sessionManager.setGlobalValue("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
		sessionManager.setGlobalValue("SafeCardSeq1", cbIbk01H00601Res.getSafeCardIndex1());
		sessionManager.setGlobalValue("SafeCardSeq2", cbIbk01H00601Res.getSafeCardIndex2());
		sessionManager.setGlobalValue("SafeCardSeq3", cbIbk01H00601Res.getSafeCardIndex3());
		sessionManager.setGlobalValue("SafeCardINDEX", cbIbk01H00601Res.getSecurityIndex());
		sessionManager.setGlobalValue("SafeCardINDEX2", cbIbk01H00601Res.getSecurityIndex2());
		sessionManager.setGlobalValue("SmartOTP", cbIbk01H00601Res.getSmartOTP());
		sessionManager.setGlobalValue("TSPassword", cbIbk01H00601Res.getTSPassword());
		sessionManager.setGlobalValue("SafeCardBranchNum", "0");

		// 해지 가능한 인증서 목록 추출 START
		log.debug("해지 가능한 인증서 목록 추출 START");
		String custName = cbIbk01H00601Res.getDeptPersonName();

		HashMap<String, String> searchByUser = new HashMap<>();
		if ("3".equals(yoCFJMGB)) {
			searchByUser.put("YOCFJMGB", yoCFJMGB);
		}
		List<OppraCertInfo> ldapList = certUtils.searchByUserID(cbIbk01H00601Res.getUserID(), cbIbk01H00601Res.getRegNo(), searchByUser);

		log.debug("info 인증서 목록(타기관) START");
		List<InfoOtherResult> dbCertList = ldapInfoDao.selectInfoOther(userId);

		for (InfoOtherResult certMap : dbCertList) {
			OppraCertInfo retCertMap = new OppraCertInfo();
			String oid = certMap.getOid();
			String custGubun = CertificateHelper.getCrtCustGubun(oid);

			retCertMap.setTranType("3"); // TranType(1:HOST, 2:LDAP, 3:INFO) - 임의 적용REGNO
			retCertMap.setCertserial(certMap.getSerial());
			retCertMap.setIssueBank(certMap.getIssuer());
			retCertMap.setJuminSaupjaNo(certMap.getIdnum());
			retCertMap.setOid(oid);
			retCertMap.setCustGubun(custGubun);
			retCertMap.setRaGubun("9"); // 타기관 : 9
			String endDate = String.valueOf(certMap.getExpired());
			retCertMap.setIssueEndDate(endDate.substring(0, 11).replace("-", ""));
			String issuerCode = certMap.getIssuer();
			if (issuerCode.equals("92") || issuerCode.equals("4") || issuerCode.equals("5") || issuerCode.equals("6")) {
				retCertMap.setCaGubun("3"); // 타기관 : SignGate 정보인증
			}
			if (issuerCode.equals("95") || issuerCode.equals("7")) {
				retCertMap.setCaGubun("4"); // 타기관 : SignKorea
			}
			if (issuerCode.equals("94") || issuerCode.equals("3")) {
				retCertMap.setCaGubun("5"); // 타기관 : NCA
			}
			if (issuerCode.equals("93") || issuerCode.equals("2")) {
				retCertMap.setCaGubun("6"); // 타기관 : CrossCert
			}
			if (issuerCode.equals("96") || issuerCode.equals("8")) {
				retCertMap.setCaGubun("7"); // 타기관 : TradeSign
			}
			retCertMap.setDeptPersonName(custName);
			retCertMap.setCertPolicyCode("");
			retCertMap.setIssueBankName(certUtils.IssueBankName(retCertMap.getCaGubun(), certMap.getIssuer()));
			retCertMap.setRaName(certUtils.getRAName("9", custGubun));

			ldapList.add(retCertMap);
		}
		log.debug("ldapList :: {}", ldapList);
		log.debug("info 인증서 목록(타기관) END");

		log.debug("해지 가능한 인증서 목록 추출 END");
		// 해지 가능한 인증서 목록 추출 END

		// 응답 객체
		CrtJctValidateOtherCertDelResponse response = new CrtJctValidateOtherCertDelResponse();
		response.setCertList(ldapList); // 해지 가능한 인증서 목록
		response.setCustName(custName);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/authorizeOtherCertDel", name = "공동인증서 타기관 해제")
	public CrtJctAuthorizeOtherCertDelResponse authorizeOtherCertDel(IServiceContext serviceContext, CrtJctAuthorizeOtherCertDelRequest request) {
		// ASIS :: MA3CRTCRT003_301S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 정보 세션
		Map<String, Object> certReleaseSession = certUtils.getCertSession("certReleaseSession");

		// 보안매체 검증 처리 START
		log.debug("authorizeOtherCertDel 보안매체 검증 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D980");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("D980");
		hostRequestOptions.setSvcCd("980");

		// 개별부 세팅
		CbIbk01H98000Req cbIbk01H98000Req = new CbIbk01H98000Req();
		cbIbk01H98000Req.setUserID((String) certReleaseSession.get("UserID")); // 사용자아이디
		cbIbk01H98000Req.setTSPassword((String) certReleaseSession.get("TSPassword")); // 통신비밀번호
		cbIbk01H98000Req.setRegNo((String) certReleaseSession.get("RegNo")); // 실명번호
		cbIbk01H98000Req.setJuminSaupjaNo((String) certReleaseSession.get("RegNo")); // 주민사업자번호
		cbIbk01H98000Req.setCertTranCode("D"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H98000Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H98000Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H98000Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업)
		cbIbk01H98000Req.setInforGubun("2"); // 정보구분
		cbIbk01H98000Req.setIssueDate("0"); // 발급일
		cbIbk01H98000Req.setExpireDate("0"); // 만료일
		cbIbk01H98000Req.setYIGSGB((String) certReleaseSession.get("PersonOrCompanyCode"));// 인증서간소발급자

		String step3SafeCardKind = (String) certReleaseSession.get("SafeCardKind"); // 보안매체 종류
		cbIbk01H98000Req.setSafeCardKind(step3SafeCardKind); // 보안카드종류

		cbIbk01H98000Req.setSecurityIndex((String) certReleaseSession.get("SecurityIndex"));// 안전카드 INDEX
		cbIbk01H98000Req.setSecurityIndex2((String) certReleaseSession.get("SecurityIndex2"));// 안전카드 INDEX2
		cbIbk01H98000Req.setSecurityValue(VerificationUtils.getSafeCardNumber1()); // 안전카드 첫번째 입력값
		cbIbk01H98000Req.setSecurityValue2(VerificationUtils.getSafeCardNumber2()); // 안전카드 두번째 입력값

		log.debug("authorizeOtherCertDel step3_SafeCardKind :: {}", step3SafeCardKind);

		OltpResponse<CbIbk01H98000Res> hostResponse = null;
		if ("3".equals(step3SafeCardKind)) { // 통합 OTP
			hostResponse = this.hostClient.sendOltpWithSecure(hostRequestOptions, cbIbk01H98000Req, CbIbk01H98000Res.class);
		} else {
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H98000Req, CbIbk01H98000Res.class);
		}
		log.debug("authorizeOtherCertDel 보안매체 검증 END");
		// 보안매체 검증 처리 END

		// RA해제 처리 START
		String tranType = request.getTranType();
		String certSerial = request.getCertSerial();
		String certPolicyCode = request.getCertPolicyCode();
		String oid = request.getOid();
		String caGubun = request.getCaGubun();
		String userId = (String) certReleaseSession.get("UserID");

		// 인증서 시리얼번호 검증 START
		String regNo = (String) certReleaseSession.get("RegNo");
		HashMap<String, String> searchByUser = new HashMap<>();
		searchByUser.put("CertInquiryType", "C");
		List<OppraCertInfo> oppraCertList = certUtils.searchByUserID(userId, regNo, searchByUser);

		// 금결원 타행 해제 처리
		for (OppraCertInfo certInfo : oppraCertList) {
			if (certSerial.equals(certInfo.getCertserial()) && certPolicyCode.equals(certInfo.getCertPolicyCode()) && "2".equals(tranType)) {
				kftcCertComponent.revokeOtherCert(certSerial, certPolicyCode);
				break;
			}
		}
		// 인증서 시리얼번호 검증 END

		// 타기관 해제 처리
		if ("3".equals(tranType)) {
			ldapInfoDao.deleteInfoOther(userId, regNo, oid);
		}
		// RA전문 처리 END

		// 세션 삭제
		sessionManager.removeGlobalValue("certReleaseSession");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return new CrtJctAuthorizeOtherCertDelResponse();
	}

	@ServiceEndpoint(url = "/validateUniCertRefund", name = "수수료 납부 취소 본인확인")
	public CrtJctValidateUniCertRefundResponse validateUniCertRefund(IServiceContext serviceContext, CrtJctValidateUniCertRefundRequest request) {
		// ASIS :: H00605 [CertTask.checkCertID() ]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 로그인된 경우 로그아웃 처리
		if (sessionManager.isLogin()) {
			sessionManager.logout();
		}

		// 인증서 발급 세션 초기화
		sessionManager.removeGlobalValue("uniCertRefundSession");

		// 본인확인 전문 처리 START
		log.debug("validateUniCertRefund 본인확인 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H006");
		hostRequestOptions.setSvcCd("006");

		// 개별부 세팅
		CbIbk01H00600Req cbIbk01H00600Req = new CbIbk01H00600Req();
		cbIbk01H00600Req.setUserID(request.getUserId()); // 사용자아이디
		cbIbk01H00600Req.setRegNo(request.getCustJumin1()+request.getCustJumin2());
		cbIbk01H00600Req.setTSPassword(" "); // 통신비밀번호
		cbIbk01H00600Req.setEmailAddr(" "); // 이메일
		cbIbk01H00600Req.setCertTranCode("3"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01H00600Req.setChuryGubun("N"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01H00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01H00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))

		// 전문 전송
		OltpResponse<CbIbk01H00600Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H00600Req, CbIbk01H00600Res.class);
		CbIbk01H00600Res cbIbk01H00600Res = hostResponse.getResponse();
		log.debug("validateUniCertRefund cbIbk01H006Res :: {}", cbIbk01H00600Res);
		log.debug("validateUniCertRefund 본인확인 END");
		// 본인확인 전문 처리 END

		// 본인 인증한 데이터 세션에 저장
		Map<String, Object> uniCertRefundSession = new HashMap<>();
		uniCertRefundSession.put("UserID", cbIbk01H00600Res.getUserID());
		uniCertRefundSession.put("TSPassword", cbIbk01H00600Res.getTSPassword());
		uniCertRefundSession.put("RegNo", cbIbk01H00600Res.getRegNo());
		uniCertRefundSession.put("PersonOrCompanyCode", cbIbk01H00600Res.getPersonOrCompanyCode());
		uniCertRefundSession.put("EmailAddr", cbIbk01H00600Res.getEmailAddr());
		uniCertRefundSession.put("DeptPersonName", cbIbk01H00600Res.getDeptPersonName());
		uniCertRefundSession.put("SaupjaNo", cbIbk01H00600Res.getSaupjaNo());
		uniCertRefundSession.put("ZipCode", cbIbk01H00600Res.getZipCode());
		uniCertRefundSession.put("Address", cbIbk01H00600Res.getAddress());
		uniCertRefundSession.put("AuthSVC4CERT", cbIbk01H00600Res.getAuthSVC4CERT());
		uniCertRefundSession.put("RealNameIDType", cbIbk01H00600Res.getYOCFJMGB()); // 실명증표구분 - 1:주민등록번호 2:사업자번호 3:여권번호 4:외국인등록번호 5:임의단체대표자주민번호 6:납세고유번호

		sessionManager.setGlobalValue("uniCertRefundSession", uniCertRefundSession);

		// 응답 객체
		CrtJctValidateUniCertRefundResponse response = new CrtJctValidateUniCertRefundResponse();
		response.setCustName(cbIbk01H00600Res.getDeptPersonName());

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getUniCertRefund", name = "수수료 납부 취소 대상 조회")
	public CrtJctGetUniCertRefundResponse getUniCertRefund(IServiceContext serviceContext, CrtJctGetUniCertRefundRequest request) {
		// ASIS :: H00605 [NotsessionServiceController.insFeeCancelData(), Callback_H0060502(), ]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 정보 세션
		Map<String, Object> uniCertRefundSession = certUtils.getCertSession("uniCertRefundSession");

		// 수수료 납부 취소 예비거래 전문 처리 START
		log.debug("getUniCertRefund 수수료 납부 취소 예비거래 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_TBS03_H982");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1TBS03");
		hostRequestOptions.setInClassCd("H982");
		hostRequestOptions.setSvcCd("982");

		// 개별부 세팅
		CbTbs03H98200Req cbTbs03H98200Req = new CbTbs03H98200Req();
		cbTbs03H98200Req.setGjno(request.getGjno()); // 수수료입금계좌
		cbTbs03H98200Req.setAcctPasswd(request.getAcctBb()); // 계좌비밀번호
		cbTbs03H98200Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbTbs03H98200Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbTbs03H98200Req.setUserGubun("1");
		cbTbs03H98200Req.setUserID((String) uniCertRefundSession.get("UserID"));
		cbTbs03H98200Req.setCancelOP((String) uniCertRefundSession.get("UserID")); // 취소완료조작자
		cbTbs03H98200Req.setJuminSaupjaNo((String) uniCertRefundSession.get("RegNo"));
		cbTbs03H98200Req.setJuminNo((String) uniCertRefundSession.get("RegNo"));

		// 현재일자
		String strFDate = DateUtils.format(DateUtils.addDays(LocalDate.now(), -7), "yyyyMMdd"); // 현재일자에서 7일 전 일자
		String strTDate = DateUtils.getCurrentDate("yyyyMMdd"); // 현재일자
		cbTbs03H98200Req.setFDate(strFDate);
		cbTbs03H98200Req.setTDate(strTDate);

		// 전문 전송
		OltpResponse<CbTbs03H98200Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbTbs03H98200Req, CbTbs03H98200Res.class);
		CbTbs03H98200Res cbTbs03H98200Res = hostResponse.getResponse();
		log.debug("getUniCertRefund 수수료 납부 취소 예비거래 END");
		// 수수료 납부 취소 예비거래 전문 처리 END

		// 응답 객체
		CrtJctGetUniCertRefundResponse response = new CrtJctGetUniCertRefundResponse();
		response.setDeptPersonName((String) uniCertRefundSession.get("DeptPersonName"));
		response.setCmsCode(cbTbs03H98200Res.getCMSCode());
		response.setFDate(strFDate);
		response.setTDate(strTDate);

		// 수수료 납부 취소 대상 목록
		List<ChargeInfo> chargeList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(cbTbs03H98200Res.getRCount_REC())) {
			chargeList = cbTbs03H98200Res.getRCount_REC().stream()
					.map(res -> {
						ChargeInfo info = new ChargeInfo();
						info.setChargeDate(res.getChargeDate());
						info.setChargeMSeq(res.getChargeMSeq());
						info.setChargeFee(res.getChargeFee());
						info.setChargeVat(res.getChargeVat());
						info.setChargeCaGubun(res.getChargeCAGubun());
						info.setChargeCustGubun(res.getChargeCustGubun());
						info.setChargeRaGubun(res.getChargeRAGubun());
						info.setJuminSaupjaNo(res.getJuminSaupjaNo());
						info.setChargeGjno(res.getChargeGjno());
						info.setChargeCYubu(res.getChargeCYubu());
						return info;
					})
					.toList();
		}
		response.setChargeList(chargeList);
		log.debug("chargeList :: {}", chargeList);

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/authorizeUniCertRefund", name = "수수료 납부 취소")
	public CrtJctAuthorizeUniCertRefundResponse authorizeUniCertRefund(IServiceContext serviceContext, CrtJctAuthorizeUniCertRefundRequest request) {
		// ASIS :: H00605 [func_H0060503(), NotsessionServiceController.insFeeCancelResultData(), Callback_H0060503()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 인증서 정보 세션
		Map<String, Object> uniCertRefundSession = certUtils.getCertSession("uniCertRefundSession");

		// 수수료 납부 취소 본거래 전문 처리 START
		log.debug("authorizeUniCertRefund 수수료 납부 취소 본거래 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_TBS03_D982");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1TBS03");
		hostRequestOptions.setInClassCd("D982");
		hostRequestOptions.setSvcCd("982");

		// 개별부 세팅
		CbTbs03H98200Req cbTbs03H98200Req = new CbTbs03H98200Req();
		cbTbs03H98200Req.setGjno(request.getGjno()); // 수수료입금계좌
		cbTbs03H98200Req.setCMSCode(request.getCmsCode());
		cbTbs03H98200Req.setMDate(request.getMDate()); // 납부일
		cbTbs03H98200Req.setMSeq(request.getMSeq()); // 납부번호
		cbTbs03H98200Req.setSSR(request.getSsr()); // 수수료 금액
		cbTbs03H98200Req.setVAT(request.getVat()); // 부가세 금액
		cbTbs03H98200Req.setJuminSaupjaNo((String) uniCertRefundSession.get("RegNo")); // 주민 사업자 번호
		cbTbs03H98200Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbTbs03H98200Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbTbs03H98200Req.setRAGubun(request.getRaGubun()); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbTbs03H98200Req.setUserID((String) uniCertRefundSession.get("UserID"));
		cbTbs03H98200Req.setCancelOP((String) uniCertRefundSession.get("UserID")); // 취소완료조작자

		// 전문 전송
		OltpResponse<CbTbs03H98200Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbTbs03H98200Req, CbTbs03H98200Res.class);
		CbTbs03H98200Res cbTbs03H98200Res = hostResponse.getResponse();
		log.debug("authorizeUniCertRefund 수수료 납부 취소 본거래 END");
		// 수수료 납부 취소 본거래 전문 처리 END

		// 수수료 납부 취소 완료 전문 처리 START
		log.debug("authorizeUniCertRefund 수수료 납부 취소 완료 START");
		OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_D006");

		// 공통부 세팅
		hostRequestOptions2.setImsTranCd("TI1IBK01");
		hostRequestOptions2.setInClassCd("D006");
		hostRequestOptions2.setSvcCd("006");

		// 개별부 세팅
		CbIbk01D00600Req cbIbk01D00600Req = new CbIbk01D00600Req();
		cbIbk01D00600Req.setUserID((String) uniCertRefundSession.get("UserID"));
		cbIbk01D00600Req.setCAGubun("2"); // 발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)
		cbIbk01D00600Req.setCustGubun("1"); // 발급자구분(1:개인,2:기업))
		cbIbk01D00600Req.setRAGubun(request.getRaGubun()); // 인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))
		cbIbk01D00600Req.setCertTranCode("2"); // 거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)
		cbIbk01D00600Req.setChuryGubun("U"); // 처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)
		cbIbk01D00600Req.setInforGubun("1");
		cbIbk01D00600Req.setJuminSaupjaNo((String) uniCertRefundSession.get("RegNo")); // 주민 사업자 번호
		cbIbk01D00600Req.setEmailAddr((String) uniCertRefundSession.get("EmailAddr"));

		// 전문 전송
		OltpResponse<CbIbk01D00600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01D00600Req, CbIbk01D00600Res.class);
		CbIbk01D00600Res cbIbk01D00600Res = hostResponse2.getResponse();
		// 수수료 납부 취소 완료 전문 처리 END

		// RA 전문 START
		String certPolicyCode = "";
		String caGubun = "2";
		String custGubun = "1";
		String raGubun = request.getRaGubun();

		if (caGubun.equals("2") && custGubun.equals("1") && raGubun.equals("1")) {
			certPolicyCode = "04";
		} else if (caGubun.equals("2") && custGubun.equals("1") && raGubun.equals("2")) {
			certPolicyCode = "01";
		}
		// 해당 조건을 탈수가 없음
//		else if (caGubun.equals("2") && custGubun.equals("2") && raGubun.equals("1")) {
//			certPolicyCode = "02";
//		} else if (caGubun.equals("2") && custGubun.equals("2") && raGubun.equals("2")) {
//			certPolicyCode = "05";
//		} else if (caGubun.equals("2") && custGubun.equals("2") && raGubun.equals("3")) {
//			certPolicyCode = "68";
//		}

		String userId = cbIbk01D00600Res.getUserID();
		String personOrCompanyCode = cbIbk01D00600Res.getPersonOrCompanyCode();
		String juminSaupjaNo = (String) uniCertRefundSession.get("RegNo");

		String certSerial = "";
		if ("2".equals(personOrCompanyCode)) {
			certSerial = certUtils.getSerialNumByRegnum(certPolicyCode, juminSaupjaNo);
			log.debug("authorizeUniCertRefund getSerialNumByRegnum certSerial :: {}", certSerial);
		} else {
			certSerial = certUtils.getSerialNumByUserID(certPolicyCode, userId);
			log.debug("authorizeUniCertRefund getSerialNumByUserID certSerial :: {}", certSerial);
		}

		// 인증서 상태변경 (폐기 : 30)
		Map<String, String> issueMap = kftcCertComponent.changeStatusCert(certSerial, certPolicyCode, "30");

		String resCode = issueMap.get("ResCode");
		String resMsg = issueMap.get("ResMsg");
		// RA 전문 END

		// 응답 객체
		CrtJctAuthorizeUniCertRefundResponse response = new CrtJctAuthorizeUniCertRefundResponse();
		response.setCancelDate(cbTbs03H98200Res.getCancelDate()); // 취소완료일자
		response.setCancelTime(cbTbs03H98200Res.getCancelTime()); // 취소완료시간
		response.setDeptPersonName(cbTbs03H98200Res.getDeptPersonName()); // 고객명
		response.setCancelSsr(cbTbs03H98200Res.getCancelSSR()); // 수수료금액
		response.setCancelVat(cbTbs03H98200Res.getCancelVAT()); // 부가세금액
		response.setCancelGjno(cbTbs03H98200Res.getCancelGjno()); // 취소수수료 입금계좌번호
		response.setCancelCms(request.getCmsCode());
		response.setCancelOper(cbTbs03H98200Res.getCancelOper()); // 취소 조작자
		response.setResMsg(resMsg); // 인증서폐기결과

		// 세션 삭제
		sessionManager.removeGlobalValue("uniCertRefundSession");

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getCurrentAppVersion", name = "공동인증서 이용안내 앱 버전 조회")
	public CrtJctGetCurrentAppVersionResponse getCurrentAppVersion(IServiceContext serviceContext, CrtJctGetCurrentAppVersionRequest request) {
		// ASIS :: MA3CRTCRT013_101S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 응답 객체
		CrtJctGetCurrentAppVersionResponse response = new CrtJctGetCurrentAppVersionResponse();

		String mblBankingType = "M5";
		if("IOS".equalsIgnoreCase(PRCSharedUtils.getOsVersion())) { //OS구분 ex) Android
			mblBankingType = "M4";
		}

		SmtPhoneBankingInfoResult result = smtPhoneBankingVsnMgtDao.selectLatestAppVersion(mblBankingType);
		log.debug("앱 버전 DB 조회 결과 :: {}", result);

		if (result != null) {
			response.setRegDt(result.getRegDt());
			response.setVsn(result.getVsn());
		}

		// TODO instanceId 수정 필요
		// response.setServerName(SystemUtils.getInstanceId());

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getCertAuthPhone", name = "공동인증서 휴대폰 인증 서비스 가입자 추가인증")
	public CrtJctGetCertAuthPhoneResponse getCertAuthPhone(IServiceContext serviceContext, CrtJctGetCertAuthPhoneRequest request) {
		// ASIS :: H00603,H00621[telAuthNo()]
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 응답 객체
		CrtJctGetCertAuthPhoneResponse response = new CrtJctGetCertAuthPhoneResponse();

		/*
         * =====================================================SMS 전문 전송======================================================
         */
        try {
        	String callSvc = request.getCallSvc();	//호출 서비스[1:공동인증서 갱신, 2:공동인증서 효력정지]
        	String randomKey = RandomKeyUtils.getKey();
        	String callMsg = "";
        	if("1".equals(callSvc)) {
        		callMsg = "[SC제일은행] 공동인증서 갱신을 위한 인증번호입니다. (" + randomKey + ")";
        	} else {
        		callMsg = "[SC제일은행] 공동인증서 효력정지를 위한 인증번호입니다. (" + randomKey + ")";
        	}

            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setMember("0"); // Client측 key일련번호
            smsRequest.setUserCode("ebanking"); // 사용자 발신 코드
            smsRequest.setUserName("I1"); // 사용자명
            smsRequest.setCallPhone1(request.getPhone1()); // 호출 번호 #1
            smsRequest.setCallPhone2(request.getPhone2()); // 호출 번호 #2
            smsRequest.setCallPhone3(request.getPhone3()); // 호출 번호 #3
            smsRequest.setCallMessage(callMsg); // 호출 메시지
            smsRequest.setRateDate(DateUtils.getCurrentDate("yyyyMMdd")); // 메시지 전송 예약일자
            smsRequest.setRateTime(DateUtils.getCurrentDate("HHmmss")); // 메시지 전송 예약시간
            smsRequest.setReqPhone1("1588"); // 회신 번호#1
            smsRequest.setReqPhone2("1599"); // 회신 번호#2
            smsRequest.setReqPhone3(""); // 회신 번호#3
            smsRequest.setCallName("SC제일은행"); // 발신자명
            smsRequest.setDeptCode("GL9-KI3-HS9"); // 회사 코드
            smsRequest.setDeptName("e-뱅u-12103 부"); // 회사명

            String result = smsComponent.sendMain(smsRequest);
            log.debug("SMS발송결과={}", result);

            response.setAuthNum(randomKey);
        } catch (Exception e) {
            log.error("SMS발송오류", e);
        }

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

	@ServiceEndpoint(url = "/getCurrentSecurityStatus", name = "인증/보안 현황 조회")
	public CrtJctGetCurrentSecurityStatusResponse getCurrentSecurityStatus(IServiceContext serviceContext, CrtJctGetCurrentSecurityStatusRequest request) {
		// ASIS :: MA3CRTSCR000_101S
		log.debug("🍕 {} 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		// 이체 한도 조회 전문 처리 START
		log.debug("getCurrentSecurityStatus 이체 한도 조회 START");
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H922");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H922");
		hostRequestOptions.setSvcCd("922");
		hostRequestOptions.setCaptureSystem("OLTP");

		// 개별부 세팅
		CbIbk01H92203Req cbIbk01H92203Req = new CbIbk01H92203Req();
		cbIbk01H92203Req.setReferPart("3");
		cbIbk01H92203Req.setMSList(10);

		OltpResponse<CbIbk01H92203Res> hostResponse;
		CbIbk01H92203Res cbIbk01H92203Res = new CbIbk01H92203Res();

		try {
			// 전문 전송
			hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H92203Req, CbIbk01H92203Res.class);
			cbIbk01H92203Res = hostResponse.getResponse();
		} catch (Exception e) {
			cbIbk01H92203Res.setTimeDepLimt(null);
			cbIbk01H92203Res.setDayDepLimt(null);
		}
		log.debug("getCurrentSecurityStatus 이체 한도 조회 END");
		// 이체 한도 조회 전문 처리 END

		// 단말기 지정 확인
		String userId = (String) sessionManager.getLoginValue("UserID", String.class);
		String deviceCount = StringUtils.nvl(String.valueOf(ipinsideComponent.getCountDeviceAuth(userId)), "0");

		String timeDepLimt = cbIbk01H92203Res.getTimeDepLimt() != null ? cbIbk01H92203Res.getTimeDepLimt().toPlainString() : "";
		String dayDepLimt = cbIbk01H92203Res.getDayDepLimt() != null ? cbIbk01H92203Res.getDayDepLimt().toPlainString() : "";

		// 응답 객체
		CrtJctGetCurrentSecurityStatusResponse response = new CrtJctGetCurrentSecurityStatusResponse();
		// ConnectType : 1: cert, 2: id/pw, 8:BlockChainLogin 뱅크사인, 9:디지털인증
		response.setLoginType(StringUtils.nvl(sessionManager.getLoginValue("ConnectType", String.class), "")); // 로그인타입
		response.setSafeCardKind(StringUtils.nvl(sessionManager.getLoginValue("SafeCardKind", String.class), "")); // 보안매체
		response.setSmartOTP(StringUtils.nvl(sessionManager.getLoginValue("SmartOTP", String.class), "")); // MOTP 여부값 index.xml에 넘겨줌
		response.setDeviceCount(deviceCount); // 지정단말사용개수
		response.setYoDELA(StringUtils.nvl(sessionManager.getLoginValue("YODELAY", String.class), "")); // 지연이체여부
		response.setTimeDepLimt(timeDepLimt); // 1회 이체한도
		response.setDayDepLimt(dayDepLimt); // 1일 이체한도

		log.debug("🍕 {} 종료 🍕", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		return response;
	}

}