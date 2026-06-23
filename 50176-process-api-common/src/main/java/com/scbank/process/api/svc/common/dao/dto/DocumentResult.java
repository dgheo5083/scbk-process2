package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class DocumentResult {
	private String elementId; // 공통약관파일명
	private String tranDt; // 공통약관명
	private String loanReqNo; // 공통약관아이디계약서류제공여부
	private String acctNo; // 공통약관파일명
	private String custSsn; // 공통약관명
	private String docCode; // 공통약관아이디계약서류제공여부
	private String docName; // 공통약관파일명
	private String brchNo; // 공통약관명
	private String empNo; // 공통약관아이디계약서류제공여부
	private String chnnlMk; // 공통약관파일명
	private String jobMk; // 공통약관명
	private String createDocDt; // 공통약관아이디계약서류제공여부

}