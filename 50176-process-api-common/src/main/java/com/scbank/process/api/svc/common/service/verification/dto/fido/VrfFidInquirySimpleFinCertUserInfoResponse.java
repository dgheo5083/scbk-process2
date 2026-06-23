package com.scbank.process.api.svc.common.service.verification.dto.fido;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFidInquirySimpleFinCertUserInfoResponse", type = Type.RESPONSE)
public class VrfFidInquirySimpleFinCertUserInfoResponse implements IMessageObject {

    @MessageField(id = "connectType", name = "", example = "")
    private String connectType;

    @MessageField(id = "finCertSeqNum", name = "", example = "")
    private String finCertSeqNum;

    @MessageField(id = "finCertSimpleKeyToken", name = "", example = "")
    private String finCertSimpleKeyToken;

}
