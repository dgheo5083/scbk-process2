package com.scbank.process.api.svc.common.service.functions.dto.holiday;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncHldListResponse", description = "휴일목록조회 응답", type = Type.RESPONSE)
public class FncHldListResponse implements IMessageObject {

    @MessageField(id = "holidayList", name = "휴일목록")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<HolidayInfo> holidayList;

    @Data
    public static class HolidayInfo implements IMessageObject {

        @MessageField(id = "date", name = "휴일")
        private String date;

        @MessageField(id = "description", name = "설명")
        private String description;
    }
}
