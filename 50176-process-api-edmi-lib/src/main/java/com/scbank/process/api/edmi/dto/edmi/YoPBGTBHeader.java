package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
public class YoPBGTBHeader implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YOGDATE", name = "전문전송일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOGDATE;
	
	@MessageField(id = "YOGTIME", name = "전문전송시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOGTIME;
	
	@MessageField(id = "YOGFROM", name = "최초전문전송처", length = 1)
	private String YOGFROM;
	
	@MessageField(id = "YOGHERR", name = "HOST ERROR CODE", length = 4)
	private String YOGHERR;
	
	@MessageField(id = "YOGARSER", name = "ARS ERROR PLAY CODE", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOGARSER;
	
	@MessageField(id = "YOGOUTGB", name = "출력구분", length = 1)
	private String YOGOUTGB;
	
	@MessageField(id = "YOGERLOC", name = "입력전문에러위치", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOGERLOC;
	
	@MessageField(id = "YOGDUMY1", name = "예비", length = 4)
	private String YOGDUMY1;
}
