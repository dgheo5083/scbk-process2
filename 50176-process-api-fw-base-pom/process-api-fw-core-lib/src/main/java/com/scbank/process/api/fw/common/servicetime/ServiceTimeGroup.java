package com.scbank.process.api.fw.common.servicetime;

/**
 * 서비스 이용시간 관리 그룹 열거형 상수
 */
public enum ServiceTimeGroup {

	MENU, MESSAGE, UNKNOWN;

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static ServiceTimeGroup of(String type) {
		switch (type.toLowerCase()) {
			case "menu":
				return MENU;
			case "message":
				return MESSAGE;
			default:
				return UNKNOWN;
		}
	}
}
