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
@IntegrationMessage(id = "CbTbs03H96200Req", type = Type.REQUEST, description = "입금계좌유효성검증 요청부")
public class CbTbs03H96200Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "IgBankCode", name = "은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String IgBankCode;

	@MessageField(id = "IgAcctNum", name = "계좌번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String IgAcctNum;

	@MessageField(id = "Gubun", name = "구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String Gubun;

}