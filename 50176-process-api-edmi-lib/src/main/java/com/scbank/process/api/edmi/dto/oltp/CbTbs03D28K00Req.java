package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03D28K00Req", type = Type.REQUEST, description = "자행 착오송금 본처리 요청부", captureSystem = "OLTP")
public class CbTbs03D28K00Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AcctPassword;

    @MessageField(id = "YIICGB", name = "즉시/예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICGB;

    @MessageField(id = "YICGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YICGIL;

    @MessageField(id = "YISEQN", name = "명세번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISEQN;

    @MessageField(id = "YITICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YITICGM;

}
