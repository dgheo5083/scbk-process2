package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaceRecApiTokenEnabledTokenParameter {
	
	private String tokenDate; // 토큰 발급일자
	private String connectLoc; // 접속서버
	private String data; // 접속정보
	
}
