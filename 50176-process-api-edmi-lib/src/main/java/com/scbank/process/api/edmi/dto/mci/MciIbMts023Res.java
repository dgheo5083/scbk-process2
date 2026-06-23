package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciIbMts023Res", type = Type.RESPONSE, description = "기초 자산 상세 내역")
public class MciIbMts023Res implements IMessageObject {
	@MessageField(id = "SOYNYJANO1", name = "운용자산번호", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOYNYJANO1;

	@MessageField(id = "SOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer SOJRPGSU;

	@MessageField(id = "SOJHINF", name = "기초자산상세정보")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbMts023Res/SOJRPGSU")
	private List<SOJHINF> SOJHINF;

	@Getter
	@Setter
	public static class SOJHINF implements IMessageObject {

		@MessageField(id = "SOJAJMKNA", name = "기초자산명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOJAJMKNA;

		@MessageField(id = "SOFSTGIJGGK", name = "최초기준가격", length = 18, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOFSTGIJGGK;

		@MessageField(id = "SOJILJISU", name = "전일지수", length = 18, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOJILJISU;

		@MessageField(id = "SOGGKBY", name = "기초자산가격대비율", length = 5, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOGGKBY;

	}

	@MessageField(id = "SOCRRS2", name = "처리결과2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOCRRS2;

	@MessageField(id = "SOERWICH", name = "에러위치", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOERWICH;

	@MessageField(id = "SOERRMSG", name = "ERROR메시지", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOERRMSG;

}