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
@IntegrationMessage(id = "SupCscSetMarketingTermsPageRequest", type = Type.REQUEST)
public class SupCscSetMarketingTermsPageRequest implements IMessageObject {

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
}
