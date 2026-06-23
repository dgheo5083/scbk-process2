package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@IntegrationMessage(id = "MciIbBnc01001Req", type = Type.REQUEST, captureSystem = "MCI", description = "보험 상품 리스트")
public class MciIbBnc01001Req implements IMessageObject {
    @MessageField(id = "FI_CONTINUOUS", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FI_CONTINUOUS;

    @MessageField(id = "FI_PAGENO", name = "", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FI_PAGENO;

    @MessageField(id = "FI_GUBUN", name = "", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FI_GUBUN;

    @MessageField(id = "FI_JUMINNO", name = "", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String FI_JUMINNO;

}