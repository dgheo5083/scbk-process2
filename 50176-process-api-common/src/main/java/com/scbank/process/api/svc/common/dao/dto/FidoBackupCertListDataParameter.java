package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class FidoBackupCertListDataParameter {
	private String trId;
	private String histId;
	private String authDt;
	private String userId;
	private String teleType;
	private String hpNum;
	private String resltCd;
}
