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
@IntegrationMessage(id = "CbIbk01H44K01Res", type = Type.RESPONSE, description = "신탁자산명 조회 응답 전문")
public class CbIbk01H44K01Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YO_UPGB", name = "업무구분", length = 1)
    private String YO_UPGB;

    @MessageField(id = "YO_JRPGSU", name = "출력조립건수", length = 3)
    private Integer YO_JRPGSU;

    @MessageField(id = "TR44K01", name = "신탁자산명조회 record", length = 3)
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H44K01Res/YO_JRPGSU")
    private List<YO_JRPGSU> TR44K01;

    @Getter
    @Setter
    public static class YO_JRPGSU implements IMessageObject {

        @MessageField(id = "YO_GJNO", name = "계좌번호", length = 11)
        private String YO_GJNO;

        @MessageField(id = "YO_ZONG", name = "계좌종별", length = 2)
        private String YO_ZONG;

        @MessageField(id = "YO_JSNO", name = "자산번호", length = 9)
        private String YO_JSNO;

        @MessageField(id = "YO_JSNM", name = "자산명", length = 42)
        private String YO_JSNM;
    }
}
