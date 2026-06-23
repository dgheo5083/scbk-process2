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
@IntegrationMessage(id = "SetCodListResponse", description = "코드관리 응답 DTO", type = Type.RESPONSE)
public class SetCodListResponse implements IMessageObject {

    @MessageField(id = "categoriesCount", name = "카테고리별 코드목록 건수")
    private Integer categoriesCount;

    @MessageField(id = "categories", name = "카테고리별 코드목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SettingsCodeResponse/categoriesCount")
    private List<CodeCategoryDto> categories;
}
