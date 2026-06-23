package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 응답 정보 클래스
 * 전화상담예약 파라미터 전달
 */
@Data
@IntegrationMessage(id = "SupCscGetPhoneConsultationResponse", type = Type.RESPONSE)
public class SupCscGetPhoneConsultationResponse implements IMessageObject {

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

    @MessageField(id = "phone1", name = "phone1")
    private String phone1;

    @MessageField(id = "phone2", name = "phone2")
    private String phone2;

    @MessageField(id = "phone3", name = "phone3")
    private String phone3;

}
