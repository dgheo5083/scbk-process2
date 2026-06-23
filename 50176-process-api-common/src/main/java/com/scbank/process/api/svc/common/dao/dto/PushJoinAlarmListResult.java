package com.scbank.process.api.svc.common.dao.dto;


import lombok.Data;

@Data
public class PushJoinAlarmListResult {

	private String bsAcctNo;
	private String vsAcctNm;
	private String serNo;
	private String benefitFlag;
	private String financeFlag;
	private String financeVal;
	private String wmloungeFlag;
	private String iotranlistFlag;
	private String notyExrateFlag;
	private String cnfrmNo;
	private String appGb;

}
