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
@IntegrationMessage(id = "CbIbk01H11405Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "잔액조회(수익증권) 응답부")
public class CbIbk01H11405Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String AcctNum;

    @MessageField(id = "ItemName", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ItemName;

    @MessageField(id = "CustName", name = "예금주", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "AdminBranch", name = "관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AdminBranch;

    @MessageField(id = "AdminBranchName", name = "관리점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AdminBranchName;

    @MessageField(id = "FundCode", name = "펀드번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundCode;

    @MessageField(id = "FundName", name = "펀드명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String FundName;

    @MessageField(id = "DepItemZong", name = "예금종별", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DepItemZong;

    @MessageField(id = "CurrencyCode", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CurrencyCode;

    @MessageField(id = "CurrencyName", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CurrencyName;

    @MessageField(id = "FirstNewDate", name = "최초신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FirstNewDate;

    @MessageField(id = "FinalTrxDate", name = "최종거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FinalTrxDate;

    @MessageField(id = "ContTerm", name = "계약기간", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer ContTerm;

    @MessageField(id = "FinalExpiryDay", name = "만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FinalExpiryDay;

    @MessageField(id = "LedgerBalance", name = "원장잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LedgerBalance;

    @MessageField(id = "AvailBalance", name = "지불가능잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal AvailBalance;

    @MessageField(id = "AcctState", name = "해약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctState;

    @MessageField(id = "BlankCnt", name = "미기장건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer BlankCnt;

    @MessageField(id = "UnFundAmt", name = "미자금화액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal UnFundAmt;

    @MessageField(id = "TheDayBankCheck", name = "당일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal TheDayBankCheck;

    @MessageField(id = "NextDayBankCheck", name = "익일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal NextDayBankCheck;

    @MessageField(id = "FinalPayTime", name = "최종납일월차", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer FinalPayTime;

    @MessageField(id = "MonthPayAmt", name = "월부금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal MonthPayAmt;

    @MessageField(id = "LeaveCnt", name = "잔존좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal LeaveCnt;

    @MessageField(id = "PayTime", name = "불입회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer PayTime;

    @MessageField(id = "RegistBalance", name = "환매등록금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal RegistBalance;

    @MessageField(id = "SubBalance", name = "입금예약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal SubBalance;

    @MessageField(id = "HungBalance", name = "평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal HungBalance;

}