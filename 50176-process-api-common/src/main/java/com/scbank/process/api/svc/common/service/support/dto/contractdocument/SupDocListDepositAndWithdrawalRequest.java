package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 수신(입출금, 예금, 적금) 신청내역조회
 */
@Data
@IntegrationMessage(id = "SupDocListDepositAndWithdrawalRequest", type = Type.REQUEST)
public class SupDocListDepositAndWithdrawalRequest implements IMessageObject {

    @MessageField(id = "drawAcctNum", name = "drawAcctNum")
    private String drawAcctNum;

}