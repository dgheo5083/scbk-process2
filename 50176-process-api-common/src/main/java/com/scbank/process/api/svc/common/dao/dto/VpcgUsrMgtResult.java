package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class VpcgUsrMgtResult {
	private String userId;
	private String connectType;
	private String finCertSeqNum;
	private String deviceId;
	private String joinFlg;
	private String initRegDt;
	private String lstUpdDt;
	private String finCertSmpKeyTkn;
}