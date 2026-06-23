package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H43S00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "청년도약계좌 계좌신규 가능여부 조회")
public class CbIbk01H43S00Res implements IMessageObject {
	
	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT)
	private String UserID;
	
	@MessageField(id = "YOSGGNYB", name = "계좌신규가능여부", length = 10, align = AlignType.LEFT)
	private String YOSGGNYB;
	
	@MessageField(id = "YOSTMSG", name = "상태메세지", length = 52, align = AlignType.LEFT)
	private String YOSTMSG;
	
	@MessageField(id = "YOOKGB", name = "가입가능여부(서금원)", length = 1, align = AlignType.LEFT)
	private String YOOKGB;
	
	@MessageField(id = "YOSTAIL", name = "가입가능 시작일자", length = 8, align = AlignType.RIGHT, padding = "0")
	private String YOSTAIL;
	
	@MessageField(id = "YOENDIL", name = "가입가능 종료일자", length = 8, align = AlignType.RIGHT, padding = "0")
	private String YOENDIL;
}
