package com.scbank.process.api.svc.common.service.settings.dto.code;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetCodListBankCodeResponse", description = "은행코드목록 응답 DTO", type = Type.RESPONSE)
public class SetCodListBankCodeResponse implements IMessageObject {

    @MessageField(id = "count", name = "코드건수")
    private Integer count;

    @MessageField(id = "codes", name = "코드목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SetCodListBankCodeResponse/count")
    private List<CodeDto> codes;

}
