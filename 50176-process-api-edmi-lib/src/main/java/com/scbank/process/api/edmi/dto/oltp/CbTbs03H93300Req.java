package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H93300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "OTP안전카드사고신고 요청부")
public class CbTbs03H93300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "GuBun", name = "구분(1조회 2사고신고)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String GuBun;

    @MessageField(id = "TelNo1", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo1;

    @MessageField(id = "TelNo2", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo2;

    @MessageField(id = "TelNo3", name = "전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TelNo3;

}