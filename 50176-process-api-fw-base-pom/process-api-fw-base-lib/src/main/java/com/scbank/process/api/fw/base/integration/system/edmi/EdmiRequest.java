package com.scbank.process.api.fw.base.integration.system.edmi;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiReqHeader;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Builder;
import lombok.Data;

/**
 * 호스트외 전문 EDMI 요청 메시지 Wrapper
 * @param <B>
 */
@Data
@Builder
public class EdmiRequest<B extends IMessageObject> implements IntegrationRequest<EdmiReqHeader, B> {

	/**
	 * 요청헤더메시지부
	 */
	private EdmiReqHeader header;
	
	/**
	 * 요청 바디 메시지부
	 */
	private B requestMessage;
}
