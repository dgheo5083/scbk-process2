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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H38100Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "계약서류조회(여신)")
public class CbIbk01H38100Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGUBUN", name = "상품구분 1-담보, 2-신용, 3-BB, 4-예펀담대 5-수담대, 6-카드론", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBUN;

    @MessageField(id = "YONAME", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOJUMIN", name = "생년월일", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMIN;

    @MessageField(id = "YOGYECD", name = "계정코드", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGYECD;

    @MessageField(id = "YOJAGUM", name = "대출종류", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJAGUM;

    @MessageField(id = "YODAEGB", name = "대출구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODAEGB;

    @MessageField(id = "YOFSTIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOFSTIL;

    @MessageField(id = "YOLASGI", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOLASGI;

    @MessageField(id = "YOSNAMT", name = "대출(한도)금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSNAMT;

    @MessageField(id = "YOSPGB", name = "특약대출 정보 1-특약대출", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSPGB;

    @MessageField(id = "YOSPCOD", name = "특약대출 코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSPCOD;

    @MessageField(id = "YOGYENM", name = "대출과목", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGYENM;

    @MessageField(id = "YOGEOHT", name = "거래형태 1-개별,2-한도", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGEOHT;

    @MessageField(id = "YOIYGBN", name = "기준금리명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIYGBN;

    @MessageField(id = "YORPWOL", name = "이율변동월수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YORPWOL;

    @MessageField(id = "YOJYIYL", name = "적용금리(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOJYIYL;

    @MessageField(id = "YOGJIYL", name = "기준금리(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGJIYL;

    @MessageField(id = "YOGASAN", name = "가산금리(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGASAN;

    @MessageField(id = "YOUDIY01", name = "우대금리 : 우량고객", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY01;

    @MessageField(id = "YOUDIY02", name = "우대금리 : 연동우대(급여이체)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY02;

    @MessageField(id = "YOUDIY03", name = "우대금리 : 소득증빙(Full doc)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY03;

    @MessageField(id = "YOUDIY04", name = "우대금리 : 연동우대(카드사용)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY04;

    @MessageField(id = "YOUDIY05", name = "우대금리 : 장애인", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY05;

    @MessageField(id = "YOUDIY06", name = "우대금리 : ３자녀이상", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY06;

    @MessageField(id = "YOUDIY07", name = "우대금리 : 다문화가정", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY07;

    @MessageField(id = "YOUDIY08", name = "우대금리 : 영업점장전결", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY08;

    @MessageField(id = "YOUDIY09", name = "우대금리 : 본부우대전결", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY09;

    @MessageField(id = "YOUDIY10", name = "우대금리 : 기타", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY10;

    @MessageField(id = "YOUDIY11", name = "우대금리 : 서민지원", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY11;

    @MessageField(id = "YOUDIY12", name = "우대금리 : COFIX", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY12;

    @MessageField(id = "YOUDIY13", name = "우대금리 : 고정금리", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY13;

    @MessageField(id = "YOUDIY14", name = "우대금리 : 우량/전문직고객", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY14;

    @MessageField(id = "YOUDIY15", name = "우대금리 : 원리균등", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY15;

    @MessageField(id = "YOUDIY16", name = "우대금리 : 1금융권부채통합", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOUDIY16;

    @MessageField(id = "YOHDUDY1", name = "한도사용우대１   40%", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOHDUDY1;

    @MessageField(id = "YOHDUDY2", name = "한도사용우대2   60%", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOHDUDY2;

    @MessageField(id = "YOHDUDY3", name = "한도사용우대3   80%", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOHDUDY3;

    @MessageField(id = "YOICHIY", name = "이차보전", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOICHIY;

    @MessageField(id = "YOICHRI", name = "이차보전만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOICHRI;

    @MessageField(id = "YOSUIY1", name = "중도상환수수료 요율1(1.2%->1200)", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIY1;

    @MessageField(id = "YOSUIL1", name = "중도상환수수료 기간1(년단위)", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIL1;

    @MessageField(id = "YOSUIY2", name = "중도상환수수료 요율2", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIY2;

    @MessageField(id = "YOSUIL2", name = "중도상환수수료 기간2(년단위)", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIL2;

    @MessageField(id = "YOSUIY3", name = "중도상환수수료 요율3", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIY3;

    @MessageField(id = "YOSUIL3", name = "중도상환수수료 기간3(년단위)", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUIL3;

    @MessageField(id = "YOSUGIJ", name = "중도상환부과기준일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUGIJ;

    @MessageField(id = "YOMJEYN", name = "상환수수료면제여부(10%면제여부 Y:면제)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMJEYN;

    @MessageField(id = "YOYDYN", name = "채권양도동의(특약) Y:채권양도", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYDYN;

    @MessageField(id = "YOCENIL", name = "자동이체일 DD", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOCENIL;

    @MessageField(id = "YOICBUN", name = "자동이체계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOICBUN;

    @MessageField(id = "YOSHCD", name = "상환방법 1:일시,2:분할,3:원리,4:거치식원리균등,A:원리+전세보증론(부분원리균등)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSHCD;

    @MessageField(id = "YOGEOCI", name = "거치기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGEOCI;

    @MessageField(id = "YOSMSGB", name = "대출이자통지서비스 1:SMS, 2:EMAIL 3:SMS&EMAIL", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSMSGB;

    @MessageField(id = "YOTOAMT", name = "총원리금 수수료부담예상액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTOAMT;

    @MessageField(id = "YOSJAMT", name = "채권최고액(설정액)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSJAMT;

    @MessageField(id = "YOIYDIS", name = "금리인하요구권 대상여부 Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIYDIS;

    @MessageField(id = "YOHYUIL", name = "휴일대출상환대상여부 Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHYUIL;

    @MessageField(id = "YOYUNIY", name = "지연배상금율(2.53% -> 25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOYUNIY;

    @MessageField(id = "YOSHGBN", name = "여신실행방법 1:여신개시일 전액실행,2:증빙서류나 현금등에 의하여 은행이 자금용도 필요금액을 확인하고 분할 실행,3:일정한요건을 갖춘 청구가 있는대로 실행,4:해당사항 없음", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSHGBN;

    @MessageField(id = "YOISUGK", name = "이수간격", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOISUGK;

    @MessageField(id = "YOISUCD", name = "이자수입방법 1:선취  2:후취", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOISUCD;

    @MessageField(id = "YOILBAN", name = "일반금융소비자대상여부 Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOILBAN;

    @MessageField(id = "YOSDJNM", name = "상담자 성명", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSDJNM;

    @MessageField(id = "YOIYFIX", name = "금리적용방식 1:변동  2:고정", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIYFIX;

    @MessageField(id = "YOSHGG", name = "상환기간(실행일～만기일　월수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSHGG;

    @MessageField(id = "YOISUYI", name = "상환일（이수약정일）", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOISUYI;

    @MessageField(id = "YOBUGUM", name = "원리금상환액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBUGUM;

    @MessageField(id = "YOCHAIL", name = "최초상환일(차회이자일）", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOCHAIL;

    @MessageField(id = "YOBJSYU", name = "모기지보험가입여부 2:MCI   3:MI", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBJSYU;

    @MessageField(id = "YODCJAN", name = "대출잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YODCJAN;

    @MessageField(id = "YOPAYJR", name = "급여이체연동우대종류", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPAYJR;

    @MessageField(id = "YORVKYN", name = "대출계약철회권 Y:대상", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YORVKYN;

    @MessageField(id = "YOSSUDY", name = "성실상환우대이자율 Y:문구출력대상", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSSUDY;

    @MessageField(id = "YOSUAMT", name = "인지세", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSUAMT;

    @MessageField(id = "YOINSU", name = "채무인수　여부 Y:거래유", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOINSU;

    @MessageField(id = "YOJNDAE", name = "증대여부 Y:거래유", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJNDAE;

    @MessageField(id = "YOBHIYL", name = "보증료율(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOBHIYL;

    @MessageField(id = "YOBKIYL", name = "은행금리(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOBKIYL;

    @MessageField(id = "YOSHBY", name = "원리금상환비율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSHBY;

    @MessageField(id = "YOPPWON", name = "부분상환원금", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOPPWON;

    @MessageField(id = "YOJHYN", name = "실행일부터３１일날전후 조회구분 Y/N ", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJHYN;

    @MessageField(id = "YOMOAYL", name = "모아우대금리(2.53%->25300)", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMOAYL;

    @MessageField(id = "YOGEYAK", name = "계약서류구분 1:서면 2:이메일 3:문자메세지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGEYAK;

    @MessageField(id = "YOMOVCD", name = "대출이동신규 1:소상공인저금리 2:신용대출 3:주택대출 4:전세대출", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMOVCD;

    @MessageField(id = "YOBPYN", name = "방문판매여부", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOBPYN;

    @MessageField(id = "YODUMMY", name = "더미", length = 57, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

    @MessageField(id = "YOSUCNT", name = "질권담보계좌수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSUCNT;

    @MessageField(id = "YOSUTBL", name = "담보계좌")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H38100Res/YOSUCNT")
    private List<YOSUTBL> YOSUTBL;

    @Data
    public static class YOSUTBL implements IMessageObject {

        @MessageField(id = "YOSUGEJ", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSUGEJ;

        @MessageField(id = "YOSUNAM", name = "계좌주성명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOSUNAM;
    }
}