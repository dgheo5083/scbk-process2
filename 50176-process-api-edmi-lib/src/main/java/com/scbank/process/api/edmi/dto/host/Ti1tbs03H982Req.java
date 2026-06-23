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
@IntegrationMessage(id = "Ti1tbs03H982Req", type = Type.REQUEST, captureSystem = "OLTP", description = "수수료 납부 취소 전문")
public class Ti1tbs03H982Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "UserGubun", name = "이용자구분(1:일반, 2:관리자)", length = 1)
	private String UserGubun;

	@MessageField(id = "JuminNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String JuminNo;

	@MessageField(id = "CAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증)", length = 1)
	private String CAGubun;

	@MessageField(id = "CustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
	private String CustGubun;

	@MessageField(id = "RAGubun", name = "인증서종류(RA)(1:공용,2:범용,6:금융인증,8:사설,9:타기관(RA틀림))", length = 1)
	private String RAGubun;

	@MessageField(id = "IssueDate", name = "인증서유효기간시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String IssueDate;

	@MessageField(id = "CloseDate", name = "인증서해지일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CloseDate;

	@MessageField(id = "ExpireDate", name = "인증서유효기간종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String ExpireDate;

	@MessageField(id = "FeeStatus", name = "수수료납부상태", length = 1)
	private String FeeStatus;

	@MessageField(id = "FeeBack", name = "수수료환불횟수", length = 3)
	private String FeeBack;

	@MessageField(id = "FeeFrom", name = "수수료유효기간시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String FeeFrom;

	@MessageField(id = "FeeTo", name = "수수료유효기간종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String FeeTo;

	@MessageField(id = "FeePay", name = "수수료납부일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String FeePay;

	@MessageField(id = "SuspendDate", name = "인증서효력정지일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SuspendDate;

	@MessageField(id = "RecoverDate", name = "인증서효력회복일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String RecoverDate;

	@MessageField(id = "IssueBank", name = "발급은행", length = 3)
	private String IssueBank;

	@MessageField(id = "FDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String FDate;

	@MessageField(id = "TDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String TDate;

	@MessageField(id = "MDate", name = "납부일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String MDate;

	@MessageField(id = "MSeq", name = "납부번호", length = 5)
	private String MSeq;

	@MessageField(id = "YoguRecordsu", name = "요구명세수", length = 2)
	private String YoguRecordsu;

	@MessageField(id = "Gjno", name = "수수료입금계좌", length = 11)
	private String Gjno;

	@MessageField(id = "AcctPasswd", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
	private String AcctPasswd;

	@MessageField(id = "Jsno", name = "접수번호-휴일반영처리용", length = 5)
	private String Jsno;

	@MessageField(id = "IPGInName", name = "입금인명-휴일반영처리용", length = 20)
	private String IPGInName;

	@MessageField(id = "OBSjum", name = "출금계좌원장점- 휴일반영처리용", length = 3)
	private String OBSjum;

	@MessageField(id = "CMSCode", name = "CMS Code", length = 20)
	private String CMSCode;

	@MessageField(id = "SSR", name = "수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SSR;

	@MessageField(id = "VAT", name = "부가세금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String VAT;

	@MessageField(id = "CancelOP", name = "취소완료조작자", length = 10)
	private String CancelOP;

	@MessageField(id = "CancelYubu", name = "취소실시여부", length = 1)
	private String CancelYubu;

	@MessageField(id = "KeyFDate", name = "납부시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String KeyFDate;

	@MessageField(id = "KeyTDate", name = "납부종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String KeyTDate;

	@MessageField(id = "KeyMSeq", name = "납부번호", length = 5)
	private String KeyMSeq;

	@MessageField(id = "JuminSaupjaNo", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
	private String JuminSaupjaNo;

	@MessageField(id = "Dummy", name = "dummy", length = 151)
	private String Dummy;

	@MessageField(id = "InputJumin", name = "주민번호", length = 13, masking = true, maskingType = "01")
	private String InputJumin;

	@MessageField(id = "ChipNo", name = "칩번호", length = 16)
	private String ChipNo;

	@MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8)
	private String CardIssueDate;

	@MessageField(id = "TeleOne", name = "지역번호", length = 4)
	private String TeleOne;

	@MessageField(id = "TeleTwo", name = "국번", length = 4)
	private String TeleTwo;

	@MessageField(id = "TeleThree", name = "전화번호", length = 4)
	private String TeleThree;

	@MessageField(id = "YIJIL", name = "주문일", length = 8)
	private String YIJIL;

	@MessageField(id = "YIJUMUN", name = "주문번호", length = 10)
	private String YIJUMUN;

	@MessageField(id = "Dummy1", name = "dummy1", length = 33)
	private String Dummy1;

	@MessageField(id = "YIIPN", name = "IP정보", length = 40)
	private String YIIPN;

	@MessageField(id = "YIMAC", name = "MAC정보", length = 20)
	private String YIMAC;

	@MessageField(id = "Dummy2", name = "dummy2", length = 10)
	private String Dummy2;

}