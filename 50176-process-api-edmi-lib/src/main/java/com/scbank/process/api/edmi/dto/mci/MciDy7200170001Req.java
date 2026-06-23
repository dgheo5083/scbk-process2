package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MCI_DY7200170001_REQ", type = Type.REQUEST, description = "담보대출 대출취소")
public class MciDy7200170001Req implements IMessageObject {
    @MessageField(id = "FIDUMMY15", name = "API HEAD IN", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDUMMY15;

    @MessageField(id = "FIRESEVE", name = "RESERVE", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIRESEVE;

    @MessageField(id = "FIBRNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FIBRNO;

    @MessageField(id = "FILTERM", name = "LTERM 번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FILTERM;

    @MessageField(id = "FIDEBUG", name = "디버그구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDEBUG;

    @MessageField(id = "FINXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FINXTTBL;

    @MessageField(id = "FIGUBUN", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIGUBUN;

    @MessageField(id = "FIDCGB", name = "대출구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDCGB;

    @MessageField(id = "FIJUBNO", name = "접수번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIJUBNO;

    @MessageField(id = "FICGJBN", name = "취급자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FICGJBN;
}
