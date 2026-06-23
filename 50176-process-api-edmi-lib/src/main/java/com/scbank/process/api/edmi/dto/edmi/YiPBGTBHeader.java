package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
public class YiPBGTBHeader implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YIGDATE", name = "전문전송일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGDATE;
	
	@MessageField(id = "YIGTIME", name = "전문전송시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGTIME;
	
	@MessageField(id = "YIGFROM", name = "최초전문전송처", length = 1)
	private String YIGFROM;
	
	@MessageField(id = "YIGHERR", name = "HOST ERROR CODE", length = 4)
	private String YIGHERR;
	
	@MessageField(id = "YIGARSER", name = "ARS ERROR PLAY CODE", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGARSER;
	
	@MessageField(id = "YIGOUTGB", name = "출력구분", length = 1)
	private String YIGOUTGB;
	
	@MessageField(id = "YIGERLOC", name = "입력전문에러위치", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YIGERLOC;
	
	@MessageField(id = "YIGDUMY1", name = "예비", length = 4)
	private String YIGDUMY1;
}
