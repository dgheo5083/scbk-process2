package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Data;

@Data
public class NfAuthCompleteYnResult {
	private String idCardCd;
	private String authntIndCd;
	private String cddReqCd;
	private String docEvdcCd;
}
