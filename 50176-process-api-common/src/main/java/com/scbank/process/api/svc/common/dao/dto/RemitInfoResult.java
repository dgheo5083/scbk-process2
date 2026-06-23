package com.scbank.process.api.svc.common.dao.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RemitInfoResult {
	private String custNo;
	private String tradNo;
	private String remitDt;
	private String cntrprtyAcctNo;
	private String cntrprtyCd;
	private String cntrprtyAuthntCd;
	private String cntrprtyAuthntFailCnt;
	private Timestamp lstChngDt;
	private String cntrprtyRemitReqCnt;
}
