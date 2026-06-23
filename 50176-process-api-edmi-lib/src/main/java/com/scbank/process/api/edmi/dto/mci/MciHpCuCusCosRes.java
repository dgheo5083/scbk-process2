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
@IntegrationMessage(id = "MciHpCuCusCosRes", type = Type.RESPONSE, captureSystem = "MCI", description = "전화상담(MCI) 응답부")
public class MciHpCuCusCosRes implements IMessageObject {

    @MessageField(id = "TORESULT", name = "TORESULT", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TORESULT;
}
