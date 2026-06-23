package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciYp0049010001Req", type = Type.REQUEST, description = "대출신청조회 및 취소")
public class MciYp0049010001Req implements IMessageObject {
    @MessageField(id = "FIDUMMY15", name = "API HEAD IN", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDUMMY15;

    @MessageField(id = "FIRESEVE", name = "RESERVE", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIRESEVE;

    @MessageField(id = "FIRESEVE9", name = "RESERVE CHR9", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIRESEVE9;

    @MessageField(id = "FIDEBUG", name = "디버그구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDEBUG;

    @MessageField(id = "FINXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FINXTTBL;

    @MessageField(id = "FIGUBUN", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIGUBUN;

    @MessageField(id = "FIJUBNO", name = "접수번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIJUBNO;

    @MessageField(id = "FISINNO", name = "신청번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FISINNO;

    @MessageField(id = "FIJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIJUMIN;

    @MessageField(id = "FICCNCHSHB", name = "취소자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FICCNCHSHB;

    @MessageField(id = "FICHWSAYU", name = "취소사유", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FICHWSAYU;

    @MessageField(id = "FISELFBANKFLG", name = "셀프뱅크여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FISELFBANKFLG;
}
