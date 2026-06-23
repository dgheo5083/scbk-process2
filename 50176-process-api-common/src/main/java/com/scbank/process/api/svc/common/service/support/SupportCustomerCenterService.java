package com.scbank.process.api.svc.common.service.support;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.scbank.process.api.edmi.dto.mci.MciHpCuCusCosReq;
import com.scbank.process.api.edmi.dto.mci.MciHpCuCusCosRes;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D75400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01D75400Res;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H75400Req;
import com.scbank.process.api.edmi.dto.oltp.CbIbk01H75400Res;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.exception.MciSystemException;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpRequestOptions;
import com.scbank.process.api.fw.base.integration.system.oltp.OltpResponse;
import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.SupportCustomerCenterComponent;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordResult;
import com.scbank.process.api.svc.common.mapper.SupportCustomerCenterMapper;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscApplyPhoneConsultationRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscApplyPhoneConsultationResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetNoticeDetailRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetNoticeDetailResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetPhoneConsultationRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscGetPhoneConsultationResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscListNoticeRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscListNoticeResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscSetMarketingTermsPageRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscSetMarketingTermsPageResponse;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscUpdateMarketingTermsRequest;
import com.scbank.process.api.svc.common.service.support.dto.customercenter.SupCscUpdateMarketingTermsResponse;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.constants.CommonBizConstants;
import com.scbank.process.api.svc.shared.integration.HostClient;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공지사항", url = "/support/customerCenter", author = "송지섭")
public class SupportCustomerCenterService {

    // 세션
    private final ISessionContextManager sessionManager;

    /**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;

    private final SmsComponent smsComponent;

    private final SupportCustomerCenterComponent supportCustomerCenterComponent;

    private final SupportCustomerCenterMapper supportCustomerCenterMapper;

    /**
     * 공지사항 목록 조회
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTNTC001_101S
     */
    @ServiceEndpoint(url = "/listNotice", name = "공지사항 목록 조회 [ASIS:MA3CSTNTC001_101S]", author = "송지섭")
    public SupCscListNoticeResponse listNotice(IServiceContext ctx,
            SupCscListNoticeRequest request) {
        SupCscListNoticeResponse response = new SupCscListNoticeResponse();
        SupCscListNoticeResponse noticeListResponse = new SupCscListNoticeResponse();

        String languageCode = ctx.locale().getLanguage();
        String language = "";

        if ("ko".equals(languageCode)) {
            language = "LN1001";
        } else {
            language = "LN1002";
        }

        String pageCount = StringUtils.defaultIfEmpty(request.getPageCount(), "1");

        request.setLanguage(language);
        request.setPageSize("10");
        request.setPaging(pageCount);

        int pageNum = Integer.parseInt(pageCount);

        // 공지사항 레코드 갯수 조회 [MA3CSTNTC001_103S]
        response = supportCustomerCenterComponent.getNoticeRecordCount(request);
        int countRecord = response.getCountRecord();

        // 공지사항 리스트 조회 MA3CSTNTC001_102S
        noticeListResponse = supportCustomerCenterComponent.listNoticeRecord(request);
        List<ListNoticeRecordResult> resultData = noticeListResponse.getNoticeSelect();

        if (resultData != null) {
            if (resultData.size() > 0) {
                if ((pageNum >= (countRecord / 10)) && (resultData.size() == (countRecord % 10))) {
                    response.setMoreData("N");
                } else {
                    response.setMoreData("Y");
                }
                response.setNoticeSelect(resultData);
            } else {
                response.setNoticeSelect(null);
                response.setMoreData("N");
            }
        } else {
            response.setNoticeSelect(null);
            response.setMoreData("N");
        }
        response.setPageCount(pageNum + 1);

        return response;
    }

    /**
     * 공지사항 상세 조회
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTNTC001_201S
     */
    @ServiceEndpoint(url = "/getNoticeDetail", name = "공지사항 상세 조회 [ASIS:MA3CSTNTC001_201S]", author = "송지섭")
    public SupCscGetNoticeDetailResponse getNoticeDetail(IServiceContext ctx,
            SupCscGetNoticeDetailRequest request) {
        SupCscGetNoticeDetailResponse response = new SupCscGetNoticeDetailResponse();

        // 게시물번호
        String languageCheck = ctx.locale().getLanguage();
        String language = "";

        if ("ko".equals(languageCheck)) {
            language = "LN1001";
        } else {
            language = "LN1002";
        }

        request.setLanguage(language);

        response = supportCustomerCenterComponent.getNoticeDetailFromTable(request);

        return response;

    }

    /**
     * 마케팅 동의/변경 페이지
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTMAR008_101S
     */
    @ServiceEndpoint(url = "/setMarketingTermsPage", name = "마케팅 동의/변경 페이지 [ASIS:MA3CSTMAR008_101S]", author = "송지섭")
    public SupCscSetMarketingTermsPageResponse setMarketingTermsPage(IServiceContext ctx,
            SupCscSetMarketingTermsPageRequest input) {

        SupCscSetMarketingTermsPageResponse response = new SupCscSetMarketingTermsPageResponse();

        // 23.04.24 나만의혜택 / 이벤트 마케팅 동의 이동시 Return Page처리
        String CALLPAGETYPE = StringUtils.defaultIfEmpty(input.getCallPageType(), "");
        String EVENT_NO = StringUtils.defaultIfEmpty(input.getEventNo(), "");
        // 화상상담 flag 추가
        String isRAS = StringUtils.defaultIfEmpty(input.getIsRas(), "");

        // 공통부 세팅
        OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_H754");
        hostRequestOptions.setImsTranCd("TI1IBK01");
        hostRequestOptions.setInClassCd("H754");
        hostRequestOptions.setSvcCd("754");
        hostRequestOptions.setCaptureSystem("OLTP");
        hostRequestOptions.setPreTran(true);

        // 개별부 세팅
        CbIbk01H75400Req sendData = new CbIbk01H75400Req();
        String userID = "";
        String tranGB = StringUtils.defaultIfEmpty(input.getTranGb(), "");
        if (sessionManager.isLogin()) {
            userID = sessionManager.getLoginValue("UserID", String.class);
        } else {
            userID = sessionManager.getGlobalValue("UserID2", String.class);

            // 비로그인성거래로 확장됨에 따라 실제 이용자번호가 없는경우 FIRST999로 정의
            if (StringUtils.isEmpty(userID)) {
                userID = CommonBizConstants.DEFAULT_USER_ID;
                sendData.setYIJUMIN(SessionUtils.getSessionValue("PerBusNo")); // 비로그인성거래로 확장되어 주민번호 추가
            }
        }

        sendData.setYIUSID(userID);
        sendData.setYIPASS(sessionManager.isLogin()
                ? sessionManager.getLoginValue("TSPassword", String.class)
                : "99999999");
        sendData.setYIGRGBN("1");

        // 전문 전송
        OltpResponse<CbIbk01H75400Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, sendData,
                CbIbk01H75400Res.class);
        response = supportCustomerCenterMapper.toSupCscSetMarketingTermsPageResponse(hostResponse.getResponse());

        if ("evt".equals(tranGB)) {
            response.setEvtData(input.getEvtData());
            response.setTranGb(tranGB);
        } else if ("mgm".equals(tranGB)) {
            response.setEvtId(input.getEvtId());
            response.setTranGb(tranGB);
        }

        response.setCallPageType(CALLPAGETYPE);
        response.setEventNo(EVENT_NO);
        response.setIsRas(isRAS);

        return response;

    }

    /**
     * 마케팅 동의/변경
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTMAR008_102S
     */
    @ServiceEndpoint(url = "/updateMarketingTerms", name = "마케팅 동의/변경 [ASIS:MA3CSTMAR008_102S]", author = "송지섭")
    public SupCscUpdateMarketingTermsResponse updateMarketingTerms(IServiceContext ctx,
            SupCscUpdateMarketingTermsRequest input) {

        SupCscUpdateMarketingTermsResponse response = new SupCscUpdateMarketingTermsResponse();

        // 23.04.24 나만의혜택 / 이벤트 마케팅 동의 이동시 Return Page처리
        String CALLPAGETYPE = StringUtils.defaultIfEmpty(input.getCallPageType(), "");
        String EVENT_NO = StringUtils.defaultIfEmpty(input.getEventNo(), "");
        // 화상상담 flag 추가
        String isRAS = StringUtils.defaultIfEmpty(input.getIsRas(), "");

        // 공통부 세팅
        OltpRequestOptions hostRequestOptions = this.hostClient.getOltpRequestOptions("CB_IBK01_D754");
        hostRequestOptions.setImsTranCd("TI1IBK01");
        hostRequestOptions.setInClassCd("D754");
        hostRequestOptions.setSvcCd("754");
        hostRequestOptions.setCaptureSystem("OLTP");
        hostRequestOptions.setPreTran(false);

        // 개별부 세팅
        CbIbk01D75400Req sendData = new CbIbk01D75400Req();
        String userID = "";
        String tranGB = StringUtils.defaultIfEmpty(input.getTranGb(), "");
        if (sessionManager.isLogin()) {
            userID = sessionManager.getLoginValue("UserID", String.class);
        } else {
            userID = sessionManager.getGlobalValue("UserID2", String.class);

            // 비로그인성거래로 확장됨에 따라 실제 이용자번호가 없는경우 FIRST999로 정의
            if (StringUtils.isEmpty(userID)) {
                userID = CommonBizConstants.DEFAULT_USER_ID;
                sendData.setYIJUMIN(SessionUtils.getSessionValue("PerBusNo")); // 비로그인성거래로 확장되어 주민번호 추가
            }
        }

        sendData.setYIUSID(userID);
        sendData.setYIPASS(sessionManager.isLogin()
                ? sessionManager.getLoginValue("TSPassword", String.class)
                : "99999999");
        sendData.setYIGRGBN("2");
        sendData.setYIPHONE01(input.getYiPHONE01()); // 자택전화
        sendData.setYIPHONE02(input.getYiPHONE02()); // 직장전화
        sendData.setYIPHONE04(input.getYiPHONE04()); // 휴대전화
        sendData.setYIPHONE05(input.getYiPHONE05()); // SMS
        sendData.setYIMAIL01(input.getYiMAIL01()); // 자택우편
        sendData.setYIMAIL02(input.getYiMAIL02()); // 직장우편
        sendData.setYIMAIL04(input.getYiMAIL04()); // 이메일
        sendData.setYIMAIL03(input.getYiMAIL03()); // 기타우편
        sendData.setYIMUSE3(input.getYiMUSE3()); // 개인정보수집이용동의 Y/N/SPACE
        sendData.setYIMUSEM3(input.getYiMUSEM3()); // 광고성정보수신동의 Y/N/SPACE

        // 전문 전송
        OltpResponse<CbIbk01D75400Res> hostResponse = this.hostClient.sendOltp(hostRequestOptions, sendData,
                CbIbk01D75400Res.class);
        response = supportCustomerCenterMapper.toSupCscUpdateMarketingTermsResponse(hostResponse.getResponse());

        if ("evt".equals(tranGB)) {
            response.setEvtData(input.getEvtData());
            response.setTranGb(tranGB);
        } else if ("mgm".equals(tranGB)) {
            response.setEvtId(input.getEvtId());
            response.setTranGb(tranGB);
        }

        response.setCallPageType(CALLPAGETYPE);
        response.setEventNo(EVENT_NO);
        response.setIsRas(isRAS);

        return response;

    }

    /**
     * 전화상담예약 파라미터 전달
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTCLT003_101S
     */
    @ServiceEndpoint(url = "/getPhoneConsultation", name = "전화상담예약 파라미터 전달 [ASIS:MA3CSTCLT003_101S]", author = "송지섭")
    public SupCscGetPhoneConsultationResponse getPhoneConsultation(IServiceContext ctx,
            SupCscGetPhoneConsultationRequest input) {
        SupCscGetPhoneConsultationResponse response = new SupCscGetPhoneConsultationResponse();

        /*
         * callType 정의
         * ploan : 신용대출
         * hloan : 담보대출
         * longcard : 장기카드대출
         * deposit : 예금
         * fx : 외환
         * asset : 자산관리상품
         * reserve : 예약상담
         */
        String callType = StringUtils.defaultIfEmpty(input.getCallType(), "");
        String PRDCT_NM = StringUtils.defaultIfEmpty(input.getPrdctNm(), ""); // 신청상품명
        String isP2P = StringUtils.defaultIfEmpty(input.getIsp2p(), "N"); // 피플펀드 여부 (전화상담예약 시 구분값 P2P세팅)
        String cstYN = StringUtils.defaultIfEmpty(input.getCstYn(), "N"); // 컨택센터 진입여부
        String arg = StringUtils.defaultIfEmpty(input.getArg(), "");

        /*
         * Sprint37# 자산관리상담서비스 이벤트 - 2025.02.03
         * 상담 신청 후 다시 이벤트 롤백 개발 적용
         */
        String CALLPAGETYPE = StringUtils.defaultIfEmpty(input.getCallPageType(), "");
        String EVENT_NO = StringUtils.defaultIfEmpty(input.getEventNo(), "");

        response.setCallType(callType);
        response.setPrdctNm(PRDCT_NM);
        response.setIsp2p(isP2P);
        response.setCstYn(cstYN);
        response.setArg(arg);
        response.setCallPageType(CALLPAGETYPE);
        response.setEventNo(EVENT_NO);
        String phoneNo1 = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("HPOne", String.class), "");
        String phoneNo2 = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("HPTwo", String.class), "");
        String phoneNo3 = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("HPThree", String.class), "");
        response.setPhone1(phoneNo1);
        response.setPhone2(phoneNo2);
        response.setPhone3(phoneNo3);

        return response;

    }

    /**
     * 찾아가는뱅킹 신청
     * 
     * @param ctx
     * @param input
     * @return
     * @throws PRCServiceException
     * @description MA3CSTCLT001_103S
     */
    @ServiceEndpoint(url = "/applyPhoneConsultation", name = "찾아가는뱅킹 신청 [ASIS:MA3CSTCLT001_103S]", author = "송지섭")
    public SupCscApplyPhoneConsultationResponse applyPhoneConsultation(IServiceContext ctx,
            SupCscApplyPhoneConsultationRequest input) {
        SupCscApplyPhoneConsultationResponse response = new SupCscApplyPhoneConsultationResponse();
        String SSN = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("PerBusNo", String.class), "");
        String callType = StringUtils.defaultIfEmpty(input.getCallType(), "");

        /*
         * MCI 전문 호출
         */
        String interFaceId = "MCI_HP_CU_CUS_COS";

        MciRequestOptions mciCfg = this.hostClient.getMciRequestOptions(interFaceId);
        mciCfg.setTranCd("CB677311CI"); // AS-IS BUSINESS_FUNCTION_ID
        mciCfg.setTxnBrNo("0019"); // AS-IS HOMEBRANCH
        mciCfg.setBlngBrNo("0019"); // AS-IS AGENTBRANCH

        // sendData.putMciComm("VAN_TYPE", "56");
        // sendData.putMciComm("WORK_TYPE", "IB");
        // sendData.putMciComm("SUBTASKCODE", "0001");

        // 개별부 세팅
        MciHpCuCusCosReq sendData = new MciHpCuCusCosReq();
        sendData.setRES_DT(input.getResDt());
        sendData.setRES_TM(input.getResTm());

        // 대출 거절사유 적재 시 휴대폰번호는 input 으로 받지않아 세션값을 사용하자
        if ("RECT".equals(callType)) {
            sendData.setPHONE_NO(StringUtils.defaultIfEmpty(sessionManager.getLoginValue("HpNum", String.class), "")
                    .replaceAll("\\D", ""));
        } else {
            sendData.setPHONE_NO(input.getPhoneNo());
        }
        sendData.setSSN(SSN);
        sendData.setCHNL_DIV(StringUtils.defaultIfEmpty(input.getChnlDiv(), ""));
        sendData.setGRADE(StringUtils.defaultIfEmpty(input.getGrade(), ""));
        MciHpCuCusCosRes mciHpCuCusCosRes = new MciHpCuCusCosRes();
        if ("RECT".equals(callType)) {

            // 대출 선택동의 적재케이스에서는 에러 적재 후 return 해주도록 하자
            try {
                String skill = StringUtils.defaultIfEmpty(input.getSkill(), "");
                String CustNm = sessionManager.getLoginValue("CustName", String.class);

                if ("".equals(CustNm)) {
                    skill = skill.replace("$$$", "");
                } else {
                    skill = skill.replace("$$$", CustNm + ", ");
                }

                sendData.setSKILL(skill);

                mciHpCuCusCosRes = this.hostClient.sendMci(mciCfg, sendData, MciHpCuCusCosRes.class).getResponse();

            } catch (MciSystemException e) {
                if (!"".equals(SSN) && SSN.length() > 6) {
                    log.error("########################_MA3CSTCLT001_103S 대출선택동의적재 오류 ::" + SSN.substring(0, 6) + "::"
                            + e.getErrorCode() + "::" + e.getErrorModule() + "::" + e.getErrorMessage() + "::");
                }

                response.setErMSG(e.getErrorMessage());
                response.setErCD(e.getErrorCode());
                response.setErMD(e.getErrorModule());
            }

        } else {
            sendData.setSKILL(input.getSkill());

            mciHpCuCusCosRes = this.hostClient.sendMci(mciCfg, sendData, MciHpCuCusCosRes.class).getResponse();
        }

        // mci결과값 저장
        response = supportCustomerCenterMapper.toSupCscApplyPhoneConsultationResponse(mciHpCuCusCosRes);

        response.setDate(input.getResDt());

        // 신청 거절 TM은 call UI 거래만 호출하도록 하자
        if (!"RECT".equals(callType)) {
            String smsCode = CodeUtils.getCodeValue("SMS_MSG", "FBINS");

            smsCode = StringUtils.replace(smsCode, "~~host~~", StringUtils.defaultIfEmpty(input.getHost(), ""));
            // 상품번호
            smsCode = StringUtils.replace(smsCode, "~~prdId~~", StringUtils.defaultIfEmpty(input.getPrdId(), ""));
            // 보내는 사람 이름(상담원 이름)
            smsCode = StringUtils.replace(smsCode, "~~name~~", input.getUserName());
            // 제목
            smsCode = StringUtils.replace(smsCode, "~~title~~", StringUtils.defaultIfEmpty(input.getTitle(), ""));

            log.debug("################## smsCode :" + smsCode);

            String[] smsCodeSplit = smsCode.split("#");
            String[] reqphone = smsCodeSplit[0].split("-");

            /*
             * SMS 보내기
             */
            String member = StringUtils.defaultIfEmpty(input.getMember(), "0");
            String usercode = StringUtils.defaultIfEmpty(input.getUserCode(), "");
            String username = StringUtils.defaultIfEmpty(input.getUserName(), "");
            String callphone1 = StringUtils.defaultIfEmpty(input.getCallPhone1(), "");
            String callphone2 = StringUtils.defaultIfEmpty(input.getCallPhone2(), "");
            String callphone3 = StringUtils.defaultIfEmpty(input.getCallPhone3(), "");
            String rdate = StringUtils.defaultIfEmpty(DateUtils.getCurrentDate(), "");
            String rtime = StringUtils.defaultIfEmpty(String.valueOf(DateUtils.getCurrentTime("HHmmss")), "");
            String reqphone1 = StringUtils.defaultIfEmpty(input.getReqPhone1(), "");
            String reqphone2 = StringUtils.defaultIfEmpty(reqphone[0], "");
            String reqphone3 = StringUtils.defaultIfEmpty(reqphone[1], "");
            String callname = "[sc제일은행]";
            String deptcode = StringUtils.defaultIfEmpty(smsCodeSplit[5], "");
            String deptname = StringUtils.defaultIfEmpty(input.getDeptName(), "");
            String callmessage = StringUtils.defaultIfEmpty(smsCodeSplit[6], "");

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

            log.debug("member : [" + member + "]" + "messageCode : [" + smsCodeSplit[3] + "] :menuId : [" + "FBINS"
                    + "] : title[" + smsCodeSplit[4] + "] : message = [" + smsCodeSplit[6] + "] 고객휴대폰번호1[" + callphone1
                    + "] 고객휴대폰번호2[" + callphone2 + "] 고객휴대폰번호3[" + callphone3 + "] 응답번호1[" + reqphone[0] + "] 응답번호2["
                    + reqphone[1] + "]");

            String result = smsComponent.sendMain(smsParam);

            log.debug("SMS 발송결과 = [" + result + "]");
        }

        return response;
    }

}
