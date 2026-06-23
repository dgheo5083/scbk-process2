package com.scbank.process.api.edmi.dto.mci;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbLmge0102Res", type = Type.RESPONSE, description = "증명서>금융거래상세")
public class MciIbLmge0102Res implements IMessageObject {
    @MessageField(id = "WSF", name = "WSF", length = 1)
    private String WSF;

    @MessageField(id = "LL", name = "LL", length = 2)
    private String LL;

    @MessageField(id = "SFID", name = "SFID", length = 1)
    private String SFID;

    @MessageField(id = "SFTP", name = "SFTP", length = 1)
    private String SFTP;

    @MessageField(id = "ERRCOD", name = "ERRCOD", length = 4)
    private String ERRCOD;

    @MessageField(id = "ERRNAME", name = "ERRNAME", length = 50)
    private String ERRNAME;

    @MessageField(id = "ISSUENO", name = "발급번호", length = 16)
    private String ISSUENO;

    @MessageField(id = "SRCHGUBUN", name = "조회구분", length = 1)
    private String SRCHGUBUN;

    @MessageField(id = "ISSUEBASEDT", name = "발급기준일", length = 8)
    private String ISSUEBASEDT;

    @MessageField(id = "USEOBJCTV", name = "이용용도", length = 2)
    private String USEOBJCTV;

    @MessageField(id = "USEPURPOSE", name = "이용목적", length = 50)
    private String USEPURPOSE;

    @MessageField(id = "DELAYYN", name = "연체여부", length = 1)
    private String DELAYYN;

    @MessageField(id = "DELAYRECKONEDDATE", name = "기산일", length = 8)
    private String DELAYRECKONEDDATE;

    @MessageField(id = "RQSTRORG", name = "발행기관", length = 50)
    private String RQSTRORG;

    @MessageField(id = "DISHONORYN", name = "당좌부도발생여부", length = 1)
    private String DISHONORYN;

    @MessageField(id = "FIRSTDT", name = "1차부도일", length = 8)
    private String FIRSTDT;

    @MessageField(id = "SECONDDT", name = "2차부도일", length = 8)
    private String SECONDDT;

    @MessageField(id = "ZIPCD", name = "우편번호", length = 6, masking = true, maskingType = "05")
    private String ZIPCD;

    @MessageField(id = "ADDR1", name = "우편주소", length = 80, masking = true, maskingType = "05")
    private String ADDR1;

    @MessageField(id = "ADDR2", name = "상세주소", length = 100, masking = true, maskingType = "05")
    private String ADDR2;

    @MessageField(id = "PARTYNM", name = "신청인상호", length = 40)
    private String PARTYNM;

    @MessageField(id = "USERNM", name = "신청인성명", length = 30, masking = true, maskingType = "03")
    private String USERNM;

    @MessageField(id = "SSNTAXID", name = "주민사업자번호", length = 13, masking = true, maskingType = "01")
    private String SSNTAXID;

    @MessageField(id = "CORPMKCD", name = "발급구분(1:주민번호, 2:법인번호, 3:사업자번호)", length = 1)
    private String CORPMKCD;

    @MessageField(id = "UNSETTLEDBILLCNT", name = "미결제 어음수", length = 5)
    private String UNSETTLEDBILLCNT;

    @MessageField(id = "UNSETTLEDCHECKCNT", name = "미결제 수표수", length = 5)
    private String UNSETTLEDCHECKCNT;

    @MessageField(id = "LOANTOTCNT", name = "조립건수(대출정보)", length = 3)
    private Integer LOANTOTCNT;

    @MessageField(id = "LOANSEL", name = "반복부(대출정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmge0102Res/LOANTOTCNT")
    private List<LOANSEL> LOANSEL;

    @Data // 반복부(대출정보) - 대출금거래현황
    public static class LOANSEL implements IMessageObject {
        @MessageField(id = "LOANNAME", name = "대출종류", length = 42)
        private String LOANNAME;

        @MessageField(id = "LOANPOSE", name = "대출용도", length = 42)
        private String LOANPOSE;

        @MessageField(id = "INTIDCD", name = "금리구분", length = 42)
        private String INTIDCD;

        @MessageField(id = "LOANRATE", name = "대출이율", length = 9)
        private String LOANRATE;

        @MessageField(id = "LOANDATE", name = "당초차입일자", length = 8)
        private String LOANDATE;

        @MessageField(id = "LOANCAMT", name = "당초차입금액", length = 9)
        private String LOANCAMT;

        @MessageField(id = "CURRENCYCODE", name = "통화코드", length = 3)
        private String CURRENCYCODE;

        @MessageField(id = "LOANBALANCE", name = "잔액", length = 9)
        private String LOANBALANCE;

        @MessageField(id = "CONVBALANCE", name = "환산잔액", length = 9)
        private String CONVBALANCE;

        @MessageField(id = "LOANEXPIRADT", name = "대출기한", length = 8)
        private String LOANEXPIRADT;
    }

    @MessageField(id = "CLLTRLTOTCNT", name = "조립건수(담보정보)", length = 3)
    private Integer CLLTRLTOTCNT;

    @MessageField(id = "CLLTRLSEL", name = "반복부(담보정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmge0102Res/CLLTRLTOTCNT")
    private List<CLLTRLSEL> CLLTRLSEL;

    @Data // 반복부(담보정보)
    public static class CLLTRLSEL implements IMessageObject {
        @MessageField(id = "GAGEOWNER", name = "소유자", length = 30, masking = true, maskingType = "03")
        private String GAGEOWNER;

        @MessageField(id = "RELATIONCODE", name = "채무자와관계", length = 2)
        private String RELATIONCODE;

        @MessageField(id = "GAGEKIND", name = "종류", length = 20)
        private String GAGEKIND;

        @MessageField(id = "GAGETYPE", name = "담보형태", length = 2)
        private String GAGETYPE;

        @MessageField(id = "QUANTITY", name = "수량", length = 9)
        private String QUANTITY;

        @MessageField(id = "ESTIMATEAMT", name = "감정가격", length = 300)
        private String ESTIMATEAMT;

        @MessageField(id = "ESTIMATEDATE", name = "감정일자", length = 8)
        private String ESTIMATEDATE;

        @MessageField(id = "GAGEORDER", name = "순위", length = 150)
        private String GAGEORDER;

        @MessageField(id = "GAGEAMT", name = "설정금액", length = 300)
        private String GAGEAMT;

        @MessageField(id = "GAGELOCATION", name = "소재지", length = 100, masking = true, maskingType = "05")
        private String GAGELOCATION;

        @MessageField(id = "CRNCYNM", name = "담보통화", length = 10)
        private String CRNCYNM;
    }

    @MessageField(id = "CARDTOTCNT", name = "조립건수(카드정보)", length = 3)
    private Integer CARDTOTCNT;

    @MessageField(id = "CARDSEL", name = "반복부(카드정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmge0102Res/CARDTOTCNT")
    private List<CARDSEL> CARDSEL;

    @Data // 반복부(카드정보) - 최근6개월이내 당좌 및 신용카드 결제내용
    public static class CARDSEL implements IMessageObject {
        @MessageField(id = "TRADEMONTH", name = "거래 월", length = 2)
        private String TRADEMONTH;

        @MessageField(id = "PAYMENTCOUNT", name = "당좌결제매수", length = 5)
        private String PAYMENTCOUNT;

        @MessageField(id = "PAYMENTAMT", name = "당좌결제금액", length = 9)
        private String PAYMENTAMT;

        @MessageField(id = "CARDCASHAMT", name = "카드 현금서비스금액", length = 9)
        private String CARDCASHAMT;

        @MessageField(id = "CARDPURAMT", name = "카드 물품구매금액", length = 9)
        private String CARDPURAMT;

        @MessageField(id = "TOTALAMT", name = "합계", length = 9)
        private String TOTALAMT;
    }

    @MessageField(id = "DLQCNT", name = "조립건수(연체정보)", length = 3)
    private Integer DLQCNT;

    @MessageField(id = "DLQSEL", name = "반복부(연체정보)")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbLmge0102Res/DLQCNT")
    private List<DLQSEL> DLQSEL;

    @Data // 반복부(연체정보) - 최근3개월이내 10일 이상 계속된 연체명세
    public static class DLQSEL implements IMessageObject {
        @MessageField(id = "LOANNAME01", name = "대출종류", length = 40)
        private String LOANNAME01;

        @MessageField(id = "DELAYDT", name = "연체발생일", length = 8)
        private String DELAYDT;

        @MessageField(id = "DELAYAMT", name = "연체 원금", length = 9)
        private String DELAYAMT;

        @MessageField(id = "DELAYINTAMT", name = "연체 이자", length = 9)
        private String DELAYINTAMT;

        @MessageField(id = "DELAYCLEARDT", name = "연체정리일", length = 8)
        private String DELAYCLEARDT;

        @MessageField(id = "DELAYDAYS", name = "연체일수", length = 5)
        private String DELAYDAYS;
    }

    @MessageField(id = "MSGCODE", name = "메시지코드", length = 30)
    private String MSGCODE;

    @MessageField(id = "MSGCONT", name = "메시지내용", length = 300)
    private String MSGCONT;

    @MessageField(id = "FLDdelimiter1", name = "1", length = 1, defaultValue = "31")
    private String FLDdelimiter1;

    @MessageField(id = "SEGdelimiter1", name = "2", length = 1, defaultValue = "30")
    private String SEGdelimiter1;

    @MessageField(id = "ENDdelimiter1", name = "3", length = 1, defaultValue = "255")
    private String ENDdelimiter1;

    @MessageField(id = "ENDdelimiter2", name = "4", length = 1, defaultValue = "255")
    private String ENDdelimiter2;
}
