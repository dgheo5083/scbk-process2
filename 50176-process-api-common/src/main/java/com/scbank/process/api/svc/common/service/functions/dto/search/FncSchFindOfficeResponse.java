package com.scbank.process.api.svc.common.service.functions.dto.search;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindOfficeResponse", description = "직장검색 응답", type = Type.RESPONSE)
public class FncSchFindOfficeResponse implements IMessageObject {

    @MessageField(id = "officeInfoList", name = "직장목록")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<OfficeInfo> officeInfoList;

    @Data
    public static class OfficeInfo implements IMessageObject {
        @MessageField(id = "soebrmupccod", name = "EB업체코드")
        private String soebrmupccod;

        @MessageField(id = "soupchnm", name = "업체명")
        private String soupchnm;

        @MessageField(id = "sotaxid", name = "사업자번호")
        private String sotaxid;
    }
}
