package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * UI 진행상태 업데이트 처리
 */
@Data
@IntegrationMessage(id = "OngoingTradeInfoCancelResponse", type = Type.RESPONSE)
public class OngoingTradeInfoCancelResponse implements IMessageObject {
	
	@MessageField(id = "successYn", name = "")
    private String successYn;

}
