package com.scbank.process.api.svc.common.service.functions.dto.search;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@IntegrationMessage(id = "FncSchFindAddressResponse", description = "주소검색 응답", type = Type.RESPONSE)
public class FncSchFindAddressResponse implements IMessageObject {

    @MessageField(id = "tototgsu", name = "총건수")
    private Integer tototgsu;

    @MessageField(id = "tojrpgsu", name = "조립건수")
    private Integer tojrpgsu;

    @MessageField(id = "addressInfo", name = "조회내역정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "FncAdrSearchResponse/tojrpgsu")
    private List<AddressInfo> addressInfo;

    @Getter
    @Setter
    public static class AddressInfo implements IMessageObject {

        @MessageField(id = "tozipcd3", name = "새우편번호")
        private String tozipcd3;

        @MessageField(id = "tooldjso1", name = "구주소1")
        private String tooldjso1;

        @MessageField(id = "tooldjso2", name = "구주소2")
        private String tooldjso2;

        @MessageField(id = "tonewjso18", name = "신주소18")
        private String tonewjso18;

        @MessageField(id = "tonewjso20", name = "신주소20")
        private String tonewjso20;

        @MessageField(id = "tonenewjso1", name = "영문신주소1")
        private String tonenewjso1;
    }

    @MessageField(id = "contData", name = "연속거래")
    private String contData;

    @MessageField(id = "contDataLen", name = "연속거래길이")
    private String contDataLen;

}
