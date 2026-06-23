package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "AthMgtVerfiyOdcloudSignRequest", type = Type.REQUEST)
public class AthMgtVerfiyOdcloudSignRequest implements IMessageObject {
	@MessageField(id = "commonCertSignYn", name = "")
    private String commonCertSignYn;

    @MessageField(id = "devlopeCertVerifyFlag", name = "")
    private String devlopeCertVerifyFlag;

    @MessageField(id = "checkFinFlag", name = "")
    private String checkFinFlag;

    @MessageField(id = "pkcs7SignedData", name = "")
    private String pkcs7SignedData;

    @MessageField(id = "finTechSignYN", name = "")
    private String finTechSignYN;
    
    @MessageField(id = "vidRandom", name = "")
    private String vidRandom;
}