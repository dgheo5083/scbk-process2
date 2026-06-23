package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태 History 등록 Parameter
 */
@Data
public class NonFaceTradeInfoHistoryParameter {
	private String custNo;
    private String tradNo;
}