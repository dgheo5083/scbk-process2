package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class RemitInfoParameter {
	private String custNo;
	private String tradNo;
	private String cntrprtyAcctNo;
	private String cntrprtyCd;
	private String cntrprtyAuthntCd;
	private String cntrprtyRemitReqCnt;
}
