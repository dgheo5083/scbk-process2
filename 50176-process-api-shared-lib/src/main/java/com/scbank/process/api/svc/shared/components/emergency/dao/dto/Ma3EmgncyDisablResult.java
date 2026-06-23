package com.scbank.process.api.svc.shared.components.emergency.dao.dto;

import lombok.Data;

/**
 * 긴급장애 목록 조회 결과
 */
@Data
public class Ma3EmgncyDisablResult {

	/**
	 * 장애 ID 
	 */
	private String disablId;
	
	/**
	 * 장애 유형 
	 */
	private String disablType;
	
	/**
	 * 장애 코드
	 */
	private String disablCd;
	
	/**
	 * 장애 에러 코드
	 */
	private String disablErrCd;
	
	/**
	 * 장애 시작 일시
	 */
	private String disablStartTm;
	
	/**
	 * 장애 종료 일시
	 */
	private String disablEndTm;
	
	/**
	 * 장애 설명
	 */
	private String disablDesc;
}
