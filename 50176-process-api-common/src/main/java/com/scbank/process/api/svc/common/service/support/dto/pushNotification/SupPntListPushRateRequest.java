package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 환율 조회
 */
@Data
@IntegrationMessage(id = "SupPntListPushRateRequest", type = Type.REQUEST)
public class SupPntListPushRateRequest implements IMessageObject {

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

    @MessageField(id = "serno", name = "")
    private String serno;

}
