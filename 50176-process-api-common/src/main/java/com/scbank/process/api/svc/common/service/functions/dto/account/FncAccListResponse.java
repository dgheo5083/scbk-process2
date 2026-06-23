package com.scbank.process.api.svc.common.service.functions.dto.account;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;
import com.scbank.process.api.svc.shared.components.account.dto.OpenbankingAccountInfo;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncAccListResponse", description = "계좌목록조회", type = Type.RESPONSE)
public class FncAccListResponse implements IMessageObject {

    @MessageField(id = "depositAccountList", name = "예적금 계좌목록(당행)")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<AllAccountInfo> depositAccountList;

    @MessageField(id = "loanAccountList", name = "대출 계좌목록(당행)")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<AllAccountInfo> loanAccountList;

    @MessageField(id = "obsAccountList", name = "오픈뱅킹 계좌목록(타행)")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<OpenbankingAccountInfo> obsAccountList;
}
