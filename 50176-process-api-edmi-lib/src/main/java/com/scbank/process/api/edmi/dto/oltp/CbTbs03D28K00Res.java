package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03D28K00Res", type = Type.RESPONSE, description = "자행 착오송금 본처리 응답부", captureSystem = "OLTP")
public class CbTbs03D28K00Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRIL;

    @MessageField(id = "YOOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOGJNO;

    @MessageField(id = "YOJINM", name = "지급인명", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJINM;

    @MessageField(id = "YOTICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTICGM;

    @MessageField(id = "YOSCNM", name = "출금통장표시내용", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSCNM;

    @MessageField(id = "YOCGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCGIL;

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 99, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
