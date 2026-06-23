/*******************************************************************************
*  업   무  명 : 공통
*  설      명 : 디지털인증서로 서명된 문서 검증 공통 컴포넌트
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

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.wizvera.WizveraConfig;
import com.wizvera.service.DelfinoServiceException;
import com.wizvera.service.SignVerifier;
import com.wizvera.service.SignVerifierResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "디지털인증서 전자서명인증서 검증", description = "디지털인증서 전자서명인증서 검증", author = "이완주")
@RequiredArgsConstructor
public class DigitalCertVerifyComponent {

    private final ISessionContextManager SessionManager;

    // delfino.properties
    @Value("${delfino.config.path:}")
    private String configPath;

    /**
     * 전자서명 검증 (L : 로그인)
     * 
     * @param dataType
     * @param signData
     * @return
     * @throws PRCServiceException
     */
    @ComponentOperation(name = "전자서명 검증", description = "전자서명 검증", author = "이완주")
    public HashMap<String, Object> verify(String dataType, String signData) throws PRCServiceException {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String resultCode = null;
        String resultMsg = null;
        String userID = null;
        String cid = null;
        String issuer_code = ""; // 발급자코드(01:yessign)
        // String orgData = "";
        X509Certificate userCert = null;

        // 여기서 베라포트 모듈로 인증서 데이터 추출
        int certStatusCheckType = SignVerifier.CERT_STATUS_CHECK_TYPE_NONE;

        // 환경설정 경로 지정 필요할경우
        WizveraConfig delfinoConfig = null;
        try {
            delfinoConfig = new WizveraConfig(configPath);
        } catch (IOException|IllegalStateException e) {
            log.error("delfino.properties 설정파일 찾지 못해 기본생성자로 SignVerifier를 생성함");
        }
        SignVerifier signVerifier = new SignVerifier(delfinoConfig); // 서명검증 객체
        SignVerifierResult signVerifierResult = null; // 서명검증 결과
        CertificateHelper certHelper = null;

        try {
            // 전자서명 데이터만 검증
            signVerifierResult = signVerifier.verifyPKCS7(signData, certStatusCheckType); // 인증서 데이터만 추출!
            userCert = signVerifierResult.getSignerCertificate(); // 사용자인증서(BC);
            certHelper = new CertificateHelper(userCert);
            log.debug(certHelper.toString());

        } catch (DelfinoServiceException dse) {
            dse.printStackTrace();
            String errorCode = "DITERR_" + Integer.toString(dse.getErrorCode());
            String message = "";

            if ("ko".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
                message = dse.getErrorUserMessage();
            } else {
                message = dse.getErrorUserMessage(com.wizvera.service.util.ErrorConvert.LOCALE_EN);
            }

            throw new PRCServiceException(errorCode, message);
        }

        if (userCert == null) {
            resultCode = "INI301";
            resultMsg = "정상적인 디지털 인증서가 아닙니다.(Code:001)";
            throw new PRCServiceException(resultCode, resultMsg);
        }

        String issuerDN = certHelper.getIssuerDN();
        String subjectDN = certHelper.getSubjectDN();
        String subjectCN = certHelper.getSubjectCN();
        String issuerO = certHelper.getIssuerO();
        String issuerBank = certHelper.getIssuerBank();
        String PolicyName = certHelper.getPolicyName();

        issuer_code = certHelper.getIssuerCode();// 발급자코드

        if (issuer_code.equals("")) {
            resultCode = "INI302";
            resultMsg = "정상적인 디지털 인증서가 아닙니다.(Code:002)";
            throw new PRCServiceException(resultCode, resultMsg);
        }

        /*
         * 금융인증서로 대출서명진행시 (dataType=LO) 타기관 금융인증서인 경우 getUserInfoByScbDB에서 타기관미등록된
         * 인증서오류발생 >> MA3CMMEDC001_501S.java 에서 catch로 빠져 session에 SignedTradData가 적재가안됨
         * >> 비대면 전자서명의 경우 타기관미등록 인증서로 서명이 가능하기때문에 로직을 호출하지않도록 해야함
         * (CertVerifyImplService.selfVerify 참고) - 제외로직 1. LDAP_INFO 조회 2. userID 비교
         *
         */
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        if (!dataType.equals("LO")) {

            // ======================================================
            // 3. DB에 등록된 사용자 정보를 조회 한다.
            // ======================================================
            userInfo = certHelper.getUserInfoByScbDB(dataType);
            userID = (String) userInfo.get("USERID");
            cid = (String) userInfo.get("CID");
            log.debug("#### PSH DigitalCertVerifyService USERID : " + userID);
            log.debug("#### PSH DigitalCertVerifyService CID : " + cid);

        }

        String certSerial = certHelper.getSerial();
        String certSerialHex = certHelper.getSerialHex();

        java.text.DateFormat myDate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm ss");

        String certBefore = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userCert.getNotBefore()); // 인증서유효기간:시작일
        String certAfter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userCert.getNotAfter()); // 인증서유효기간:종료일

        java.util.Date currentTime = new java.util.Date();// 현재 시간
        int dateformat = (int) ((userCert.getNotAfter().getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000));

        // 금결원일경우 은행코드 추출
        String bankCode = "";
        if ("yessign".equals(issuerO)) {
            int idx = subjectCN.lastIndexOf(")");
            if (idx != -1)
                bankCode = subjectCN.substring(idx + 2, idx + 5);
        }

        log.debug("인증서 정보 Start");
        log.debug("인증서 정보 인증서발급기관 공인인증서 ID [" + userID + "]");
        log.debug("인증서 정보 인증서발급기관 주민등록번호 [" + cid + "]");
        log.debug("인증서 정보 인증서발급주체 subjectDN [" + subjectDN + "]");
        log.debug("인증서 정보 인증서은행 issuerBank [" + issuerBank + "]");
        log.debug("인증서 정보 인증서은행 PolicyName [" + PolicyName + "]");
        log.debug("인증서 정보 인증서부가정보 issuerO [" + issuerO + "] subjectCN : [" + subjectCN + "]");
        log.debug("인증서 정보 인증서부가정보 bankCode [" + bankCode + "]");
        log.debug("인증서 정보 인증서일련번호 10진수 [ " + certSerial + "  16진수 : [" + certSerialHex + "]");
        log.debug("인증서 정보 인증서유효기간 발급기간 [ " + certBefore + " ~ " + certAfter + "]");
        log.debug("인증서 정보 인증서 정책 식별 ID [ " + certHelper.getCertificatePolicyOID() + "]");

        log.debug("####현재시간 : " + myDate.format(currentTime));
        log.debug("####고객님의 인증서는 ");
        log.debug(dateformat + "일 후에 만료가 됩니다.");
        log.debug("########");
        log.debug("########");

        // ======================================================
        // # 사용자 정보를 세션에 저장함.
        // ======================================================
        SessionManager.setGlobalValue("session_cid", cid);
        SessionManager.setGlobalValue("cid", cid);
        SessionManager.setGlobalValue("session_UserID", userID);
        SessionManager.setGlobalValue("certID", userID);
        SessionManager.setGlobalValue("uid", userID);
        SessionManager.setGlobalValue("policy", userInfo.get("POLICY"));
        SessionManager.setGlobalValue("serial", userInfo.get("SERIAL"));
        SessionManager.setGlobalValue("subjectDN", subjectDN);
        SessionManager.setGlobalValue("issuerDN", issuerDN);
        SessionManager.setGlobalValue("certType", issuer_code);
        SessionManager.setGlobalValue("serialSession", certSerialHex);
        // SessionManager.setGlobalObject("issueDate", userCert.getNotBefore());
        // SessionManager.setGlobalObject("expireDate", userCert.getNotAfter());
//        SessionManager.setGlobalValue("CertOID", CertUtil.getCertificatePolicyOID(userCert));
        SessionManager.setGlobalValue("CertOID", certHelper.getCertificatePolicyOID());
        SessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "0000");

        log.debug("인증서 검증 완료하였습니다.");
        resultMap.put("returnCode", "0000");
        resultMap.put("returnMsg", "인증서 검증 완료하였습니다.");
        resultMap.put("userCert", userCert);

        return resultMap;
    }

}