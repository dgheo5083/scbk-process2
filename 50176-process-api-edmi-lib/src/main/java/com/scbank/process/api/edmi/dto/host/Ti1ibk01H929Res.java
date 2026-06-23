package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "Ti1ibk01H929Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "개인사업자 조회 및 검증 응답 전문")
public class Ti1ibk01H929Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOYCONT", name = "카운트", length = 1)
    private String YOYCONT;

    @MessageField(id = "YOYSAUP", name = "사업", length = 10)
    private String YOYSAUP;

    @MessageField(id = "YOYCIFNO", name = "번호", length = 13)
    private String YOYCIFNO;

    @MessageField(id = "YOSACHK", name = "사업자체크", length = 1)
    private String YOSACHK;

    @MessageField(id = "YOSANO", name = "사업자번호", length = 10)
    private String YOSANO;

    @MessageField(id = "YOGDUMY", name = "더미", length = 10)
    private String YOGDUMY;

    @MessageField(id = "YOMSSU", name = "명세수", length = 5)
    private String YOMSSU;

    @MessageField(id = "REC_01", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<TI1IBK01_H929_OUT_REC_01> REC_01;

    @Getter
    @Setter
    public static class TI1IBK01_H929_OUT_REC_01 implements IMessageObject {

        @MessageField(id = "SAUP", name = "개인사업자번호", length = 10)
        private String SAUP;

        @MessageField(id = "NAME", name = "상호명", length = 32)
        private String NAME;

        @MessageField(id = "UPZON", name = "업종구분", length = 4)
        private String UPZON;

        @MessageField(id = "GIUP", name = "기업규모", length = 2)
        private String GIUP;

        @MessageField(id = "CHAGB", name = "차주구분", length = 2)
        private String CHAGB;

        @MessageField(id = "SINUP", name = "신업종코드1", length = 6)
        private String SINUP;

        @MessageField(id = "DSMUP", name = "신업종코드2", length = 6)
        private String DSMUP;

        @MessageField(id = "ADUMMY", name = "어레이더미", length = 8)
        private String ADUMMY;

    }
}
