package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 환율 조회
 */
@Data
@IntegrationMessage(id = "SupPntListPushRateResponse", type = Type.RESPONSE)
public class SupPntListPushRateResponse implements IMessageObject {

    @MessageField(id = "rateData", name = "")
    @RepeatedField
    private List<ExchangeInfo> rateData;

    @MessageField(id = "dateTime", name = "")
    private String dateTime;

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

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "pushId", name = "")
    private String pushId;

    @MessageField(id = "dbData", name = "")
    @RepeatedField
    private List<ExrateData> dbData;

    @Data
    public static class ExchangeInfo implements IMessageObject {

        @MessageField(id = "stateName", name = "")
        private String stateName;

        @MessageField(id = "currency", name = "")
        private String currency;

        @MessageField(id = "dateTime", name = "")
        private String dateTime;

        @MessageField(id = "buyRate", name = "")
        private String buyRate;

        @MessageField(id = "ttBuyRate", name = "")
        private String ttBuyRate;

        @MessageField(id = "ttSaleRate", name = "")
        private String ttSaleRate;

        @MessageField(id = "cashBuyRate", name = "")
        private String cashBuyRate;

        @MessageField(id = "cashSaleRate", name = "")
        private String cashSaleRate;

        @MessageField(id = "usChangeRate", name = "")
        private String usChangeRate;

        @MessageField(id = "yyChangeRate", name = "")
        private String yyChangeRate;

        @MessageField(id = "tmpRate", name = "")
        private String tmpRate;
    }

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
