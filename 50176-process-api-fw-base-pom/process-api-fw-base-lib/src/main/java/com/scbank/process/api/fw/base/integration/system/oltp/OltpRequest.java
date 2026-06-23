package com.scbank.process.api.fw.base.integration.system.oltp;

import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpReqHeader;
import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Builder;
import lombok.Data;

/**
 * 호스트 전문 요청 메시지 Wrapper
 * 
 * @author sungdon.choi
 * @param <B>
 */
@Data
@Builder
public class OltpRequest<B extends IMessageObject> implements IntegrationRequest<OltpReqHeader, B> {

	/**
	 * 호스트 요청 헤더 메시지부
	 */
	private OltpReqHeader header;

	/**
	 * 호스트 요청 바디 메시지부
	 */
	private B requestMessage;

}
