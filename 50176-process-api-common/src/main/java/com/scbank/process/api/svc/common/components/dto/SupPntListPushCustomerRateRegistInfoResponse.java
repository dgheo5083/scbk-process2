package com.scbank.process.api.svc.common.components.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * PUSH 고객 환율 등록 정보 조회
 */
@Data
@IntegrationMessage(id = "SupPntListPushCustomerRateRegistInfoResponse", type = Type.RESPONSE)
public class SupPntListPushCustomerRateRegistInfoResponse implements IMessageObject {

    @MessageField(id = "dbData", name = "")
    @RepeatedField
    private List<ExrateData> dbData;

    @Data
    public static class ExrateData implements IMessageObject {

        @MessageField(id = "seqNo", name = "")
        private String seqNo;

        @MessageField(id = "puSerno", name = "")
        private String puSerno;

        @MessageField(id = "gb", name = "")
        private String gb;

        @MessageField(id = "currnm", name = "")
        private String currnm;

        @MessageField(id = "type", name = "")
        private String type;

        @MessageField(id = "fitRate", name = "")
        private String fitRate;

        @MessageField(id = "pushDay", name = "")
        private String pushDay;

        @MessageField(id = "pushTime", name = "")
        private String pushTime;

    }

}
