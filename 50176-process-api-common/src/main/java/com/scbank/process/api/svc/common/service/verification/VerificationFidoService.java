package com.scbank.process.api.svc.common.service.verification;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.initech.core.util.URLDecoder;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.FidoCertificateInfoDao;
import com.scbank.process.api.svc.common.dao.Ma3CertUserMgtDao;
import com.scbank.process.api.svc.common.dao.OpDeviceInfoDao;
import com.scbank.process.api.svc.common.dao.OpMyselfConfrHistDao;
import com.scbank.process.api.svc.common.dao.PinUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupCertListDataParameter;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.FidoBackupInfoResult;
import com.scbank.process.api.svc.common.dao.dto.FidoLogInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtByDeviceResult;
import com.scbank.process.api.svc.common.dao.dto.PinUsrMgtUpdateParameter;
import com.scbank.process.api.svc.common.dao.dto.SimpleFinCertUserInfoResult;
import com.scbank.process.api.svc.common.service.verification.dto.fido.PinUserInfoVo;
import com.scbank.process.api.svc.common.service.verification.dto.fido.VrfFidInquirySimpleFinCertUserInfoRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fido.VrfFidInquirySimpleFinCertUserInfoResponse;
import com.scbank.process.api.svc.common.service.verification.dto.fido.VrfFidRequestFidoServiceRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fido.VrfFidRequestFidoServiceResponse;
import com.scbank.process.api.svc.shared.components.cert.CertUtils;
import com.scbank.process.api.svc.shared.components.cert.DigitalCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.dao.LdapInfoDao;
import com.scbank.process.api.svc.shared.components.cert.dao.dto.LdapInfoResult;
import com.scbank.process.api.svc.shared.components.fido.FidoHttpComponent;
import com.scbank.process.api.svc.shared.components.ipinside.IpinsideComponent;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.utils.CommonBizUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.wizvera.WizveraConfig;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-인증-생체인증(FIDO)", url = "/verification/fido")
public class VerificationFidoService {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * FIDO 공통 통신 컴포넌트
     */
    private final FidoHttpComponent fidoHttpComponent;

    /**
     * Ipinside 모듈
     */
    private final IpinsideComponent ipinsideComponent;

    /**
     * 디지털인증서 검증 컴포넌트
     */
    private final DigitalCertVerifyComponent digitalCertVerifyComponent;

    /**
     * 인증서 공통 기능 컴포넌트
     */
    private final CertUtils certUtils;

    /**
     * 간편로그인 사용자 DAO
     */
    private final PinUsrMgtDao pinUsrMgtDao;

    /**
     * Fido 사용자 백업 DAO
     */
    private final OpMyselfConfrHistDao opMyselfConfrHistDao;

    /**
     * Fido 로그 DAO
     */
    private final OpDeviceInfoDao opDeviceInfoDao;

    /**
     * Ldap 인증서 정보 DAO
     */
    private final LdapInfoDao ldapInfoDao;

    /**
     * 금융인증서 간편로그인 사용자 정보 DAO
     */
    private final Ma3CertUserMgtDao ma3CertUserMgtDao;

    /**
     * 디지털인증서 파기처리 DAO
     */
    private final FidoCertificateInfoDao fidoCertificateInfoDao;

    // delfino.properties
    @Value("${delfino.config.path:}")
    private String configPath;

    /**
     * Fido 서비스 요청 service
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/requestFidoService", name = "fido 서비스 요청")
    public VrfFidRequestFidoServiceResponse requestFidoService(IServiceContext ctx,
            VrfFidRequestFidoServiceRequest request) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        // fido 거래번호 생성
        String fidoSvcTrId = fidoHttpComponent.getFidoSvctrId();

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestFidoService - fidoSvcTrId : [{}], request : [{}]",
                    fidoSvcTrId, request.toString());
        }

        // fido 파라미터 객체 생성
        JSONObject fidoParamJson = new JSONObject();
        fidoParamJson.put("svcTrId", fidoSvcTrId);
        fidoParamJson.put("bizReqType", "app");
        fidoParamJson.put("signReqDt", System.currentTimeMillis());

        try {
            switch (StringUtils.defaultString(request.getFidoCommand())) {
                case "allowedAuthnr":
                    response = allowedAuthnr(request, fidoParamJson);
                    break;
                case "checkRegisteredStatus":
                    response = checkRegisteredStatus(request, fidoParamJson);
                    break;
                case "requestP7Sign":
                    response = requestP7Sign(request, fidoParamJson);
                    break;
                case "trResultConfirm":
                    response = trResultConfirm(request, fidoParamJson);
                    break;
                case "expireDigitalCert":
                    response = expireDigitalCert(request);
                    break;
                case "requestServiceAuth":
                    response = requestServiceAuth(request, fidoParamJson);
                    break;
            }
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0047", "FIDO 처리중 오류가 발생하였습니다.", e);
        }

        return response;
    };

    /**
     * FIDO 등록 허용 장치 조회
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse allowedAuthnr(VrfFidRequestFidoServiceRequest request,
            JSONObject fidoParamJson) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        fidoParamJson.put("appId", StringUtils.defaultString(request.getFidoAppId()));
        fidoParamJson.put("deviceId", StringUtils.defaultString(request.getFidoDeviceId()));

        Map<String, Object> fidoResultMap = new HashMap<String, Object>();
        List<String> aaidPin = new ArrayList<String>();
        List<String> aaidFinger = new ArrayList<String>();

        fidoResultMap = fidoHttpComponent.allowedAuthnr(fidoParamJson);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > allowedAuthnr - fidoResultMap : [{}]",
                    fidoResultMap.toString());
        }

        response.setResultCode(StringUtils.defaultString((String) fidoResultMap.get("resultCode")));
        response.setResultMsg(StringUtils.defaultString((String) fidoResultMap.get("resultMsg")));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> aaidAllowMapList = (List<Map<String, Object>>) fidoResultMap
                .get("aaidAllowListMap");

        if (aaidAllowMapList != null && !aaidAllowMapList.isEmpty()) {
            aaidAllowMapList.forEach(map -> {
                String verificationType = String.valueOf(map.get("verificationType"));
                String aaid = (String) map.get("aaid");

                if ("16384".equals(verificationType)) {
                    aaidPin.add(aaid);
                } else if ("2".equals(verificationType)) {
                    aaidFinger.add(aaid);
                }
            });
        }

        response.setAaidPin(aaidPin);
        response.setAaidFinger(aaidFinger);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > allowedAuthnr - response : ", response.toString());
        }

        return response;
    }

    /**
     * FIDO 등록 상태 조회
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse checkRegisteredStatus(VrfFidRequestFidoServiceRequest request,
            JSONObject fidoParamJson) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        PinUserInfoVo pinUserInfo = getPinUserInfo(
                StringUtils.defaultString(request.getDigitalCertUpdatedYn()));

        if (pinUserInfo == null || StringUtils.defaultString(pinUserInfo.getUserBankingId()).isEmpty()) {
            response.setResultCode(StringUtils.EMPTY);
            response.setResultMsg(StringUtils.EMPTY);
            response.setNotAfter(StringUtils.EMPTY);
            response.setRegYn("N");
            return response;
        }

        fidoParamJson.put("appId", StringUtils.defaultString(request.getFidoAppId()));
        fidoParamJson.put("deviceId", StringUtils.defaultString(request.getFidoDeviceId()));
        fidoParamJson.put("verifyType", StringUtils.defaultString(request.getFidoVerifyType()));
        fidoParamJson.put("loginId", StringUtils.defaultString(pinUserInfo.getUserBankingId()));

        Map<String, Object> fidoResultMap = fidoHttpComponent.checkRegisteredStatus(fidoParamJson);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > checkRegisteredStatus - fidoResultMap : [{}]",
                    fidoResultMap.toString());
        }

        response.setResultCode(StringUtils.defaultString((String) fidoResultMap.get("resultCode")));
        response.setResultMsg(StringUtils.defaultString((String) fidoResultMap.get("resultMsg")));
        response.setNotAfter(StringUtils.defaultString((String) fidoResultMap.get("notAfter")));
        response.setRegYn(StringUtils.defaultString((String) fidoResultMap.get("regYn")));

        // 오류로그
        if (!"000".equals(fidoResultMap.getOrDefault("resultCode", ""))
                || "N".equals(fidoResultMap.getOrDefault("regYn", ""))
                || "".equals(fidoResultMap.getOrDefault("notAfter", ""))) {
            if (log.isErrorEnabled()) {
                log.error(
                        "VerificationFidoService > checkRegisteredStatus - fidoParamJson : [{}], fidoResultMap : [{}]",
                        fidoParamJson.toString(), fidoResultMap.toString());
            }
        }

        // pinDeviceId 업데이트
        PinUserInfoVo pinUserUpdateInfo = updateDigitalCertPinDeviceId(
                StringUtils.defaultString(request.getDigitalCertUpdatedYn()), pinUserInfo);

        response.setExecutedDeviceInfoUpdateYn(
                StringUtils.defaultString(pinUserUpdateInfo.getExecutedDeviceInfoUpdateYn()));

        return response;
    }

    /**
     * FIDO 디지털인증서 서명 요청
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse requestP7Sign(VrfFidRequestFidoServiceRequest request,
            JSONObject fidoParamJson) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        PinUserInfoVo pinUserInfo = getPinUserInfo(
                StringUtils.defaultString(request.getDigitalCertUpdatedYn()));

        fidoParamJson.put("appId", StringUtils.defaultString(request.getFidoAppId()));
        fidoParamJson.put("deviceId", StringUtils.defaultString(request.getFidoDeviceId()));
        fidoParamJson.put("verifyType", StringUtils.defaultString(request.getFidoVerifyType()));
        fidoParamJson.put("loginId", StringUtils.defaultString(pinUserInfo.getUserBankingId()));
        fidoParamJson.put("issueType", "03"); // "03" : 지문/안면
        fidoParamJson.put("tranData", StringUtils.defaultString(request.getFidoSignData()));

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestP7Sign - fidoParamJson : [{}]", fidoParamJson.toString());
        }
        Map<String, Object> fidoResultMap = fidoHttpComponent.requestP7Sign(fidoParamJson);
        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestP7Sign - fidoResultMap : [{}]", fidoResultMap.toString());
        }

        response.setResultCode(StringUtils.defaultString((String) fidoResultMap.get("resultCode")));
        response.setResultMsg(StringUtils.defaultString((String) fidoResultMap.get("resultMsg")));
        response.setUserBankingId(StringUtils.defaultString(pinUserInfo.getUserBankingId()));
        response.setTrId(StringUtils.defaultString((String) fidoResultMap.getOrDefault("trId", "")));

        // 결과확인(trResultConfirm)시 요청할 거래아이디 저장
        sessionManager.setGlobalValue("FIDO_SVCTR_ID", fidoParamJson.get("svcTrId"));

        try {
            // 전자서명 검증시 원문
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME,
                    StringUtils.defaultString(URLDecoder.decode(request.getFidoSignData(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    /**
     * FIDO 디지털인증서 거래 결과 확인
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse trResultConfirm(VrfFidRequestFidoServiceRequest request,
            JSONObject fidoParamJson) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        String fidoSvcTrId = StringUtils.defaultString(sessionManager.getGlobalValue("FIDO_SVCTR_ID", String.class));

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > trResultConfirm - svcTrId : [{}]", fidoSvcTrId);
        }

        // 서명시 요청한 거래ID 설정
        fidoParamJson.put("svcTrId", fidoSvcTrId);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > trResultConfirm - fidoParamJson : [{}]", fidoParamJson.toString());
        }

        // FIDO 인증 결과 확인
        Map<String, Object> fidoResultMap = fidoHttpComponent.trResultConfirm(fidoParamJson);

        String fidoUserId = StringUtils.defaultString((String) fidoResultMap.get("fidoLoginId"));
        String fidoVerifyType = StringUtils.defaultString((String) fidoResultMap.get("fidoVerifyType"));
        String signedData = StringUtils.defaultString((String) fidoResultMap.get("signedData"));

        if (log.isDebugEnabled()) {
            log.debug(
                    "VerificationFidoService > trResultConfirm - fidoUserId : [{}], fidoVerifyType : [{}], signedData : [{}]",
                    fidoUserId, fidoVerifyType, signedData);
        }

        response.setResultCode(StringUtils.defaultString((String) fidoResultMap.get("resultCode")));
        response.setResultMsg(StringUtils.defaultString((String) fidoResultMap.get("resultMsg")));
        response.setTrStatus(StringUtils.defaultString((String) fidoResultMap.get("trStatus")));
        response.setTrStatusMsg(StringUtils.defaultString((String) fidoResultMap.get("trStatusMsg")));
        response.setSignedData(signedData);
        response.setSignedDataList(StringUtils.defaultString((String) fidoResultMap.get("signedDataList")));

        if ("Y".equals(PropertiesUtils.getString("IS_DISTCERT_VERIFY"))) {
            try {
                // 디지털인증서 검증
                digitalCertVerifyComponent.verify("L", signedData);
            } catch (PRCServiceException e) {
                if (log.isWarnEnabled()) {
                    log.warn("VerificationFidoService > trResultConfirm - digitalCertVerify : Fail");
                }
                e.printStackTrace();

                if ("INI603".equalsIgnoreCase(e.getErrorCode()) || "INI303".equalsIgnoreCase(e.getErrorCode())) {
                    // 복구가능 사용자 정보 조회
                    checkEnabledRecoveryUser(signedData, fidoUserId);
                } else {
                    throw new PRCServiceException(e.getErrorCode(),
                            PRCSharedUtils.isEnglish() ? "An error has occurred during certificate authentication"
                                    : "인증 처리중 오류가 발생되었습니다.");
                }
            }
        }

        if ("Y".equals(PropertiesUtils.getString("IS_DISTCERT_LOGTRKING"))) {
            saveFidoLog(signedData, fidoUserId, fidoVerifyType);
        }

        if ("000".equals(StringUtils.defaultString(response.getResultCode()))) {
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_DATA_NAME, signedData);// 전자서명데이터.
        } else {
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "9999");// 에러
        }

        return response;
    }

    /**
     * 디지털인증서 파기 처리
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse expireDigitalCert(VrfFidRequestFidoServiceRequest request) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > expireDigitalCert - start");
        }

        try {
            String fidoAppId = StringUtils.defaultString(request.getFidoAppId());
            String fidoDeviceId = StringUtils.defaultString(request.getFidoDeviceId());
            String fidoVerifyType = StringUtils.defaultString(request.getFidoVerifyType());
            String userBankingId = StringUtils.defaultString(request.getUserBankingId());

            Map<String, Object> fidoRegisterStatusMap = fidoHttpComponent.checkRegisteredStatus2(userBankingId,
                    fidoDeviceId,
                    fidoAppId, "", null);

            JSONArray registeredJsonArray = (JSONArray) fidoRegisterStatusMap.get("registeredJsonArray");

            // 파기대상 인증서 일련번호
            String expireSerialNumber = "";

            for (Object registeredObject : registeredJsonArray) {
                JSONObject registeredJsonObject = (JSONObject) registeredObject;

                String tmpDeviceId = registeredJsonObject.optString("deviceId");
                String tmpVerifyType = registeredJsonObject.optString("verificationType");

                if (fidoDeviceId.equalsIgnoreCase(tmpDeviceId) && fidoVerifyType.equalsIgnoreCase(tmpVerifyType)) {
                    expireSerialNumber = registeredJsonObject.optString("serialNumber");
                }
            }

            // 인증서 파기
            fidoCertificateInfoDao.updateExpiredCertificateIssued(expireSerialNumber);

            String currentDateTime = DateUtils.getCurrentDate("yyyyMMddHHmmss");
            Random rand0 = new Random();
            String certifyNo = "000" + ((rand0.nextInt() % 5000) + 5000);
            String randomKey = certifyNo.substring(certifyNo.length() - 4, certifyNo.length());

            FidoBackupCertListDataParameter parameter = new FidoBackupCertListDataParameter();
            parameter.setTrId(certUtils.getOPPRASerial(expireSerialNumber));
            parameter.setHistId(currentDateTime + "" + randomKey);
            parameter.setAuthDt(currentDateTime);
            parameter.setUserId(userBankingId);
            parameter.setTeleType("N");
            parameter.setHpNum("L");
            parameter.setResltCd(fidoVerifyType);

            // 디지털인증서 Backup 저장
            fidoCertificateInfoDao.insertFidoBackupCertListData(parameter);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("VerificationFidoService > expireDigitalCert - Exception");
            }
            e.printStackTrace();
        }
        return response;
    }

    /**
     * FIDO 금융인증서 생체 인증서비스 요청
     * 
     * @param request
     * @param fidoParamJson
     * @return
     */
    private VrfFidRequestFidoServiceResponse requestServiceAuth(VrfFidRequestFidoServiceRequest request,
            JSONObject fidoParamJson) {
        VrfFidRequestFidoServiceResponse response = new VrfFidRequestFidoServiceResponse();

        PinUserInfoVo pinUserInfo = getPinUserInfo(
                StringUtils.defaultString(request.getDigitalCertUpdatedYn()));

        String pinUserId = pinUserInfo == null ? "" : StringUtils.defaultString(pinUserInfo.getUserBankingId());
        String sessionUserId = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),
                sessionManager.getGlobalValue("UserID", String.class));
        String userBankingID = StringUtils.defaultIfEmpty(pinUserId, sessionUserId);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestServiceAuth - pinUserId : [{}], sessionUserId : [{}]",
                    pinUserId, sessionUserId);
        }

        fidoParamJson.put("appId", StringUtils.defaultString(request.getFidoAppId()));
        fidoParamJson.put("deviceId", StringUtils.defaultString(request.getFidoDeviceId()));
        fidoParamJson.put("verifyType", StringUtils.defaultString(request.getFidoVerifyType()));
        fidoParamJson.put("loginId", StringUtils.defaultString(userBankingID));
        fidoParamJson.put("issueType", "03");

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestServiceAuth - fidoParamJson : [{}]", fidoParamJson.toString());
        }
        Map<String, Object> fidoResultMap = fidoHttpComponent.requestServiceAuth(fidoParamJson);
        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > requestServiceAuth - fidoResultMap : [{}]", fidoResultMap.toString());
        }

        response.setResultCode(StringUtils.defaultString((String) fidoResultMap.get("resultCode")));
        response.setResultMsg(StringUtils.defaultString((String) fidoResultMap.get("resultMsg")));
        response.setUserBankingId(userBankingID);
        response.setTrId(StringUtils.defaultString((String) fidoResultMap.getOrDefault("trId", "")));

        // 결과확인(trResultConfirm)시 요청할 거래아이디 저장
        sessionManager.setGlobalValue("FIDO_SVCTR_ID", fidoParamJson.get("svcTrId"));

        return response;
    }

    /**
     * 금융인증서 간편로그인 사용자 정보(인증서일련번호, 간편로그인 토큰) 조회
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/inquirySimpleFinCertUserInfo", name = "금융인증서 간편로그인 사용자 정보 조회")
    public VrfFidInquirySimpleFinCertUserInfoResponse inquirySimpleFinCertUserInfo(IServiceContext ctx,
            VrfFidInquirySimpleFinCertUserInfoRequest request) {
        VrfFidInquirySimpleFinCertUserInfoResponse response = new VrfFidInquirySimpleFinCertUserInfoResponse();

        String pinDeviceId = "";
        String osVersion = StringUtils.defaultString(PRCSharedUtils.getOsVersion());
        String ipinsideHdd = PRCSharedUtils.getIpinsideHdd();

        if (!"".equals(osVersion) && osVersion.indexOf(".") > -1) {
            osVersion = osVersion.substring(0, osVersion.indexOf("."));
        }

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > inquirySimpleFinCertUserInfo - isAndroid : [{}], osVersion : [{}]",
                    PRCSharedUtils.isAndroid(), osVersion);
        }

        try {
            if (PRCSharedUtils.isAndroid() && Integer.parseInt(osVersion) > 5) {
                // 안드로이드이면서 OSVersion이 6.XX이상이면서 MAC && IMEI 값으로 PIN TABLE 조회할 경우
                pinDeviceId = ipinsideComponent.simpleDataDecodeIMEI(); // AS-IS
                Map<String, String> deviceData = CommonBizUtils.getDataMap(ipinsideHdd); // TO-BE

                if (log.isDebugEnabled()) {
                    log.debug(
                            "VerificationFidoService > inquirySimpleFinCertUserInfo - PinDeviceId(IMEI) : [{}], deviceData : [{}]",
                            pinDeviceId, deviceData.toString());
                }

                if ("Y".equals(StringUtils.defaultString(request.getDigitalCertUpdatedYn()))) {
                    // 디지털인증서 고유식별값 업데이트 완료 상태일 경우 Android ID를 통해 조회
                    pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_ANDROID_ID);
                }
            } else {
                pinDeviceId = ipinsideComponent.simpleDataDecode();
            }
        } catch (Exception e) {
            pinDeviceId = ipinsideComponent.simpleDataDecode();
        }

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > inquirySimpleFinCertUserInfo - pinDeviceId : [{}]", pinDeviceId);
        }

        SimpleFinCertUserInfoResult result = ma3CertUserMgtDao.selectSimpleFinCertUserInfo(pinDeviceId);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > inquirySimpleFinCertUserInfo - result : [{}]",
                    result == null ? "NULL" : result.toString());
        }

        if (result != null) {
            response.setConnectType(StringUtils.defaultString(result.getConnectType()));
            response.setFinCertSeqNum(StringUtils.defaultString(result.getFinCertSeqNum()));
            response.setFinCertSimpleKeyToken(StringUtils.defaultString(result.getFinCertSmpKeyTkn()));
        }

        return response;
    };

    /**
     * FIDO 유저정보(userId) 조회
     * 
     * @param digitalCertUpdatedYn
     * @return
     */
    private PinUserInfoVo getPinUserInfo(String digitalCertUpdatedYn) {
        PinUserInfoVo pinUserInfo = new PinUserInfoVo();

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > getPinUserInfo - isSB : [{}], IpinsideHdd : [{}]",
                    PRCSharedUtils.isSB(), PRCSharedUtils.getIpinsideHdd());
        }

        // 로컬환경이거나 Ipinside 데이터가 없을 경우 스마트뱅킹이 아닌경우 간편로그인 사용불가
        if (StringUtils.defaultString(PRCSharedUtils.getIpinsideHdd()).isBlank()
                || !PRCSharedUtils.isSB()
        // || RunMode.LOCAL.equals(RuntimeContext.getRunMode())
        ) {
            return pinUserInfo;
        }

        String pinDeviceId = "";
        String osVersion = StringUtils.defaultString(PRCSharedUtils.getOsVersion());
        String ipinsideHdd = PRCSharedUtils.getIpinsideHdd();

        if (!"".equals(osVersion) && osVersion.indexOf(".") > -1) {
            osVersion = osVersion.substring(0, osVersion.indexOf("."));
        }

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > getPinUserInfo - isAndroid : [{}], osVersion : [{}]",
                    PRCSharedUtils.isAndroid(), osVersion);
        }

        try {
            if (PRCSharedUtils.isAndroid() && Integer.parseInt(osVersion) > 5) {
                // 안드로이드이면서 OSVersion이 6.XX이상이면서 MAC && IMEI 값으로 PIN TABLE 조회할 경우
                pinDeviceId = ipinsideComponent.simpleDataDecodeIMEI(); // AS-IS
                Map<String, String> deviceData = CommonBizUtils.getDataMap(ipinsideHdd); // TO-BE

                if (log.isDebugEnabled()) {
                    log.debug("VerificationFidoService > getPinUserInfo - PinDeviceId(IMEI) : [{}], deviceData : [{}]",
                            pinDeviceId, deviceData.toString());
                }

                if ("Y".equals(digitalCertUpdatedYn)) {
                    // 디지털인증서 고유식별값 업데이트 완료 상태일 경우 Android ID를 통해 조회
                    pinDeviceId = deviceData.get(CommonBizConstants.IPINSIDE_ANDROID_ID);
                } else if (pinDeviceId.equals(deviceData.get(CommonBizConstants.IPINSIDE_WIDEVINE_ID))) {
                    // 디지털인증서 고유식별값 업데이트가 완료 상태가 아닌 경우
                    // 고유식별값으로 widevine ID를 사용중인 경우만, Android ID로 업데이트 필요하므로 업데이트 정보 셋팅
                    pinUserInfo.setRequireDeviceInfoUpdate(true);
                    pinUserInfo.setBeforeDeviceId(deviceData.get(CommonBizConstants.IPINSIDE_WIDEVINE_ID)); // 현재데이터(WidevineID)
                    pinUserInfo.setAfterDeviceId(deviceData.get(CommonBizConstants.IPINSIDE_ANDROID_ID)); // 변경할데이터(AndroidID)
                }
            } else {
                pinDeviceId = ipinsideComponent.simpleDataDecode();
            }
        } catch (Exception e) {
            pinDeviceId = ipinsideComponent.simpleDataDecode();
        }

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > getPinUserInfo - pinDeviceId : [{}], pinUserInfo : [{}]", pinDeviceId,
                    pinUserInfo.toString());
        }

        // 간편로그인 사용자 조회
        PinUsrMgtByDeviceResult result = pinUsrMgtDao.selectPinUsrMgtByDeviceId(pinDeviceId);

        if (log.isDebugEnabled()) {
            log.debug("LoginHelper > getPinUserInfo - result : ", result);
        }

        // 간편로그인 사용자정보가 없다면 종료
        if (result == null) {
            return pinUserInfo;
        }

        pinUserInfo.setUserBankingId(result.getUserBankingId());
        pinUserInfo.setDeviceId(result.getDeviceId());
        pinUserInfo.setDeviceType(result.getDeviceType());
        pinUserInfo.setNickName(result.getNickName());

        return pinUserInfo;
    }

    private PinUserInfoVo updateDigitalCertPinDeviceId(String digitalCertUpdatedYn, PinUserInfoVo pinUserInfo) {

        try {
            if ("Y".equals(digitalCertUpdatedYn) && !pinUserInfo.isRequireDeviceInfoUpdate()) {
                pinUserInfo.setExecutedDeviceInfoUpdateYn("N");
                return pinUserInfo;
            }

            String userBankingId = StringUtils.defaultString(pinUserInfo.getUserBankingId());
            String beforeDeviceId = StringUtils.defaultString(pinUserInfo.getBeforeDeviceId());
            String afterDeviceId = StringUtils.defaultString(pinUserInfo.getAfterDeviceId());

            if (StringUtils.isNotEmpty(beforeDeviceId) && StringUtils.isNotEmpty(afterDeviceId)) {
                HashMap<String, String> queryParam = new HashMap<String, String>();
                queryParam.put("targetUserId", userBankingId); // 사용자 뱅킹 id
                queryParam.put("beforeDeviceId", beforeDeviceId); // 업데이트 전 사용자 기기 id
                queryParam.put("afterDeviceId", afterDeviceId); // 업데이트 후 사용자 기기 id

                PinUsrMgtUpdateParameter parameter = PinUsrMgtUpdateParameter.builder()
                        .userBankingId(userBankingId)
                        .beforeDeviceId(beforeDeviceId)
                        .afterDeviceId(afterDeviceId)
                        .build();

                pinUsrMgtDao.updatePinUsrMgtDeviceId(parameter);

                pinUserInfo.setExecutedDeviceInfoUpdateYn("Y");
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("VerificationFidoService > updatePinUserInfo : ");
            }
        }

        return pinUserInfo;
    }

    private void checkEnabledRecoveryUser(String signedData, String fidoUserId) {
        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > checkEnabledRecoveryUser - start");
        }

        // 환경설정 경로 지정 필요할경우
        WizveraConfig delfinoConfig = null;
        try {
            delfinoConfig = new WizveraConfig(configPath);
        } catch (IOException e) {
            log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
        }
        SignVerifier signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
        SignVerifierResult signVerifierResult = null; // 서명검증 결과
        X509Certificate userCert = null;
        String digitalCertSerial = null;

        try {
            signVerifierResult = signVerifier.verifyPKCS7(signedData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
            userCert = signVerifierResult.getSignerCertificate(); // 사용자인증서
        } catch (DelfinoServiceException e) {
            e.printStackTrace();
            String errorCode = "DITERR_" + Integer.toString(e.getErrorCode());
            String message = "";

            IServiceContext sc = ServiceContextHolder.getContext();
            if (Locale.KOREA.equals(sc.locale())) {
                message = e.getErrorUserMessage();
            } else {
                message = e.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN);
            }

            throw new PRCServiceException(errorCode, message);
        }

        digitalCertSerial = certUtils.getOPPRASerial(userCert); // 시리얼 번호를 12자리로 변환함

        if (log.isDebugEnabled()) {
            log.debug("VerificationFidoService > checkEnabledRecoveryUser - digitalCertSerial : [{}]",
                    digitalCertSerial);
        }

        FidoBackupInfoResult result = null;

        try {
            FidoBackupInfoParameter parameter = FidoBackupInfoParameter.builder().userId(fidoUserId)
                    .trId(digitalCertSerial).hpNum("L").build();
            if (log.isDebugEnabled()) {
                log.debug("VerificationFidoService > checkEnabledRecoveryUser - parameter : [{}]",
                        parameter.toString());
            }
            result = opMyselfConfrHistDao.selectFidoBackupInfo(parameter);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("VerificationFidoService > checkEnabledRecoveryUser - selectFidoBackupInfo : FAIL");
            }
            e.printStackTrace();
        }

        if (result != null) {
            if (log.isDebugEnabled()) {
                log.debug("VerificationFidoService > checkEnabledRecoveryUser - result : [{}]", result.toString());
            }
            String teleType = StringUtils.defaultString((String) result.getTeleType());

            if ("N".equalsIgnoreCase(teleType)) {
                throw new PRCServiceException("DTERR999",
                        "디지털 인증서가 효력정지되어 새로운 인증서를 발급받으셔야 이용 가능합니다. 인증센터>디지털인증서 발급 거래를 진행 부탁드립니다.");
            }
        } else {
            throw new PRCServiceException("DTERR999",
                    "디지털 인증서가 효력정지되어 새로운 인증서를 발급받으셔야 이용 가능합니다. 인증센터>디지털인증서 발급 거래를 진행 부탁드립니다.");
        }
    }

    private void saveFidoLog(String signedData, String fidoUserId, String fidoVerifyType) {
        try {
            // 환경설정 경로 지정 필요할경우
            WizveraConfig delfinoConfig = null;
            try {
                delfinoConfig = new WizveraConfig(configPath);
            } catch (IOException e) {
                log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
            }
            SignVerifier signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
            SignVerifierResult signVerifierResult = null; // 서명검증 결과

            signVerifierResult = signVerifier.verifyPKCS7(signedData, SignVerifier.CERT_STATUS_CHECK_TYPE_NONE);
            X509Certificate userCert = signVerifierResult.getSignerCertificate(); // 사용자인증서
            String digitalCertSerial = certUtils.getOPPRASerial(userCert); // 시리얼 번호를 12자리로 변환함
            String ldapSerial = "";
            String spassYn = "N";

            List<LdapInfoResult> resultList = ldapInfoDao.selectOppraLdapInfo(digitalCertSerial);
            if (log.isDebugEnabled()) {
                log.debug("VerificationFidoService > saveFidoLog - resultList : [{}]",
                        resultList == null ? "NULL" : resultList.toString());
            }

            if (resultList != null && resultList.size() > 0) {
                for (LdapInfoResult result : resultList) {
                    // 조회 결과가 있다면 당행일 경우 인증서 상태 체크
                    String raflag = (String) result.getRaflag(); // 당행이면 1, 타행이면 0
                    String status = (String) result.getStatus(); // 효력정지면 S 정상은 V

                    ldapSerial = (String) result.getSerial();

                    // if (raflag.equals("1") && status.equals("S")) {
                    // spassYn = "Y";
                    // } else
                    if (raflag.equals("1") && !status.equals("V")) {
                        spassYn = "Y";
                    }
                }
            } else {
                spassYn = "Y";
            }

            if ("Y".equalsIgnoreCase(spassYn)) {
                String currentDateTime = DateUtils.getCurrentDate("yyyyMMddHHmmss");
                Random rand0 = new Random();
                String certifyNo = "000" + ((rand0.nextInt() % 5000) + 5000);
                String randomKey = certifyNo.substring(certifyNo.length() - 4, certifyNo.length());

                FidoLogInfoParameter parameter = FidoLogInfoParameter.builder()
                        .deviceMngtId(currentDateTime + "" + randomKey)
                        .regDt(currentDateTime)
                        .deviceMac(fidoUserId)
                        .hpNum(digitalCertSerial)
                        .deviceImei(ldapSerial)
                        .spassYn(spassYn)
                        .delDt("16384".equalsIgnoreCase(fidoVerifyType) ? "PIN" : "BIO").build();

                opDeviceInfoDao.insertFidoLogInfo(parameter);
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn(
                        "VerificationFidoService > saveFidoLog - selectFidoBackupInfo : FAIL");
            }
            e.printStackTrace();
        }
    }

}
