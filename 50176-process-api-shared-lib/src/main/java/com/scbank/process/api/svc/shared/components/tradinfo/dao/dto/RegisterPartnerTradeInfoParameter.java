package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 제휴처거래정보 Parameter
 */
@Data
public class RegisterPartnerTradeInfoParameter {
	private String custNo;
	private String tradNo;
	private String cnnctnTradNo;
	private String cnnctnWay;
	private String cnnctnWayDynamic;
	private String cnnctnSignId;
	private String cnnctnLoanLmtAmt;
}
