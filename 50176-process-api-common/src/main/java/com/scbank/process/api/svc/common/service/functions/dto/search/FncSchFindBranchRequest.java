package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindBranchRequest", description = "영업점찾기 요청", type = Type.REQUEST)
public class FncSchFindBranchRequest implements IMessageObject {

    @MessageField(id = "searchKeyword", name = "검색어", example = "경기도")
    private String searchKeyword;
}
