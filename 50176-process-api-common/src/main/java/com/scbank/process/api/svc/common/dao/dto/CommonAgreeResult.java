package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class CommonAgreeResult {
	private String cmmAgreeFileNm; // 공통약관파일명
	private String cmmAgreeNm; // 공통약관명
	private String cmmAgreeCntrctOfferCode; // 공통약관아이디계약서류제공여부
	private String ctgryCmmAgreeFileNm; // 카테고리공통약관파일명
	private String ctgryCmmAgreeNm; // 카테고리공통약관명
	private String ctgryCmmAgreeCntrctOfferCode; // 카테고리공통약관아이디계약서류제공여부
	private String prdctAgreeFileNm; // 상품약관명파일명
	private String prdctAgreeNm; // 상품약관명
	private String prdctAgreeCntrctOfferCode; // 상품약관아이디계약서류제공여부
	private String prdctExplnFileNm; // 상품설명서파일명
	private String prdctExplnNm; // 상품설명서명
	private String prdctExplnCntrctOfferCode; // 상품설명서계약서류제공여부
}