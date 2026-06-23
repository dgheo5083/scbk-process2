package com.scbank.process.api.svc.shared.components.tradinfo.dao.dto;

import lombok.Data;

/**
 * CDD전자문서 정보 등록 Parameter
 */
@Data
public class CddElectronicDocumentParameter {
	private String rcvryMk;
    private String edocCd;
    private String tradNo;
    private String custNo;
    private String rcvryData;
}
