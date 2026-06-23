package com.scbank.process.api.svc.common.service.securities.dto.recovery;

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
@IntegrationMessage(id = "SecRcvListMyAccountsResponse", type = Type.RESPONSE)
public class SecRcvListMyAccountsResponse implements IMessageObject {
    // 전문
    @MessageField(id = "userID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
    private String userID;

    @MessageField(id = "yoIBID", name = "인터넷뱅킹ID", length = 10, masking = true, maskingType = "01")
    private String yoIBID;

    @MessageField(id = "yoIBGB", name = "인터넷뱅킹가입구분", length = 1)
    private String yoIBGB;

    @MessageField(id = "yoTBGB", name = "텔레뱅킹가입구분", length = 1)
    private String yoTBGB;

    @MessageField(id = "yoFBGB", name = "퍼스트비즈가입구분", length = 1)
    private String yoFBGB;

    @MessageField(id = "yoPSOGB", name = "IB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
    private String yoPSOGB;

    @MessageField(id = "yoTSOGB", name = "TB 보안카드사용정보 1:안전 2:OTP 3:NEWOTP", length = 1)
    private String yoTSOGB;

    @MessageField(id = "yoDSGB", name = "동시사용여부 1:동시 2:개별", length = 1)
    private String yoDSGB;

    @MessageField(id = "yoPJANGO", name = "리워드포인트잔고", length = 10, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal yoPJANGO;

    @MessageField(id = "yoNAME", name = "이용자성명", length = 32, masking = true, maskingType = "04")
    private String yoNAME;

    @MessageField(id = "yoHDDD", name = "HD 지역번호", length = 4)
    private String yoHDDD;

    @MessageField(id = "yoHGUK", name = "HD 국번", length = 4)
    private String yoHGUK;

    @MessageField(id = "yoHTEL", name = "HD 전화번호", length = 4, masking = true, maskingType = "03")
    private String yoHTEL;

    @MessageField(id = "yoJMNO", name = "개인：주민등록번호", length = 13, masking = true, maskingType = "01")
    private String yoJMNO;

    @MessageField(id = "yoFATIL", name = "FATCA평가일", length = 8)
    private String yoFATIL;

    @MessageField(id = "yoCIFNO", name = "CIFNO", length = 13)
    private String yoCIFNO;

    @MessageField(id = "yoMKTYN", name = "마케팅동의존재여부", length = 1)
    private String yoMKTYN;

    @MessageField(id = "yoSMSYN", name = "퍼스트알리미", length = 1)
    private String yoSMSYN;

    @MessageField(id = "yoHUMGB", name = "요구불외화휴면계좌YN", length = 1)
    private String yoHUMGB;

    @MessageField(id = "yoPAYDD", name = "급여이체일자", length = 2)
    private String yoPAYDD;

    @MessageField(id = "yoTSUPGB", name = "업체구분", length = 3)
    private String yoTSUPGB;

    @MessageField(id = "yoTSCNOD", name = "보안매체 일련번호", length = 12)
    private String yoTSCNOD;

    @MessageField(id = "yoTSIRCD", name = "발급기관코드", length = 5)
    private String yoTSIRCD;

    @MessageField(id = "yoPSCST", name = "보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:해지：재발급시,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류,9:보안카드미사용,A:이용등록해지)", length = 1)
    private String yoPSCST;

    @MessageField(id = "yoTSCST", name = "텔레보안매체상태(0:정상,1:분실등록,2:이용자해지/법인해지시,3:재등록,4:오손,5:오류횟수초과,6:발급취소,7:보안카드폐기,8:일련번호오류)", length = 1)
    private String yoTSCST;

    @MessageField(id = "yoDUMMY", name = "DUMMY", length = 50)
    private String yoDUMMY;

    @MessageField(id = "yoGUNSU", name = "자기계좌정보 배열건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer yoGUNSU;

    @MessageField(id = "yoMYINF_REC", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "Ti1ibk01H866Res/yoGUNSU")
    private List<YOMYINF_REC> yoMYINF_REC;

    @Getter
    @Setter
    public static class YOMYINF_REC implements IMessageObject {
        @MessageField(id = "yoMYGJ", name = "본인계좌", length = 11, masking = true, maskingType = "02")
        private String yoMYGJ;

        @MessageField(id = "yoZONG", name = "계좌종별", length = 2)
        private String yoZONG;

        @MessageField(id = "yoBSJUM", name = "개설점", length = 3)
        private String yoBSJUM;

        @MessageField(id = "yoSIIL", name = "비대면신규승인일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String yoSIIL;

        @MessageField(id = "yoMKJANSIGN", name = "잔액부호", length = 1)
        private String yoMKJANSIGN;

        @MessageField(id = "yoMKJAN", name = "잔액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal yoMKJAN;

        @MessageField(id = "yoPAYGB", name = "급여구분(공배:미등록, 1:급여통장, J:당행직원급여)", length = 1)
        private String yoPAYGB;

        @MessageField(id = "yoHDGB", name = "한도제한구분(여신-1:비대면한도제한)", length = 1)
        private String yoHDGB;

        @MessageField(id = "yoNHDGB", name = "한도제한구분(메뉴-Y:셀프＆한도NEW)", length = 1)
        private String yoNHDGB;

        @MessageField(id = "yoTONM", name = "통화명", length = 3)
        private String yoTONM;

        @MessageField(id = "yoHUMYN", name = "휴면계좌구분YN", length = 1)
        private String yoHUMYN;

        @MessageField(id = "yoGRJJ", name = "거래중지계좌1", length = 1)
        private String yoGRJJ;

        @MessageField(id = "yoTJDC", name = "통장대출계좌여부(Y)", length = 1)
        private String yoTJDC;

        @MessageField(id = "yoHDLMGB", name = "금융거래한도제한여부(Y:대상)", length = 1)
        private String yoHDLMGB;

        // input
        @MessageField(id = "yoMYGJNM", name = "yoMYGJNM", length = 1)
        private String yoMYGJNM;

    }

    @MessageField(id = "yoGRJJCnt", name = "yoGRJJCnt")
    private int yoGRJJCnt;

    @MessageField(id = "totAcctCnt", name = "totAcctCnt")
    private int totAcctCnt;

    @MessageField(id = "scrnDataInfo", name = "scrnDataInfo")
    private String scrnDataInfo;

    @MessageField(id = "screenFlag", name = "screenFlag")
    private String screenFlag;

    @MessageField(id = "resultAcctList", name = "resultAcctList")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "Ti1ibk01H866Res/totAcctCnt")
    private List<YOMYINF_REC> resultAcctList;
}
