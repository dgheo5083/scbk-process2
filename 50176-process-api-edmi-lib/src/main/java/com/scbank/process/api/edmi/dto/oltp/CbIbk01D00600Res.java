package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01D00600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "본인확인 응답 전문")
public class CbIbk01D00600Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "DeptPersonName", name = "예금주", length = 32, masking = true, maskingType = "04", multiBytes = true)
	private String DeptPersonName;

	@MessageField(id = "ZipCode", name = "우편번호", length = 6)
	private String ZipCode;

	@MessageField(id = "Address", name = "주소", length = 62, multiBytes = true)
	private String Address;

	@MessageField(id = "TelCode", name = "전화번호", length = 12, masking = true, maskingType = "05")
	private String TelCode;

	@MessageField(id = "EmailAddr", name = "이메일", length = 80, masking = true, maskingType = "07")
	private String EmailAddr;

	@MessageField(id = "PersonOrCompanyCode", name = "개인법인 구분코드", length = 1)
	private String PersonOrCompanyCode;

	@MessageField(id = "RegNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String RegNo;

	@MessageField(id = "InforGubun", name = "정보구분", length = 1)
	private String InforGubun;

	@MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
	private String SafeCardKind;

	@MessageField(id = "SecurityIndex", name = "안전카드 INDEX 1", length = 6)
	private String SecurityIndex;

	@MessageField(id = "TransGubun", name = "이체비밀번호등록여부", length = 1)
	private String TransGubun;

	@MessageField(id = "SaupjaNo", name = "개인사업자번호", length = 10, masking = true, maskingType = "01")
	private String SaupjaNo;

	@MessageField(id = "UserStatus", name = "이용자 상태", length = 1)
	private String UserStatus;

	@MessageField(id = "CAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증)", length = 1)
	private String CAGubun;

	@MessageField(id = "CustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
	private String CustGubun;

	@MessageField(id = "RAGubun", name = "인증서종류(RA)(1:금융용,2:범용,6:금융인증서,8:사설,9:타기관(RA틀림))", length = 1)
	private String RAGubun;

	@MessageField(id = "IssueBank", name = "인증서발급은행(은행코드)", length = 3)
	private String IssueBank;

	@MessageField(id = "IssueGubun", name = "발급구분(1:신규,2:재발급)", length = 1)
	private String IssueGubun;

	@MessageField(id = "SSRGubun", name = "수수료징구구분(1:징구,2:미징구)", length = 1)
	private String SSRGubun;

	@MessageField(id = "SSRFee", name = "공인인증발급 수수료", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SSRFee;

	@MessageField(id = "SSRVat", name = "부가가치세", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SSRVat;

	@MessageField(id = "IssueDate", name = "인증서유효기간시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String IssueDate;

	@MessageField(id = "ExpireDate", name = "인증서유효기간종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ExpireDate;

	@MessageField(id = "SecurityIndex2", name = "안전카드 INDEX 2", length = 6)
	private String SecurityIndex2;

	@MessageField(id = "SafeCardIndex1", name = "안전카드일련번호 위치1 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex1;

	@MessageField(id = "SafeCardIndex2", name = "안전카드일련번호 위치2 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex2;

	@MessageField(id = "SafeCardIndex3", name = "안전카드일련번호 위치3 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex3;

	@MessageField(id = "YOHPIN", name = "휴대폰인증사용여부", length = 1)
	private String YOHPIN;

	@MessageField(id = "YOADRGB1", name = "신구주소사용구분(자택)", length = 1)
	private String YOADRGB1;

	@MessageField(id = "YOZIPCD40", name = "신우편번호(자택)", length = 6)
	private String YOZIPCD40;

	@MessageField(id = "YOZIPCD41", name = "신도로명코드(자택)", length = 12)
	private String YOZIPCD41;

	@MessageField(id = "YOZIPCD42", name = "신일련번호(자택)", length = 2)
	private String YOZIPCD42;

	@MessageField(id = "YOZIPCD43", name = "신지하여부(자택)", length = 1)
	private String YOZIPCD43;

	@MessageField(id = "YOZIPCD44", name = "신건물번호(자택)", length = 10)
	private String YOZIPCD44;

	@MessageField(id = "YOSJUSO4", name = "신도로명(자택)", length = 72, multiBytes = true)
	private String YOSJUSO4;

	@MessageField(id = "YOJUSO4", name = "신상세(자택)", length = 102, multiBytes = true)
	private String YOJUSO4;

	@MessageField(id = "YOIDGB", name = "이용등급", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOIDGB;

	@MessageField(id = "AuthSVC4TRAN", name = "추가인증(이체)", length = 1)
	private String AuthSVC4TRAN;

	@MessageField(id = "AuthSVC4CERT", name = "추가인증(공인인증)", length = 1)
	private String AuthSVC4CERT;

	@MessageField(id = "OTPStatus", name = "일회용비밀번호상태", length = 1)
	private String OTPStatus;

	@MessageField(id = "YODRBR", name = "등록점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YODRBR;

	@MessageField(id = "SmartOTP", name = "스마트OTP", length = 1)
	private String SmartOTP;

	@MessageField(id = "YOAGREEGB", name = "인증서발급동의여부", length = 1)
	private String YOAGREEGB;

	@MessageField(id = "YOAGIL", name = "인증서발급동의거부일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOAGIL;

	@MessageField(id = "YOISYN", name = "이체비밀번호 사용여부( 1: 미사용 )", length = 1)
	private String YOISYN;

	@MessageField(id = "YOPAYFROM", name = "수수료유효기간시작일", length = 8, align = AlignType.RIGHT)
	private String YOPAYFROM;

	@MessageField(id = "YOPAYTO", name = "수수료유효기간만료일", length = 8, align = AlignType.RIGHT)
	private String YOPAYTO;

	@MessageField(id = "YOCFJMGB", name = "실명증표구분( 1:주민  2:사업자  3:여권  4:외국인등록 )", length = 1)
	private String YOCFJMGB;

	@MessageField(id = "YODUMY1", name = "더미", length = 3)
	private String YODUMY1;

}
