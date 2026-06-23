package com.scbank.process.api.svc.common.service.services.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 내소식 알림 메시지 확인
 */
@Data
@IntegrationMessage(id = "SvcMntGetMyNotificationResponse", type = Type.RESPONSE)
public class SvcMntGetMyNotificationResponse implements IMessageObject {

    @MessageField(id = "contentsMsg", name = "")
    private String contentsMsg;

    @MessageField(id = "sendDate", name = "")
    private String sendDate;

    @MessageField(id = "title", name = "")
    private String title;

    @MessageField(id = "msgSeq", name = "")
    private String msgSeq;

    @MessageField(id = "msgType", name = "")
    private String msgType;

}
