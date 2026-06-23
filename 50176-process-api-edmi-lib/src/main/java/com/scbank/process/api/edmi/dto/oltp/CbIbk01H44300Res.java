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
@IntegrationMessage(id = "CbIbk01H44300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "환매해지 예상조회")
public class CbIbk01H44300Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "CustName", name = "고객명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
	private String CustName;

	@MessageField(id = "CloseAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
	private String CloseAcctNum;

	@MessageField(id = "IncomeGubun", name = "입금방식", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String IncomeGubun;

	@MessageField(id = "TranGubun", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TranGubun;

	@MessageField(id = "CloseAmt", name = "환매금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal CloseAmt;

	@MessageField(id = "CloseAcctSu", name = "환매좌수", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal CloseAcctSu;

	@MessageField(id = "IncomeNameH", name = "소득구분명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String IncomeNameH;

	@MessageField(id = "InterestAmtSign", name = "이자Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String InterestAmtSign;

	@MessageField(id = "InterestAmt", name = "이자", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal InterestAmt;

	@MessageField(id = "TaxProfitSign", name = "과세소득액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TaxProfitSign;

	@MessageField(id = "TaxProfit", name = "과세소득액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TaxProfit;

	@MessageField(id = "IncomeTax", name = "소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal IncomeTax;

	@MessageField(id = "SumTax", name = "세금합계", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SumTax;

	@MessageField(id = "ResidualTax", name = "주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal ResidualTax;

	@MessageField(id = "FarmTax", name = "농특세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal FarmTax;

	@MessageField(id = "EduTax", name = "교육세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal EduTax;

	@MessageField(id = "FeeAmt", name = "환매수수료", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal FeeAmt;

	@MessageField(id = "SumIncomeAmt", name = "차감지급액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SumIncomeAmt;

	@MessageField(id = "FundCode", name = "펀드코드", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String FundCode;

	@MessageField(id = "FundName", name = "펀드명(한글)", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String FundName;

	@MessageField(id = "YOOHCLTAXAK", name = "환출수입제세", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOOHCLTAXAK;

	@MessageField(id = "YOTONM", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOTONM;

	@MessageField(id = "YOGJGIL", name = "기준가적용일", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOGJGIL;

	@MessageField(id = "YOGYSD", name = "기타소득", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOGYSD;

	@MessageField(id = "YOTAXSO", name = "기타소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOTAXSO;

	@MessageField(id = "YOTAXJU", name = "기타주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOTAXJU;

	@MessageField(id = "YOTAXAK1", name = "해지가산소득세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOTAXAK1;

	@MessageField(id = "YOHJGASAN", name = "해지가산주민세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOHJGASAN;

	@MessageField(id = "YOYSPCD", name = "상품코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYSPCD;

	@MessageField(id = "YOFXIJAAK", name = "외화이자금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXIJAAK;

	@MessageField(id = "YOFXGSESOD", name = "외화과세소득액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXGSESOD;

	@MessageField(id = "YOFXHMSSR", name = "외화환매수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXHMSSR;

	@MessageField(id = "YOFXSTAX", name = "외화소득세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXSTAX;

	@MessageField(id = "YOFXJTAX", name = "외화주민세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXJTAX;

	@MessageField(id = "YOFXNTAX", name = "외화농특세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXNTAX;

	@MessageField(id = "YOFXTAXHAP", name = "외화세금합계", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXTAXHAP;

	@MessageField(id = "YOFXCGHJAK", name = "외화차감후지급", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXCGHJAK;

	@MessageField(id = "YOFTTBY", name = "TT매입율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOFTTBY;

	@MessageField(id = "YOFXHCTAX", name = "환출수입제세", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFXHCTAX;

	@MessageField(id = "YOFTGYSD", name = "환산과세대상소", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFTGYSD;

	@MessageField(id = "YOFTAXTO", name = "환산세금합계", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOFTAXTO;

}