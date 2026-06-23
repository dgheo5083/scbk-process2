package com.scbank.process.api.svc.common.service.services.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 내소식 알림 정보 조회
 */
@Data
@IntegrationMessage(id = "SvcMntListMyNotificationRequest", type = Type.REQUEST)
public class SvcMntListMyNotificationRequest implements IMessageObject {

    @MessageField(id = "type", name = "")
    private String type;

    @MessageField(id = "messageId", name = "")
    private String messageId;

    @MessageField(id = "inquirySetKind", name = "")
    private String inquirySetKind;

    @MessageField(id = "inqEndDate", name = "")
    private String inqEndDate;

    @MessageField(id = "depositInqStrDate", name = "")
    private String depositInqStrDate;

    @MessageField(id = "serviceInqStrDate", name = "")
    private String serviceInqStrDate;

    @MessageField(id = "pmsServiceInqStrDate", name = "")
    private String pmsServiceInqStrDate;

}
