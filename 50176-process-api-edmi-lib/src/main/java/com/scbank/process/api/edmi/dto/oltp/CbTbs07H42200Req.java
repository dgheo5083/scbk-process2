package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs07H42200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "자동이체거래내역조회 요청부")
public class CbTbs07H42200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransKind", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransKind;

    @MessageField(id = "YgMsNum", name = "요구명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YgMsNum;

    @MessageField(id = "KBranchNum1", name = "연속-점번호1", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KBranchNum1;

    @MessageField(id = "KCustNum1", name = "연속-고객번호1", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KCustNum1;

    @MessageField(id = "KGmCode1", name = "연속-과목코드1", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KGmCode1;

    @MessageField(id = "KAcctNum1", name = "연속-계좌번호1", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KAcctNum1;

    @MessageField(id = "KTransDate1", name = "연속-이체일자1", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KTransDate1;

    @MessageField(id = "KBankCode1", name = "연속-은행코드1", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KBankCode1;

    @MessageField(id = "KTransAcctNum1", name = "연속-이체계좌번호1", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KTransAcctNum1;

    @MessageField(id = "KBranchNum2", name = "연속-점번호2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KBranchNum2;

    @MessageField(id = "KCustNum2", name = "연속-고객번호2", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String KCustNum2;

    @MessageField(id = "KCgAcctNum2", name = "연속-출금계좌번호2", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KCgAcctNum2;

    @MessageField(id = "KGiroNum2", name = "연속-지로번호2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KGiroNum2;

    @MessageField(id = "KNapNum2", name = "연속-납부자번호2", length = 25, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String KNapNum2;

}
