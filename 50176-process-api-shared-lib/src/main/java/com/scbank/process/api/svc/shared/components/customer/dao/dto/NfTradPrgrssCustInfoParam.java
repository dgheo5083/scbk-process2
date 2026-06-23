package com.scbank.process.api.svc.shared.components.customer.dao.dto;

import lombok.Data;

@Data
public class NfTradPrgrssCustInfoParam {
	private String custNo;
	private String bizType;
	private String tradRegGb;
	private String integratedConselingYn;
}
