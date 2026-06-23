package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H12600Res", type = Type.RESPONSE, description = "이체가능금액조회 응답부")
public class CbIbk01H12600Res implements IMessageObject {
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "NomiBalance", name = "이체가능금액(인출가능잔액)", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal NomiBalance;

	@MessageField(id = "AvailBalance", name = "통잔잔액", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal AvailBalance;

	@MessageField(id = "OneLimit", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal OneLimit;

	@MessageField(id = "DayLimit", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal DayLimit;

	@MessageField(id = "TodayLimit", name = "금일사용한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal TodayLimit;

	@MessageField(id = "RestLimit", name = "잔여한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal RestLimit;

	@MessageField(id = "YOPNTJAN", name = "잔고포인트", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOPNTJAN;

	@MessageField(id = "YOJDGJYY", name = "자동화기기결제", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJDGJYY;

	@MessageField(id = "YOCCGJYY", name = "센터처리결제예", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOCCGJYY;

	@MessageField(id = "YOSMSYY", name = "SMS결제예약", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOSMSYY;

	@MessageField(id = "YOLEVEL", name = "마일리지등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOLEVEL;

	@MessageField(id = "YOTHNM", name = "통화명", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOTHNM;

	@MessageField(id = "YOJBGNJ1", name = "이체가능금액외화", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOJBGNJ1;

	@MessageField(id = "YOMKJAN1", name = "통장잔액외화", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOMKJAN1;

}