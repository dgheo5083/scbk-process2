package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] 요청
 */
@Data
@IntegrationMessage(id = "RegistCustInfoRequest", type = Type.REQUEST)
public class RegistCustInfoRequest implements IMessageObject {
	@MessageField(id = "isUpdate", name = "")
	private String isUpdate;
	
	@MessageField(id = "otherAcctAuthYn", name = "타행계좌인증여부")
	private String otherAcctAuthYn;
	
	@MessageField(id = "acctNum", name = "계좌번호")
	private String acctNum;
	
	@MessageField(id = "acctPss", name = "계좌비번")
	private String acctPss;
	
	@MessageField(id = "isNativeFlag", name = "isNativeFlag")
	private String isNativeFlag;
	
	@MessageField(id = "bizType", name = "업무구분")
	private String bizType;
	
	////////////////////////////////////////
	// 진행상태 저장
	@MessageField(id = "tradNo", name = "진행상태 저장-거래번호")
	private String tradNo;
	
	
	@MessageField(id = "cnnctnWay", name = "진행상태 저장-제휴처")
	private String cnnctnWay;
	
	@MessageField(id = "cnnctnTradNo", name = "진행상태 저장-제휴처거래번호")
	private String cnnctnTradNo;
	
	@MessageField(id = "reacdn", name = "진행상태 저장-reacdn")
	private String reacdn;
	
	@MessageField(id = "prdctId", name = "진행상태 저장-상룸ID")
	private String prdctId;

	@MessageField(id = "prdctCd", name = "진행상태 저장-상품코드")
	private String prdctCd;
	
	@MessageField(id = "prdctNm", name = "진행상태 저장-상품명")
	private String prdctNm;
	
	@MessageField(id = "cddReqCd", name = "진행상태 저장-CDD_REQ_CD")
	private String cddReqCd;
	
	@MessageField(id = "isEtb", name = "진행상태 저장-ETB여부")
	private String isEtb;

	@MessageField(id = "isPagi", name = "진행상태 저장-isPagi")
	private String isPagi;
	
	@MessageField(id = "clerkNo", name = "진행상태 저장-행번")
	private String clerkNo;
	
	@MessageField(id = "rcvryData", name = "진행상태 저장-rcvryData")
	private String rcvryData;
	
	//////////////////////////////////////////////////////////
	//
	@MessageField(id = "yoGEORE", name = "거래자구분")
	private String yoGEORE;
	
	@MessageField(id = "yiSASAUP", name = "사업자번호")
	private String yiSASAUP;
	
	@MessageField(id = "yoOBIRNC", name = "출생국")
	private String yoOBIRNC;
	
	@MessageField(id = "yiIDGB", name = "로그온구분, (미사용)세션값 시용함")
	private String yiIDGB;
	
	@MessageField(id = "yiJHGB", name = "거래구분")
	private String yiJHGB;
	
	@MessageField(id = "yiCMFJN", name = "고객점번호")
	private String yiCMFJN;
	
	@MessageField(id = "yiENGNA", name = "영문고객명1")
	private String yiENGNA;
	
	@MessageField(id = "yiUPSRGB", name = "우편수령구분")
	private String yiUPSRGB;
	
	@MessageField(id = "yiHMUPNO", name = "자택우편번호")
	private String yiHMUPNO;
	
	@MessageField(id = "yiHMJSO1", name = "H자택주소1")
	private String yiHMJSO1;
	
	@MessageField(id = "yiHMJSO2", name = "H자택주소2")
	private String yiHMJSO2;
	
	@MessageField(id = "yiTELRGN1", name = "전화지역번호1")
	private String yiTELRGN1;
	
	@MessageField(id = "yiTELK1", name = "전화국번1")
	private String yiTELK1;
	
	@MessageField(id = "yiTEL1", name = "전화번호1")
	private String yiTEL1;
	
	@MessageField(id = "yiOFIUPNO", name = "직장우편번호")
	private String yiOFIUPNO;
	
	@MessageField(id = "yiOFIJSO1", name = "H직장주소1")
	private String yiOFIJSO1;
	
	@MessageField(id = "yiOFIJSO2", name = "H직장주소2")
	private String yiOFIJSO2;
	
	@MessageField(id = "yiTELRGN2", name = "전화지역번호2")
	private String yiTELRGN2;
	
	@MessageField(id = "yiTELK2", name = "전화국번2")
	private String yiTELK2;
	
	@MessageField(id = "yiTEL2", name = "전화번호2")
	private String yiTEL2;
	
	@MessageField(id = "yiMBRGN", name = "핸드폰지역번호")
	private String yiMBRGN;
	
	@MessageField(id = "yiMBK", name = "핸드폰국번")
	private String yiMBK;
	
	@MessageField(id = "yiMBTEL", name = "핸드폰전화번호")
	private String yiMBTEL;
	
	@MessageField(id = "yiEMALJSO", name = "이메일주소")
	private String yiEMALJSO;
	
	@MessageField(id = "yiGSEGB", name = "과세구분")
	private String yiGSEGB;
	
	@MessageField(id = "yiGRJGB", name = "거래자구분")
	private String yiGRJGB;
	
	@MessageField(id = "yiJOBCOD", name = "직업코드")
	private String yiJOBCOD;
	
	@MessageField(id = "yiSDJGB", name = "고객소득자")
	private String yiSDJGB;
	
	@MessageField(id = "yiCLDGBCOD", name = "양력음력구분")
	private String yiCLDGBCOD;
	
	@MessageField(id = "yiWEDYMCOD", name = "결혼유무코드")
	private String yiWEDYMCOD;

	@MessageField(id = "yiJSCDGB1", name = "자택 신주소")
	private String yiJSCDGB1;
	
	@MessageField(id = "yiJSCD4", name = "주소코드")
	private String yiJSCD4;
	
	@MessageField(id = "yiJUSO4", name = "상세")
	private String yiJUSO4;
	
	@MessageField(id = "yiZIP41", name = "YIZIP41")
	private String yiZIP41;
	
	@MessageField(id = "yiZIP42", name = "YIZIP42")
	private String yiZIP42;
	
	@MessageField(id = "yiZIP43", name = "YIZIP43")
	private String yiZIP43;
	
	@MessageField(id = "yiZIP44", name = "YIZIP44")
	private String yiZIP44;
	
	@MessageField(id = "yiJSCDGB2", name = "직장 신주소")
	private String yiJSCDGB2;
	
	@MessageField(id = "yiJSCD5", name = "YIJSCD5")
	private String yiJSCD5;
	
	@MessageField(id = "yiJUSO5", name = "YIJUSO5")
	private String yiJUSO5;
	
	@MessageField(id = "yiZIP51", name = "YIZIP51")
	private String yiZIP51;

	@MessageField(id = "yiZIP52", name = "YIZIP52")
	private String yiZIP52;
	
	@MessageField(id = "yiZIP53", name = "YIZIP53")
	private String yiZIP53;
	
	@MessageField(id = "yiZIP54", name = "YIZIP54")
	private String yiZIP54;
	
	@MessageField(id = "yiKUK", name = "국적")
	private String yiKUK;
	
	@MessageField(id = "yiOFINA", name = "H직장명")
	private String yiOFINA;
	
	@MessageField(id = "yiDPTNA", name = "H부서명")
	private String yiDPTNA;
	
	@MessageField(id = "yiJWINA1", name = "H직위명1")
	private String yiJWINA1;
	
	@MessageField(id = "yiPAGI", name = "파기고객신규희망점시")
	private String yiPAGI;
	
	@MessageField(id = "yiEADGB", name = "영문주소구분")
	private String yiEADGB;
	
	@MessageField(id = "yiEJUSO1", name = "영문/외국주소1")
	private String yiEJUSO1;
	
	@MessageField(id = "yiEJUSO2", name = "영문/외국주소2")
	private String yiEJUSO2;
	
	@MessageField(id = "yiEBUNHO", name = "해외전화번호")
	private String yiEBUNHO;
	
	@MessageField(id = "yiENAYN", name = "영문성명 수기 입력여부 (Y.직접입력, N.네이버 API)")
	private String yiENAYN;
	
	@MessageField(id = "yiENAME", name = "네이버영문API 1순위성명 (API결과 정상 + 직접입력시 SET)")
	private String yiENAME;
	
	@MessageField(id = "yiJOBDEL", name = "해외전화번호")
	private String yiJOBDEL;
}
