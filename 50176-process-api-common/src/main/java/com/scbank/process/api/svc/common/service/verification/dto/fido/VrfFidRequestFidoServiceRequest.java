package com.scbank.process.api.svc.common.service.verification.dto.fido;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFidRequestFidoServiceRequest", type = Type.REQUEST)
public class VrfFidRequestFidoServiceRequest implements IMessageObject {

    @MessageField(id = "fidoCommand", name = "", example = "allowedAuthnr, checkRegisteredStatus, requestP7Sign")
    private String fidoCommand;

    @MessageField(id = "fidoAppId", name = "", example = "")
    private String fidoAppId;

    @MessageField(id = "fidoDeviceId", name = "", example = "")
    private String fidoDeviceId;

    @MessageField(id = "fidoVerifyType", name = "", example = "2, 16384")
    private String fidoVerifyType;

    @MessageField(id = "fidoSignData", name = "", example = "")
    private String fidoSignData;

    @MessageField(id = "digitalCertUpdatedYn", name = "", example = "Y, N")
    private String digitalCertUpdatedYn;

    @MessageField(id = "userBankingId", name = "", example = "")
    private String userBankingId;

}
