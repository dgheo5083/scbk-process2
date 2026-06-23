package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 비대면 화면정보 및 스크래핑 정보 조회 Result
 */
@Data
public class NonFaceScreenAndScrapingInfoResult {
	private String custNo;
    private String tradNo;
    private String bizType;
    private String scrnDataInfo;
    private String scrppngData;
}
