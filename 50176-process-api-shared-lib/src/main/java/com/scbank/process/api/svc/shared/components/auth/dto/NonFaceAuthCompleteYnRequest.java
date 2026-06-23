package com.scbank.process.api.svc.shared.components.auth.dto;

import lombok.Data;

@Data
public class NonFaceAuthCompleteYnRequest {
	private String custNo;
	private String tradNo;
}
