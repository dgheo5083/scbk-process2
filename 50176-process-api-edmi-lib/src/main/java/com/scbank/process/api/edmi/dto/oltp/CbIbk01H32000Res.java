package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H32000Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드추가입금 적정성체크 응답부")
public class CbIbk01H32000Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJNO;

    @MessageField(id = "YOCPBNO", name = "TBGTCPB SEQNO", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCPBNO;

    @MessageField(id = "YOCPBDG", name = "CIPS 등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCPBDG;

    @MessageField(id = "YOPRRDG", name = "PRR  등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPRRDG;

    @MessageField(id = "YODUMMY", name = "dummy", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;
}
