package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * UI 진행상태 업데이트 처리
 */
@Data
@IntegrationMessage(id = "NfScrnInfoInqiryResponse", type = Type.RESPONSE)
public class NfScrnInfoInqiryResponse {
	@MessageField(id = "custNo", name = "고객번호")
	private String custNo;
	
	@MessageField(id = "tradNo", name = "거래번호")
	private String tradNo;
	
	@MessageField(id = "bizType", name = "업무구분")
	private String bizType;
	
	@MessageField(id = "scrnDataInfo", name = "화면데이터정보")
	private String scrnDataInfo;
	
	@MessageField(id = "scrppngData", name = "스크래핑데이터")
	private String scrppngData;
}
