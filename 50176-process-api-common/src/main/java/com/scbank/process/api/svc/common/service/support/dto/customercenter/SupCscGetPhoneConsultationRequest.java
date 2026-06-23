package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 요청 정보 클래스
 * 전화상담예약 파라미터 전달
 */
@Data
@IntegrationMessage(id = "SupCscGetPhoneConsultationRequest", type = Type.REQUEST)
public class SupCscGetPhoneConsultationRequest implements IMessageObject {

    @MessageField(id = "callType", name = "callType")
    private String callType;

    @MessageField(id = "prdctNm", name = "prdctNm")
    private String prdctNm;

    @MessageField(id = "isp2p", name = "isp2p")
    private String isp2p;

    @MessageField(id = "cstYn", name = "cstYn")
    private String cstYn;

    @MessageField(id = "arg", name = "arg")
    private String arg;

    @MessageField(id = "callPageType", name = "callPageType")
    private String callPageType;

    @MessageField(id = "eventNo", name = "eventNo")
    private String eventNo;

}
