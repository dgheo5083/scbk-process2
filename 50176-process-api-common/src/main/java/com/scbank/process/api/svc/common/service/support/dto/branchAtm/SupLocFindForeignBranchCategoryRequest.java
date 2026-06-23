package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindForeignBranchCategoryRequest", type = Type.REQUEST)
public class SupLocFindForeignBranchCategoryRequest implements IMessageObject {

    @MessageField(id = "ntnl", name = "국가")
    private String ntnl;

    @MessageField(id = "city", name = "도시")
    private String city;

    @MessageField(id = "searchKey", name = "검색구분")
    private String searchKey;

}
