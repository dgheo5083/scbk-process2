package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CSL 서비스 요청 정보 클래스
 * 공공꾸러미 API 호출
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "GetMyDataRequest", type = Type.REQUEST)
public class GetMyDataRequest implements IMessageObject {

    @MessageField(id = "apiType", name = "")
    private String apiType;

    @MessageField(id = "apiKey", name = "")
    private String apiKey;

    @MessageField(id = "dtarget", name = "")
    private String dtarget;

    @MessageField(id = "srvc", name = "서비스 ID")
    private String srvc;

    @MessageField(id = "jobMk", name = "업무구분")
    private String jobMk;

    @MessageField(id = "chnnMk", name = "채널구분")
    private String chnnMk;

    @MessageField(id = "agrmntFlg", name = "동의여부")
    private String agrmntFlg;

    @MessageField(id = "untactMk", name = "대면/비대면구분")
    private String untactMk;
    
    //////////////////////////////////
    /// 사업자등록증/폐업사실증명/휴업사실증명
    @MessageField(id = "l022K001", name = "사업자등록번호앞번호")
    private String l022K001;

    @MessageField(id = "l022K002", name = "사업자등록번호중간번호")
    private String l022K002;

    @MessageField(id = "l022K003", name = "사업자등록번호뒷번호")
    private String l022K003;

	//////////////////////////////////
	/// 건강장기요양보험료납부확인서(개인|직장가입자)
    @MessageField(id = "l008K004", name = "조회년도")
    private String l008K004;

    @MessageField(id = "l008K005", name = "업무구분")
    private String l008K005;

    @MessageField(id = "l008K006", name = "보험구분")
    private String l008K006;

	//////////////////////////////////
	/// 부가가치세과세표준증명
	@MessageField(id = "l025K001", name = "신청사업자등록번호	")
    private String l025K001;
	
	@MessageField(id = "l025K002", name = "신청개업일자	")
    private String l025K002;
	
	@MessageField(id = "l025K003", name = "신청과세시작년월")
    private String l025K003;
	
	@MessageField(id = "l025K004", name = "신청과세종료년월")
    private String l025K004;
	
	@MessageField(id = "l028K001", name = "신청사업자등록번호	")
    private String l028K001;
	
	@MessageField(id = "l028K002", name = "신청개업일자	")
    private String l028K002;
	
	@MessageField(id = "l028K003", name = "신청과세시작년월")
    private String l028K003;
	
	@MessageField(id = "l028K004", name = "신청과세종료년월")
    private String l028K004;
	
	//////////////////////////////////
	/// 납세증명서 
	@MessageField(id = "l021K001", name = "신청인정보사업자구분(개인, 법인)")
    private String l021K001;
	
	@MessageField(id = "l021K002", name = "내외국인구분코드(1:내국인, 9:외국인)")
    private String l021K002;
	
	@MessageField(id = "l021K006", name = "사업자등록번호1 (사업자구분이 법인 일경우 필수)")
    private String l021K006;
	
	@MessageField(id = "l021K007", name = "사업자등록번호2 (사업자구분이 법인 일경우 필수)")
    private String l021K007;
	
	@MessageField(id = "l021K008", name = "사업자등록번호3 (사업자구분이 법인 일경우 필수)")
    private String l021K008;
	
	@MessageField(id = "l021K009", name = "식별번호 구분(0:주민등록번호, 1:개인식별자(CI), 2:사업자등록번호 )")
    private String l021K009;
	
	@MessageField(id = "l021K010", name = "개인식별자")
    private String l021K010;
	
	//////////////////////////////////
	/// 소득금액증명. 20220922 소득금액증명 1760 -> 2427 문서변경으로 파라 변경
	@MessageField(id = "l170K004", name = "과세기간시작연도")
    private String l170K004;
	
	@MessageField(id = "l170K005", name = "과세기간종료연동")
    private String l170K005;
	
	//////////////////////////////////
	/// 비과세종합저축 관련 우대사항확인
	@MessageField(id = "lsrchType", name = "우대사항확인 구분값")
    private String lsrchType;
	
	@MessageField(id = "l398K001", name = "L398 국가유공자유족확인 - 보훈번호")
    private String l398K001;

	//////////////////////////////////
	/// 공무원연금공단
	@MessageField(id = "l033K001", name = "주민번호 (13자리)")
    private String l033K001;
}
