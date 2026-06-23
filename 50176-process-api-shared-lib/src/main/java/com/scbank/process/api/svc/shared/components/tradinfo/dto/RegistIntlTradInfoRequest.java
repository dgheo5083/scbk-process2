package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import lombok.Data;

@Data
public class RegistIntlTradInfoRequest {
	private String custNo;
	private String tradNo;
	private String codeType;
	private String grpCodeName;
	private String grpCode;
	private String gaesaCode;
}
