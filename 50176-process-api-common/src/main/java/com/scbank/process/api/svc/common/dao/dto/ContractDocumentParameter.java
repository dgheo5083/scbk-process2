package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractDocumentParameter {
	private String mappingCd1;
	private String mappingCd2;
	private String mappingCd3;
}
