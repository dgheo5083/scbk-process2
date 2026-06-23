package com.scbank.process.api.svc.common.service.functions.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindAddressRequest", description = "주소검색 요청", type = Type.REQUEST)
public class FncSchFindAddressRequest implements IMessageObject {

    @MessageField(id = "tisidona", name = "시도명", example = "서울특별시")
    private String tisidona;

    @MessageField(id = "tisiggna", name = "시군구명")
    private String tisiggna;

    @MessageField(id = "tijsojh", name = "신주소조회", example = "860")
    private String tijsojh;

    @MessageField(id = "contData", name = "연속거래")
    private String contData;

    @MessageField(id = "contDataLen", name = "연속거래길이")
    private String contDataLen;

}
