package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbFep01001Res", type = Type.RESPONSE, description = "해외출입국 여부 확인 응답부")
public class MciIbFep01001Res implements IMessageObject {

    @MessageField(id = "DUMMY_CD", name = "더미코드", length = 30)
    private String DUMMY_CD;

    @MessageField(id = "CLASS_CD", name = "전문종별코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CLASS_CD;

    @MessageField(id = "BIZ_KIND_CD", name = "업무구분코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BIZ_KIND_CD;

    @MessageField(id = "REQ_DT", name = "전문전송일시", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String REQ_DT;

    @MessageField(id = "REC_CNT", name = "레코드수", length = 4)
    private Integer REC_CNT;

    @MessageField(id = "REC_LEN", name = "레코드길이", length = 4)
    private Integer REC_LEN;

    @MessageField(id = "INQ_SERIAL_NO", name = "조회일련번호", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String INQ_SERIAL_NO;

    @MessageField(id = "ORG_CD", name = "참가기관코드", length = 20)
    private String ORG_CD;

    @MessageField(id = "RES_CD", name = "응답코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String RES_CD;

    @MessageField(id = "ABROAD_YN", name = "출국여부코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ABROAD_YN;

    @MessageField(id = "ORG_CD", name = "조회자한글성명", length = 24)
    private String INQ_CUS_NAME;

    @MessageField(id = "INQ_CUS_SSN", name = "조회자주민번호", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String INQ_CUS_SSN;

    @MessageField(id = "RESERVE_FLD", name = "참가기관예비필드", length = 20)
    private String RESERVE_FLD;
}
