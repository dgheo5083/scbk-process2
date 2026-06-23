package com.scbank.process.api.svc.shared.components.accesscontrol.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 서비스이용시간
 */
@Data
@Builder
public class ServiceTimeCheckRequest {

	/**
	 * 이용시간 체크 타입 (메뉴/전문)
	 */
	private String type;
	
	/**
	 * 이용시간 체크 대상 코드(메뉴ID/전문ID)
	 */
	private String code;
	
	/**
	 * <pre>
	 * 이용시간 체크 코드
	 * (업무에서 임의로 설정하면, code를 무시 후 해당 필드를 기준으로 이용시간 체크 진행)
	 * </pre>
	 */
	private String forceCheckCode;
}
