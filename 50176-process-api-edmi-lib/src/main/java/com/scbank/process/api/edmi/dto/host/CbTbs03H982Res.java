package com.scbank.process.api.edmi.dto.host;

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
@IntegrationMessage(id = "CbTbs03H982Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "수수료 납부 취소 응답 전문")
public class CbTbs03H982Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "JuminNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
	private String JuminNo;

	@MessageField(id = "DeptPersonName", name = "예금주", length = 32, masking = true, maskingType = "04", multiBytes = true)
	private String DeptPersonName;

	@MessageField(id = "FDate", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String FDate;

	@MessageField(id = "TDate", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String TDate;

	@MessageField(id = "CancelDate", name = "취소완료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CancelDate;

	@MessageField(id = "CancelTime", name = "취소완료시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CancelTime;

	@MessageField(id = "CancelSSR", name = "취소수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CancelSSR;

	@MessageField(id = "CancelVAT", name = "취소부가세금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String CancelVAT;

	@MessageField(id = "CancelGjno", name = "취소수수료입금계좌", length = 11)
	private String CancelGjno;

	@MessageField(id = "CMSCode", name = "CMS Code", length = 20)
	private String CMSCode;

	@MessageField(id = "CancelOper", name = "취소완료조작자", length = 10)
	private String CancelOper;

	@MessageField(id = "CancelResult", name = "취소완료결과", length = 1)
	private String CancelResult;

	@MessageField(id = "KeyFDate", name = "납부시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String KeyFDate;

	@MessageField(id = "KeyTDate", name = "납부종료일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String KeyTDate;

	@MessageField(id = "KeyMSeq", name = "납부번호", length = 5)
	private String KeyMSeq;

	@MessageField(id = "RCount", name = "명세수", length = 2)
	private Integer RCount;

	@MessageField(id = "RCount_REC", name = "반복부")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03H982Res/RCount")
	private List<RCount_REC> RCount_REC;

	@Getter
	@Setter
	public static class RCount_REC implements IMessageObject {

		@MessageField(id = "ChargeDate", name = "납부일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String ChargeDate;

		@MessageField(id = "ChargeMSeq", name = "접수번호", length = 5)
		private String ChargeMSeq;

		@MessageField(id = "ChargeGjno", name = "수수료납부계좌", length = 11)
		private String ChargeGjno;

		@MessageField(id = "ChargeFee", name = "수수료금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String ChargeFee;

		@MessageField(id = "ChargeVat", name = "부가세금액", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String ChargeVat;

		@MessageField(id = "ChargeCDate", name = "취소일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String ChargeCDate;

		@MessageField(id = "ChargeCTime", name = "취소시각", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String ChargeCTime;

		@MessageField(id = "ChargeCOP", name = "취소조작자", length = 10)
		private String ChargeCOP;

		@MessageField(id = "ChargeCYubu", name = "취소가능여부(1:취소가능,2:취소불가)", length = 1)
		private String ChargeCYubu;

		@MessageField(id = "ChargeSec", name = "취소번호", length = 5)
		private String ChargeSec;

		@MessageField(id = "ChargeCAGubun", name = "발급기관(CA)(1:KFB(사설),2:금결원,3:한국정보인증)", length = 1)
		private String ChargeCAGubun;

		@MessageField(id = "ChargeCustGubun", name = "발급자구분(1:개인,2:기업)", length = 1)
		private String ChargeCustGubun;

		@MessageField(id = "ChargeRAGubun", name = "인증서종류(RA)(1:공용,2:범용,6:금융인증,8:사설,9:타기관(RA틀림))", length = 1)
		private String ChargeRAGubun;

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

		@MessageField(id = "JuminSaupjaNo", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
		private String JuminSaupjaNo;

		@MessageField(id = "ADummy", name = "dummy", length = 7)
		private String ADummy;

	}

}