package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterNonFaceScreenInfoRequest", type = Type.REQUEST)
public class RegisterNonFaceScreenInfoRequest implements IMessageObject {
	@MessageField(id = "custNo", name = "고객번호", example = "9999")
	private String custNo;
	@MessageField(id = "tradNo", name = "거래 번호", example = "9999")
	private String tradNo;
	@MessageField(id = "bizType", name = "업무 구분", example = "HELS")
    private String bizType;
	@MessageField(id = "scrnDataInfo", name = "화면정보(JSON Text)", example = "")
    private String scrnDataInfo;
}
