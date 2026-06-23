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
@IntegrationMessage(id = "CbTbs03H43800Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "입출금 해지 본거래 응답 전문")
public class CbTbs03H43800Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "ApplNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ApplNum;

    @MessageField(id = "AcctCustName", name = "예금주", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctCustName;

    @MessageField(id = "CloseAcctNum", name = "해약계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CloseAcctNum;

    @MessageField(id = "NewOpenDate", name = "개설일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NewOpenDate;

    @MessageField(id = "YGKind", name = "예금종류코드", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YGKind;

    @MessageField(id = "DepositAcctFlag", name = "수신계좌플래그", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositAcctFlag;

    @MessageField(id = "DepositAcctNum", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositAcctNum;

    @MessageField(id = "PrincipalAmt", name = "해지원금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PrincipalAmt;

    @MessageField(id = "BasicInterest", name = "기본이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BasicInterest;

    @MessageField(id = "IncomeTax", name = "소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String IncomeTax;

    @MessageField(id = "ResidenceTax", name = "주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ResidenceTax;

    @MessageField(id = "SumTax", name = "세금합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SumTax;

    @MessageField(id = "NetPaymentAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NetPaymentAmt;

    @MessageField(id = "CGPassbookBranch", name = "통장관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CGPassbookBranch;

    @MessageField(id = "CGPassbookBranchH", name = "통장관리점(한글)", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CGPassbookBranchH;
}
