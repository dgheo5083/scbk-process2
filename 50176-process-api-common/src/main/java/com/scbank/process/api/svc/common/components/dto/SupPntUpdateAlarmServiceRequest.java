package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 알림 서비스 등록 및 변경
 */
@Data
@IntegrationMessage(id = "SupPntUpdateAlarmServiceRequest", type = Type.REQUEST)
public class SupPntUpdateAlarmServiceRequest implements IMessageObject {

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

    @MessageField(id = "benefitFlag", name = "맞춤혜택가입여부")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "금융시장정보가입여부")
    private String financeFlag;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @MessageField(id = "iotranlistFlag", name = "입출금내역가입여부")
    private String iotranlistFlag;

    @MessageField(id = "notyExrateFlg", name = "환율알림가입여부")
    private String notyExrateFlg;

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

}
