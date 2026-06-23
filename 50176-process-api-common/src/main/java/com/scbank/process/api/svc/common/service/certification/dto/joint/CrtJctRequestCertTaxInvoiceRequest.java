package com.scbank.process.api.svc.common.service.certification.dto.joint;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;



/**
 * CSL 서비스 요청 정보 클래스
 * 세금계산서 신청
 */
@Data
@IntegrationMessage(id = "CrtJctRequestCertTaxInvoiceRequest", type = Type.REQUEST)
public class CrtJctRequestCertTaxInvoiceRequest implements IMessageObject {

	@MessageField(id = "userId", name = "사용자 아이디")
	private String userId;

	@MessageField(id = "personInCharge", name = "담당자명")
	private String personInCharge;

	@MessageField(id = "issueDate", name = "발행일")
	private String issueDate;

	@MessageField(id = "emailAddr2", name = "이메일")
	private String emailAddr2;

	@MessageField(id = "telCode1", name = "회사 전화1")
	private String telCode1;

	@MessageField(id = "telCode2", name = "회사 전화2")
	private String telCode2;

	@MessageField(id = "telCode3", name = "회사 전화3")
	private String telCode3;

	@MessageField(id = "companyFax1", name = "FAX1")
	private String companyFax1;

	@MessageField(id = "companyFax2", name = "FAX2")
	private String companyFax2;

	@MessageField(id = "companyFax3", name = "FAX3")
	private String companyFax3;

	@MessageField(id = "saupjaNo1", name = "사업자 등록번호1")
	private String saupjaNo1;

	@MessageField(id = "saupjaNo2", name = "사업자 등록번호2")
	private String saupjaNo2;

	@MessageField(id = "saupjaNo3", name = "사업자 등록번호3")
	private String saupjaNo3;

	@MessageField(id = "companyName", name = "회사명")
	private String companyName;

	@MessageField(id = "president", name = "대표자 이름")
	private String president;

	@MessageField(id = "companyLocation", name = "사업자 소재지")
	private String companyLocation;

	@MessageField(id = "typesOfIndustry", name = "업태")
	private String typesOfIndustry;

	@MessageField(id = "item", name = "종목")
	private String item;

	@MessageField(id = "ssrFee", name = "공급가액")
	private String ssrFee;

	@MessageField(id = "ssrVat", name = "세액")
	private String ssrVat;

}