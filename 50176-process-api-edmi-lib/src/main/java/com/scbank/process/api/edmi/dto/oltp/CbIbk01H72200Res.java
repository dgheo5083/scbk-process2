package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbIbk01H72200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드 통장상세조회")
public class CbIbk01H72200Res implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String UserID;

	@MessageField(id = "YOGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
	private String YOGJNO;

	@MessageField(id = "YOONAME", name = "고객명", length = 34, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
	private String YOONAME;

	@MessageField(id = "YOFNDNA", name = "펀드명", length = 34, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOFNDNA;

	@MessageField(id = "YOCURNA", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOCURNA;

	@MessageField(id = "YOSINIL", name = "신규일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOSINIL;

	@MessageField(id = "YOMANIL", name = "만기일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOMANIL;

	@MessageField(id = "YOSUTAKSign", name = "총수탁금액Sign", length = 1, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOSUTAKSign;

	@MessageField(id = "YOSUTAK", name = "총수탁금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOSUTAK;

	@MessageField(id = "YONUJSURTSign", name = "누적수익율Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YONUJSURTSign;

	@MessageField(id = "YONUJSURT", name = "누적수익율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YONUJSURT;

	@MessageField(id = "YOSUTAKSign2", name = "총원장금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOSUTAKSign2;

	@MessageField(id = "YOSUTAK2", name = "총원장금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOSUTAK2;

	@MessageField(id = "YOJJNJASUSign", name = "잔존좌수Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJJNJASUSign;

	@MessageField(id = "YOJJNJASU", name = "잔존좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOJJNJASU;

	@MessageField(id = "YODRGJGSign", name = "당일기준가격Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YODRGJGSign;

	@MessageField(id = "YODRGJG", name = "당일기준가격", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YODRGJG;

	@MessageField(id = "YOPGAAKSign", name = "세전평가액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOPGAAKSign;

	@MessageField(id = "YOPGAAK", name = "세전평가액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOPGAAK;

	@MessageField(id = "YODRGJGSign2", name = "전일기준가격Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YODRGJGSign2;

	@MessageField(id = "YODRGJG2", name = "전일기준가격", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YODRGJG2;

	@MessageField(id = "YOPGAPLSign", name = "평가손익Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOPGAPLSign;

	@MessageField(id = "YOPGAPL", name = "평가손익", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOPGAPL;

	@MessageField(id = "YOJAIL", name = "자동이체일", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJAIL;

	@MessageField(id = "YOJAAMTSign", name = "자동이체금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJAAMTSign;

	@MessageField(id = "YOJAAMT", name = "자동이체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOJAAMT;

	@MessageField(id = "YOLASIL", name = "최종거래일", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOLASIL;

	@MessageField(id = "YOGIGAN", name = "계약기간", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOGIGAN;

	@MessageField(id = "YOWOCHA", name = "최종납입월차", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOWOCHA;

	@MessageField(id = "YOHECHA", name = "불입회차", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOHECHA;

	@MessageField(id = "YODANGJWSign", name = "당일자기앞금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YODANGJWSign;

	@MessageField(id = "YODANGJW", name = "당일자기앞금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YODANGJW;

	@MessageField(id = "YOIKILJWSign", name = "익일자기앞금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOIKILJWSign;

	@MessageField(id = "YOIKILJW", name = "익일자기앞금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOIKILJW;

	@MessageField(id = "YOHYBSGSign", name = "환매등록금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOHYBSGSign;

	@MessageField(id = "YOHYBSG", name = "환매등록금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOHYBSG;

	@MessageField(id = "YOYMBSGSign", name = "입금예약금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYMBSGSign;

	@MessageField(id = "YOYMBSG", name = "입금예약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOYMBSG;

	@MessageField(id = "YOHYJWGSign", name = "환매등록좌수Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOHYJWGSign;

	@MessageField(id = "YOHYJWG", name = "환매등록좌수", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOHYJWG;

	@MessageField(id = "YOMIJGAKSign", name = "미자금화금액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOMIJGAKSign;

	@MessageField(id = "YOMIJGAK", name = "미자금화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private BigDecimal YOMIJGAK;

	@MessageField(id = "YOYGGJNO", name = "환매연결계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
	private String YOYGGJNO;

	@MessageField(id = "YOGJGIL", name = "기준가적용일", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private String YOGJGIL;

	@MessageField(id = "YOJUKGG", name = "적립기간", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJUKGG;

	@MessageField(id = "YOJUKMROIL", name = "적립만료일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJUKMROIL;

	@MessageField(id = "YOGCGG", name = "거치기간", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOGCGG;

	@MessageField(id = "YOGCMROIL", name = "거치만료일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOGCMROIL;

	@MessageField(id = "YOYNGJIGAN", name = "연금지급기간", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYNGJIGAN;

	@MessageField(id = "YOYNGIEDIL", name = "연금지급만료일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYNGIEDIL;

	@MessageField(id = "YOYNGJIGJGI", name = "연금지급주기", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYNGJIGJGI;

	@MessageField(id = "YOYNGJISTIL", name = "최초연금지급일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYNGJISTIL;

	@MessageField(id = "YOYSPCD", name = "최초연금지급일", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOYSPCD;

	@MessageField(id = "YOGJST", name = "이관상태", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOGJST;

	@MessageField(id = "YOJSWMK", name = "펀드설정완료", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOJSWMK;

	@MessageField(id = "YOJFJMK", name = "펀드순자산원본", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOJFJMK;

	@MessageField(id = "YOJJYGB", name = "적용법율", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOJJYGB;

	@MessageField(id = "YOTOBY", name = "총보수율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
	private Integer YOTOBY;

	@MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
	private String YOZONG;

}