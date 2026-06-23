package com.scbank.process.api.edmi.dto.host;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H11402Res", type = Type.RESPONSE, description = "잔액 조회(신탁)")
public class Ti1ibk01H11402Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String AcctNum;

	@MessageField(id = "ItemName", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String ItemName;

	@MessageField(id = "CustName", name = "예금주", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String CustName;

	@MessageField(id = "AdminBranch", name = "관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer AdminBranch;

	@MessageField(id = "AdminBranchName", name = "관리점명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String AdminBranchName;

	@MessageField(id = "AvailBalSign", name = "지불가능잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String AvailBalSign;

	@MessageField(id = "AvailBalance", name = "지불가능잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal AvailBalance;

	@MessageField(id = "RealBalSign", name = "실질잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String RealBalSign;

	@MessageField(id = "RealBalance", name = "실질잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal RealBalance;

	@MessageField(id = "NomiBalSign", name = "명목잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String NomiBalSign;

	@MessageField(id = "NomiBalance", name = "명목잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NomiBalance;

	@MessageField(id = "UnpaidReportAmt", name = "미결제통보액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal UnpaidReportAmt;

	@MessageField(id = "UnFundAmt", name = "미자금화액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal UnFundAmt;

	@MessageField(id = "TheDayBankCheck", name = "당일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TheDayBankCheck;

	@MessageField(id = "TheDayHouse", name = "당일가계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TheDayHouse;

	@MessageField(id = "TheDayOther", name = "당일기타", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TheDayOther;

	@MessageField(id = "NextDayBankCheck", name = "익일자기앞", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NextDayBankCheck;

	@MessageField(id = "NextDayHouse", name = "익일가계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NextDayHouse;

	@MessageField(id = "NextDayOther", name = "익일기타", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NextDayOther;

	@MessageField(id = "LoanLimit", name = "대출한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal LoanLimit;

	@MessageField(id = "PromiseDueDate", name = "약정기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal PromiseDueDate;

	@MessageField(id = "RevoDueDate", name = "회전기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal RevoDueDate;

	@MessageField(id = "NewDate", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NewDate;

	@MessageField(id = "ExpiryDate", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal ExpiryDate;

	@MessageField(id = "ContTerm", name = "계약기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer ContTerm;

	@MessageField(id = "MonthPayAmt", name = "월부금", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal MonthPayAmt;

	@MessageField(id = "PayTime", name = "불입횟수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer PayTime;

	@MessageField(id = "FinalTrxDate", name = "최종거래일자")
	private String FinalTrxDate;

	@MessageField(id = "BlankCnt", name = "미기장건수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer BlankCnt;

	@MessageField(id = "YOWBIL1", name = "적립만료일", length = 8)
	private String YOWBIL1;

	@MessageField(id = "YONJIGB", name = "지급간격", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YONJIGB;

	@MessageField(id = "YONJICHA", name = "지급회차", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YONJICHA;

	@MessageField(id = "YOJIGMK", name = "연금지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOJIGMK;

	@MessageField(id = "YOGUYN", name = "공시여부　", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOGUYN;

	@MessageField(id = "YOSUTAK", name = "납입원금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOSUTAK;

	@MessageField(id = "YOPGAAK", name = "평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOPGAAK;

	@MessageField(id = "YOJIGB", name = "누적연금지급액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOJIGB;

	@MessageField(id = "YONUJSU_SIGN", name = "누적수익률부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YONUJSU_SIGN;

	@MessageField(id = "YONUJSU", name = "누적수익률", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YONUJSU;

	@MessageField(id = "YOTOYM", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOTOYM;

}