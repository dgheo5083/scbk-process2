package com.scbank.process.api.svc.common.service.authenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initech.core.util.URLDecoder;
import com.scbank.process.api.edmi.dto.host.CbIbk01H504Req;
import com.scbank.process.api.edmi.dto.host.CbIbk01H504Res;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01D76400Req;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01D76400Res;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01D76A00Req;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01D76A00Res;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01H76400Req;
import com.scbank.process.api.edmi.dto.oltp.CBIbk01H76400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H87300Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89200Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H89200Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D84200Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D84200Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D90400Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D90400Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D98500Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03D98500Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H81800Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H81800Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H90300Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H90300Res;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H90400Req;
import com.scbank.process.api.edmi.dto.oltp.CbTbs03H90400Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.code.ICodeManager;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.CertificationSharedComponent;
import com.scbank.process.api.svc.common.dao.NfTradInfoMgtDao;
import com.scbank.process.api.svc.common.dao.SaoOtpChargeDao;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.NonFaceAuthInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SaoOtpChargeParameter;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeMotpIssueRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeMotpIssueResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeOtpErrorClearRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeOtpErrorClearResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeOtpRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeOtpResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeSimpLognDeleteRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeSimpLognDeleteResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeSimpLognRegistRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtAuthorizeSimpLognRegistResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtCustInfoLeakerTranBlockRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtCustInfoLeakerTranBlockResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetMotpIssueCddRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetMotpIssueCddResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetMotpPinNumRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetMotpPinNumResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetOtpRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetOtpResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetSaupNumOdcloudRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetSaupNumOdcloudResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetSimpLognRegistMotpRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtGetSimpLognRegistMotpResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtListMotpIssueAcctRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtListMotpIssueAcctResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtRequestOtpCorrectionRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtRequestOtpCorrectionResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateMotpIssuePreTranRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateMotpIssuePreTranResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateMotpIssueUserRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateMotpIssueUserResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateOtpAcctRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateOtpAcctResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidatePreOtpErrorClearRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidatePreOtpErrorClearResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognPreRegistRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognPreRegistResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognRegistAcctRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognRegistAcctResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognRegistRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtValidateSimpLognRegistResponse;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtVerfiyOdcloudSignRequest;
import com.scbank.process.api.svc.common.service.authenticator.dto.management.AthMgtVerfiyOdcloudSignResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.PaymentAccountInfo;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.CertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.components.verification.VerificationComponent;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiRequest;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationGetCustomerCiResponse;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.FormatUtils;
import com.scbank.process.api.svc.shared.utils.MotpUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;
import com.wizvera.WizveraConfig;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-보안매체-보안매체관리", url = "/authenticator/management")
public class AuthenticatorManagementService {

    // 전문
    private final HostClient hostClient;

    // DAO
    private final SaoOtpChargeDao saoOtpChargeDao;
    private final NfTradInfoMgtDao nfTradInfoMgtDao;

    // 세션
    private final ISessionContextManager sessionManager;

    private final SmsComponent smsComponent;
    private final ICodeManager codeManager;
    
    private final AccountListComponent accountListComponent;
    private final CertificationSharedComponent certificationSharedComponent;
    private final VerificationComponent verificationComponent;
    private final CertVerifyComponent certVerifyComponent;
    private final FinCertVerifyComponent finCertVerifyComponent;
    private final FinTechCertVerifyComponent finTechCertVerifyComponent;
    private final CertUtils certUtils;

    @Value("${delfino.config.path:}")
	private String configPath;
    
    /************************************************************************************************************************************************
     * 간편로그인(MOTP) 가입
     * validateSimpLognPreRegist : 간편로그인 가입 페이지 진입시 체크
     * validateSimpLognRegist : 간편로그인 가입 비로그인 본인확인
     * getSimpLognRegistMotp : 간편로그인 가입 모바일OTP 검사
     * validateSimpLognRegistAcct : 간편로그인 가입 계좌확인
     * authorizeSimpLognRegist : 간편로그인 가입
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/validateSimpLognPreRegist", name = "간편로그인 가입 페이지 진입시 체크")
    public AthMgtValidateSimpLognPreRegistResponse validateSimpLognPreRegist(AthMgtValidateSimpLognPreRegistRequest request) {
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        AthMgtValidateSimpLognPreRegistResponse response = new AthMgtValidateSimpLognPreRegistResponse();

        if (sessionManager.isLogin()) {
            response.setNoLogin("N");
            response.setSafeCardIssueNum(sessionManager.getLoginValue("SafeCardIssueNum", String.class));
            response.setSmartOTP(sessionManager.getLoginValue("SmartOTP", String.class));
            response.setSafeCardKind(sessionManager.getLoginValue("SafeCardKind", String.class));
        } else {
            response.setNoLogin("Y");
        }

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }

    @ServiceEndpoint(url = "/validateSimpLognRegist", name = "간편로그인 가입 비로그인 본인확인")
    public AthMgtValidateSimpLognRegistResponse validateSimpLognRegist(AthMgtValidateSimpLognRegistRequest request) {
        // MA3CRTSMP301_101S
    	log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        // 전문전송 처리
        OltpRequestOptions h892Option = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
        h892Option.setImsTranCd("TI1IBK01");
        h892Option.setInClassCd("H892");
        h892Option.setSvcCd("892");

        // 개별부 세팅
        CbIbk01H89200Req h892Req = new CbIbk01H89200Req();
        h892Req.setUserID(SessionUtils.getSessionValue("UserID"));
        h892Req.setActType("1");
        h892Req.setE2ERegNum(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), sessionManager.getGlobalValue("cid", String.class)));

        CbIbk01H89200Res h892Res = this.hostClient.sendOltp(h892Option, h892Req, CbIbk01H89200Res.class).getResponse();
        
        AthMgtValidateSimpLognRegistResponse response = new AthMgtValidateSimpLognRegistResponse();
        response.setSafeCardKind(h892Res.getSafeCardKind());
        response.setSmartOTP(h892Res.getSmartOTP());
        
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }

    @ServiceEndpoint(url = "/getSimpLognRegistMotp", name = "간편로그인 가입 모바일OTP 검사")
    public AthMgtGetSimpLognRegistMotpResponse getSimpLognRegistMotp(AthMgtGetSimpLognRegistMotpRequest request) {
        // MA3CRTSMP101_101S TI1IBK01_H764
        // 이미 간편로그인 가입되어있는지 확인
    	log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        // 전문전송 처리
        OltpRequestOptions h764Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H764");
        h764Options.setImsTranCd("TI1IBK01");
        h764Options.setInClassCd("H764");
        h764Options.setSvcCd("764");

        // 개별부 세팅
        CBIbk01H76400Req h764Req = new CBIbk01H76400Req();
        h764Req.setYIUSERID(SessionUtils.getSessionValue("UserID"));
        h764Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
        
        if (request.getYiMOTPNO() != null && !"".equals(request.getYiMOTPNO())) {
            h764Req.setYIMOTPGB("Y"); // OTP번호여부
            h764Req.setYIMOTPNO(request.getYiMOTPNO());
        }
        if (request.getNoLogin() != null && "Y".equals(request.getNoLogin())) {
            h764Req.setYIJMNO(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), SessionUtils.getSessionValue("cid"))); // 주민번호
            h764Req.setYIIDGB("Y"); // 비로그인 여부
        }

        CBIbk01H76400Res h764Res = this.hostClient.sendOltp(h764Options, h764Req, CBIbk01H76400Res.class).getResponse();

        AthMgtGetSimpLognRegistMotpResponse response = new AthMgtGetSimpLognRegistMotpResponse();
        response.setYoCVMYN(h764Res.getYOCVMYN()); // 가입여부
        response.setYoCVMHP(h764Res.getYOCVMHP()); // 가입일
        
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }

    @ServiceEndpoint(url = "/validateSimpLognRegistAcct", name = "간편로그인 가입 계좌확인")
    public AthMgtValidateSimpLognRegistAcctResponse validateSimpLognRegistAcct(AthMgtValidateSimpLognRegistAcctRequest request) {
        // MA3CRTSMP101_152S
        // 간편로그인 해지도 같이 사용함
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        OltpRequestOptions h873Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H873");
        h873Options.setImsTranCd("TI1IBK01");
        h873Options.setInClassCd("H873");
        h873Options.setSvcCd("873");

        CbIbk01H87300Req h873Req = new CbIbk01H87300Req();
        h873Req.setCGAcctNum(request.getSelAcctNum());
        h873Req.setAcctPassword(request.getAcctBb());
        h873Req.setPasswordVerifiYorN("Y"); // 암호검증여부
        h873Req.setPerNo(SessionUtils.getSessionValue("PerBusNo"));
        h873Req.setUserID(StringUtils.defaultIfEmpty(request.getUserId(), CommonBizConstants.DEFAULT_USER_ID));
        h873Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);

        this.hostClient.sendOltp(h873Options, h873Req, CbIbk01H87300Res.class).getResponse();

        AthMgtValidateSimpLognRegistAcctResponse response = new AthMgtValidateSimpLognRegistAcctResponse();
        return response;
    }

    @ServiceEndpoint(url = "/authorizeSimpLognRegist", name = "간편로그인 가입")
    public AthMgtAuthorizeSimpLognRegistResponse authorizeSimpLognRegist(AthMgtAuthorizeSimpLognRegistRequest request) {
        // MA3CRTMOA003_101S, MA3CRTSMP101_101S
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        /**
         * PIN 번호 확인
         */
        // 전문전송 처리
        OltpRequestOptions h903Options = this.hostClient.getOltpRequestOptions("CB_TBS03_H903");
        h903Options.setImsTranCd("TI1TBS03");
        h903Options.setInClassCd("H903");
        h903Options.setSvcCd("903");

        // 개별부 세팅
        CbTbs03H90300Req h903Req = new CbTbs03H90300Req();
        h903Req.setYIUSERID(sessionManager.getLoginValue("UserID", String.class));
        h903Req.setYIDVINF(request.getYiDVINF()); // 모바일OTP인증 Device Id
        h903Req.setYIDVGBN(request.getYiDVGBN()); // 모바일OTP인증 Device Cd
        h903Req.setYIOTPNO(request.getYiOTPNO()); // 모바일OTP 일련번호
        h903Req.setYIPINNO(request.getPinBb01()); // 모바일OTP PIN비밀번호
        h903Req.setYIOTPVDGB("990"); // OTP벤더구분

        CbTbs03H90300Res h903Res = this.hostClient.sendOltp(h903Options, h903Req, CbTbs03H90300Res.class).getResponse();

        String secretKeyb = h903Res.getYOSEKEY();
        String rndChallenge = h903Res.getYOMRAND();
        String pinNumber = request.getPinBb01();
        String clientRnd = request.getClientRnd();
        String deviceInfo = request.getYiDVINF();

        log.debug("secretKeyb={}, rndChallenge={}, pinNumber={}, clientRnd={}, deviceInfo={}", secretKeyb, rndChallenge, pinNumber, clientRnd, deviceInfo);

        AthMgtAuthorizeSimpLognRegistResponse response = new AthMgtAuthorizeSimpLognRegistResponse();
        response.setYoSEKEY(MotpUtils.encryptTokenKey(secretKeyb, deviceInfo, clientRnd, pinNumber));
        response.setYoMRAND(MotpUtils.encryptServerChallenge(rndChallenge, deviceInfo, clientRnd, pinNumber));

        /**
         * 간편로그인 등록
         */
        String noLogin = StringUtils.defaultIfEmpty(request.getNoLogin(), "");
        String SafeCardKind = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("SafeCardKind", String.class), ""); // 보안매체종류 (0:미발급, 1:안전카드, 2:구OTP, 3:OTP(SmartOTP > 0:스마트OTP, M:모바일OTP))
        String SmartOTP = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("SmartOTP", String.class), "");

        // 전문전송 처리
        OltpRequestOptions d764Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D764");
        d764Options.setImsTranCd("TI1IBK01");
        d764Options.setInClassCd("D764");
        d764Options.setSvcCd("764");

        // 개별부 세팅
        CBIbk01D76400Req d764Req = new CBIbk01D76400Req();
        d764Req.setYIUSERID(SessionUtils.getSessionValue("UserID"));
        d764Req.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
        d764Req.setYIMOTPGB("Y");
        d764Req.setYIMOTPNO(request.getYiOTPNO().substring(4, request.getYiOTPNO().length()));
		if ("Y".equals(noLogin)) {
            d764Req.setYIJMNO(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), sessionManager.getGlobalValue("cid", String.class)));
            d764Req.setYIIDGB("Y"); // 비로그인
        }

        if (("3".equals(SafeCardKind) && "M".equals(SmartOTP)) || "M".equals(SmartOTP) || "Y".equals(noLogin)) { // MOTP일 경우
            this.hostClient.sendOltp(d764Options, d764Req, CBIbk01D76400Res.class).getResponse();
        } else {
            throw new PRCServiceException("999999", "보안매체를 확인해 주세요.");
        }

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }

    /************************************************************************************************************************************************
     * 간편로그인(MOTP) 가입 해제
     * authorizeSimpLognDelete : 간편로그인 해지
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/authorizeSimpLognDelete", name = "간편로그인 해지")
    public AthMgtAuthorizeSimpLognDeleteResponse authorizeSimpLognDelete(AthMgtAuthorizeSimpLognDeleteRequest request) {
        // MA3CRTSMP101_101S
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        String noLogin = StringUtils.defaultIfEmpty(request.getNoLogin(), "");

        // 전문전송 처리
        OltpRequestOptions d764Options = this.hostClient.getOltpRequestOptions("CB_IBK01_D76A");
        d764Options.setImsTranCd("TI1IBK01");
        d764Options.setInClassCd("D76A");
        d764Options.setSvcCd("76A");

        // 개별부 세팅
        CBIbk01D76A00Req d76aReq = new CBIbk01D76A00Req();
        d76aReq.setYIUSERID(SessionUtils.getSessionValue("UserID"));
        d76aReq.setTSPassword(SessionUtils.getSessionValue("TSPassword"));
        
        if ("Y".equals(noLogin)) {
        	d76aReq.setYIJMNO(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo"), sessionManager.getGlobalValue("cid", String.class)));
            d76aReq.setYIIDGB("Y"); // 비로그인
            this.hostClient.sendOltp(d764Options, d76aReq, CBIbk01D76A00Res.class);
            
        } else {
            this.hostClient.sendOltpWithSecure(d764Options, d76aReq, CBIbk01D76A00Res.class);
        }

        AthMgtAuthorizeSimpLognDeleteResponse response = new AthMgtAuthorizeSimpLognDeleteResponse();
        return response;
    }

    /************************************************************************************************************************************************
     * OTP 이용 등록
     * validateOtpAcct : OTP 이용등록/해제 계좌확인
     * authorizeOtp : OTP 이용등록/해제
     * getOtp : OTP 이용등록/해제 정보조회
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/validateOtpAcct", name = "OTP 이용등록/해제 계좌확인")
	public AthMgtValidateOtpAcctResponse validateOtpAcct(AthMgtValidateOtpAcctRequest request) {
		// MA3CRTOTP002_102S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String accountNum = StringUtils.nvl(request.getAccountNum(), "").replaceAll("-", "");
		String bbKeypadNum = request.getBbKeypadNum();
		String perBusNo = SessionUtils.getSessionValue("PerBusNo");
		
		// 전문전송 처리
		OltpRequestOptions h892Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H892");
		h892Options.setImsTranCd("TI1IBK01");
		h892Options.setInClassCd("H892");
		h892Options.setSvcCd("892");
		
		// 개별부 세팅
		CbIbk01H89200Req h892Req = new CbIbk01H89200Req();
		if (sessionManager.isLogin()) {
			h892Req.setUserID(sessionManager.getLoginValue("UserID", String.class));
		} else {
			h892Req.setUserID("FIRST999");
		}
		h892Req.setE2ERegNum(perBusNo);
		h892Req.setActType("2"); // 처리구분 ( 예비처리 ) 1: 고객정보조회( 본처리 ) 2: 계좌검증 3: 계좌미검증 4: 법인 ID 검증 5: 신용카드조회회원
		h892Req.setAcctNum(accountNum); // 계좌번호
		h892Req.setAcctPassword(bbKeypadNum); // 계좌비밀번호

		CbIbk01H89200Res h892Res = this.hostClient.sendOltp(h892Options, h892Req, CbIbk01H89200Res.class).getResponse();

		String userId = h892Res.getUserID2();
		String safeCardKind = h892Res.getSafeCardKind();
		String smartOTP = h892Res.getSmartOTP();
		String custName = h892Res.getCustName();

		sessionManager.setGlobalValue("CustName", custName);
		sessionManager.setGlobalValue("PerBusNo", perBusNo);
		sessionManager.setGlobalValue("UserID", userId);
		sessionManager.setGlobalValue("TSPassword", "99999999");
		sessionManager.setGlobalValue("SafeCardKind", safeCardKind);
		sessionManager.setGlobalValue("SmartOTP", smartOTP);
		sessionManager.setGlobalValue("otpAccountNum", accountNum);
		sessionManager.setGlobalValue("otpBbKeypadNum", bbKeypadNum);

		AthMgtValidateOtpAcctResponse response = new AthMgtValidateOtpAcctResponse();
		// 꼭 필요하진 않은거가은데
		response.setUserId(userId);
		response.setSafeCardKind(safeCardKind);
		response.setSmartOTP(smartOTP);
		response.setCustName(custName);
		
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

    @ServiceEndpoint(url = "/getOtp", name = "OTP 이용등록/해제 정보조회")
    public AthMgtGetOtpResponse getOtp(AthMgtGetOtpRequest request) {
    	// MA3CRTMNG003_102S
    	log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());
    	
    	// 개인정보노출자 판단
		Map<String, String> h504Res = certificationSharedComponent.getYoLRSOTGB(SessionUtils.getSessionValue("PerBusNo"));
		String YOLRSOTGB = h504Res.get("YOLRSOTGB");
		if("1".equals(YOLRSOTGB)) {
			AthMgtGetOtpResponse response = new AthMgtGetOtpResponse();
			response.setYOLRSOTGB(YOLRSOTGB);
			return response;
		}
		
		String SafeCardKind = "";
		String SmartOTP = "";
		String CustName = "";
		String accountNum = "";
		String accountListJsonStr = "";

		log.debug("sessionManager.isLogin():{}", sessionManager.isLogin());
		
		if (sessionManager.isLogin()) {
			SafeCardKind = sessionManager.getLoginValue("SafeCardKind", String.class);
			SmartOTP = sessionManager.getLoginValue("SmartOTP", String.class);
			CustName = sessionManager.getLoginValue("CustName", String.class);

			List<PaymentAccountInfo> paymentAccountList = accountListComponent.getPaymentAccountList();
			if (paymentAccountList != null && paymentAccountList.size() > 0) {
				accountListJsonStr = new JSONArray(paymentAccountList.stream()
						.filter(item -> "10".equals(item.getDrawAcctNum().substring(3, 5))
								|| "20".equals(item.getDrawAcctNum().substring(3, 5))
								|| "30".equals(item.getDrawAcctNum().substring(3, 5)))
						.collect(Collectors.toList())).toString();
			}
			

		} else {
			SafeCardKind = sessionManager.getGlobalValue("SafeCardKind", String.class);
			SmartOTP = sessionManager.getGlobalValue("SmartOTP", String.class);
			CustName = sessionManager.getGlobalValue("CustName", String.class);
			accountNum = sessionManager.getGlobalValue("otpAccountNum", String.class);
		}
		
		AthMgtGetOtpResponse response = new AthMgtGetOtpResponse();
		response.setCustName(CustName);
		response.setSafeCardKind(SafeCardKind);
		response.setSmartOTP(SmartOTP);
		response.setAccountNum(accountNum);
		response.setAccountListJsonStr(accountListJsonStr);
		
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
    	
        return response;
    }
    
    @ServiceEndpoint(url = "/authorizeOtp", name = "OTP 이용등록/해제")
	public AthMgtAuthorizeOtpResponse authorizeOtp(AthMgtAuthorizeOtpRequest request) {
		// MA3CRTMNG003_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		String inClassCode = request.getInClassCode();
		String yiGubun = request.getYiGUBUN();

		// 전문전송 처리
		OltpRequestOptions h818Options = this.hostClient.getOltpRequestOptions("CB_TBS03_"+inClassCode); // H818조회, D81H등록, D818해지
		h818Options.setImsTranCd("TI1TBS03");
		h818Options.setInClassCd(inClassCode);
		h818Options.setSvcCd("818");

		// 개별부 세팅
		CbTbs03H81800Req h818Req = new CbTbs03H81800Req();
		log.debug("{}", sessionManager.isLogin());
		h818Req.setUserID(sessionManager.isLogin() ? sessionManager.getLoginValue("UserID", String.class) : sessionManager.getGlobalValue("UserID", String.class));
		h818Req.setTSPassword("99999999");
		h818Req.setYIGUBUN(yiGubun);

		// OTP이용 해지(D81H), 이용등록(D818)
		if ("D81H".equals(inClassCode) || "D818".equals(inClassCode)) {
			h818Req.setYIVDCD(request.getYiVDCD());
			h818Req.setYIOTPNO(request.getYiOTPNO());
			h818Req.setCgAcctNum(sessionManager.isLogin() ? request.getCgAcctNum() : sessionManager.getGlobalValue("otpAccountNum", String.class));
//			h818Req.setCgAcctPassword(sessionManager.isLogin() ? request.getBbKeypadNum() : sessionManager.getGlobalValue("otpBbKeypadNum", String.class));
			h818Req.setCgAcctPassword(sessionManager.getGlobalValue("otpBbKeypadNum", String.class)); // 로그인도 계좌검증 하고 여기 왔음
		}

		CbTbs03H81800Res h818Res = this.hostClient.sendOltp(h818Options, h818Req, CbTbs03H81800Res.class).getResponse();
		
		// 등록,해지 거래 성공 후 세션 클리어
		if ("D81H".equals(inClassCode) || "D818".equals(inClassCode)) {
			sessionManager.removeGlobalValue("otpAccountNum");
			sessionManager.removeGlobalValue("otpBbKeypadNum");
		}
		
		AthMgtAuthorizeOtpResponse response = new AthMgtAuthorizeOtpResponse();
		response.setOTPREGCODE(CodeUtils.getCodeValue("OTPREGCODE", h818Res.getYOIRCD()));
		response.setOTPVDCODE(CodeUtils.getCodeValue("OTPVDCODE", h818Res.getYOVDCD()));
		response.setYOVDCD(h818Res.getYOVDCD());
		response.setYOIRCD(h818Res.getYOIRCD());
		response.setYOOTPNO(h818Res.getYOOTPNO());
		
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

    /************************************************************************************************************************************************
     * OTP,보안카드 오류 해제
     * validatePreOtpErrorClear : OTP보안카드 오류해제 페이지 진입시 체크 (새로추가함)
     * getSaupNumOdcloud : 휴폐업사실조회 (새로추가함)
     * authorizeOtpErrorClear : OTP보안카드 오류해제
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/validatePreOtpErrorClear", name = "OTP보안카드 오류해제 페이지 진입시 체크")
	public AthMgtValidatePreOtpErrorClearResponse validatePreOtpErrorClear(AthMgtValidatePreOtpErrorClearRequest request) {
		// MA3CRTOEL001_101S
		log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

		AthMgtValidatePreOtpErrorClearResponse response = new AthMgtValidatePreOtpErrorClearResponse();

		String SafeCardState = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("SafeCardState", String.class), ""); // 안전카드상태 (0:미사용, 1:사용, 2:분실등록, 3:오류횟수초과, 4:일련번호 오류횟수 초과)
		String SafeCardKind = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("SafeCardKind", String.class), ""); // 보안매체종류 (0:미발급, 1:안전카드, 2:구OTP, 3:OTP(SmartOTP > 0:스마트OTP, M:모바일OTP))
		String SmartOTP = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("SmartOTP", String.class), "");
		String result = "";

		// 보안매체 상태
		if ("2".equals(SafeCardState) || "3".equals(SafeCardState) || "4".equals(SafeCardState) || "0".equals(SafeCardState)) {
			if ("2".equals(SafeCardState)) { // 분실등록
				if ("1".equals(SafeCardKind)) {
					result = codeManager.getCodeItem("APP_TXT", "CARD_001");
				} else {
					result = codeManager.getCodeItem("APP_TXT", "OTP_001");
				}
			} else if ("3".equals(SafeCardState)) { // 오류횟수초과
				if ("M".equals(SmartOTP)) { // MOTP일 경우
					result = codeManager.getCodeItem("APP_TXT", "OTP_004");
				} else {
					result = codeManager.getCodeItem("APP_TXT", "OTP_002");
				}
			} else if ("4".equals(SafeCardState)) { // 일련번호 오류횟수 초과
				if ("1".equals(SafeCardKind)) {
					result = codeManager.getCodeItem("APP_TXT", "CARD_003");
				} else {
					result = codeManager.getCodeItem("APP_TXT", "OTP_003");
				}
			} else if ("0".equals(SafeCardState)) { // 보안매체 미등록
				result = codeManager.getCodeItem("APP_TXT", "BALNK_OTP_CARD");
			}
		} else {
			// 정상, 오류건수 미달
			result = codeManager.getCodeItem("APP_TXT", "OEL_001");
		}

		/* 로그인 세션시 CI조회 */
		if (sessionManager.isLogin()) {
			String perBusNo = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("PerBusNo", String.class), ""));

			if (!"".equals((perBusNo))) {
				sessionManager.setGlobalValue("PerBusNo", perBusNo);
			}

			VerificationGetCustomerCiRequest verificationGetCustomerCiRequest = new VerificationGetCustomerCiRequest();
			verificationGetCustomerCiRequest.setAicrgb("3"); // 처리구분(1:CI번호, 2:CIF번호, 3:주민번호, 4:계좌번호)
			verificationGetCustomerCiRequest.setAigubun("1"); // 등록구분(1:조회, 2:등록, 3:해지)
			verificationGetCustomerCiRequest.setAismno(perBusNo);

			VerificationGetCustomerCiResponse verificationGetCustomerCiResponse = verificationComponent.getCustomerCi(verificationGetCustomerCiRequest);

			String ciResult = "";
			String isCIERR = "";
			String statusYN = "";
			if (verificationGetCustomerCiResponse != null) {
				ciResult = verificationGetCustomerCiResponse.getResult(); // CI 값 보유여부
				isCIERR = verificationGetCustomerCiResponse.getIsCIERR(); // 실명번호 불일치 여부
				statusYN = verificationGetCustomerCiResponse.getStatusYN(); // 사활구분
				// CI값 정상여부 체크
				if ("N".equals(statusYN) || "N".equals(ciResult) || "Y".equals(isCIERR)) {
					response.setIsCI("N");
				}
			}
		}

		sessionManager.removeGlobalValue("TRAD_NO");

		sessionManager.setGlobalValue("PRDCT_ID", "9990");
		sessionManager.setGlobalValue("PRDCT_NM", "OTP/보안카드 오류 해제");
		sessionManager.setGlobalValue("PRDCT_CD", "9990");
		sessionManager.setGlobalValue("BIZ_TYPE", "MOLR");

		response.setUserCiInfo(sessionManager.getGlobalValue("USER_CI_INFO", String.class));
		response.setSafeCardState(SafeCardState);
		response.setSafeCardKind(SafeCardKind);
		response.setSafeCardStateNm(result);
		response.setSmartOTP(SmartOTP);

		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
		log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
		log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

		return response;
	}

    @ServiceEndpoint(url = "/getSaupNumOdcloud", name = "휴폐업사실조회")
    public AthMgtGetSaupNumOdcloudResponse getSaupNumOdcloud(AthMgtGetSaupNumOdcloudRequest request) {
        // MA3CMMBIZ026_201S
        AthMgtGetSaupNumOdcloudResponse response = new AthMgtGetSaupNumOdcloudResponse();

        String saupNumListString = StringUtils.defaultIfEmpty(request.getSaupNumList(), "");

        List<HashMap<String, Object>> resultRecordList = new ArrayList<HashMap<String, Object>>();

        if (!saupNumListString.equals("")) {
            try {
            	ObjectMapper mapper = new ObjectMapper();
                resultRecordList = mapper.readValue(URLDecoder.decode(saupNumListString), new TypeReference<List<HashMap<String, Object>>>() {});
                log.debug("resultRecordList size={}", resultRecordList.size());
            } catch (Exception e) {
            	resultRecordList = null;
            }
        }

        if (resultRecordList != null) {

            StringBuilder saupNumList = new StringBuilder();
            for (HashMap<String, Object> r : resultRecordList) {
                saupNumList.append(r.get("yoSaSaup")).append(",");
            }

            List<String> saupList = new ArrayList<String>();
            String[] params = saupNumList.toString().split(",");

            for (String str : params) {
                saupList.add(str.replaceAll("-", "")); // 숫자로 이루어진 10자리 값만 가능('-'등의 기호 반드시 제거 후 호출)
            }

            JSONObject requestparam = new JSONObject();
            requestparam.put("b_no", saupList); // 최종 API bodyParam
            log.debug("requestparam :::: {}", requestparam.toString());

            String APIKey = PropertiesUtils.getString("ODCLOUD_OPENAPI_KEY"); // APIkey
            String APIUrl = PropertiesUtils.getString("ODCLOUD_OPENAPI_URL"); // APIUrl
            String APIType = PropertiesUtils.getString("ODCLOUD_OPENAPI_TAXSTATUS"); // API종류 (사업자상태조회)

            HttpURLConnection conn = null;
            String responseData = "";
            BufferedReader br = null;
            StringBuffer sb = null;
            String returnData = "";
            OutputStream os = null;

            try {
                APIUrl = APIUrl + APIType + APIKey;

                // API 요청 URL
                URL url = new URL(APIUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                os = conn.getOutputStream();
                os.write(requestparam.toString().getBytes("UTF-8"));
                os.flush();

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                sb = new StringBuffer();

                while ((responseData = br.readLine()) != null) {
                    sb.append(responseData);
                }

                returnData = sb.toString();
                response.setList(returnData.toString());

                log.debug("################ API 응답데이터 ::::::::::::::{}", returnData);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (Exception ignoredException) {
                    br = null;
                }
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (Exception ignoredException) {
                    os = null;
                }
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception ignoredException) {
                    conn = null;
                }
            }
        }

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }
    
    @ServiceEndpoint(url = "/verfiyOdcloudSign", name = "공공데이터포털-전자서명검증")
    public AthMgtVerfiyOdcloudSignResponse verfiyOdcloudSign(AthMgtVerfiyOdcloudSignRequest input) {
		// 환경설정 경로 지정 필요할경우
		WizveraConfig delfinoConfig = null;
		try {
			delfinoConfig = new WizveraConfig(configPath);
		} catch (IOException e) {
			log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
		}

		log.debug("@@@@validateGovmDataCert Start LOG" + input.toString());

		// TODO. 테스트 필요
		AthMgtVerfiyOdcloudSignResponse output = new AthMgtVerfiyOdcloudSignResponse();
		String CommonCertSignYN = StringUtils.defaultIfEmpty(input.getCommonCertSignYn(), "");
		String devlopeCertVerifyFlag = StringUtils.defaultIfEmpty(input.getDevlopeCertVerifyFlag(), "N");
		String checkFinFlag = StringUtils.defaultIfEmpty(input.getCheckFinFlag(), "");
		// 개발서버일때 인증서 검증을 무조건 하도록 조치
		sessionManager.setGlobalValue("devlopeCertVerifyFlag", devlopeCertVerifyFlag);
		String PKCS7SignedData = input.getPkcs7SignedData();
		String vidRandom = StringUtils.defaultIfEmpty(input.getVidRandom(), "");

		// [SC제일 신용대출] 사설인증서 관련 추가
		String FinTechSignYN = StringUtils.defaultIfEmpty(input.getFinTechSignYN(), ""); // 사설인증서 전자서명여부

		log.debug("@@@@CommonCertSignYN ::" + CommonCertSignYN);
		log.debug("@@@@devlopeCertVerifyFlag ::" + devlopeCertVerifyFlag);
		log.debug("@@@@FinTechSignYN ::" + FinTechSignYN);
		log.debug("@@@@PKCS7SignedData ::" + PKCS7SignedData);

		if ("Y".equals(CommonCertSignYN)) {
			// CommonCertSignYN : "Y", PKCS7SignedData : 있음 > 공동인증 검증
//			PKCS7SignedData = java.net.URLDecoder.decode(input.getPkcs7SignedData());
//			log.debug("@@@@@ signData 2[" + PKCS7SignedData + "]");

			try {
				// VID검사
				certVerifyComponent.selfVerify(PKCS7SignedData, vidRandom);
				
				// 인증서상태검증
				SignVerifier signer = new SignVerifier(delfinoConfig);
				SignVerifierResult signVerifierResult = signer.verifyPKCS7(PKCS7SignedData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
				X509Certificate userCert = signVerifierResult.getSignerCertificate();
				certUtils.getUserInfoByScbDBEdoc(userCert, "");
				
			} catch (Exception e) {
				throw new PRCServiceException("PRCCRT9001", "제출하신 공인인증서의 검증에 실패 하였습니다. 인증서를 다시한번 확인해 주시기 바랍니다.");
			}

		} else if (!"Y".equals(CommonCertSignYN) && !"".equals(PKCS7SignedData) && null != PKCS7SignedData && !"Y".equals(FinTechSignYN)) {
			if ("FC".equals(checkFinFlag)) {
				// 22.09.26 금융인증서 확인을 위해 flag 값 추가 (FINCERT - 금융인증서 스크래핑용)
				finCertVerifyComponent.finVerify("FC", PKCS7SignedData);
			} else {
				// CommonCertSignYN : 없음, PKCS7SignedData : 있음 > 금융인증 검증
				// 금융인증서 타행인증서 인증 내용 확인을 위해 MD추가 - 다른 곳에서 사용 X. 처음 추가되는 소스 20220520
				finCertVerifyComponent.verify("MD", PKCS7SignedData);
			}
			
		} else if ("Y".equals(FinTechSignYN)) {
			finTechCertVerifyComponent.authVerify(PKCS7SignedData);
			
		} else { // 간편인증
		// CommonCertSignYN : 없음, PKCS7SignedData : 없음 > 간편인증 (검증X)
		}

		return output;
	}

    @ServiceEndpoint(url = "/authorizeOtpErrorClear", name = "OTP보안카드 오류해제")
    public AthMgtAuthorizeOtpErrorClearResponse authorizeOtpErrorClear(AthMgtAuthorizeOtpErrorClearRequest request) {
        // MA3CRTOEL001_301S

        OltpRequestOptions d842Options = this.hostClient.getOltpRequestOptions("CB_TBS03_D842");
        d842Options.setImsTranCd("TI1TBS03");
        d842Options.setInClassCd("D842");
        d842Options.setSvcCd("842");
        // 개별부 세팅
        CbTbs03D84200Req d842Req = new CbTbs03D84200Req();
        d842Req.setUserID(SessionUtils.getSessionValue("UserID"));
        // 전문발송
        this.hostClient.sendOltp(d842Options, d842Req, CbTbs03D84200Res.class);

        /*
         * =====================================================SMS 전문 전송======================================================
         */
        try {
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setMember("0"); // Client측 key일련번호
            smsRequest.setUserCode("ebanking"); // 사용자 발신 코드
            smsRequest.setUserName("I5"); // 사용자명
            smsRequest.setCallPhone1(sessionManager.getLoginValue("HPOne", String.class)); // 호출 번호 #1
            smsRequest.setCallPhone2(sessionManager.getLoginValue("HPTwo", String.class)); // 호출 번호 #2
            smsRequest.setCallPhone3(sessionManager.getLoginValue("HPThree", String.class)); // 호출 번호 #3
            smsRequest.setCallMessage("[SC제일은행]보안매체 오류건수 해제 완료(본인 아닐 시 문의요망)"); // 호출 메시지
            smsRequest.setRateDate(DateUtils.getCurrentDate("yyyyMMdd")); // 메시지 전송 예약일자
            smsRequest.setRateTime(DateUtils.getCurrentDate("HHmmss")); // 메시지 전송 예약시간
            smsRequest.setReqPhone1(""); // 회신 번호#1
            smsRequest.setReqPhone2("1588"); // 회신 번호#2
            smsRequest.setReqPhone3("1599"); // 회신 번호#3
            smsRequest.setCallName("SC제일은행"); // 발신자명
            smsRequest.setDeptCode("GL9-KI3-HS9"); // 회사 코드
            smsRequest.setDeptName("디지털뱅킹부"); // 회사명

            String result = smsComponent.sendMain(smsRequest);
            log.debug("SMS발송결과={}", result);

        } catch (Exception e) {
            log.error("SMS발송 오류", e);
        }
        /*
         * =====================================================SMS 전문 전송======================================================
         */

        AthMgtAuthorizeOtpErrorClearResponse response = new AthMgtAuthorizeOtpErrorClearResponse();
        return response;
    }

    /************************************************************************************************************************************************
     * 모바일OTP 발급/재발급
     * validateMotpIssueUser : 모바일OTP 발급 고객확인 (2026-02-26 추가)
     * validateMotpIssuePreTran : 모바일OTP 발급 예비거래
     * authorizeMotpIssue : 모바일OTP 발급 본거래
     * listMotpIssueAcct : 모바일OTP 발급 출금계좌목록 조회
     * getMotpIssueCdd : 모바일OTP 발급 CDD 확인 (2026-05-07 추가)
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/validateMotpIssueUser", name = "모바일OTP 발급 고객확인")
    public AthMgtValidateMotpIssueUserResponse validateMotpIssueUser(AthMgtValidateMotpIssueUserRequest request) {
    	log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());
    	
    	AthMgtValidateMotpIssueUserResponse response = new AthMgtValidateMotpIssueUserResponse();
    	
    	String scrrenFalg = StringUtils.nvl(request.getScreenFlag(), "");
    	if("".equals(scrrenFalg)) {
    		sessionManager.removeGlobalValue("TRAD_NO"); //거래번호 초기화.
    		if("".equals(StringUtils.nvl(SessionUtils.getSessionValue("CNNCTN_WAY"), ""))){
    			sessionManager.setGlobalValue("CNNCTN_WAY", StringUtils.nvl(request.getCnnctnWay(), ""));
    		}
    		if("".equals(StringUtils.nvl(SessionUtils.getSessionValue("CLERK_NO"), ""))){
    			sessionManager.setGlobalValue("CLERK_NO", StringUtils.nvl(request.getClerkNo(), ""));
    		}
    		
    		response.setClerkNo(request.getClerkNo());
    		response.setCnnctnWay(response.getCnnctnWay());
    		
    	} 
//    	else if("CO".equals(scrrenFalg)) {
    		
    		// TODO 이어하기 작성필요
//    	}
    	
    	
        return response;
    }

    @ServiceEndpoint(url = "/validateMotpIssuePreTran", name = "모바일OTP 발급 예비거래")
    public AthMgtValidateMotpIssuePreTranResponse validateMotpIssuePreTran(AthMgtValidateMotpIssuePreTranRequest request) {
        // MA3CRTMOM004_101S TranTypeCd=PRE, MA3CMMBIZ001_117S
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        // MOTP발급 예비거래 전문처리 - 주민등록번호를 전달하여 사용자 보안카드 타입을 가져온다.
        OltpRequestOptions cbTbs03H904Options = this.hostClient.getOltpRequestOptions("CB_TBS03_H904");
        cbTbs03H904Options.setImsTranCd("TI1TBS03");
        cbTbs03H904Options.setInClassCd("H904"); // 본거래는 D904임
        cbTbs03H904Options.setSvcCd("904");
        cbTbs03H904Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);

        // 개별부 세팅
        CbTbs03H90400Req cbTbs03H90400Req = new CbTbs03H90400Req();
        cbTbs03H90400Req.setPerBusNo(SessionUtils.getSessionValue("PerBusNo", String.class));
        CbTbs03H90400Res cbTbs03H90400Res = this.hostClient.sendOltp(cbTbs03H904Options, cbTbs03H90400Req, CbTbs03H90400Res.class).getResponse();

        // 고객정보조회하여 직업 찾음
        OltpRequestOptions cbIbk01H920Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");
        cbIbk01H920Options.setImsTranCd("TI1IBK01");
        cbIbk01H920Options.setInClassCd("H920");
        cbIbk01H920Options.setSvcCd("920");
        cbIbk01H920Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);

        // 개별부 세팅
        CbIbk01H92000Req cbIbk01H92000Req = new CbIbk01H92000Req();
        cbIbk01H92000Req.setUserID(CommonBizConstants.DEFAULT_USER_ID);
        cbIbk01H92000Req.setTSPassword(CommonBizConstants.DEFAULT_TS_PASS_WORD);
        cbIbk01H92000Req.setInJuMinNo(SessionUtils.getSessionValue("PerBusNo", String.class));
        cbIbk01H92000Req.setYISALES("Y");
        cbIbk01H92000Req.setBKGuBun("2");
        cbIbk01H92000Req.setYIEADRGB("");
        CbIbk01H92000Res cbIbk01H920Res = this.hostClient.sendOltp(cbIbk01H920Options, cbIbk01H92000Req, CbIbk01H92000Res.class).getResponse();

        // 응답 생성
        AthMgtValidateMotpIssuePreTranResponse response = new AthMgtValidateMotpIssuePreTranResponse();
        response.setYoICHDAYHD(cbTbs03H90400Res.getYOICHDAYHD());
        response.setYoICHONCEHD(cbTbs03H90400Res.getYOICHONCEHD());
        response.setYoSCGB(cbTbs03H90400Res.getYOSCGB());
        response.setYoSCNO(cbTbs03H90400Res.getYOSCNO());
        response.setUserId2(cbTbs03H90400Res.getUserID2());
        response.setJob(cbIbk01H920Res.getJob());
        response.setTradNo(sessionManager.getGlobalValue("TRAD_NO", String.class)); // 값있는지없는지모름
        response.setCustNo(sessionManager.getGlobalValue("CUST_NO", String.class)); // 값있는지없는지모름

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

        return response;
    }

    @ServiceEndpoint(url = "/authorizeMotpIssue", name = "모바일OTP 발급 본거래")
    public AthMgtAuthorizeMotpIssueResponse authorizeMotpIssue(AthMgtAuthorizeMotpIssueRequest request) {
        // MA3CRTMOM004_101S TranTypeCd=REAL
        log.debug("🌷{}🌷{}", new Object() {
        }.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        // MOTP발급 본거래 전문처리
        OltpRequestOptions d904Options = this.hostClient.getOltpRequestOptions("CB_TBS03_D904");
        d904Options.setImsTranCd("TI1TBS03");
        d904Options.setInClassCd("D904"); // 예비거래는 H904임
        d904Options.setSvcCd("904");
        d904Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);

        // 개별부 세팅
        CbTbs03D90400Req d904Req = new CbTbs03D90400Req();
        d904Req.setPerBusNo(SessionUtils.getSessionValue("PerBusNo", String.class));
        d904Req.setUserID(StringUtils.nvl(request.getUserId(), ""));
        d904Req.setYIGJNO(StringUtils.nvl(request.getYiGJNO(), "")); // 출금계좌번호
        d904Req.setYISCNO(StringUtils.nvl(request.getYiSCNO(), ""));
        d904Req.setYIICHONCEHD(StringUtils.nvl(request.getYiICHONCEHD(), "")); // 1회이체한도
        d904Req.setYIICHDAYHD(StringUtils.nvl(request.getYiICHDAYHD(), "")); // 1회이체한도

        d904Req.setYIDVINF(StringUtils.nvl(request.getYiDVINF(), "")); // 디바이스 Token
        d904Req.setYIDVGBN(StringUtils.nvl(request.getYiDVGBN(), "")); // 디바이스 Type

        d904Req.setYIPINNO(StringUtils.nvl(request.getPinBb01(), "")); // 모바일OTP PIN비밀번호
        // ti1tbs03H904Req.setYIPINNO(StringUtils.nvl(request.getPinBb02(), "")); //
        // 모바일OTP PIN비밀번호 확인
        d904Req.setYIOTPVDGB(StringUtils.nvl(request.getYiOTPVDGB(), "")); // OTP벤더구분

        CbTbs03D90400Res d904Res = this.hostClient.sendOltp(d904Options, d904Req, CbTbs03D90400Res.class).getResponse();

        String secretYOSEKEY = d904Res.getYOSEKEY();
        String deviceToken = request.getYiDVINF(); // 디바이스 Token
        String clientRnd = request.getClientRnd();
        String pinNumber = request.getPinBb01(); // 모바일OTP PIN비밀번호

        String yosekey = MotpUtils.encryptTokenKey(secretYOSEKEY, deviceToken, clientRnd, pinNumber);

        if ("".equals(yosekey)) {
            throw new PRCServiceException("9999", "모바일OTP 발급 정상적이지 않아 처리중 오류가 발생하였습니다.");
        }

        // 과금테이블 INSERT
        // 본거래전문의 request, response를 재사용함..
        SaoOtpChargeParameter param = new SaoOtpChargeParameter();
        param.setUserId(d904Req.getUserID());
        param.setDeviceId(d904Req.getYIDVINF());
        param.setDeviceType(d904Req.getYIDVGBN());
        param.setTokenId(d904Res.getYOOTPNO()); // 본거래전문의 응답 재사용
        /**
         * FIXME
         * SessionManager.getMA30Agent(1) => PRCSharedUtils.getDeviceUUID() 맞나?
         */
        param.setRegDeviceUuid(PRCSharedUtils.getDeviceUUID());

        int idSearchCnt = saoOtpChargeDao.countSaoOtpChargeByUserId(d904Req.getUserID());
        if (idSearchCnt == 0) {
            param.setIssueType("1");
            saoOtpChargeDao.insertSaoOtpCharge(param);

        } else {
            // 조회결과가 있음
            int deviceSearchCnt = saoOtpChargeDao.countSaoOtpChargeByDeviceId(d904Req.getYIDVINF(), d904Req.getUserID());
            param.setIssueType(deviceSearchCnt == 0 ? "2" : "9");
            saoOtpChargeDao.updateSaoOtpCharge(param);

        }

        AthMgtAuthorizeMotpIssueResponse response = new AthMgtAuthorizeMotpIssueResponse();
        response.setYoSEKEY(yosekey);
        response.setYoOTPNO(d904Res.getYOOTPNO());

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

        return response;
    }

    @ServiceEndpoint(url = "/listMotpIssueAcct", name = "모바일OTP 발급 출금계좌목록 조회")
    public AthMgtListMotpIssueAcctResponse listMotpIssueAcct(AthMgtListMotpIssueAcctRequest request) {
        // MA3CRTMOM004_501S
        // 간편로그인가입도 같이 사용함
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        String userId = request.getUserId();
        if (userId == null || "".equals(userId)) {
            userId = SessionUtils.getSessionValue("UserID", String.class);
        }
        if (userId == null || "".equals(userId)) {
            userId = sessionManager.getGlobalValue("uid", String.class);
        }

        // 보유계좌조회 시작
        OltpRequestOptions h866Options = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");
        h866Options.setImsTranCd("TI1IBK01");
        h866Options.setInClassCd("H866");
        h866Options.setSvcCd("866");
        h866Options.setVanTp(IntegrationConstant.SELF_VAN_TYPE);
        // 개별부 세팅
        CbIbk01H86600Req h866Req = new CbIbk01H86600Req();
        h866Req.setUserID(userId);
        h866Req.setYIJMNO(StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo", String.class), sessionManager.getGlobalValue("cid", String.class)));
        h866Req.setTSPassword(SessionUtils.getSessionValue("TSPassword", String.class));
        // 전문발송
        CbIbk01H86600Res h866Res = this.hostClient.sendOltp(h866Options, h866Req, CbIbk01H86600Res.class).getResponse();

        // 응답 변환
        AthMgtListMotpIssueAcctResponse response = new AthMgtListMotpIssueAcctResponse();

    	response.setUserId(h866Res.getYOIBID());

        List<CbIbk01H86600Res.YOMYINF_REC> yomyinfRec = h866Res.getYOMYINF_REC();
        if (yomyinfRec != null) {
            response.setAccountList(yomyinfRec.stream().map(o -> {
                AthMgtListMotpIssueAcctResponse.myinfRec record = new AthMgtListMotpIssueAcctResponse.myinfRec();
                record.setMygj(o.getYOMYGJ());
                record.setMygjNm(PRCSharedUtils.getAccountName(o.getYOMYGJ().substring(3, 5), o.getYOZONG()));
                record.setMygjFmt(FormatUtils.getFrmAcct(o.getYOMYGJ()));
                return record;
            }).collect(Collectors.toList()));
        }

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");

        return response;
    }
    
    @ServiceEndpoint(url = "/getMotpIssueCdd", name = "모바일OTP 발급 CDD 확인")
    public AthMgtGetMotpIssueCddResponse getMotpIssueCdd(AthMgtGetMotpIssueCddRequest request) {
    	
    	AthMgtGetMotpIssueCddResponse response = new AthMgtGetMotpIssueCddResponse();
    	response.setKcddPollingYn(PropertiesUtils.getString("KCDD_POLLING_YN", "N")); //20230803.SK2.[CDD자동화]KCDD자동화여부 Y일 때 실행 하도록 처리
    	response.setYocddilFlag(sessionManager.getGlobalValue("YOCDDIL_FLAG", String.class)); // CDD유효기간도래 체크 (Y. 대상)
    	
    	// CDD결과확인
    	if("NF_TRADINFO_MGT_S_06".equals(request.getPrcType())) {
        	NonFaceAuthInfoParameter parameter = new NonFaceAuthInfoParameter();
        	parameter.setCustNo(SessionUtils.getSessionValue("CUST_NO"));
        	parameter.setTradNo(SessionUtils.getSessionValue("TRAD_NO"));
        	
        	NonFaceAuthInfoResult result = nfTradInfoMgtDao.selectNonFaceAuthInfo(parameter);
        	if(result != null) {
        		response.setIdCardCd(result.getIdCardCd());
            	response.setAuthntIndCd(result.getAuthntIndCd());
            	response.setCddReqCd(result.getCddReqCd());
        	}
    	}
    	
    	log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        
    	return response;
    }

    /************************************************************************************************************************************************
     * 모바일OTP PIN번호 확인
     * getMotpPinNum : 모바일OTP PIN번호 확인
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/getMotpPinNum", name = "모바일OTP PIN번호 확인")
    public AthMgtGetMotpPinNumResponse getMotpPinNum(AthMgtGetMotpPinNumRequest request) {
        // MA3CRTMOA003_101S
        log.debug("🌷{}🌷{}", new Object() {}.getClass().getEnclosingMethod().getAnnotation(ServiceEndpoint.class).name(), request.toString());

        // 전문전송 처리
        OltpRequestOptions h903Options = this.hostClient.getOltpRequestOptions("CB_TBS03_H903");
        h903Options.setImsTranCd("TI1TBS03");
        h903Options.setInClassCd("H903");
        h903Options.setSvcCd("903");

        // 개별부 세팅
        CbTbs03H90300Req h903Req = new CbTbs03H90300Req();
        h903Req.setYIUSERID(sessionManager.getLoginValue("UserID", String.class));
        h903Req.setYIDVINF(request.getYiDVINF()); // 모바일OTP인증 Device Id
        h903Req.setYIDVGBN(request.getYiDVGBN()); // 모바일OTP인증 Device Cd
        h903Req.setYIOTPNO(request.getYiOTPNO()); // 모바일OTP 일련번호
        h903Req.setYIPINNO(request.getPinBb01()); // 모바일OTP PIN비밀번호
        h903Req.setYIOTPVDGB("990"); // OTP벤더구분

        CbTbs03H90300Res h903Res = this.hostClient.sendOltp(h903Options, h903Req, CbTbs03H90300Res.class).getResponse();

        String secretKeyb = h903Res.getYOSEKEY();
        String rndChallenge = h903Res.getYOMRAND();
        String pinNumber = request.getPinBb01();
        String clientRnd = request.getClientRnd();
        String deviceInfo = request.getYiDVINF();

        log.debug("secretKeyb={}, rndChallenge={}, pinNumber={}, clientRnd={}, deviceInfo={}", secretKeyb, rndChallenge, pinNumber, clientRnd, deviceInfo);

        AthMgtGetMotpPinNumResponse response = new AthMgtGetMotpPinNumResponse();
        response.setYoSEKEY(MotpUtils.encryptTokenKey(secretKeyb, deviceInfo, clientRnd, pinNumber));
        response.setYoMRAND(MotpUtils.encryptServerChallenge(rndChallenge, deviceInfo, clientRnd, pinNumber));

        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        log.debug("🌷{}={}🌷", response.getClass().getSimpleName(), response);
        log.debug("🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷🌷");
        return response;
    }

    /************************************************************************************************************************************************
     * OTP 보정
     * requestOtpCorrection : OTP 보정
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/requestOtpCorrection", name = "OTP보정")
    public AthMgtRequestOtpCorrectionResponse requestOtpCorrection(AthMgtRequestOtpCorrectionRequest request) {
        // MA3CRTOTP001_101S
        OltpRequestOptions d985Options = this.hostClient.getOltpRequestOptions("CB_TBS03_D985");
        d985Options.setImsTranCd("TI1TBS03");
        d985Options.setInClassCd("D985");
        d985Options.setSvcCd("985");
        // 개별부 세팅
        CbTbs03D98500Req d985Req = new CbTbs03D98500Req();
        d985Req.setPSSCD(request.getPssCd());

        this.hostClient.sendOltp(d985Options, d985Req, CbTbs03D98500Res.class);

        return new AthMgtRequestOtpCorrectionResponse();
    }
    
    /************************************************************************************************************************************************
     * 개인정보 노출자 조회
     * custInfoLeakerTranBlock : 개인정보 노출자 조회
     ************************************************************************************************************************************************/
    @ServiceEndpoint(url = "/custInfoLeakerTranBlock", name = "개인정보 노출자 조회")
    public AthMgtCustInfoLeakerTranBlockResponse custInfoLeakerTranBlock(AthMgtCustInfoLeakerTranBlockRequest request) {
    	// MA3LGNCIL001_101S
    	AthMgtCustInfoLeakerTranBlockResponse response = new AthMgtCustInfoLeakerTranBlockResponse();
    	
    	String perBusNo = SessionUtils.getSessionValue("PerBusNo");
    	String sessionYOLRSOTGB = SessionUtils.getSessionValue("YOLRSOTGB");
    	String yoLRSOTGB = "";
    	boolean isLoginFlg = sessionManager.isLogin();
    	
    	if(StringUtils.isNotEmpty(sessionYOLRSOTGB)) {
    		yoLRSOTGB = sessionYOLRSOTGB;
    		response.setYoLRSOTGB(yoLRSOTGB);
    		
		}else {
    		try {
    			OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H504");
    			CbIbk01H504Req h504Req = new CbIbk01H504Req();

    			hostCfg.setImsTranCd("TI1IBK01");
    			hostCfg.setInClassCd("H504");
                hostCfg.setSvcCd("504");
                h504Req.setE2ERegNum(perBusNo);	//주민번호

                if(isLoginFlg) {
                	String ssrBrNo = sessionManager.getLoginValue("SSRBrNo", String.class);
    				String ssrKmCd = sessionManager.getLoginValue("SSRKmCD", String.class);
    				String ssrGeNo = sessionManager.getLoginValue("SSRGeNo", String.class);
    				String userAcctNum = ssrBrNo + ssrKmCd + ssrGeNo;

    				h504Req.setYILGGB("Y");															//로그인여부
    				h504Req.setYIUSID(sessionManager.getLoginValue("UserID", String.class));			//이용자번호
    				h504Req.setYIPASS(sessionManager.getLoginValue("TSPassword", String.class));		//이용자비밀번호
    				h504Req.setYIGJNO(userAcctNum);													//계좌번호
                } else {
                	h504Req.setYILGGB("N");	//로그인여부
    				h504Req.setYIUSID("N");	//이용자번호
    				h504Req.setYIPASS("N");	//이용자비밀번호
    				h504Req.setYIGJNO("");		//계좌번호
                }

                OltpResponse<CbIbk01H504Res> hostResponse = this.hostClient.sendOltp(hostCfg, h504Req, CbIbk01H504Res.class);
                CbIbk01H504Res h504Res = hostResponse.getResponse();

                yoLRSOTGB = com.scbank.process.api.fw.core.utils.StringUtils.nvl(h504Res.getYOLRSOTGB(), "") ;
                log.debug("getYoLRSOTGB YOLRSOTGB :: {}", yoLRSOTGB);
                log.debug("getYoLRSOTGB isLogined :: {}", isLoginFlg);
    		} catch (PRCServiceException e) {
    			log.debug("getYoLRSOTGB EXCEPTION START");
    			e.printStackTrace();
    			log.debug("getYoLRSOTGB EXCEPTION END");
    		}
    		response.setYoLRSOTGB(yoLRSOTGB);

    		sessionManager.setGlobalValue("YOLRSOTGB", yoLRSOTGB);
    		if(isLoginFlg) {
    			sessionManager.setLoginValue("YOLRSOTGB", yoLRSOTGB);
    		}
    	}
    	
    	return response;
    }
}