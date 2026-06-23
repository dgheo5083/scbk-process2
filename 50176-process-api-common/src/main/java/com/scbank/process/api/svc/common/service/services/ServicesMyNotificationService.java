package com.scbank.process.api.svc.common.service.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.TmbPushMsgCampDao;
import com.scbank.process.api.svc.common.dao.TmbPushMsgDao;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgCampParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgCampResult;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgResult;
import com.scbank.process.api.svc.common.dao.dto.ListPushNotificationParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushNotificationResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushMsgCampResult;
import com.scbank.process.api.svc.common.mapper.ServicesMyNotificationMapper;
import com.scbank.process.api.svc.common.service.services.dto.SvcMntGetMyNotificationRequest;
import com.scbank.process.api.svc.common.service.services.dto.SvcMntGetMyNotificationResponse;
import com.scbank.process.api.svc.common.service.services.dto.SvcMntListMyNotificationRequest;
import com.scbank.process.api.svc.common.service.services.dto.SvcMntListMyNotificationResponse;
import com.scbank.process.api.svc.shared.components.account.AccountListComponent;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "내소식 알림 정보 조회", url = "/services/myNotification", author = "김기주")
public class ServicesMyNotificationService {

    private final TmbPushMsgCampDao tmbPushMsgCampDao;

    private final TmbPushMsgDao tmbPushMsgDao;

    private final ISessionContextManager sessionManager;

    private final AccountListComponent accountListComponent;

    private final ServicesMyNotificationMapper servicesMyNotificationMapper;

    /**
     * 내소식 알림 정보 조회
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3MSCNTF001_401S
     */
    @ServiceEndpoint(url = "/listMyNotification", name = "내소식 알림 정보 조회 [ASIS:MA3MSCNTF001_401S]", author = "김기주")
    @Transactional(value = "smsdbTransactionManager", rollbackFor = { Throwable.class })
    public SvcMntListMyNotificationResponse listMyNotification(IServiceContext ctx,
            SvcMntListMyNotificationRequest input) throws PRCServiceException {

        SvcMntListMyNotificationResponse output = new SvcMntListMyNotificationResponse();

        // 푸시 알림 클릭하고 들어왔을때 내소식으로 바로 보내기
        // PMS 푸시 연동제어를 위한 로직 추가
        String type = StringUtils.defaultIfEmpty(input.getType(), "");
        String messageId = StringUtils.defaultIfEmpty(input.getMessageId(), "");
        if (!"".equals(type)) {
            if ((type.equals("87") || type.equals("88") || type.equals("89") && !"".equals(messageId))) {
                try {
                    messageId = messageId.substring(2, messageId.length());
                    SelectPushMsgCampResult outputDB = this.tmbPushMsgCampDao.selectPushMsgCamp(messageId);

                    String WEB_URL = outputDB.getWebUrl();

                    if (WEB_URL == null || "".equals(WEB_URL)) {
                    } else {
                        output.setWebUrl(WEB_URL);
                        return output;
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }

        String inquirySetKind = StringUtils.defaultIfEmpty(input.getInquirySetKind(), "");

        this.sessionManager.setLoginValue("pushTabN", "N");
        output.setCurrData(DateUtils.getCurrentDate("yyyyMMddHHmmss"));
        output.setBnkingId(sessionManager.getLoginValue("UserID", String.class));
        output.setBreezePushJoinYN(sessionManager.getLoginValue("BreezePushJoinYN", String.class));

        if (inquirySetKind.equals("1")) { // 입출금조회 빈 날짜 값 유효성
            if (StringUtils.isEmpty(input.getInqEndDate()) || StringUtils.isEmpty(input.getDepositInqStrDate())) {
                return output;
            }
        } else if (inquirySetKind.equals("2")) { // 환율조회 빈 날짜 값 유효성
            if (StringUtils.isEmpty(input.getInqEndDate()) || StringUtils.isEmpty(input.getServiceInqStrDate())) {
                return output;
            }
        } else if (inquirySetKind.equals("3") || inquirySetKind.equals("4")) { // 공지,혜택, 기타 등등 조회 빈 날짜 값 유효성
            if (StringUtils.isEmpty(input.getInqEndDate()) || StringUtils.isEmpty(input.getPmsServiceInqStrDate())) {
                return output;
            }
        } else { // 전체조회 빈 날짜 값 유효성
            if (StringUtils.isEmpty(input.getInqEndDate()) || StringUtils.isEmpty(input.getDepositInqStrDate())
                    || StringUtils.isEmpty(input.getServiceInqStrDate())
                    || StringUtils.isEmpty(input.getPmsServiceInqStrDate())) {
                return output;
            }
        }

        List<ListPushMsgResult> acctPushList = new ArrayList<>(); // 입출금 내역
        List<ListPushMsgCampResult> campPushList = new ArrayList<>(); // 환율, 금융시장
        List<ListPushNotificationResult> pmsPushList = new ArrayList<>(); // PMS 푸시내역

        if (inquirySetKind.equals("1") || inquirySetKind.equals("0")) {
            ListPushMsgParameter depositParameter = new ListPushMsgParameter();
            depositParameter.setInqEndDate(StringUtils.defaultIfEmpty(input.getInqEndDate(), ""));
            depositParameter.setInqStrDate(StringUtils.defaultIfEmpty(input.getDepositInqStrDate(), ""));
            depositParameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));

            acctPushList = this.tmbPushMsgDao.listPushMsg(depositParameter); // 입출금 내역 알림
        }

        if (inquirySetKind.equals("2") || inquirySetKind.equals("3") || inquirySetKind.equals("4")
                || inquirySetKind.equals("0")) {
            ListPushMsgCampParameter listPushMsgCampParameter = new ListPushMsgCampParameter();
            listPushMsgCampParameter.setInqEndDate(StringUtils.defaultIfEmpty(input.getInqEndDate(), ""));
            listPushMsgCampParameter.setInqStrDate(StringUtils.defaultIfEmpty(input.getServiceInqStrDate(), ""));
            listPushMsgCampParameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));
            listPushMsgCampParameter.setInquirySetKind(inquirySetKind);

            campPushList = this.tmbPushMsgCampDao.listPushMsgCamp(listPushMsgCampParameter); // 환율, 금융시장

            try {
                ListPushNotificationParameter pmsServiceParameter = new ListPushNotificationParameter();
                pmsServiceParameter.setInqEndDate(StringUtils.defaultIfEmpty(input.getInqEndDate(), ""));
                pmsServiceParameter.setInqStrDate(StringUtils.defaultIfEmpty(input.getPmsServiceInqStrDate(), ""));
                pmsServiceParameter.setBnkingId(sessionManager.getLoginValue("UserID", String.class));

                pmsPushList = this.tmbPushMsgCampDao.listPushNotification(pmsServiceParameter);
            } catch (Exception e) {
                pmsPushList = null; // PMS 푸시쿼리조회 이슈 발생해도 진행하기 위함
            }
        }

        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> acctPushList");
        List<SvcMntListMyNotificationResponse.PushData> resultList = new ArrayList<>();
        if (acctPushList != null && acctPushList.size() > 0) {
            for (ListPushMsgResult m : acctPushList) {
                SvcMntListMyNotificationResponse.PushData r = new SvcMntListMyNotificationResponse.PushData();
                r.setMsgSeq(m.getMsgSeq());
                r.setMsgType(StringUtils.defaultIfEmpty(m.getMsgType(), ""));
                r.setBnkingId(StringUtils.defaultIfEmpty(m.getBnkingId(), ""));
                r.setAcctNo(StringUtils.defaultIfEmpty(m.getAcctNo(), ""));
                r.setInoutKind(StringUtils.defaultIfEmpty(m.getInoutKind(), ""));
                r.setBankName(StringUtils.defaultIfEmpty(m.getBankName(), ""));
                r.setLonely(StringUtils.defaultIfEmpty(m.getLonely(), ""));
                r.setAmount(StringUtils.defaultIfEmpty(m.getAmount(), ""));
                r.setBalance(StringUtils.defaultIfEmpty(m.getBalance(), ""));
                r.setSendDate(StringUtils.defaultIfEmpty(m.getSendDate(), ""));
                r.setCampSeq("");
                r.setTitle("");
                r.setCnts("");
                r.setContentsMsg("");
                r.setWebUrl("");
                r.setImgUrl("");
                r.setVectorId("");
                resultList.add(r);
            }
        }

        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> campPushList");
        if (campPushList != null && campPushList.size() > 0) {
            for (ListPushMsgCampResult m : campPushList) {
                String campMsgType = StringUtils.defaultIfEmpty(m.getMsgType(), "");
                String campWebUrl = StringUtils.defaultIfEmpty(m.getWebUrl(), "");

                SvcMntListMyNotificationResponse.PushData r = new SvcMntListMyNotificationResponse.PushData();
                r.setMsgSeq(m.getMsgSeq());
                r.setMsgType(campMsgType);
                r.setBnkingId(StringUtils.defaultIfEmpty(m.getBnkingId(), ""));
                r.setAcctNo("");
                r.setInoutKind("");
                r.setBankName("");
                r.setLonely("");
                r.setAmount("");
                r.setBalance("");
                r.setSendDate(StringUtils.defaultIfEmpty(m.getSendDate(), ""));
                r.setCampSeq(m.getCampSeq());
                r.setTitle(StringUtils.defaultIfEmpty(m.getTitle(), ""));
                r.setCnts(StringUtils.defaultIfEmpty(m.getCnts(), ""));

                if (StringUtils.isNotEmpty(campMsgType) && campMsgType.matches("03|13")
                        && StringUtils.isNotEmpty(campWebUrl)) {
                    // MSG_TYPE(03|13) && WEB_URL 존재 = wmlounge페이지로 이동하면서 상세조회하므로 컨텐츠내용 불필요
                    r.setContentsMsg("");
                } else {
                    r.setContentsMsg(StringUtils.defaultIfEmpty(m.getContentsMsg(), ""));
                }

                r.setWebUrl(campWebUrl);
                r.setImgUrl(StringUtils.defaultIfEmpty(m.getImgUrl(), ""));
                r.setVectorId(StringUtils.defaultIfEmpty(m.getVectorId(), ""));
                resultList.add(r);
            }
        }

        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>> pmsPushList");
        if (pmsPushList != null && pmsPushList.size() > 0) {
            for (ListPushNotificationResult m : pmsPushList) {
                SvcMntListMyNotificationResponse.PushData r = new SvcMntListMyNotificationResponse.PushData();
                r.setMsgSeq(m.getMsgSeq());
                r.setMsgType(StringUtils.defaultIfEmpty(m.getMsgType(), ""));
                r.setBnkingId(StringUtils.defaultIfEmpty(m.getBnkingId(), ""));
                r.setAcctNo("");
                r.setInoutKind("");
                r.setBankName("");
                r.setLonely("");
                r.setAmount("");
                r.setBalance("");
                r.setSendDate(StringUtils.defaultIfEmpty(m.getSendDate(), ""));
                r.setCampSeq(m.getCampSeq());
                r.setTitle(StringUtils.defaultIfEmpty(m.getTitle(), ""));
                r.setCnts(StringUtils.defaultIfEmpty(m.getCnts(), ""));
                r.setContentsMsg(StringUtils.defaultIfEmpty(m.getContentsMsg(), ""));
                r.setWebUrl(StringUtils.defaultIfEmpty(m.getWebUrl(), ""));
                r.setImgUrl(StringUtils.defaultIfEmpty(m.getImgUrl(), ""));
                r.setVectorId(StringUtils.defaultIfEmpty(m.getVectorId(), ""));
                resultList.add(r);
            }
        }

        output.setResultList(resultList);
        output.setResultSize(resultList.size());

        List<AllAccountInfo> allAccountInfoList = this.accountListComponent.getAllAccountList();

        if (allAccountInfoList != null && allAccountInfoList.size() > 0) {
            List<SvcMntListMyNotificationResponse.AccountInfo> accountInfoList = new ArrayList<>();

            for (AllAccountInfo allAccountInfo : allAccountInfoList) {

                String acctType = allAccountInfo.getAcctType();

                if (acctType.equals("1")) {
                    accountInfoList.add(this.servicesMyNotificationMapper.toAccountInfo(allAccountInfo));
                }
            }

            output.setAccountList(accountInfoList);
        }

        log.debug("################################# listMyNotification end ###############");

        return output;
    }

    /**
     * 내소식 알림 메시지 확인
     * 
     * @param ctx
     * @param input
     * @return
     * @description MA3MSCNTF001_701S
     */
    @ServiceEndpoint(url = "/getMyNotification", name = "내소식 알림 메시지 확인 [ASIS:MA3MSCNTF001_701S]", author = "김기주")
    public SvcMntGetMyNotificationResponse getMyNotification(IServiceContext ctx,
            SvcMntGetMyNotificationRequest input) {

        SvcMntGetMyNotificationResponse output = new SvcMntGetMyNotificationResponse();
        output.setContentsMsg(getReplaceText(StringUtils.defaultIfEmpty(input.getContentsMsg(), "")));
        output.setSendDate(StringUtils.defaultIfEmpty(input.getSendDate(), ""));
        output.setTitle(StringUtils.defaultIfEmpty(input.getTitle(), ""));
        output.setMsgSeq(StringUtils.defaultIfEmpty(input.getMsgSeq(), ""));
        output.setMsgType(StringUtils.defaultIfEmpty(input.getMsgType(), ""));

        return output;

    }

    private String getReplaceText(String text) {

        String result = text;
        result = result.replaceAll("&amp;", "&");
        result = result.replaceAll("&&", "\r\n");
        result = result.replaceAll("&lt;", "<");
        result = result.replaceAll("&gt;", ">");
        result = result.replaceAll("&qout;", "\"");
        result = result.replaceAll("&quot;", "\"");
        result = result.replaceAll("&#039;", "\'");
        result = result.replaceAll("java:", "javascript:");

        return result;
    }

}
