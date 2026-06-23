package com.scbank.process.api.edmi.dto.host;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "Ti1ibk01H893Req", type = Type.REQUEST, captureSystem = "OLTP", description = "FATCA등록/조회 요청 전문")
public class Ti1ibk01H893Req implements IMessageObject {

	@MessageField(id = "UserID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
	private String UserID;
	
	@MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, encoding = "cp500")
	private String TSPassword;
	
	@MessageField(id = "YIJMNO", name = "주민등록번호", length = 12, masking = true, maskingType = "01")
	private String YIJMNO;
	
	@MessageField(id = "YIITM01", name = "FATCA평가항목1", length = 1)
	private String YIITM01;
	
	@MessageField(id = "YIITM02", name = "FATCA평가항목2", length = 1)
	private String YIITM02;

	@MessageField(id = "YIITM03", name = "FATCA평가항목3", length = 1)
	private String YIITM03;
	
	@MessageField(id = "YIITM04", name = "FATCA평가항목4", length = 1)
	private String YIITM04;
	
	@MessageField(id = "YIITM05", name = "FATCA평가항목5", length = 1)
	private String YIITM05;
	
	@MessageField(id = "YIITM06", name = "FATCA평가항목6", length = 1)
	private String YIITM06;
	
	@MessageField(id = "YIITM07", name = "FATCA평가항목7", length = 1)
	private String YIITM07;
	
	@MessageField(id = "YIITM08", name = "FATCA평가항목8", length = 1)
	private String YIITM08;
	
	@MessageField(id = "YIITM09", name = "FATCA평가항목9", length = 1)
	private String YIITM09;

	@MessageField(id = "YIFGUBN", name = "FATCA/USP조회구분(1:요구불, 2:펀드, 3:고객확인안내제도(뒷단 로그적재 구분을 위한 분기)", length = 1)
	private String YIFGUBN;
	
	@MessageField(id = "YIELTNM", name = "영문성(LAST)", length = 50)
	private String YIELTNM;
	
	@MessageField(id = "YIEFTNM", name = "영문이름(FIRST)", length = 50)
	private String YIEFTNM;
	
	@MessageField(id = "YIEMDNM", name = "영문중간(MIDDLE)", length = 30)
	private String YIEMDNM;
	
	@MessageField(id = "YIBIRNC", name = "출생국가코드", length = 3)
	private String YIBIRNC;
	
	@MessageField(id = "YIGKCD", name = "해외납세의무국가코드", length = 3)
	private String YIGKCD;
	
	@MessageField(id = "YITAXNO", name = "해외납세자번호", length = 20)
	private String YITAXNO;
	
	@MessageField(id = "YIADDR1", name = "해외주소1", length = 45)
	private String YIADDR1;
	
	@MessageField(id = "YIADDR2", name = "해외주소2", length = 45)
	private String YIADDR2;
	
	@MessageField(id = "YITELNO", name = "해외전화번호", length = 22)
	private String YITELNO;
	
	@MessageField(id = "YITAXGB", name = "미국영주권보유여부", length = 1)
	private String YITAXGB;
}
