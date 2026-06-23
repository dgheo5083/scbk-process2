package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class NonFaceAuthInfoResult {
	private String idCardCd;
	private String authntIndCd;
	private String cddReqCd;
}
