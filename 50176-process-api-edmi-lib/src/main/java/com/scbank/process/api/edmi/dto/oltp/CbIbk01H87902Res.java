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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H87902Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "증명서>인터넷증명서조회-예금잔액증명서상세")
public class CbIbk01H87902Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "YOJANPRFGBN", name = "H잔액증명구분", length = 10)
    private String YOJANPRFGBN;

    @MessageField(id = "YOCMFNA", name = "H고객명", length = 32, masking = true, maskingType = "03")
    private String YOCMFNA;

    @MessageField(id = "YOENGCMN", name = "영문고객명", length = 30, masking = true, maskingType = "03")
    private String YOENGCMN;

    @MessageField(id = "YOCMFENGJSO1", name = "고객영문주소1", length = 45, masking = true, maskingType = "05")
    private String YOCMFENGJSO1;

    @MessageField(id = "YOCMFENGJSO2", name = "고객영문주소2", length = 45, masking = true, maskingType = "05")
    private String YOCMFENGJSO2;

    @MessageField(id = "YODUMMY1", name = "DUMMY", length = 100)
    private String YODUMMY1;

    @MessageField(id = "YOHAPAK", name = "합계금액", length = 15)
    private BigDecimal YOHAPAK;

    @MessageField(id = "YOFXHAPAK", name = "외화합계금액", length = 15)
    private BigDecimal YOFXHAPAK;

    @MessageField(id = "YOTJMHAPAK", name = "타점합계금액", length = 15)
    private BigDecimal YOTJMHAPAK;

    @MessageField(id = "YOSTWGHAP", name = "신탁원금합계", length = 15)
    private BigDecimal YOSTWGHAP;

    @MessageField(id = "YOWGAIIGHAP", name = "원가이익금합계", length = 15)
    private BigDecimal YOWGAIIGHAP;

    @MessageField(id = "YOHGPYOAK", name = "H한글표시금액", length = 82)
    private String YOHGPYOAK;

    @MessageField(id = "YOJANPRFIL", name = "잔액증명일자", length = 8)
    private String YOJANPRFIL;

    @MessageField(id = "YOGRIL", name = "거래일자", length = 8)
    private String YOGRIL;

    @MessageField(id = "YOENGGRIL", name = "영문거래일자", length = 11)
    private String YOENGGRIL;

    @MessageField(id = "YOJPRFILENG", name = "잔액증명일자영", length = 11)
    private String YOJPRFILENG;

    @MessageField(id = "YOCURCODGBN", name = "통화코드구분명", length = 10)
    private String YOCURCODGBN;

    @MessageField(id = "YOJYHY", name = "적용환율", length = 7)
    private Integer YOJYHY;

    @MessageField(id = "YOSSRAK", name = "수수료금액", length = 15)
    private BigDecimal YOSSRAK;

    @MessageField(id = "YOJMSUPNO", name = "주민사업자번호", length = 14, masking = true, maskingType = "01")
    private String YOJMSUPNO;

    @MessageField(id = "YOSEQNO1", name = "잔액증명발급번호", length = 17)
    private String YOSEQNO1;

    @MessageField(id = "YOCMFKORJSO1", name = "H고객한글주소1", length = 72, masking = true, maskingType = "05")
    private String YOCMFKORJSO1;

    @MessageField(id = "YOCMFKORJSO2", name = "H고객한글주소2", length = 82, masking = true, maskingType = "05")
    private String YOCMFKORJSO2;

    @MessageField(id = "YOMJYB", name = "면제여부(Y/N)", length = 1)
    private String YOMJYB;

    @MessageField(id = "YOPTYB", name = "포인트결제여부(Y/N)", length = 1)
    private String YOPTYB;

    @MessageField(id = "YOSGJNO", name = "수수료출금계좌", length = 11, masking = true, maskingType = "02")
    private String YOSGJNO;

    @MessageField(id = "YOPURPOSE", name = "H이용목적", length = 22)
    private String YOPURPOSE;

    @MessageField(id = "YOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOJRPGSU;

    @MessageField(id = "YOGTAOUTINF", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H87902Res/YOJRPGSU")
    private List<YOGTAOUTINF> YOGTAOUTINF;

    @Getter
    @Setter
    public static class YOGTAOUTINF implements IMessageObject {
        @MessageField(id = "YOKMNA", name = "H과목명", length = 22)
        private String YOKMNA;

        @MessageField(id = "YOGJWNO2", name = "계좌번호2", length = 11, masking = true, maskingType = "02")
        private String YOGJWNO2;

        @MessageField(id = "YOWJJAN", name = "원장잔액", length = 15)
        private BigDecimal YOWJJAN;

        @MessageField(id = "YOTAJAK", name = "타점권금액", length = 15)
        private BigDecimal YOTAJAK;

        @MessageField(id = "YOGRNDCAK", name = "관련대출금액", length = 15)
        private BigDecimal YOGRNDCAK;

        @MessageField(id = "YOJIGLMTAK", name = "지급제한금액", length = 15)
        private BigDecimal YOJIGLMTAK;

        @MessageField(id = "YOSTWBAK", name = "신탁원본금액", length = 15)
        private BigDecimal YOSTWBAK;

        @MessageField(id = "YOWGAIJA", name = "원가이자", length = 15)
        private BigDecimal YOWGAIJA;

        @MessageField(id = "YOWBSINA", name = "H원본수익자명", length = 14, masking = true, maskingType = "03")
        private String YOWBSINA;

        @MessageField(id = "YOIKSINA", name = "H이익수익자명", length = 14, masking = true, maskingType = "03")
        private String YOIKSINA;

        @MessageField(id = "YOENGKMN", name = "영문과목명", length = 50)
        private String YOENGKMN;

        @MessageField(id = "YOENGKMN1", name = "영문과목명1", length = 50)
        private String YOENGKMN1;

        @MessageField(id = "YOCURNA", name = "통화명", length = 5)
        private String YOCURNA;

        @MessageField(id = "YOFXJAN", name = "외화잔액", length = 15)
        private BigDecimal YOFXJAN;

        @MessageField(id = "YOWBSUIJENM", name = "원본수익자영문", length = 20, masking = true, maskingType = "03")
        private String YOWBSUIJENM;

        @MessageField(id = "YOIIKSUIJENM", name = "이익수익자영문", length = 20, masking = true, maskingType = "03")
        private String YOIIKSUIJENM;

        @MessageField(id = "YOJKILJA", name = "질권해지일자", length = 9)
        private String YOJKILJA;
    }

}