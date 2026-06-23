package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class CnnctnTradInfoParameter {
	private String tradNo;
	private String cnnctnTradNo;
	private String cnnctnWayDynamic;
	private String cnnctnSignId;
	private String cnnctnWay;
	private String custNo;
	private String cnnctnLoanLmtAmt;
}
