package com.scbank.process.api.svc.common.service.certification.dto.universal;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 범용인증서 발급 수수료 징수 여부 조회
 */
@Data
@IntegrationMessage(id = "CrtUctGetUniCertFeeRequest", type = Type.REQUEST)
public class CrtUctGetUniCertFeeRequest implements IMessageObject {

	@MessageField(id = "safeCardNum", name = "안전카드값")
	private String safeCardNum;

	@MessageField(id = "safeCardNum2", name = "안전카드값2")
	private String safeCardNum2;

	@MessageField(id = "safeCardSeq1", name = "안전카드일련번호 사용자입력값1")
	private String safeCardSeq1;

	@MessageField(id = "safeCardSeq2", name = "안전카드일련번호 사용자입력값2")
	private String safeCardSeq2;

	@MessageField(id = "safeCardSeq3", name = "안전카드일련번호 사용자입력값3")
	private String safeCardSeq3;

	@MessageField(id = "companyName", name = "회사명")
	private String companyName;

	@MessageField(id = "engName", name = "영문명")
	private String engName;

}