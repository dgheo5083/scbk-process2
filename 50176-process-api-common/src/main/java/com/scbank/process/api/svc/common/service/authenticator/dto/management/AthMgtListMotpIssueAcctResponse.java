package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 모바일OTP 발급 출금계좌목록 조회
 */
@Data
@IntegrationMessage(id = "AthMgtListMotpIssueAcctResponse", type = Type.RESPONSE)
public class AthMgtListMotpIssueAcctResponse implements IMessageObject {
	
	@MessageField(id = "userId", name = "userId")
	private String userId;
	
	@MessageField(id = "accountList", name = "계좌번호목록")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<myinfRec> accountList;
	
	@Data
    public static class myinfRec implements IMessageObject {
		@MessageField(id = "mygj", name = "mygj")
        private String mygj;
		
		@MessageField(id = "mygjFmt", name = "mygjFmt")
		private String mygjFmt;
		
		@MessageField(id = "mygjNm", name = "mygjNm")
		private String mygjNm;
	}
	
}