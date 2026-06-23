package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H86600Res", type = Type.RESPONSE, description = "보유계좌조회 응답 전문")
public class CbIbk01H86600Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10)
	private String UserID;

	@MessageField(id = "YOIBID", name = "인터넷뱅킹ID", length = 10, masking = true, maskingType = "01")
	private String YOIBID;

	@MessageField(id = "YOIBGB", name = "인터넷뱅킹가입구분", length = 1)
	private String YOIBGB;

	@MessageField(id = "YOTBGB", name = "텔레뱅킹가입구분", length = 1)
	private String YOTBGB;

	@MessageField(id = "YOFBGB", name = "퍼스트비즈가입구분", length = 1)
	private String YOFBGB;

	@MessageField(id = "YOPSOGB", name = "IB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
	private String YOPSOGB;

	@MessageField(id = "YOTSOGB", name = "TB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
	private String YOTSOGB;

	@MessageField(id = "YODSGB", name = "동시사용여부 1:동시 2:개별", length = 1)
	private String YODSGB;

	@MessageField(id = "YOPJANGO", name = "리워드포인트잔고", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOPJANGO;

	@MessageField(id = "YONAME", name = "이용자성명", length = 32, masking = true, maskingType = "04")
	private String YONAME;

	@MessageField(id = "YOHDDD", name = "HD 지역번호", length = 4)
	private String YOHDDD;

	@MessageField(id = "YOHGUK", name = "HD 국번", length = 4)
	private String YOHGUK;

	@MessageField(id = "YOHTEL", name = "HD 전화번호", length = 4, masking = true, maskingType = "03")
	private String YOHTEL;

	@MessageField(id = "YOJMNO", name = "개인：주민등록번호", length = 13, masking = true, maskingType = "01")
	private String YOJMNO;

	@MessageField(id = "YOFATIL", name = "FATCA평가일", length = 8)
	private String YOFATIL;

	@MessageField(id = "YOCIFNO", name = "CIFNO", length = 13)
	private String YOCIFNO;

	@MessageField(id = "YOMKTYN", name = "마케팅동의존재여부", length = 1)
	private String YOMKTYN;

	@MessageField(id = "YOSMSYN", name = "퍼스트알리미", length = 1)
	private String YOSMSYN;

	@MessageField(id = "YOHUMGB", name = "요구불외화휴면계좌YN", length = 1)
	private String YOHUMGB;

	@MessageField(id = "YOPAYDD", name = "급여이체일자", length = 2)
	private String YOPAYDD;

	@MessageField(id = "YOTSUPGB", name = "업체구분", length = 3)
	private String YOTSUPGB;

	@MessageField(id = "YOTSCNOD", name = "보안매체 일련번호", length = 12)
	private String YOTSCNOD;

	@MessageField(id = "YOTSIRCD", name = "발급기관코드", length = 5)
	private String YOTSIRCD;

	@MessageField(id = "YOPSCST", name = "보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:해지：재발급시,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류,9:보안카드미사용,A:이용등록해지)", length = 1)
	private String YOPSCST;

	@MessageField(id = "YOTSCST", name = "텔레보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:오손,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류)", length = 1)
	private String YOTSCST;

	@MessageField(id = "YODUMMY", name = "DUMMY", length = 50)
	private String YODUMMY;

	@MessageField(id = "YOGUNSU", name = "자기계좌정보 배열건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOGUNSU;

	@MessageField(id = "YOMYINF_REC", name = "반복부")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H86600Res/YOGUNSU")
	private List<YOMYINF_REC> YOMYINF_REC;

	@Getter
	@Setter
	public static class YOMYINF_REC implements IMessageObject {
		@MessageField(id = "YOMYGJ", name = "본인계좌", length = 11, masking = true, maskingType = "02")
		private String YOMYGJ;

		@MessageField(id = "YOZONG", name = "계좌종별", length = 2)
		private String YOZONG;

		@MessageField(id = "YOBSJUM", name = "개설점", length = 3)
		private String YOBSJUM;

		@MessageField(id = "YOSIIL", name = "비대면신규승인일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private String YOSIIL;

		@MessageField(id = "YOMKJANSIGN", name = "잔액부호", length = 1)
		private String YOMKJANSIGN;

		@MessageField(id = "YOMKJAN", name = "잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal YOMKJAN;

		@MessageField(id = "YOPAYGB", name = "급여구분(공배:미등록, 1:급여통장, J:당행직원급여)", length = 1)
		private String YOPAYGB;

		@MessageField(id = "YOHDGB", name = "한도제한구분(여신-1:비대면한도제한)", length = 1)
		private String YOHDGB;

		@MessageField(id = "YONHDGB", name = "한도제한구분(메뉴-Y:셀프＆한도NEW)", length = 1)
		private String YONHDGB;

		@MessageField(id = "YOTONM", name = "통화명", length = 3)
		private String YOTONM;

		@MessageField(id = "YOHUMYN", name = "휴면계좌구분YN", length = 1)
		private String YOHUMYN;

		@MessageField(id = "YOGRJJ", name = "거래중지계좌1", length = 1)
		private String YOGRJJ;

		@MessageField(id = "YOTJDC", name = "통장대출계좌여부(Y)", length = 1)
		private String YOTJDC;

		@MessageField(id = "YOHDLMGB", name = "금융거래한도제한여부(Y:대상)", length = 1)
		private String YOHDLMGB;

	}

}
