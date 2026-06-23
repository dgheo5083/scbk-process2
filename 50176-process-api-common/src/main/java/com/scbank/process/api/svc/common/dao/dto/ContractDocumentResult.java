package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ContractDocumentResult {
	private String cmmAgreeFileNm; // 공통약관파일명
	private String cmmAgreeNm; // 공통약관명
	private String cmmAgreeCntrctOfferCode; // 공통약관아이디 계약서류제공코드 1:홈페이지,2:계약서류,3:홈+계약서류
	private String ctgryCmmAgreeFileNm; // 카테고리공통약관파일명
	private String ctgryCmmAgreeNm; // 카테고리공통약관명
	private String ctgryCmmAgreeCntrctOfferCode; // 카테고리공통약관아이디계약서류제공여부 카테고리공통약관아이디계약서류제공코드 1:홈페이지,2:계약서류,3:홈+계약서류
	private String prdctAgreeFileNm; // 상품약관명파일명
	private String prdctAgreeNm; // 상품약관명
	private String prdctAgreeCntrctOfferCode; // 상품약관아이디계약서류제공여부 1:홈페이지,2:계약서류,3:홈+계약서류
	private String prdctExplnFileNm; // 상품설명서파일명
	private String prdctExplnNm; // 상품설명서명
	private String prdctExplnCntrctOfferCode; // 상품설명서계약서류제공여부 1:홈페이지,2:계약서류,3:홈+계약서류
	private String prdctId; // 상품명
	private String mappingCd1; //
	private String mappingCd2; //
	private String mappingCd3; //
}