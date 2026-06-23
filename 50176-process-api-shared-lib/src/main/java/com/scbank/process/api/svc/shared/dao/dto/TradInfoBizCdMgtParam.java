package com.scbank.process.api.svc.shared.dao.dto;

import java.util.List;

import lombok.Data;

@Data
public class TradInfoBizCdMgtParam {
	private String custNo;
	private String tradNo;
	private String codeType;
	private String grpCode;
	private String grpCodeName;
	private List<gaesaCode> gaesaCodeList;
	
	@Data
	public static class gaesaCode {
		private String code;
		private String codeNm;
		private String cnt;
	}
}
