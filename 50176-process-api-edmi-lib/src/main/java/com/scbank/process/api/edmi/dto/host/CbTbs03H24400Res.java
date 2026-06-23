package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H24400Res", type = Type.REQUEST, description = "오픈뱅킹 자동이체 신청 예비거래 응답부")
public class CbTbs03H24400Res implements IMessageObject {

	@MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOUSID;

}
