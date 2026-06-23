package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class PinUsrMgtByDeviceResult {
	private String userBankingId; 
	private String deviceId;
	private String deviceType;
	private String joinFlag;
	private String usePinFlag;
	private String deviceIdUpdate; 
	private String popupStatus; 
	private String nickName;
	private String fingerLoginFlag; 
}
