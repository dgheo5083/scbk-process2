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
@IntegrationMessage(id = "CbTbs03D43700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "예적금 해지 본거래 응답 전문")
public class CbTbs03D43700Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "JSNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String JSNum;

    @MessageField(id = "AcctCustName", name = "고객명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctCustName;

    @MessageField(id = "CloseAcctNum", name = "해약계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CloseAcctNum;

    @MessageField(id = "TermS", name = "예치기간(S)", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TermS;

    @MessageField(id = "TermE", name = "예치기간(E)", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TermE;

    @MessageField(id = "DepositAcctNum", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepositAcctNum;

    @MessageField(id = "CloseCode_H", name = "해약구분(한글)", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CloseCode_H;

    @MessageField(id = "PrincipalAmt", name = "해약원금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PrincipalAmt;

    @MessageField(id = "BasicInterest", name = "기본이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String BasicInterest;

    @MessageField(id = "AfterMaturityInterest", name = "만기후이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AfterMaturityInterest;

    @MessageField(id = "ReOutInterest", name = "환출세액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReOutInterest;

    @MessageField(id = "SumPayment", name = "예금합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SumPayment;

    @MessageField(id = "IncomeTax", name = "소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String IncomeTax;

    @MessageField(id = "ResidualTax", name = "주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ResidualTax;

    @MessageField(id = "FarmTax", name = "농특세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FarmTax;

    @MessageField(id = "SumTax", name = "세금합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SumTax;

    @MessageField(id = "ReInInterest", name = "환입이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ReInInterest;

    @MessageField(id = "DelayMinusInterest", name = "지연공제이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String DelayMinusInterest;

    @MessageField(id = "PreMaturityMinusInterest", name = "만기앞당김이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String PreMaturityMinusInterest;

    @MessageField(id = "SumIncomeAmt", name = "공제금액합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SumIncomeAmt;

    @MessageField(id = "NetPaymentAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String NetPaymentAmt;

    @MessageField(id = "TaxGubun", name = "세금우대구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TaxGubun;

    @MessageField(id = "LiveGubun", name = "세금우대구분(생계형여부)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String LiveGubun;

    @MessageField(id = "GigumAmt", name = "고객기금출연액", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GigumAmt;

    @MessageField(id = "Gigumrate", name = "고객기금출연율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Gigumrate;

    @MessageField(id = "YOJONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJONG;

    @MessageField(id = "YOKMNM", name = "상품명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKMNM;

    @MessageField(id = "YOPOP", name = "멀티상품팝업여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPOP;

    @MessageField(id = "GovernmentPayAmt", name = "정부기여금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GovernmentPayAmt;

    @MessageField(id = "GovernmentInter", name = "정부기여금 이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GovernmentInter;

    @MessageField(id = "GovernmentSumTot", name = "총 지급 합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String GovernmentSumTot;
}
