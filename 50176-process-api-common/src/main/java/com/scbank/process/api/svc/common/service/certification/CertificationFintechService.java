package com.scbank.process.api.svc.common.service.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H00601Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H84300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H84300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H20100Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H20100Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.dao.Ma3CertUserMgtDao;
import com.scbank.process.api.svc.common.dao.dto.VpcgUsrMgtParameter;
import com.scbank.process.api.svc.common.dao.dto.VpcgUsrMgtResult;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcAuthorizeFintechCertIssueRequest;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcAuthorizeFintechCertIssueResponse;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcAuthorizeFintechCertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcAuthorizeFintechCertRevokeResponse;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertCertificateRequest;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertCertificateResponse;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertIssueUserRequest;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertIssueUserResponse;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertRevokeRequest;
import com.scbank.process.api.svc.common.service.certification.dto.fintech.CrtFtcValidateFintechCertRevokeResponse;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.verification.utils.VerificationUtils;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name="공통-인증센터-핀테크인증서", url="/certification/fintech")
public class CertificationFintechService {

	// 전문
	private final HostClient hostClient;

	// 세션
	private final ISessionContextManager sessionManager;

	// 인증센터 공통
	private final CertUtils certUtils;
	private final FinTechCertVerifyComponent finTechCertVerifyComponent;
	private final CertificationSharedComponent certificationSharedComponent;

	private final SmsComponent smsComponent;

	// DBIO
	private final Ma3CertUserMgtDao ma3CertUserMgtDao;

	// IPINSIDE
	private final IpinsideComponent ipinsideComponent;

	@ServiceEndpoint(url = "/validateFintechCertIssueUser", name = "핀테크인증서 이용등록 본인확인")
	public CrtFtcValidateFintechCertIssueUserResponse validateFintechCertIssueUser(IServiceContext serviceContext, CrtFtcValidateFintechCertIssueUserRequest request) {
		// MA3CRTFTC102_201S, MA3CRTFTC102_301S
		log.debug("🌷{}🌷", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());
		CrtFtcValidateFintechCertIssueUserResponse response = new CrtFtcValidateFintechCertIssueUserResponse();

		/**
		 * 비대면본인확인 프로세스를 끝내고 여기로 오면 세션에 고객정보가 들어있다.
		 */
		String userId = CommonBizConstants.DEFAULT_USER_ID; // "FIRST999"
		String tsPswd = CommonBizConstants.DEFAULT_TS_PASS_WORD; // "111111"
		String yiJMNO = StringUtils.nvl(sessionManager.getGlobalValue("PerBusNo", String.class), "");
		String yiCMFNA = StringUtils.nvl(sessionManager.getGlobalValue("CustName", String.class), "");

		// 개인정보 노출자 여부 판단
		log.debug("validateFintechCertIssueUser 개인정보 노출자 여부 판단 시작");
		Map<String, String> resH504Map = certificationSharedComponent.getYoLRSOTGB(yiJMNO);
		String yoLRSOTGB = resH504Map.get("YOLRSOTGB");
		log.debug("validateFintechCertIssueUser resH504Map :: {}", yiJMNO);

		if("1".equals(yoLRSOTGB)) { // 개인정보노출등록구분 값이 1이면 거래불가 고객으로 안내페이지로 이동
			sessionManager.removeGlobalValue("PerBusNo");
			sessionManager.removeGlobalValue("CustName");
			response.setYoLRSOTGB("1");
			return response;
		}

		/**
		 * 본인확인 전문처리
		 */
		// H92C조회
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H92C");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H92C");
		hostRequestOptions.setSvcCd("92C");
		hostRequestOptions.setVanTp("56");

		// 개별부 세팅
		CbIbk01H92C00Req cbIbk01H92c00Req = new CbIbk01H92C00Req();
        cbIbk01H92c00Req.setUserID(userId);
        cbIbk01H92c00Req.setTSPassword(tsPswd);
        cbIbk01H92c00Req.setYIJMNO(yiJMNO); //주민등록번호
        cbIbk01H92c00Req.setYICMFNA(yiCMFNA); //사용자이름
        cbIbk01H92c00Req.setYIIDGB("2"); //로그인구분(1:로그인, 2:로그아웃(비로그인))
        cbIbk01H92c00Req.setYIJHGB("3"); //거래구분(1:당행고객여부조회, 2:당행고객신규, 3:온라인CASA체크, 4:온라인CASA신규

        // 전문 전송
        OltpResponse<CbIbk01H92C00Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H92c00Req, CbIbk01H92C00Res.class);
        CbIbk01H92C00Res cbIbk01H92c00Res = hostResponse.getResponse();

        // H866조회
        OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");

        // 공통부 세팅
        hostRequestOptions2.setImsTranCd("TI1IBK01");
        hostRequestOptions2.setInClassCd("H866");
        hostRequestOptions2.setSvcCd("866");
        hostRequestOptions2.setVanTp("56");

        // 개별부 세팅
        CbIbk01H86600Req cbIbk01H86600Req = new CbIbk01H86600Req();
        cbIbk01H86600Req.setUserID(userId);
        cbIbk01H86600Req.setTSPassword(tsPswd);
        cbIbk01H86600Req.setYIJMNO(yiJMNO);
        cbIbk01H86600Req.setYIHANDO("Y");

        // 전문 전송
        OltpResponse<CbIbk01H86600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H86600Req, CbIbk01H86600Res.class);
        CbIbk01H86600Res cbIbk01H86600Res = hostResponse2.getResponse();

        // 이용자 판단
        boolean isETB = "Y".equalsIgnoreCase(cbIbk01H92c00Res.getYOEXIST());//당행고객여부
        List<CbIbk01H86600Res.YOMYINF_REC> yomyinfRec =  cbIbk01H86600Res.getYOMYINF_REC();
        boolean isCASA = yomyinfRec != null && yomyinfRec.size() > 0; // 카사유무
        boolean isIBUser = "Y".equalsIgnoreCase(cbIbk01H92c00Res.getYOIBUSER()) && "Y".equalsIgnoreCase(cbIbk01H86600Res.getYOIBGB()); // 인뱅가입유무

        // 모두 참일 경우 인뱅아이디(YOIBID)이 있기때문에 H006조회를 위한값을 세션에 저장해놓는다.
        if (isETB && isCASA && isIBUser) {
        	// H006조회를 위한값 세팅
			HashMap<String, Object> paramH006 = new HashMap<String, Object>();
			paramH006.put("UserID", cbIbk01H86600Res.getYOIBID());//당행DB 기등록자 체크를 위해 UserID 값 사용함.
			paramH006.put("RegNo", yiJMNO);
			paramH006.put("JuminSaupjaNo", yiJMNO);
			paramH006.put("CustJumin1", yiJMNO.substring(0, 6)); //주민번호 앞자리
			paramH006.put("CustJumin2", yiJMNO.substring(6, 13)); //주민번호 뒷자리
			log.debug("paramH006={}", paramH006);
			sessionManager.setGlobalValue("paramH006", paramH006);
			sessionManager.setGlobalValue("CONNECT_TYPE", "D"); //토스면 D 카카오면 F, 값입력시 사용될 구분값 => 현재 토스만 존재
			sessionManager.setGlobalValue("UserID", cbIbk01H86600Res.getYOIBID()); //이용자번호(인뱅아이디)

			/**
	         * 추가인증 세션값 세팅
	         */
	        // 본인확인 전문처리
			OltpRequestOptions hostRequestOptions3 = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

			// 공통부 세팅
			hostRequestOptions3.setImsTranCd("TI1IBK01");
			hostRequestOptions3.setInClassCd("H006");
			hostRequestOptions3.setSvcCd("006");

			// 개별부 세팅
			CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
			cbIbk01H00601Req.setUserID((String) paramH006.get("UserID"));
			cbIbk01H00601Req.setJuminSaupjaNo((String) paramH006.get("JuminSaupjaNo"));
			cbIbk01H00601Req.setCustJumin1((String) paramH006.get("CustJumin1"));
			cbIbk01H00601Req.setCustJumin2((String) paramH006.get("CustJumin2"));
			cbIbk01H00601Req.setCertTranCode("1");
			cbIbk01H00601Req.setChuryGubun("N");
			cbIbk01H00601Req.setCAGubun("2");
			cbIbk01H00601Req.setCustGubun("1");
			// 전문 전송
			OltpResponse<CbIbk01H00601Res> hostResponse3 = this.hostClient.sendOltp(hostRequestOptions3, cbIbk01H00601Req, CbIbk01H00601Res.class);
			CbIbk01H00601Res cbIbk01H00601Res = hostResponse3.getResponse();

			// 세션저장
			sessionManager.setGlobalValue("TSPassword", cbIbk01H00601Res.getTSPassword());
			sessionManager.setGlobalValue("TransPWUseYN", "1");
			sessionManager.setGlobalValue("SafeCardState", "1");
			sessionManager.setGlobalValue("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
			sessionManager.setGlobalValue("SafeCardSeq1", cbIbk01H00601Res.getSafeCardIndex1());
			sessionManager.setGlobalValue("SafeCardSeq2", cbIbk01H00601Res.getSafeCardIndex2());
			sessionManager.setGlobalValue("SafeCardSeq3", cbIbk01H00601Res.getSafeCardIndex3());
			sessionManager.setGlobalValue("SafeCardINDEX", cbIbk01H00601Res.getSecurityIndex());
			sessionManager.setGlobalValue("SafeCardINDEX2", cbIbk01H00601Res.getSecurityIndex2());
			sessionManager.setGlobalValue("SmartOTP", cbIbk01H00601Res.getSmartOTP());

			String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00601Res.getTelCode());
			String HPOne = telCode[0];
			String HPTwo = telCode[1];
			String HPThree = telCode[2];
			sessionManager.setGlobalValue("TeleOne", HPOne);
			sessionManager.setGlobalValue("TeleTwo", HPTwo);
			sessionManager.setGlobalValue("TeleThree", HPThree);

			HashMap<String, Object> paramCheckSafeCard = new HashMap<String, Object>();
			paramCheckSafeCard.put("UserID", paramH006.get("UserID"));
			paramCheckSafeCard.put("RegNo", paramH006.get("RegNo"));
			paramCheckSafeCard.put("TSPassword", cbIbk01H00601Res.getTSPassword());
			paramCheckSafeCard.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
			paramCheckSafeCard.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
			paramCheckSafeCard.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
			paramCheckSafeCard.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
			paramCheckSafeCard.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
			paramCheckSafeCard.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
			paramCheckSafeCard.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
			paramCheckSafeCard.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
			paramCheckSafeCard.put("TeleOne", HPOne);
			paramCheckSafeCard.put("TeleTwo", HPTwo);
			paramCheckSafeCard.put("TeleThree", HPThree);

			sessionManager.setGlobalValue("paramCheckSafeCard", paramCheckSafeCard);
        }

        response.setEtbYn(isETB ? "Y" : "N");
        response.setCasaYn(isCASA ? "Y" : "N");
        response.setIbUserYn(isIBUser ? "Y" : "N");

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

	@ServiceEndpoint(url = "/validateFintechCertCertificate", name = "핀테크인증서 이용등록 인증서 유효성 검사")
	public CrtFtcValidateFintechCertCertificateResponse validateFintechCertCertificate(IServiceContext serviceContext, CrtFtcValidateFintechCertCertificateRequest request) {
		// MA3CMMVCG001_101S
		log.debug("🌷{}🌷", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		CrtFtcValidateFintechCertCertificateResponse response = new CrtFtcValidateFintechCertCertificateResponse();

		// delfinoNonce를 sessionmanager로 넣은게 아닌듯
		HttpSession currentSession = ServiceContextHolder.getContext().request().getSession();

		String vpcg = java.net.URLDecoder.decode(request.getVpcg());

		String dataType 	= StringUtils.nvl(request.getDatatype(), "A");
		String vpcgObj 		= StringUtils.nvl(vpcg, "");
		String delfinoNonce = null;
		JSONObject jVPCG_DATA = new JSONObject();

		sessionManager.removeGlobalValue("FINCERT_SEQNUM");
		sessionManager.removeGlobalValue("FINCERT_CONTYPE");
		sessionManager.removeGlobalValue("FINCERT_CI_VERIFY");

		/**
		 * 서명데이터 파싱
		 */
		if ("".equals(vpcgObj)) {
			currentSession.removeAttribute("delfinoNonce");
			throw new PRCServiceException("PRCCMMVPCG_0001","핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
		}
		try {
			jVPCG_DATA = new JSONObject(vpcgObj);
		} catch (Exception e) {
			currentSession.removeAttribute("delfinoNonce");
			throw new PRCServiceException("PRCCMMVPCG_0001","핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
		}

		delfinoNonce = (String) currentSession.getAttribute("delfinoNonce");
		currentSession.removeAttribute("delfinoNonce");

		if (delfinoNonce == null || "".equalsIgnoreCase(delfinoNonce)) {
			throw new PRCServiceException("PRCCMMVPCG_0001", "핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
		}

		/**
		 * 1단계에서 보관한 정보로 서명값 검증 시작
		 */
		Map<String, Object> paramH006 = certUtils.getCertSession("paramH006");

		if(paramH006 == null) {
        	throw new PRCServiceException("PRCCMMVPCG_0003","본인인증이 정상적으로 처리되지 않았습니다. 처음부터 다시 신청해주시길 바랍니다.");
        }

		String paramh006Userid = StringUtils.defaultIfEmpty((String) paramH006.get("UserID"), ""); // 이용자번호
        if("".equals(paramh006Userid)) {
        	throw new PRCServiceException("PRCCMMVPCG_0003","본인인증이 정상적으로 처리되지 않았습니다. 처음부터 다시 신청해주시길 바랍니다.");
        }

        log.debug("[VPCG] delfinoNonce ::: [{}]", delfinoNonce);
        log.debug("[VPCG] dataType ::: [{}]", dataType);
        log.debug("[VPCG] jVPCG_DATA ::: [{}]", jVPCG_DATA.toString());

        HashMap<String, Object> authVerify = null;

		String resultCode 	= null;			// 본인인증 서명 결과 코드 - 'Y'
		String provider 	= null;			// 핀테크 인증 코드 - toss:'D' , kakao:'F'
		String verifyCi 	= null;			// 고객 CI 정보 - globalSession 'USER_CI_INFO'
		String certSerial 	= null;			// 사설인증서 Serial
		String signText 	= null;			// 전자서명 원본 Text
		String pkcs7Data 	= null;			// pkcs7

		// 서명값검증
		authVerify = finTechCertVerifyComponent.authVerify(delfinoNonce, dataType, jVPCG_DATA.toString(), paramh006Userid);

		if(authVerify == null) {
			throw new PRCServiceException("PRCCMMVPCG_0002","핀테크인증 전자서명 데이터가 존재하지 않습니다. 다시 거래해 주시기 바랍니다.");
		}

		resultCode = (String) authVerify.get("resultCode");
		provider = (String) authVerify.get("provider");
		certSerial = (String) authVerify.get("certSerial");
		signText = (String) authVerify.get("plainText");
		pkcs7Data = (String) authVerify.get("pkcs7Data");
		verifyCi = (String) authVerify.get("verifyCi");

		log.debug("[VPCG] resultCode ::: [{}]", resultCode );
		log.debug("[VPCG] provider ::: [{}]", provider);
		log.debug("[VPCG] verifyCi ::: [{}]", verifyCi);
		log.debug("[VPCG] certSerial ::: [{}]", certSerial);
		log.debug("[VPCG] signText ::: [{}]", signText);
		log.debug("[VPCG] pkcs7Data ::: [{}]", pkcs7Data);

		if( !"000".equalsIgnoreCase(resultCode) || (provider == null || "".equalsIgnoreCase(provider))
				|| (verifyCi == null || "".equalsIgnoreCase(verifyCi)) || (certSerial == null || "".equalsIgnoreCase(certSerial))
				|| (signText == null || "".equalsIgnoreCase(signText)) || (pkcs7Data == null || "".equalsIgnoreCase(pkcs7Data))) {
			throw new PRCServiceException("PRCCMMVPCG_0002","핀테크인증 전자서명 데이터가 존재하지 않습니다. 다시 거래해 주시기 바랍니다.");
		}

		/**
		 * CI 검증
		 */
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H843");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H843");
		hostRequestOptions.setSvcCd("843");

		// 개별부 세팅
        CbIbk01H84300Req cbIbk01H84300Req = new CbIbk01H84300Req();
        cbIbk01H84300Req.setUserID("FIRST999");
        cbIbk01H84300Req.setTSPassword("111111");
        cbIbk01H84300Req.setYIIPGB("3"); // 입력구분(1:이용자번호, 2:CI번호, 3:주민번호)
        cbIbk01H84300Req.setYIJUMIN((String) paramH006.get("RegNo")); // 주민등록번호
        cbIbk01H84300Req.setYICINO(""); // CI번호
		// 전문 전송
        OltpResponse<CbIbk01H84300Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H84300Req, CbIbk01H84300Res.class);
        CbIbk01H84300Res cbIbk01H84300Res = hostResponse.getResponse();

        String yoCONFIRM = cbIbk01H84300Res.getYOCONFIRM(); // CI등록여부(Y:등록, N:미등록)
        String yoCINO = cbIbk01H84300Res.getYOCINO(); // CI번호
        String yoCOND = cbIbk01H84300Res.getYOCOND(); // 사활구분 Y:정상, N:해지

        if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
        	// YOCONFIRM :: CI 미등록 사용자 이거나 / YOCOND :: 사활구분이 N인 사용자는 CI 등록 처리
        	if ("N".equalsIgnoreCase(yoCONFIRM) || "N".equalsIgnoreCase(yoCOND)) {
        		log.error("[VPCG] 등록구분(YOCONFIRM) :::::: [{}] [{}]", paramh006Userid, yoCONFIRM);
        		log.error("[VPCG] 사활구분(YOCOND) :::::: [{}] [{}]", paramh006Userid, yoCOND);
        		sessionManager.setGlobalValue("FINCERT_CI_VERIFY", "N");
        		throw new PRCServiceException("PRCCMMVPCG_0004", "본인인증에 실패했습니다. 고객님의 CI 정보가 미등록 또는 비활성화 상태입니다.");

        	} else {
        		sessionManager.setGlobalValue("FINCERT_CI_VERIFY", "Y");
        	}

        	if(!yoCINO.equals(verifyCi)) {
        		throw new PRCServiceException("PRCCMMVPCG_0005", "본인인증에 실패했습니다. 본인인증 사용자와 핀테크 인증서를 제출한 사용자가 다릅니다.");
        	}

        } else {
        	log.debug("[RunMode={}]CI검증을 스킵했습니다", RuntimeContext.getRunMode());
        }

        sessionManager.setGlobalValue("FINCERT_SEQNUM", certSerial);
        sessionManager.setGlobalValue("FINCERT_CONTYPE", provider);

        VpcgUsrMgtResult vpcgUsrMgtResult = ma3CertUserMgtDao.selectVpcgUsrMgt(paramh006Userid, provider);

        if(vpcgUsrMgtResult == null) {
        	response.setResultCode("VPCG000"); // 신규가입자

		} else {

			String _FIN_CERT_SEQ_NUM = vpcgUsrMgtResult.getFinCertSeqNum(); // 인증서 시리얼 번호
			String _CONNECT_TYPE = vpcgUsrMgtResult.getConnectType(); // 인증서 타입 ::: B(금융인증서-PIN), C(금융인증서-BIO), D(핀테크-토스), F(핀테크-카카오)
			String _JOIN_FLG = vpcgUsrMgtResult.getJoinFlg(); // 핀테크 가입 여주

			response.setJoinFlag(_JOIN_FLG);

			if(certSerial.equalsIgnoreCase(_FIN_CERT_SEQ_NUM) && provider.equalsIgnoreCase(_CONNECT_TYPE) && "Y".equalsIgnoreCase(_JOIN_FLG)) {	// 인증서 시리얼 번호 == O & 인증서 타입 == O ::: 기가입 고객
				response.setResultCode("VPCG101");

        	} else if(certSerial.equalsIgnoreCase(_FIN_CERT_SEQ_NUM) && provider.equalsIgnoreCase(_CONNECT_TYPE) && "N".equalsIgnoreCase(_JOIN_FLG)) {	// 인증서 시리얼 번호 == O & 인증서 타입 == O ::: 이용해지 고객
        		response.setResultCode("VPCG102");

        	} else if(!certSerial.equalsIgnoreCase(_FIN_CERT_SEQ_NUM) && provider.equalsIgnoreCase(_CONNECT_TYPE)) {	// 인증서 시리얼 번호 == X & 인증서 타입 == O ::: 기가입 고객이지만 인증서 시리얼 갱신 필요 고객
        		response.setResultCode("VPCG201");

        	} else if(certSerial.equalsIgnoreCase(_FIN_CERT_SEQ_NUM) && !provider.equalsIgnoreCase(_CONNECT_TYPE)) {	// 인증서 시리얼 번호 == O & 인증서 타입 == X ::: 기가입 고객이지만 인증서 타입 갱신 필요 고객(잘못 거래 고객)
        		response.setResultCode("VPCG301");

        	} else if(!certSerial.equalsIgnoreCase(_FIN_CERT_SEQ_NUM) && !provider.equalsIgnoreCase(_CONNECT_TYPE)) {	// 인증서 시리얼 번호 == X & 인증서 타입 == X ::: 기가입 고객이지만 저장정보와 일치하지 않음 (잘못 거래 고객)
        		response.setResultCode("VPCG302");

        	} else {
        		response.setResultCode("VPCG999");
        	}
		}

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

        return response;
	}

	@ServiceEndpoint(url = "/authorizeFintechCertIssue", name = "핀테크인증서 이용등록")
	public CrtFtcAuthorizeFintechCertIssueResponse authorizeFintechCertIssue(IServiceContext serviceContext, CrtFtcAuthorizeFintechCertIssueRequest request) {
		// MA3CRTFTC102_401S
		log.debug("🌷{}🌷", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		/**
		 * 보안매체 검증
		 */
		Map<String,Object> paramCheckSafeCard = certUtils.getCertSession("paramCheckSafeCard");

		String safeCardKind = (String) paramCheckSafeCard.get("SafeCardKind");
		String smartOTP = (String) paramCheckSafeCard.get("SmartOTP");

		// 본인확인 전문처리
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_TBS03_H201");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1TBS03");
		hostRequestOptions.setInClassCd("H201");
		hostRequestOptions.setSvcCd("731");

		// 개별부 세팅
		CbTbs03H20100Req cbTbs03H20100Req = new CbTbs03H20100Req();
		cbTbs03H20100Req.setUserID((String) paramCheckSafeCard.get("UserID"));
		cbTbs03H20100Req.setInputJumin((String) paramCheckSafeCard.get("RegNo"));
		cbTbs03H20100Req.setTSPassword((String) paramCheckSafeCard.get("TSPassword"));
		cbTbs03H20100Req.setSafeCardKind((String) paramCheckSafeCard.get("SafeCardKind"));
		cbTbs03H20100Req.setSafeCardINDEX((String) paramCheckSafeCard.get("SecurityIndex"));
		cbTbs03H20100Req.setSafeCardINDEX2((String) paramCheckSafeCard.get("SecurityIndex2"));

		cbTbs03H20100Req.setSafeCardState("1");
		cbTbs03H20100Req.setSafeCardIssueNum(" ");
		cbTbs03H20100Req.setSafeCardBranchNum("0");
		cbTbs03H20100Req.setTransPasswordconfirm("0");
		cbTbs03H20100Req.setTransPassword("99999999");

		cbTbs03H20100Req.setTelNo1((String) paramCheckSafeCard.get("TeleOne"));
		cbTbs03H20100Req.setTelNo2((String) paramCheckSafeCard.get("TeleTwo"));
		cbTbs03H20100Req.setTelNo3((String) paramCheckSafeCard.get("TeleThree"));

		if ("1".equals(safeCardKind)) { // 보안카드
			cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			cbTbs03H20100Req.setSafeCardNum2(VerificationUtils.getSafeCardNumber2());

		} else {
			cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			cbTbs03H20100Req.setSafeCardNum2("");

			if ("M".equals(smartOTP) && "3".equals(safeCardKind)) { // MOTP
				cbTbs03H20100Req.setYIMBOTP("Y");
				cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			}
		}

		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions, cbTbs03H20100Req, CbTbs03H20100Res.class);

		/**
		 * 인증서 사용등록
		 */
		String DEVICE_ID = ipinsideComponent.simpleDataDecode();

		String userId = StringUtils.nvl(sessionManager.getGlobalValue("UserID", String.class), ""); //전문조회값
		String connectType = StringUtils.nvl(sessionManager.getGlobalValue("CONNECT_TYPE", String.class), "");//이용자의 의한 선택값
		String connectTypeSession = StringUtils.nvl(sessionManager.getGlobalValue("FINCERT_CONTYPE", String.class), ""); //베라포트처리 세션값
		String finCertSeqNum = StringUtils.nvl(sessionManager.getGlobalValue("FINCERT_SEQNUM", String.class), ""); //베라포트처리 세션값

		log.debug("🌷 userId={}", userId);
		log.debug("🌷 connectType={}", connectType);
		log.debug("🌷 connectTypeSession={}", connectTypeSession);
		log.debug("🌷 finCertSeqNum={}", finCertSeqNum);

		if ("".equals(userId) || "".equals(connectType) || "".equals(connectTypeSession) || "".equals(finCertSeqNum)) {
			// 하나라도 공백이면 예외
			throw new PRCServiceException("PRCFTC0007", "이용등록중 에러가 발생했습니다.");
		}

		// 이용자가 선택한 인증서종류와 베라포트에서 선택된 인증서종류 값이 다를경우
		if (!connectType.equals(connectTypeSession)) {
			if (connectType.equals("D")) {
				throw new PRCServiceException("PRCFTC0003", "'토스' 인증에 실패했습니다.<br>다시 시도해 주시기 바랍니다.");
			} else {
				throw new PRCServiceException("PRCFTC0004", "'카카오페이' 인증에 실패했습니다.<br>다시 시도해 주시기 바랍니다.");
			}
		}

		VpcgUsrMgtParameter vpcgUsrMgtParameter = new VpcgUsrMgtParameter();
		vpcgUsrMgtParameter.setUserId(userId);
		vpcgUsrMgtParameter.setDeviceId(DEVICE_ID);
		vpcgUsrMgtParameter.setConnectType(connectType);
		vpcgUsrMgtParameter.setFinCertSeqNum(finCertSeqNum);

		VpcgUsrMgtResult vpcgUsrMgtResult = ma3CertUserMgtDao.selectVpcgUsrMgt(userId, connectType); // 가입이력 조회

		int successCnt = 0;
		if (vpcgUsrMgtResult == null) { //미가입자일 경우 처음 insert 실행
			successCnt = ma3CertUserMgtDao.insertVpcgUsrMgt(vpcgUsrMgtParameter);

		} else {//가입한적이 있다면 인증정보를 갱신함.
			successCnt = ma3CertUserMgtDao.updateVpcgUsrMgt(vpcgUsrMgtParameter);
		}

		// DB 처리건수가 있으면 SMS 발송
		if(successCnt > 0) {
			String CustName = StringUtils.nvl(sessionManager.getGlobalValue("CustName", String.class), "");
			CustName = CustName.trim().length()>4 ? CustName.trim().substring(0,4) : CustName.trim(); //이름4자리이상 처리.
			String smsCustName = BizCommonUtils.getMaskCustData(CustName, "01");
			String fincertType = "D".equals(connectType) ? "토스" : "카카오페이";
			String SMSMsg = smsCustName + "님 " + fincertType + "인증서 이용 등록완료. 본인요청 아닐 시 신고요망";
			SMSMsg = SMSMsg.length()>80 ? SMSMsg.substring(0,80) : SMSMsg; //80자 처리.
			log.debug("SMSMsgLength(0, 80) => {}", SMSMsg);

			/*
			 *  거래구분(TransGB) 코드값
			 *  001공인인증서(재발급)
			 *  002고객정보관리변경
			 *  003고객휴대폰번호변경
			 *  004고객이메일변경
			 *  005이체한도변경(감액)
			 *  006출금계좌 등록
			 *  007출금계좌 해지
			 *  008블록체인
			 *  009디지털인증서
			 *  010금융인증서
			 *  011토스인증서
			 *  012카카오인증서
			 */

			Map<String, String> smsInput = new HashMap<String, String>();
			smsInput.put("UserID", sessionManager.getGlobalValue("UserID", String.class));
			smsInput.put("RegNo", sessionManager.getGlobalValue("PerBusNo", String.class));
			smsInput.put("TransGB", "D".equals(connectType) ? "011" : "012");
			smsInput.put("SMSMsg", SMSMsg);

			smsComponent.sendCompleteSMS(smsInput);
		}

		// 응답 반환
		CrtFtcAuthorizeFintechCertIssueResponse response = new CrtFtcAuthorizeFintechCertIssueResponse();
		return response;
	}

	@ServiceEndpoint(url = "/validateFintechCertRevoke", name = "핀테크인증서 이용해지 본인확인")
	public CrtFtcValidateFintechCertRevokeResponse validateFintechCertRevoke(IServiceContext serviceContext, CrtFtcValidateFintechCertRevokeRequest request) {
		// MA3CRTFTC102_201S, MA3CRTFTC201_301S
		log.debug("🌷{}🌷", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		CrtFtcValidateFintechCertRevokeResponse response = new CrtFtcValidateFintechCertRevokeResponse();

		/**
		 * 비대면본인확인 프로세스를 끝내고 여기로 오면 세션에 고객정보가 들어있다.
		 */
		String userId = "FIRST999";
		String tSPswd = "111111";
		String yiJMNO = StringUtils.nvl(sessionManager.getGlobalValue("PerBusNo", String.class), "");
		String yiCMFNA = StringUtils.nvl(sessionManager.getGlobalValue("CustName", String.class), "");

		/**
		 * 본인확인 전문처리
		 */
		// H92C조회
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H92C");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1IBK01");
		hostRequestOptions.setInClassCd("H92C");
		hostRequestOptions.setSvcCd("92C");
		hostRequestOptions.setVanTp("56");

		// 개별부 세팅
        CbIbk01H92C00Req cbIbk01H92c00Req = new CbIbk01H92C00Req();
        cbIbk01H92c00Req.setUserID(userId);
        cbIbk01H92c00Req.setTSPassword(tSPswd);
        cbIbk01H92c00Req.setYIJMNO(yiJMNO); //주민등록번호
        cbIbk01H92c00Req.setYICMFNA(yiCMFNA); //사용자이름
        cbIbk01H92c00Req.setYIIDGB("2"); //로그인구분(1:로그인, 2:로그아웃(비로그인))
        cbIbk01H92c00Req.setYIJHGB("3"); //거래구분(1:당행고객여부조회, 2:당행고객신규, 3:온라인CASA체크, 4:온라인CASA신규

        // 전문 전송
        OltpResponse<CbIbk01H92C00Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, cbIbk01H92c00Req, CbIbk01H92C00Res.class);
        CbIbk01H92C00Res cbIbk01H92c00Res = hostResponse.getResponse();

        // H866조회
        OltpRequestOptions hostRequestOptions2 = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");

        // 공통부 세팅
        hostRequestOptions2.setImsTranCd("TI1IBK01");
        hostRequestOptions2.setInClassCd("H866");
        hostRequestOptions2.setSvcCd("866");
        hostRequestOptions2.setVanTp("56");

        // 개별부 세팅
        CbIbk01H86600Req cbIbk01H86600Req = new CbIbk01H86600Req();
        cbIbk01H86600Req.setUserID(userId);
        cbIbk01H86600Req.setTSPassword(tSPswd);
        cbIbk01H86600Req.setYIJMNO(yiJMNO);
        cbIbk01H86600Req.setYIHANDO("Y");

        // 전문 전송
        OltpResponse<CbIbk01H86600Res> hostResponse2 = this.hostClient.sendOltp(hostRequestOptions2, cbIbk01H86600Req, CbIbk01H86600Res.class);
        CbIbk01H86600Res cbIbk01H86600Res = hostResponse2.getResponse();

        // 이용자 판단
        boolean isETB = "Y".equalsIgnoreCase(cbIbk01H92c00Res.getYOEXIST());//당행고객여부
        List<CbIbk01H86600Res.YOMYINF_REC> yomyinfRec =  cbIbk01H86600Res.getYOMYINF_REC();
        boolean isCASA = yomyinfRec != null && yomyinfRec.size() > 0; // 카사유무
        boolean isIBUser = "Y".equalsIgnoreCase(cbIbk01H92c00Res.getYOIBUSER()) && "Y".equalsIgnoreCase(cbIbk01H86600Res.getYOIBGB()); // 인뱅가입유무

        if (isETB && isCASA && isIBUser) {
        	//당행고객이며, 입출금계좌가 존재하고, 인터넷뱅킹 가입자일때

        	//인증서DB 조회 시작
        	VpcgUsrMgtResult vpcgUsrMgtResult = ma3CertUserMgtDao.selectVpcgUsrMgt(cbIbk01H86600Res.getYOIBID(), sessionManager.getGlobalValue("CONNECT_TYPE", String.class));

        	if (vpcgUsrMgtResult == null) {
        		// 조회결과가 없으면 신규
        		response.setFincertUserYn("N");

        	} else {

        		String joinFlg = vpcgUsrMgtResult.getJoinFlg();
        		if ("N".equals(joinFlg)) {
        			// 결과가 있으며, 가입여부(JOIN_FLG)가 N이면 기가입자이며 해지상태
        			response.setFincertUserYn("N");

        		} else {
        			// 기가입자, 등록상태
        			response.setFincertUserYn("Y");

        			// H006조회를 위한값 세팅
					HashMap<String, Object> paramH006 = new HashMap<String, Object>();
					paramH006.put("UserID", cbIbk01H86600Res.getYOIBID());//당행DB 기등록자 체크를 위해 UserID 값 사용함.
					paramH006.put("RegNo", yiJMNO);
					paramH006.put("JuminSaupjaNo", yiJMNO);
					paramH006.put("CustJumin1", yiJMNO.substring(0, 6)); //주민번호 앞자리
					paramH006.put("CustJumin2", yiJMNO.substring(6, 13)); //주민번호 뒷자리

					log.debug("paramH006={}", paramH006);

					sessionManager.setGlobalValue("paramH006", paramH006);
					sessionManager.setGlobalValue("UserID", cbIbk01H86600Res.getYOIBID()); //이용자번호(인뱅아이디)

					//보안매체 후 당행DB조회 시 사용될 key값
					HashMap<String, String> paramFincertDB = new HashMap<String, String>();
					paramFincertDB.put("USER_ID", vpcgUsrMgtResult.getUserId());
					paramFincertDB.put("CONNECT_TYPE", vpcgUsrMgtResult.getConnectType());

					log.debug("UserID={}", vpcgUsrMgtResult.getUserId());
					log.debug("paramFincertDB={}", paramFincertDB);

					sessionManager.setGlobalValue("paramFincertDB", paramFincertDB);
					sessionManager.setGlobalValue("UserID", vpcgUsrMgtResult.getUserId());
					sessionManager.setGlobalValue("CONNECT_TYPE", "D"); //토스면 D 카카오면 F, 값입력시 사용될 구분값 => 현재 토스만 존재

					/**
			         * 추가인증 세션값 세팅
			         */
			        // 본인확인 전문처리
					OltpRequestOptions hostRequestOptions3 = this.hostClient.getOltpRequestOptions("CB_IBK01_H006");

					// 공통부 세팅
					hostRequestOptions3.setImsTranCd("TI1IBK01");
					hostRequestOptions3.setInClassCd("H006");
					hostRequestOptions3.setSvcCd("006");

					// 개별부 세팅
					CbIbk01H00601Req cbIbk01H00601Req = new CbIbk01H00601Req();
					cbIbk01H00601Req.setUserID((String) paramH006.get("UserID"));
					cbIbk01H00601Req.setJuminSaupjaNo((String) paramH006.get("JuminSaupjaNo"));
					cbIbk01H00601Req.setCustJumin1((String) paramH006.get("CustJumin1"));
					cbIbk01H00601Req.setCustJumin2((String) paramH006.get("CustJumin2"));
					cbIbk01H00601Req.setCertTranCode("1");
					cbIbk01H00601Req.setChuryGubun("N");
					cbIbk01H00601Req.setCAGubun("2");
					cbIbk01H00601Req.setCustGubun("1");

					// 전문 전송
					OltpResponse<CbIbk01H00601Res> hostResponse3 = this.hostClient.sendOltp(hostRequestOptions3, cbIbk01H00601Req, CbIbk01H00601Res.class);
					CbIbk01H00601Res cbIbk01H00601Res = hostResponse3.getResponse();

					// 세션저장
					sessionManager.setGlobalValue("TSPassword", cbIbk01H00601Res.getTSPassword());
					sessionManager.setGlobalValue("TransPWUseYN", "1");
					sessionManager.setGlobalValue("SafeCardState", "1");
					sessionManager.setGlobalValue("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
					sessionManager.setGlobalValue("SafeCardSeq1", cbIbk01H00601Res.getSafeCardIndex1());
					sessionManager.setGlobalValue("SafeCardSeq2", cbIbk01H00601Res.getSafeCardIndex2());
					sessionManager.setGlobalValue("SafeCardSeq3", cbIbk01H00601Res.getSafeCardIndex3());
					sessionManager.setGlobalValue("SafeCardINDEX", cbIbk01H00601Res.getSecurityIndex());
					sessionManager.setGlobalValue("SafeCardINDEX2", cbIbk01H00601Res.getSecurityIndex2());
					sessionManager.setGlobalValue("SmartOTP", cbIbk01H00601Res.getSmartOTP());

					String[] telCode = certUtils.phoneNumberPartArray(cbIbk01H00601Res.getTelCode());
					String HPOne = telCode[0];
					String HPTwo = telCode[1];
					String HPThree = telCode[2];
					sessionManager.setGlobalValue("TeleOne", HPOne);
					sessionManager.setGlobalValue("TeleTwo", HPTwo);
					sessionManager.setGlobalValue("TeleThree", HPThree);

					HashMap<String, Object> paramCheckSafeCard = new HashMap<String, Object>();
					paramCheckSafeCard.put("UserID", paramH006.get("UserID"));
					paramCheckSafeCard.put("RegNo", paramH006.get("RegNo"));
					paramCheckSafeCard.put("TSPassword", cbIbk01H00601Res.getTSPassword());
					paramCheckSafeCard.put("EmailAddr", cbIbk01H00601Res.getEmailAddr());
					paramCheckSafeCard.put("SafeCardKind", cbIbk01H00601Res.getSafeCardKind());
					paramCheckSafeCard.put("SafeCardIndex1", cbIbk01H00601Res.getSafeCardIndex1());
					paramCheckSafeCard.put("SafeCardIndex2", cbIbk01H00601Res.getSafeCardIndex2());
					paramCheckSafeCard.put("SafeCardIndex3", cbIbk01H00601Res.getSafeCardIndex3());
					paramCheckSafeCard.put("SecurityIndex", cbIbk01H00601Res.getSecurityIndex());
					paramCheckSafeCard.put("SecurityIndex2", cbIbk01H00601Res.getSecurityIndex2());
					paramCheckSafeCard.put("SmartOTP", cbIbk01H00601Res.getSmartOTP());
					paramCheckSafeCard.put("TeleOne", HPOne);
					paramCheckSafeCard.put("TeleTwo", HPTwo);
					paramCheckSafeCard.put("TeleThree", HPThree);

					sessionManager.setGlobalValue("paramCheckSafeCard", paramCheckSafeCard);
        		}

        	}

        } else {
        	response.setFincertUserYn("N");
        }

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		return response;
	}

	@ServiceEndpoint(url = "/authorizeFintechCertRevoke", name = "핀테크인증서 이용해지")
	public CrtFtcAuthorizeFintechCertRevokeResponse authorizeFintechCertRevoke(IServiceContext serviceContext, CrtFtcAuthorizeFintechCertRevokeRequest request) {
		// MA3CRTFTC201_401S
		log.debug("🌷{}🌷", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name());

		/**
		 * 보안매체 검증
		 */
		Map<String,Object> paramCheckSafeCard = certUtils.getCertSession("paramCheckSafeCard");

		String safeCardKind = (String) paramCheckSafeCard.get("SafeCardKind");
		String smartOTP = (String) paramCheckSafeCard.get("SmartOTP");

		// 본인확인 전문처리
		OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_TBS03_H201");

		// 공통부 세팅
		hostRequestOptions.setImsTranCd("TI1TBS03");
		hostRequestOptions.setInClassCd("H201");
		hostRequestOptions.setSvcCd("731");

		// 개별부 세팅
		CbTbs03H20100Req cbTbs03H20100Req = new CbTbs03H20100Req();
		cbTbs03H20100Req.setUserID((String) paramCheckSafeCard.get("UserID"));
		cbTbs03H20100Req.setInputJumin((String) paramCheckSafeCard.get("RegNo"));
		cbTbs03H20100Req.setTSPassword((String) paramCheckSafeCard.get("TSPassword"));
		cbTbs03H20100Req.setSafeCardKind((String) paramCheckSafeCard.get("SafeCardKind"));
		cbTbs03H20100Req.setSafeCardINDEX((String) paramCheckSafeCard.get("SecurityIndex"));
		cbTbs03H20100Req.setSafeCardINDEX2((String) paramCheckSafeCard.get("SecurityIndex2"));

		cbTbs03H20100Req.setSafeCardState("1");
		cbTbs03H20100Req.setSafeCardIssueNum(" ");
		cbTbs03H20100Req.setSafeCardBranchNum("0");
		cbTbs03H20100Req.setTransPasswordconfirm("0");
		cbTbs03H20100Req.setTransPassword("99999999");

		cbTbs03H20100Req.setTelNo1((String) paramCheckSafeCard.get("TeleOne"));
		cbTbs03H20100Req.setTelNo2((String) paramCheckSafeCard.get("TeleTwo"));
		cbTbs03H20100Req.setTelNo3((String) paramCheckSafeCard.get("TeleThree"));

		if ("1".equals(safeCardKind)) { // 보안카드
			cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			cbTbs03H20100Req.setSafeCardNum2(VerificationUtils.getSafeCardNumber2());

		} else {
			cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			cbTbs03H20100Req.setSafeCardNum2("");

			if ("M".equals(smartOTP) && "3".equals(safeCardKind)) { // MOTP
				cbTbs03H20100Req.setYIMBOTP("Y");
				cbTbs03H20100Req.setSafeCardNum(VerificationUtils.getSafeCardNumber1());
			}
		}
		// 전문 전송
		this.hostClient.sendOltp(hostRequestOptions, cbTbs03H20100Req, CbTbs03H20100Res.class);

		/**
		 * DB처리
		 */
		//인증서DB 조회 시작
    	VpcgUsrMgtResult vpcgUsrMgtResult = ma3CertUserMgtDao.selectVpcgUsrMgt(sessionManager.getGlobalValue("UserID", String.class), sessionManager.getGlobalValue("CONNECT_TYPE", String.class));

    	if (vpcgUsrMgtResult == null) {
    		throw new PRCServiceException("PRCFTC0009", "이용해지중 에러가 발생했습니다.");

    	} else {

    		String joinFlg = vpcgUsrMgtResult.getJoinFlg();

    		if ("N".equals(joinFlg)) {
    			throw new PRCServiceException("PRCFTC0010", "이용해지중 에러가 발생했습니다.");

    		} else {

    			HashMap<String, Object> paramFincertDB = (HashMap<String, Object>) sessionManager.getGlobalValue("paramFincertDB", Map.class);
    			String DEVICE_ID = ipinsideComponent.simpleDataDecode();

    			VpcgUsrMgtParameter parameter = new VpcgUsrMgtParameter();
    			parameter.setUserId(sessionManager.getGlobalValue("UserID", String.class));
    			parameter.setConnectType(sessionManager.getGlobalValue("CONNECT_TYPE", String.class)); //인증서구분(D:토스, F:카카오)
    			parameter.setDeviceId(DEVICE_ID);
    			ma3CertUserMgtDao.deleteVpcgUsrMgt(parameter);

    		}

    	}

		CrtFtcAuthorizeFintechCertRevokeResponse response = new CrtFtcAuthorizeFintechCertRevokeResponse();
		return response;
	}

}