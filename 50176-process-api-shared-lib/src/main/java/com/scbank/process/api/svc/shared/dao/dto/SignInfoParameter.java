package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class SignInfoParameter {
	
	private String userId;
	
	private String userName;
	
	private String regDate;
	
	private String juminNo;
	
	private String sysIp;
	
	private String sysName;
	
	private String gubunCode;
	
	private String txCode;
	
	private String pageLang;
	
	private String regSite;
	
	@ToString.Exclude
	private byte[] encSign;
	
	private String delFlag;
	
	private String regDay;
}