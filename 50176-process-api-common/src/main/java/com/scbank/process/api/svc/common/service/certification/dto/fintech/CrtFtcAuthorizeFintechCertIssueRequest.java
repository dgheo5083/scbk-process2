package com.scbank.process.api.svc.common.service.certification.dto.fintech;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 핀테크인증서 이용등록
 */
@Data
@IntegrationMessage(id = "CrtFtcAuthorizeFintechCertIssueRequest", type = Type.REQUEST)
public class CrtFtcAuthorizeFintechCertIssueRequest implements IMessageObject {

	@MessageField(id = "safeCardNum", name = "안전카드값")
	private String safeCardNum;

	@MessageField(id = "safeCardNum2", name = "안전카드값2")
	private String safeCardNum2;

	@MessageField(id = "deviceId", name = "디바이스 ID")
	private String deviceId;

	@MessageField(id = "telNo1", name = "전화번호1")
	private String telNo1;

	@MessageField(id = "telNo2", name = "전화번호2")
	private String telNo2;

	@MessageField(id = "telNo3", name = "전화번호3")
	private String telNo3;

}