package com.scbank.process.api.svc.shared.components.emergency.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 긴급장애정보 클래스
 */
@Data
@Builder
public class EmergencyIncidentInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 장애ID
	 */
	private String id;
	
	/**
	 * 장애타입
	 */
	private String type;
	
	/**
	 * 장애코드
	 */
	private String cd;
	
	/**
	 * 장애 에러코드
	 */
	private String errCd;
	
	/**
	 * 장애 시작일시
	 */
	private String startTm;
	
	/**
	 * 장애 종료일시
	 */
	private String endTm;
	
	/**
	 * 설명
	 */
	private String desc;
}
