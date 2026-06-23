package com.scbank.process.api.fw.base.integration.system.oltp;

import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpError;
import com.scbank.process.api.fw.base.integration.system.oltp.vo.OltpResHeader;
import com.scbank.process.api.fw.integration.response.IntegrationResponse;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Data;

/**
 * 호스트 응답 메시지
 * 
 * @param <O>
 * @author sungdon.choi
 */
@Data
public class OltpResponse<O extends IMessageObject> implements IntegrationResponse<OltpResHeader, O, OltpError> {

	/**
	 * 호스트 응답 헤더 메시지부
	 */
	private OltpResHeader header;

	/**
	 * 호스트 응답 바디 메시지부
	 */
	private O response;

	/**
	 * 호스트 응답 에러 메시지
	 */
	private OltpError errorResponse;

	/**
	 * 호스트 응답 에러여부
	 */
	private boolean isError;
}
