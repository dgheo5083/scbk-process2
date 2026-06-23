package com.scbank.process.api.svc.common.service.securities.dto.blocking;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * PRC 서비스 응답 정보 클래스
 * 해외IP차단서비스 본거래
 */
@Data
@IntegrationMessage(id = "SecBlkApplyBlockingForeignIpMainResponse", description = "해외IP차단서비스 본거래 응답 DTO", type = Type.RESPONSE)
public class SecBlkApplyBlockingForeignIpMainResponse implements IMessageObject {

    @MessageField(id = "userID", name = "이용자번호")
    private String userID;

    @MessageField(id = "yoSTGB", name = "전체/특정")
    private String yoSTGB;

    @MessageField(id = "yoGUNSU", name = "국가갯수")
    private Integer yoGUNSU;

    @MessageField(id = "comNameReqRlt", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<ComNameReqRltDto> comNameReqRlt;

    @Data
    public static class ComNameReqRltDto implements IMessageObject {
        @MessageField(id = "yoNTNM", name = "국가이름")
        private String yoNTNM;

        @MessageField(id = "yoNTCD", name = "국가코드")
        private String yoNTCD;
    }
}
