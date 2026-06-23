package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbTbs03H96300Res", type = Type.RESPONSE, description = "타행계좌 유효성 검증 응답부")

public class CbTbs03H96300Res implements IMessageObject {

    @MessageField(id="YO_GRGB",name="거래구분",length=1,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_GRGB;

    @MessageField(id="UserID",name="이용자번호",length=10,masking=true,maskingType="01",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String UserID;

    @MessageField(id="YO_ERCD",name="ERROR  CODE",length=4,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_ERCD;

    @MessageField(id="YO_MODN",name="MODULE NAME",length=8,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_MODN;

    @MessageField(id="YO_YNAME",name="예금주명",length=80,masking=true,maskingType="04",align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_YNAME;

    @MessageField(id="YO_ICIL",name="이체실행일자",length=8,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_ICIL;

    @MessageField(id="YO_DASU",name="다수계좌여부(Y/N)",length=1,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_DASU;

    @MessageField(id="YO_DEPO",name="대포통장여부(Y/N)",length=1,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_DEPO;

    @MessageField(id="YO_NGJNO",name="원래계좌번호(신)",length=14,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_NGJNO;

    @MessageField(id="YO_BDMGS",name="비대면건수확인",length=1,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_BDMGS;

    @MessageField(id="YO_DUMY",name="DUMMY",length=6,align=AlignType.LEFT,padding=StringUtils.SPACE)
    private String YO_DUMY;

}
