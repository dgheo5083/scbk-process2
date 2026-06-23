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
 * 계약서류제공 카드 계약서조회
 */
@Data
@IntegrationMessage(id = "SupDocListCardResponse", type = Type.RESPONSE)
public class SupDocListCardResponse implements IMessageObject {

	@MessageField(id = "cardDocList", name = "계약서류 조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AddCntrctDocument> cardDocList;

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

	@MessageField(id = "drawAcctNum", name = "drawAcctNum")
	private String drawAcctNum;

	// @Data
	// public class AddCntrctDocument implements IMessageObject {
	// @MessageField(id = "addCntrctFileNm", name = "공통약관파일명")
	// private String addCntrctFileNm; // 공통약관파일명
	// @MessageField(id = "addCntrctNm", name = "공통약관명")
	// private String addCntrctNm; // 공통약관명
	// @MessageField(id = "addCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	// private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
	// }

	@MessageField(id = "yoNAME", name = "한글성명")
	private String yoNAME;

	@MessageField(id = "yoJHNM", name = "제휴코드명")
	private String yoJHNM;

	@MessageField(id = "yoCDGB", name = "카드구분")
	private String yoCDGB;

	@MessageField(id = "yoBKNM", name = "은행명")
	private String yoBKNM;

	@MessageField(id = "yoKJGJ", name = "결제계좌")
	private String yoKJGJ;

	@MessageField(id = "yoGJIL", name = "결제일")
	private Integer yoGJIL;

	@MessageField(id = "yoSINHD", name = "총한도")
	private BigDecimal yoSINHD;

	@MessageField(id = "yoDBGIL", name = "발급일자")
	private Integer yoDBGIL;

	@MessageField(id = "yoSVCIY", name = "현금서비스수수료율 99V99")
	private BigDecimal yoSVCIY;

	@MessageField(id = "yoHB1IY", name = "할부2개월수수료율")
	private BigDecimal yoHB1IY;

	@MessageField(id = "yoHB2IY", name = "할부24개월수수료율")
	private BigDecimal yoHB2IY;

	@MessageField(id = "yoRVSIY", name = "리볼빙현금수수료율")
	private BigDecimal yoRVSIY;

	@MessageField(id = "yoGJGB", name = "공용지정구분 (1:공용, 2:지정)")
	private String yoGJGB;

	@MessageField(id = "yoJGNM", name = "지정자한글명")
	private String yoJGNM;

	@MessageField(id = "yoGIGAN", name = "유효기간")
	private Integer yoGIGAN;

	@MessageField(id = "yoHMUPNO", name = "자택우편번호")
	private String yoHMUPNO;

	@MessageField(id = "yoHMJSO1", name = "자택주소1")
	private String yoHMJSO1;

	@MessageField(id = "yoHMJSO2", name = "자택주소2")
	private String yoHMJSO2;

	@MessageField(id = "yoCARD", name = "개인기업구분 (1:개인, 2:기업)")
	private String yoCARD;

	@MessageField(id = "yoGYHWNO", name = "계약카드번호")
	private String yoGYHWNO;

	@MessageField(id = "yoHNAME", name = "현재고객명，업체명")
	private String yoHNAME;

	@MessageField(id = "yoHJUMIN", name = "현재주민사업자번호")
	private String yoHJUMIN;

	@MessageField(id = "yoJHCD", name = "제휴코드")
	private String yoJHCD;

	@MessageField(id = "yoSVCGB", name = "현금서비스한도구분")
	private String yoSVCGB;

	@MessageField(id = "yoSVCHD", name = "현금서비스한도")
	private BigDecimal yoSVCHD;

	@MessageField(id = "errorCode")
	private String errorCode;

	@MessageField(id = "errorMessage")
	private String errorMessage;

	@MessageField(id = "errorModule")
	private String errorModule;

	@MessageField(id = "perBusNo", name = "주민등록번호")
	private String perBusNo;
}
