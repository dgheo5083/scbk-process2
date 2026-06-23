package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 서비스 알림 동의 등록/수정
 */
@Data
@IntegrationMessage(id = "SupPntUpdateServicePushAlarmRequest", type = Type.REQUEST)
public class SupPntUpdateServicePushAlarmRequest implements IMessageObject {

    @MessageField(id = "servicePushAlarmListCount", name = "")
    private int servicePushAlarmListCount;

    @MessageField(id = "servicePushAlarmList", name = "")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SupPntUpdateServicePushAlarmRequest/servicePushAlarmListCount")
    private List<ServicePushAlarm> servicePushAlarmList;

    @Data
    public static class ServicePushAlarm implements IMessageObject {

        @MessageField(id = "msgTypeSubCd", name = "")
        private String msgTypeSubCd;

        @MessageField(id = "useYn", name = "")
        private String useYn;

    }

}
