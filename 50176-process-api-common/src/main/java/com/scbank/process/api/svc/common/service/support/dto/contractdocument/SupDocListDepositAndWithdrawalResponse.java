package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.AddCntrctDocument;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 계약서류제공 계좌목록 조회
 */
@Data
@IntegrationMessage(id = "SupDocListDepositAndWithdrawalResponse", type = Type.RESPONSE)
public class SupDocListDepositAndWithdrawalResponse implements IMessageObject {

	@MessageField(id = "depsitDocList", name = "계약서류 조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AddCntrctDocument> depsitDocList;

	@MessageField(id = "homepageUrl", name = "homepageUrl")
	private String homepageUrl;

	@MessageField(id = "pdfUrl", name = "pdfUrl")
	private String pdfUrl;

	@MessageField(id = "pdfUrlKyir", name = "pdfUrlKyir")
	private String pdfUrlKyir;

	@MessageField(id = "acctNum", name = "acctNum")
	private String acctNum;

	@MessageField(id = "acctType", name = "acctType")
	private String acctType;

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

	// @Data
	// public class AddCntrctDocument implements IMessageObject {
	// @MessageField(id = "addCntrctFileNm", name = "공통약관파일명")
	// private String addCntrctFileNm; // 공통약관파일명
	// @MessageField(id = "addCntrctNm", name = "공통약관명")
	// private String addCntrctNm; // 공통약관명
	// @MessageField(id = "addCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	// private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
	// }

	@MessageField(id = "yoBRNO", name = "점")
	private String yoBRNO;

	@MessageField(id = "yoKMCD", name = "과목")
	private String yoKMCD;

	@MessageField(id = "yoBUNHO", name = "계좌번호")
	private String yoBUNHO;

	@MessageField(id = "yoSINIL", name = "신규일자")
	private Integer yoSINIL;

	@MessageField(id = "yoGRGB", name = "거래구분 10-신규, 11-신규취소, 20-해약, 21-해약취소, 30-전출입, 31-전출입취소")
	private String yoGRGB;

	@MessageField(id = "yoLASIL", name = "최종갱신일자")
	private Integer yoLASIL;

	@MessageField(id = "yoDOCGB", name = "계약서류제공방법 1-서면(계좌조회서비스), 2-이메일, 3-문자메세지")
	private String yoDOCGB;

	@MessageField(id = "yoHPNO", name = "발송휴대폰번호")
	private String yoHPNO;

	@MessageField(id = "yoEMAIL", name = "발송이메일주소")
	private String yoEMAIL;

	@MessageField(id = "yoZONG", name = "종별")
	private String yoZONG;

	@MessageField(id = "yoPRDNM", name = "상품명")
	private String yoPRDNM;

	@MessageField(id = "yoCUSNM", name = "고객명")
	private String yoCUSNM;

	@MessageField(id = "yoSGJAN", name = "신규금액")
	private BigDecimal yoSGJAN;

	@MessageField(id = "yoGAIPJA", name = "가입자구분")
	private String yoGAIPJA;

	@MessageField(id = "yoSWHAN", name = "비과세한도")
	private BigDecimal yoSWHAN;

	@MessageField(id = "yoTBGJL", name = "예금잔액조회서")
	private String yoTBGJL;

	@MessageField(id = "yoBSJUM", name = "계좌관리점")
	private Integer yoBSJUM;

	@MessageField(id = "yoMANIL", name = "만기일자")
	private Integer yoMANIL;

	@MessageField(id = "yoIYUL", name = "약정이율")
	private Integer yoIYUL;

	@MessageField(id = "yoMANGJ", name = "만기자동입금계좌")
	private String yoMANGJ;

	@MessageField(id = "yoMANTJ", name = "만기통지SMS")
	private String yoMANTJ;

	@MessageField(id = "yoIJAGB", name = "이자지급방법")
	private String yoIJAGB;

	@MessageField(id = "yoJYCGB", name = "만기재예치신청")
	private String yoJYCGB;

	@MessageField(id = "yoGAYAK", name = "계약금액")
	private BigDecimal yoGAYAK;

	@MessageField(id = "yoSAVGB", name = "적립방법")
	private String yoSAVGB;

	@MessageField(id = "yoJYTJ", name = "납입지연SMS")
	private String yoJYTJ;

	@MessageField(id = "yoITDC", name = "IB담보대출신청")
	private String yoITDC;

	@MessageField(id = "yoURL", name = "URL고유번호")
	private String yoURL;

	@MessageField(id = "yoBUGUM", name = "월부금")
	private BigDecimal yoBUGUM;

	@MessageField(id = "yoBPYN", name = "방문판매여부")
	private String yoBPYN;

	@MessageField(id = "yoDUMMY", name = "더미")
	private String yoDUMMY;

	@MessageField(id = "errorCode")
	private String errorCode;

	@MessageField(id = "errorMessage")
	private String errorMessage;

	@MessageField(id = "errorModule")
	private String errorModule;

	@MessageField(id = "perBusNo", name = "주민등록번호")
	private String perBusNo;
}