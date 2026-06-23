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
@IntegrationMessage(id = "CbIbk01H38400Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "금융소비자보호법 BC카드 계약 카드조회 서비스")
public class CbIbk01H38400Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YONAME", name = "한글성명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOJHNM", name = "제휴코드명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJHNM;

    @MessageField(id = "YOCDGB", name = "카드구분 (1:BC, 2:VISA, 3:JCB, 4:MASTER, 6:CUP, 7:GLOBAL)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCDGB;

    @MessageField(id = "YOBKNM", name = "은행명", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBKNM;

    @MessageField(id = "YOKJGJ", name = "결제계좌", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKJGJ;

    @MessageField(id = "YOGJIL", name = "결제일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGJIL;

    @MessageField(id = "YOSINHD", name = "총한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSINHD;

    @MessageField(id = "YODBGIL", name = "발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YODBGIL;

    @MessageField(id = "YOSVCIY", name = "현금서비스수수료율 99V99", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSVCIY;

    @MessageField(id = "YOHB1IY", name = "할부2개월수수료율 99V99", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHB1IY;

    @MessageField(id = "YOHB2IY", name = "할부24개월수수료율 99V99", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHB2IY;

    @MessageField(id = "YORVSIY", name = "리볼빙현금수수료율 99V99", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YORVSIY;

    @MessageField(id = "YOGJGB", name = "공용지정구분 (1:공용, 2:지정)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJGB;

    @MessageField(id = "YOJGNM", name = "지정자한글명", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJGNM;

    @MessageField(id = "YOGIGAN", name = "유효기간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGIGAN;

    @MessageField(id = "YOHMUPNO", name = "자택우편번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHMUPNO;

    @MessageField(id = "YOHMJSO1", name = "자택주소1", length = 72, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHMJSO1;

    @MessageField(id = "YOHMJSO2", name = "자택주소2", length = 102, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHMJSO2;

    @MessageField(id = "YOCARD", name = "개인기업구분 (1:개인, 2:기업)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCARD;

    @MessageField(id = "YOGYHWNO", name = "계약카드번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGYHWNO;

    @MessageField(id = "YOHNAME", name = "현재고객명，업체명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHNAME;

    @MessageField(id = "YOHJUMIN", name = "현재주민사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJUMIN;

    @MessageField(id = "YOJHCD", name = "제휴코드", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJHCD;

    @MessageField(id = "YOSVCGB", name = "현금서비스한도구분 Y:show, N:hide", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSVCGB;

    @MessageField(id = "YOSVCHD", name = "현금서비스한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSVCHD;
}