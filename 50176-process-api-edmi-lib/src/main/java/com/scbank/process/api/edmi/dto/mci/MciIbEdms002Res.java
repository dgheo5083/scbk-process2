package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbEdms002Res", type = Type.RESPONSE, captureSystem = "MCI", description = "EDMS 계약서류조회 조회수/다운로드수 증가")
public class MciIbEdms002Res implements IMessageObject {

    @MessageField(id = "DUMMY_TEMP1", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DUMMY_TEMP1;

    @MessageField(id = "RESCODE", name = "정상코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RESCODE;

    @MessageField(id = "BODY_LENGTH", name = "BODY부LENGTH", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int BODY_LENGTH;

    @MessageField(id = "AOMACRONM", name = "마크로명으로 보임", length = 21, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOMACRONM;

    @MessageField(id = "DUMMY_TEMP2", name = "무슨데이터인지모름", length = 131, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private int DUMMY_TEMP2;
}