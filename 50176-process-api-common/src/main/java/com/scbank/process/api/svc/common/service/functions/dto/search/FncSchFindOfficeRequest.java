package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindOfficeRequest", description = "직장검색 요청", type = Type.REQUEST)
public class FncSchFindOfficeRequest implements IMessageObject {

    @MessageField(id = "sibrno", name = "점번호", defaultValue = "086")
    private String sibrno;

    @MessageField(id = "sitaxid", name = "사업자번호", defaultValue = "")
    private String sitaxid;

    @MessageField(id = "siupchnm", name = "업체명", defaultValue = "", example = "테스트")
    private String siupchnm;

    @MessageField(id = "sinxttbl", name = "연속거래", defaultValue = "01")
    private String sinxttbl;

}
