package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 해외영업점 찾기 응답
 */
@Data
@IntegrationMessage(id = "GetForeignBranchAtmResponse", type = Type.REQUEST)
public class GetForeignBranchAtmResponse implements IMessageObject {

    @MessageField(id = "countData", name = "데이터수")
    private int countData;
}
