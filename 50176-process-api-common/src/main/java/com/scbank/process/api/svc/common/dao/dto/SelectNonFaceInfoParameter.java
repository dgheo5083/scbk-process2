package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SelectNonFaceInfoParameter {
	private String psNo;
	private String passCode;
	private String seqNo;
}
