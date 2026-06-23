package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciDy7200170001Res", type = Type.RESPONSE, description = "담보대출 대출취소")
public class MciDy7200170001Res implements IMessageObject {
    @MessageField(id = "FORESERVE5", name = "API HEAD OUT", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FORESERVE5;

    @MessageField(id = "FOERRCOD", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOERRCOD;

    @MessageField(id = "FOERRNAME", name = "ERROR한글명30", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FOERRNAME;

    @MessageField(id = "FOTOTCNT", name = "전체건수", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FOTOTCNT;

    @MessageField(id = "FONXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FONXTTBL;

    @MessageField(id = "FOGUNSU", name = "건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FOGUNSU;
}
