package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ComfyBankResult {
	
	private String ssnOrTaxid   ; // 주민/사업자번호
	private String usrNo        ; // 이용자번호
	private String partyNm      ; // 고객명
	private String registDt     ; // 등록일자
	private String registTm     ; // 등록시간
	private String questRply1   ; // 설문답변1
	private String questRply2   ; // 설문답변2
	private String questRply3   ; // 설문답변3
	private String questRply4   ; // 설문답변4
	private String questRply5   ; // 설문답변5
	private String othrInfo1    ; // 기타정보1
	private String othrInfo2    ; // 기타정보2
	private String emailAddr    ; // 이메일주소
	private String cntctPlce    ; // 연락처
}