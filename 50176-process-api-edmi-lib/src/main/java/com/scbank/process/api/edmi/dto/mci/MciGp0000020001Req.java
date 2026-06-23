package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@IntegrationMessage(id = "MciGp0000020001Req", type = Type.REQUEST, xmlRootName = "TRANDATA")
public class MciGp0000020001Req implements IMessageObject {

    @MessageField(id = "TIROADCD1", name = "도로명", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TIROADCD1;

    @MessageField(id = "TIROADSEQ", name = "도로일련번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TIROADSEQ;

    @MessageField(id = "TINEWJSJHAYB", name = "신주소지하여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TINEWJSJHAYB;

    @MessageField(id = "TIBLDNO", name = "건물번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TIBLDNO;
}
