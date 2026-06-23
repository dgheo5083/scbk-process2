package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrNonFaceImageDataProcessRequest", description = "비대면이미지데이터처리 요청 DTO", type = Type.REQUEST)
public class IdtOcrNonFaceImageDataProcessRequest implements IMessageObject {
	private String passCode;
	private String seqNo;
	private String tradNo;
	private String fileName;
	private String businessDate;
	private String idCardData;
	private String idCdIsuDt;
//	private String dataModifyYn;
	private String oplicNo;
	private String pssprtNo;
	private String mrtsNo;
	private String idCdDsCd;
	private String fngrInfo;
	private String photoInfo;
	private String photoInfoSize;
	private String vtrnNoChgYn;
	private String passportNoChgYn;
	private String custIssDtChgYn;
	private String foreignerTypeChgYn;
	private String custNmChgYn;
	private String custNoChgYn;
	private String extractScore;
	private String clerkNo;
	private String branchNo;
	private String branchNm;
	private String filePath;
	private String isTruthCheck;
	private String alienType;
	private String mrz1Enc;
	private String mrz2Enc;
	private String name;
	private String perBusNo1;
	private String perBusNo2;
}
