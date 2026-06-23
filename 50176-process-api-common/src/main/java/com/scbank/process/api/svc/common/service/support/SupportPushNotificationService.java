package com.scbank.process.api.svc.common.service.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01D92E00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D92E00Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92B00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92B00Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.integration.system.oltp.exception.OltpSystemException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.SupportPushNotificationComponent;
import com.scbank.process.api.svc.common.components.dto.GetPushAlarmServiceTermsAgreeRequest;
import com.scbank.process.api.svc.common.components.dto.GetPushAlarmServiceTermsAgreeResponse;
import com.scbank.process.api.svc.common.components.dto.SetTransferListPushSettingPageRequest;
import com.scbank.process.api.svc.common.components.dto.SetTransferListPushSettingPageResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoRequest;
import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateAlarmServiceRequest;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateAlarmServiceResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateMarketingNoticeTermsAgreeRequest;
import com.scbank.process.api.svc.common.dao.Exchange100Dao;
import com.scbank.process.api.svc.common.dao.Ma30PrdctPrvsnMgtDao;
import com.scbank.process.api.svc.common.dao.Ma30SedMsgAgrmntDao;
import com.scbank.process.api.svc.common.dao.SubCdDao;
import com.scbank.process.api.svc.common.dao.TmbBkstatMgtDao;
import com.scbank.process.api.svc.common.dao.TmbExrateMgtDao;
import com.scbank.process.api.svc.common.dao.TmbObjUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.DeleteAllExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.DeleteExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.DeleteTransferPushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertPushJoinDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertTransferPushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertUserPushAgreeParameter;
import com.scbank.process.api.svc.common.dao.dto.PushJoinStatusResult;
import com.scbank.process.api.svc.common.dao.dto.SelectExchangeInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushAgreeInfoResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushServiceCdResult;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.TermsInfoResult;
import com.scbank.process.api.svc.common.dao.dto.UpdateExratePushDataParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushJoinAlarmParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushJoinDataParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdateTransferPushDataParameter;
import com.scbank.process.api.svc.common.mapper.SupportPushNotificationMapper;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntApplyPushAlarmServiceRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntApplyPushAlarmServiceResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntClosePushAlarmServiceRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntClosePushAlarmServiceResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntConfirmPushJoinYnRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntConfirmPushJoinYnResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntListPushRateRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntListPushRateResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetDetailSettingPageRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetDetailSettingPageResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetMarketingNoticePopRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetMarketingNoticePopResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetPushAlarmServicePopRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntSetPushAlarmServicePopResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateRatePushAlarmRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateRatePushAlarmResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateServicePushAlarmRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateServicePushAlarmResponse;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateTransferPushRequest;
import com.scbank.process.api.svc.common.service.support.dto.pushNotification.SupPntUpdateTransferPushResponse;
import com.scbank.process.api.svc.shared.integration.HostClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "푸시알림 서비스조회/설정", url = "/support/pushNotification", author = "김기주")
public class SupportPushNotificationService {

    private final ISessionContextManager sessionManager;

    private final SupportPushNotificationComponent supportPushNotificationComponent;

    private final TmbObjUsrMgtDao tmbObjUsrMgtDao;

    private final TmbExrateMgtDao tmbExrateMgtDao;

    private final HostClient hostClient;

    private final Ma30PrdctPrvsnMgtDao ma30PrdctPrvsnMgtDao;

    private final SupportPushNotificationMapper supportPushNotificationMapper;

    private final TmbBkstatMgtDao tmbBkstatMgtDao;

    private final Exchange100Dao exchange100Dao;

    private final SubCdDao subCdDao;

    private final Ma30SedMsgAgrmntDao ma30SedMsgAgrmntDao;

    /**
     * 푸시 알림 서비스 설정 팝업
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3MSCNTF002_101S
     */
    @ServiceEndpoint(url = "/setPushAlarmServicePop", name = "푸시 알림 서비스 설정 팝업 [ASIS:MA3MSCNTF002_101S]", author = "김기주")
    public SupPntSetPushAlarmServicePopResponse setPushAlarmServicePop(IServiceContext ctx,
            SupPntSetPushAlarmServicePopRequest input) {
        SupPntSetPushAlarmServicePopResponse output = new SupPntSetPushAlarmServicePopResponse();
        // 가입여부
        output.setBreezePushJoinYN(sessionManager.getLoginValue("BreezePushJoinYN", String.class));
        // 휴대폰번호
        output.setPhoneNo(sessionManager.getLoginValue("phoneNo", String.class));
        output.setHpOne(sessionManager.getLoginValue("HPOne", String.class));
        output.setHpTwo(sessionManager.getLoginValue("HPTwo", String.class));
        output.setHpThree(sessionManager.getLoginValue("HPThree", String.class));

        return output;
    }

    /**
     * 푸시 알림 서비스 설정화면 화면을 호출 및 푸시 가입여부를 조회
     * 
     * @param IServiceContext
     * @param SupPntConfirmPushJoinYnRequest
     * @return SupPntConfirmPushJoinYnResponse
     * @description MA3MSCNTF002_301S
     */
    @ServiceEndpoint(url = "/confirmPushJoinYn", name = "푸시 알림 서비스 설정화면 화면을 호출 및 푸시 가입여부를 조회 [ASIS:MA3MSCNTF002_301S]", author = "김기주")
    public SupPntConfirmPushJoinYnResponse confirmPushJoinYn(IServiceContext ctx,
            SupPntConfirmPushJoinYnRequest input) {

        SupPntConfirmPushJoinYnResponse output = new SupPntConfirmPushJoinYnResponse();

        // 23.04.24 나만의혜택 / 이벤트 마케팅 동의 이동시 Return Page처리
        String CALLPAGETYPE = StringUtils.defaultIfEmpty(input.getCallPageType(), "");
        String EVENT_NO = StringUtils.defaultIfEmpty(input.getEventNo(), "");

        String userId = this.sessionManager.getLoginValue("UserID", String.class);

        List<PushJoinStatusResult> pushJoinYnList = this.tmbObjUsrMgtDao.selectPushJoinStatus(userId);
        if (pushJoinYnList != null && !pushJoinYnList.isEmpty()) {
            if (pushJoinYnList.size() > 0) {
                for (int i = 0; i < 1; i++) {
                    // 고객일련번호
                    output.setSerno(pushJoinYnList.get(i).getSerNo());
                    // 입출금 내역
                    output.setIotranlistFlag(pushJoinYnList.get(i).getIotranlistFlag());
                    // 환율알림 설정
                    output.setExrateFlg(pushJoinYnList.get(i).getExrateFlg());
                    // 맞춤혜택 안내
                    output.setBenefitFlag(pushJoinYnList.get(i).getBenefitFlag());
                    // 금융시장 정보
                    output.setFinanceFlag(pushJoinYnList.get(i).getFinanceFlag());
                    // 금융시장 정보 값 - 1:전체, 2:주간정보, 3:월간정보
                    output.setFinanceVal(pushJoinYnList.get(i).getFinanceVal());
                    // WMLOUNGE 정보
                    output.setWmloungeFlag(pushJoinYnList.get(i).getWmloungeFlag());
                    log.debug("FINANCE_VAL : [" + pushJoinYnList.get(i).getFinanceVal() + "]");
                }
            }
        }

        // 서비스 동의 항목 및 툴팀 정보 조회
        List<SelectPushServiceCdResult> dbPushServiceCdList = this.subCdDao.selectPushServiceCd();

        if (dbPushServiceCdList != null && !dbPushServiceCdList.isEmpty()) {
            Locale locale = LocaleContextHolder.getLocale();
            boolean isKorean = Locale.KOREAN.equals(locale);

            List<SelectPushServiceCdResult> pushServiceCdList = dbPushServiceCdList.stream().map(obj -> {
                // SelectPushServiceCdResult newObj = new SelectPushServiceCdResult();
                String[] cdNms = obj.getCdNm().split("\\|");
                String[] explns = obj.getExpln().split("\\|");

                obj.setCdNm(isKorean ? cdNms[0] : cdNms[1]);
                obj.setExpln(isKorean ? explns[0] : explns[1]);

                return obj;
            }).collect(Collectors.toList());

            output.setPushServiceCdList(this.supportPushNotificationMapper.toPushServiceCd(pushServiceCdList));
        }

        // 서비스 동의 항목 별 동의 여부 조회
        List<SelectPushAgreeInfoResult> pushAgreeInfoResultList = this.ma30SedMsgAgrmntDao.selectPushAgreeInfo(userId);

        if (pushAgreeInfoResultList != null && !pushAgreeInfoResultList.isEmpty()) {
            output.setPushAgreeInfoList(this.supportPushNotificationMapper.toPushAgreeInfo(pushAgreeInfoResultList));
        }

        SetTransferListPushSettingPageRequest inputData = new SetTransferListPushSettingPageRequest();
        SetTransferListPushSettingPageResponse resultData = this.supportPushNotificationComponent
                .setTransferListPushSettingPage(ctx, inputData);

        GetPushAlarmServiceTermsAgreeRequest inputAgreeData = new GetPushAlarmServiceTermsAgreeRequest();
        inputAgreeData.setSsn(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), ""));
        GetPushAlarmServiceTermsAgreeResponse resultAgreeData = this.supportPushNotificationComponent
                .getPushAlarmServiceTermsAgree(ctx, inputAgreeData);

        output.setAppGb(resultData.getAppGb());
        output.setOkCount(resultData.getOkCount());
        output.setCnfrmNo(resultData.getCnfrmNo());
        output.setCnfrmNoNew(StringUtils.defaultIfEmpty(input.getCnfrmNoNew(), ""));
        output.setOperType(StringUtils.defaultIfEmpty(input.getOperType(), ""));
        output.setPushSrvcApprvlFlg(StringUtils.defaultIfEmpty(input.getPushSrvcApprvlFlg(), ""));
        output.setAppInfo(StringUtils.defaultIfEmpty(input.getAppInfo(), ""));
        output.setDeviceMd(StringUtils.defaultIfEmpty(input.getDeviceMd(), ""));
        output.setDeviceVis(StringUtils.defaultIfEmpty(input.getDeviceVis(), ""));
        output.setAgrmntMk(StringUtils.defaultIfEmpty(resultAgreeData.getAgrmntMk(), ""));

        output.setCallPageType(CALLPAGETYPE);
        output.setEventNo(EVENT_NO);
        output.setBreezePushJoinYN(sessionManager.getLoginValue("BreezePushJoinYN", String.class));

        return output;
    }

    /**
     * 푸시알림 서비스 가입
     * 
     * @param IServiceContext
     * @param SupPntApplyPushAlarmServiceRequest
     * @return SupPntApplyPushAlarmServiceResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_306S
     */
    @ServiceEndpoint(url = "/applyPushAlarmService", name = "푸시알림 서비스 가입 [ASIS:MA3MSCNTF002_306S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntApplyPushAlarmServiceResponse applyPushAlarmService(IServiceContext ctx,
            SupPntApplyPushAlarmServiceRequest input) throws PRCServiceException {

        SupPntApplyPushAlarmServiceResponse output = new SupPntApplyPushAlarmServiceResponse();

        // DB조회 입력 값
        String userId = sessionManager.getLoginValue("UserID", String.class);
        // 재가입자 여부 판단을 하여 update 또는 insert 정의
        List<PushJoinStatusResult> pushJoindataList = this.tmbObjUsrMgtDao.selectPushJoinStatus(userId);

        try {
            if (pushJoindataList != null && !pushJoindataList.isEmpty()) { // 재가입
                String phoneNo = (sessionManager.getLoginValue("HPOne", String.class)
                        + sessionManager.getLoginValue("HPTwo", String.class)
                        + sessionManager.getLoginValue("HPThree", String.class));

                UpdatePushJoinDataParameter parameter = new UpdatePushJoinDataParameter();
                // 고객일련번호
                parameter.setSerno(StringUtils.defaultIfEmpty(input.getSerno(), ""));
                // 단말 운영체제 정보
                parameter.setOperType(StringUtils.defaultIfEmpty(input.getOperType(), ""));
                // 이용자ID
                parameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));
                // 이용자이름
                parameter.setUserNm(sessionManager.getLoginValue("CustName", String.class));
                // 이용자 전화번호
                parameter.setTelno(phoneNo);
                // Push기기확인번호
                parameter.setCnfrmNo(StringUtils.defaultIfEmpty(input.getCnfrmNo(), ""));
                // Push서비스 가입여부
                parameter.setPushSrvcApprvlFlg(StringUtils.defaultIfEmpty(input.getPushSrvcApprvlFlg(), ""));
                // APP정보 - APP버전정보
                parameter.setAppInfo(StringUtils.defaultIfEmpty(input.getAppInfo(), ""));
                // 기기모델명
                parameter.setDeviceMd(StringUtils.defaultIfEmpty(input.getDeviceMd(), ""));
                // 단말OS버전
                parameter.setDeviceVis(StringUtils.defaultIfEmpty(input.getDeviceVis(), ""));

                this.tmbObjUsrMgtDao.updatePushJoinData(parameter);
                sessionManager.setLoginValue("BreezePushJoinYN", "Y");
            } else { // 신규가입
                InsertPushJoinDataParameter insertParameter = new InsertPushJoinDataParameter();

                // 고객일련번호 시퀀스 조회
                int nextVal = this.tmbObjUsrMgtDao.selectPushUsrSeq();

                String phoneNo = (sessionManager.getLoginValue("HPOne", String.class)
                        + sessionManager.getLoginValue("HPTwo", String.class)
                        + sessionManager.getLoginValue("HPThree", String.class));
                // 고객일련번호
                insertParameter.setSerno(nextVal);
                // 단말 운영체제 정보
                insertParameter.setOperType(StringUtils.defaultIfEmpty(input.getOperType(), ""));
                // 이용자ID
                insertParameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));
                // 이용자이름
                insertParameter.setUserNm(sessionManager.getLoginValue("CustName", String.class));
                // 이용자 전화번호
                insertParameter.setTelno(phoneNo);
                // Push기기확인번호
                insertParameter.setCnfrmNo(StringUtils.defaultIfEmpty(input.getCnfrmNo(), ""));
                // Push서비스 가입여부
                insertParameter.setPushSrvcApprvlFlg(StringUtils.defaultIfEmpty(input.getPushSrvcApprvlFlg(), ""));
                // APP정보 - APP버전정보
                insertParameter.setAppInfo(StringUtils.defaultIfEmpty(input.getAppInfo(), ""));
                // 기기모델명
                insertParameter.setDeviceMd(StringUtils.defaultIfEmpty(input.getDeviceMd(), ""));
                // 단말OS버전
                insertParameter.setDeviceVis(StringUtils.defaultIfEmpty(input.getDeviceVis(), ""));

                this.tmbObjUsrMgtDao.insertPushJoinData(insertParameter);
                sessionManager.setLoginValue("BreezePushJoinYN", "Y");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException(e);
        }

        return output;
    }

    /**
     * 알림 서비스 등록 및 변경
     *
     * @param SupPntUpdateAlarmServiceRequest
     * @return SupPntUpdateAlarmServiceResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_307S
     */
    @ServiceEndpoint(url = "/updateAlarmService", name = "알림 서비스 등록 및 변경 [ASIS:MA3MSCNTF002_307S]", author = "김기주")
    public SupPntUpdateAlarmServiceResponse updateAlarmService(IServiceContext ctx,
            SupPntUpdateAlarmServiceRequest input) {
        return this.supportPushNotificationComponent.updateAlarmService(ctx, input);
    }

    /**
     * 알림 서비스 초기화
     * 
     * @param IServiceContext
     * @param SupPntClosePushAlarmServiceRequest
     * @return SupPntClosePushAlarmServiceResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_308S
     */
    @ServiceEndpoint(url = "/closePushAlarmService", name = "알림 서비스 초기화 [ASIS:MA3MSCNTF002_308S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntClosePushAlarmServiceResponse closePushAlarmService(IServiceContext ctx,
            SupPntClosePushAlarmServiceRequest input) throws PRCServiceException {

        SupPntClosePushAlarmServiceResponse output = new SupPntClosePushAlarmServiceResponse();

        try {
            String benefitFlag = "N"; // 맞춤혜택가입여부
            String financeFlag = "N"; // 금융시장정보가입여부
            String financeVal = "1"; // 금융시장정보값
            String wmloungeFlag = "N"; // WMLOUNGE가입여부
            String iotranlistFlag = "N"; // 입출금내역가입여부
            String notyExrateFlg = "N"; // 환율알림가입여부
            String serno = StringUtils.defaultIfEmpty(input.getSerno(), "");
            String bnkingId = sessionManager.getLoginValue("UserID", String.class);

            // 환율 푸쉬 초기화
            DeleteAllExratePushDataParameter deleteExratePushDataParameter = new DeleteAllExratePushDataParameter();
            deleteExratePushDataParameter.setSerno(serno);
            this.tmbExrateMgtDao.deleteAllExratePushData(deleteExratePushDataParameter);

            // 푸쉬알림 초기화
            UpdatePushJoinAlarmParameter updatePushJoinAlarmParameter = new UpdatePushJoinAlarmParameter();
            updatePushJoinAlarmParameter.setBenefitFlag(benefitFlag);
            updatePushJoinAlarmParameter.setFinanceFlag(financeFlag);
            updatePushJoinAlarmParameter.setFinanceVal(financeVal);
            updatePushJoinAlarmParameter.setWmloungeFlag(wmloungeFlag);
            updatePushJoinAlarmParameter.setIotranlistFlag(iotranlistFlag);
            updatePushJoinAlarmParameter.setNotyExrateFlg(notyExrateFlg);
            updatePushJoinAlarmParameter.setSerno(serno);
            updatePushJoinAlarmParameter.setBnkingId(bnkingId);
            this.tmbObjUsrMgtDao.updatePushJoinAlarm(updatePushJoinAlarmParameter);

            // PUSH 마케팅 개인정보수집이용동의 원장/이력 초기화
            SupPntUpdateMarketingNoticeTermsAgreeRequest supPntUpdateMarketingNoticeTermsAgreeRequest = new SupPntUpdateMarketingNoticeTermsAgreeRequest();
            supPntUpdateMarketingNoticeTermsAgreeRequest
                    .setSsn(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), ""));
            supPntUpdateMarketingNoticeTermsAgreeRequest
                    .setUsrNo(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class), ""));
            supPntUpdateMarketingNoticeTermsAgreeRequest.setAgrmntMk("2"); // 미동의
            supPntUpdateMarketingNoticeTermsAgreeRequest.setChnlgb("M"); // M : Mobile
            supPntUpdateMarketingNoticeTermsAgreeRequest.setUpdClerkNo("");
            this.supportPushNotificationComponent.updateMarketingNoticeTermsAgree(ctx,
                    supPntUpdateMarketingNoticeTermsAgreeRequest);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException(e);
        }

        return output;
    }

    /**
     * 입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회
     * 
     * @param IServiceContext
     * @param SetTransferListPushSettingPageRequest
     * @return SetTransferListPushSettingPageResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_302S
     */
    @ServiceEndpoint(url = "/setTransferListPushSettingPage", name = "입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회 [ASIS:MA3MSCNTF002_302S]", author = "김기주")
    public SetTransferListPushSettingPageResponse setTransferListPushSettingPage(IServiceContext ctx,
            SetTransferListPushSettingPageRequest input) {
        return this.supportPushNotificationComponent.setTransferListPushSettingPage(ctx, input);
    }

    /**
     * 상세 조회 및 상세설정 화면
     * 
     * @param IServiceContext
     * @param SupPntSetDetailSettingPageRequest
     * @return SupPntSetDetailSettingPageResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_303S
     */
    @ServiceEndpoint(url = "/setDetailSettingPage", name = "상세 조회 및 상세설정 화면 [ASIS:MA3MSCNTF002_303S]", author = "김기주")
    public SupPntSetDetailSettingPageResponse setDetailSettingPage(IServiceContext ctx,
            SupPntSetDetailSettingPageRequest input) throws PRCServiceException {

        HttpServletRequest request = ctx.request();
        SupPntSetDetailSettingPageResponse output = new SupPntSetDetailSettingPageResponse();

        // 고객일련번호
        output.setSerno(StringUtils.defaultIfEmpty(input.getSerno(), ""));

        // 통장번호
        output.setDrawAccNum(StringUtils.defaultIfEmpty(input.getDrawAccNum(), ""));

        // 통장명
        output.setDrawAccName(StringUtils.defaultIfEmpty(input.getDrawAccName(), ""));

        // 업무구분값 (1:신규, 2:추가, 3:변경, 4:삭제)
        output.setWorkType(StringUtils.defaultIfEmpty(input.getWorkType(), ""));

        try {

            // 공통부 세팅
            OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H92B");
            hostCfg.setImsTranCd("TI1IBK01");
            hostCfg.setInClassCd("H92B");
            hostCfg.setSvcCd("92B");
            hostCfg.setCaptureSystem("OLTP");
            // sendData.putHostWebComm("UserIP" , request.getRemoteAddr());

            // 개별부 세팅
            CbIbk01H92B00Req sendData = new CbIbk01H92B00Req();
            sendData.setTranServiceGB("R"); // 거래구분(R: 조회)
            sendData.setJHTranServiceGB("1"); // 조회구분(1: 상세조회)
            sendData.setTongjiAccutNum1(StringUtils.defaultIfEmpty(input.getDrawAccNum(), "")); // 계좌번호

            // 전문 호출
            OltpResponse<CbIbk01H92B00Res> oltpResponse = this.hostClient.sendOltp(hostCfg, sendData,
                    CbIbk01H92B00Res.class);
            CbIbk01H92B00Res cbIbk01H92B00Res = oltpResponse.getResponse();

            // 통지거래금액 - 금액
            output.setTongjiMoney(cbIbk01H92B00Res.getTongjiMoney());

            // 무통입금통지구분 - 통지범위
            String _YIMUINObj = cbIbk01H92B00Res.getMutongInGB1();

            // 무통출금통지구분 - 통지범위
            String _YIMUOUObj = cbIbk01H92B00Res.getMutongOutGB1();

            if (_YIMUINObj.equals("1") && _YIMUOUObj.equals("1")) {
                output.setMutongGB("1");
            } else if (_YIMUINObj.equals("1") && _YIMUOUObj.equals("")) {
                output.setMutongGB("2");
            } else if (_YIMUINObj.equals("") && _YIMUOUObj.equals("1")) {
                output.setMutongGB("3");
            } else {
                output.setMutongGB("");
            }

            // 거래명세시작시간 - 알림수신 제외시간 시작
            output.setTransferStartTime(cbIbk01H92B00Res.getTransferStartTime());

            // 거래명세종료시간 - 알림수신 제외시간 종료
            output.setTransferOutTime(cbIbk01H92B00Res.getTransferOutTime());

            // 센터처리통보구분 - 시간 설정
            output.setCenterProcessGB(cbIbk01H92B00Res.getCenterProcessGB());

            // 통지범위 - 잔액범위
            output.setYiJANMU(cbIbk01H92B00Res.getYIJANMU());

            // 고객 푸쉬설정 상태
            output.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
            output.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
            output.setFinanceVal(StringUtils.defaultIfEmpty(input.getFinanceVal(), ""));
            output.setIotranlistFlag(StringUtils.defaultIfEmpty(input.getIotranlistFlag(), ""));
            output.setNotyExrateFlg(StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), ""));
            output.setWmloungeFlag(StringUtils.defaultIfEmpty(input.getWmloungeFlag(), ""));

        } catch (Exception e) {
        }

        return output;
    }

    /**
     * 마케팅 혜택 안내 팝업
     * 
     * @param IServiceContext
     * @param SupPntSetMarketingNoticePopRequest
     * @return SupPntSetMarketingNoticePopResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_341S
     */
    @ServiceEndpoint(url = "/setMarketingNoticePop", name = "마케팅 혜택 안내 팝업 [ASIS:MA3MSCNTF002_341S]", author = "김기주")
    public SupPntSetMarketingNoticePopResponse setMarketingNoticePop(IServiceContext ctx,
            SupPntSetMarketingNoticePopRequest input) throws PRCServiceException {

        SupPntSetMarketingNoticePopResponse output = new SupPntSetMarketingNoticePopResponse();
        // PRVCMM009003
        String prvsnCd = StringUtils.defaultIfEmpty(input.getPrvsnCd(), "").trim();
        String actionType = StringUtils.defaultIfEmpty(input.getActionType(), "").trim();
        String viewDepth = StringUtils.defaultIfEmpty(input.getViewDepth(), "").trim();
        String benefitAgreeFlag = StringUtils.defaultIfEmpty(input.getBenefitAgreeFlag(), "").trim();
        String serno = StringUtils.defaultIfEmpty(input.getSerno(), "").trim();
        String benefitFlag = StringUtils.defaultIfEmpty(input.getBenefitFlag(), "").trim();
        String financeFlag = StringUtils.defaultIfEmpty(input.getFinanceFlag(), "").trim();
        String financeVal = StringUtils.defaultIfEmpty(input.getFinanceVal(), "").trim();
        String wmloungeFlag = StringUtils.defaultIfEmpty(input.getWmloungeFlag(), "").trim();
        String iotranListFlag = StringUtils.defaultIfEmpty(input.getIotranListFlag(), "").trim();
        String notyExrateFlag = StringUtils.defaultIfEmpty(input.getNotyExrateFlag(), "").trim();
        String AGRMNT_MK = StringUtils.defaultIfEmpty(input.getAgrmntMk(), "").trim();
        String agreeFlag = StringUtils.defaultIfEmpty(input.getAgreeFlag(), "").trim();

        List<String> loctnCdList = new ArrayList<>();
        loctnCdList.add("LN1001");

        log.debug("------------benefitAgreeFlag--------------- : " + benefitAgreeFlag);

        // 국문모드가 아닌 경우 영문까지 조회되게 영문언어구분 추가
        Locale locale = LocaleContextHolder.getLocale();
        String langCode = locale.getLanguage();

        if (!"ko".equals(langCode)) {
            loctnCdList.add("LN1002");
        }

        TermsInfoParameter parameter = TermsInfoParameter
                .builder()
                .actionType(actionType)
                .loctnCdList(loctnCdList)
                .prvsnCd(prvsnCd)
                .build();

        // 약관정보 가져오기
        List<TermsInfoResult> result = this.ma30PrdctPrvsnMgtDao.selectTermsInfo(parameter);
        if (result == null || result.isEmpty()) {
            throw new PRCServiceException("MA3CMM0035", "등록된 약관이 없습니다. 약관정보를 확인해 주세요.");
        }

        List<SupPntSetMarketingNoticePopResponse.TermsInfo> termsInfo = this.supportPushNotificationMapper
                .toTermsInfoList(result);

        output.setTermsInfo(termsInfo);
        output.setViewDepth(viewDepth);
        output.setBenefitAgreeFlag(benefitAgreeFlag);
        output.setSerno(serno);
        output.setBenefitFlag(benefitFlag);
        output.setFinanceFlag(financeFlag);
        output.setFinanceVal(financeVal);
        output.setWmloungeFlag(wmloungeFlag);
        output.setIotranListFlag(iotranListFlag);
        output.setNotyExrateFlag(notyExrateFlag);
        output.setAgrmntMk(AGRMNT_MK);
        output.setAgreeFlag(agreeFlag);

        return output;
    }

    /**
     * 입출금 내역 푸시 등록/수정/삭제
     * 
     * @param IServiceContext
     * @param SupPntUpdateTransferPushRequest
     * @return SupPntUpdateTransferPushResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_305S
     */
    @ServiceEndpoint(url = "/updateTransferPush", name = "입출금 내역 푸시 등록/수정/삭제 [ASIS:MA3MSCNTF002_305S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntUpdateTransferPushResponse updateTransferPush(IServiceContext ctx,
            SupPntUpdateTransferPushRequest input) throws PRCServiceException {
        HttpServletRequest request = ctx.request();
        SupPntUpdateTransferPushResponse output = new SupPntUpdateTransferPushResponse();

        // 업무구분값 (1:신규, 2:추가, 3:변경, 4:삭제)
        String workType = StringUtils.defaultIfEmpty(input.getWorkType(), "");
        // 계좌명
        String drawAccName = "";
        // 통지구분
        String mutongGB = "";
        // 통지금액
        String noticeMoney = "0";
        // 시간설정
        String timeSet = "";
        // 시간설정-시작
        String timeSetStart = "";
        // 시간설정-종료
        String timeSetEnd = "";
        // 잔액표시
        String balanceShow = "";
        // 입출금 푸쉬알림 활성화 설정상태 Y/N Y-활성화 N-비활성화
        String IOTRANLIST_FLAG = StringUtils.defaultIfEmpty(input.getIotranlistFlag(), "");

        if (!workType.equals("4")) {
            // 계좌명
            drawAccName = StringUtils.defaultIfEmpty(input.getDrawAccName(), "");
            // 통지구분
            mutongGB = StringUtils.defaultIfEmpty(input.getMutongGB(), "");
            // 통지금액
            noticeMoney = StringUtils.defaultIfEmpty(input.getTongjiMoney(), "0");
            // 시간설정
            timeSet = StringUtils.defaultIfEmpty(input.getCenterProcessGB(), "");
            // 시간설정-시작
            timeSetStart = StringUtils.defaultIfEmpty(input.getTransferStartTime(), "");
            // 시간설정-종료
            timeSetEnd = StringUtils.defaultIfEmpty(input.getTransferOutTime(), "");
            // 잔액표시
            balanceShow = StringUtils.defaultIfEmpty(input.getYiJANMU(), "");
        }

        // 계좌번호
        String accountNo = StringUtils.defaultIfEmpty(input.getTongjiAccutNum1(), "");
        // 계좌비밀번호
        String accountPassword = StringUtils.defaultIfEmpty(input.getAcctPasswd(), "");

        // 고객일련번호
        String serno = StringUtils.defaultIfEmpty(input.getSerno(), "");

        if ("".equals(serno)) {
            throw new PRCServiceException("처리중 오류가 발생하였습니다. [updateTransferPush]");
        }

        try {

            if (!timeSet.equals("3")) {
                timeSetStart = "";
                timeSetEnd = "";
            }

            if (workType.equals("1") || workType.equals("2")) {
                int acctCnt = this.tmbBkstatMgtDao.selectTransferPushAcctCnt(accountNo);

                log.debug("acctCnt : " + acctCnt);
                if (acctCnt > 0) {
                    log.debug("등록조건IN : " + acctCnt);
                    this.tmbBkstatMgtDao.deleteTransferPushDataByAcctNo(accountNo);
                }

                InsertTransferPushDataParameter insertTransferPushDataParameter = new InsertTransferPushDataParameter();
                insertTransferPushDataParameter.setBsAcctNo(accountNo);
                insertTransferPushDataParameter.setBsPuserno(serno);
                insertTransferPushDataParameter.setBsAcctNm(drawAccName);
                insertTransferPushDataParameter.setBsNotiRg(mutongGB);
                insertTransferPushDataParameter.setBsAmount(noticeMoney);
                insertTransferPushDataParameter.setBsTimeFg(timeSet);
                insertTransferPushDataParameter.setBsStartTm(timeSetStart);
                insertTransferPushDataParameter.setBsEndTm(timeSetEnd);
                insertTransferPushDataParameter.setBsBalanMk(balanceShow);

                this.tmbBkstatMgtDao.insertTransferPushData(insertTransferPushDataParameter);
            } else if (workType.equals("3")) {
                UpdateTransferPushDataParameter updateTransferPushDataParameter = new UpdateTransferPushDataParameter();
                updateTransferPushDataParameter.setBsAcctNo(accountNo);
                updateTransferPushDataParameter.setBsPuserno(serno);
                updateTransferPushDataParameter.setBsNotiRg(mutongGB);
                updateTransferPushDataParameter.setBsAmount(noticeMoney);
                updateTransferPushDataParameter.setBsTimeFg(timeSet);
                updateTransferPushDataParameter.setBsStartTm(timeSetStart);
                updateTransferPushDataParameter.setBsEndTm(timeSetEnd);
                updateTransferPushDataParameter.setBsBalanMk(balanceShow);

                this.tmbBkstatMgtDao.updateTransferPushData(updateTransferPushDataParameter);
            }

            // 공통부 세팅
            OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_D92E");
            hostCfg.setImsTranCd("TI1IBK01");
            hostCfg.setInClassCd("D92E");
            hostCfg.setSvcCd("92B");
            hostCfg.setCaptureSystem("OLTP");
            // sendData.putHostWebComm("UserIP" , request.getRemoteAddr());

            // 개별부 세팅
            CbIbk01D92E00Req sendData = new CbIbk01D92E00Req();
            if (workType.equals("1")) { // 거래구분(1: 신규신청(최초등록 0건 일때))
                sendData.setTranServiceGB("1");
            } else if (workType.equals("2")) { // 추가
                sendData.setTranServiceGB("4"); // 거래구분(4: 추가)
            } else if (workType.equals("3")) { // 변경
                sendData.setTranServiceGB("2"); // 거래구분(2: 변경)
            } else if (workType.equals("4")) { // 삭제
                sendData.setTranServiceGB("3"); // 거래구분(3: 삭제)
            }

            sendData.setTongjiAccutNum1(accountNo); // 통지계좌번호
            sendData.setAcctPasswd(accountPassword); // 통지계좌비밀번호

            if (workType.equals("1") || workType.equals("2") || workType.equals("3")) {
                if (mutongGB.equals("1")) {
                    sendData.setMutongInGB1("1"); // 무통입금통지구분
                    sendData.setMutongOutGB1("1"); // 무통출금통지구분
                } else if (mutongGB.equals("2")) {
                    sendData.setMutongInGB1("1"); // 무통입금통지구분
                    sendData.setMutongOutGB1(""); // 무통출금통지구분
                } else if (mutongGB.equals("3")) {
                    sendData.setMutongInGB1(""); // 무통입금통지구분
                    sendData.setMutongOutGB1("1"); // 무통출금통지구분
                }

                sendData.setTongjiMoney(noticeMoney); // 통지금액
                sendData.setCenterProcessGB(timeSet); // 시간설정(센터처리통보구분)
                sendData.setTransferStartTime(timeSetStart); // 시간설정(거래명세시작시간)
                sendData.setTransferOutTime(timeSetEnd); // 시간설정(거래명세종료시간)
                sendData.setYIJANMU(balanceShow); // 잔액표시
            }

            sendData.setUserID(sessionManager.getLoginValue("UserID", String.class));
            sendData.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));

            // 전문 호출
            this.hostClient.sendOltp(hostCfg, sendData, CbIbk01D92E00Res.class);

            if (workType.equals("4")) {
                DeleteTransferPushDataParameter deleteTransferPushDataParameter = new DeleteTransferPushDataParameter();
                deleteTransferPushDataParameter.setBsAcctNo(accountNo);
                deleteTransferPushDataParameter.setBsPuserno(serno);

                this.tmbBkstatMgtDao.deleteTransferPushData(deleteTransferPushDataParameter);
            }

            String benefitFlag = StringUtils.defaultIfEmpty(input.getBenefitFlag(), "");
            String financeFlag = StringUtils.defaultIfEmpty(input.getFinanceFlag(), "");
            String wmloungeFlag = StringUtils.defaultIfEmpty(input.getWmloungeFlag(), "");
            String notyExrateFlag = StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), "");
            String agrmntMk = "9";

            SupPntUpdateAlarmServiceRequest supPntUpdateAlarmServiceRequest = new SupPntUpdateAlarmServiceRequest();
            supPntUpdateAlarmServiceRequest.setSerno(serno);
            supPntUpdateAlarmServiceRequest.setBenefitFlag(benefitFlag);
            supPntUpdateAlarmServiceRequest.setFinanceFlag(financeFlag);
            supPntUpdateAlarmServiceRequest.setWmloungeFlag(wmloungeFlag);
            supPntUpdateAlarmServiceRequest.setNotyExrateFlg(notyExrateFlag);
            supPntUpdateAlarmServiceRequest.setAgrmntMk(agrmntMk);

            if (workType.equals("1") || workType.equals("2")) {
                supPntUpdateAlarmServiceRequest.setIotranlistFlag("Y");
                this.supportPushNotificationComponent.updateAlarmService(ctx, supPntUpdateAlarmServiceRequest);
            } else if (workType.equals("4")) {
                String acctSum = StringUtils.defaultIfEmpty(input.getAcctSum(), "");
                if (acctSum.equals("1")) {
                    supPntUpdateAlarmServiceRequest.setIotranlistFlag("N");
                    this.supportPushNotificationComponent.updateAlarmService(ctx, supPntUpdateAlarmServiceRequest);
                }
            }
        } catch (OltpSystemException oltpSystemException) {
            this.tmbBkstatMgtDao.deleteTransferPushDataByAcctNo(accountNo);

            PRCServiceException exception = new PRCServiceException(oltpSystemException.getErrorCode(),
                    oltpSystemException.getErrorModule());
            exception.setErrorMessage(oltpSystemException.getErrorMessage());

            throw exception;
        } catch (Exception e) {
            e.printStackTrace();

            this.tmbBkstatMgtDao.deleteTransferPushDataByAcctNo(accountNo);
            throw new PRCServiceException(e);
        }

        // 고객 푸쉬설정 상태
        output.setSerno(serno);
        output.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
        output.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
        output.setFinanceVal(StringUtils.defaultIfEmpty(input.getFinanceVal(), ""));
        output.setWmloungeFlag(StringUtils.defaultIfEmpty(input.getWmloungeFlag(), ""));
        output.setIotranlistFlag(IOTRANLIST_FLAG);
        output.setNotyExrateFlg(StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), ""));
        return output;
    }

    /**
     * 환율 조회
     * 
     * @param IServiceContext
     * @param SupPntListPushRateRequest
     * @return SupPntListPushRateResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_401S
     */
    @ServiceEndpoint(url = "/listPushRate", name = "환율 조회 [ASIS:MA3MSCNTF002_401S]", author = "김기주")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntListPushRateResponse listPushRate(IServiceContext ctx, SupPntListPushRateRequest input)
            throws PRCServiceException {

        SupPntListPushRateResponse output = new SupPntListPushRateResponse();

        List<SelectExchangeInfoResult> rateList = this.exchange100Dao.selectExchangeInfo();

        if (!rateList.isEmpty()) {
            // 환율조회
            SelectExchangeInfoResult DATETIME = rateList.get(0);
            output.setRateData(this.supportPushNotificationMapper.toExchangeInfo(rateList));

            // 환율 조회 시간
            output.setDateTime(DATETIME.getDateTime());
        }

        // 환율등록정보
        log.debug("KTS : 환율등록정보 start");
        SupPntListPushCustomerRateRegistInfoRequest supPntListPushCustomerRateRegistInfoRequest = new SupPntListPushCustomerRateRegistInfoRequest();
        supPntListPushCustomerRateRegistInfoRequest.setSerno(input.getSerno());

        SupPntListPushCustomerRateRegistInfoResponse supPntListPushCustomerRateRegistInfoResponse = this.supportPushNotificationComponent
                .listPushCustomerRateRegistInfo(ctx,
                        supPntListPushCustomerRateRegistInfoRequest);

        List<SupPntListPushCustomerRateRegistInfoResponse.ExrateData> exrateDataList = supPntListPushCustomerRateRegistInfoResponse
                .getDbData();

        output.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
        output.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
        output.setFinanceVal(StringUtils.defaultIfEmpty(input.getFinanceVal(), ""));
        output.setWmloungeFlag(StringUtils.defaultIfEmpty(input.getWmloungeFlag(), ""));
        output.setIotranlistFlag(StringUtils.defaultIfEmpty(input.getIotranlistFlag(), ""));
        output.setNotyExrateFlg(StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), ""));

        if (exrateDataList != null && !exrateDataList.isEmpty()) {
            output.setDbData(this.supportPushNotificationMapper.toExrateDataList(exrateDataList));
        }

        output.setPushId(StringUtils.defaultIfEmpty(input.getSerno(), ""));

        return output;
    }

    /**
     * 환율 푸쉬알림 등록/수정/삭제
     * 
     * @param IServiceContext
     * @param SupPntUpdateRatePushAlarmRequest
     * @return SupPntUpdateRatePushAlarmResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_403S
     */
    @ServiceEndpoint(url = "/updateRatePushAlarm", name = "환율 푸쉬알림 등록/수정/삭제 [ASIS:MA3MSCNTF002_403S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntUpdateRatePushAlarmResponse updateRatePushAlarm(IServiceContext ctx,
            SupPntUpdateRatePushAlarmRequest input) throws PRCServiceException {

        SupPntUpdateRatePushAlarmResponse output = new SupPntUpdateRatePushAlarmResponse();

        String allListCount = StringUtils.defaultIfEmpty(input.getAllListCount(), "");
        List<SupPntUpdateRatePushAlarmRequest.PushData> pushDataList = input.getPushDataList();

        for (int index = 0; index < pushDataList.size(); index++) {
            // input에서 받은 값 추출하여 insert update delete 문을 연속 처리
            SupPntUpdateRatePushAlarmRequest.PushData pushData = pushDataList.get(index);

            if (pushData.getDocType().equals("UPDATE")) {
                log.debug("updateRatePushAlarm DATA [UPDATE] : " + pushData.toString());
                UpdateExratePushDataParameter parameter = new UpdateExratePushDataParameter();
                parameter.setType(StringUtils.defaultIfEmpty(pushData.getType(), ""));
                parameter.setFitRate(StringUtils.defaultIfEmpty(pushData.getFitRate(), ""));
                parameter.setPushDay(StringUtils.defaultIfEmpty(pushData.getPushDay(), ""));
                parameter.setPuserno(StringUtils.defaultIfEmpty(input.getPushId(), ""));
                parameter.setPushTime(StringUtils.defaultIfEmpty(pushData.getPushTime(), ""));
                parameter.setSeqNo(StringUtils.defaultIfEmpty(pushData.getSeqNo(), ""));
                log.debug("updateRatePushAlarm DB PARAMETER [UPDATE] : " + parameter.toString());
                this.tmbExrateMgtDao.updateExratePushData(parameter);
            } else if (pushData.getDocType().equals("INSERT")) {
                log.debug("updateRatePushAlarm DATA [INSERT] : " + pushData.toString());
                InsertExratePushDataParameter parameter = new InsertExratePushDataParameter();
                parameter.setGb(StringUtils.defaultIfEmpty(pushData.getGb(), ""));
                parameter.setCurrnm(StringUtils.defaultIfEmpty(pushData.getCurrnm(), ""));
                parameter.setType(StringUtils.defaultIfEmpty(pushData.getType(), ""));
                parameter.setFitRate(StringUtils.defaultIfEmpty(pushData.getFitRate(), ""));
                parameter.setPushDay(StringUtils.defaultIfEmpty(pushData.getPushDay(), ""));
                parameter.setPushTime(StringUtils.defaultIfEmpty(pushData.getPushTime(), ""));
                parameter.setPuSerno(StringUtils.defaultIfEmpty(input.getPushId(), ""));
                log.debug("updateRatePushAlarm DB PARAMETER [INSERT] : " + parameter.toString());
                this.tmbExrateMgtDao.insertExratePushData(parameter);
            } else {
                log.debug("updateRatePushAlarm DATA [DELETE] : " + pushData.toString());
                DeleteExratePushDataParameter parameter = new DeleteExratePushDataParameter();
                parameter.setPuSerno(StringUtils.defaultIfEmpty(input.getPushId(), ""));
                parameter.setSeqNo(StringUtils.defaultIfEmpty(pushData.getSeqNo(), ""));
                log.debug("updateRatePushAlarm DB PARAMETER [DELETE] : " + parameter.toString());
                this.tmbExrateMgtDao.deleteExratePushData(parameter);
            }
        }

        SupPntUpdateAlarmServiceRequest supPntUpdateAlarmServiceRequest = new SupPntUpdateAlarmServiceRequest();
        supPntUpdateAlarmServiceRequest.setSerno(StringUtils.defaultIfEmpty(input.getSerno(), ""));
        supPntUpdateAlarmServiceRequest.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
        supPntUpdateAlarmServiceRequest.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
        supPntUpdateAlarmServiceRequest.setWmloungeFlag(StringUtils.defaultIfEmpty(input.getWmloungeFlag(), ""));
        supPntUpdateAlarmServiceRequest.setIotranlistFlag(StringUtils.defaultIfEmpty(input.getIotranlistFlag(), ""));
        supPntUpdateAlarmServiceRequest.setAgrmntMk(StringUtils.defaultIfEmpty(input.getAgrmntMk(), "2"));

        if (allListCount.equals("0")) {
            supPntUpdateAlarmServiceRequest.setNotyExrateFlg("N");
            this.supportPushNotificationComponent.updateAlarmService(ctx, supPntUpdateAlarmServiceRequest);
        } else {
            supPntUpdateAlarmServiceRequest.setNotyExrateFlg("Y");
            this.supportPushNotificationComponent.updateAlarmService(ctx, supPntUpdateAlarmServiceRequest);
        }

        return output;
    }

    /**
     * 서비스 알림 동의 등록/수정
     * 
     * @param IServiceContext
     * @param SupPntUpdateServicePushAlarmRequest
     * @return SupPntUpdateServicePushAlarmResponse
     * @throws PRCServiceException
     */
    @ServiceEndpoint(url = "/updateServicePushAlarm", name = "서비스 알림 동의 등록/수정", author = "김기주")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntUpdateServicePushAlarmResponse updateServicePushAlarm(IServiceContext ctx,
            SupPntUpdateServicePushAlarmRequest input) throws PRCServiceException {

        String userId = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class), "");

        List<SupPntUpdateServicePushAlarmRequest.ServicePushAlarm> servicePushAlarmList = input
                .getServicePushAlarmList();

        if (servicePushAlarmList != null && !servicePushAlarmList.isEmpty()) {
            for (SupPntUpdateServicePushAlarmRequest.ServicePushAlarm alarm : servicePushAlarmList) {
                InsertUserPushAgreeParameter parameter = new InsertUserPushAgreeParameter();
                parameter.setMsgTypeSubCd(alarm.getMsgTypeSubCd());
                parameter.setUseYn(alarm.getUseYn());
                parameter.setUserId(userId);
                this.ma30SedMsgAgrmntDao.insertUserPushAgree(parameter);
            }
        }

        SupPntUpdateServicePushAlarmResponse output = new SupPntUpdateServicePushAlarmResponse();
        return output;
    }

}
