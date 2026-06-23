package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 요청 정보 클래스
 * 마케팅 동의/변경 페이지
 */
@Data
@IntegrationMessage(id = "SupCscUpdateMarketingTermsRequest", type = Type.REQUEST)
public class SupCscUpdateMarketingTermsRequest implements IMessageObject {

    @MessageField(id = "callPageType", name = "callPageType")
    private String callPageType;

    @MessageField(id = "eventNo", name = "이벤트번호")
    private String eventNo;

    @MessageField(id = "isRas", name = "화상상담구분값")
    private String isRas;

    @MessageField(id = "tranGb", name = "tranGb")
    private String tranGb;

    @MessageField(id = "evtData", name = "evtData")
    private String evtData;

    @MessageField(id = "evtId", name = "evtId")
    private String evtId;

    @MessageField(id = "yiPHONE01", name = "자택전화")
    private String yiPHONE01;

    @MessageField(id = "yiPHONE02", name = "직장전화")
    private String yiPHONE02;

    @MessageField(id = "yiPHONE04", name = "휴대전화")
    private String yiPHONE04;

    @MessageField(id = "yiPHONE05", name = "SMS")
    private String yiPHONE05;

    @MessageField(id = "yiMAIL01", name = "자택우편")
    private String yiMAIL01;

    @MessageField(id = "yiMAIL02", name = "직장우편")
    private String yiMAIL02;

    @MessageField(id = "yiMAIL04", name = "이메일")
    private String yiMAIL04;

    @MessageField(id = "yiMAIL03", name = "기타우편")
    private String yiMAIL03;

    @MessageField(id = "yiMUSE3", name = "개인정보수집이용동의")
    private String yiMUSE3;

    @MessageField(id = "yiMUSEM3", name = "광고성정보수신동의")
    private String yiMUSEM3;

}
