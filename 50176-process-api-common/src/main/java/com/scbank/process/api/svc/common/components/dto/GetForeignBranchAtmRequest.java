package com.scbank.process.api.svc.common.components.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 해외영업점 찾기 요청
 */
@Data
@IntegrationMessage(id = "GetForeignBranchAtmRequest", type = Type.REQUEST)
public class GetForeignBranchAtmRequest implements IMessageObject {

    @MessageField(id = "keyWord", name = "키워드")
    private String keyWord;

    @MessageField(id = "ntnl", name = "국가")
    private String ntnl;

    @MessageField(id = "city", name = "도시")
    private String city;

    @MessageField(id = "local", name = "지역")
    private String local;

}
