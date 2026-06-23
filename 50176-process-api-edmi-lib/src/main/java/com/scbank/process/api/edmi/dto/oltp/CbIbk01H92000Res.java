package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H920Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "고객정보조회 응답 전문")
public class CbIbk01H92000Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "BKGuBun", name = "변경구분", length = 1)
    private String BKGuBun;

    @MessageField(id = "CustName", name = "고객이름", length = 22, masking = true, maskingType = "04")
    private String CustName;

    @MessageField(id = "JuMinNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01")
    private String JuMinNo;

    @MessageField(id = "Birthday", name = "생년월일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String Birthday;

    @MessageField(id = "SolarLunarCalPart", name = "양음력구분", length = 1)
    private String SolarLunarCalPart;

    @MessageField(id = "HomeHT", name = "주거소유형태", length = 1)
    private String HomeHT;

    @MessageField(id = "HomeJL", name = "주거종류", length = 1)
    private String HomeJL;

    @MessageField(id = "HomePostNum", name = "우편번호", length = 6)
    private String HomePostNum;

    @MessageField(id = "HomeAddr", name = "주소", length = 62)
    private String HomeAddr;

    @MessageField(id = "HomeTele1", name = "전화번호1", length = 4)
    private String HomeTele1;

    @MessageField(id = "HomeTele2", name = "전화번호2", length = 4)
    private String HomeTele2;

    @MessageField(id = "HomeTele3", name = "전화번호3", length = 4, masking = true, maskingType = "03")
    private String HomeTele3;

    @MessageField(id = "JobTele1", name = "전화번호1", length = 4)
    private String JobTele1;

    @MessageField(id = "JobTele2", name = "전화번호2", length = 4)
    private String JobTele2;

    @MessageField(id = "JobTele3", name = "전화번호3", length = 4, masking = true, maskingType = "03")
    private String JobTele3;

    @MessageField(id = "JobPostNum", name = "우편번호", length = 6)
    private String JobPostNum;

    @MessageField(id = "JobAddr", name = "주소", length = 62)
    private String JobAddr;

    @MessageField(id = "JobName", name = "직장명", length = 22)
    private String JobName;

    @MessageField(id = "JobBSName", name = "부서명", length = 12)
    private String JobBSName;

    @MessageField(id = "Jobposition", name = "직장직위", length = 12)
    private String Jobposition;

    @MessageField(id = "Job", name = "직업", length = 2)
    private String Job;

    @MessageField(id = "Email", name = "E-mail", length = 30, masking = true, maskingType = "07")
    private String Email;

    @MessageField(id = "HandPhone1", name = "이동전화번호1", length = 4)
    private String HandPhone1;

    @MessageField(id = "HandPhone2", name = "이동전화번호2", length = 4)
    private String HandPhone2;

    @MessageField(id = "HandPhone3", name = "이동전화번호3", length = 4, masking = true, maskingType = "03")
    private String HandPhone3;

    @MessageField(id = "Weddingday", name = "결혼기념일", length = 8)
    private String Weddingday;

    @MessageField(id = "Religion", name = "종교", length = 1)
    private String Religion;

    @MessageField(id = "Hobby", name = "취미", length = 50)
    private String Hobby;

    @MessageField(id = "YearPay", name = "연간소득", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YearPay;

    @MessageField(id = "MarryYeubu", name = "결혼여부", length = 1)
    private String MarryYeubu;

    @MessageField(id = "MailSend", name = "우편물발송지", length = 1)
    private String MailSend;

    @MessageField(id = "OneTimeDepLimt", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OneTimeDepLimt;

    @MessageField(id = "OneDayDepLimt", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String OneDayDepLimt;

    @MessageField(id = "Interest", name = "관심분야", length = 20)
    private String Interest;

    @MessageField(id = "SecureLV", name = "보안등급", length = 1)
    private String SecureLV;

    @MessageField(id = "YOADRGB1", name = "자택사용여부", length = 1)
    private String YOADRGB1;

    @MessageField(id = "YOZIPTXT1", name = "구주소(자택)", length = 42)
    private String YOZIPTXT1;

    @MessageField(id = "YOZIPCD40", name = "신우편번호(자택)", length = 6)
    private String YOZIPCD40;

    @MessageField(id = "YOZIPCD41", name = "신도로명코드(자택)", length = 12)
    private String YOZIPCD41;

    @MessageField(id = "YOZIPCD42", name = "신일련번호(자택)", length = 2)
    private String YOZIPCD42;

    @MessageField(id = "YOZIPCD43", name = "신지하여부(자택)", length = 1)
    private String YOZIPCD43;

    @MessageField(id = "YOZIPCD44", name = "신건물번호(자택)", length = 10)
    private String YOZIPCD44;

    @MessageField(id = "YOSJUSO4", name = "신도로명(자택)", length = 72)
    private String YOSJUSO4;

    @MessageField(id = "YOJUSO4", name = "신상세주소(자택)", length = 102, masking = true, maskingType = "06")
    private String YOJUSO4;

    @MessageField(id = "YOJIKGB", name = "직장사용여부", length = 1)
    private String YOJIKGB;

    @MessageField(id = "YOJUSOK2", name = "구주소(직장)", length = 42)
    private String YOJUSOK2;

    @MessageField(id = "YOJIKCD40", name = "신우편번호(직장)", length = 6)
    private String YOJIKCD40;

    @MessageField(id = "YOJIKCD41", name = "신도로명코드(직장)", length = 12)
    private String YOJIKCD41;

    @MessageField(id = "YOJIKCD42", name = "신일련번호(직장)", length = 2)
    private String YOJIKCD42;

    @MessageField(id = "YOJIKCD43", name = "신지하여부(직장)", length = 1)
    private String YOJIKCD43;

    @MessageField(id = "YOJIKCD44", name = "신건물번호(직장)", length = 10)
    private String YOJIKCD44;

    @MessageField(id = "YOJIKDORO", name = "신도로명(직장)", length = 72)
    private String YOJIKDORO;

    @MessageField(id = "YOJIKSANG", name = "신상세주소(직장)", length = 102, masking = true, maskingType = "06")
    private String YOJIKSANG;

    @MessageField(id = "YOENAME", name = "고객영문명", length = 50)
    private String YOENAME;

    @MessageField(id = "YOOGKCD", name = "거주국", length = 3)
    private String YOOGKCD;

    @MessageField(id = "YOOCDDNI", name = "국적", length = 3)
    private String YOOCDDNI;

    @MessageField(id = "YOOBIRNC", name = "출생국", length = 3)
    private String YOOBIRNC;

    @MessageField(id = "YOEJUSO1", name = "영문주소1", length = 45)
    private String YOEJUSO1;
    
    @MessageField(id = "YOEJUSO1", name = "영문주소2", length = 45)
    private String YOEJUSO2;

    @MessageField(id = "YOEBUNHO", name = "해외전화번호", length = 22)
    private String YOEBUNHO;

    @MessageField(id = "YOFXYN", name = "외환고객여부유무", length = 1)
    private String YOFXYN;

    @MessageField(id = "YOEADGB", name = "영문주소연결주소코드", length = 1)
    private String YOEADGB;

    @MessageField(id = "YOGEOJU", name = "1:거주2:비거주", length = 1)
    private String YOGEOJU;

    @MessageField(id = "YOOFAGKCD", name = "해외주소국가코드", length = 3)
    private String YOOFAGKCD;

    @MessageField(id = "YOOFTGKCD", name = "해외전화국가코드", length = 3)
    private String YOOFTGKCD;

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 177)
    private String YODUMMY;

}
