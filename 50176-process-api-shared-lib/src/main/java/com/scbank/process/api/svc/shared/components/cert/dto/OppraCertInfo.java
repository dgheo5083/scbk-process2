package com.scbank.process.api.svc.shared.components.cert.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class OppraCertInfo implements IMessageObject {
	
	@MessageField(id = "tranType", name = "1:HOST, 2:LDAP, 3:INFO")
    private String tranType;
	
	@MessageField(id = "certserial", name = "인증서 일련번호")
	private String certserial;
	
	@MessageField(id = "issueBank", name = "발급은행")
	private String issueBank;
	
	@MessageField(id = "issueBankName", name = "발급은행")
	private String issueBankName;
	
	@MessageField(id = "certPolicyCode", name = "certPolicyCode")
	private String certPolicyCode;
	
	@MessageField(id = "juminSaupjaNo", name = "주민사업자번호")
	private String juminSaupjaNo;
	
	@MessageField(id = "oid", name = "oid")
	private String oid;
	
	@MessageField(id = "raGubun", name = "raGubun")
	private String raGubun;
	
	@MessageField(id = "raName", name = "raName")
	private String raName;
	
	@MessageField(id = "custGubun", name = "custGubun")
	private String custGubun;
	
	@MessageField(id = "issueEndDate", name = "issueEndDate")
	private String issueEndDate;
	
	@MessageField(id = "caGubun", name = "caGubun")
	private String caGubun;
	
	@MessageField(id = "deptPersonName", name = "deptPersonName")
	private String deptPersonName;

}
