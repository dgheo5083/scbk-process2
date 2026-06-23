package com.scbank.process.api.svc.common.service.identity.dto.mid;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrRequestSpServerProcessResponse", description = "특장점 추출 응답 DTO", type = Type.RESPONSE)
public class IdtMidRequestSpServerProcessResponse implements IMessageObject {
	
	@MessageField(id = "profile", name = "프로필")
	private String profile;
	
	@MessageField(id = "trxCode", name = "거래코드")
	private String trxCode;
	
	@MessageField(id = "successYn", name = "성공여부")
	private String successYn;
	
	@MessageField(id = "errorCode", name = "오류코드")
	private String errorCode;
	
	@MessageField(id = "errorMsg", name = "오류메시지")
	private String errorMsg;
	
	@MessageField(id = "idCardImg", name = "신분증 이미지")
	private String idCardImg;
	
	@MessageField(id = "resultInfo", name = "결과정보")
	private String resultInfo;
	
	@MessageField(id = "resultCode", name = "결과코드")
	private String resultCode;
	
	@MessageField(id = "caList", name = "CA리스트")
	private String caList;
	
}
