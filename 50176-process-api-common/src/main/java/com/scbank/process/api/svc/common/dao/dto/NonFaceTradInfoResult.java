package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class NonFaceTradInfoResult {
	private String prdctId;
	private String prdctNm;
	private String bizType;
	private String brnchNo;
	private String initRegistDt;
}
