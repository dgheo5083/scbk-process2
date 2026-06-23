package com.scbank.process.api.svc.common.service.settings.dto.code;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetCodListBankCodeRequest", description = "은행코드목록 요청 DTO", type = Type.REQUEST)
public class SetCodListBankCodeRequest implements IMessageObject {

    @MessageField(id = "taxDisplayYn", name = "지방세/국고금 노출여부", defaultValue = "N", example = "Y")
    private String taxDisplayYn;

    @MessageField(id = "scbankDisplayYn", name = "SC제일은행 노출여부", defaultValue = "Y", example = "Y")
    private String scbankDisplayYn;

    @MessageField(id = "codeType", name = "은행코드 타입", defaultValue = "", example = "obs")
    private String codeType;

}
