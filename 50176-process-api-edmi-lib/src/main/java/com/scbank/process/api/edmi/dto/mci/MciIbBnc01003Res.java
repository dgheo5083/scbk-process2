package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbBnc01003Res", type = Type.RESPONSE, captureSystem = "MCI", description = "해약환급금조회 응답부")
public class MciIbBnc01003Res implements IMessageObject {
    @MessageField(id = "FO_ERRCD", name = "", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRCD;

    @MessageField(id = "FO_ERRMSG", name = "", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_ERRMSG;

    @MessageField(id = "FO_INSUNAME", name = "", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_INSUNAME;

    @MessageField(id = "FO_PRODTNAME", name = "", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_PRODTNAME;

    @MessageField(id = "FO_CONTNAME", name = "", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CONTNAME;

    @MessageField(id = "FO_CURRENCY", name = "", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FO_CURRENCY;

    @MessageField(id = "FO_RFND", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_RFND;

    @MessageField(id = "FO_PLLNAMT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_PLLNAMT;

    @MessageField(id = "FO_PMAMT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_PMAMT;

    @MessageField(id = "FO_PLLNPOSAMT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_PLLNPOSAMT;

    @MessageField(id = "FO_NRMINT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_NRMINT;

    @MessageField(id = "FO_ARRINT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_ARRINT;

    @MessageField(id = "FO_TAXAMT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_TAXAMT;

    @MessageField(id = "FO_REALAMT", name = "", length = 17, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FO_REALAMT;

}
