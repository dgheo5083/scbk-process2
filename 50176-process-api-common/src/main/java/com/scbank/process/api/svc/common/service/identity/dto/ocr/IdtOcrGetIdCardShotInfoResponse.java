package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetIdCardShotInfoResponse", description = "신분증촬영 정보 조회 응답 DTO", type = Type.RESPONSE)
public class IdtOcrGetIdCardShotInfoResponse implements IMessageObject {
	
	@MessageField(id = "viewIdTypeJsonString", name = "신분증촬영목록 JSONString")
	private String viewIdTypeJsonString;
	
	@MessageField(id = "alcheraClientLicenseKey", name = "알체라클라이언트 라이센스키")
	private String alcheraClientLicenseKey;
	
	@MessageField(id = "faceTargetYN", name = "안면인식 대상여부")
	private String faceTargetYN;
	
	@MessageField(id = "isCddOk", name = "CDD정상여부")
	private String isCddOk;
	
	@MessageField(id = "cddIlFlag", name = "CDD도래대상여부")
	private String cddIlFlag;
	
	@MessageField(id = "kcddPollingYn", name = "KCDD 자동화여부")
	private String kcddPollingYn;
	
	@MessageField(id = "tradNo", name = "거래번호")
	private String tradNo;
	
	@MessageField(id = "custNo", name = "고객번호")
	private String custNo;
	
	@MessageField(id = "bizType", name = "업무구분")
	private String bizType;
	
	@MessageField(id = "dataModifyYn", name = "신분증정보 수정 허용여부")
	private String dataModifyYn;
	
	@MessageField(id = "isOldApp", name = "최신앱 버전 여부")
	private String isOldApp;
	
}
