package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 입출금 내역 푸시 설정 화면 호출 및 설정 계좌 조회 요청
 */
@Data
@IntegrationMessage(id = "SetTransferListPushSettingPageRequest", type = Type.REQUEST)
public class SetTransferListPushSettingPageRequest implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "iotranlistFlag", name = "")
    private String iotranlistFlag;

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

}
