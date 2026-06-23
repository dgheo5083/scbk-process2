package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01D96000Res", type = Type.RESPONSE, description = "해외/특정IP차단서비스 등록 응답 전문")
public class CbIbk01D96000Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOSTGB", name = "전체/특정", length = 1)
    private String YOSTGB;

    @MessageField(id = "YOGUNSU", name = "국가갯수", length = 2)
    private Integer YOGUNSU;

    @MessageField(id = "ComNameReqRlt", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01D96000Res/YOGUNSU")
    private List<ComNameReqRltDto> ComNameReqRlt;

    @Data
    public static class ComNameReqRltDto implements IMessageObject {
        @MessageField(id = "YONTNM", name = "국가이름", length = 12)
        private String YONTNM;

        @MessageField(id = "YONTCD", name = "국가코드", length = 3)
        private String YONTCD;
    }
}
