package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태 테이블 고객번호 & 거래번호 조회 <br>
 * ASIS : NF_TRADINFO_MGT_S_05
 */
@Data
public class TradeInfoInquiryParameter {

	private String custNo;
    private String tradNo;
    
}
