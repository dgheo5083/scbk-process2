package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;
// import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocumentResult;
import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocument;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 신청내역 조회
 */
@Data
@IntegrationMessage(id = "SupDocListApplyResponse", type = Type.RESPONSE)
public class SupDocListApplyResponse implements IMessageObject {

	@MessageField(id = "applyDocList", name = "계약서류 조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AddCntrctDocument> applyDocList;

	@MessageField(id = "homepageUrl", name = "homepageUrl")
	private String homepageUrl;

	@MessageField(id = "pdfUrl", name = "pdfUrl")
	private String pdfUrl;

	@MessageField(id = "pdfUrlKyir", name = "pdfUrlKyir")
	private String pdfUrlKyir;

	@MessageField(id = "acctNum", name = "대출계좌번호")
	private String acctNum;

	@MessageField(id = "acctType", name = "acctType")
	private String acctType;

	@MessageField(id = "yoSGJAN", name = "yoSGJAN")
	private String yoSGJAN;

	@MessageField(id = "prdctExplnDoc1", name = "prdctExplnDoc1")
	private String prdctExplnDoc1;

	@MessageField(id = "cmmAgreeFileNm", name = "공통약관파일명")
	private String cmmAgreeFileNm; // 공통약관파일명

	@MessageField(id = "cmmAgreeNm", name = "공통약관명")
	private String cmmAgreeNm; // 공통약관명

	@MessageField(id = "cmmAgreeCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	private String cmmAgreeCntrctOfferCode; // 공통약관아이디계약서류제공여부

	@MessageField(id = "ctgryCmmAgreeFileNm", name = "카테고리공통약관파일명")
	private String ctgryCmmAgreeFileNm; // 카테고리공통약관파일명

	@MessageField(id = "ctgryCmmAgreeNm", name = "카테고리공통약관명")
	private String ctgryCmmAgreeNm; // 카테고리공통약관명

	@MessageField(id = "ctgryCmmAgreeCntrctOfferCode", name = "카테고리공통약관아이디계약서류제공여부")
	private String ctgryCmmAgreeCntrctOfferCode; // 카테고리공통약관아이디계약서류제공여부

	@MessageField(id = "prdctAgreeFileNm", name = "상품약관명파일명")
	private String prdctAgreeFileNm; // 상품약관명파일명

	@MessageField(id = "prdctAgreeNm", name = "상품약관명")
	private String prdctAgreeNm; // 상품약관명

	@MessageField(id = "prdctAgreeCntrctOfferCode", name = "상품약관아이디계약서류제공여부")
	private String prdctAgreeCntrctOfferCode; // 상품약관아이디계약서류제공여부

	@MessageField(id = "prdctExplnFileNm", name = "상품설명서파일명")
	private String prdctExplnFileNm; // 상품설명서파일명

	@MessageField(id = "prdctExplnNm", name = "상품설명서명")
	private String prdctExplnNm; // 상품설명서명

	@MessageField(id = "prdctExplnCntrctOfferCode", name = "상품설명서계약서류제공여부")
	private String prdctExplnCntrctOfferCode; // 상품설명서계약서류제공여부

	@MessageField(id = "balance", name = "대출잔액(전계좌 잔액으로 변경)")
	private String balance;

	@MessageField(id = "acctName", name = "대출계좌이름")
	private String acctName;

	@MessageField(id = "loanRepayPrinAmt", name = "승인한도")
	private String loanRepayPrinAmt;

	@MessageField(id = "savingStartDate", name = "신규일")
	private String savingStartDate;

	@MessageField(id = "loanKind", name = "대출종류")
	private String loanKind;

	@MessageField(id = "loanAcctKmCD", name = "계정과목코드")
	private String loanAcctKmCD;

	@MessageField(id = "assort", name = "종별코드")
	private String assort;

	@MessageField(id = "kwamok", name = "과목코드")
	private String kwamok;

	@MessageField(id = "cntrctFileList", name = "계약서류 조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AddCntrctDocument> cntrctFileList;

	// @Data
	// public class AddCntrctDocument implements IMessageObject {
	// @MessageField(id = "addCntrctFileNm", name = "공통약관파일명")
	// private String addCntrctFileNm; // 공통약관파일명
	// @MessageField(id = "addCntrctNm", name = "공통약관명")
	// private String addCntrctNm; // 공통약관명
	// @MessageField(id = "addCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	// private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
	// }

	@MessageField(id = "requestURLCheck", name = "requestURLCheck")
	private String requestURLCheck;

	@MessageField(id = "edMSQueryFlag", name = "eDMSQueryFlag")
	private String edMSQueryFlag;

	@MessageField(id = "edMSContractList", name = "계약서류 조회")
	private JSONArray edMSContractList;
	/*
	 * @MessageField(id = "responseH381Map", name = "responseH381Map")
	 * private ResponseH381 responseH381Map;
	 * 
	 * @MessageField(id = "responseH381Json", name = "responseH381Json")
	 * private String responseH381Json;
	 * 
	 * @Data
	 * public static class ResponseH381 implements IMessageObject {
	 * 
	 * @MessageField(id = "YOGUBUN", name =
	 * "상품구분 1-담보, 2-신용, 3-BB, 4-예펀담대 5-수담대, 6-카드론")
	 * private String YOGUBUN;
	 * 
	 * @MessageField(id = "YONAME", name = "고객명")
	 * private String YONAME;
	 * 
	 * @MessageField(id = "YOJUMIN", name = "생년월일")
	 * private String YOJUMIN;
	 * 
	 * @MessageField(id = "YOGYECD", name = "계정코드")
	 * private String YOGYECD;
	 * 
	 * @MessageField(id = "YOJAGUM", name = "대출종류")
	 * private String YOJAGUM;
	 * 
	 * @MessageField(id = "YODAEGB", name = "대출구분")
	 * private String YODAEGB;
	 * 
	 * @MessageField(id = "YOFSTIL", name = "신규일자")
	 * private Integer YOFSTIL;
	 * 
	 * @MessageField(id = "YOLASGI", name = "만기일자")
	 * private Integer YOLASGI;
	 * 
	 * @MessageField(id = "YOSNAMT", name = "대출(한도)금액")
	 * private BigDecimal YOSNAMT;
	 * 
	 * @MessageField(id = "YOSPGB", name = "특약대출 코드")
	 * private String YOSPGB;
	 * 
	 * @MessageField(id = "YOGYENM", name = "대출과목")
	 * private String YOGYENM;
	 * 
	 * @MessageField(id = "YOGEOHT", name = "거래형태 1-개별,2-한도")
	 * private String YOGEOHT;
	 * 
	 * @MessageField(id = "YOIYGBN", name = "기준금리명")
	 * private String YOIYGBN;
	 * 
	 * @MessageField(id = "YORPWOL", name = "이율변동월수")
	 * private Integer YORPWOL;
	 * 
	 * @MessageField(id = "YOJYIYL", name = "적용금리(2.53%->25300)")
	 * private Integer YOJYIYL;
	 * 
	 * @MessageField(id = "YOGJIYL", name = "기준금리(2.53%->25300)")
	 * private Integer YOGJIYL;
	 * 
	 * @MessageField(id = "YOGASAN", name = "가산금리(2.53%->25300)")
	 * private Integer YOGASAN;
	 * 
	 * @MessageField(id = "YOUDIY01", name = "우대금리 : 우량고객")
	 * private Integer YOUDIY01;
	 * 
	 * @MessageField(id = "YOUDIY02", name = "우대금리 : 연동우대(급여이체)")
	 * private Integer YOUDIY02;
	 * 
	 * @MessageField(id = "YOUDIY03", name = "우대금리 : 소득증빙(Full doc)")
	 * private Integer YOUDIY03;
	 * 
	 * @MessageField(id = "YOUDIY04", name = "우대금리 : 연동우대(카드사용)")
	 * private Integer YOUDIY04;
	 * 
	 * @MessageField(id = "YOUDIY05", name = "우대금리 : 장애인")
	 * private Integer YOUDIY05;
	 * 
	 * @MessageField(id = "YOUDIY06", name = "우대금리 : ３자녀이상")
	 * private Integer YOUDIY06;
	 * 
	 * @MessageField(id = "YOUDIY07", name = "우대금리 : 다문화가정")
	 * private Integer YOUDIY07;
	 * 
	 * @MessageField(id = "YOUDIY08", name = "우대금리 : 영업점장전결")
	 * private Integer YOUDIY08;
	 * 
	 * @MessageField(id = "YOUDIY09", name = "우대금리 : 본부우대전결")
	 * private Integer YOUDIY09;
	 * 
	 * @MessageField(id = "YOUDIY10", name = "우대금리 : 기타")
	 * private Integer YOUDIY10;
	 * 
	 * @MessageField(id = "YOUDIY11", name = "우대금리 : 서민지원")
	 * private Integer YOUDIY11;
	 * 
	 * @MessageField(id = "YOUDIY12", name = "우대금리 : COFIX")
	 * private Integer YOUDIY12;
	 * 
	 * @MessageField(id = "YOUDIY13", name = "우대금리 : 고정금리")
	 * private Integer YOUDIY13;
	 * 
	 * @MessageField(id = "YOUDIY14", name = "우대금리 : 우량/전문직고객")
	 * private Integer YOUDIY14;
	 * 
	 * @MessageField(id = "YOUDIY15", name = "우대금리 : 원리균등")
	 * private Integer YOUDIY15;
	 * 
	 * @MessageField(id = "YOUDIY16", name = "우대금리 : 1금융권부채통합")
	 * private Integer YOUDIY16;
	 * 
	 * @MessageField(id = "YOHDUDY1", name = "한도사용우대1 40%")
	 * private Integer YOHDUDY1;
	 * 
	 * @MessageField(id = "YOHDUDY2", name = "한도사용우대2 60%")
	 * private Integer YOHDUDY2;
	 * 
	 * @MessageField(id = "YOHDUDY3", name = "한도사용우대3 80%")
	 * private Integer YOHDUDY3;
	 * 
	 * @MessageField(id = "YOICHIY", name = "이차보전")
	 * private Integer YOICHIY;
	 * 
	 * @MessageField(id = "YOICHRI", name = "이차보전만기일")
	 * private Integer YOICHRI;
	 * 
	 * @MessageField(id = "YOSUIY1", name = "중도상환수수료 요율1(1.2%->1200)")
	 * private Integer YOSUIY1;
	 * 
	 * @MessageField(id = "YOSUIL1", name = "중도상환수수료 기간1(년단위)")
	 * private Integer YOSUIL1;
	 * 
	 * @MessageField(id = "YOSUIY2", name = "중도상환수수료 요율2")
	 * private Integer YOSUIY2;
	 * 
	 * @MessageField(id = "YOSUIL2", name = "중도상환수수료 기간2(년단위)")
	 * private Integer YOSUIL2;
	 * 
	 * @MessageField(id = "YOSUIY3", name = "중도상환수수료 요율3")
	 * private Integer YOSUIY3;
	 * 
	 * @MessageField(id = "YOSUIL3", name = "중도상환수수료 기간3(년단위)")
	 * private Integer YOSUIL3;
	 * 
	 * @MessageField(id = "YOSUGIJ", name = "중도상환부과기준일")
	 * private Integer YOSUGIJ;
	 * 
	 * @MessageField(id = "YOMJEYN", name = "상환수수료면제여부(10%면제여부 Y:면제)")
	 * private String YOMJEYN;
	 * 
	 * @MessageField(id = "YOYDYN", name = "채권양도동의(특약) Y:채권양도")
	 * private String YOYDYN;
	 * 
	 * @MessageField(id = "YOCENIL", name = "자동이체일 DD")
	 * private Integer YOCENIL;
	 * 
	 * @MessageField(id = "YOICBUN", name = "자동이체계좌번호")
	 * private String YOICBUN;
	 * 
	 * @MessageField(id = "YOSHCD", name =
	 * "상환방법 1:일시,2:분할,3:원리,4:거치식원리균등,A:원리+전세보증론(부분원리균등)")
	 * private String YOSHCD;
	 * 
	 * @MessageField(id = "YOGEOCI", name = "거치기일")
	 * private Integer YOGEOCI;
	 * 
	 * @MessageField(id = "YOSMSGB", name = "대출이자통지서비스 1:SMS, 2:EMAIL 3:SMS&EMAIL")
	 * private String YOSMSGB;
	 * 
	 * @MessageField(id = "YOTOAMT", name = "총원리금 수수료부담예상액")
	 * private BigDecimal YOTOAMT;
	 * 
	 * @MessageField(id = "YOSJAMT", name = "채권최고액(설정액)")
	 * private BigDecimal YOSJAMT;
	 * 
	 * @MessageField(id = "YOIYDIS", name = "금리인하요구권 대상여부 Y")
	 * private String YOIYDIS;
	 * 
	 * @MessageField(id = "YOHYUIL", name = "휴일대출상환대상여부 Y")
	 * private String YOHYUIL;
	 * 
	 * @MessageField(id = "YOYUNIY", name = "지연배상금율(2.53% -> 25300)")
	 * private Integer YOYUNIY;
	 * 
	 * @MessageField(id = "YOSHGBN", name =
	 * "여신실행방법 1:여신개시일 전액실행,2:증빙서류나 현금등에 의하여 은행이 자금용도 필요금액을 확인하고 분할 실행,3:일정한요건을 갖춘 청구가 있는대로 실행,4:해당사항 없음"
	 * )
	 * private String YOSHGBN;
	 * 
	 * @MessageField(id = "YOISUGK", name = "이수간격")
	 * private Integer YOISUGK;
	 * 
	 * @MessageField(id = "YOISUCD", name = "이자수입방법 1:선취  2:후취")
	 * private String YOISUCD;
	 * 
	 * @MessageField(id = "YOILBAN", name = "일반금융소비자대상여부 Y")
	 * private String YOILBAN;
	 * 
	 * @MessageField(id = "YOSDJNM", name = "상담자 성명")
	 * private String YOSDJNM;
	 * 
	 * @MessageField(id = "YOIYFIX", name = "금리적용방식 1:변동  2:고정")
	 * private String YOIYFIX;
	 * 
	 * @MessageField(id = "YOSHGG", name = "상환기간(실행일～만기일 월수")
	 * private Integer YOSHGG;
	 * 
	 * @MessageField(id = "YOISUYI", name = "상환일（이수약정일）")
	 * private Integer YOISUYI;
	 * 
	 * @MessageField(id = "YOBUGUM", name = "원리금상환액")
	 * private BigDecimal YOBUGUM;
	 * 
	 * @MessageField(id = "YOCHAIL", name = "최초상환일(차회이자일）")
	 * private Integer YOCHAIL;
	 * 
	 * @MessageField(id = "YOBJSYU", name = "모기지보험가입여부 2:MCI   3:MI")
	 * private String YOBJSYU;
	 * 
	 * @MessageField(id = "YODCJAN", name = "대출잔액")
	 * private BigDecimal YODCJAN;
	 * 
	 * @MessageField(id = "YOPAYJR", name = "급여이체연동우대종류")
	 * private String YOPAYJR;
	 * 
	 * @MessageField(id = "YORVKYN", name = "대출계약철회권 Y:대상")
	 * private String YORVKYN;
	 * 
	 * @MessageField(id = "YOSSUDY", name = "성실상환우대이자율 Y:문구출력대상")
	 * private String YOSSUDY;
	 * 
	 * @MessageField(id = "YOSUAMT", name = "인지세")
	 * private BigDecimal YOSUAMT;
	 * 
	 * @MessageField(id = "YOINSU", name = "채무인수 여부 Y:거래유")
	 * private String YOINSU;
	 * 
	 * @MessageField(id = "YOJNDAE", name = "증대여부 Y:거래유")
	 * private String YOJNDAE;
	 * 
	 * @MessageField(id = "YOBHIYL", name = "보증료율(2.53%->25300)")
	 * private Integer YOBHIYL;
	 * 
	 * @MessageField(id = "YOBKIYL", name = "은행금리(2.53%->25300)")
	 * private Integer YOBKIYL;
	 * 
	 * @MessageField(id = "YOSHBY", name = "원리금상환비율")
	 * private Integer YOSHBY;
	 * 
	 * @MessageField(id = "YOPPWON", name = "부분상환원금")
	 * private BigDecimal YOPPWON;
	 * 
	 * @MessageField(id = "YOJHYN", name = "실행일부터３１일날전후 조회구분 Y/N ")
	 * private String YOJHYN;
	 * 
	 * @MessageField(id = "YOMOAYL", name = "모아우대금리(2.53%->25300)")
	 * private Integer YOMOAYL;
	 * 
	 * @MessageField(id = "YOGEYAK", name = "계약서류구분 1:서면 2:이메일 3:문자메세지")
	 * private String YOGEYAK;
	 * 
	 * @MessageField(id = "YOMOVCD", name = "대출이동신규 1:소상공인저금리 2:신용대출 3:주택대출 4:전세대출")
	 * private String YOMOVCD;
	 * 
	 * @MessageField(id = "YOBPYN", name = "방문판매여부")
	 * private String YOBPYN;
	 * 
	 * @MessageField(id = "YODUMMY", name = "더미")
	 * private String YODUMMY;
	 * 
	 * @MessageField(id = "YOSUCNT", name = "질권담보계좌수")
	 * private Integer YOSUCNT;
	 * 
	 * @MessageField(id = "YOSUTBL", name = "담보계좌")
	 * 
	 * @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount =
	 * "ResponseH381/YOSUCNT")
	 * private List<YOSUTBL> YOSUTBL;
	 * 
	 * }
	 */
	@MessageField(id = "yoSUTBL", name = "담보계좌")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "SupDocListApplyResponse/yoSUCNT")
	private List<YOSUTBL> yoSUTBL;

	@Data
	public static class YOSUTBL implements IMessageObject {

		@MessageField(id = "yoSUGEJ", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String yoSUGEJ;

		@MessageField(id = "yoSUNAM", name = "계좌주성명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
		private String yoSUNAM;
	}

	// @MessageField(id = "yoSUTBL", name = "yoSUTBL")
	// private String yoSUTBL;
	@MessageField(id = "yoGUBUN", name = "상품구분 1-담보, 2-신용, 3-BB, 4-예펀담대 5-수담대, 6-카드론")
	private String yoGUBUN;

	@MessageField(id = "yoNAME", name = "고객명")
	private String yoNAME;

	@MessageField(id = "yoJUMIN", name = "생년월일")
	private String yoJUMIN;

	@MessageField(id = "yoGYECD", name = "계정코드")
	private String yoGYECD;

	@MessageField(id = "yoJAGUM", name = "대출종류")
	private String yoJAGUM;

	@MessageField(id = "yoDAEGB", name = "대출구분")
	private String yoDAEGB;

	@MessageField(id = "yoFSTIL", name = "신규일자")
	private Integer yoFSTIL;

	@MessageField(id = "yoLASGI", name = "만기일자")
	private Integer yoLASGI;

	@MessageField(id = "yoSNAMT", name = "대출(한도)금액")
	private BigDecimal yoSNAMT;

	@MessageField(id = "yoSPGB", name = "특약대출 정보 1-특약대출")
	private String yoSPGB;

	@MessageField(id = "yoSPCOD", name = "특약대출 코드")
	private String yoSPCOD;

	@MessageField(id = "yoGYENM", name = "대출과목")
	private String yoGYENM;

	@MessageField(id = "yoGEOHT", name = "거래형태 1-개별,2-한도")
	private String yoGEOHT;

	@MessageField(id = "yoIYGBN", name = "기준금리명")
	private String yoIYGBN;

	@MessageField(id = "yoRPWOL", name = "이율변동월수")
	private Integer yoRPWOL;

	@MessageField(id = "yoJYIYL", name = "적용금리(2.53%->25300)")
	private Integer yoJYIYL;

	@MessageField(id = "yoGJIYL", name = "기준금리(2.53%->25300)")
	private Integer yoGJIYL;

	@MessageField(id = "yoGASAN", name = "가산금리(2.53%->25300)")
	private Integer yoGASAN;

	@MessageField(id = "yoUDIY01", name = "우대금리 : 우량고객")
	private Integer yoUDIY01;

	@MessageField(id = "yoUDIY02", name = "우대금리 : 연동우대(급여이체)")
	private Integer yoUDIY02;

	@MessageField(id = "yoUDIY03", name = "우대금리 : 소득증빙(Full doc)")
	private Integer yoUDIY03;

	@MessageField(id = "yoUDIY04", name = "우대금리 : 연동우대(카드사용)")
	private Integer yoUDIY04;

	@MessageField(id = "yoUDIY05", name = "우대금리 : 장애인")
	private Integer yoUDIY05;

	@MessageField(id = "yoUDIY06", name = "우대금리 : ３자녀이상")
	private Integer yoUDIY06;

	@MessageField(id = "yoUDIY07", name = "우대금리 : 다문화가정")
	private Integer yoUDIY07;

	@MessageField(id = "yoUDIY08", name = "우대금리 : 영업점장전결")
	private Integer yoUDIY08;

	@MessageField(id = "yoUDIY09", name = "우대금리 : 본부우대전결")
	private Integer yoUDIY09;

	@MessageField(id = "yoUDIY10", name = "우대금리 : 기타")
	private Integer yoUDIY10;

	@MessageField(id = "yoUDIY11", name = "우대금리 : 서민지원")
	private Integer yoUDIY11;

	@MessageField(id = "yoUDIY12", name = "우대금리 : COFIX")
	private Integer yoUDIY12;

	@MessageField(id = "yoUDIY13", name = "우대금리 : 고정금리")
	private Integer yoUDIY13;

	@MessageField(id = "yoUDIY14", name = "우대금리 : 우량/전문직고객")
	private Integer yoUDIY14;

	@MessageField(id = "yoUDIY15", name = "우대금리 : 원리균등")
	private Integer yoUDIY15;

	@MessageField(id = "yoUDIY16", name = "우대금리 : 1금융권부채통합")
	private Integer yoUDIY16;

	@MessageField(id = "yoHDUDY1", name = "한도사용우대1 40%")
	private Integer yoHDUDY1;

	@MessageField(id = "yoHDUDY2", name = "한도사용우대2 60%")
	private Integer yoHDUDY2;

	@MessageField(id = "yoHDUDY3", name = "한도사용우대3 80%")
	private Integer yoHDUDY3;

	@MessageField(id = "yoICHIY", name = "이차보전")
	private Integer yoICHIY;

	@MessageField(id = "yoICHRI", name = "이차보전만기일")
	private Integer yoICHRI;

	@MessageField(id = "yoSUIY1", name = "중도상환수수료 요율1(1.2%->1200)")
	private Integer yoSUIY1;

	@MessageField(id = "yoSUIL1", name = "중도상환수수료 기간1(년단위)")
	private Integer yoSUIL1;

	@MessageField(id = "yoSUIY2", name = "중도상환수수료 요율2")
	private Integer yoSUIY2;

	@MessageField(id = "yoSUIL2", name = "중도상환수수료 기간2(년단위)")
	private Integer yoSUIL2;

	@MessageField(id = "yoSUIY3", name = "중도상환수수료 요율3")
	private Integer yoSUIY3;

	@MessageField(id = "yoSUIL3", name = "중도상환수수료 기간3(년단위)")
	private Integer yoSUIL3;

	@MessageField(id = "yoSUGIJ", name = "중도상환부과기준일")
	private Integer yoSUGIJ;

	@MessageField(id = "yoMJEYN", name = "상환수수료면제여부(10%면제여부 Y:면제)")
	private String yoMJEYN;

	@MessageField(id = "yoYDYN", name = "채권양도동의(특약) Y:채권양도")
	private String yoYDYN;

	@MessageField(id = "yoCENIL", name = "자동이체일 DD")
	private Integer yoCENIL;

	@MessageField(id = "yoICBUN", name = "자동이체계좌번호")
	private String yoICBUN;

	@MessageField(id = "yoSHCD", name = "상환방법 1:일시,2:분할,3:원리,4:거치식원리균등,A:원리+전세보증론(부분원리균등)")
	private String yoSHCD;

	@MessageField(id = "yoGEOCI", name = "거치기일")
	private Integer yoGEOCI;

	@MessageField(id = "yoSMSGB", name = "대출이자통지서비스 1:SMS, 2:EMAIL 3:SMS&EMAIL")
	private String yoSMSGB;

	@MessageField(id = "yoTOAMT", name = "총원리금 수수료부담예상액")
	private BigDecimal yoTOAMT;

	@MessageField(id = "yoSJAMT", name = "채권최고액(설정액)")
	private BigDecimal yoSJAMT;

	@MessageField(id = "yoIYDIS", name = "금리인하요구권 대상여부 Y")
	private String yoIYDIS;

	@MessageField(id = "yoHYUIL", name = "휴일대출상환대상여부 Y")
	private String yoHYUIL;

	@MessageField(id = "yoYUNIY", name = "지연배상금율(2.53% -> 25300)")
	private Integer yoYUNIY;

	@MessageField(id = "yoSHGBN", name = "여신실행방법 1:여신개시일 전액실행,2:증빙서류나 현금등에 의하여 은행이 자금용도 필요금액을 확인하고 분할 실행,3:일정한요건을 갖춘 청구가 있는대로 실행,4:해당사항 없음")
	private String yoSHGBN;

	@MessageField(id = "yoISUGK", name = "이수간격")
	private Integer yoISUGK;

	@MessageField(id = "yoISUCD", name = "이자수입방법 1:선취  2:후취")
	private String yoISUCD;

	@MessageField(id = "yoILBAN", name = "일반금융소비자대상여부 Y")
	private String yoILBAN;

	@MessageField(id = "yoSDJNM", name = "상담자 성명")
	private String yoSDJNM;

	@MessageField(id = "yoIYFIX", name = "금리적용방식 1:변동  2:고정")
	private String yoIYFIX;

	@MessageField(id = "yoSHGG", name = "상환기간(실행일～만기일 월수")
	private Integer yoSHGG;

	@MessageField(id = "yoISUYI", name = "상환일（이수약정일）")
	private Integer yoISUYI;

	@MessageField(id = "yoBUGUM", name = "원리금상환액")
	private BigDecimal yoBUGUM;

	@MessageField(id = "yoCHAIL", name = "최초상환일(차회이자일）")
	private Integer yoCHAIL;

	@MessageField(id = "yoBJSYU", name = "모기지보험가입여부 2:MCI   3:MI")
	private String yoBJSYU;

	@MessageField(id = "yoDCJAN", name = "대출잔액")
	private BigDecimal yoDCJAN;

	@MessageField(id = "yoPAYJR", name = "급여이체연동우대종류")
	private String yoPAYJR;

	@MessageField(id = "yoRVKYN", name = "대출계약철회권 Y:대상")
	private String yoRVKYN;

	@MessageField(id = "yoSSUDY", name = "성실상환우대이자율 Y:문구출력대상")
	private String yoSSUDY;

	@MessageField(id = "yoSUAMT", name = "인지세")
	private BigDecimal yoSUAMT;

	@MessageField(id = "yoINSU", name = "채무인수 여부 Y:거래유")
	private String yoINSU;

	@MessageField(id = "yoJNDAE", name = "증대여부 Y:거래유")
	private String yoJNDAE;

	@MessageField(id = "yoBHIYL", name = "보증료율(2.53%->25300)")
	private Integer yoBHIYL;

	@MessageField(id = "yoBKIYL", name = "은행금리(2.53%->25300)")
	private Integer yoBKIYL;

	@MessageField(id = "yoSHBY", name = "원리금상환비율")
	private Integer yoSHBY;

	@MessageField(id = "yoPPWON", name = "부분상환원금")
	private BigDecimal yoPPWON;

	@MessageField(id = "yoJHYN", name = "실행일부터３１일날전후 조회구분 Y/N ")
	private String yoJHYN;

	@MessageField(id = "yoMOAYL", name = "모아우대금리(2.53%->25300)")
	private Integer yoMOAYL;

	@MessageField(id = "yoGEYAK", name = "계약서류구분 1:서면 2:이메일 3:문자메세지")
	private String yoGEYAK;

	@MessageField(id = "yoMOVCD", name = "대출이동신규 1:소상공인저금리 2:신용대출 3:주택대출 4:전세대출")
	private String yoMOVCD;

	@MessageField(id = "yoBPYN", name = "방문판매여부")
	private String yoBPYN;

	@MessageField(id = "yoDUMMY", name = "더미")
	private String yoDUMMY;

	@MessageField(id = "yoSUCNT", name = "질권담보계좌수")
	private Integer yoSUCNT;

	@MessageField(id = "errorCode")
	private String errorCode;

	@MessageField(id = "errorMessage")
	private String errorMessage;

	@MessageField(id = "errorModule")
	private String errorModule;

	@MessageField(id = "perBusNo", name = "주민등록번호")
	private String perBusNo;
}
