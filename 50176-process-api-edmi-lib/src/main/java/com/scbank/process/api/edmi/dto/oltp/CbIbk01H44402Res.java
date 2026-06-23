package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H44402Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드계산서 상세조회")
public class CbIbk01H44402Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "CustName", name = "고객명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
	private String CustName;

	@MessageField(id = "JuminNo", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
	private String JuminNo;

	@MessageField(id = "TranGubun", name = "거래구분(0간략조회,1상세)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TranGubun;

	@MessageField(id = "CloseAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
	private String CloseAcctNum;

	@MessageField(id = "ReferGubun", name = "조회구분(1원가,3지급,4해지,7배당)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String ReferGubun;

	@MessageField(id = "ReferStartDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ReferStartDate;

	@MessageField(id = "ReferEndDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ReferEndDate;

	@MessageField(id = "TranDate", name = "상세조회 거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String TranDate;

	@MessageField(id = "Index", name = "상세조회 회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String Index;

	@MessageField(id = "FundCode", name = "펀드코드", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String FundCode;

	@MessageField(id = "FundName", name = "펀드명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String FundName;

	@MessageField(id = "InterestAmtSign", name = "이자합계액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String InterestAmtSign;

	@MessageField(id = "InterestAmt", name = "이자합계액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal InterestAmt;

	@MessageField(id = "TaxAmt", name = "세금합계액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TaxAmt;

	@MessageField(id = "TranAmtSign", name = "거래금액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TranAmtSign;

	@MessageField(id = "TranAmt", name = "거래금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TranAmt;

	@MessageField(id = "TaxProfitSign", name = "과세대상금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TaxProfitSign;

	@MessageField(id = "TaxProfit", name = "과세대상금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TaxProfit;

	@MessageField(id = "FeeAmt", name = "환매수수료", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal FeeAmt;

	@MessageField(id = "SumAmtSign", name = "차감지급액부호", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SumAmtSign;

	@MessageField(id = "SumAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SumAmt;

	@MessageField(id = "RCount", name = "명세건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer RCount;

	@MessageField(id = "REC_01", name = "간략조회명세")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H44402Res/RCount")
	private List<REC_01> REC_01;

	@Data
	public static class REC_01 implements IMessageObject {

		@MessageField(id = "TaxDetailGubunH", name = "과세구분", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String TaxDetailGubunH;

		@MessageField(id = "TaxDetailBGubunH", name = "소득구분", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String TaxDetailBGubunH;

		@MessageField(id = "TaxDetailInterestSign", name = "이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String TaxDetailInterestSign;

		@MessageField(id = "TaxDetailInterest", name = "이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal TaxDetailInterest;

		@MessageField(id = "TaxDetailProfitSign", name = "과표소득액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String TaxDetailProfitSign;

		@MessageField(id = "TaxDetailProfit", name = "과표소득액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal TaxDetailProfit;

		@MessageField(id = "TaxDetailRate", name = "세율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private Integer TaxDetailRate;

		@MessageField(id = "TaxDetailIncome", name = "소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal TaxDetailIncome;

		@MessageField(id = "TaxDetailResidual", name = "주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal TaxDetailResidual;

		@MessageField(id = "TaxDetailFarm", name = "농특세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal TaxDetailFarm;

		@MessageField(id = "TaxDetailFDate", name = "발생초일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private Integer TaxDetailFDate;

		@MessageField(id = "TaxDetailEDate", name = "발생말일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private Integer TaxDetailEDate;

	}
}