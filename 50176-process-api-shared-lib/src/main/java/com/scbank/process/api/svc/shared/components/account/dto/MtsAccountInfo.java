package com.scbank.process.api.svc.shared.components.account.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "MtsAccountInfo", type = Type.RESPONSE, description = "MTS 계좌목록조회 응답")
public class MtsAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "assort", name = "계좌종별")
    private String assort;

    @MessageField(id = "depKind", name = "계좌종류(1:입출금, 2:예금, 3:BC카드, H:현대카드)")
    private String depKind;

    @MessageField(id = "balSign", name = "잔액부호")
    private String balSign;

    @MessageField(id = "balance", name = "잔액")
    private BigDecimal balance;

    @MessageField(id = "curcy", name = "통화코드")
    private String curcy;

    @MessageField(id = "drawAcctNameAlias", name = "계좌별명/제휴카드명")
    private String drawAcctNameAlias;

    @MessageField(id = "savingStartDate", name = "예금신규일")
    private String savingStartDate;

    @MessageField(id = "savingEndDate", name = "예금만기일")
    private String savingEndDate;

    @MessageField(id = "drawAcctName", name = "MTS 자산명")
    private String drawAcctName;

    @MessageField(id = "mtsAssetName", name = "MTS 자산명")
    private String mtsAssetName;

    @MessageField(id = "mtsAssetNo", name = "MTS 자산계좌번호")
    private String mtsAssetNo;

    @MessageField(id = "mtsNo", name = "MTS 자산번호")
    private String mtsNo;
}
