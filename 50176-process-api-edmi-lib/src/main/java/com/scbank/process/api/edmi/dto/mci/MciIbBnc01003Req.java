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
@IntegrationMessage(id = "MciIbBnc01003Req", type = Type.REQUEST, captureSystem = "MCI", description = "해약환급금조회 요청부")
public class MciIbBnc01003Req implements IMessageObject {
    @MessageField(id = "FI_POLINO", name = "", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FI_POLINO;

    @MessageField(id = "FI_JUMINNO", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String FI_JUMINNO;

}
