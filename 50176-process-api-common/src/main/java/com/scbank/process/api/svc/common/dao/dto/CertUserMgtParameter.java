package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class CertUserMgtParameter {
	private String userId;
	private String deviceId;
	private String connectType;
	private String joinFlg;
	private String finCertSeqNum;
	private String finCertSmpKeyTkn;
}
