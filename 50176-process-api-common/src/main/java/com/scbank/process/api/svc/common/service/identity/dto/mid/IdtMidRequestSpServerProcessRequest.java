package com.scbank.process.api.svc.common.service.identity.dto.mid;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@IntegrationMessage(id = "IdtOcrRequestSpServerProcessRequest", description = "SP서버처리요청 요청 DTO", type = Type.REQUEST)
public class IdtMidRequestSpServerProcessRequest implements IMessageObject {
	
	@MessageField(id = "action", name = "액션", example = "")
	private String action;
	
	@MessageField(id = "serviceCode", name = "서비스코드", example = "scbank.1")
	private String serviceCode;
	
	@MessageField(id = "interfaceType", name = "인터페이스타입", example = "APP2APP")
	private String interfaceType;
	
	@Schema(description = "submitMode", example = "DIRECT")
	@MessageField(id = "submitMode", name = "submit모드", example = "DIRECT")
	private String submitMode;
	
	@MessageField(id = "includeProfile", name = "프로필include", example = "true")
	private String includeProfile;
	
	@MessageField(id = "channelCode", name = "채널코드", example = "app")
	private String channelCode;
	
	@MessageField(id = "branchCode", name = "영업점코드", example = "")
	private String branchCode;
	
	@MessageField(id = "branchName", name = "영업점명", example = "")
	private String branchName;
	
	@MessageField(id = "employeeNumber", name = "", example = "")
	private String employeeNumber;
	
	@MessageField(id = "employeeName", name = "", example = "")
	private String employeeName;
	
	@MessageField(id = "idCdDsCd", name = "신분증구분코드", example = "11")
	private String idCdDsCd;
	
	@MessageField(id = "appType", name = "app타입", example = "IOS")
	private String appType;
	
	@MessageField(id = "trxCode", name = "거래코드", example = "")
	private String trxCode;
	
	@MessageField(id = "seqNo", name = "seqNo", example = "")
	private String seqNo;
	
	@MessageField(id = "issude", name = "발급일자", example = "")
	private String issude;
	
	@MessageField(id = "dlno", name = "운전면허증번호", example = "")
	private String dlno;
	
	@MessageField(id = "name", name = "이름", example = "")
	private String name;
	
	@MessageField(id = "engnm", name = "영문이름", example = "")
	private String engnm;
	
	@MessageField(id = "businessDate", name = "", example = "")
	private String businessDate;
	
	@MessageField(id = "idCardImg", name = "신분증이미지", example = "")
	private String idCardImg;
	
	@MessageField(id = "isOverSeasKorean", name = "재외국민 여부", example = "")
	private String isOverSeasKorean;
	
	@MessageField(id = "isTruthCheck", name = "", example = "")
	private String isTruthCheck;
	
}
