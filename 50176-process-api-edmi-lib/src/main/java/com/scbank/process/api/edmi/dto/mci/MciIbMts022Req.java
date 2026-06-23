package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbMts022Req", type = Type.REQUEST, captureSystem = "MCI", description = "자산 보유 현황 조회")
public class MciIbMts022Req implements IMessageObject {
	@MessageField(id = "MTS_ACCTNUM", name = "계좌번호", length = 11)
	private String MTS_ACCTNUM;

	@MessageField(id = "SIGRJR", name = "거래종류", length = 1)
	private String SIGRJR;

	@MessageField(id = "SIGRHCH", name = "거래회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SIGRHCH;

	@MessageField(id = "SIPRODJR", name = "상품종류", length = 1)
	private String SIPRODJR;

	@MessageField(id = "SIJHSTAIL", name = "조회시작일자", length = 8, align = AlignType.LEFT, padding = StringUtils.ZERO)
	private String SIJHSTAIL;

	@MessageField(id = "SIJHENDIL", name = "조회종료일자", length = 8, align = AlignType.LEFT, padding = StringUtils.ZERO)
	private String SIJHENDIL;

	@MessageField(id = "SINEXTINF", name = "연속정보", length = 50)
	private String SINEXTINF;

	@MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
	private String FLDdelimiter1;

	@MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
	private String SEGdelimiter1;

	@MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
	private String ENDdelimiter1;

	@MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
	private String ENDdelimiter2;

}