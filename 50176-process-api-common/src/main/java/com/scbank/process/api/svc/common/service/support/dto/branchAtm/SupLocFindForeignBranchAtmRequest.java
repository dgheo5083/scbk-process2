package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindForeignBranchAtmRequest", type = Type.REQUEST)
public class SupLocFindForeignBranchAtmRequest implements IMessageObject {

    @MessageField(id = "keyWord", name = "키워드")
    private String keyWord;

    @MessageField(id = "ntnl", name = "국가")
    private String ntnl;

    @MessageField(id = "city", name = "도시")
    private String city;

    @MessageField(id = "local", name = "지역")
    private String local;

    @MessageField(id = "pageCount", name = "pageCount")
    private String pageCount;

}
