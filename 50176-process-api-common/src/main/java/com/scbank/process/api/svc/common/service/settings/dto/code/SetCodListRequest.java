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
@IntegrationMessage(id = "SetCodListRequest", description = "코드관리 요청 DTO", type = Type.REQUEST)
public class SetCodListRequest implements IMessageObject {

    @MessageField(id = "categories", name = "코드 카테고리 목록", example = "BKCODE")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<String> categories;

}
