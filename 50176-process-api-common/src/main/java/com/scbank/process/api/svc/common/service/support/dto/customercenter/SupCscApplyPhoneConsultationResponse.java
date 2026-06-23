package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 응답 정보 클래스
 * 찾아가는뱅킹 신청
 */
@Data
@IntegrationMessage(id = "SupCscApplyPhoneConsultationResponse", type = Type.RESPONSE)
public class SupCscApplyPhoneConsultationResponse implements IMessageObject {

    @MessageField(id = "erMSG", name = "")
    private String erMSG;

    @MessageField(id = "erCD", name = "")
    private String erCD;

    @MessageField(id = "erMD", name = "")
    private String erMD;

    @MessageField(id = "date", name = "")
    private String date;

    @MessageField(id = "toRESULT", name = "toRESULT")
    private String toRESULT;
}
