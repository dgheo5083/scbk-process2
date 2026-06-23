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
@IntegrationMessage(id = "CbIbk01H44300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "환매해지 예상조회")
public class CbIbk01H44300Req implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "CloseAcctNum", name = "환매해약계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
	private String CloseAcctNum;

	@MessageField(id = "CloseAmt", name = "환매금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal CloseAmt;

	@MessageField(id = "CloseAcctSu", name = "환매좌수", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal CloseAcctSu;

	@MessageField(id = "TranGubun", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String TranGubun;

	@MessageField(id = "InputGubun", name = "입력구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String InputGubun;

	@MessageField(id = "YICFXGUBN", name = "외화구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YICFXGUBN;

	@MessageField(id = "YICFXJWASU", name = "환매좌수(외화펀드)", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YICFXJWASU;

	@MessageField(id = "Dummy", name = "dummy", length = 413, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String Dummy;

	@MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "01")
	private BigDecimal InputJumin;

	@MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal ChipNo;

	@MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer CardIssueDate;

	@MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer TeleOne;

	@MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer TeleTwo;

	@MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "03")
	private Integer TeleThree;

}