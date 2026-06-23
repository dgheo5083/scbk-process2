package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 오픈뱅킹 API 로그 적재 파라미터 클래스
 */
@Data
@Builder
@ToString
public class OpenBankApiLogInsertLogParameter {

	private String userId;
	private String userCifNo;
	private String tradCls;
	private String bankTradId;
	private String apiTradId;
	private Integer tradSeq;
	private String chnlGb;
	private String apiUrl;
	private String data;
	private String apiDetailCls;
	private String resCode;
	private String bankRspCode;
}
