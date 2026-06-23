package com.scbank.process.api.svc.shared.components.account.dto;

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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@IntegrationMessage(id = "ListAccountHeldInfo", type = Type.RESPONSE)
public class ListAccountHeldInfo implements IMessageObject {
	@MessageField(id = "userID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String userID;

    @MessageField(id = "yoIbId", name = "인터넷뱅킹ID", length = 10, masking = true, maskingType = "01")
    private String yoIbId;

    @MessageField(id = "yoIbGb", name = "인터넷뱅킹가입구분", length = 1)
    private String yoIbGb;

    @MessageField(id = "yoTbGb", name = "텔레뱅킹가입구분", length = 1)
    private String yoTbGb;

    @MessageField(id = "yoFbGb", name = "퍼스트비즈가입구분", length = 1)
    private String yoFbGb;

    @MessageField(id = "yoPsoGb", name = "IB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
    private String yoPsoGb;

    @MessageField(id = "yoTsoGb", name = "TB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
    private String yoTsoGb;

    @MessageField(id = "yoDsGb", name = "동시사용여부 1:동시 2:개별", length = 1)
    private String yoDsGb;

    @MessageField(id = "yoPjango", name = "리워드포인트잔고", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoPjango;

    @MessageField(id = "yoName", name = "이용자성명", length = 32, masking = true, maskingType = "04")
    private String yoName;

    @MessageField(id = "yoHddd", name = "HD 지역번호", length = 4)
    private String yoHddd;

    @MessageField(id = "yoHguk", name = "HD 국번", length = 4)
    private String yoHguk;

    @MessageField(id = "yoHtel", name = "HD 전화번호", length = 4, masking = true, maskingType = "03")
    private String yoHtel;

    @MessageField(id = "yoJmNo", name = "개인：주민등록번호", length = 13, masking = true, maskingType = "01")
    private String yoJmNo;

    @MessageField(id = "yoFatIl", name = "FATCA평가일", length = 8)
    private String yoFatIl;

    @MessageField(id = "yoCifNo", name = "CIFNO", length = 13)
    private String yoCifNo;

    @MessageField(id = "yoMktYn", name = "마케팅동의존재여부", length = 1)
    private String yoMktYn;

    @MessageField(id = "yoSmsYn", name = "퍼스트알리미", length = 1)
    private String yoSmsYn;

    @MessageField(id = "yoHumGb", name = "요구불외화휴면계좌YN", length = 1)
    private String yoHumGb;

    @MessageField(id = "yoPayDd", name = "급여이체일자", length = 2)
    private String yoPayDd;

    @MessageField(id = "yoTsupGb", name = "업체구분", length = 3)
    private String yoTsupGb;

    @MessageField(id = "yoTscNod", name = "보안매체 일련번호", length = 12)
    private String yoTscNod;

    @MessageField(id = "yoTsirCd", name = "발급기관코드", length = 5)
    private String yoTsirCd;

    @MessageField(id = "yoPscSt", name = "보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:해지：재발급시,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류,9:보안카드미사용,A:이용등록해지)", length = 1)
    private String yoPscSt;

    @MessageField(id = "yoTscSt", name = "텔레보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:오손,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류)", length = 1)
    private String yoTscSt;

    @MessageField(id = "yoDummy", name = "DUMMY", length = 50)
    private String yoDummy;

    @MessageField(id = "yoGunsu", name = "자기계좌정보 배열건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String yoGunsu;

    @MessageField(id = "yoMyInfRec", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "ListAccountHoldingsInquiryResponse/yogunsu")
    private List<yoMyInfRecList> yoMyInfRec;

    @Getter
    @Setter
    public static class yoMyInfRecList implements IMessageObject {
        @MessageField(id = "yoMyGj", name = "본인계좌", length = 11, masking = true, maskingType = "02")
        private String yoMyGj;

        @MessageField(id = "yoZong", name = "계좌종별", length = 2)
        private String yoZong;

        @MessageField(id = "yoBsJum", name = "개설점", length = 3)
        private String yoBsJum;

        @MessageField(id = "yoSiIl", name = "비대면신규승인일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String yoSiIl;

        @MessageField(id = "yoMkJanSign", name = "잔액부호", length = 1)
        private String yoMkJanSign;

        @MessageField(id = "yoMkJan", name = "잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal yoMkJan;

        @MessageField(id = "yoPayGb", name = "급여구분(공배:미등록, 1:급여통장, J:당행직원급여)", length = 1)
        private String yoPayGb;

        @MessageField(id = "yoHdGb", name = "한도제한구분(여신-1:비대면한도제한)", length = 1)
        private String yoHdGb;

        @MessageField(id = "yoNhdGb", name = "한도제한구분(메뉴-Y:셀프＆한도NEW)", length = 1)
        private String yoNhdGb;

        @MessageField(id = "yoToNm", name = "통화명", length = 3)
        private String yoToNm;

        @MessageField(id = "yoHumYn", name = "휴면계좌구분YN", length = 1)
        private String yoHumYn;

        @MessageField(id = "yoGrJj", name = "거래중지계좌1", length = 1)
        private String yoGrJj;

        @MessageField(id = "yoTjDc", name = "통장대출계좌여부(Y)", length = 1)
        private String yoTjDc;

        @MessageField(id = "yoHdlmGb", name = "금융거래한도제한여부(Y:대상)", length = 1)
        private String yoHdlmGb;

        @MessageField(id = "yoZongCode", name = "계좌종별코드")
        private String yoZongCode;

        @MessageField(id = "yoMyGjNm", name = "계좌명")
        private String yoMyGjNm;

        @MessageField(id = "yoMyGjFmt", name = "계좌번호(포맷팅)")
        private String yoMyGjFmt;

    }
}
