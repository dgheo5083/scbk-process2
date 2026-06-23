package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01D85B00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "기본화면변경 요청부")
public class CbIbk01D85B00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YISMHP", name = "YISMHP", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISMHP;

    @MessageField(id = "YIHPNO", name = "휴대폰번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHPNO;

    @MessageField(id = "YIGUBN", name = "YIGUBN", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBN;

}
