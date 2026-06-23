package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * 비대면 진행 거래 취소 Parameter
 */
@Data
public class OngoingTradeInfoCancelParameter {
	private String custNo;
    private String tradNo;
}
