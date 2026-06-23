package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H12600Req", type = Type.REQUEST, description = "이체가능금액조회 요청부", captureSystem = "OLTP")
public class CbIbk01H12600Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "AcctNum", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String AcctNum;

}