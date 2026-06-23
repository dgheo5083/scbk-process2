package com.scbank.process.api.svc.shared.components.cert.dao.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class LdapInfoResult {
	private String dn; // 인증서 dn
	private String serial;
	private String objectclass; // 엔트리 종류
	private String cn; // 인증서 cn
	private String sn; // 인증서 sn
	private String mail; // mail
	private String userid; // user id
	private String timeid;
	private String raflag; // 자행,타행 구분
	private String status;
	private String policy; // 인증정책
	private String cid; // 주민,사업자 번호
	private Timestamp issuedate; // 발급일
	private Timestamp expiredate; // 만기일
	private byte[] usercertificate; // 인증서
	private String issuerCode; // CA기관 구분코드
}