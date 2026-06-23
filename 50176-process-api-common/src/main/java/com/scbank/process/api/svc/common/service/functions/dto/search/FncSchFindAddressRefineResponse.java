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
@IntegrationMessage(id = "FncSchFindAddressRefineResponse", description = "주소정제 응답", type = Type.RESPONSE)
public class FncSchFindAddressRefineResponse implements IMessageObject {

    @MessageField(id = "tonewjsogb2", name = "신주소구분2")
    private String tonewjsogb2;

    @MessageField(id = "tonewjsogb", name = "신주소구분")
    private String tonewjsogb;

    @MessageField(id = "tonewjso1", name = "신주소1")
    private String tonewjso1;

    @MessageField(id = "tonewjso4", name = "신주소4")
    private String tonewjso4;

    @MessageField(id = "tozipcd1", name = "우편번호1")
    private String tozipcd1;

    @MessageField(id = "tozipcd2", name = "우편번호2")
    private String tozipcd2;

    @MessageField(id = "tonewjso2", name = "신주소2")
    private String tonewjso2;

    @MessageField(id = "tonewjso5", name = "신주소5")
    private String tonewjso5;

    @MessageField(id = "toroadnacod", name = "도로명코드")
    private String toroadnacod;

    @MessageField(id = "toroadseq3", name = "기타도로일련번호")
    private String toroadseq3;

    @MessageField(id = "tojihayb", name = "지하여부")
    private String tojihayb;

    @MessageField(id = "tobldno", name = "건물번호")
    private String tobldno;

    @MessageField(id = "tojrpgsu", name = "조립건수")
    private Integer tojrpgsu;

    @MessageField(id = "addressRefineInfo", name = "조회내역정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "FncSchFindAddressRefineResponse/tojrpgsu")
    private List<AddressRefineInfo> addressRefineInfo;

    @Data
    public static class AddressRefineInfo implements IMessageObject {

        @MessageField(id = "tozipcd1", name = "우편번호1")
        private String tozipcd1;

        @MessageField(id = "tozipcd2", name = "우편번호2")
        private String tozipcd2;

        @MessageField(id = "tonewjso3", name = "신주소3")
        private String tonewjso3;

        @MessageField(id = "tonewjso5", name = "신주소5")
        private String tonewjso5;

        @MessageField(id = "toroadnacod", name = "도로명코드")
        private String toroadnacod;

        @MessageField(id = "toroadseq3", name = "기타도로일련번호")
        private String toroadseq3;

        @MessageField(id = "tojihayb", name = "지하여부")
        private String tojihayb;

        @MessageField(id = "tobldno", name = "건물번호")
        private String tobldno;

    }
}
