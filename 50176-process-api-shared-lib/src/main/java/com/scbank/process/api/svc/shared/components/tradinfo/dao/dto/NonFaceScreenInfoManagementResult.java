package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 비대면거래관리테이블 Result
 */
@Data
public class NonFaceScreenInfoManagementResult {
	private String initRegistDt;
    private String custNo;
    private String tradNo;
    private String bizType;
    private String prdctId;
    private String prdctCd;
    private String prdctNm;
    private String callCntrStsCd;
    private String prgrssStsCd;
    private String cddReqCd;
    private String idCardCd;
    private String lstChngDt;
    private String authntReqCmpltnDt;
    private String authntIndCd;
    private String authntInd;
}
