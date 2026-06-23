package com.scbank.process.api.svc.shared.components.account.dto;

import java.math.BigDecimal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "RepresentAccountInfo", type = Type.RESPONSE, description = "대표 계좌목록조회 응답")
public class RepresentAccountInfo implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "계좌번호")
    private String drawAcctNum;

    @MessageField(id = "drawAcctName", name = "계좌명")
    private String drawAcctName;

    @MessageField(id = "balance", name = "잔액")
    private BigDecimal balance;

    @MessageField(id = "number", name = "정렬순서")
    private Integer number;

    @MessageField(id = "balSign", name = "잔액부호")
    private String balSign;

    @MessageField(id = "umbrellaGroup", name = "엄블렐러펀드그룹")
    private String umbrellaGroup;

    @MessageField(id = "umbrellaTransferYN", name = "전환가능BIT")
    private String umbrellaTransferYN;

}
