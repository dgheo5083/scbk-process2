package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행중인상품조회 Result
 * 거래정보 등록 후 거래번호 조회 요도로 사용하고 있음
 * - asis 비대면 화면정보관리 등록/변경(MA3CMMBIZ010_10AS)
 */
@Data
public class OngoingProductInfoInquiryResult {
	/** 최초등록일 */
	private String initRegistDt;
	/** 고객번호 */
    private String custNo;
    /** 거래번호 */
    private String tradNo;
    /** 업무구분  */
    private String bizType;
    /** 상품ID */
    private String prdctId;
    /** 상품코드 */
    private String prdctCd;
    /** 상품명 */
    private String prdctNm;
    /** 콜센터상태코드 */
    private String callCntrStsCd;
    /** 진행상태코드 */
    private String prgrssStsCd;
    /** CDD요청여부 */
    private String cddReqCd;
    /** 신분증 코드 */
    private String idCardCd;
    /** 인증유형 여부 */
    private String authntIndCd;
    /** 인증신청완료일자 */
    private String authntReqCmpltnDt;
    /** 최종변경일 */
    private String lstChngDt;
}
