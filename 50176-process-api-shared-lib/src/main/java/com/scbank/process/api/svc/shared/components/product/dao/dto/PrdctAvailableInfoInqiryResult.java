package com.scbank.process.api.svc.shared.components.product.dao.dto;

import lombok.Data;

@Data
public class PrdctAvailableInfoInqiryResult {
	private String prdctCd;
	private String seqNo;
	private String useDateFlg;
	private String useTimeFlg;
	private String useTimeStartHh;
	private String useTimeStartMm;
	private String useTimeStart;
	private String useTimeEndHh;
	private String useTimeEndMm;
	private String useTimeEnd;
	private String useAgeFlg;
	private String useAbroadFlg;
	private String useEtc1;
	private String useEtc2;
	private String useEtc3;
}
