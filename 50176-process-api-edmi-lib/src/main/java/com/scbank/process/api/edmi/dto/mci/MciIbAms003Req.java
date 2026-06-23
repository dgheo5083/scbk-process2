package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbAms003Req", type = Type.REQUEST, captureSystem = "MCI", description = "MTS_조회거래")
public class MciIbAms003Req implements IMessageObject {
    @MessageField(id = "FIDUMMY15", name = "API HEAD IN", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FIDUMMY15;

    @MessageField(id = "RESEVE", name = "RESERVE", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RESEVE;

    @MessageField(id = "SIJUMIN", name = "주민사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SIJUMIN;

    @MessageField(id = "FLDdelimiter1", name = "delimiter1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "delimiter1", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "delimiter1", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "delimiter2", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
