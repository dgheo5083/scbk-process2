package com.scbank.process.api.svc.shared.components.accesscontrol.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuAuthorityCheckRequest {

	/**
	 * 메뉴ID
	 */
	private String menuId;
	
	/**
	 * 접근제어 정보
	 */
	private String acType;
	
	/**
	 * 
	 */
	private String forceCheckCode;
}
