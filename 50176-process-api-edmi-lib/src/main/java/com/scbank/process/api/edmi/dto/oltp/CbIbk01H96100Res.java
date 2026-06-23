package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H96100Res", type = Type.RESPONSE, description = "해외/특정IP차단서비스 조회 응답 전문")
public class CbIbk01H96100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOTOYB", name = "전체/특정", length = 1)
    private String YOTOYB;

    @MessageField(id = "YONTSU", name = "국가갯수", length = 2)
    private int YONTSU;

    @MessageField(id = "ComNameReqRlt", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H96100Res/YONTSU")
    private List<ComNameReqRltDto> ComNameReqRlt;

    @Getter
    @Setter
    public static class ComNameReqRltDto implements IMessageObject {
        @MessageField(id = "YONTNM", name = "국가이름", length = 12)
        private String YONTNM;

        @MessageField(id = "YONTCD", name = "국가코드", length = 3)
        private String YONTCD;
    }
}
