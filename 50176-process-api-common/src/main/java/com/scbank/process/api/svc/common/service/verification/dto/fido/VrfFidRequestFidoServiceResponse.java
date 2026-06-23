package com.scbank.process.api.svc.common.service.verification.dto.fido;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "VrfFidRequestFidoServiceResponse", type = Type.RESPONSE)
public class VrfFidRequestFidoServiceResponse implements IMessageObject {

    @MessageField(id = "resultCode", name = "", example = "")
    private String resultCode;

    @MessageField(id = "resultMsg", name = "", example = "")
    private String resultMsg;

    @MessageField(id = "aaidPin", name = "", example = "")
    @RepeatedField
    private List<String> aaidPin;

    @MessageField(id = "aaidFinger", name = "", example = "")
    @RepeatedField
    private List<String> aaidFinger;

    @MessageField(id = "notAfter", name = "", example = "")
    private String notAfter;

    @MessageField(id = "regYn", name = "", example = "")
    private String regYn;

    @MessageField(id = "executedDeviceInfoUpdateYn", name = "", example = "")
    private String executedDeviceInfoUpdateYn;

    @MessageField(id = "trId", name = "", example = "")
    private String trId;

    @MessageField(id = "userBankingId", name = "", example = "")
    private String userBankingId;

    @MessageField(id = "trStatus", name = "", example = "")
    private String trStatus;

    @MessageField(id = "trStatusMsg", name = "", example = "")
    private String trStatusMsg;

    @MessageField(id = "signedData", name = "", example = "")
    private String signedData;

    @MessageField(id = "signedDataList", name = "", example = "")
    private String signedDataList;

}
