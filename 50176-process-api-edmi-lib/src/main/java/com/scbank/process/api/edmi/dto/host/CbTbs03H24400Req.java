package com.scbank.process.api.edmi.dto.host;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H24400Req", type = Type.REQUEST, description = "오픈뱅킹 자동이체 신청 예비거래 요청부")
public class CbTbs03H24400Req implements IMessageObject {

	@MessageField(id = "YIUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YIUSID;

	@MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
	private String YIPASS;

	@MessageField(id = "YIJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
	private String YIJMNO;

	@MessageField(id = "YIICJR", name = "자동이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YIICJR;

	@MessageField(id = "YIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YIGRGB;

	@MessageField(id = "YIJJECD", name = "자동중지응답코드", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YIJJECD;

	@MessageField(id = "YIIGJNO", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YIIGJNO;

	@MessageField(id = "YICGJNO", name = "출금계좌번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YICGJNO;

	@MessageField(id = "YICBKCD", name = "출금은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YICBKCD;

	@MessageField(id = "YICIFNO", name = "CIF번호", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YICIFNO;

	@MessageField(id = "YIICIL", name = "매월자동이체일자", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIICIL;

	@MessageField(id = "YIICGM", name = "이체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YIICGM;

	@MessageField(id = "YIICSTA", name = "자동이체시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIICSTA;

	@MessageField(id = "YIICEND", name = "자동이체종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIICEND;

	@MessageField(id = "YIJUKYO", name = "입금계좌표기내용", length = 20, align = AlignType.LEFT, sosi = true)
	private String YIJUKYO;

	@MessageField(id = "YISGNM", name = "송금인명", length = 24, align = AlignType.LEFT, sosi = true)
	private String YISGNM;

	@MessageField(id = "YISCNM", name = "수취인명", length = 24, align = AlignType.LEFT, sosi = true)
	private String YISCNM;

}
