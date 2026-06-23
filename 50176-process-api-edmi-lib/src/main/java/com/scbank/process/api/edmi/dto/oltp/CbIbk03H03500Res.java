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
import lombok.ToString;

@Getter
@Setter
@ToString
@IntegrationMessage(id = "CbIbk03H03500Res", type = Type.RESPONSE, description = "로그인 응답 전문")
public class CbIbk03H03500Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    // @MessageField(id = "ContinueInfo", name = "연속정보", length = 0) // 필드길이가 0인데?
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

    @MessageField(id = "DrawBankCode", name = "출금은행코드", length = 3)
    private String DrawBankCode;

    @MessageField(id = "DrawAcctNum", name = "출금계좌번호", length = 16, masking = true, maskingType = "02")
    private String DrawAcctNum;

    @MessageField(id = "DepBankCode", name = "입금계좌은행코드", length = 3)
    private String DepBankCode;

    @MessageField(id = "DepAcctNum", name = "입금계좌번호", length = 16, masking = true, maskingType = "02")
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

    @MessageField(id = "YOHAPYN", name = "펀드합산정보세팅여부Y", length = 1)
    private String YOHAPYN;

    @MessageField(id = "YOHAPGMK", name = "펀드합산금액", length = 14)
    private BigDecimal YOHAPGMK;

    @MessageField(id = "YOARRAY05", name = "현대카드연속정보", length = 3)
    private BigDecimal YOARRAY05;

    @MessageField(id = "dumy00", name = "dummy", length = 12)
    private String dumy00;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "RegiBranchNum", name = "등록점번호", length = 3)
    private String RegiBranchNum;

    @MessageField(id = "CustName", name = "고객명", length = 22, masking = true, maskingType = "04", multiBytes = true)
    private String CustName;

    @MessageField(id = "EngCustName", name = "영문고객명", length = 50, masking = true, maskingType = "04")
    private String EngCustName;

    @MessageField(id = "LastTransDate", name = "최종이체일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String LastTransDate;

    @MessageField(id = "FinalUseDate", name = "최종이용일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String FinalUseDate;

    @MessageField(id = "FinalUseTime", name = "최종이용시", length = 6)
    private String FinalUseTime;

    @MessageField(id = "FinalNetwork", name = "최종접속망", length = 16, multiBytes = true)
    private String FinalNetwork;

    @MessageField(id = "FinalServiceCode", name = "최종서비스코드", length = 32, multiBytes = true)
    private String FinalServiceCode;

    @MessageField(id = "UseGrade", name = "이용등급", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String UseGrade;

    // @MessageField(id = "freeAcctInfo", name = "수수료계좌정보", length = 0) // 필드길이가
    // 0인데?
    // private String freeAcctInfo;

    // @MessageField(id = "freeAcct", name = "수수료계좌", length = 0) // 필드길이가 0인데?
    // private String freeAcct;

    @MessageField(id = "SSRBrNo", name = "점번호", length = 3)
    private String SSRBrNo;

    @MessageField(id = "SSRKmCD", name = "과목코드", length = 2)
    private String SSRKmCD;

    @MessageField(id = "SSRGeNo", name = "계좌번호", length = 6, masking = true, maskingType = "02")
    private String SSRGeNo;

    @MessageField(id = "UserCmfNo", name = "CMF번호", length = 7)
    private String UserCmfNo;

    @MessageField(id = "UserCifNo", name = "CIF번호", length = 13)
    private String UserCifNo;

    // @MessageField(id = "serviceUsingStatus", name = "서비스이용상태", length = 0)
    // //필드길이가 0인데?
    // private String serviceUsingStatus;

    @MessageField(id = "TotAcctPrintYN", name = "화면출력구분", length = 1)
    private String TotAcctPrintYN;

    @MessageField(id = "cardPrintYn", name = "카드출력여부", length = 1)
    private String CardPrintYN;

    @MessageField(id = "ReferAcctInBoxYN", name = "조회계좌입력박스여부", length = 1)
    private String ReferAcctInBoxYN;

    @MessageField(id = "DrawAcctInBoxYN", name = "출금계좌입력박스여부", length = 1)
    private String DrawAcctInBoxYN;

    @MessageField(id = "TSPasswordState", name = "통신비밀번호상태", length = 1)
    private String TSPasswordState;

    // @MessageField(id = "AtonceTrans", name = "즉시이체", length = 0) // 필드길이가 0인데?
    // private String AtonceTrans;

    @MessageField(id = "AtonceTransState", name = "즉시이체상태", length = 1)
    private String AtonceTransState;

    @MessageField(id = "AtonceApprState", name = "즉시이체승인용", length = 1)
    private String AtonceApprState;

    @MessageField(id = "AtonceAdminNumState", name = "즉시이체관리번호", length = 1)
    private String AtonceAdminNumState;

    @MessageField(id = "UserPWRegisterYN", name = "이용자PW등록여부", length = 1)
    private String UserPWRegisterYN;

    @MessageField(id = "TransPWUseYN", name = "이체비밀번사용여부", length = 1)
    private String TransPWUseYN;

    @MessageField(id = "ForcurReferState", name = "외국환조회상태", length = 1)
    private String ForcurReferState;

    @MessageField(id = "LoanReferState", name = "대출조회상태", length = 1)
    private String LoanReferState;

    @MessageField(id = "RemitType", name = "송금확인증유형", length = 1)
    private String RemitType;

    @MessageField(id = "IPProtectYN", name = "해외IP차단여부", length = 1)
    private String IPProtectYN;

    @MessageField(id = "OtherBankErrorYN", name = "타행이체장애여부", length = 1)
    private String OtherBankErrorYN;

    // @MessageField(id = "SafeCardInfo", name = "안전카드정보", length = 0) // 필드길이가 0인데?
    // private String SafeCardInfo;

    @MessageField(id = "SafeCardKind", name = "안전카드종류", length = 1)
    private String SafeCardKind;

    @MessageField(id = "SafeCardState", name = "안전카드상태", length = 1)
    private String SafeCardState;

    @MessageField(id = "SafeCardIssueNum", name = "안전카드발급번호", length = 12)
    private String SafeCardIssueNum;

    @MessageField(id = "SafeCardBranchNum", name = "안전카드점번호", length = 3)
    private String SafeCardBranchNum;

    @MessageField(id = "SafeCardINDEX", name = "안전카드INDEX", length = 6)
    private String SafeCardINDEX;

    @MessageField(id = "SafeCardINDEX2", name = "안전카드INDEX2", length = 6)
    private String SafeCardINDEX2;

    @MessageField(id = "Reserved0", name = "예비0", length = 1)
    private String Reserved0;

    @MessageField(id = "SmartOTP", name = "스마트OTP(0~9)", length = 1)
    private String SmartOTP;

    @MessageField(id = "Reserved1", name = "예비1", length = 8)
    private String Reserved1;

    @MessageField(id = "PerNoPart", name = "주민번호구분", length = 1)
    private String PerNoPart;

    @MessageField(id = "PerBusNo", name = "주민/사업자번호", length = 13, masking = true, maskingType = "01")
    private String PerBusNo;

    @MessageField(id = "PCLoanPromiseYN", name = "TelePC론약정여부", length = 1)
    private String PCLoanPromiseYN;

    @MessageField(id = "UserLevel", name = "으뜸고객정보", length = 1)
    private String UserLevel;

    @MessageField(id = "TeleOne", name = "연락전화번호1", length = 4)
    private String TeleOne;

    @MessageField(id = "TeleDash1", name = "-", length = 1)
    private String TeleDash1;

    @MessageField(id = "TeleTwo", name = "전화국번", length = 4)
    private String TeleTwo;

    @MessageField(id = "TeleDash2", name = "-", length = 1)
    private String TeleDash2;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4)
    private String TeleThree;

    @MessageField(id = "HPOne", name = "핸드폰번호1", length = 4)
    private String HPOne;

    @MessageField(id = "HPDash1", name = "-", length = 1)
    private String HPDash1;

    @MessageField(id = "HPTwo", name = "핸드폰번호2", length = 4)
    private String HPTwo;

    @MessageField(id = "HPDash2", name = "-", length = 1)
    private String HPDash2;

    @MessageField(id = "HPThree", name = "핸드폰번호3", length = 4)
    private String HPThree;

    @MessageField(id = "HomePostNum", name = "우편번호", length = 6)
    private String HomePostNum;

    @MessageField(id = "Email", name = "E-MAIL", length = 60)
    private String Email;

    @MessageField(id = "AllAccountListYN", name = "전계좌조회가능여부(1:조회불가)", length = 1)
    private String AllAccountListYN;

    @MessageField(id = "VirtualAccountYN", name = "가상계좌여부", length = 1)
    private String VirtualAccountYN;

    @MessageField(id = "PerNoBusNo2", name = "개인사업자번호", length = 13)
    private String PerNoBusNo2;

    @MessageField(id = "SafeCardYearOver", name = "안전카드사용1년초과", length = 1)
    private String SafeCardYearOver;

    @MessageField(id = "YOPASSYN", name = "실명확인증표", length = 1)
    private String YOPASSYN;

    @MessageField(id = "YOGEOJU", name = "거주구분", length = 1)
    private String YOGEOJU;

    @MessageField(id = "YOBIRTH", name = "생년월일(양력기준)", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOBIRTH;

    @MessageField(id = "YOKWASE", name = "과세구분", length = 2)
    private String YOKWASE;

    @MessageField(id = "YOSMBH", name = "실명확인번호", length = 13)
    private String YOSMBH;

    @MessageField(id = "YOSUSIN", name = "수신３３조３２조", length = 1)
    private String YOSUSIN;

    @MessageField(id = "YOSMTGB", name = "스마트폰뱅킹가입여부", length = 1)
    private String YOSMTGB;

    @MessageField(id = "YODOHAP", name = "두드림합계3천이상", length = 1)
    private String YODOHAP;

    @MessageField(id = "YOHPIN", name = "휴대폰인증사용여부", length = 1)
    private String YOHPIN;

    @MessageField(id = "YOPHGB", name = "전화승인사용여부", length = 1)
    private String YOPHGB;

    @MessageField(id = "YOPCGB", name = "이용자PC지정서비스", length = 1)
    private String YOPCGB;

    @MessageField(id = "YOLYTJGO", name = "로열티포인트잔고", length = 10)
    private BigDecimal YOLYTJGO;

    @MessageField(id = "YOJUSOGB", name = "신주소구분", length = 1)
    private String YOJUSOGB;

    @MessageField(id = "Reserved2", name = "예비2", length = 21)
    private String Reserved2;

    @MessageField(id = "YOPINSTAT", name = "PIN가입상태", length = 1)
    private String YOPINSTAT;

    @MessageField(id = "YODELAY", name = "지연이체등록여부", length = 1)
    private String YODELAY;

    @MessageField(id = "YOMARK", name = "마케팅동의서출력여부", length = 1)
    private String YOMARK;

    @MessageField(id = "YOSIMPLE", name = "간편이체여부(Y:사용)", length = 1)
    private String YOSIMPLE;

    @MessageField(id = "YOBLKYB", name = "블록체인검증생략여부", length = 1)
    private String YOBLKYB;

    @MessageField(id = "YOBFLG", name = "CDD채널블로킹FLAG", length = 1)
    private String YOBFLG;

    @MessageField(id = "YOLRSOTGB", name = "개인정보노출등록구분(1:등록 3:미등록)", length = 1)
    private String YOLRSOTGB;

    @MessageField(id = "YOGEORE", name = "거래자구분", length = 2)
    private String YOGEORE;

    @MessageField(id = "YOPBANK", name = "세그먼트등급", length = 1)
    private String YOPBANK;

    @MessageField(id = "YOJDNAME", name = "전담관리자성명", length = 14, multiBytes = true)
    private String YOJDNAME;

    @MessageField(id = "YOJDOPNO", name = "전담관리자행번", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJDOPNO;

    @MessageField(id = "YOGBRNM", name = "점명", length = 14)
    private String YOGBRNM;

    @MessageField(id = "YOJDTEL", name = "전담관리자등록점전화", length = 12)
    private String YOJDTEL;

    @MessageField(id = "YOPNTJAN", name = "리워드포인트적립합계", length = 15)
    private BigDecimal YOPNTJAN;

    @MessageField(id = "YOBUBNO", name = "법인등록번호", length = 13)
    private String YOBUBNO;

    @MessageField(id = "YOPEGB", name = "PE고객여부", length = 1)
    private String YOPEGB;

    @MessageField(id = "YOICGUN", name = "자동이체출금건수", length = 5)
    private Integer YOICGUN;

    @MessageField(id = "YOICGMT", name = "자동이체출금금액합계", length = 15)
    private BigDecimal YOICGMT;

    @MessageField(id = "YOCDDIL", name = "CDD유효일자2308", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCDDIL;

    @MessageField(id = "YOORYU", name = "오류송금수취인여부(1:자행환,2:타행환,3:자타모두,SPACE:대상외)", length = 1)
    private String YOORYU;

    @MessageField(id = "YODUMMYA", name = "더미", length = 5)
    private String YODUMMYA;

    @MessageField(id = "NationListRecord", name = "국가출력건수", length = 2)
    private Integer NationListRecord;

    @MessageField(id = "NationCode", name = "국가코드(001~010)", length = 3)
    private String NationCode;

    @MessageField(id = "NationName", name = "국가이름", length = 12)
    private String NationName;

    @MessageField(id = "NationCode1", name = "국가코드(001~010)", length = 3)
    private String NationCode1;

    @MessageField(id = "NationName1", name = "국가이름", length = 12)
    private String NationName1;

    @MessageField(id = "NationCode2", name = "국가코드(001~010)", length = 3)
    private String NationCode2;

    @MessageField(id = "NationName2", name = "국가이름", length = 12)
    private String NationName2;

    @MessageField(id = "NationCode3", name = "국가코드(001~010)", length = 3)
    private String NationCode3;

    @MessageField(id = "NationName3", name = "국가이름", length = 12)
    private String NationName3;

    @MessageField(id = "NationCode4", name = "국가코드(001~010)", length = 3)
    private String NationCode4;

    @MessageField(id = "NationName4", name = "국가이름", length = 12)
    private String NationName4;

    @MessageField(id = "NationCode5", name = "국가코드(001~010)", length = 3)
    private String NationCode5;

    @MessageField(id = "NationName5", name = "국가이름", length = 12)
    private String NationName5;

    @MessageField(id = "NationCode6", name = "국가코드(001~010)", length = 3)
    private String NationCode6;

    @MessageField(id = "NationName6", name = "국가이름", length = 12)
    private String NationName6;

    @MessageField(id = "NationCode7", name = "국가코드(001~010)", length = 3)
    private String NationCode7;

    @MessageField(id = "NationName7", name = "국가이름", length = 12)
    private String NationName7;

    @MessageField(id = "NationCode8", name = "국가코드(001~010)", length = 3)
    private String NationCode8;

    @MessageField(id = "NationName8", name = "국가이름", length = 12)
    private String NationName8;

    @MessageField(id = "NationCode9", name = "국가코드(001~010)", length = 3)
    private String NationCode9;

    @MessageField(id = "NationName9", name = "국가이름", length = 12)
    private String NationName9;

    @MessageField(id = "YOBANKCD", name = "은행코드", length = 3)
    private String YOBANKCD;

    @MessageField(id = "YOBCGJNO", name = "결제계좌번호", length = 16, masking = true, maskingType = "02")
    private String YOBCGJNO;

    @MessageField(id = "YOGGIL", name = "결제일", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGGIL;

    @MessageField(id = "YOYCIL", name = "최초연체일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYCIL;

    @MessageField(id = "YOWON", name = "결제원금", length = 11)
    private BigDecimal YOWON;

    @MessageField(id = "YOSUSU", name = "결제수수료", length = 9)
    private BigDecimal YOSUSU;

    @MessageField(id = "YOIJA", name = "결제이자", length = 9)
    private BigDecimal YOIJA;

    @MessageField(id = "YOTOT", name = "결제합계", length = 11)
    private BigDecimal YOTOT;

    @MessageField(id = "YOBRNO", name = "관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOBRNO;

    @MessageField(id = "YOCGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCGIL;

    @MessageField(id = "YOREVGB", name = "리볼빙여부", length = 1)
    private String YOREVGB;

    @MessageField(id = "YOHPNO", name = "핸드폰번호", length = 12, masking = true, maskingType = "05")
    private String YOHPNO;

    @MessageField(id = "YOBSBB", name = "구분(1:우편2:E-MAIL)", length = 1)
    private String YOBSBB;

    @MessageField(id = "YOMAIL", name = "E-MAIL", length = 40, masking = true, maskingType = "07")
    private String YOMAIL;

    @MessageField(id = "YOBSBB2", name = "타행선결제구분Y", length = 1)
    private String YOBSBB2;

    @MessageField(id = "DrawAcctNumListRecord", name = "출력계좌건수", length = 3)
    private Integer DrawAcctNumListRecord;

    @MessageField(id = "REC_01", name = "출력계좌반복부")
    @RepeatedField(repeatType = RepeatType.FIXED, repeatCount = "25")
    private List<CbIbk03H03500ResGrid> REC_01;

    @MessageField(id = "UserCi", name = "CI정보", length = 88)
    private String UserCi;

    @Getter
    @Setter
    public static class CbIbk03H03500ResGrid implements IMessageObject {

        @MessageField(id = "DrawAcctNum", name = "계좌번호", length = 16)
        private String DrawAcctNum;

        @MessageField(id = "Assort", name = "계좌종별", length = 2)
        private String Assort;

        @MessageField(id = "DrawYN", name = "출금여부", length = 1)
        private String DrawYN;

        @MessageField(id = "DepKind", name = "계좌종류(1:입출금, 2:예금, 3:BC카드, H:현대카드)", length = 1)
        private String DepKind;

        @MessageField(id = "BalSign", name = "잔액부호", length = 1)
        private String BalSign;

        @MessageField(id = "Balance", name = "잔액", length = 13)
        private BigDecimal Balance;

        @MessageField(id = "Curcy", name = "통화코드", length = 3)
        private String Curcy;

        @MessageField(id = "DrawAcctNameAlias", name = "계좌별명/제휴카드명", length = 22, multiBytes = true)
        private String DrawAcctNameAlias;

        @MessageField(id = "AcctDisplay", name = "송금인명지정방법", length = 1)
        private String AcctDisplay;

        @MessageField(id = "SavingStartDate", name = "예금신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingStartDate;

        @MessageField(id = "SavingEndDate", name = "예금만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String SavingEndDate;

        @MessageField(id = "FundGubun", name = "펀드구분", length = 1)
        private String FundGubun;

        @MessageField(id = "FundType", name = "펀드종류", length = 2)
        private String FundType;

        @MessageField(id = "FundCode", name = "펀드코드", length = 10)
        private String FundCode;

        @MessageField(id = "CustodianAmountSign", name = "수탁금액Sign", length = 1)
        private String CustodianAmountSign;

        @MessageField(id = "CustodianAmount", name = "수탁금액", length = 15)
        private BigDecimal CustodianAmount;

        @MessageField(id = "EstimateAmountSign", name = "평가금액Sign", length = 1)
        private String EstimateAmountSign;

        @MessageField(id = "EstimateAmount", name = "평가금액", length = 15)
        private BigDecimal EstimateAmount;

        @MessageField(id = "SumReciveRateSign", name = "누적수익률Sign", length = 1)
        private String SumReciveRateSign;

        @MessageField(id = "SumReciveRate", name = "누적수익률", length = 7)
        private BigDecimal SumReciveRate;

        @MessageField(id = "AliveAccountSign", name = "잔존좌수Sign", length = 1)
        private String AliveAccountSign;

        @MessageField(id = "AliveAccount", name = "잔존좌수", length = 15)
        private BigDecimal AliveAccount;

        @MessageField(id = "SaleStandardPriceSign", name = "매매기준가격Sign", length = 1)
        private String SaleStandardPriceSign;

        @MessageField(id = "SaleStandardPrice", name = "매매기준가격", length = 15)
        private BigDecimal SaleStandardPrice;

        @MessageField(id = "UmbrellaGroup", name = "엄블렐러펀드그룹", length = 2)
        private String UmbrellaGroup;

        @MessageField(id = "UmbrellaTransferYN", name = "전환가능BIT", length = 1)
        private String UmbrellaTransferYN;

        @MessageField(id = "UmbrellaCancelYN", name = "전환등록취소가능", length = 1)
        private String UmbrellaCancelYN;

        @MessageField(id = "UmbrellaAutoTransferYN", name = "자동전환등록취소가능", length = 1)
        private String UmbrellaAutoTransferYN;

        @MessageField(id = "YOPANYN", name = "판매사이동가능여부", length = 1)
        private String YOPANYN;

        @MessageField(id = "YOMANYN", name = "만기연장가능여부", length = 1)
        private String YOMANYN;

        @MessageField(id = "PaymentEstimateAmountSign", name = "기지급분평가금액Sign", length = 1)
        private String PaymentEstimateAmountSign;

        @MessageField(id = "PaymentEstimateAmount", name = "기지급분평가금액", length = 15)
        private BigDecimal PaymentEstimateAmount;

        @MessageField(id = "LoanBalanceSign", name = "대출잔액부호", length = 1)
        private String LoanBalanceSign;

        @MessageField(id = "LoanBalance", name = "대출잔액", length = 15)
        private BigDecimal LoanBalance;

        @MessageField(id = "LoanRepayPrinAmt", name = "승인한도", length = 15)
        private BigDecimal LoanRepayPrinAmt;

        @MessageField(id = "ExpectedDate", name = "이자예정일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String ExpectedDate;

        @MessageField(id = "LoanRate", name = "이율", length = 5)
        private BigDecimal LoanRate;

        @MessageField(id = "CardType", name = "카드종류", length = 1)
        private String CardType;

        @MessageField(id = "FamilyType", name = "본인가족구분", length = 1)
        private String FamilyType;

        @MessageField(id = "PaymentDate", name = "결제일자", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String PaymentDate;

        @MessageField(id = "OpenDate", name = "개설일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String OpenDate;

        @MessageField(id = "Period", name = "유효기간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String Period;

        @MessageField(id = "CheckCardType", name = "체크카드구분", length = 1)
        private String CheckCardType;

        @MessageField(id = "CoOperCardCode", name = "제휴카드코드", length = 6)
        private String CoOperCardCode;

        @MessageField(id = "AcctType", name = "계좌구분(1:예금, 2:펀드, 3:대출, 4:카드)", length = 1)
        private String AcctType;

        @MessageField(id = "LoanKind", name = "대출종류", length = 3)
        private String LoanKind;

        @MessageField(id = "LoanAcctKmCD", name = "계정과목코드", length = 6)
        private String LoanAcctKmCD;
    }
}
