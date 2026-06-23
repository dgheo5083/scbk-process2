package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H59102Req", type = Type.REQUEST, description = "국세이체 예비처리및관세조회 요청부")
public class CbTbs07H59102Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIO_K_GJNum", name = "연속-지정번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIO_K_GJNum;

    @MessageField(id = "YIO_K_Spare", name = "연속-예비", length = 47, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIO_K_Spare;

    @MessageField(id = "YI_ReqCnt", name = "요구명세수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YI_ReqCnt;

    @MessageField(id = "YI_ReferCode", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_ReferCode;

    @MessageField(id = "E2ERegNum", name = "고객관리번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum;

    @MessageField(id = "YIO_ElePayNum", name = "전자납부번호", length = 19, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIO_ElePayNum;

    @MessageField(id = "YIO_AdminArea", name = "행정구역", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIO_AdminArea;

    @MessageField(id = "YIO_UseLocalGiroNum", name = "지자체명", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIO_UseLocalGiroNum;

    @MessageField(id = "YI_UcGb", name = "업체구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YI_UcGb;

    @MessageField(id = "YITRGB", name = "이체화면구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITRGB;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "YIOGPAS", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
    private String YIOGPAS; // 이중암호화 대응

    @MessageField(id = "YIMOBILE3", name = "모바일3.0 Y(계좌비밀번호검증PASS)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMOBILE3;

}
