package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "CbIbk01H006Req", type = Type.REQUEST, captureSystem = "OLTP", description = "본인확인2 요청 전문")
public class CbIbk01H006Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "CustJumin1", name = "주민등록번호1", length = 6)
	private String CustJumin1;

	@MessageField(id = "CustJumin2", name = "주민등록번호2", length = 7, masking = true, maskingType = "01")
	private String CustJumin2; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "CertTranCode", name = "거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복,6:타기관인증서사용등록)", length = 1)
	private String CertTranCode;

	@MessageField(id = "ChuryGubun", name = "처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)", length = 1)
	private String ChuryGubun;

	@MessageField(id = "CAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증,8:블록체인)", length = 1)
	private String CAGubun;

	@MessageField(id = "CustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
	private String CustGubun;

	@MessageField(id = "RAGubun", name = "인증서종류(RA)(1:금융용,2:범용,7:블록체인,8:사설,9:타기관(RA틀림))", length = 1)
	private String RAGubun;

	@MessageField(id = "IssueBank", name = "인증서발급은행(은행코드)", length = 3)
	private String IssueBank;

	@MessageField(id = "PageGubun", name = "페이지구분(1:한글,2:영문)", length = 1)
	private String PageGubun;

	@MessageField(id = "EngName", name = "영문이름", length = 30)
	private String EngName;

	@MessageField(id = "EmailAddr", name = "이메일", length = 80)
	private String EmailAddr;

	@MessageField(id = "InforGubun", name = "정보구분", length = 1)
	private String InforGubun;

	@MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
	private String SafeCardKind;

	@MessageField(id = "SecurityIndex", name = "안전카드 INDEX", length = 6)
	private String SecurityIndex;

	@MessageField(id = "SecurityValue", name = "안전카드값", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SecurityValue;

	@MessageField(id = "SSRAcctNum", name = "수수료계좌번호", length = 11)
	private String SSRAcctNum;

	@MessageField(id = "AcctPasswd", name = "수수료계좌번호", length = 4, masking = true, maskingType = "03")
	private String AcctPasswd; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "JoinLoad", name = "가입경로", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String JoinLoad;

	@MessageField(id = "TelCode", name = "전화번호", length = 12, masking = true, maskingType = "05")
	private String TelCode;

	@MessageField(id = "IssueDate", name = "인증서유효기간시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String IssueDate;

	@MessageField(id = "ExpireDate", name = "인증서유효기간종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ExpireDate;

	@MessageField(id = "SecurityIndex2", name = "안전카드 INDEX 2", length = 6)
	private String SecurityIndex2;

	@MessageField(id = "SecurityValue2", name = "안전카드값 2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SecurityValue2;

	@MessageField(id = "SafeCardIndex1", name = "안전카드일련번호 위치1 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex1;

	@MessageField(id = "SafeCardValue1", name = "안전카드일련번호 사용자입력값1 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardValue1;

	@MessageField(id = "SafeCardIndex2", name = "안전카드일련번호 위치2 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex2;

	@MessageField(id = "SafeCardValue2", name = "안전카드일련번호 사용자입력값2 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardValue2;

	@MessageField(id = "SafeCardIndex3", name = "안전카드일련번호 위치3 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardIndex3;

	@MessageField(id = "SafeCardValue3", name = "안전카드일련번호 사용자입력값3 (인증서 발급시만 사용)", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SafeCardValue3;

	@MessageField(id = "JuminSaupjaNo", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
	private String JuminSaupjaNo; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "SimplifyGB", name = "인증서간소화체크", length = 1)
	private String SimplifyGB;

	@MessageField(id = "YIAGREE", name = "사전승인동의여부", length = 1)
	private String YIAGREE;

	@MessageField(id = "YIGSGB", name = "인증서간소발급자", length = 1)
	private String YIGSGB;

	@MessageField(id = "YIJMGB", name = "인증서사용자구분", length = 1)
	private String YIJMGB;

}
