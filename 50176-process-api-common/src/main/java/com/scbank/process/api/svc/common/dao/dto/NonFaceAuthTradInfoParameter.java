package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class NonFaceAuthTradInfoParameter {
	String idCardCd;
	String callCntrStsCd;
	String prgrssStsCd;
	String seqNo;
	String cnnctnWayDynamic;
	String custNo;
	String tradNo;
}
