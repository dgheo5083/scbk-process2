package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonAgreeParameter {
	private String pubNtcPrdctTypeCd;
	private String deviceId;
	private String connectType;
	private String joinFlg;
	private String finCertSeqNum;
	private String finCertSmpKeyTkn;
}
