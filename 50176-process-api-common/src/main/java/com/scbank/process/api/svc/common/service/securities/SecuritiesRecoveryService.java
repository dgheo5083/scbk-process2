package com.scbank.process.api.svc.common.service.securities;

import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D84400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D84400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H86600Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92000Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpCommon;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.common.holiday.impl.DefaultHolidayManager;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.mapper.SecuritiesRecoveryMapper;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvClosePasswordErrorClearAccountRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvClosePasswordErrorClearAccountResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvEditNewPasswordMainRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvEditNewPasswordMainResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListMyAccountsRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListMyAccountsResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListMyAccountsResponse.YOMYINF_REC;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListPasswordErrorClearAccountRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvListPasswordErrorClearAccountResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetArsInfoRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetArsInfoResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetServiceNoticeRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetServiceNoticeResponse;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetSignPageInfoRequest;
import com.scbank.process.api.svc.common.service.securities.dto.recovery.SecRcvSetSignPageInfoResponse;
import com.scbank.process.api.svc.shared.components.auth.SecureDataComponent;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.BizCommonUtils;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "계좌비밀번호 재설정", url = "/securities/recovery")
public class SecuritiesRecoveryService {

    // 세션
    private final ISessionContextManager sessionManager;

    // 전문
    private final HostClient hostClient;

    private final SecuritiesRecoveryMapper securitiesRecoveryMapper;

    private final DefaultHolidayManager defaultHolidayManager;

    private final SmsComponent smsComponent;

    // 추가인증/보안매체 검증 컴포넌트
    private final SecureDataComponent secureDataComponent;

    /**
     * 서비스 안내
     * 
     * @param ctx
     * @param input
     * @return
     */
    @ServiceEndpoint(url = "/setServiceNotice", name = "서비스 안내 [ASIS:MA3CRTAPM001_101S]", author = "허정우")
    public SecRcvSetServiceNoticeResponse setServiceNotice(IServiceContext ctx, SecRcvSetServiceNoticeRequest input) {
        log.debug("####### SecuritiesRecoveryService.setServiceNotice 진입 #######");
        SecRcvSetServiceNoticeResponse output = new SecRcvSetServiceNoticeResponse();

        String paramJsonString = StringUtils.defaultIfEmpty(input.getParamJsonString(), "");
        String SCRN_DATA_INFO = StringUtils.defaultIfEmpty(input.getScrnDataInfo(), "");
        String screenFlag = StringUtils.defaultIfEmpty(input.getScreenFlag(), "X");
        String isLoginYN = sessionManager.isLogin() ? "Y" : "N";

        sessionManager.removeGlobalValue("TRAD_NO");

        sessionManager.setGlobalValue("PRDCT_ID", "9991");
        sessionManager.setGlobalValue("PRDCT_NM", "계좌비밀번호 재설정");
        sessionManager.setGlobalValue("PRDCT_CD", "9991");
        sessionManager.setGlobalValue("BIZ_TYPE", "MPLR");

        output.setParamJsonString(paramJsonString);
        output.setScrnDataInfo(SCRN_DATA_INFO);
        output.setScreenFlag(screenFlag);
        output.setIsLoginYN(isLoginYN);

        output.setPrdctId("9991");
        output.setPrdctCd("9991");
        output.setBizType("MPLR");
        output.setPrdctNm("계좌비밀번호 재설정");

        log.debug("####### SecuritiesRecoveryService.setServiceNotice 끝 #######" + output);
        log.debug("####### SecuritiesRecoveryService.setServiceNotice 끝 #######");

        return output;
    }

    /**
     * 계좌 선택
     * 
     * @param ctx
     * @param input
     * @return
     */
    @ServiceEndpoint(url = "/listMyAccounts", name = "계좌 선택 [ASIS:MA3CRTAPM001_201S]", author = "허정우")
    public SecRcvListMyAccountsResponse listMyAccounts(IServiceContext ctx, SecRcvListMyAccountsRequest input) {
        log.debug("####### SecuritiesRecoveryService.listMyAccounts 진입 #######");

        String SCRN_DATA_INFO = StringUtils.defaultIfEmpty(input.getScrnDataInfo(), "");
        String screenFlag = StringUtils.defaultIfEmpty(input.getScreenFlag(), "");
        // String userId = "";
        String PerBusNo = "";

        String sendDate = DateUtils.getCurrentDate("yyyyMMdd");
        boolean checkHoliday = defaultHolidayManager.isHoliday(sendDate); // 휴일여부 Check
        /* 휴일 */
        String holiDay = checkHoliday ? "Y" : "N";

        if (sessionManager.isLogin()) {
            PerBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
            // userId = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID",
            // String.class), "");
        } else {
            PerBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);
            // userId =
            // StringUtils.defaultIfEmpty(SessionManager.getGlobalValue("UserID"),"FIRST999");
        }

        // 평일 : 10,20,30,61,80,85,88,90
        // 휴일 : 10,20,30

        // 보유계좌조회 전문
        // TI1IBK01_H866 (CB_IBK01_H866)
        OltpRequestOptions oltpRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");

        CbIbk01H86600Req scbkSendData = new CbIbk01H86600Req();

        // 공통부 세팅
        oltpRequestOptions.setImsTranCd("TI1IBK01");
        oltpRequestOptions.setInClassCd("H866");
        oltpRequestOptions.setSvcCd("866");
        oltpRequestOptions.setCaptureSystem("OLTP");

        scbkSendData.setUserID("FIRST999");
        scbkSendData.setTSPassword("99999999");
        // scbkSendData.setYIBIDAE(YIBIDAE); // 비대면실명승인계좌
        scbkSendData.setYIJMNO(PerBusNo);
        scbkSendData.setYIMBSINGO("Y");

        OltpResponse<CbIbk01H86600Res> oltpResponse = this.hostClient.sendOltp(oltpRequestOptions, scbkSendData,
                CbIbk01H86600Res.class);

        SecRcvListMyAccountsResponse output = this.securitiesRecoveryMapper
                .toSecRcvListMyAccountsResponse(oltpResponse.getResponse());

        log.debug("========= 보유계좌조회 전문 응답전문 출력 start =================================");
        log.debug("이용자번호 UserID : " + output.getUserID());
        log.debug("인터넷뱅킹ID YOIBID : " + output.getYoIBID());
        log.debug("인터넷뱅킹가입구분 YOIBGB : " + output.getYoIBGB());
        log.debug("텔레뱅킹가입구분 YOTBGB : " + output.getYoTBGB());
        log.debug("퍼스트비즈가입구분 YOFBGB : " + output.getYoFBGB());
        log.debug("IB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP YOPSOGB : " + output.getYoPSOGB());
        log.debug("TB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP YOTSOGB : " + output.getYoTSOGB());
        log.debug("동시사용여부 1:동시 2:개별 YODSGB : " + output.getYoDSGB());
        log.debug("리워드포인트잔고 YOPJANGO : " + output.getYoPJANGO());
        log.debug("이용자성명 YONAME : " + output.getYoNAME());
        log.debug("HD 지역번호 YOHDDD : " + output.getYoHDDD());
        log.debug("HD 국번 YOHGUK : " + output.getYoHGUK());
        log.debug("HD 전화번호 YOHTEL : " + output.getYoHTEL());
        log.debug("개인：주민등록번호 YOJMNO : " + output.getYoJMNO());
        log.debug("FATCA평가일 YOFATIL : " + output.getYoFATIL());
        log.debug("CIFNO YOCIFNO : " + output.getYoCIFNO());
        log.debug("마케팅동의존재여부 YOMKTYN : " + output.getYoMKTYN());
        log.debug("퍼스트알리미 YOSMSYN : " + output.getYoSMSYN());
        log.debug("요구불외화휴면계좌YN YOHUMGB : " + output.getYoHUMGB());
        log.debug("자기계좌정보 배열건수 YOGUNSU : " + output.getYoGUNSU());
        // log.debug("거래중지계좌여부 YOGRJJ : " + output.getYoGRJJ());
        log.debug("계좌정보 : " + output.getYoMYINF_REC());
        log.debug("========= 보유계좌조회 전문 응답전문 출력 end =================================");

        List<YOMYINF_REC> resultAcctList = new ArrayList<>(); // 계좌목록
        int YOGRJJCnt = 0;
        String acctNum = "";
        int acctSubCode = 0;
        List<YOMYINF_REC> allAcount = output.getYoMYINF_REC();

        // 평일: 10,20,30,61,80,85,88,90 / 휴일: 10,20,30만 노출
        // 거래중지계좌가 아닌 경우만 노출
        if (allAcount != null) {
            if (allAcount.size() > 0) {
                for (YOMYINF_REC r : allAcount) {
                    acctNum = r.getYoMYGJ();
                    acctSubCode = Integer.parseInt(acctNum.substring(3, 5));
                    String kwamok = r.getYoMYGJ().substring(3, 5);
                    String assort = r.getYoZONG();
                    String YOGRJJ = r.getYoGRJJ(); // 거래중지계좌 (1:거래중지)
                    r.setYoMYGJNM(PRCSharedUtils.getAccountName(kwamok, assort));

                    if (YOGRJJ == null || YOGRJJ.equals("") || !YOGRJJ.equals("1")) {
                        if (holiDay.equals("N")) {
                            // 평일
                            if (acctSubCode == 10 || acctSubCode == 20 || acctSubCode == 30 || acctSubCode == 61
                                    || acctSubCode == 80
                                    || acctSubCode == 85 || acctSubCode == 88 || acctSubCode == 90) {
                                log.debug("########## 평일 acctNum : " + acctNum
                                        + PRCSharedUtils.getAccountName(kwamok, assort));
                                resultAcctList.add(r);
                            }
                        } else {
                            // 휴일
                            if (acctSubCode == 10 || acctSubCode == 20 || acctSubCode == 30) {
                                log.debug("########## 휴일 acctNum : " + acctNum
                                        + PRCSharedUtils.getAccountName(kwamok, assort));
                                resultAcctList.add(r);
                            }
                        }
                    } else if (YOGRJJ.equals("1")) {
                        YOGRJJCnt++; // 거래중지계좌 갯수
                    }
                }
            }
        }

        output.setYoGRJJCnt(YOGRJJCnt);
        output.setTotAcctCnt(resultAcctList.size());
        output.setScrnDataInfo(SCRN_DATA_INFO);
        output.setScreenFlag(screenFlag);
        output.setResultAcctList(resultAcctList);

        log.debug("========= 보유계좌조회 holiDay =================================" + holiDay);
        log.debug("####### SecuritiesRecoveryService.listMyAccounts 끝 #######" + output);

        return output;
    }

    /**
     * 전자서명페이지 정보 설정
     * 
     * @param ctx
     * @param input
     * @return
     */
    @ServiceEndpoint(url = "/setSignPageInfo", name = "전자서명페이지 정보 설정 [ASIS:MA3CRTAPM001_401S]", author = "허정우")
    public SecRcvSetSignPageInfoResponse setSignPageInfo(IServiceContext ctx, SecRcvSetSignPageInfoRequest input) {
        log.debug("####### SecuritiesRecoveryService.setSignPageInfo 진입 #######");
        SecRcvSetSignPageInfoResponse output = new SecRcvSetSignPageInfoResponse();

        String paramJsonString = StringUtils.defaultIfEmpty(input.getParamJsonString(), "");
        String SCRN_DATA_INFO = StringUtils.defaultIfEmpty(input.getScrnDataInfo(), "");
        String screenFlag = StringUtils.defaultIfEmpty(input.getScreenFlag(), "");

        String YIGJNO = StringUtils.defaultIfEmpty(input.getYiGJNO(), ""); // 계좌번호
        String newPassword = StringUtils.defaultIfEmpty(input.getNewPassword(), ""); // 신규비밀밀번호
        String newPassword2 = StringUtils.defaultIfEmpty(input.getNewPassword2(), ""); // 신규비밀번호 재확인

        if (!newPassword.equals(newPassword2)) {
            PRCServiceException exception = new PRCServiceException("9999", "계좌비밀번호 불일치");
            throw exception;
        }

        output.setParamJsonString(paramJsonString);
        output.setScrnDataInfo(SCRN_DATA_INFO);
        output.setScreenFlag(screenFlag);
        output.setYiGJNO(YIGJNO);

        sessionManager.setGlobalValue("CRTAPM_newPassword", newPassword);
        sessionManager.setGlobalValue("CRTAPM_newPassword2", newPassword2);

        log.debug("####### SecuritiesRecoveryService.setSignPageInfo 끝 #######");

        return output;
    }

    /**
     * ARS 인증 정보 설정
     * 
     * @param ctx
     * @param input
     * @return
     */
    @ServiceEndpoint(url = "/setArsInfo", name = "ARS 인증 정보 설정 [ASIS:MA3CRTAPM001_411S]", author = "허정우")
    public SecRcvSetArsInfoResponse setArsInfo(IServiceContext ctx, SecRcvSetArsInfoRequest input) {

        log.debug("####### SecuritiesRecoveryService.setArsInfo 진입 #######");

        String paramJsonString = StringUtils.defaultIfEmpty(input.getParamJsonString(), "");
        String SCRN_DATA_INFO = StringUtils.defaultIfEmpty(input.getScrnDataInfo(), "");
        String screenFlag = StringUtils.defaultIfEmpty(input.getScreenFlag(), "");

        String YIGJNO = StringUtils.defaultIfEmpty(input.getYiGJNO(), ""); // 계좌번호
        String newPassword = StringUtils.defaultIfEmpty(input.getNewPassword(), ""); // 신규비밀밀번호
        String newPassword2 = StringUtils.defaultIfEmpty(input.getNewPassword2(), ""); // 신규비밀번호 재확인
        if (!newPassword.equals(newPassword2)) {
            PRCServiceException exception = new PRCServiceException("9999", "계좌비밀번호 불일치");
            throw exception;
        }

        String PerBusNo = sessionManager.getGlobalValue("PerBusNo", String.class);

        // 전화번호 조회를 위한 H920 조회
        OltpRequestOptions oltpRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H920");
        CbIbk01H92000Req scbkSendData_H920 = new CbIbk01H92000Req(); // 전문코드 세팅

        // 공통부 세팅
        oltpRequestOptions.setImsTranCd("TI1IBK01");
        oltpRequestOptions.setInClassCd("H920");
        oltpRequestOptions.setSvcCd("920");

        if (!sessionManager.isLogin()) {
            scbkSendData_H920.setUserID(SessionUtils.getSessionValue("UserID", String.class));
            scbkSendData_H920.setTSPassword(SessionUtils.getSessionValue("TSPassword", String.class));
        } else {
            scbkSendData_H920.setUserID("FIRST999");
            scbkSendData_H920.setTSPassword("99999999");
        }

        // 개별부 세팅
        scbkSendData_H920.setInJuMinNo(PerBusNo);
        scbkSendData_H920.setYISALES("Y");
        scbkSendData_H920.setBKGuBun("2");
        // scbkSendData_H920.putBody("TRAN_CHECK", "N");

        // 전문 전송
        OltpResponse<CbIbk01H92000Res> oltpResponse = this.hostClient.sendOltp(oltpRequestOptions, scbkSendData_H920,
                CbIbk01H92000Res.class);
        CbIbk01H92000Res output_H920 = oltpResponse.getResponse();

        // ARS 인증을위한 세션 세팅
        OltpCommon oltpCommon = oltpResponse.getHeader().getOltpCommon();
        sessionManager.setGlobalValue("GUEST_USER_TRCD", StringUtils.defaultIfEmpty(oltpCommon.getTrCd(), ""));
        sessionManager.setGlobalValue("GUEST_USER_TEL1", StringUtils.defaultIfEmpty(output_H920.getHandPhone1(), ""));
        sessionManager.setGlobalValue("GUEST_USER_TEL2", StringUtils.defaultIfEmpty(output_H920.getHandPhone2(), ""));
        sessionManager.setGlobalValue("GUEST_USER_TEL3", StringUtils.defaultIfEmpty(output_H920.getHandPhone3(), ""));

        SecRcvSetArsInfoResponse output = new SecRcvSetArsInfoResponse();
        output.setParamJsonString(paramJsonString);
        output.setScrnDataInfo(SCRN_DATA_INFO);
        output.setScreenFlag(screenFlag);

        output.setYiGJNO(YIGJNO);
        sessionManager.setGlobalValue("CRTAPM_newPassword", newPassword);
        sessionManager.setGlobalValue("CRTAPM_newPassword2", newPassword2);
        log.debug("####### SecuritiesRecoveryService.setArsInfo 끝 #######" + output);

        return output;
    }

    /**
     * 재설정 본거래
     * 
     * @param ctx
     * @param input
     * @return
     */
    @ServiceEndpoint(url = "/editNewPasswordMain", name = "재설정 본거래 [ASIS:MA3CRTAPM001_501S]", author = "허정우")
    public SecRcvEditNewPasswordMainResponse editNewPasswordMain(IServiceContext ctx,
            SecRcvEditNewPasswordMainRequest input) {

        String paramJsonString = StringUtils.defaultIfEmpty(input.getParamJsonString(), "");
        String SCRN_DATA_INFO = StringUtils.defaultIfEmpty(input.getScrnDataInfo(), "");

        String UserID = "";
        String YIJMNO = ""; // 주민등록번호
        String YIUPGB = StringUtils.defaultIfEmpty(input.getYiUPGB(), ""); // 업무구분
        String YIGJNO = StringUtils.defaultIfEmpty(input.getYiGJNO(), ""); // 계좌번호
        // 25.06 상반기 취약점 조치 newPassword 값을 Session 으로 변경
        // String YIGJPASS = StringUtils.defaultIfEmpty(input.getString("YIGJPASS"),
        // ""); // 계좌비밀번호
        String YIGJPASS = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("CRTAPM_newPassword", String.class),
                ""); // 계좌비밀번호
        String isLoginYN = "";

        if (sessionManager.isLogin()) {
            UserID = sessionManager.getLoginValue("UserID", String.class);
            YIJMNO = sessionManager.getLoginValue("PerBusNo", String.class);
            isLoginYN = "Y";
        } else {
            UserID = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("UserID", String.class), "FIRST999");
            YIJMNO = sessionManager.getGlobalValue("PerBusNo", String.class);
            isLoginYN = "N";
        }

        // TI1IBK01_D844 (CB_IBK01_D844)
        OltpRequestOptions oltpRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D844");
        CbIbk01D84400Req sendData = new CbIbk01D84400Req();

        oltpRequestOptions.setImsTranCd("TI1IBK01");
        oltpRequestOptions.setInClassCd("D844");
        oltpRequestOptions.setSvcCd("844");

        // 로그인인 경우만 전자서명
        if ("Y".equals(isLoginYN)) {
            // 전자서명 검증관련 속성 설정
            oltpRequestOptions.setAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.VERIFY_N_SAVE);
        }

        sendData.setUserID(UserID);
        sendData.setYIUPGB(YIUPGB); // 업무구분 (1:재설정, 2:오류건수해제)
        sendData.setYIJMNO(YIJMNO); // 주민/사업자번호
        sendData.setYIGJNO(YIGJNO); // 계좌번호
        sendData.setYIGJPASS(YIGJPASS); // 계좌비밀번호

        OltpResponse<CbIbk01D84400Res> oltpResponse = this.hostClient.sendOltp(oltpRequestOptions, sendData,
                CbIbk01D84400Res.class);
        SecRcvEditNewPasswordMainResponse output = this.securitiesRecoveryMapper
                .toSecRcvEditNewPasswordMainResponse(oltpResponse.getResponse());

        output.setParamJsonString(paramJsonString);
        output.setScrnDataInfo(SCRN_DATA_INFO);
        output.setIsLoginYN(isLoginYN);

        log.debug("########### SecuritiesRecoveryService.editNewPasswordMain output : " + output);
        return output;
    }

    /**
     * 계좌 비밀번호 오류건수 해제용 보유계좌조회
     * 
     * @param SecRcvListPasswordErrorClearAccountRequest
     * @return SecRcvListPasswordErrorClearAccountResponse
     */
    @ServiceEndpoint(url = "/listPasswordErrorClearAccount", name = "계좌 비밀번호 오류건수 해제용 보유계좌조회 [ASIS:MA3CRTAPM002_101S]", author = "김기주")
    public SecRcvListPasswordErrorClearAccountResponse listPasswordErrorClearAccount(IServiceContext ctx,
            SecRcvListPasswordErrorClearAccountRequest input) {

        log.debug("####### SecuritiesRecoveryService.listPasswordErrorClearAccount 진입 #######");

        sessionManager.removeGlobalValue("TRAD_NO");

        sessionManager.setGlobalValue("PRDCT_ID", "9992");
        sessionManager.setGlobalValue("PRDCT_NM", "계좌 비밀번호 오류건수 해제");
        sessionManager.setGlobalValue("PRDCT_CD", "9992");
        sessionManager.setGlobalValue("BIZ_TYPE", "MRLR");

        String PerBusNo = sessionManager.getLoginValue("PerBusNo", String.class);
        String sendDateTime = DateUtils.getCurrentDate("yyyyMMddHHmmss");
        boolean checkHoliday = defaultHolidayManager.isHoliday(sendDateTime); // 휴일여부 Check
        String holiDay = "";

        /* 휴일 */
        if (checkHoliday) {
            holiDay = "Y";
        } else {
            holiDay = "N";
        }

        // 평일 : 10,20,30,61,80,85,88,90
        // 휴일 : 10,20,30

        // 보유계좌조회 전문
        OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H866");

        CbIbk01H86600Req scbkSendData = new CbIbk01H86600Req();

        // 공통부 세팅
        hostRequestOptions.setImsTranCd("TI1IBK01");
        hostRequestOptions.setInClassCd("H866");
        hostRequestOptions.setSvcCd("866");
        hostRequestOptions.setCaptureSystem("OLTP");

        scbkSendData.setUserID("FIRST999");
        scbkSendData.setTSPassword("99999999");
        // scbkSendData.setYIBIDAE(YIBIDAE); // 비대면실명승인계좌
        scbkSendData.setYIJMNO(PerBusNo);
        scbkSendData.setYIMBSINGO("Y");

        OltpResponse<CbIbk01H86600Res> oltpResponse = this.hostClient.sendOltp(hostRequestOptions, scbkSendData,
                CbIbk01H86600Res.class);

        SecRcvListPasswordErrorClearAccountResponse output = this.securitiesRecoveryMapper
                .toSecRcvListPasswordErrorClearAccountResponse(oltpResponse.getResponse());

        log.debug("========= 보유계좌조회 전문 응답전문 출력 start =================================");
        log.debug("이용자번호 UserID : " + output.getUserID());
        log.debug("인터넷뱅킹ID YOIBID : " + output.getYoIBID());
        log.debug("인터넷뱅킹가입구분 YOIBGB : " + output.getYoIBGB());
        log.debug("텔레뱅킹가입구분 YOTBGB : " + output.getYoTBGB());
        log.debug("퍼스트비즈가입구분 YOFBGB : " + output.getYoFBGB());
        log.debug("IB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP YOPSOGB : " + output.getYoPSOGB());
        log.debug("TB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP YOTSOGB : " + output.getYoTSOGB());
        log.debug("동시사용여부 1:동시 2:개별 YODSGB : " + output.getYoDSGB());
        log.debug("리워드포인트잔고 YOPJANGO : " + output.getYoPJANGO());
        log.debug("이용자성명 YONAME : " + output.getYoNAME());
        log.debug("HD 지역번호 YOHDDD : " + output.getYoHDDD());
        log.debug("HD 국번 YOHGUK : " + output.getYoHGUK());
        log.debug("HD 전화번호 YOHTEL : " + output.getYoHTEL());
        log.debug("개인：주민등록번호 YOJMNO : " + output.getYoJMNO());
        log.debug("FATCA평가일 YOFATIL : " + output.getYoFATIL());
        log.debug("CIFNO YOCIFNO : " + output.getYoCIFNO());
        log.debug("마케팅동의존재여부 YOMKTYN : " + output.getYoMKTYN());
        log.debug("퍼스트알리미 YOSMSYN : " + output.getYoSMSYN());
        log.debug("요구불외화휴면계좌YN YOHUMGB : " + output.getYoHUMGB());
        log.debug("자기계좌정보 배열건수 YOGUNSU : " + output.getYoGUNSU());
        // log.debug("거래중지계좌여부 YOGRJJ : " + output.getYoGRJJ());
        log.debug("계좌정보 : " + output.getYoMYINF_REC());
        log.debug("========= 보유계좌조회 전문 응답전문 출력 end =================================");

        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> resultAcctList = new ArrayList<>(); // 계좌목록

        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> allAcctList = new ArrayList<>(); // 전체목록
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> casaAcctList = new ArrayList<>(); // 입출금목록(10,20,30)
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> fxAcctList = new ArrayList<>(); // 외화보통예금(85)
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> depositAcctiList = new ArrayList<>(); // 원화예금목록(80)
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> fxDepoAcctList = new ArrayList<>(); // 외화정기예금목록(88)
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> savingAcctList = new ArrayList<>(); // 적금목록(90)
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> fundAcctList = new ArrayList<>(); // 펀드목록(61)

        String acctNum = "";
        int acctSubCode = 0;
        List<SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC> allAcount = output.getYoMYINF_REC();

        int allAcountSize = allAcount != null ? allAcount.size() : 0;

        log.debug("========= 보유계좌조회 allAcount SIZE =================================" + allAcountSize);
        log.debug("========= 보유계좌조회 holiDay =================================" + holiDay);

        // 평일: 10,20,30,61,80,85,88,90 / 휴일: 10,20,30만 노출
        // 거래중지계좌가 아닌 경우만 노출
        if (allAcount != null) {
            if (allAcount.size() > 0) {
                for (SecRcvListPasswordErrorClearAccountResponse.YOMYINF_REC r : allAcount) {
                    String kwamok = r.getYoMYGJ().substring(3, 5);
                    String assort = r.getYoZONG();
                    String YOGRJJ = r.getYoGRJJ(); // 거래중지계좌 (1:거래중지)
                    acctNum = r.getYoMYGJ();
                    acctSubCode = Integer.parseInt(acctNum.substring(3, 5));
                    r.setYoMYGJNM(PRCSharedUtils.getAccountName("", kwamok, assort));
                    log.debug("############## acctSubCode" + acctSubCode);

                    if ((YOGRJJ == null) || YOGRJJ.equals("") || !YOGRJJ.equals("1")) {
                        if (holiDay.equals("N")) {

                            // 평일
                            if (acctSubCode == 10 || acctSubCode == 20 || acctSubCode == 30 || acctSubCode == 61
                                    || acctSubCode == 80 || acctSubCode == 85 || acctSubCode == 88
                                    || acctSubCode == 90) {
                                log.debug("########## 평일 acctNum : " + acctNum);
                                resultAcctList.add(r);
                            }

                            // 입출금리스트 적재
                            if (acctSubCode == 10 || acctSubCode == 20 || acctSubCode == 30) {
                                casaAcctList.add(r);
                            }

                            // 외화보통예금리스트 적재
                            if (acctSubCode == 85) {
                                fxAcctList.add(r);
                            }

                            // 원화예금리스트 적재
                            if (acctSubCode == 88) {
                                fxDepoAcctList.add(r);
                            }

                            // 외화정기예금리스트 적재
                            if (acctSubCode == 90) {
                                savingAcctList.add(r);
                            }

                            // 펀드리스트 적재
                            if (acctSubCode == 61) {
                                fundAcctList.add(r);
                            }
                        } else {
                            // 휴일
                            if (acctSubCode == 10 || acctSubCode == 20 || acctSubCode == 30) {
                                log.debug("########## 휴일 acctNum : " + acctNum);
                                resultAcctList.add(r);
                            }
                        }
                    }
                }
            }
        }

        // 전체 리스트에 순서대로 주입
        allAcctList.addAll(casaAcctList);
        allAcctList.addAll(fxAcctList);
        allAcctList.addAll(depositAcctiList);
        allAcctList.addAll(fxDepoAcctList);
        allAcctList.addAll(savingAcctList);
        allAcctList.addAll(fundAcctList);

        // 전체 리스트 로그
        log.debug("############## allAcctList" + allAcctList);

        int allAcctListSize = allAcctList != null ? allAcctList.size() : 0;
        int resultAcctListSize = resultAcctList != null ? resultAcctList.size() : 0;

        log.debug("========= 주입계좌조회 allAcctList SIZE =================================" + allAcctListSize);
        log.debug("========= 전체계좌조회 resultAcctList SIZE =================================" + resultAcctListSize);

        // 방어로직(사이즈 비교해서 다르면 AS-IS 리스트 노출)
        if (allAcctListSize == resultAcctListSize) {
            resultAcctList = allAcctList;
        }

        output.setResultAcctList(resultAcctList);
        log.debug("####### SecuritiesRecoveryService.listPasswordErrorClearAccount 끝 #######" + output.toString());

        return output;
    }

    /**
     * 계좌 비밀번호 오류건수 해제
     * 
     * @param SecRcvClosePasswordErrorClearAccountResponse
     * @return SecRcvClosePasswordErrorClearAccountRequest
     */
    @ServiceEndpoint(url = "/closePasswordErrorClearAccount", name = "계좌 비밀번호 오류건수 해제 [ASIS:MA3CRTAPM002_301S]", author = "김기주")
    public SecRcvClosePasswordErrorClearAccountResponse closePasswordErrorClearAccount(IServiceContext ctx,
            SecRcvClosePasswordErrorClearAccountRequest input) {

        log.debug("####### SecuritiesRecoveryService.closePasswordErrorClearAccount 진입 #######");

        // 보안매체 검증
        this.secureDataComponent.verifyVerification();

        // TI1IBK01_D844 (CB_IBK01_D844)
        OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D84A");
        CbIbk01D84400Req sendData = new CbIbk01D84400Req();
        SecRcvClosePasswordErrorClearAccountResponse output = new SecRcvClosePasswordErrorClearAccountResponse();
        String UserID = sessionManager.getLoginValue("UserID", String.class);
        String YIJMNO = sessionManager.getLoginValue("PerBusNo", String.class); // 주민등록번호
        String YIUPGB = StringUtils.defaultIfEmpty(input.getYiUPGB(), ""); // 업무구분
        String YIGJNO = StringUtils.defaultIfEmpty(input.getYiGJNO(), ""); // 계좌번호
        String YIGJPASS = StringUtils.defaultIfEmpty(input.getYiGJPASS(), ""); // 계좌비밀번호

        log.debug("########### SecuritiesRecoveryService.closePasswordErrorClearAccount UserID : " + UserID);
        log.debug("########### SecuritiesRecoveryService.closePasswordErrorClearAccount YIJMNO : " + YIJMNO);

        hostRequestOptions.setImsTranCd("TI1IBK01");
        hostRequestOptions.setInClassCd("D84A");
        hostRequestOptions.setSvcCd("84A");
        hostRequestOptions.setCaptureSystem("OLTP");
        hostRequestOptions.setAttribute(SignConstants.VERIFY_TYPE, SignVerifyType.VERIFY_N_SAVE);

        sendData.setUserID(UserID);
        sendData.setYIUPGB(YIUPGB); // 업무구분 (1:재설정, 2:오류건수해제)
        sendData.setYIJMNO(YIJMNO); // 주민/사업자번호
        sendData.setYIGJNO(YIGJNO); // 계좌번호
        sendData.setYIGJPASS(YIGJPASS); // 계좌비밀번호

        OltpResponse<CbIbk01D84400Res> hostResponse;

        try {
            hostResponse = this.hostClient.sendOltp(hostRequestOptions, sendData,
                    CbIbk01D84400Res.class);
        } catch (OltpSystemException e) {
            PRCServiceException exception = new PRCServiceException(e.getErrorCode(), e.getErrorModule());
            exception.setErrorMessage(e.getErrorMessage());

            throw exception;
        }

        output = this.securitiesRecoveryMapper
                .toSecRcvClosePasswordErrorClearAccountResponse(hostResponse.getResponse());

        String YIGJNO_1 = YIGJNO.substring(0, 3);
        String YIGJNO_2 = YIGJNO.substring(3, 5);
        String YIGJNO_3 = YIGJNO.substring(5, 11);

        // 11월 정기 sms 마스킹 로직 변경
        String sms_YIGJNO = YIGJNO_1 + "-" + YIGJNO_2 + "-" + YIGJNO_3;
        sms_YIGJNO = BizCommonUtils.getMaskCustData(sms_YIGJNO, "02");

        String smsMsg = "[SC제일은행]통장 비밀번호 오류건수가 해제되었습니다(" + sms_YIGJNO + ")";
        log.debug(
                "########### SecuritiesRecoveryService.closePasswordErrorClearAccount sms_YIGJNO ################## : "
                        + sms_YIGJNO);

        /* ====================SMS 전문전송==================== */

        String member = StringUtils.defaultIfEmpty(input.getMember(), "0"); // Client측 key일련번호
        String usercode = StringUtils.defaultIfEmpty(input.getUserCode(), "ebanking"); // 사용자 발신 코드
        String username = StringUtils.defaultIfEmpty(input.getUsername(), "I5"); // 사용자명
        String callphone1 = sessionManager.getLoginValue("HPOne", String.class); // 호출 번호 #1
        String callphone2 = sessionManager.getLoginValue("HPTwo", String.class); // 호출 번호 #2
        String callphone3 = sessionManager.getLoginValue("HPThree", String.class); // 호출 번호 #3
        String callmessage = smsMsg; // 호출 메시지
        String rdate = DateUtils.getCurrentDate("yyyyMMdd"); // 메시지 전송 예약일자
        String rtime = DateUtils.getCurrentDate("HHmmss"); // 메시지 전송 예약시간
        String reqphone1 = ""; // 회신 번호#1
        String reqphone2 = "1588"; // 회신 번호#2
        String reqphone3 = "1599"; // 회신 번호#3
        String callname = "SC제일은행"; // 발신자명
        String deptcode = "GL9-KI3-HS9"; // 회사 코드
        String deptname = "디지털뱅킹부"; // 회사명

        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #1 : [" + callphone1 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #2 : [" + callphone2 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #3 : [" + callphone3 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 메시지 : [" + callmessage + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 발신자명 : [" + callname + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 메시지 전송 예약일자 : [" + rdate + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 메시지 전송 예약시간 : [" + rtime + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #1 : [" + reqphone1 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #2 : [" + reqphone2 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 호출 번호 #3 : [" + reqphone3 + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 부서코드 : [" + deptcode + "]");
        log.debug("#### closePasswordErrorClearAccount sendSMSService 부서명 : [" + deptname + "]");

        try {
            SmsRequest smsParam = new SmsRequest();

            smsParam.setMember(member);
            smsParam.setUserCode(usercode);
            smsParam.setUserName(username);
            smsParam.setCallPhone1(callphone1);
            smsParam.setCallPhone2(callphone2);
            smsParam.setCallPhone3(callphone3);
            smsParam.setCallMessage(callmessage);
            smsParam.setRateDate(rdate);
            smsParam.setRateTime(rtime);
            smsParam.setReqPhone1(reqphone1);
            smsParam.setReqPhone2(reqphone2);
            smsParam.setReqPhone3(reqphone3);
            smsParam.setCallName(callname);
            smsParam.setDeptCode(deptcode);
            smsParam.setDeptName(deptname);

            String result = smsComponent.sendMain(smsParam);

            log.debug("SMS 발송결과  = [" + result + "]");
        } catch (PRCServiceException e) {
            log.debug("SMS 발송에러 \n" + e.getErrorMessage());
        }
        /* ====================SMS 전문전송==================== */

        log.debug("########### SecuritiesRecoveryService.closePasswordErrorClearAccount output : " + output);

        return output;
    }
}
