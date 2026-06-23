package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행중인 거래정보 조회 Parameter
 */
@Data
public class OngoingTradeInfoInquiryParameter {
	private String custNo;
    private String tradNo;
    private String bizType;
    private String tradRegGb;
    private String integratedConselingYn;
    private String prdctCd;
    private String prdctNm;
    private String callCntrStsCd;
    private String prgrssStsCd;
    private String clerkNo;
    private String cnnctnWay;
    private String reaCd;
}
