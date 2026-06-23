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
@IntegrationMessage(id = "CbIbk01H98000Req", type = Type.REQUEST, captureSystem = "OLTP", description = "타행타기관인증서목록 요청 전문")
public class CbIbk01H98000Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
	private String TSPassword;

	@MessageField(id = "RegNo", name = "주민등록번호", length = 13)
	private String RegNo;

	@MessageField(id = "CertTranCode", name = "거래구분(1:발급,2:폐기,3:갱신,4:효력정지,5:효력회복, 6:타기관인증서사용등록)", length = 1)
	private String CertTranCode;

	@MessageField(id = "ChuryGubun", name = "처리구분(N:일반,S:수수료납부확인,U:인증정보갱신,C:계좌인증)", length = 1)
	private String ChuryGubun;

	@MessageField(id = "InforGubun", name = "정보구분(1:계좌번호,2:안전카드)", length = 1)
	private String InforGubun;

	@MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1)
	private String SafeCardKind;

	@MessageField(id = "SecurityIndex", name = "안전카드 INDEX", length = 6)
	private String SecurityIndex;

	@MessageField(id = "SecurityValue", name = "안전카드값", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SecurityValue; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "SSRAcctNum", name = "기본계좌번호", length = 11)
	private String SSRAcctNum;

	@MessageField(id = "AcctPasswd", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
	private String AcctPasswd; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "IssueDate", name = "인증서유효기간시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String IssueDate;

	@MessageField(id = "ExpireDate", name = "인증서유효기간종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ExpireDate;

	@MessageField(id = "SecurityIndex2", name = "안전카드 INDEX 2", length = 6)
	private String SecurityIndex2;

	@MessageField(id = "SecurityValue2", name = "안전카드값 2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SecurityValue2; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

	@MessageField(id = "MyeongSe", name = "입력명세수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String MyeongSe;

	@MessageField(id = "CAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증)", length = 1)
	private String CAGubun;

	@MessageField(id = "CustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
	private String CustGubun;

	@MessageField(id = "RAGubun", name = "인증서종류(RA)(1:금융용,2:범용,8:사설,9:타기관(RA틀림))", length = 1)
	private String RAGubun;

	@MessageField(id = "IssueBank", name = "인증서발급은행(은행코드)", length = 3)
	private String IssueBank;

	@MessageField(id = "JuminSaupjaNo", name = "주민사업자번호", length = 13)
	private String JuminSaupjaNo;

	@MessageField(id = "YISIMPLE", name = "간소화구분", length = 1)
	private String YISIMPLE;

	@MessageField(id = "YIGSGB", name = "개인구분", length = 1)
	private String YIGSGB;

}
