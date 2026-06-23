package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class SmtPhoneBankingInfoResult {
	private String type; // 모바일뱅킹종류
	private String vsn; // 모바일뱅킹버전
	private String regDt; // 모바일뱅킹등록날짜
	private String regTm; // 모바일뱅킹등록시간
}