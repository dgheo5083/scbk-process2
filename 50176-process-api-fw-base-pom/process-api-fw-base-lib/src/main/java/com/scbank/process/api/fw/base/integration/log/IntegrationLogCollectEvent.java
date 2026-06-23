package com.scbank.process.api.fw.base.integration.log;

import java.io.Serializable;

import com.scbank.process.api.fw.integration.request.IntegrationRequest;
import com.scbank.process.api.fw.integration.response.IntegrationResponse;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 전문로그 수집처리 이벤트
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class IntegrationLogCollectEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 사용자ID
	 */
	private String userId;
	
	/**
	 * 시스템ID
	 */
	private String systemId;
	
	/**
	 * 채널타입
	 */
	private String channelType;
	
	/**
	 * 전문 송/수신 구분코드
	 */
	private String txDscd;
	
	/**
	 * 요청 메시지
	 */
	private IntegrationRequest<? extends IMessageObject, ? extends IMessageObject> request;
	
	/**
	 * 요청 메시지
	 */
	private IntegrationResponse<? extends IMessageObject, ? extends IMessageObject, ? extends IMessageObject> response;
	
	/**
	 * 거래데이터
	 */
	private String data;
	
	/**
	 * VFDS 디바이스키
	 */
	private String deviceKey;
}
