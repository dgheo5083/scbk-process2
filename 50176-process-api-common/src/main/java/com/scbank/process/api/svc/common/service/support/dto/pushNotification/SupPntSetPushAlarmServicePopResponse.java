package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 푸시 알림 서비스 설정 팝업
 */
@Data
@IntegrationMessage(id = "SupPntSetPushAlarmServicePopResponse", type = Type.RESPONSE)
public class SupPntSetPushAlarmServicePopResponse implements IMessageObject {

    @MessageField(id = "breezePushJoinYN", name = "가입여부")
    private String breezePushJoinYN;

    @MessageField(id = "phoneNo", name = "휴대폰번호")
    private String phoneNo;

    @MessageField(id = "hpOne", name = "핸드폰번호1")
    private String hpOne;

    @MessageField(id = "hpTwo", name = "핸드폰번호2")
    private String hpTwo;

    @MessageField(id = "hpThree", name = "핸드폰번호3")
    private String hpThree;

}
