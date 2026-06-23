package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

@Data
public class NonFaceBasicTradeInfoResult {
	/** 고객번호 */
	private String custNo;
	/** 거래번호 */
    private String tradNo;
    /** 업무구분 */
    private String bizType;
    /** 상품ID */
    private String prdctId;
    /** 상품코드 */
    private String prdctCd;
    /** 상품명 */
    private String prdctNm;
}
