package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 비대면 화면정보조회 parameter
 */
@Data
public class NfScrnInfoInqiryParam {
	private String custNo;
	private String tradNo;
	private String bizType;
}
