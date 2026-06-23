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
@IntegrationMessage(id = "CbIbk01H9a400Res", type = Type.RESPONSE, description = "자기앞수표 사고신고 응답부")
public class CbIbk01H9a400Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "CheckNum", name = "수표번호", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CheckNum;

    @MessageField(id = "IssueBranchCode", name = "발행점 Code", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IssueBranchCode;

    @MessageField(id = "CheckKind", name = "수표종류", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CheckKind;

    @MessageField(id = "CheckAmt", name = "금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal CheckAmt;

    @MessageField(id = "IssueBranchName", name = "발행점명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IssueBranchName;

    @MessageField(id = "IssueBranchTele", name = "발행점 전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IssueBranchTele;

    @MessageField(id = "ClientPerNo", name = "의뢰인 주민번호", length = 13, masking = true, maskingType = "05", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientPerNo;

    @MessageField(id = "ClientTele1", name = "의뢰인 지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele1;

    @MessageField(id = "ClientTele2", name = "의뢰인 국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele2;

    @MessageField(id = "ClientTele3", name = "의뢰인 전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ClientTele3;

    @MessageField(id = "DateTime", name = "사고신고일시", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DateTime;

}