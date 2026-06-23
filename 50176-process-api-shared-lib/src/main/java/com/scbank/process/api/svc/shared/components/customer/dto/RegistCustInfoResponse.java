package com.scbank.process.api.svc.shared.components.customer.dto;

import java.util.List;

import com.scbank.process.api.edmi.dto.oltp.CbIbk01H92C00Res;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * 고객정보확인변경 저장 [AS-IS MA3CMMBIZ001_112S] 응답
 */
@Data
@IntegrationMessage(id = "RegistCustInfoResponse", type = Type.RESPONSE)
public class RegistCustInfoResponse  implements IMessageObject {
	/////////////////////////////////////////////////
	// 진행상태 업데이트
	@MessageField(id = "tradNo", name = "거래번호")
    private String tradNo;
	
	@MessageField(id = "custNo", name = "고객번호")
    private String custNo;
	
	@MessageField(id = "outputJsonString", name = "outputJsonString")
	private String outputJsonString;
	
	/////////////////////////////////////////////////
	// TI1IBK01_H920 (고객정보조회)
	@MessageField(id = "userID", name = "이용자번호")
    private String userID;

    @MessageField(id = "bkGuBun", name = "변경구분")
    private String bkGuBun;

    @MessageField(id = "custName", name = "고객이름")
    private String custName;

    @MessageField(id = "juMinNo", name = "주민등록번호")
    private String juMinNo;

    @MessageField(id = "birthday", name = "생년월일")
    private String birthday;

    @MessageField(id = "solarLunarCalPart", name = "양음력구분")
    private String solarLunarCalPart;

    @MessageField(id = "homeHT", name = "주거소유형태")
    private String homeHT;

    @MessageField(id = "homeJL", name = "주거종류")
    private String homeJL;

    @MessageField(id = "homePostNum", name = "우편번호")
    private String homePostNum;

    @MessageField(id = "homeAddr", name = "주소")
    private String homeAddr;

    @MessageField(id = "homeTele1", name = "전화번호1")
    private String homeTele1;

    @MessageField(id = "homeTele2", name = "전화번호2")
    private String homeTele2;

    @MessageField(id = "homeTele3", name = "전화번호3")
    private String homeTele3;

    @MessageField(id = "jobTele1", name = "전화번호1")
    private String jobTele1;

    @MessageField(id = "jobTele2", name = "전화번호2")
    private String jobTele2;

    @MessageField(id = "jobTele3", name = "전화번호3")
    private String jobTele3;

    @MessageField(id = "jobPostNum", name = "우편번호")
    private String jobPostNum;

    @MessageField(id = "jobAddr", name = "주소")
    private String jobAddr;

    @MessageField(id = "jobName", name = "직장명")
    private String jobName;

    @MessageField(id = "jobBSName", name = "부서명")
    private String jobBSName;

    @MessageField(id = "jobposition", name = "직장직위")
    private String jobposition;

    @MessageField(id = "job", name = "직업")
    private String job;

    @MessageField(id = "email", name = "E-mail")
    private String email;

    @MessageField(id = "handPhone1", name = "이동전화번호1")
    private String handPhone1;

    @MessageField(id = "handPhone2", name = "이동전화번호2")
    private String handPhone2;

    @MessageField(id = "handPhone3", name = "이동전화번호3")
    private String handPhone3;

    @MessageField(id = "weddingday", name = "결혼기념일")
    private String weddingday;

    @MessageField(id = "religion", name = "종교")
    private String religion;

    @MessageField(id = "hobby", name = "취미")
    private String hobby;

    @MessageField(id = "yearPay", name = "연간소득")
    private String yearPay;

    @MessageField(id = "marryYeubu", name = "결혼여부")
    private String marryYeubu;

    @MessageField(id = "mailSend", name = "우편물발송지")
    private String mailSend;

    @MessageField(id = "oneTimeDepLimt", name = "1회이체한도")
    private String oneTimeDepLimt;

    @MessageField(id = "oneDayDepLimt", name = "1일이체한도")
    private String oneDayDepLimt;

    @MessageField(id = "interest", name = "관심분야")
    private String interest;

    @MessageField(id = "secureLv", name = "보안등급")
    private String secureLv;

    @MessageField(id = "yoADRGB1", name = "자택사용여부")
    private String yoADRGB1;

    @MessageField(id = "yoZIPTXT1", name = "구주소(자택)")
    private String yoZIPTXT1;

    @MessageField(id = "yoZIPCD40", name = "신우편번호(자택)")
    private String yoZIPCD40;

    @MessageField(id = "yoZIPCD41", name = "신도로명코드(자택)")
    private String yoZIPCD41;

    @MessageField(id = "yoZIPCD42", name = "신일련번호(자택)")
    private String yoZIPCD42;

    @MessageField(id = "yoZIPCD43", name = "신지하여부(자택)")
    private String yoZIPCD43;

    @MessageField(id = "yoZIPCD44", name = "신건물번호(자택)")
    private String yoZIPCD44;

    @MessageField(id = "yoSJUSO4", name = "신도로명(자택)")
    private String yoSJUSO4;

    @MessageField(id = "yoJUSO4", name = "신상세주소(자택)")
    private String yoJUSO4;

    @MessageField(id = "yoJIKGB", name = "직장사용여부")
    private String yoJIKGB;

    @MessageField(id = "yoJUSOK2", name = "구주소(직장)")
    private String yoJUSOK2;

    @MessageField(id = "yoJIKCD40", name = "신우편번호(직장)")
    private String yoJIKCD40;

    @MessageField(id = "yoJIKCD41", name = "신도로명코드(직장)")
    private String yoJIKCD41;

    @MessageField(id = "yoJIKCD42", name = "신일련번호(직장)")
    private String yoJIKCD42;

    @MessageField(id = "yoJIKCD43", name = "신지하여부(직장)")
    private String yoJIKCD43;

    @MessageField(id = "yoJIKCD44", name = "신건물번호(직장)")
    private String yoJIKCD44;

    @MessageField(id = "yoJIKDORO", name = "신도로명(직장)")
    private String yoJIKDORO;

    @MessageField(id = "yoJIKSANG", name = "신상세주소(직장)")
    private String yoJIKSANG;

    @MessageField(id = "yoENAME", name = "고객영문명")
    private String yoENAME;

    @MessageField(id = "yoOGKCD", name = "국적")
    private String yoOGKCD;

    @MessageField(id = "yoOBIRNC", name = "출생국")
    private String yoOBIRNC;

    @MessageField(id = "yoEJUSO1", name = "영문주소2")
    private String yoEJUSO1;

    @MessageField(id = "yoEBUNHO", name = "해외전화번호")
    private String yoEBUNHO;

    @MessageField(id = "yoFXYN", name = "외환고객여부유무")
    private String yoFXYN;

    @MessageField(id = "yoEADGB", name = "영문주소연결주소코드")
    private String yoEADGB;

    @MessageField(id = "yoGEOJU", name = "1:거주2:비거주")
    private String yoGEOJU;

    @MessageField(id = "yoOFAGKCD", name = "해외주소국가코드")
    private String yoOFAGKCD;

    @MessageField(id = "yoOFTGKCD", name = "해외전화국가코드")
    private String yoOFTGKCD;

    ////////////////////////////////////
    @MessageField(id = "authMobileNum1", name = "본인인증한 휴대폰번호1")
    private String authMobileNum1;
    
    @MessageField(id = "authMobileNum2", name = "본인인증한 휴대폰번호2")
    private String authMobileNum2;
    
    @MessageField(id = "authMobileNum3", name = "본인인증한 휴대폰번호3")
    private String authMobileNum3;
    
    //////////////////////////////////////
    // TI1IBK01_H92C (고객정보조회)
    
    @MessageField(id = "yoMSSU", name = "출력조립수")
    private int yoMSSU;
    
    @MessageField(id = "yoMSSU_REC", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "RegistCustInfoResponse/yoMSSU")
    private List<CbIbk01H92C00Res.YOMSSU_REC_GRID> yoMSSU_REC;
    
    @MessageField(id = "yoACMFNO", name = "고객번호")
    private String yoACMFNO;

}
