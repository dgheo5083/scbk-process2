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
@IntegrationMessage(id = "CbIbk01D93400Req", type = Type.REQUEST, captureSystem = "OLTP", description = "자기앞수표 사고신고 요청부")
public class CbIbk01D93400Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CheckNum", name = "수표번호", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CheckNum;

    @MessageField(id = "IssueBranchCode", name = "발행점 Code", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String IssueBranchCode;

    @MessageField(id = "CheckKind", name = "수표종류", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CheckKind;

    @MessageField(id = "CheckAmt", name = "금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CheckAmt;

    @MessageField(id = "E2ERegNum", name = "의뢰인 주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum;

    @MessageField(id = "ClientTele1", name = "의뢰인 지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele1;

    @MessageField(id = "ClientTele2", name = "의뢰인 국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele2;

    @MessageField(id = "ClientTele3", name = "의뢰인 전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele3;

}