package com.scbank.process.api.svc.shared.components.account.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 오픈뱅킹 등록 계좌조회 API RESPONSE
 */
@Data
@IntegrationMessage(id = "ListRegisteredOpenBankAccountFromAPIResponse", type = Type.RESPONSE)
public class ListRegisteredOpenBankAccountFromAPIResponse implements IMessageObject {

    @MessageField(id = "open_regAcctList", name = "")
    private String open_regAcctList;

}
