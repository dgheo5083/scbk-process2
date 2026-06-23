package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "MciIbBnc01002Req", type = Type.REQUEST, captureSystem = "MCI", description = "보험 계약상세조회 요청부")
public class MciIbBnc01002Req implements IMessageObject {

    @MessageField(id = "FI_POLINO", name = "", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FI_POLINO;

    @MessageField(id = "FI_JUMINNO", name = "", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FI_JUMINNO;

}