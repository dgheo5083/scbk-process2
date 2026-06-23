package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H90200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "온라인OTP 분실신고해제 거래 요청부")
public class CbTbs03H90200Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIDUMMY", name = "매체구분", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}