package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 진행상태 상세 등록 Parameter
 */
@Data
public class NonFaceTradeInfoDetailParameter {
	private String custNo;
    private String tradNo;
}
