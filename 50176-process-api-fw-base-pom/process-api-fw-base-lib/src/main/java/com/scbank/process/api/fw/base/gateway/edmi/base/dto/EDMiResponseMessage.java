package com.scbank.process.api.fw.base.gateway.edmi.base.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * EDMi 응답 메시지 Wrapper 클래스
 * 
 * @author sungdon.choi
 */
@Data
@Builder
public class EDMiResponseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String trackingId;

	/**
	 * 응답 메시지
	 */
	private Object responseMessage;
}
