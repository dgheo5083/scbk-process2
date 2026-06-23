package com.scbank.process.api.svc.shared.components.customer.dao.dto;

import lombok.Data;

@Data
public class NonFaceCustomerInfoParameter {

	/** 주민번호 */
	private String ssn;
	/** 고객번호 */
    private String custNo;
    /** 휴대폰번호 */
    private String mblphnNo;
    /** 사용자명 */
    private String userNm;
    /** 중복체크키 */
    private String cmpndCheckKey;
    /** 원천신규고객여부 */
    private String srcNewUserFlg;
    /** 신규고객여부 */
    private String newUserFlg;
    /** 원천파기해제여부 */
    private String srcDelObjFlg;
    /** 파기해제여부 */
    private String delObjFlg;
    /** 최조접속일 */
    private String initCnnctnDt;
    /** 최종접속일 */
    private String lstCnnctnDt;
}
