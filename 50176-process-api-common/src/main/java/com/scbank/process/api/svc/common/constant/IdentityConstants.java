package com.scbank.process.api.svc.common.constant;

public class IdentityConstants {
	// 신분증 종류
	public static enum IdType {
		JUMIN,				// 주민등록증
		DRIVER,				// 운전면허증
		VETERAN,    		// 국가보훈등록증 
		PASSPORT,			// 여권
		ALIEN, 				// 외국인등록증
		DISABILITY,			// 장애인신분증
	}
	
	// 신분증 타입 (실물/모바일)
	public static enum Medium {
		PHYSICAL,			// 실물 신분증
		MOBILE,				// 모바일 신분증
	}
}
