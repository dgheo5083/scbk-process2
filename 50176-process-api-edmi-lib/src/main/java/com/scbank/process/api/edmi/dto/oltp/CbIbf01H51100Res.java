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
@IntegrationMessage(id = "CbIbf01H51100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "인터넷 환전 응답")
public class CbIbf01H51100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOJSNO", name = "접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJSNO;

    @MessageField(id = "YOREFNO", name = "환전취결번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOREFNO;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRIL;

    @MessageField(id = "YONAME", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOSLIL", name = "실물수령희망일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSLIL;

    @MessageField(id = "YOYGNO", name = "여권번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYGNO;

    @MessageField(id = "YOWNGJ", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWNGJ;

    @MessageField(id = "YOSGJNO", name = "수수료계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSGJNO;

    @MessageField(id = "YOSWAK", name = "수수료원화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSWAK;

    @MessageField(id = "YOSHAK", name = "수수료외화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSHAK;

    @MessageField(id = "YOJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJMNO;

    @MessageField(id = "YOOHGJ", name = "외화예금입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOHGJ;

    @MessageField(id = "YOGUBN", name = "거래내용", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBN;

    @MessageField(id = "YOTOTGB", name = "환전누계출력여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTOTGB;

    @MessageField(id = "YOTOTAL", name = "연중환전누계", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTOTAL;

    @MessageField(id = "YOTONG", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTONG;

    @MessageField(id = "YOUSAK", name = "USD환산금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOUSAK;

    @MessageField(id = "YOHAPAK", name = "환전합계금액", length = 15, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHAPAK;

    @MessageField(id = "YOHJAK", name = "외화현찰금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHJAK;

    @MessageField(id = "YOHJYL", name = "외화현찰적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJYL;

    @MessageField(id = "YOHJWON", name = "외화현찰원화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHJWON;

    @MessageField(id = "YOTCAK", name = "T/C금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTCAK;

    @MessageField(id = "YOTCYL", name = "T/C적용환율", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTCYL;

    @MessageField(id = "YOTCWON", name = "T/C원화금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTCWON;

    @MessageField(id = "YOWNAK", name = "원화출금합계", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOWNAK;

    @MessageField(id = "YOTEL", name = "신청인연락처", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTEL;

    @MessageField(id = "YOMLIGE", name = "우대금액", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMLIGE;

    @MessageField(id = "YOSYJUM", name = "수령영업점명(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSYJUM;

    @MessageField(id = "YOJUMTEL", name = "영업점전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMTEL;

    @MessageField(id = "YOOHJUM", name = "외화예금개설점(한글)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOHJUM;

    @MessageField(id = "YOGDMNO", name = "공동구매채번", length = 9, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGDMNO;

    @MessageField(id = "YOTCSSR", name = "T/C판매수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTCSSR;

    @MessageField(id = "YOUDYUL", name = "환율우대율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDYUL;

}
