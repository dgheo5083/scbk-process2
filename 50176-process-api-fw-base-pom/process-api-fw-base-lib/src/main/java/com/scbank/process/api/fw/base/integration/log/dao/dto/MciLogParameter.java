package com.scbank.process.api.fw.base.integration.log.dao.dto;

import lombok.Builder;
import lombok.Data;

/**
 * MCI/EDMI 전문 로그 적재 파라미터 클래스
 */
@Data
@Builder
public class MciLogParameter {

	/**
	 * 거래구분코드(S: 송신, R: 수신)
	 */
	private String trCls;
	
	/**
	 * 거래코드
	 */
	private String txCd;
	
	/**
	 * 응답코드
	 */
	private String errCd;
	
	/**
	 * 서버 IP주소
	 */
	private String ipAddress;

	/**
	 * 전문 요청/응답 데이터
	 */
	private Object data;
	
	/**
	 * 채널구분
	 */
	private String chnlGb;
	
	/**
	 * 사용자ID
	 */
	private String userId;
	
	/**
	 * 고객번호
	 */
	private String custInfo;
	
	/**
	 * 테이블명 (NF_MCI_LOG_yyyyMMdd)
	 */
	private String dynamic;
	
}
