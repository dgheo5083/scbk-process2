package com.scbank.process.api.edmi.dto.host;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03H201Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "보안매체 검증처리 응답부")
public class CbTbs03H201Res implements IMessageObject {
	@MessageField(id = "UserID", name = "", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "GrNumSyYN", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String GrNumSyYN;

	@MessageField(id = "GrNumErrCnt", name = "", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String GrNumErrCnt;

	@MessageField(id = "SafeCardKind", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SafeCardKind;

	@MessageField(id = "SafeCardINDEX", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SafeCardINDEX;

	@MessageField(id = "REC_01", name = "")
	@RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "40")
	private List<REC_01> REC_01;

	@MessageField(id = "FileGB", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String FileGB;

	@MessageField(id = "SafeCardINDEX2", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SafeCardINDEX2;

	@Getter
	@Setter
	public static class REC_01 implements IMessageObject {
		@MessageField(id = "BankCode", name = "", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String BankCode;

		@MessageField(id = "ErrorState", name = "", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String ErrorState;

		@MessageField(id = "BankName", name = "", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String BankName;

	}
}