package com.scbank.process.api.svc.common.dao.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class InfoOtherResult {
	private String serial;			//인증서일련번호
	private String issuer;			//발급기관코드
	private String userid;			//뱅킹계정
	private String idnum;			//실명번호
	private Timestamp expired;		//인증서만료일
	private Timestamp registerd;	//인증서등록일
	private String oid;				//인증서 oid
	private byte[] cert;			//인증서
}
