package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "CbTbs03H32100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드 원화 추가납입")
public class CbTbs03H32100Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "JsNum", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer JsNum;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "GrNum", name = "관리번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer GrNum;

    @MessageField(id = "JYCode", name = "즉시예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String JYCode;

    @MessageField(id = "SgAmt", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SgAmt;

    @MessageField(id = "FeeAmt", name = "선취판매수수료 (9자리로 변환)", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FeeAmt;

    @MessageField(id = "FundAmt", name = "투자금액 (13 추가)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal FundAmt;

    @MessageField(id = "YyDate", name = "입금예정일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YyDate;

    @MessageField(id = "IgBankCode", name = "입금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String IgBankCode;

    @MessageField(id = "IgAcctNumFund", name = "입금계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String IgAcctNumFund;

    @MessageField(id = "FundName", name = "펀드명 신규", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundName;

    @MessageField(id = "NewDate", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NewDate;

    @MessageField(id = "DueDate", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer DueDate;

    @MessageField(id = "FeeCode", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FeeCode;

    @MessageField(id = "TransCode", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransCode;

    @MessageField(id = "AmtSign", name = "출금후잔액 부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AmtSign;

    @MessageField(id = "AfterBal", name = "출금후잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AfterBal;

    @MessageField(id = "EndSale", name = "판매중지펀드", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String EndSale;

    @MessageField(id = "Dummy", name = "dummy", length = 218, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "dummy1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "dummy2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;
}