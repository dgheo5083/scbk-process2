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
 * 계약서류제공 펀드 계약서조회
 */
@Data
@IntegrationMessage(id = "SupDocListFundResponse", type = Type.RESPONSE)
public class SupDocListFundResponse implements IMessageObject {

	@MessageField(id = "fundCommDocList", name = "펀드 계좌목록조회")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<AddCntrctDocument> fundCommDocList;

	@MessageField(id = "homepageUrl", name = "homepageUrl")
	private String homepageUrl;

	@MessageField(id = "ma30Url", name = "pdfUrl")
	private String ma30Url;

	@MessageField(id = "ma30PdfUrl", name = "ma30PdfUrl")
	private String ma30PdfUrl;

	@MessageField(id = "pdfUrl", name = "pdfUrl")
	private String pdfUrl;

	@MessageField(id = "pms20Path2", name = "pms20Path2")
	private String pms20Path2;

	@MessageField(id = "pdfUrlKyir", name = "pdfUrlKyir")
	private String pdfUrlKyir;

	@MessageField(id = "acctNum", name = "acctNum")
	private String acctNum;

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

	@MessageField(id = "investExplnDoc", name = "투자설명서")
	private String investExplnDoc;

	@MessageField(id = "tmpInvestExplnDoc", name = "간이투자설명서")
	private String tmpInvestExplnDoc;

	@MessageField(id = "prvsnMk5Filenm", name = "핵심상품설명서")
	private String prvsnMk5Filenm;

	@MessageField(id = "prvsnMk6Filenm", name = "사모집합투자증권 핵심요약상품설명서")
	private String prvsnMk6Filenm;

	@MessageField(id = "fundStatusKRW", name = "역내,역외")
	private String fundStatusKRW;

	@MessageField(id = "edMSQueryFlag", name = "edMSQueryFlag")
	private String edMSQueryFlag;

	// @Data
	// public class AddCntrctDocument implements IMessageObject {
	// @MessageField(id = "addCntrctFileNm", name = "공통약관파일명")
	// private String addCntrctFileNm; // 공통약관파일명
	// @MessageField(id = "addCntrctNm", name = "공통약관명")
	// private String addCntrctNm; // 공통약관명
	// @MessageField(id = "addCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	// private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
	// }

	@MessageField(id = "yoGRGB", name = "거래구분")
	private String yoGRGB;

	@MessageField(id = "yoBRNO", name = "계좌점번호")
	private String yoBRNO;

	@MessageField(id = "yoKMCD", name = "과목번호")
	private String yoKMCD;

	@MessageField(id = "yoBUNHO", name = "계좌번호")
	private String yoBUNHO;

	@MessageField(id = "yoCIFNO", name = "CIF번호")
	private BigDecimal yoCIFNO;

	@MessageField(id = "yoCNAME", name = "고객명")
	private String yoCNAME;

	@MessageField(id = "yoOPEIL", name = "계좌개설일자")
	private String yoOPEIL;

	@MessageField(id = "yoTOCD", name = "통화코드")
	private Integer yoTOCD;

	@MessageField(id = "yoSINAK", name = "계좌개설금액")
	private BigDecimal yoSINAK;

	@MessageField(id = "yoPRID", name = "상품번호")
	private String yoPRID;

	@MessageField(id = "yoPNAME", name = "상품명")
	private String yoPNAME;

	@MessageField(id = "yoCRISK", name = "고객투자등급")
	private String yoCRISK;

	@MessageField(id = "yoPRISK", name = "상품위험등급")
	private String yoPRISK;

	@MessageField(id = "yoJAJUM", name = "연결계좌점번호")
	private String yoJAJUM;

	@MessageField(id = "yoJAKM", name = "연결계좌과목번호")
	private String yoJAKM;

	@MessageField(id = "yoJABUN", name = "연결계좌번호")
	private String yoJABUN;

	@MessageField(id = "yoWBIL", name = "만기일자")
	private String yoWBIL;

	@MessageField(id = "yoUNGB", name = "운용보고서통보")
	private String yoUNGB;

	@MessageField(id = "yoSUGB", name = "수익률／계산서통보")
	private String yoSUGB;

	@MessageField(id = "yoALGB", name = "알림／이벤트통보")
	private String yoALGB;

	@MessageField(id = "yoZONG", name = "종별")
	private String yoZONG;

	@MessageField(id = "yoYSPCD", name = "세제유형")
	private String yoYSPCD;

	@MessageField(id = "yoHANDO", name = "세제한도금액")
	private BigDecimal yoHANDO;

	@MessageField(id = "yoGRBB", name = "거래방법")
	private String yoGRBB;

	@MessageField(id = "yoBSJUM", name = "계좌관리점")
	private String yoBSJUM;

	@MessageField(id = "yoTOGB", name = "계약서류통보방법")
	private String yoTOGB;

	@MessageField(id = "yoGIGAN", name = "계약기간")
	private Integer yoGIGAN;

	@MessageField(id = "yoPBOSU", name = "선취보수")
	private Integer yoPBOSU;

	@MessageField(id = "yoGBOSU", name = "후취보수")
	private Integer yoGBOSU;

	@MessageField(id = "yoJDSSR", name = "중도해지수수료율")
	private Integer yoJDSSR;

	@MessageField(id = "yoBSGB", name = "발송구분（신탁용)")
	private String yoBSGB;

	@MessageField(id = "yoURL", name = "URL정보")
	private String yoURL;

	@MessageField(id = "yoTONM", name = "통화명")
	private String yoTONM;

	@MessageField(id = "yoWJAGB", name = "자동이체등록여부")
	private String yoWJAGB;

	@MessageField(id = "yoWJAJUM", name = "자동이체계좌점번호")
	private String yoWJAJUM;

	@MessageField(id = "yoWJAKM", name = "자동이체계좌과목")
	private String yoWJAKM;

	@MessageField(id = "yoWJABUN", name = "자동이체계좌번호")
	private String yoWJABUN;

	@MessageField(id = "yoWJAIL", name = "자동이체일자")
	private String yoWJAIL;

	@MessageField(id = "yoWJAAMT", name = "자동이체금액")
	private BigDecimal yoWJAAMT;

	@MessageField(id = "yoWSTIL", name = "자동이체시작일자")
	private String yoWSTIL;

	@MessageField(id = "yoWLTIL", name = "자동이체종료일자")
	private String yoWLTIL;

	@MessageField(id = "yoFDSPYH", name = "펀드상품유형")
	private String yoFDSPYH;

	@MessageField(id = "yoJHYB", name = "31일간 조회 가능 여부 Y/N")
	private String yoJHYB;

	@MessageField(id = "yoBCODE", name = "바코드생성정보")
	private String yoBCODE;

	@MessageField(id = "yoBD2DC", name = "방문판매관리번호")
	private String yoBD2DC;

	@MessageField(id = "errorCode")
	private String errorCode;

	@MessageField(id = "errorMessage")
	private String errorMessage;

	@MessageField(id = "errorModule")
	private String errorModule;

	@MessageField(id = "perBusNo", name = "주민등록번호")
	private String perBusNo;

}
