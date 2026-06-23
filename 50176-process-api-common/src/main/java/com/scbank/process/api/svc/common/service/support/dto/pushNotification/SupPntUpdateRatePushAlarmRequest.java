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
 * 환율 푸쉬알림 등록/수정/삭제
 */
@Data
@IntegrationMessage(id = "SupPntUpdateRatePushAlarmRequest", type = Type.REQUEST)
public class SupPntUpdateRatePushAlarmRequest implements IMessageObject {

    @MessageField(id = "allListCount", name = "")
    private String allListCount;

    @MessageField(id = "pushId", name = "")
    private String pushId;

    @MessageField(id = "pushDataListCount", name = "")
    private int pushDataListCount;

    @MessageField(id = "pushDataList", name = "")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SupPntUpdateRatePushAlarmRequest/pushDataListCount")
    private List<PushData> pushDataList;

    @MessageField(id = "serno", name = "")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @MessageField(id = "iotranlistFlag", name = "")
    private String iotranlistFlag;

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

    @Data
    public static class PushData implements IMessageObject {

        @MessageField(id = "docType", name = "")
        private String docType;

        @MessageField(id = "type", name = "")
        private String type;

        @MessageField(id = "fitRate", name = "")
        private String fitRate;

        @MessageField(id = "pushDay", name = "")
        private String pushDay;

        @MessageField(id = "pushTime", name = "")
        private String pushTime;

        @MessageField(id = "seqNo", name = "")
        private String seqNo;

        @MessageField(id = "gb", name = "")
        private String gb;

        @MessageField(id = "currnm", name = "")
        private String currnm;

    }

}
