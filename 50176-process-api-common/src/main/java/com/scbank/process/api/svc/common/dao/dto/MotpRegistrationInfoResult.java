package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class MotpRegistrationInfoResult {

	private String userId;

	private String deviceId;

	private String deviceType;

	private String tokenId;

	private String issueType;

	private String regDeviceUUid;

}