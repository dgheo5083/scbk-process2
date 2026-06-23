package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class AcctAuthNumberParameter {
	private String custNo;
	private String tradNo;
	private String bizType;
	private String tradRegGb;
}
