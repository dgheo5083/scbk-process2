package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs07H59100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "국세이체 예비처리및관세조회")
public class CbTbs07H59100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIO_K_GJNum", name = "연속-지정번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIO_K_GJNum;

    @MessageField(id = "YIO_K_Spare", name = "연속-예비", length = 47)
    private String YIO_K_Spare;

    @MessageField(id = "YI_ReqCnt", name = "요구명세수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YI_ReqCnt;

    @MessageField(id = "YI_ReferCode", name = "조회구분", length = 1)
    private String YI_ReferCode;

    @MessageField(id = "E2ERegNum", name = "고객관리번호", length = 13)
    private String E2ERegNum;

    @MessageField(id = "YIO_ElePayNum", name = "전자납부번호", length = 19)
    private String YIO_ElePayNum;

    @MessageField(id = "YIO_AdminArea", name = "행정구역", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIO_AdminArea;

    @MessageField(id = "YIO_UseLocalGiroNum", name = "지자체명", length = 7)
    private String YIO_UseLocalGiroNum;

    @MessageField(id = "YI_UcGb", name = "업체구분", length = 1)
    private String YI_UcGb;

    @MessageField(id = "YITRGB", name = "이체화면구분", length = 1)
    private String YITRGB;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 14)
    private String YIOGJNO;

    @MessageField(id = "YIOGPAS", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int YIOGPAS;

    @MessageField(id = "YIMOBILE3", name = "모바일3.0 Y(계좌비밀번호검증PASS)", length = 1)
    private String YIMOBILE3;

}
