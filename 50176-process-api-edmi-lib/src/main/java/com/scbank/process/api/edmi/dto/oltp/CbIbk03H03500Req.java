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
@IntegrationMessage(id = "CbIbk03H03500Req", type = Type.REQUEST, captureSystem = "OLTP", description = "로그인 요청 전문")
public class CbIbk03H03500Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    // TODO: 임시 인코딩 지정(아이디 로그인시 비밀번호 인코딩 오류)
    @MessageField(id = "TSPassword", name = "통신비밀번호", encoding = "cp500", length = 8)
    private String TSPassword;

    @MessageField(id = "ConnectType", name = "접속방법", length = 1)
    private String ConnectType;

    @MessageField(id = "HPNumber", name = "핸드폰번호", length = 12, masking = true, maskingType = "05")
    private String HPNumber;

    @MessageField(id = "AccountListCount", name = "계좌명세요구건수", length = 3)
    private Integer AccountListCount;

    @MessageField(id = "YIMOBILE3", name = "모바일3.0거래여부", length = 1)
    private String YIMOBILE3;

    @MessageField(id = "dumy01", name = "dumy01", length = 99)
    private String dumy01;

    // @MessageField(id = "ContinueInfo", name = "연속정보") // 필드길이가 0인데?
    // private String continueInfo;

    @MessageField(id = "ContinueType", name = "연속거래종류", length = 1)
    private String ContinueType;

    @MessageField(id = "AvailBalSign", name = "출금계좌두드림잔액부호", length = 1)
    private String AvailBalSign;

    @MessageField(id = "AvailBalance", name = "출금계좌두드림잔액합계", length = 13)
    private BigDecimal AvailBalance;

    @MessageField(id = "ContinueYn", name = "연속여부", length = 3)
    private String ContinueYn;

    @MessageField(id = "ReferOrder", name = "조회순서", length = 3)
    private String ReferOrder;

    @MessageField(id = "ContinueAssort", name = "연속구분", length = 3)
    private String ContinueAssort;

    @MessageField(id = "AcctServiceCode", name = "계좌부서비스코드", length = 3)
    private String AcctServiceCode;

    @MessageField(id = "DepPayPart", name = "입지급구분", length = 1)
    private String DepPayPart;

    @MessageField(id = "DrawBankCode", name = "은행코드", length = 3)
    private String DrawBankCode;

    @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 16, masking = true, maskingType = "02")
    private String DrawAcctNum;

    @MessageField(id = "DepBankCode", name = "입금계좌은행코드", length = 3)
    private String DepBankCode;

    @MessageField(id = "DepAcctNum", name = "입금계좌은행", length = 16, masking = true, maskingType = "02")
    private String DepAcctNum;

    @MessageField(id = "PrintCount", name = "출력명세수 TOT", length = 3)
    private Integer PrintCount;

    @MessageField(id = "ReferCount", name = "조회계좌수 TOT", length = 3)
    private Integer ReferCount;

    @MessageField(id = "PayCount", name = "출금계좌수 TOT", length = 3)
    private Integer PayCount;

    @MessageField(id = "CurrentPage", name = "현재요청 페이지", length = 5)
    private String CurrentPage;

    @MessageField(id = "TotPageCnt", name = "전체 페이지수", length = 5)
    private Integer TotPageCnt;

    @MessageField(id = "TotCount", name = "전체 건수", length = 5)
    private Integer TotCount;

    @MessageField(id = "CardContinueInfo", name = "카드연속정보", length = 3)
    private Integer CardContinueInfo;

    @MessageField(id = "YIARRAY05", name = "현대카드연속정보", length = 3)
    private Integer YIARRAY05;

    @MessageField(id = "dumy02", name = "dumy02", length = 27)
    private String dumy02;

    @MessageField(id = "Dummy", name = "더미", length = 123)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4)
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "U정보화마을주문일", length = 8)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "U정보화마을주문번호", length = 10)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "더미", length = 33)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "더미2", length = 10)
    private String Dummy2;
}
