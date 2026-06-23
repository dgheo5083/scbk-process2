package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

@Data
public class FaceRecTokenInfo {
	
	private String tokenDate;
	private String connectLoc;
	private String data;
	private String tokenState;
	private String useYn;
	private String registTime;
}
