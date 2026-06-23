package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciHpCuCusCosReq", type = Type.REQUEST, captureSystem = "MCI", description = "전화상담(MCI) 요청부")
public class MciHpCuCusCosReq implements IMessageObject {

    @MessageField(id = "RES_DT", name = "RES_DT", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RES_DT;

    @MessageField(id = "RES_TM", name = "RES_TM", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RES_TM;

    @MessageField(id = "SSN", name = "SSN", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SSN;

    @MessageField(id = "PHONE_NO", name = "PHONE_NO", length = 12, masking = true, maskingType = "05", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PHONE_NO;

    @MessageField(id = "SKILL", name = "SKILL", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SKILL;

    @MessageField(id = "CHNL_DIV", name = "CHNL_DIV", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CHNL_DIV;

    @MessageField(id = "GRADE", name = "GRADE", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GRADE;

    @MessageField(id = "FLDdelimiter1", name = "FLDdelimiter1", length = 1, defaultValue = "0x1F")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "SEGdelimiter1", length = 1, defaultValue = "0x1E")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "ENDdelimiter1", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "ENDdelimiter2", length = 1, defaultValue = "0xFF")
    private String ENDdelimiter2;
}
