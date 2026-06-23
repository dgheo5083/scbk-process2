package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindForeignBranchAtmResponse", type = Type.RESPONSE)
public class SupLocFindForeignBranchAtmResponse implements IMessageObject {

    @MessageField(id = "atmSelect", name = "해외영업점/ATM목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<ForeignBranchAtm> atmSelect;

    @MessageField(id = "moreData", name = "moreData")
    private String moreData;

    @MessageField(id = "pageCount", name = "pageCount")
    private int pageCount;

    @MessageField(id = "keyWord", name = "키워드")
    private String keyWord;

    @Data
    public static class ForeignBranchAtm implements IMessageObject {

        @MessageField(id = "serno", name = "고유번호")
        private String serno;

        @MessageField(id = "lat", name = "위도")
        private String lat;

        @MessageField(id = "lng", name = "경도")
        private String lng;

        @MessageField(id = "type", name = "분류")
        private String type;

        @MessageField(id = "name", name = "영업점/ATM명")
        private String name;

        @MessageField(id = "address", name = "주소")
        private String address;

        @MessageField(id = "businessHours", name = "영업시간")
        private String businessHours;

        @MessageField(id = "tel", name = "영업점번호")
        private String tel;
    }

}
