package com.scbank.process.api.svc.shared.components.sign;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONObject;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.base.store.secure.vo.SignInfo;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.IpUtils;
import com.scbank.process.api.fw.core.utils.ReflectionUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.cert.CertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.cert.FinTechCertVerifyComponent;
import com.scbank.process.api.svc.shared.components.fido.FidoHttpComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignActionType;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignType;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.components.sign.utils.SignUtils;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.constants.PRCSharedEnums.ConnectType;
import com.scbank.process.api.svc.shared.dao.SignTransactionDao;
import com.scbank.process.api.svc.shared.dao.dto.SignInfoParameter;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent(name = "전자서명 공통 컴포넌트", author = "2034263")
@RequiredArgsConstructor
public class SignComponent {

    /*
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * 공동인증서 검증 컴포넌트
     */
    private final CertVerifyComponent certVerifyComponent;

    /**
     * FIDO 공통 통신 컴포넌트
     */
    private final FidoHttpComponent fidoHttpComponent;

    /**
     * 금융인증서 검증 컴포넌트
     */
    private final FinCertVerifyComponent finCertVerifyComponent;

    /**
     * 금융인증서 검증 컴포넌트
     */
    private final FinTechCertVerifyComponent finTechCertVerifyComponent;

    /*
     * 서명 저장 DAO
     */
    private final SignTransactionDao signTransactionDao;

    @ComponentOperation(name = "전자서명 검증", author = "2034263")
    public void verifySign(IMessageObject requestMessage, SignVerifyInfo signVerifyInfo) {
        verify(requestMessage, signVerifyInfo);
    }

    @ComponentOperation(name = "전자서명 검증", author = "2034263")
    public void verifySign(SignVerifyInfo signVerifyInfo) {

        // 서명 검증 타입 요청 확인
        if (signVerifyInfo == null || signVerifyInfo.getSignVerifyType() == null) {
            // 전자서명 검증 타입 : 미설정
            throw new PRCServiceException("PRCCMM0000", "전자서명 요청이 유효하지 않습니다.");
        } else if (signVerifyInfo.getSignVerifyType() == SignVerifyType.NONE) {
            // 전자서명 검증 타입 : 미실행(전자서명 비대상)
            log.debug("signComponent > verifySign - 전자서명 비대상 거래");
            return;
        }

        IMessageObject serviceRequest = signVerifyInfo.getMessageRequest();
        Map<String, String> messageMap = signVerifyInfo.getMessageMap();

        if (messageMap != null && !messageMap.isEmpty()) {
            verify(messageMap, signVerifyInfo);
            return;
        }

        if ((serviceRequest != null)) {
            verify(serviceRequest, signVerifyInfo);
            return;
        }

        throw new PRCServiceException("PRCCMM0000", "전자서명 요청이 유효하지 않습니다.");
    }

    /**
     * 
     * @param messageObject
     * @param signVerifyInfo
     */
    private void verify(Object messageObject, SignVerifyInfo signVerifyInfo) {
        // SecureContext
        Optional<SecureContext> secureContext = SecureContextStore.getContext();

        // 전자서명 실행 타입 확인
        if (signVerifyInfo.getSignActionType() == null) {
            // default
            signVerifyInfo.setSignActionType(SignActionType.SIGN);
        }

        // 전자서명 secureData 확인
        if (secureContext.isEmpty() || secureContext.get() == null || secureContext.get().getSign() == null) {
            if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
                throw new PRCServiceException("PRCCMM0000", "전자서명 요청이 유효하지 않습니다.");
            }
            if (log.isDebugEnabled()) {
                log.debug("전자서명 검증 실패 로컬 환경 PASS");
            }
            return;
        }

        // client 서명 결과 데이터
        SignInfo signInfo = secureContext.get().getSign();
        if (log.isDebugEnabled()) {
            log.debug("SignComponent > verify - signInfo : [{}]", signInfo.toString());
        }

        // TODO: 로그인 수단별 서명 검증
        String connectType = "";
        String yiFido = ""; // G: MOTP로그인
        if (sessionManager.isLogin()) {
            connectType = StringUtils.defaultString(sessionManager.getLoginValue("ConnectType", String.class));
            yiFido = sessionManager.getLoginValue("YIFIDO", String.class);
        } else {
            // TODO: 비로그인 확인필요
            connectType = StringUtils.defaultString(ReflectionUtils.getFieldValue(messageObject, "ConnectType"));
        }

        signVerifyInfo.setConnectType(connectType);

        if (log.isDebugEnabled()) {
            log.debug("SignComponent > verify - signVerifyInfo : [{}], yiFido : [{}]", signVerifyInfo.toString(),
                    yiFido);
        }

        String motpSignSkipYn = sessionManager.getGlobalValue("motpSignSkipYn", String.class);
        String fidoSignSkipYn = sessionManager.getGlobalValue("fidoSignSkipYn", String.class);

        // TODO: 스킵처리를 유효성 검증 앞으로 빼야하는데...?
        // MOTP 간편로그인 시 서명 스킵
        if (SignConstants.SIGN_SKIP_RESULT_CODE.equals(signInfo.getSignResultCode())
                && "Y".equals(motpSignSkipYn) && ConnectType.MOBILE_OTP.isEquals(connectType)) {
            if (log.isDebugEnabled()) {
                log.debug("전자서명 스킵 처리(간편로그인)");
            }
            // sessionManager.removeGlobalValue("motpSignSkipYn");
            return;
        }

        // 금융인증서생체 / 디지털인증서 간편 로그인 시 서명 스킵
        if (SignConstants.SIGN_SKIP_RESULT_CODE.equals(signInfo.getSignResultCode())
                && "Y".equals(fidoSignSkipYn)
                && (ConnectType.DIGITAL_CERT.isEquals(connectType) || ConnectType.FIN_CERT_BIO.isEquals(connectType))) {
            if (log.isDebugEnabled()) {
                log.debug("전자서명 스킵 처리(간편로그인)");
            }
            // sessionManager.removeGlobalValue("fidoSignSkipYn");
            return;
        }

        // 로컬/개발환경 스킵 처리
        if (SignConstants.SIGN_SKIP_RESULT_CODE.equals(signInfo.getSignResultCode())
                && (RunMode.LOCAL.equals(RuntimeContext.getRunMode())
                        || RunMode.UT.equals(RuntimeContext.getRunMode()))) {
            if (log.isDebugEnabled()) {
                log.debug("전자서명 스킵 처리(로컬/개발 스킵)");
            }
            return;
        }

        // 서명 및 인증서 검증 실행
        verifyCertificateSign(signVerifyInfo, signInfo);

        try {
            // 서명데이터와 실제데이터 값 일치여부 검증
            verifyCompareSignData(messageObject, signVerifyInfo, signInfo);
        } catch (Exception e) {
            throw e;
        }

        if (signVerifyInfo.getSignVerifyType() != SignVerifyType.VERIFY_N_SAVE) {
            if (log.isDebugEnabled()) {
                log.debug("SignComponent > verify - 전자서명 저장 비대상 거래");
            }
            return;
        }

        if (sessionManager.isLogin()
        // || "D756".equals(signVerifyInfo.getInClassCd()) // TOBE 비로그인 기본계좌변경 업무 삭제
        ) {
            // 전자서명 저장 실행
            saveSignData(signVerifyInfo, signInfo);
        }

        // if("D47R".equals(in_class_code) || "D748".equals(in_class_code)) {
        // //D47R : NTB,ETB 순신규 타행계좌 결제계좌로 처리시 추가 전자서명 프로세스 호출
        // //D748 : 카드론 전자서명데이터 저장 처리
        // result_saveibSignData = signService.saveibSignData2(ctx, sendData,
        // Integer.parseInt(SIGNDATA_CHECK_CNT));
        // }

        return;
    }

    /**
     * 서명 및 인증서 검증
     * 
     * @param messageObject
     * @param signVerifyInfo
     * @param signInfo
     */
    private void verifyCertificateSign(SignVerifyInfo signVerifyInfo, SignInfo signInfo) {
        // ServiceContext
        IServiceContext sCtx = ServiceContextHolder.getContext();

        // 서명 검증 완료 여부
        String signVerifyCompleteYn = StringUtils
                .defaultIfBlank(sCtx.attribute(SignConstants.SIGN_VERIFY_COMPLETE_YN, String.class), "N");

        if (log.isDebugEnabled()) {
            log.debug("SignComponent > verifyCertificateSign - signVerifyCompleteYn(전자서명 검증 완료여부) : [{}]",
                    signVerifyCompleteYn);
        }

        if ("Y".equals(signVerifyCompleteYn)) {
            // 검증 완료하였을시 스킵
            return;
        }

        try {
            if (signInfo == null) {
                throw new PRCServiceException("MA3CMM0002", "전자서명 데이터가 존재하지 않습니다.");
            }

            // 전자서명 타입 체크
            SignType signType = SignType.fromCode(StringUtils.defaultString(signInfo.getSignedType()));
            SignActionType signActionType = signVerifyInfo.getSignActionType();

            // 서명데이터
            String signedData = StringUtils.defaultString(signInfo.getSignedData());

            // 디지털인증서는 서명데이터를 FIDO 서버에 서명요청 결과 확인 요청을 날리는것으로 검증하면서 가져옴
            if (SignType.DIGITAL_CERT != signType && signedData.isEmpty()) {
                throw new PRCServiceException("MA3CMM0002", "전자서명 데이터가 존재하지 않습니다.");
            }

            // 검증타입과 실행된 서명타입 일치 여부 확인
            switch (signType) {
                case SignType.JOINT_CERT:
                    // TODO: 로컬테스트
                    // if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
                    // sessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "0000");
                    // sessionManager.setGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME,
                    // StringUtils.defaultString(signInfo.getSignOrgData()));
                    // sessionManager.setGlobalValue(PRCSharedConstants.SIGN_DATA_NAME,
                    // StringUtils.defaultString(signInfo.getSignedData()));
                    // sCtx.setAttribute(SignConstants.SIGN_VERIFY_COMPLETE_YN, "Y");

                    // if (log.isDebugEnabled()) {
                    // log.debug("로컬 환경 강제 서명 데이터 검증 완료 처리");
                    // }
                    // return;
                    // }
                    certVerifyComponent.verify(signActionType.getCode(), signedData, signInfo.getVidRandom());
                    break;
                case SignType.DIGITAL_CERT:
                    checkDigitalCertSignResult();
                    break;
                case SignType.FIN_CERT_PIN:
                case SignType.FIN_CERT_BIO:
                    finCertVerifyComponent.verify(signActionType.getCode(), signedData);
                    break;
                case SignType.FIN_TECH_TOSS:
                    // 서명원문
                    String viewOrgData = StringUtils.defaultString(signInfo.getSignOrgData());

                    HashMap<String, Object> resultMap = finTechCertVerifyComponent.signVerify(signActionType.getCode(),
                            viewOrgData, signedData);

                    if ("999".equals(StringUtils.defaultString((String) resultMap.get("resultCode")))) {
                        sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME);
                        sessionManager.removeGlobalValue(PRCSharedConstants.SIGN_DATA_NAME);

                        if (sessionManager.isLogin()) {
                            // TODO: 강제 로그아웃 처리???
                            sessionManager.logout();
                        }

                        PRCServiceException customException = new PRCServiceException("MA3CMMVPCG_0012",
                                "인증서 정보가 변경되어 인증에 실패했습니다.<br />“인증센터 > 핀테크인증서 이용 등록” 메뉴에서 인증서를 재등록 하시거나, 다른 방식으로 로그인 후 이용할 수 있습니다.<br />확인을 누르시면 로그인 화면으로 이동합니다.");
                        customException.setNextPage("LGNADV000"); // TODO: TOBE 로그인? 프리로그인? (LGNMTH000/MA3LGNLIN001)
                        throw customException;
                    }

                    break;
                default:
                    throw new PRCServiceException("MA3CMM0001", "전달된 전자서명 처리 중 오류가 발생하였습니다.");
            }

            // 서명 검증 결과코드 체크
            String signSuccessCode = StringUtils
                    .defaultString(sessionManager.getGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, String.class));

            if (
            // TODO: TOBE REAL_TRAN 값 없음 확인 필요
            // "Y".equalsIgnoreCase(StringUtils.nvl(request.getParameter("REAL_TRAN")).toUpperCase(Locale.ENGLISH))
            (!"0000".equalsIgnoreCase(signSuccessCode))) {
                throw new PRCServiceException("MA3CMM0003", "전달된 전자서명 검증에 실패하였습니다.");
            }

        } catch (PRCServiceException e) {
            if (log.isErrorEnabled()) {
                log.error("SignComponent > execute - PRCServiceException : [{}] , {}",
                        e.getMessage(), e);
            }
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("SignComponent > execute - Exception : [{}] , {}",
                        e.getMessage(), e);
            }
            e.printStackTrace();
            throw new PRCServiceException("MA3CMM0001", "전달된 전자서명 처리 중 오류가 발생하였습니다.");
        }

        // 전자서명 검증 완료 여부
        sCtx.setAttribute(SignConstants.SIGN_VERIFY_COMPLETE_YN, "Y");
        return;
    }

    /**
     * 디지털인증서 서명 결과 확인 요청
     */
    private void checkDigitalCertSignResult() {

        // fido 파라미터 객체 생성
        JSONObject fidoParamJson = new JSONObject();

        // 서명시 요청한 거래ID 설정
        String fidoSvcTrId = StringUtils.defaultString(sessionManager.getGlobalValue("FIDO_SVCTR_ID", String.class));
        fidoParamJson.put("svcTrId", fidoSvcTrId);

        if (log.isDebugEnabled()) {
            log.debug("SignComponent > checkDigitalCertSignResult - fidoParamJson : [{}]", fidoParamJson.toString());
        }

        // FIDO 인증 결과 확인
        Map<String, Object> fidoResultMap = fidoHttpComponent.trResultConfirm(fidoParamJson);

        if (log.isDebugEnabled()) {
            log.debug("SignComponent > checkDigitalCertSignResult - fidoResultMap : [{}]", fidoResultMap.toString());
        }

        String signedData = StringUtils.defaultString((String) fidoResultMap.get("signedData"));

        if ("000".equals(StringUtils.defaultString((String) fidoResultMap.get("resultCode")))) {
            // 전자서명데이터
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_DATA_NAME, signedData);
            // 전자서명 정상응답코드
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "0000");
        } else {
            // 전자서명 오류응답코드
            sessionManager.setGlobalValue(PRCSharedConstants.SIGN_SUCCESS_CODE, "9999");
        }
    }

    /**
     * 서명 데이터와 거래 데이터 값 비교 검증
     * 
     * @param messageObject
     * @param signVerifyInfo
     * @param signInfo
     */
    private void verifyCompareSignData(Object messageObject, SignVerifyInfo signVerifyInfo, SignInfo signInfo) {
        // ServiceContext
        IServiceContext sCtx = ServiceContextHolder.getContext();
        // 서명 검증 완료 여부
        String signVerifyCompleteYn = sCtx.attribute(SignConstants.SIGN_VERIFY_COMPLETE_YN, String.class);
        // 다건거래 서명검증 카운트
        Integer signCompareCount = sCtx.attribute(SignConstants.SIGN_COMPARE_VERIFY_COUNT_KEY, Integer.class);

        try {
            if (signCompareCount == null || signCompareCount < 1) {
                signCompareCount = 1;
            }

            if (!"Y".equals(signVerifyCompleteYn)) {
                throw new PRCServiceException("MA3CMM0003", "전달된 전자서명 검증에 실패하였습니다.");
            }

            // 인증서 검증 완료 후 설정된 서명 원문
            String verifiedOrgData = StringUtils
                    .defaultString(sessionManager.getGlobalValue(PRCSharedConstants.SIGN_ORG_DATA_NAME, String.class));
            // 화면에서 받아온 서명 데이터에서 원문 뽑은 값
            String viewOrgData = StringUtils.defaultString(signInfo.getSignOrgData()); // docSignData

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > verifyCompareSignData - verifiedOrgData : [{}]", verifiedOrgData);
                log.debug("SignComponent > verifyCompareSignData - viewOrgData : [{}]", viewOrgData);
            }

            // 서명데이터 검증 원문 기반 데이터
            Map<String, List<String>> verifiedOrgDataMap = SignUtils.getCompareMapFromOrgString(verifiedOrgData);
            // 화면에서 올려준 원문데이터
            Map<String, List<String>> viewOrgDataMap = SignUtils.getCompareMapFromOrgString(viewOrgData);
            // 거래 데이터를 파싱한 데이터
            Map<String, List<String>> transCompareDataMap = SignUtils.getCompareMapFromObject(messageObject,
                    signVerifyInfo);

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > verifyCompareSignData > verifiedOrgDataMap : [{}]",
                        verifiedOrgDataMap == null ? "null" : verifiedOrgDataMap.toString());
                log.debug("SignComponent > verifyCompareSignData > viewOrgDataMap : [{}]",
                        viewOrgDataMap == null ? "null" : viewOrgDataMap.toString());
                log.debug("SignComponent > verifyCompareSignData > transCompareDataMap : [{}]",
                        transCompareDataMap == null ? "null" : transCompareDataMap.toString());
            }

            if (verifiedOrgDataMap == null || viewOrgDataMap == null || transCompareDataMap == null) {
                throw new PRCServiceException("MA3CMM0004", "전자서명 데이터가 존재하지 않습니다.");
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "SignComponent > verifyCompareSignData > verifiedOrgDataMap_size : [{}], viewOrgDataMap_size : [{}], transCompareDataMap_size : [{}]",
                        verifiedOrgDataMap.size(),
                        viewOrgDataMap.size(),
                        transCompareDataMap.size());
            }

            Integer totalSignDataCount = SignUtils.getTotalSignDataCount(verifiedOrgDataMap);

            if (log.isDebugEnabled()) {
                log.debug(
                        "SignComponent > verifyCompareSignData - signCompareCount : [{}] / totalSignDataCount : [{}]",
                        signCompareCount, totalSignDataCount);
            }

            for (Map.Entry<String, List<String>> entry : verifiedOrgDataMap.entrySet()) {
                String key = entry.getKey();

                // 서명키중 제외키 검증 PASS 처리
                if (SignConstants.excludeSignDataKeySet.stream()
                        .anyMatch(exclude -> key.toLowerCase().contains(exclude))) {
                    continue;
                }

                List<String> list1 = verifiedOrgDataMap.get(key);
                List<String> list2 = viewOrgDataMap.get(key);
                List<String> list3 = transCompareDataMap.get(key);

                // 해당 서명키의 데이터 존재여부 확인
                if (list1 == null || list2 == null || list3 == null) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "SignComponent > verifyCompareSignData > key : [{}], list1 : [{}], list2 : [{}] list3: [{}]",
                                key,
                                list1 == null ? "null" : list1.toString(),
                                list2 == null ? "null" : list2.toString(),
                                list3 == null ? "null" : list3.toString());
                    }

                    if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())
                            || RunMode.UT.equals(RuntimeContext.getRunMode())) {
                        PRCServiceException e = new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                        e.setErrorGuideMessage("전자서명 데이터 검증에 실패하였습니다. \n"
                                + " [" + key + "] 데이터가 존재하지 않습니다. ");
                        throw e;
                    }

                    throw new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                }

                if (log.isDebugEnabled()) {
                    log.debug(
                            "SignComponent > verifyCompareSignData > key : [{}], list1({}) : [{}], list2({}) : [{}] list3: [{}]",
                            key, list1.size(), list1.toString(), list2.size(), list2.toString(), list3.toString());
                }

                // 검증원문과 화면원문의 서명데이터 수 일치 여부 확인
                if (list1.size() != list2.size()) {
                    if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())
                            || RunMode.UT.equals(RuntimeContext.getRunMode())) {
                        PRCServiceException e = new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                        e.setErrorGuideMessage("전자서명 데이터 검증에 실패하였습니다. \n"
                                + "[" + key + "] 의 데이터 개수가 일치하지 않습니다. \n"
                                + "(실제서명데이터수 : [" + list1.size() + "] | 요청서명데이터수 : [" + list2.size() + "])");
                        throw e;
                    }

                    throw new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                }

                // 검증원문과 화면원문의 서명데이터 값비교
                IntStream.range(0, list1.size())
                        .filter(i -> {
                            String value1 = list1.get(i);
                            String value2;

                            try {
                                value2 = URLDecoder.decode(list2.get(i), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                value2 = "";
                            }

                            return !StringUtils.equalsAny(value1, value2);
                        })
                        .findFirst()
                        .ifPresent(i -> {

                            String decoded_list2_get;
                            try {
                                decoded_list2_get = URLDecoder.decode(list2.get(i), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                decoded_list2_get = "";
                            }

                            if (log.isErrorEnabled()) {
                                log.error(
                                        "SignComponent > verifyCompareSignData > 서명원문 데이터 불일치 - key : [{}], i : [{}], list1_value : [{}], list2_value : [{}]",
                                        key, i, list1.get(i),
                                        decoded_list2_get);
                            }

                            if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())
                                    || RunMode.UT.equals(RuntimeContext.getRunMode())) {
                                PRCServiceException e = new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                                e.setErrorGuideMessage("전자서명 데이터 검증에 실패하였습니다. \n"
                                        + "[" + key + "] 의 [" + (i + 1) + "]번째 서명 데이터가 일치하지 않습니다. \n"
                                        + "(실제서명데이터의값 : [" + list1.get(i) + "] | 요청서명데이터의값 : [" + decoded_list2_get
                                        + "])");
                                throw e;
                            }

                            throw new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                        });

                // 검증원문과 거래데이터간의 알치여부 체크 (다건 거래일 경우 반복적으로 들어오므로 몇번째 거래인지 체크하여 해당 index로 검증)
                if (!StringUtils.equalsAny(list1.get(signCompareCount - 1), list3.get(0))) {
                    if (log.isErrorEnabled()) {
                        log.error(
                                "SignComponent > verifyCompareSignData > 서명 거래 데이터 불일치 - key : [{}], signCompareCount : [{}], list1_value : [{}], list3_value : [{}]",
                                key, signCompareCount, list1.get(signCompareCount - 1), list3.get(0));
                    }

                    if (RunMode.LOCAL.equals(RuntimeContext.getRunMode())
                            || RunMode.UT.equals(RuntimeContext.getRunMode())) {
                        PRCServiceException e = new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                        e.setErrorGuideMessage("전자서명 데이터 검증에 실패하였습니다. \n"
                                + "[" + key + "] 의 [" + signCompareCount + "/" + totalSignDataCount
                                + "]번째 거래 데이터가 일치하지 않습니다. \n"
                                + "(서명데이터값 : [" + list1.get(signCompareCount - 1) + "] | 거래데이터값 : [" + list3.get(0)
                                + "])");
                        throw e;
                    }

                    throw new PRCServiceException("MA3CMM0006", "전자서명 데이터 검증에 실패하였습니다.");
                }
            }
        } catch (PRCServiceException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException("MA3CMM0007", "전자서명 데이터 검증에 실패하였습니다.", e);
        }

        sCtx.setAttribute(SignConstants.SIGN_COMPARE_VERIFY_COUNT_KEY, ++signCompareCount);
    }

    /**
     * 전자서명 저장
     * 
     * @param signVerifyInfo
     * @param signInfo
     */
    private void saveSignData(SignVerifyInfo signVerifyInfo, SignInfo signInfo) {
        // ServiceContext
        IServiceContext sCtx = ServiceContextHolder.getContext();

        // 전자서명 저장 완료 여부
        String signSaveCompleteYn = StringUtils
                .defaultIfBlank(sCtx.attribute(SignConstants.SIGN_SAVE_COMPLETE_YN, String.class), "N");

        String signedType = StringUtils.defaultString(signInfo.getSignedType());
        SignType signType = SignType.fromCode(signedType);
        // String connectType = signVerifyInfo.getConnectType();

        if (log.isDebugEnabled()) {
            log.debug("SignComponent > saveSignData - signSaveCompleteYn(전자서명 저장 완료여부) : [{}], signType(서명 타입) : [{}]",
                    signSaveCompleteYn, signType);
        }

        // 공동/디지털 인증서는 반복거래에 대해 전자서명 저장 1번만 처리
        if ("Y".equalsIgnoreCase(signSaveCompleteYn)
                && (SignType.JOINT_CERT == signType || SignType.DIGITAL_CERT == signType)) {
            return;
        }

        String saveType = PropertiesUtils.getString("SDTA_TYPE");
        if (saveType == null || saveType.isEmpty()) {
            throw new PRCServiceException("MA3CMM0010", "전자서명 데이터 저장 구분이 없습니다.");
        }

        // TODO: default 제거
        String userId = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("UserID", String.class), "FIRST999");
        String userName = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("CustName", String.class), "테스트이름");
        String juminNo = StringUtils.defaultIfEmpty(SessionUtils.getSessionValue("PerBusNo", String.class),
                "9999999999999");

        String gubunCode = "";
        String sysName = "";

        if (PRCSharedUtils.isIB()) {
            gubunCode = SignConstants.SIGN_IB_GUBUN_CODE;
            sysName = SignConstants.SIGN_IB_SYS_NAME;

            if (RunMode.UT.equals(RuntimeContext.getRunMode())) {
                sysName += "UT";
            } else if (RunMode.SIT.equals(RuntimeContext.getRunMode())) {
                sysName += "SIT";
            } else if (RunMode.UAT.equals(RuntimeContext.getRunMode())) {
                sysName += "UAT";
            }

            if (signType == SignType.FIN_CERT_PIN) {
                sysName += "_B";
            }

        } else {
            gubunCode = SignConstants.SIGN_MB_GUBUN_CODE;
            sysName = SignConstants.SIGN_MB_SYS_NAME;

            if (signType == SignType.FIN_CERT_PIN
                    || signType == SignType.FIN_CERT_BIO
                    || signType == SignType.FIN_TECH_TOSS) {
                sysName += "_" + signedType;
            }
        }

        String txCode = StringUtils.defaultString(signVerifyInfo.getInClassCd());
        String regSite = sCtx.request().getRequestURI();

        // 서명 저장 데이터
        SignInfoParameter parameter = SignInfoParameter.builder()
                .userId(userId)
                .userName(userName)
                .regDate(DateUtils.getCurrentDate("yyyyMMddHHmmssSSS"))
                .juminNo(juminNo)
                .sysIp(IpUtils.getLocalIp())
                .sysName(sysName)
                .gubunCode(gubunCode)
                .txCode(txCode)
                .pageLang("K") // TODO: (ASIS "K" 하드코딩)
                .regSite(regSite)
                .delFlag("0")
                .regDay(DateUtils.getCurrentDate("yyyyMMdd"))
                .build();

        // 서명데이터
        String signedData = sessionManager.getGlobalValue(PRCSharedConstants.SIGN_DATA_NAME, String.class);

        try {
            // 서명 암호화
            byte[] encSign = SignUtils.encrypt(signType, signedData);
            parameter.setEncSign(encSign);

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignData - parameter : [{}]", parameter);
            }
            // 전자서명 저장 실행
            signTransactionDao.insertSignInfo(parameter);
            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignData > insertSignInfo : success");
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignData > insertSignInfo : fail");
            }

            // DB 저장 실패시 서명 저장을 위한 파일저장
            saveSignFile(parameter, signType, signedData);
            return;
        }

        sCtx.setAttribute(SignConstants.SIGN_SAVE_COMPLETE_YN, "Y");
        return;
    }

    /**
     * 전자서명 파일 저장
     * 
     * @param signVerifyInfo
     * @param signInfo
     */
    private synchronized void saveSignFile(SignInfoParameter signParameter, SignType signType, String signedData) {
        if (log.isDebugEnabled()) {
            log.debug("SignComponent > saveSignFile > signParameter : [{}]",
                    signParameter.toString());
        }

        try {
            // 서명 암호화
            byte[] encSign = SignUtils.encrypt(signType, signedData);
            signParameter.setEncSign(encSign);

            String currentDateTime = DateUtils.getCurrentTime(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String saveFileDir = PropertiesUtils.getString("SIGN_DIR");
            String saveFileName = SignConstants.SIGN_SAVE_FILE_NAME + currentDateTime;

            Path fileDirPath = Path.of(saveFileDir);
            if (!Files.exists(fileDirPath)) {
                if (log.isDebugEnabled()) {
                    log.debug("SignComponent > saveSignFile > createDirectories - fileDirPath : [{}]",
                            fileDirPath.toAbsolutePath().toString());
                }
                Files.createDirectories(fileDirPath);
            }

            Path filePath = fileDirPath.resolve(saveFileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignFile - filePath : [{}]",
                        filePath.toString());
            }

            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream((Files.newOutputStream(filePath))))) {
                dos.writeUTF(currentDateTime);
                dos.writeUTF(signParameter.getUserId());
                dos.writeUTF(signParameter.getTxCode());
                dos.writeUTF(signParameter.getRegSite());
                dos.writeUTF(signParameter.getJuminNo());
                dos.writeUTF(signParameter.getSysIp());
                dos.writeUTF(signParameter.getSysName());
                dos.writeUTF(signParameter.getUserName());
                dos.writeUTF(signParameter.getGubunCode());
                dos.writeUTF(signParameter.getPageLang());
                dos.writeUTF(signParameter.getRegDay());
                dos.writeInt(signParameter.getEncSign().length);
                dos.write(signParameter.getEncSign());

                if (log.isDebugEnabled()) {
                    log.debug("SignComponent > saveSignFile - Success");
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            // 파일 저장시 실패시 Exception 처리
            e.printStackTrace();

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignFile - Fail");
            }

            // 파일저장 실패 로그 저장
            saveSignFileLog(signParameter, signType, signedData);
        }
    }

    /**
     * 전자서명 파일 저장 실패 로그 파일 저장
     * 
     * @param signVerifyInfo
     * @param signInfo
     */
    private synchronized void saveSignFileLog(SignInfoParameter signParameter, SignType signType, String signedData) {
        if (log.isDebugEnabled()) {
            log.debug("SignComponent > saveSignFile > signParameter : [{}]",
                    signParameter.toString());
        }

        try {
            // 서명 암호화
            byte[] encSign = SignUtils.encrypt(signType, signedData);
            signParameter.setEncSign(encSign);

            String currentDateTime = DateUtils.getCurrentTime(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String saveLogDir = PropertiesUtils.getString("SIGN_DIR");
            String saveLogName = SignConstants.SIGN_SAVE_FILE_LOG_NAME + currentDateTime;

            Path logDirPath = Path.of(saveLogDir);
            Path filePath = logDirPath.resolve(saveLogName);

            try (PrintStream print = new PrintStream(
                    new BufferedOutputStream(
                            (Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND))))) {

                String logData = new StringBuffer()
                        .append(currentDateTime)
                        .append("$")
                        .append(signParameter.getUserId())
                        .append("$")
                        .append(signParameter.getTxCode())
                        .append("$")
                        .append(signParameter.getRegSite())
                        .append("$")
                        .append(signParameter.getJuminNo())
                        .append("$")
                        .append(signParameter.getSysIp())
                        .append("$")
                        .append(signParameter.getSysName())
                        .append("$")
                        .append(signParameter.getUserName())
                        .append("$")
                        .append(signParameter.getGubunCode())
                        .append("$")
                        .append(signParameter.getPageLang())
                        .append("$")
                        .append(signParameter.getRegDay())
                        .append("$")
                        .append(new String(signParameter.getEncSign()))
                        .toString();

                print.println(logData);

                if (log.isDebugEnabled()) {
                    log.debug("SignComponent > saveSignFileLog - Success");
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            // 파일 저장시 실패시 Exception 처리
            e.printStackTrace();

            if (log.isDebugEnabled()) {
                log.debug("SignComponent > saveSignFileLog - Fail");
            }
        }
    }
}
