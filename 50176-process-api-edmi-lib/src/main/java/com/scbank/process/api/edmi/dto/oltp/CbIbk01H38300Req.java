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
@IntegrationMessage(id = "CbIbk01H38300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "계약서류제공 신탁 계약서조회")
public class CbIbk01H38300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGRGB", name = "거래구분조회 : 2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGB;

    @MessageField(id = "YIBUNHO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIBUNHO;

    @MessageField(id = "YIURL", name = "URL정보", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIURL;

    @MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

}