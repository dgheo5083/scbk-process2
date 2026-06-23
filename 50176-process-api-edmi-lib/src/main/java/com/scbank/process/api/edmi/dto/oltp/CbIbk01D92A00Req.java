package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01D92A00Req", type = Type.REQUEST, description = "공인인증서 휴대폰인증서비스 신청", captureSystem = "OLTP")
public class CbIbk01D92A00Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIUPGU", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGU;

    @MessageField(id = "YIHHP21", name = "휴대폰번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHHP21;

    @MessageField(id = "YIHHP22", name = "휴대폰번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHHP22;

    @MessageField(id = "YIHHP23", name = "휴대폰번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHHP23;
}
