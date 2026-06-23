package com.scbank.process.api.svc.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92B00Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92B00Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.dto.GetPushAlarmServiceTermsAgreeRequest;
import com.scbank.process.api.svc.common.components.dto.GetPushAlarmServiceTermsAgreeResponse;
import com.scbank.process.api.svc.common.components.dto.SetTransferListPushSettingPageRequest;
import com.scbank.process.api.svc.common.components.dto.SetTransferListPushSettingPageResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoRequest;
import com.scbank.process.api.svc.common.components.dto.SupPntListPushCustomerRateRegistInfoResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateAlarmServiceRequest;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateAlarmServiceResponse;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateMarketingNoticeTermsAgreeRequest;
import com.scbank.process.api.svc.common.components.dto.SupPntUpdateMarketingNoticeTermsAgreeResponse;
import com.scbank.process.api.svc.common.dao.PushCllctUsageAgrmntDao;
import com.scbank.process.api.svc.common.dao.TmbExrateMgtDao;
import com.scbank.process.api.svc.common.dao.TmbObjUsrMgtDao;
import com.scbank.process.api.svc.common.dao.dto.InsertPushCllctUsageAgrmntHistoryParameter;
import com.scbank.process.api.svc.common.dao.dto.InsertPushCllctUsageAgrmntParameter;
import com.scbank.process.api.svc.common.dao.dto.PushJoinAlarmListResult;
import com.scbank.process.api.svc.common.dao.dto.PushJoinStatusResult;
import com.scbank.process.api.svc.common.dao.dto.SelectExratePushDataResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushCllctUsageAgrmntResult;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushCllctUsageAgrmntParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushJoinAlarmParameter;
import com.scbank.process.api.svc.common.mapper.SupportPushNotificationComponentMapper;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.integration.HostClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "푸시알림 서비스조회/설정 컴포넌트", description = "푸시알림 서비스조회/설정 컴포넌트", author = "김기주")
public class SupportPushNotificationComponent {

    private final AccountListComponent accountListComponent;

    private final TmbObjUsrMgtDao tmbObjUsrMgtDao;

    private final HostClient hostClient;

    private final PushCllctUsageAgrmntDao pushCllctUsageAgrmntDao;

    private final TmbExrateMgtDao tmbExrateMgtDao;

    private final SupportPushNotificationComponentMapper supportPushNotificationComponentMapper;

    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    /**
     * 입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회
     *
     * @param SetTransferListPushSettingPageRequest
     * @return SetTransferListPushSettingPageResponse
     * @throws Exception
     * @description MA3MSCNTF002_302S
     */
    @ComponentOperation(name = "입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회 [ASIS:MA3MSCNTF002_302S]", author = "김기주")
    public SetTransferListPushSettingPageResponse setTransferListPushSettingPage(IServiceContext ctx,
            SetTransferListPushSettingPageRequest input) {

        HttpServletRequest request = ctx.request();
        // DB조회 입력값
        String userId = sessionManager.getLoginValue("UserID", String.class);

        // HOST 계좌 저장
        HashMap<String, String> getH92BAccNoData = new HashMap<>();

        // DB 계좌 저장
        HashMap<String, String> getDBAccNoData = new HashMap<>();

        // PUSH DB 계좌
        HashMap<String, String> pushDBAccNo = new HashMap<>();

        // 설정완료된 계좌
        List<SetTransferListPushSettingPageResponse.Acct> setupOK = new ArrayList<>();

        // 미설정 계좌
        List<SetTransferListPushSettingPageResponse.Acct> setupNO = new ArrayList<>();

        // DB조회 결과 저장
        List<PushJoinAlarmListResult> pushJoinAlarmYnList = null;

        // HOST SUB 결과 저장
        List<CbIbk01H92B00Res.TotServiceJoinData> resH926Sub = new ArrayList<>();

        SetTransferListPushSettingPageResponse output = new SetTransferListPushSettingPageResponse();

        // PRCSharedUtils.getIpinsideIp();

        // 등록된 계좌수
        int OKCount = 0;
        // 등록된 계좌수
        int NOCount = 0;

        // 전계좌
        List<AllAccountInfo> ssoDepositAccountList = this.accountListComponent.getAllAccountList();
        List<AllAccountInfo> depositAccountList = new ArrayList<>();

        if (ssoDepositAccountList == null || ssoDepositAccountList.size() <= 0) {
            // 11.09 목표환율매매 완료 > 푸시알림 진입 시 세션이 비어있어 오류현상발생하여 세션 비어있을경우 재거래하게 수정
            ssoDepositAccountList = this.accountListComponent.getAllAccountList(true);
        }

        for (AllAccountInfo r : ssoDepositAccountList) {
            String acctNum = StringUtils.defaultIfEmpty(r.getDrawAcctNum(), "");
            String kwamok = acctNum.substring(3, 5);

            log.debug("####SSH####출금계좌 : " + acctNum + " AcctType : " + r.getAcctType() + " DrawYN : " + r.getDrawYn());

            // 예금계좌추출(10,20,30)
            if (kwamok.equals("10") || kwamok.equals("20") || kwamok.equals("30")) {
                if (r.getAcctType().equals("1") && r.getDrawYn().equals("1")) {
                    log.debug("####SSH####계좌 : " + acctNum);
                    depositAccountList.add(r);
                }
            }
        }

        // 공통부 세팅
        OltpRequestOptions hostCfg = this.hostClient.getOltpRequestOptions("CB_IBK01_H92B");
        hostCfg.setImsTranCd("TI1IBK01");
        hostCfg.setInClassCd("H92B");
        hostCfg.setSvcCd("92B");
        hostCfg.setCaptureSystem("OLTP");
        // sendData.putHostWebComm("UserIP" , request.getRemoteAddr());

        // 개별부 세팅
        CbIbk01H92B00Req sendData = new CbIbk01H92B00Req();
        sendData.setTranServiceGB("R");
        sendData.setJHTranServiceGB("9");
        sendData.setUserID(sessionManager.getLoginValue("UserID", String.class));
        sendData.setTSPassword(sessionManager.getLoginValue("TSPassword", String.class));

        // 전문 호출
        OltpResponse<CbIbk01H92B00Res> oltpResponse = this.hostClient.sendOltp(hostCfg, sendData,
                CbIbk01H92B00Res.class);
        CbIbk01H92B00Res cbIbk01H92B00Res = oltpResponse.getResponse();
        resH926Sub = cbIbk01H92B00Res.getTotServiceJoinData();

        pushJoinAlarmYnList = tmbObjUsrMgtDao.selectPushJoinAlarmList(userId);

        // DB 조회결과 저장
        if (pushJoinAlarmYnList != null && !pushJoinAlarmYnList.isEmpty()) {
            for (int i = 0; i < pushJoinAlarmYnList.size(); i++) {
                log.debug("BS_ACCT_NO : " + pushJoinAlarmYnList.get(i).getBsAcctNo());

                if (i == 0) {
                    if (pushJoinAlarmYnList.get(i).getCnfrmNo() == null
                            || "".equals(pushJoinAlarmYnList.get(i).getCnfrmNo())) {
                        output.setCnfrmNo("");
                    } else {
                        output.setCnfrmNo(pushJoinAlarmYnList.get(i).getCnfrmNo());
                    }
                    if (pushJoinAlarmYnList.get(i).getAppGb() == null
                            || "".equals(pushJoinAlarmYnList.get(i).getAppGb())) {
                        output.setAppGb("");
                    } else {
                        output.setAppGb(pushJoinAlarmYnList.get(i).getAppGb());
                    }
                }
                getDBAccNoData.put(pushJoinAlarmYnList.get(i).getBsAcctNo(), pushJoinAlarmYnList.get(i).getBsAcctNo());
            }
        } else {
            /*
             * 등록된 계좌가 없을 경우 기기변경 되었다고 나오는 버그 수정
             * 푸쉬등록 정보를 가져와 등록된 기기토큰 값을 출력한다.
             */
            PushJoinStatusResult pushJoinAlarmYn = this.tmbObjUsrMgtDao.selectPushJoinAlarm(userId);
            if (pushJoinAlarmYn != null) {
                if (pushJoinAlarmYn.getCnfrmNo() == null || "".equals(pushJoinAlarmYn.getCnfrmNo())) {
                    output.setCnfrmNo("");
                } else {
                    output.setCnfrmNo(pushJoinAlarmYn.getCnfrmNo());
                }
                if (pushJoinAlarmYn.getAppGb() == null || "".equals(pushJoinAlarmYn.getAppGb())) {
                    output.setAppGb("");
                } else {
                    output.setAppGb(pushJoinAlarmYn.getAppGb());
                }
            }
        }

        if (resH926Sub != null) {
            // H92B 조회결과 저장
            if (resH926Sub.size() > 0) {
                for (int i = 0; i < resH926Sub.size(); i++) {
                    log.debug("TongjiAcctNum : " + resH926Sub.get(i).getTongjiAcctNum());
                    // 대상계좌번호(명세통지:계좌번호, 환율통지:환율코드)
                    getH92BAccNoData.put(resH926Sub.get(i).getTongjiAcctNum(), resH926Sub.get(i).getTongjiAcctNum());
                }
            }
        }

        // PUSH DB 계좌 설정
        for (int i = 0; i < depositAccountList.size(); i++) {
            String depositAccNo = depositAccountList.get(i).getDrawAcctNum();
            String depositAccName = depositAccountList.get(i).getDrawAcctName();

            if (getDBAccNoData.containsKey(depositAccNo) && getH92BAccNoData.containsKey(depositAccNo)) {
                SetTransferListPushSettingPageResponse.Acct map = new SetTransferListPushSettingPageResponse.Acct();
                if (pushJoinAlarmYnList != null) {
                    for (int j = 0; j < pushJoinAlarmYnList.size(); j++) {
                        if (getH92BAccNoData.containsKey(pushJoinAlarmYnList.get(j).getBsAcctNo())) {
                            map.setSerno(pushJoinAlarmYnList.get(j).getSerNo());
                        }
                    }
                }
                map.setDrawAcctNum(depositAccNo);
                map.setDrawAcctName(depositAccName);
                setupOK.add(map);
                pushDBAccNo.put(depositAccNo, depositAccNo);
                OKCount++;
            }
        }

        // 공동망 DB에 등록된 계좌는 무조건 등록
        if (resH926Sub != null) {
            for (int i = 0; i < resH926Sub.size(); i++) {
                String H92BAccNo = resH926Sub.get(i).getTongjiAcctNum();

                log.debug("pushDBAccNo.containsKey(H92BAccNo) : " + pushDBAccNo.containsKey(H92BAccNo));
                if (!pushDBAccNo.containsKey(H92BAccNo)) {
                    SetTransferListPushSettingPageResponse.Acct map = new SetTransferListPushSettingPageResponse.Acct();
                    map.setDrawAcctNum(H92BAccNo);
                    setupOK.add(map);
                    pushDBAccNo.put(H92BAccNo, H92BAccNo);
                    OKCount++;
                }
            }
        }

        // 푸시 미등록 계좌 설정
        for (int i = 0; i < depositAccountList.size(); i++) {
            String depositAccNo = depositAccountList.get(i).getDrawAcctNum();
            String depositAccName = depositAccountList.get(i).getDrawAcctName();
            log.debug("####SSH####미등록계좌 : " + depositAccNo);
            if (!pushDBAccNo.containsKey(depositAccNo)) {
                SetTransferListPushSettingPageResponse.Acct map = new SetTransferListPushSettingPageResponse.Acct();
                map.setDrawAcctNum(depositAccNo);
                map.setDrawAcctName(depositAccName);
                setupNO.add(map);
                NOCount++;
            }
        }

        // 등록된 계좌
        output.setRegistAcctlist(setupOK);
        // 미등록된 계좌
        output.setNonRegistAcctlist(setupNO);
        // 등록된 계좌 수
        output.setOkCount(OKCount);
        // 미등록된 계좌 수
        output.setNoCount(NOCount);
        // 고객일련번호
        output.setSerno(StringUtils.defaultIfEmpty(input.getSerno(), ""));
        // 고객 푸쉬설정 상태
        output.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
        output.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
        output.setFinanceVal(StringUtils.defaultIfEmpty(input.getFinanceVal(), ""));
        output.setIotranlistFlag(StringUtils.defaultIfEmpty(input.getIotranlistFlag(), ""));
        output.setNotyExrateFlg(StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), ""));
        output.setWmloungeFlag(StringUtils.defaultIfEmpty(input.getWmloungeFlag(), ""));

        log.debug("REGIST_ACCTLIST size : " + setupOK.size());
        log.debug("NON_REGIST_ACCTLIST : " + setupNO.size());
        log.debug("OKCount : " + OKCount);
        log.debug("NOCount : " + NOCount);

        return output;
    }

    /**
     * 푸시 알림 서비스 이용 동의 원장 조회
     *
     * @param GetPushAlarmServiceTermsAgreeRequest
     * @return GetPushAlarmServiceTermsAgreeResponse
     * @throws Exception
     * @description MA3MSCNTF002_310S
     */
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    @ComponentOperation(name = "푸시 알림 서비스 이용 동의 원장 조회 [ASIS:MA3MSCNTF002_310S]", author = "김기주")
    public GetPushAlarmServiceTermsAgreeResponse getPushAlarmServiceTermsAgree(IServiceContext ctx,
            GetPushAlarmServiceTermsAgreeRequest input) {

        GetPushAlarmServiceTermsAgreeResponse output = new GetPushAlarmServiceTermsAgreeResponse();

        String ssn = StringUtils.defaultIfEmpty(input.getSsn(), "");

        SelectPushCllctUsageAgrmntResult result = this.pushCllctUsageAgrmntDao.selectPushCllctUsageAgrmnt(ssn);

        if (result != null) {
            output.setAgrmntMk(result.getAgrmntMk() == null ? "2" : result.getAgrmntMk());
        } else {
            output.setAgrmntMk("2");
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
    @ComponentOperation(name = "알림 서비스 등록 및 변경 [ASIS:MA3MSCNTF002_307S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntUpdateAlarmServiceResponse updateAlarmService(IServiceContext ctx,
            SupPntUpdateAlarmServiceRequest input) throws PRCServiceException {

        SupPntUpdateAlarmServiceResponse output = new SupPntUpdateAlarmServiceResponse();

        // DB조회 입력값
        UpdatePushJoinAlarmParameter dbParameter = new UpdatePushJoinAlarmParameter();

        try {
            /* AGRMNT_MK == "9" 인 경우 맞춤혜택/금융시장/웰쓰케어 정보는 스킵 */
            String AGRMNT_MK = StringUtils.defaultIfEmpty(input.getAgrmntMk(), "");
            if (!"9".equals(AGRMNT_MK)) {
                // 맞춤혜택가입여부
                dbParameter.setBenefitFlag(StringUtils.defaultIfEmpty(input.getBenefitFlag(), ""));
                // 금융시장정보가입여부
                dbParameter.setFinanceFlag(StringUtils.defaultIfEmpty(input.getFinanceFlag(), ""));
                // 금융시장정보값
                dbParameter.setFinanceVal("0");

                /**
                 * 푸시알림 설정 화면에서 오는 경우에만 WMLOUNGE_FLAG 값 UPDATE 처리
                 */
                String WMLOUNGE_FLAG = input.getWmloungeFlag() == null ? null
                        : StringUtils.defaultIfEmpty(input.getWmloungeFlag(), "");

                if ("Y".equals(WMLOUNGE_FLAG) || "N".equals(WMLOUNGE_FLAG)) {
                    dbParameter.setWmloungeFlag(WMLOUNGE_FLAG);
                }
            }

            // 입출금내역가입여부
            dbParameter.setIotranlistFlag(StringUtils.defaultIfEmpty(input.getIotranlistFlag(), ""));
            // 환율알림가입여부
            dbParameter.setNotyExrateFlg(StringUtils.defaultIfEmpty(input.getNotyExrateFlg(), ""));
            // 고객일련번호
            dbParameter.setSerno(StringUtils.defaultIfEmpty(input.getSerno(), ""));
            // 이용자ID
            dbParameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));

            this.tmbObjUsrMgtDao.updatePushJoinAlarm(dbParameter);

            // PUSH 마케팅 개인정보수집이용동의 원장/이력 갱신
            // 환율설정알림에서 들어온 경우 동의함 PASS : 푸시알림설정화면에서 설정완료한 경우에만 동의함 원장갱신
            if (!"9".equals(AGRMNT_MK)) {
                SupPntUpdateMarketingNoticeTermsAgreeRequest paramData = new SupPntUpdateMarketingNoticeTermsAgreeRequest();
                paramData
                        .setSsn(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), ""));
                paramData
                        .setUsrNo(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class), ""));
                paramData.setAgrmntMk(StringUtils.defaultIfEmpty(input.getAgrmntMk(), "2"));
                paramData.setChnlgb("M"); // M : Mobile
                paramData.setUpdClerkNo("");
                this.updateMarketingNoticeTermsAgree(ctx, paramData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException(e);
        }

        return output;
    }

    /**
     * PUSH_마케팅_개인정보수집이용동의 원장/이력 등록 및 변경
     *
     * @param SupPntUpdateMarketingNoticeTermsAgreeRequest
     * @return SupPntUpdateMarketingNoticeTermsAgreeResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_309S
     */
    @ComponentOperation(name = "PUSH_마케팅_개인정보수집이용동의 원장/이력 등록 및 변경 [ASIS:MA3MSCNTF002_309S]", author = "김기주")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntUpdateMarketingNoticeTermsAgreeResponse updateMarketingNoticeTermsAgree(IServiceContext ctx,
            SupPntUpdateMarketingNoticeTermsAgreeRequest input) throws PRCServiceException {

        SupPntUpdateMarketingNoticeTermsAgreeResponse output = new SupPntUpdateMarketingNoticeTermsAgreeResponse();

        String ssn = StringUtils.defaultIfEmpty(input.getSsn(), "");
        String usrNo = StringUtils.defaultIfEmpty(input.getUsrNo(), "");
        String agrmntMk = StringUtils.defaultIfEmpty(input.getAgrmntMk(), "");
        String chnlgb = StringUtils.defaultIfEmpty(input.getChnlgb(), "");
        String updClerkNo = StringUtils.defaultIfEmpty(input.getUpdClerkNo(), "");

        SelectPushCllctUsageAgrmntResult result = this.pushCllctUsageAgrmntDao.selectPushCllctUsageAgrmnt(ssn);

        // PUSH_마케팅_개인정보수집이용동의 원장 UPDATE
        if (result != null) {
            UpdatePushCllctUsageAgrmntParameter parameter = new UpdatePushCllctUsageAgrmntParameter();
            parameter.setSsn(ssn);
            parameter.setUsrNo(usrNo);
            parameter.setAgrmntMk(agrmntMk);
            parameter.setChnlgb(chnlgb);
            parameter.setUpdClerkNo(updClerkNo);

            this.pushCllctUsageAgrmntDao.updatePushCllctUsageAgrmnt(parameter);
            output.setRtnFlag("U");
        } else {
            // PUSH_마케팅_개인정보수집이용동의 원장 INSERT
            InsertPushCllctUsageAgrmntParameter parameter = new InsertPushCllctUsageAgrmntParameter();
            parameter.setSsn(ssn);
            parameter.setUsrNo(usrNo);
            parameter.setAgrmntMk(agrmntMk);
            parameter.setChnlgb(chnlgb);
            parameter.setUpdClerkNo(updClerkNo);

            this.pushCllctUsageAgrmntDao.insertPushCllctUsageAgrmnt(parameter);
            output.setRtnFlag("I");
        }

        // PUSH_마케팅_개인정보수집이용동의 이력 INSERT
        InsertPushCllctUsageAgrmntHistoryParameter parameter = new InsertPushCllctUsageAgrmntHistoryParameter();
        parameter.setSsn(ssn);
        parameter.setUsrNo(usrNo);
        parameter.setAgrmntMk(agrmntMk);
        parameter.setChnlgb(chnlgb);
        parameter.setUpdClerkNo(updClerkNo);

        this.pushCllctUsageAgrmntDao.insertPushCllctUsageAgrmntHistory(parameter);

        return output;
    }

    /**
     * PUSH 고객 환율 등록 정보 조회
     *
     * @param SupPntListPushCustomerRateRegistInfoRequest
     * @return SupPntListPushCustomerRateRegistInfoResponse
     * @throws PRCServiceException
     * @description MA3MSCNTF002_402S
     */
    @ComponentOperation(name = "PUSH 고객 환율 등록 정보 조회 [ASIS:MA3MSCNTF002_402S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SupPntListPushCustomerRateRegistInfoResponse listPushCustomerRateRegistInfo(IServiceContext ctx,
            SupPntListPushCustomerRateRegistInfoRequest input) throws PRCServiceException {

        SupPntListPushCustomerRateRegistInfoResponse output = new SupPntListPushCustomerRateRegistInfoResponse();

        String serno = StringUtils.defaultIfEmpty(input.getSerno(), "");

        List<SelectExratePushDataResult> resultList = this.tmbExrateMgtDao.selectExratePushData(serno);

        // 고객일련번호
        if (resultList.size() > 0) {
            output.setDbData(this.supportPushNotificationComponentMapper.toExrateDataList(resultList));
        } else {
            output.setDbData(null);
        }

        return output;
    }

}
