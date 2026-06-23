package com.scbank.process.api.fw.base.integration.log;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 전문로그 이벤트 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class IntegrationLogEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TRAN_IN = "S";
	
	public static final String TRAN_OUT = "R";
	
	private String systemId;
	
	/**
	 * 거래구분코드(S: 송신, R: 수신)
	 */
	private String trCls;
	
	/**
	 * 전문 ID
	 */
	private String messageId;
	
	/**
	 * 거래코드
	 */
	private String txCd;
	
	/**
	 * 응답코드
	 */
	private String responseCode;
	
	/**
	 * 서버 IP주소
	 */
	private String ipAddress;

	/**
	 * 전문 요청/응답 데이터
	 */
	private Object data;
}
