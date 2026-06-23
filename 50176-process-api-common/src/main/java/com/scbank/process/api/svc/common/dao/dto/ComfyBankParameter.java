package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class ComfyBankParameter {
		
	private String userId;
	private String orgSeniorYn;
	private String chnSeniorYn; 
	private String othr1Chk; 
}