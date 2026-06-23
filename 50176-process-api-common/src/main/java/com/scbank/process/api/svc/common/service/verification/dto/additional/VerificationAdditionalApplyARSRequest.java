package com.scbank.process.api.svc.common.service.verification.dto.additional;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VerificationAdditionalApplyARSRequest", description = "추가인증 - ARS 인증 요청 DTO", type = Type.REQUEST)
public class VerificationAdditionalApplyARSRequest implements IMessageObject {

    @MessageField(id = "transType", name = "거래구분")
    private String transType;

    @MessageField(id = "authTelNo", name = "인증요청 전화번호")
    private String authTelNo;

    @MessageField(id = "account", name = "계좌번호(출금? 입금?)")
    private String account;

    @MessageField(id = "clientName", name = "clientName")
    private String clientName;

    @MessageField(id = "wasTranNo", name = "wasTranNo")
    private String wasTranNo;

}
