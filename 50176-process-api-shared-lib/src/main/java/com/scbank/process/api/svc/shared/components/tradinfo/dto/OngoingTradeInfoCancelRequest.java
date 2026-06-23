package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * UI 진행중인 상품 취소 처리
 */
@Data
@IntegrationMessage(id = "OngoingTradeInfoCancelRequest", type = Type.REQUEST)
public class OngoingTradeInfoCancelRequest implements IMessageObject {
	@MessageField(id = "custNo", name = "고객번호", example = "9999")
	private String custNo;
	@MessageField(id = "tradNo", name = "거래번호", example = "9999")
	private String tradNo;
}
