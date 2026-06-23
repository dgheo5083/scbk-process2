package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindEmployeeRequest", description = "직원검색 요청", type = Type.REQUEST)
public class FncSchFindEmployeeRequest implements IMessageObject {

    @MessageField(id = "emplyNm", name = "직원명", example = "김")
    private String emplyNm;

    @MessageField(id = "clerkNo", name = "직원번호", defaultValue = "")
    private String clerkNo;

    @MessageField(id = "loanTypeProduct", name = "대출상품여부", defaultValue = "N")
    private String loanTypeProduct;
}
