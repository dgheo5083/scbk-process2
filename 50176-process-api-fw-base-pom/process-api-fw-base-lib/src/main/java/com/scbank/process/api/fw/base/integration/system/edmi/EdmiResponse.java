package com.scbank.process.api.fw.base.integration.system.edmi;

import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiError;
import com.scbank.process.api.fw.base.integration.system.edmi.vo.EdmiResHeader;
import com.scbank.process.api.fw.integration.response.IntegrationResponse;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Data;

/**
 * 호스트 외 전문 거래 응답 메시지
 * 
 * @param <O>
 * @author sungdon.choi
 */
@Data
public class EdmiResponse<O extends IMessageObject> implements IntegrationResponse<EdmiResHeader, O, EdmiError>  {

	/**
	 * 응답 헤더 메시지부
	 */
	private EdmiResHeader header;
	
	/**
	 * 응답 바디 메시지부
	 */
	private O response;
	
	/**
	 * 응답 에러 메시지부
	 */
	private EdmiError errorResponse;
	
	/**
	 * 응답 에러여부
	 */
	private boolean isError;
}
