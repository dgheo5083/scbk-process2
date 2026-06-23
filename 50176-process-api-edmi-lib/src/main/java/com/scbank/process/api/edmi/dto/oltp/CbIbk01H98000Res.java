package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H98000Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "타행타기관인증서목록 응답 전문")
public class CbIbk01H98000Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "PersonOrCompanyCode", name = "개인법인구분코드", length = 1)
	private String PersonOrCompanyCode;

	@MessageField(id = "RegNo", name = "RegNo", length = 13)
	private String RegNo;

	@MessageField(id = "InforGubun", name = "확인구분", length = 1)
	private String InforGubun;

	@MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
	private String SafeCardKind;

	@MessageField(id = "SecurityIndex", name = "안전카드 INDEX 1", length = 6)
	private String SecurityIndex;

	@MessageField(id = "SafeCardGubun", name = "보안카드사용구분", length = 1)
	private String SafeCardGubun;

	@MessageField(id = "SaupjaNo", name = "개인사업자번호", length = 10)
	private String SaupjaNo;

	@MessageField(id = "UserStatus", name = "이용자 상태", length = 1)
	private String UserStatus;

	@MessageField(id = "SecurityIndex2", name = "안전카드 INDEX 2", length = 6)
	private String SecurityIndex2;

	@MessageField(id = "DeptPersonName", name = "이용자이름", length = 32)
	private String DeptPersonName;

	@MessageField(id = "YOHPIN", name = "휴대폰인증사용여부", length = 1)
	private String YOHPIN;

	@MessageField(id = "RCount", name = "출력건수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private int RCount;

	@MessageField(id = "ARRLIST1", name = "반복부")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H980Res/RCount")
	private List<ARRLIST1> ARRLIST1;

	@Getter
	@Setter
	public static class ARRLIST1 implements IMessageObject {

		@MessageField(id = "CAGubun", name = "발급기관", length = 1)
		private String CAGubun;

		@MessageField(id = "CustGubun", name = "발급자구분", length = 1)
		private String CustGubun;

		@MessageField(id = "RAGubun", name = "인증서종류", length = 1)
		private String RAGubun;

		@MessageField(id = "IssueBank", name = "인증서발급은행", length = 3)
		private String IssueBank;

		@MessageField(id = "IssueEndDate", name = "인증서만료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String IssueEndDate;

	}

}
