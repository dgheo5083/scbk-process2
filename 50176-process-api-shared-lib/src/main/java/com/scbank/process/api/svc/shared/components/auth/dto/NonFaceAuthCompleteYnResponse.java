package com.scbank.process.api.svc.shared.components.auth.dto;

import lombok.Data;

@Data
public class NonFaceAuthCompleteYnResponse {
	private String idCardCd;//신분증코드
	private String authntIndCd;//인증구분코드
	private String cddReqCd;//cdd요청코드
	private String docEvdcCd;//증빙서류확인
	
}
