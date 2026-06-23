package com.scbank.process.api.svc.common.service.certification.dto.joint;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 공동인증서 폐기 계좌 검증
 */
@Data
@IntegrationMessage(id = "CrtJctValidateCertRevokeAcctResponse", type = Type.RESPONSE)
public class CrtJctValidateCertRevokeAcctResponse implements IMessageObject {

	@MessageField(id = "certList", name = "인증서목록")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<CertListRecord> certList;

	@Data
    public static class CertListRecord implements IMessageObject {
		@MessageField(id = "dn", name = "인증서 dn")
        private String dn;

		@MessageField(id = "serial", name = "serial")
    	private String serial;

		@MessageField(id = "objectclass", name = "엔트리 종류")
    	private String objectclass; 	// 엔트리 종류

		@MessageField(id = "cn", name = "인증서 cn")
    	private String cn; 				// 인증서 cn

		@MessageField(id = "sn", name = "인증서 sn")
    	private String sn; 				// 인증서 sn

		@MessageField(id = "mail", name = "mail")
    	private String mail; 			// mail

		@MessageField(id = "userid", name = "userid")
    	private String userid; 			// userid

		@MessageField(id = "timeid", name = "timeid")
    	private String timeid;

		@MessageField(id = "raflag", name = "자행,타행 구분")
		private String raflag; 			// 자행,타행 구분

		@MessageField(id = "status", name = "status")
    	private String status;

		@MessageField(id = "policy", name = "인증정책")
    	private String policy; 			// 인증정책

		@MessageField(id = "cid", name = "주민,사업자 번호")
    	private String cid; 			// 주민,사업자 번호

		@MessageField(id = "issuedate", name = "발급일")
		private String issuedate; 	// 발급일

		@MessageField(id = "expiredate", name = "만기일")
		private String expiredate; 	// 만기일

		@MessageField(id = "usercertificate", name = "인증서")
    	private byte[] usercertificate; // 인증서

		@MessageField(id = "issuerCode", name = "CA기관 구분코드")
    	private String issuerCode; 		// CA기관 구분코드

		@MessageField(id = "bankName", name = "bankName")
		private String bankName;

		@MessageField(id = "issueEndDate", name = "issueEndDate")
		private String issueEndDate;

		@MessageField(id = "custname", name = "custname")
		private String custname;
	}
}