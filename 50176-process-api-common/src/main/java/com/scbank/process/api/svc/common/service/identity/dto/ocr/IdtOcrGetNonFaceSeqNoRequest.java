package com.scbank.process.api.svc.common.service.identity.dto.ocr;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrGetNonFaceSeqNoRequest", description = "비대면신청 일련번호 조회 요청 DTO", type = Type.REQUEST)
public class IdtOcrGetNonFaceSeqNoRequest  implements IMessageObject {
	
	@MessageField(id = "productCode", name = "상품코드", example = "9989")
	private String productCode;
	
	@MessageField(id = "productName", name = "상품명", example = "기본계좌변경")
	private String productName;
	
	@MessageField(id = "acctNo", name = "계좌번호", example = "")
	private String acctNo;
	
	@MessageField(id = "bankName", name = "은행명", example = "")
	private String bankName;
	
	@MessageField(id = "anotherAcctNo", name = "타계좌번호", example = "")
	private String anotherAcctNo;
	
	@MessageField(id = "agtYn", name = "신청자구분", example = "0")
	private String agtYn;
	
	@MessageField(id = "jobTypeCd", name = "업무구분코드", example = "Z")
	private String jobTypeCd;
	
	@MessageField(id = "assetsFlag", name = "자산없음", example = "N")
	private String assetsFlag;
	
	@MessageField(id = "detbFlag", name = "부채없음", example = "N")
	private String detbFlag;
	
	@MessageField(id = "deposit", name = "예금", example = "0" , defaultValue = "0")
	private String deposit;
	
	@MessageField(id = "invest", name = "투자", example = "0" , defaultValue = "0")
	private String invest;
	
	@MessageField(id = "property", name = "부동산", example = "0" , defaultValue = "0")
	private String property;
	
	@MessageField(id = "assets", name = "기타자산", example = "0" , defaultValue = "0")
	private String assets;
	
	@MessageField(id = "mortgage", name = "모기지", example = "0" , defaultValue = "0")
	private String mortgage;
	
	@MessageField(id = "loan", name = "대출", example = "0", defaultValue = "0")
	private String loan;
	
	@MessageField(id = "debt", name = "기타부채", example = "0" , defaultValue = "0")
	private String debt;
	
	@MessageField(id = "reqBrCd", name = "요청지점코드", example = "")
	private String reqBrCd;
	
	@MessageField(id = "reqBrNm", name = "요청지점명", example = "")
	private String reqBrNm;
	
	@MessageField(id = "shootingType", name = "재촬영구분", example = "R")
	private String shootingType;
}
