package com.scbank.process.api.edmi.dto.mci;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@IntegrationMessage(id = "MciIbMts022Res", type = Type.RESPONSE, description = "자산 보유 현황 조회")
public class MciIbMts022Res implements IMessageObject {
	@MessageField(id = "MTS_CgAcctNum", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String MTS_CgAcctNum;

	@MessageField(id = "SOGRJR", name = "거래종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOGRJR;

	@MessageField(id = "SOPRODJR", name = "상품종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOPRODJR;

	@MessageField(id = "SOERCODE", name = "에러코드", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOERCODE;

	@MessageField(id = "SOERRNAM", name = "에러한글명", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SOERRNAM;

	@MessageField(id = "SOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer SOJRPGSU;

	@MessageField(id = "SOJH_REC", name = "조회내역정보")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbMts022Res/SOJRPGSU")
	private List<SOJH_REC> SOJH_REC;

	@Getter
	@Setter
	@ToString
	public static class SOJH_REC implements IMessageObject {

		@MessageField(id = "SOGRIL", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOGRIL;

		@MessageField(id = "SOGRIL1", name = "거래일자1", length = 8, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOGRIL1;

		@MessageField(id = "SOGRGBN1", name = "거래구분명1", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOGRGBN1;

		@MessageField(id = "SOJAJMKNA", name = "자산종목명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOJAJMKNA;

		@MessageField(id = "SOGRGBN", name = "거래구분명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOGRGBN;

		@MessageField(id = "SOIY1BUHO", name = "이율1부호", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
		private String SOIY1BUHO;

		@MessageField(id = "SOFXGRAK", name = "외화거래금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal SOFXGRAK;

		@MessageField(id = "SOFXGRAK1", name = "외화거래금액1", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal SOFXGRAK1;

		@MessageField(id = "SOCEKDNK", name = "체결단가", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
		private BigDecimal SOCEKDNK;

		@MessageField(id = "SOTOCD", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOTOCD;

		@MessageField(id = "SOAECO", name = "누적수익율", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOAECO;

		@MessageField(id = "SOCBRJN", name = "취금점점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOCBRJN;

		@MessageField(id = "SOOPNO1", name = "조작자", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOOPNO1;

		@MessageField(id = "SOPRODTYP3", name = "상품타입3", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String SOPRODTYP3;

	}

	@MessageField(id = "SONEXTGB", name = "연속구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SONEXTGB;

	@MessageField(id = "SONEXTINF", name = "연속정보", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String SONEXTINF;

	@MessageField(id = "SOTBUHO", name = "총운용수익부호", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String SOTBUHO;

	@MessageField(id = "SOUSETAK", name = "사용총금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SOUSETAK;

	@MessageField(id = "SOPLTAK", name = "손익총금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SOPLTAK;

	@MessageField(id = "SOPRFTRAT1", name = "수익율", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO, scale = 3)
	private BigDecimal SOPRFTRAT1;

	@MessageField(id = "SOFXSUTAK", name = "외화수탁금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal SOFXSUTAK;

}