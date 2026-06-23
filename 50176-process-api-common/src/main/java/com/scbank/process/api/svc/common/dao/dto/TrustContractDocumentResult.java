package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class TrustContractDocumentResult {
	private String prdctCd; // 상품코드
	private String prdctNm; // 상품명
	private String prdctExplnDoc1; // 상품설명문서1
	private String prdctExplnDoc2; // 상품설명문서2
	private String prdctExplnDoc3; // 상품설명문서3
}