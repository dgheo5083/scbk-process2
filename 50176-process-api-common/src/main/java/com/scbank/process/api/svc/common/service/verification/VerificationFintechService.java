package com.scbank.process.api.svc.common.service.verification;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H84300Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H84300Res;
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
import com.scbank.process.api.svc.common.dao.Ma3CertUserMgtDao;
import com.scbank.process.api.svc.common.dao.dto.FinTechCertUserInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.FinTechCertUserInfoResult;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcCheckFintechCertUserRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcCheckFintechCertUserResponse;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcInquiryCiInfoRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcInquiryCiInfoResponse;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcVerifyFintechCertLoginSignRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcVerifyFintechCertLoginSignResponse;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcVerifyFintechCertSignRequest;
import com.scbank.process.api.svc.common.service.verification.dto.fintech.VrfFtcVerifyFintechCertSignResponse;
import com.scbank.process.api.svc.shared.components.cert.CertificateHelper;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.SessionUtils;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.SignVerifierResult;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통-인증-핀테크(TOSS)", url = "/verification/fintech")
public class VerificationFintechService {

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;

    /**
     * 핀테크인증서 검증 컴포넌트
     */
    private final FinTechCertVerifyComponent finTechCertVerifyComponent;

    /**
     * 핀테크인증서 사용자 정보 DAO
     */
    private final Ma3CertUserMgtDao ma3CertUserMgtDao;

    /**
     * 핀테크인증서 로그인 서명 검증 및 CI 검증
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/verifyFintechCertLoginSign", name = "핀테크인증서 로그인 서명 검증 및 CI검증")
    public VrfFtcVerifyFintechCertLoginSignResponse verifyFintechCertLoginSign(IServiceContext ctx,
            VrfFtcVerifyFintechCertLoginSignRequest request) {
        VrfFtcVerifyFintechCertLoginSignResponse response = new VrfFtcVerifyFintechCertLoginSignResponse();

        // 핀테크 검증 정보 세션 초기화
        sessionManager.removeGlobalValue("FINCERT_VERIFIER_USERID");
        sessionManager.removeGlobalValue("FINCERT_VERIFIER_LOGIN");
        sessionManager.removeGlobalValue("FINCERT_VERIFIER_TYPE");
        sessionManager.removeGlobalValue("FINCERT_VERIFIER_CINO");
        sessionManager.removeGlobalValue("FINCERT_VERIFIER_SERIAL");

        HttpSession httpSession = ServiceContextHolder.getContext().request().getSession();

        String vpcg = StringUtils.defaultString(request.getVpcg()).trim();
        String delfinoNonce = StringUtils.defaultString((String) httpSession.getAttribute("delfinoNonce"));

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > verifyFintechCertLoginSign - delfinoNonce : [{}], request : [{}]",
                    delfinoNonce, request.toString());
        }

        if (delfinoNonce == null || delfinoNonce.isEmpty() || StringUtils.isEmpty(vpcg)) {
            throw new PRCServiceException("PRCCMMVPCG_0001", "핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        JSONObject vpcgObject = new JSONObject();

        @SuppressWarnings("deprecation")
        String decodedVpcg = java.net.URLDecoder.decode(vpcg);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > verifyFintechCertLoginSign - decodedVpcg : [{}]", decodedVpcg);
        }

        try {
            vpcgObject = new JSONObject(decodedVpcg);
            if (log.isDebugEnabled()) {
                log.debug("VerificationFintechService > verifyFintechCertLoginSign - vpcgObject : [{}]",
                        vpcgObject.toString());
            }
        } catch (JSONException e) {
            throw new PRCServiceException("PRCCMMVPCG_0001", "핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        SignVerifierResult signVerifierResult = null;
        signVerifierResult = finTechCertVerifyComponent.loginVerify(delfinoNonce, "L", vpcgObject.toString());

        if (signVerifierResult == null) {
            throw new PRCServiceException("MA3CMMVPCG_0002", "핀테크인증 전자서명 데이터가 존재하지 않습니다. 다시 거래해 주시기 바랍니다.");
        }

        if (log.isDebugEnabled()) {
            log.debug(
                    "VerificationFintechService > verifyFintechCertLoginSign - PKCS7SignedData : [{}], OriginSignedRawData : [{}]",
                    signVerifierResult.getPKCS7SignedData(), signVerifierResult.getOriginSignedRawData());
        }

        X509Certificate userCert = signVerifierResult.getSignerCertificate(); // 사용자인증서
        CertificateHelper certificateHelper = new CertificateHelper(userCert);
        String finCertSeqNum = StringUtils.defaultString(certificateHelper.getSerialDecimal());
        String connectType = StringUtils.defaultString(signVerifierResult.getProvider()).indexOf("toss") > -1 ? "D"
                : "";
        String userId = "";
        String ciNo = "";

        if (StringUtils.isEmpty(finCertSeqNum) || StringUtils.isEmpty(connectType)) {
            throw new PRCServiceException("MA3CMMVPCG_0010", "전달된 핀테크인증 전자서명 데이터 검증에 실패하였습니다.");
        }

        try {
            FinTechCertUserInfoParameter parameter = FinTechCertUserInfoParameter.builder()
                    .finCertSeqNum(finCertSeqNum)
                    .connectType(connectType)
                    .build();

            // 핀테크인증서 가입 사용자 정보 조회
            FinTechCertUserInfoResult result = ma3CertUserMgtDao.selectFinTechCertJoinedUserInfo(parameter);

            if (log.isDebugEnabled()) {
                log.debug("VerificationFintechService > verifyFintechCertLoginSign - result : [{}]",
                        result == null ? "NULL" : result.toString());
            }

            if (result == null) {
                // 미가입자 처리
                sessionManager.setGlobalValue("FINCERT_VERIFIER_LOGIN", "N"); // 로그인 우회 방지(로그인 처리시 검증진행)
                response.setResultCode("VPCG100");
                return response;
            }
            userId = StringUtils.defaultString(result.getUserId());

            if ("N".equalsIgnoreCase((result.getJoinFlg()))) {
                // 이용해지 고객
                sessionManager.setGlobalValue("FINCERT_VERIFIER_LOGIN", "N"); // 로그인 우회 방지(로그인 처리시 검증진행)
                response.setResultCode("VPCG100");
                return response;
            }

            boolean verifyUser = false;

            // CI 조회 실행
            OltpRequestOptions oltpOpt = hostClient.getOltpRequestOptions("CB_IBK01_H843");

            // 필수 공통부 설정
            oltpOpt.setImsTranCd("TI1IBK01");
            oltpOpt.setInClassCd("H843");
            oltpOpt.setSvcCd("843");

            // 전문 요청부 설정
            CbIbk01H84300Req h84300Req = new CbIbk01H84300Req();
            h84300Req.setUserID(StringUtils.defaultString(userId));
            h84300Req.setTSPassword(StringUtils.defaultString(SessionUtils.getSessionValue("TSPassword")));
            h84300Req.setYIIPGB("1"); // 이용자 구분(1:이용자번호, 2:CI번호, 3:주민등록번호)
            h84300Req.setYICINO("");// CI번호
            h84300Req.setYIJUMIN(""); // 주민등록번호

            OltpResponse<CbIbk01H84300Res> oltpResponse = hostClient.sendOltp(oltpOpt, h84300Req,
                    CbIbk01H84300Res.class);
            CbIbk01H84300Res h84300Res = oltpResponse.getResponse();

            if (log.isDebugEnabled()) {
                log.debug(
                        "VerificationFintechService > verifyFintechCertLoginSign - YOCINO : [{}], YOCONFIRM : [{}], YOCOND: [{}]",
                        h84300Res.getYOCINO(), // YOCINO : 고객 CI
                        h84300Res.getYOCONFIRM(), // YOCONFIRM : CI상태(Y: 정상, N: 미등록자)
                        h84300Res.getYOCOND()); // YOCOND : 사활구분 (Y: 활, N: 사)
            }

            // CI 유효성 체크
            if ("N".equalsIgnoreCase(h84300Res.getYOCONFIRM()) || "N".equalsIgnoreCase(h84300Res.getYOCOND())) {
                // CI값 비정상 사용자
                throw new PRCServiceException("MA3CMMVPCG_0004",
                        "본인인증에 실패했습니다. 고객님의 CI 정보가 미등록 또는 비활성화 상태입니다.");
            }

            ciNo = StringUtils.defaultString(h84300Res.getYOCINO());

            try {
                if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
                    // CI 검증 실행
                    verifyUser = signVerifierResult.verifyCi(ciNo);
                } else {
                    verifyUser = true;
                }
            } catch (DelfinoServiceException e) {
                if (log.isErrorEnabled()) {
                    log.error(
                            "VerificationFintechService > verifyFintechCertLoginSign > verifyCi - ErrorCode : [{}], ErrorUserMessage : [{}], ErrorMessage : [{}]",
                            e.getErrorCode(), e.getErrorUserMessage(), e.getMessage());
                }
                verifyUser = false;
            }

            if (!verifyUser) {
                throw new PRCServiceException("MA3CMMVPCG_0006", "로그인 사용자와 핀테크 인증서를 제출한 사용자가 다릅니다.");
            }

            response.setResultCode("VPCG000");

            // 검증 완료 정보 세션 저장
            sessionManager.setGlobalValue("FINCERT_VERIFIER_LOGIN", "Y");
            sessionManager.setGlobalValue("FINCERT_VERIFIER_USERID", userId);
            sessionManager.setGlobalValue("FINCERT_VERIFIER_TYPE", connectType);
            sessionManager.setGlobalValue("FINCERT_VERIFIER_CINO", ciNo);
            sessionManager.setGlobalValue("FINCERT_VERIFIER_SERIAL", finCertSeqNum);

        } catch (PRCServiceException e) {
            throw e;
        } finally {
            if (signVerifierResult != null) {
                boolean isSaveTxInfo = false;
                isSaveTxInfo = signVerifierResult.saveTxInfo(StringUtils.defaultIfEmpty(userId, ""), "LOGIN");

                if (log.isDebugEnabled()) {
                    log.debug(
                            "VerificationFintechService > verifyFintechCertLoginSign - userId : [{}], isSaveTxInfo : [{}]",
                            userId,
                            isSaveTxInfo);
                }
            }
        }

        return response;
    };

    /**
     * 핀테크인증서 서명 사용자 확인
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/checkFintechCertUser", name = "핀테크인증서 서명 사용자 확인")
    public VrfFtcCheckFintechCertUserResponse checkFintechCertUser(IServiceContext ctx,
            VrfFtcCheckFintechCertUserRequest request) {
        VrfFtcCheckFintechCertUserResponse response = new VrfFtcCheckFintechCertUserResponse();

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > checkFintechCertUser - request : [{}]", request.toString());
        }

        String vpcg = StringUtils.defaultString(request.getVpcg());
        String plainText = StringUtils.defaultString(request.getPlainText());
        String connectType = StringUtils.defaultString(request.getProvider()).indexOf("toss") > -1 ? "D" : "";

        if (StringUtils.isEmpty(connectType) || StringUtils.isEmpty(vpcg) || StringUtils.isEmpty(plainText)) {
            throw new PRCServiceException("MA3CMMVPCG_0007", "핀테크인증 전자서명 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        JSONObject vpcgObject = new JSONObject();

        @SuppressWarnings("deprecation")
        String decodedVpcg = java.net.URLDecoder.decode(vpcg); // ? 이미 decode 처리?

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > checkFintechCertUser - decodedVpcg : [{}]", decodedVpcg);
        }

        try {
            vpcgObject = new JSONObject(decodedVpcg);
        } catch (JSONException e) {
            throw new PRCServiceException("MA3CMMVPCG_0007", "핀테크인증 전자서명 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        // try{
        String userId = "";

        if (sessionManager.isLogin()) {
            userId = StringUtils.defaultString(sessionManager.getLoginValue("UserID", String.class));
        } else {
            userId = StringUtils.defaultString(sessionManager.getGlobalValue("UserID", String.class));
        }

        if (StringUtils.isEmpty(userId)) {
            throw new PRCServiceException("MA3CMMVPCG_0008", "핀테크인증 전자서명에 사용될 사용자 아이디가 존재하지 않습니다.");
        }

        FinTechCertUserInfoParameter parameter = FinTechCertUserInfoParameter.builder()
                .userId(userId)
                .connectType(connectType)
                .build();

        // 핀테크인증서 사용자 정보 조회
        FinTechCertUserInfoResult result = ma3CertUserMgtDao.selectFinTechCertUserInfo(parameter);

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > checkFintechCertUser - result : [{}]",
                    result == null ? "NULL" : result.toString());
        }

        if (result == null || !"Y".equalsIgnoreCase(StringUtils.defaultString(result.getJoinFlg()))) {
            throw new PRCServiceException("MA3CMMVPCG_0009", "핀테크인증 미등록 사용자 입니다. 핀테크인증 이용등록 후 다시 거래해 주시기 바랍니다.");
        }

        JSONObject vpcgOrgData = new JSONObject();
        vpcgOrgData.put("signData", plainText);

        response.setResultCode("000");
        // response.setVpcgOrgData(vpcgOrgData.toString());
        response.setVpcgVerifyData(vpcgOrgData.toString());
        // response.setVpcgSignedData(vpcgObject.toString());
        response.setVpcgPkcs7Data(vpcgObject.toString());

        // } catch (PRCServiceException e) {
        // throw e;
        // }

        return response;
    }

    /**
     * 핀테크인증서 전자서명
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/verifyFintechCertSign", name = "핀테크인증서 전자서명")
    public VrfFtcVerifyFintechCertSignResponse verifyFintechCertSign(IServiceContext ctx,
            VrfFtcVerifyFintechCertSignRequest request) {
        VrfFtcVerifyFintechCertSignResponse response = new VrfFtcVerifyFintechCertSignResponse();

        HttpSession httpSession = ServiceContextHolder.getContext().request().getSession();

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > verifyFintechCertSign - request : [{}]", request.toString());
        }

        String actionType = StringUtils.defaultString(request.getActionType());
        String vpcg = StringUtils.defaultString(request.getVpcg()).trim();
        String connectType = StringUtils.defaultString(request.getProvider()).indexOf("toss") > -1 ? "D" : "";

        if (StringUtils.isEmpty(vpcg)) {
            httpSession.removeAttribute("delfinoNonce");
            throw new PRCServiceException("MA3CMMVPCG_0007", "핀테크인증 전자서명 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        JSONObject vpcgObject = new JSONObject();

        @SuppressWarnings("deprecation")
        String decodedVpcg = java.net.URLDecoder.decode(vpcg); // ?? 이미 decode???
        String finCertSeqNum = "";

        if (log.isDebugEnabled()) {
            log.debug("VerificationFintechService > verifyFintechCertSign - decodedVpcg : [{}]", decodedVpcg);
        }

        try {
            vpcgObject = new JSONObject(decodedVpcg);
        } catch (JSONException e) {
            throw new PRCServiceException("MA3CMMVPCG_0001", "핀테크인증 요청 정보가 잘못되었습니다. 다시 거래해 주시기 바랍니다.");
        }

        if (sessionManager.isLogin()) {
            // 로그인 사용자의 경우 현재 등록된 사설인증서인지 체크
            String userId = StringUtils.defaultString(sessionManager.getLoginValue("UserID", String.class));

            if ("".equalsIgnoreCase(userId)) {
                throw new PRCServiceException("MA3CMMVPCG_0008", "핀테크인증 전자서명에 사용될 사용자 아이디가 존재하지 않습니다.");
            }

            FinTechCertUserInfoParameter parameter = FinTechCertUserInfoParameter.builder()
                    .userId(userId)
                    .connectType(connectType)
                    .build();

            // 핀테크인증서 사용자 정보 조회
            FinTechCertUserInfoResult result = ma3CertUserMgtDao.selectFinTechCertUserInfo(parameter);

            if (log.isDebugEnabled()) {
                log.debug("VerificationFintechService > verifyFintechCertSign - result : [{}]",
                        result == null ? "NULL" : result.toString());
            }

            if (result == null || !"Y".equalsIgnoreCase(StringUtils.defaultString(result.getJoinFlg()))) {
                throw new PRCServiceException("MA3CMMVPCG_0009", "핀테크인증 미등록 사용자 입니다. 핀테크인증 이용등록 후 다시 거래해 주시기 바랍니다.");
            }
        }

        if ("AUTH".equals(actionType)) {
            // 본인인증
            HashMap<String, Object> authVerifyResultMap = finTechCertVerifyComponent.authVerify(vpcgObject.toString());

            if (sessionManager.isLogin() && !finCertSeqNum.equals(authVerifyResultMap.get("certSerial"))) {
                // 로그인 인증서와 일치여부 검증
                throw new PRCServiceException("MA3CMMVPCG_0005", "본인인증에 실패했습니다. 본인인증 사용자와 핀테크 인증서를 제출한 사용자가 다릅니다.");
            }

            if (log.isDebugEnabled()) {
                log.debug("VerificationFintechService > verifyFintechCertSign - authVerifyResultMap : [{}]",
                        authVerifyResultMap.toString());
            }

            if ("0000".equals(authVerifyResultMap.getOrDefault("returnCode", ""))) {
                response.setResultCode("000");
                response.setPkcs7SignedData(
                        StringUtils.defaultString((String) authVerifyResultMap.get("pkcs7SignedData")));
                // response.setProvider(provider);
            }

            return response;
        }

        String multiSignYn = StringUtils.defaultString(request.getMultiSignYn());
        String finTechVerifyYn = StringUtils.defaultString(request.getFinTechVerifyYn());
        String plainText = StringUtils.defaultString(request.getPlainText());
        HashMap<String, Object> signVerifyResultMap = new HashMap<String, Object>();

        if ("Y".equals(finTechVerifyYn)) {
            sessionManager.setGlobalValue("finTechSignPlainText", plainText);

            signVerifyResultMap = finTechCertVerifyComponent.signVerify(multiSignYn,
                    vpcgObject.toString());

            if ("0000".equals(signVerifyResultMap.getOrDefault("returnCode", ""))) {
                if (log.isDebugEnabled()) {
                    log.debug("VerificationFintechService > verifyFintechCertSign - signVerifyResultMap : [{}]",
                            signVerifyResultMap.toString());
                }

                response.setResultCode("000");
                response.setPkcs7SignedData(
                        StringUtils.defaultString((String) signVerifyResultMap.get("pkcs7SignedData")));
                // response.setProvider(provider);

                if ("Y".equals(multiSignYn)) {
                    // 다건(?)서명
                    response.setPkcs7PdfSignData(
                            StringUtils.defaultString((String) signVerifyResultMap.get("pkcs7PdfSignData")));

                    // 전자서명 저장시에 ConnectType으로 저장될 사용자가 인증한 사설인증서 종류설정
                    sessionManager.setGlobalValue("SEL_FINTECH_CONNECTTYPE", connectType);
                }
            }

            sessionManager.removeGlobalValue("finTechSignPlainText");
        } else {

            // (사설인증서로그인+사설인증서 전자서명 허용메뉴인 경우 서비스)와 리턴값 동일
            JSONObject vpcgOrgData = new JSONObject();
            vpcgOrgData.put("signData", plainText);
            response.setVpcgVerifyData(vpcgOrgData.toString()); // 거래원문
            response.setVpcgPkcs7Data(vpcgObject.toString()); // 사인데이터(txId)
        }

        if (sessionManager.isLogin() && !finCertSeqNum.equals(signVerifyResultMap.get("certSerial"))) {
            // 로그인 인증서와 일치여부 검증
            throw new PRCServiceException("MA3CMMVPCG_0005", "본인인증에 실패했습니다. 본인인증 사용자와 핀테크 인증서를 제출한 사용자가 다릅니다.");
        }

        return response;
    }

    /**
     * 핀테크인증서 ci정보 조회
     * 
     * @param ctx
     * @param request
     * @return
     */
    @ServiceEndpoint(url = "/inquiryCiInfo", name = "핀테크인증서 ci정보 조회")
    public VrfFtcInquiryCiInfoResponse inquiryCiInfo(IServiceContext ctx,
            VrfFtcInquiryCiInfoRequest request) {
        VrfFtcInquiryCiInfoResponse response = new VrfFtcInquiryCiInfoResponse();

        String userCi = "";

        if (sessionManager.isLogin()) {
            userCi = StringUtils.defaultString(sessionManager.getLoginValue("FINCERT_VERIFIER_CINO", String.class));

            if (!"".equals(userCi)) {
                sessionManager.setLoginValue("USER_CI_INFO", userCi);
            }
        } else {
            userCi = StringUtils.defaultString(sessionManager.getGlobalValue("FINCERT_VERIFIER_CINO", String.class));

            if (!"".equals(userCi)) {
                sessionManager.setGlobalValue("USER_CI_INFO", userCi);
            }
        }

        response.setUserCi(userCi);

        return response;
    }
}
