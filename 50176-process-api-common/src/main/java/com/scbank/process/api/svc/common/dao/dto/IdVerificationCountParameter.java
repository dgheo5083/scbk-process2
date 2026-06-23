package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class IdVerificationCountParameter {
	private String idCardCnt;
	private String idCardReqDt;
	private String custNo;
	private String tradNo;
}
