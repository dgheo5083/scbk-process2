package com.scbank.process.api.svc.common.service.verification;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.VerificationFDSComponent;
import com.scbank.process.api.svc.common.components.VerificationTokensComponent;
import com.scbank.process.api.svc.common.dao.SaoOtpChargeDao;
import com.scbank.process.api.svc.common.dao.TmbCampContentDao;
import com.scbank.process.api.svc.common.dao.dto.InsertTmbCampContents01Parameter;
import com.scbank.process.api.svc.common.dao.dto.InsertTmbPushMsgCamp01Parameter;
import com.scbank.process.api.svc.common.dao.dto.MotpRegistrationInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.MotpRegistrationInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectTmbObjUsrMgtInfoResult;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensActivateRequest;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensActivateResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensCheckAbroadResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSelectMotpDeviceStatusRequest;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSelectMotpDeviceStatusResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensSendMotpPushResponse;
import com.scbank.process.api.svc.common.service.verification.dto.tokens.VerificationTokensTestRequest;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.components.verification.dto.VerificationVerifyInfo;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "인증 - 보안매체", url = "/verification/tokens")
public class VerificationTokensService {

    private final HostClient hostClient;

    private final ISessionContextManager sessionManager;

    private final VerificationTokensComponent verificationTokensComponent;

    private final VerificationFDSComponent verificationFDSComponent;

    private final TmbCampContentDao tmbCampContentDao;

    /**
     * MOTP 정보 DAO
     */
    private final SaoOtpChargeDao saoOtpChargeDao;

    private final static double BANK_SIGN_LIMIT_AMOUNT = 5000000; // 뱅크사인 이체거래 체크 금액
    private final static double MOTP_LIMIT_AMOUNT = 2000000; // MOTP 로그인 대상 이체거래 체크 금액

    private final static double FINCERT_BIO_ONETIME_EXCEPTION_LIMIT_AMOUNT = 100000000; // 이체한도 예외승인 1회한도체크
    private final static double FINCERT_BIO_DALIY_EXCEPTION_LIMIT_AMOUNT = 500000000; // 이체한도 예외승인 일일한도체크

    private final SecureDataComponent secureData;

    @ServiceEndpoint(url = "/test", name = "보안매체 검증테스트")
    public void test(IServiceContext serviceContext, VerificationTokensTestRequest request) {

        VerificationVerifyInfo verifyInfo = new VerificationVerifyInfo();

        secureData.verifyVerification();

    }

    @ServiceEndpoint(url = "/activate", name = "보안매체 초기화")
    public VerificationTokensActivateResponse activate(IServiceContext serviceContext,
            VerificationTokensActivateRequest request) {

        VerificationTokensActivateResponse response = new VerificationTokensActivateResponse();

        /* 1. 고객정보 설정 ##### (S) */
        String connectType = SessionUtils.getSessionValue("ConnectType");
        String transPWUseYN = SessionUtils.getSessionValue("TransPWUseYN");
        String safeCardState = SessionUtils.getSessionValue("SafeCardState");
        String safeCardKind = SessionUtils.getSessionValue("SafeCardKind");
        String smartOTP = SessionUtils.getSessionValue("SmartOTP");
        String safeCardIndex1 = SessionUtils.getSessionValue("SafeCardINDEX");
        String safeCardIndex2 = SessionUtils.getSessionValue("SafeCardINDEX2");
        String safeCardSeq1 = SessionUtils.getSessionValue("SafeCardSeq1");
        String safeCardSeq2 = SessionUtils.getSessionValue("SafeCardSeq2");
        String safeCardSeq3 = SessionUtils.getSessionValue("SafeCardSeq3");

        log.debug("HDG Debug VerificationTokensService activate safeCardKind1 > " + safeCardKind);
        log.debug("HDG Debug VerificationTokensService activate safeCardKind2 > "
                + sessionManager.getLoginValue("SafeCardKind", String.class));

        String telNo1 = SessionUtils.getSessionValue("TeleOne");
        String telNo2 = SessionUtils.getSessionValue("TeleTwo");
        String telNo3 = SessionUtils.getSessionValue("TeleThree");
        /* 1. 고객정보 설정 ##### (E) */

        /* 2. 추가인증 세션 초기화 ##### (S) */
        sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", ""); // 추가인증 대상여부(Y/N/'')
        sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_TYPE", ""); // 추가인증 인증Type
        /* 2. 추가인증 세션 초기화 ##### (E) */

        // 3. 내 계좌 송금인 경우 ( 예약, 즉시, 지연이체 ) - 추가인증 & 보안매체 pass
        if (verificationTokensComponent.isMyAcctTran(request.getTranType())) {
            sessionManager.setGlobalValue("myAcctSkipYn", "Y");
            response.setTokensYn("N");
            response.setAdditionalYn("N");

            log.debug("HDG Debug VerificationTokensService activate myAcctSkipYn > {}",
                    sessionManager.getGlobalValue("myAcctSkipYn", String.class));
        } else {

            // 3-1. FDS 차단 체크
            if (verificationFDSComponent.isTranBlock()) {
                throw new PRCServiceException("PRCCMM0011",
                        "안전한 인터넷뱅킹 거래를 위해 이체거래가 제한되었습니다. 고객센터로 문의하여 주시기 바랍니다.(1588-1599)");
            }

            /* 3-2. 추가인증 여부 ##### (S) */
            double todayTranAmt = Double.parseDouble(request.getTodayTranAmt());
            boolean isAdditional = verificationTokensComponent.isAdditional(request.getAdditionalYn(),
                    request.getTranType(), todayTranAmt);

            log.debug("HDG Debug VerificationTokensService activate isAdditional >> " + isAdditional);

            /* 3-2-1. FDS 신분증촬영 체크 ##### */
            if (verificationFDSComponent.isIDCapture()) {

                if (PRCSharedUtils.isIB()) {
                    // 추가인증 신분증촬영 대상여부
                    sessionManager.setGlobalValue("ADD_AUTH_ID_CAPTURE_FLAG", "Y");
                    // 추가인증 대상여부 갱신
                    sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");

                    // 신분증촬영으로 거래타입 변경
                    request.setAdditionalType("P");

                    isAdditional = true;
                } else {
                    // TODO: PRCCMM0072 영문 번역 필요함.
                    throw new PRCServiceException("PRCCMM0072",
                            "안전한 금융거래를 위해 신분증 제출이 필요해요. 모바일뱅킹에서 신분증 제출 후 다시 이용해 주세요.");
                }

            }

            /* 3-2-2. 추가인증 실행 or FDS 상태가 출금정지인 경우 체크 ##### */
            if ((isAdditional || verificationFDSComponent.isWdrawFreeze())
                    && StringUtils.isNotEmpty(request.getTranType())) {

                // 추가인증 대상여부 갱신
                sessionManager.setGlobalValue("ADD_AUTH_SUCCESS_FLAG", "N");

                if (StringUtils.isEmpty(request.getAdditionalType())) {
                    response.setAdditionalType("A:D:F");
                } else {
                    response.setAdditionalType(request.getAdditionalType());
                }

                // 출금정지 상태를 알수있는 코드(03이면 출금정치)
                response.setRspActnMethCd(verificationFDSComponent.getRspActnMethCd());

                isAdditional = true;
            }

            response.setAdditionalYn(isAdditional ? "Y" : "N");
            /* 3-2. 추가인증 여부 ##### (E) */

            /* 3-3. 보안매체 사용여부지정에 따른 설정 ##### (S) */
            if (StringUtils.isNotBlank(request.getTokensYn())) {
                response.setTokensYn(request.getTokensYn());
            } else {

                // 9: 디지털인증, C: 금융인증서(생체인증)
                if ("9".equals(connectType) || "C".equals(connectType)) {

                    String fidoRejectMenu = CodeUtils.getCodeValue("FIDO_REJECT_MENU", "REJECT");

                    String menuId = PRCSharedUtils.getMenuId();

                    if (!"".equals(menuId) && fidoRejectMenu.indexOf(menuId) > -1) {
                        log.info("HDG Debug VerificationTokensService activate 디지털인증서, 금융인증서(생체인증) 보안매체 SKIP 제외 대상메뉴["
                                + menuId + "]");
                        response.setTokensYn("Y");
                    } else {
                        log.info("HDG Debug VerificationTokensService activate 디지털인증서, 금융인증서(생체인증) 보안매체 SKIP");
                        if (request.getTranType().indexOf("TRAN_") > -1 && todayTranAmt <= BANK_SIGN_LIMIT_AMOUNT) {
                            sessionManager.setGlobalValue("fidoSignSkipYn", "Y");
                            response.setFidoSignSkipYn("Y");
                        }

                        if ("C".equals(connectType)) {
                            if (FINCERT_BIO_DALIY_EXCEPTION_LIMIT_AMOUNT < todayTranAmt) {
                                response.setTokensYn("Y");
                                log.info("금융인증서 생체인증 이체 일일 이체 한도 이상 이체로 인하여 보안매체+생체인증받아야됨.");
                            }

                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> tranInfoList = sessionManager.getGlobalValue(
                                    "addCertTranInfoList",
                                    List.class);

                            if (tranInfoList != null) {
                                log.debug(
                                        "HDG Debug VerificationTokensService activate 금융인증서 생체인증 이체 tranInfoList: [{}]",
                                        tranInfoList.toString());

                                for (int i = 0; i < tranInfoList.size(); i++) {
                                    Map<String, Object> transferInfo = tranInfoList.get(0);
                                    log.debug("HDG Debug VerificationTokensService activate 금융인증서 생체인증 이체 transferInfo:"
                                            + transferInfo.toString());
                                    String RSgAmt = StringUtils.defaultIfEmpty((String) transferInfo.get("RSgAmt"), "");
                                    double dRSgAmt = 0;
                                    if (!"".equals(RSgAmt)) {
                                        dRSgAmt = Double.parseDouble(RSgAmt);
                                    }
                                    if (FINCERT_BIO_ONETIME_EXCEPTION_LIMIT_AMOUNT < dRSgAmt) { // 일일이체 체크
                                        response.setTokensYn("Y");
                                        log.debug(
                                                "HDG Debug VerificationTokensService activate 금융인증서 생체인증 이체 1회 이체 한도 이상 이체로 인하여 보안매체+생체인증받아야됨.");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // 보안매체 설정여부 기본값 설정
                    response.setTokensYn("Y");
                }

                // MOTP 로그인 (이체업무 보안매체 Skip 체크)
                if (request.getTranType().indexOf("TRAN_") > -1 && "G".equals(connectType)) {

                    log.debug("HDG Debug VerificationTokensService activate Transfer MOTP [{}], [{}]",
                            request.getTranType(), connectType);
                    log.debug(
                            "HDG Debug VerificationTokensService activate Transfer MOTP todayTranAmt [{}], MOTP_LIMIT_AMOUNT [{}]",
                            todayTranAmt,
                            MOTP_LIMIT_AMOUNT);

                    if (todayTranAmt <= MOTP_LIMIT_AMOUNT) {

                        sessionManager.setGlobalValue("motpSignSkipYn", "Y");
                        response.setMotpSignSkipYn("Y");
                        response.setTokensYn("N");
                    } else {
                        sessionManager.setGlobalValue("motpSignSkipYn", "N");
                        response.setMotpSignSkipYn("N");
                        response.setTokensYn("Y");
                    }

                    // TODO: 필요없는듯. SIT까지 문제 없으면 삭제처리함.
                    // sessionManager.setGlobalValue("MOTP_TRAN_CHECK",
                    // "Y".equals(response.getTokensYn()) ? "N" : "Y");

                }

            }
            /* 3-3. 보안매체 사용여부지정에 따른 설정 ##### (E) */

        }

        if ("Y".equals(response.getTokensYn())) {

            if (!"0".equals(safeCardState)) {

                // 보안카드
                if ("1".equals(safeCardKind)) {
                    // 기본 입력값 체크
                    if ("Y".equals(request.getSafeCardSeqYn())
                            && ("".equals(safeCardSeq1) || "".equals(safeCardSeq2) || "".equals(safeCardSeq3))) {
                        throw new PRCServiceException("PRCCMM0037", "보안카드 지시번호가 누락되었습니다.");
                    }

                    if ("Y".equals(request.getSafeCardSeqYn())) {
                        response.setSafeCardSeq1(safeCardSeq1);
                        response.setSafeCardSeq2(safeCardSeq2);
                        response.setSafeCardSeq3(safeCardSeq3);
                        response.setSafeCardIndex1(safeCardIndex1);
                        response.setSafeCardIndex2(safeCardIndex2);
                        response.setTokensType("11");
                    } else {
                        response.setSafeCardIndex1(safeCardIndex1);
                        response.setSafeCardIndex2(safeCardIndex2);
                        response.setTokensType("10");
                    }
                } else {

                    // 구 OTP
                    if ("2".equals(safeCardKind)) {
                        response.setSafeCardIndex1(safeCardIndex1);
                        response.setTokensType("20");
                    } else if ("3".equals(safeCardKind)) {
                        // 스마트OTP ( 값이 숫자형인 경우 )
                        if (!"".equals(smartOTP) && "0123456789".indexOf(smartOTP) > -1) {
                            response.setTokensType("31");
                        }
                        // 모바일 OTP
                        else if ("M".equals(smartOTP)) {
                            response.setTokensType("32");
                        }
                        // 일반 OTP
                        else {
                            response.setTokensType("30");
                        }
                    } else {
                        response.setTokensType("30");
                    }

                }

            } else {
                throw new PRCServiceException("PRCCMM0051",
                        "고객님의 보안카드(혹은 OTP)는 폐기된 상태로, 조회 메뉴만 사용 가능합니다. 돈보내기 등 거래 메뉴를 사용하시기 위해서는 가까운 영업점을 방문하시어 보안카드를 재발급 받으시기 바랍니다.");
            }

        }

        // 이체비밀번호 생성
        // 이체 비밀번호 영역 생성
        if (!"1".equals(transPWUseYN)) {
            response.setTranPwdYn("Y");
        }

        response.setTelNo1(telNo1);
        response.setTelNo2(telNo2);
        response.setTelNo3(telNo3);

        log.debug("HDG Debug VerificationTokensService activate response data >> {}", response.toString());

        return response;
    }

    @ServiceEndpoint(url = "/selectMotpDeviceStatus", name = "MOTP 기기상태조회")
    public VerificationTokensSelectMotpDeviceStatusResponse selectMotpDeviceStatus(IServiceContext serviceContext,
            VerificationTokensSelectMotpDeviceStatusRequest request) {

        VerificationTokensSelectMotpDeviceStatusResponse response = new VerificationTokensSelectMotpDeviceStatusResponse();

        String userId = sessionManager.getGlobalValue("UserID", String.class);
        if (sessionManager.isLogin()) {
            userId = sessionManager.getLoginValue("UserID", String.class);
        }

        MotpRegistrationInfoParameter parameter = MotpRegistrationInfoParameter.builder()
                .userId(StringUtils.defaultString(userId))
                .tokenId(request.getDeviceId())
                .build();

        MotpRegistrationInfoResult result = saoOtpChargeDao.selectMotpRegistrationInfo(parameter);

        // 조회된 데이터 없을 시 기기변경
        if (result == null) {
            response.setMotpDeviceStatus("02");// 02: 기기변경
        }

        return response;

    }

    @ServiceEndpoint(url = "/abroadyn", name = "해외접속 여부조회")
    public VerificationTokensCheckAbroadResponse getAbroadYn(IServiceContext serviceContext) {
        VerificationTokensCheckAbroadResponse response = new VerificationTokensCheckAbroadResponse();

        response.setFlagIsAbroadYN(verificationTokensComponent.getAbroadYn());
        return response;
    }

    @ServiceEndpoint(url = "/sendMotpPush", name = "MOTP PUSH 발송")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = Exception.class)
    public VerificationTokensSendMotpPushResponse sendMotpPush(IServiceContext serviceContext) {

        VerificationTokensSendMotpPushResponse response = new VerificationTokensSendMotpPushResponse();

        String userId = sessionManager.getGlobalValue("UserID", String.class);
        if (sessionManager.isLogin()) {
            userId = sessionManager.getLoginValue("UserID", String.class);
        }

        SelectTmbObjUsrMgtInfoResult selectTmbObjUsrMgtInfoResult = tmbCampContentDao.selectTmbObjUsrMgtInfo(userId);

        if (selectTmbObjUsrMgtInfoResult == null) {
            response.setResultCd("99");
            return response;
        }

        if ("Y".equals(selectTmbObjUsrMgtInfoResult.getPushSrvcApprvlFlg())
                && StringUtils.isNotBlank(selectTmbObjUsrMgtInfoResult.getCnfrmNo())) {
            String title = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG001");
            String cnts = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG002");
            String contentsMsg = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG002");

            if (!"1".equals(selectTmbObjUsrMgtInfoResult.getOperType())) {
                cnts = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG003");
                contentsMsg = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG003");
            }

            String regId = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG004");
            String sendDepartment = CodeUtils.getCodeValue("MOTP_PUSH_MESSAGE", "MSG005");

            String seq = tmbCampContentDao.selectTmbCampContentSeq();

            InsertTmbCampContents01Parameter insertTmbCampContents01Parameter = InsertTmbCampContents01Parameter
                    .builder().campSeq(seq).title(title)
                    .cnts(cnts).webUrl("").imgUrl("").stsUrl("")
                    .popupYn("Y").stsYn("Y").contentsMsg(contentsMsg)
                    .build();

            tmbCampContentDao.insertTmbCampContents01(insertTmbCampContents01Parameter);

            InsertTmbPushMsgCamp01Parameter insertTmbPushMsgCamp01Parameter = InsertTmbPushMsgCamp01Parameter.builder()
                    .campSeq(seq).msgType("21").recvYN("N").resvDt("").retryCnt("0")
                    .testYN("N").stsCd("003").regId(regId)
                    .kind("CCS-US").sendDepartment(sendDepartment).bnkingId(userId).bandSeqNo("").build();

            tmbCampContentDao.insertTmbPushmsgCamp01(insertTmbPushMsgCamp01Parameter);
        } else {
            // PUSH 미등록 고객
            response.setResultCd("99");
        }

        return response;

    }
}
