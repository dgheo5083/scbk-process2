package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrSendIdCardImageRequest", description = "실물신분증 이미지 전송 요청 DTO", type = Type.REQUEST)
public class IdtOcrSendIdCardImageRequest implements IMessageObject {
	@MessageField(id = "shootingType", name = "촬영구분", example = "R")
	private String shootingType;
	
	@MessageField(id = "isTruthCheck", name = "신분증진위여부체크", example = "Y")
	private String isTruthCheck;
	
	@MessageField(id = "clerkNo", name = "소개자행번", example = "")
	private String clerkNo;
	
	@MessageField(id = "branchNo", name = "영업점번호", example = "")
	private String branchNo;
	
	@MessageField(id = "branchNm", name = "영업점명", example = "")
	private String branchNm;
	
	@MessageField(id = "isRAS", name = "화상상담여부", example = "N")
	private String isRAS;
	
	@MessageField(id = "seqNoList", name = "시퀀스리스트", example = "112,314")
	private String seqNoList;
	
	@MessageField(id = "tradNoList", name = "거래번호리스트", example = "4421,1123")
	private String tradNoList;
	
	@MessageField(id = "businessDateList", name = "영업일리스트", example = "20260409,20260410")
	private String businessDateList;
	
	@MessageField(id = "fileNameList", name = "파일명리스트", example = "name1,name2")
	private String fileNameList;
	
	@MessageField(id = "idCardData", name = "파일명리스트", example = "name1,name2")
	private String idCardData;
	
	@MessageField(id = "seqNo", name = "시퀀스", example = "")
	private String seqNo;
	
	@MessageField(id = "tradNo", name = "거래번호", example = "")
	private String tradNo;
	
	@MessageField(id = "fileName", name = "파일명", example = "")
	private String fileName;
	
	@MessageField(id = "businessDate", name = "영업일", example = "")
	private String businessDate;
	
	@MessageField(id = "idCdIsuDt", name = "발급일", example = "")
	private String idCdIsuDt;
	
	@MessageField(id = "licenseFrontNumber", name = "운전면허번호 앞부분", example = "")
	private String licenseFrontNumber;
	
	@MessageField(id = "licenseMiddleNumberEnc", name = "운전면허번호 중간부분", example = "")
	private String licenseMiddleNumberEnc;
	
	@MessageField(id = "licenseLastNumber", name = "운전면허번호 뒷부분", example = "")
	private String licenseLastNumber;
	
	@MessageField(id = "pssprtNo", name = "여권번호", example = "")
	private String pssprtNo;
	
	@MessageField(id = "veteransNumber1", name = "보훈번호1", example = "")
	private String veteransNumber1;
	
	@MessageField(id = "veteransNumber2", name = "보훈번호2", example = "")
	private String veteransNumber2;
	
	@MessageField(id = "idCdDsCd", name = "신분증구분코드", example = "")
	private String idCdDsCd;
	
	@MessageField(id = "fngrInfo", name = "지문정보", example = "")
	private String fngrInfo;
	
	@MessageField(id = "photoInfo", name = "사진정보", example = "")
	private String photoInfo;
	
	@MessageField(id = "photoInfoSize", name = "사진사이즈", example = "")
	private String photoInfoSize;
	
	@MessageField(id = "vtrnNoChgYn", name = "보훈번호변경여부", example = "N")
	private String vtrnNoChgYn;
	
	@MessageField(id = "passportNoChgYn", name = "여권번호변경여부", example = "N")
	private String passportNoChgYn;
	
	@MessageField(id = "custIssDtChgYn", name = "발급일자변경여부", example = "N")
	private String custIssDtChgYn;
	
	@MessageField(id = "custNmChgYn", name = "고객명변경여부", example = "N")
	private String custNmChgYn;
	
	@MessageField(id = "custNoChgYn", name = "실명번호변경여부", example = "N")
	private String custNoChgYn;
	
	@MessageField(id = "foreignerTypeChgYn", name = "외국인등록증구분 수정여부", example = "N")
	private String foreignerTypeChgYn;
	
	@MessageField(id = "extractScore", name = "특징점점수", example = "")
	private String extractScore;
	
	@MessageField(id = "alienType", name = "외국인신분증구분", example = "")
	private String alienType;
	
	@MessageField(id = "mrz1Enc", name = "여권MRZ1", example = "")
	private String mrz1Enc;
	
	@MessageField(id = "mrz2Enc", name = "여권MRZ2", example = "")
	private String mrz2Enc;
	
	@MessageField(id = "name", name = "이름", example = "")
	private String name;
	
	@MessageField(id = "perBusNo1", name = "주민번호 앞자리", example = "")
	private String perBusNo1;
	
	@MessageField(id = "perBusNoEnc", name = "주민번호 뒷자리", example = "")
	private String perBusNoEnc;
}
