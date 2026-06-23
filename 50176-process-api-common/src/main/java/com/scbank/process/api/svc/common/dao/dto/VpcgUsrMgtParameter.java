package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class VpcgUsrMgtParameter {
	private String userId;
	private String connectType;
	private String finCertSeqNum;
	private String deviceId;
}