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
@IntegrationMessage(id = "MciIbMts023Req", type = Type.REQUEST, captureSystem = "MCI", description = "기초 자산 상세 내역")
public class MciIbMts023Req implements IMessageObject {
	@MessageField(id = "SIYNYJANO1", name = "운용자산번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SIYNYJANO1;

	@MessageField(id = "MTS_ACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String MTS_ACCTNUM;

}