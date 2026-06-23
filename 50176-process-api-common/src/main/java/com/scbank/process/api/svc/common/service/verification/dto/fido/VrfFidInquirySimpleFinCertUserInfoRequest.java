package com.scbank.process.api.svc.common.service.verification.dto.fido;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFidInquirySimpleFinCertUserInfoRequest", type = Type.REQUEST)
public class VrfFidInquirySimpleFinCertUserInfoRequest implements IMessageObject {

    @MessageField(id = "digitalCertUpdatedYn", name = "", example = "Y, N")
    private String digitalCertUpdatedYn;

}
