package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class PinUsrMgtParameter {
	private String userBankingId;
	private String deviceId;
	private String targetUserId;
	private String deviceType;
	private String joinFlag;
	private String deviceIdUpdate;
	private String popupStatus;
	private String nickName;
}
