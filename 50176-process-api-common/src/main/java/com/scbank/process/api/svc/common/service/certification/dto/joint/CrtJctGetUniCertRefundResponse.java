package com.scbank.process.api.svc.common.service.certification.dto.joint;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;



/**
 * CSL 서비스 응답 정보 클래스
 * 수수료 납부 취소 대상 조회
 */
@Data
@IntegrationMessage(id = "CrtJctGetUniCertRefundResponse", type = Type.RESPONSE)
public class CrtJctGetUniCertRefundResponse implements IMessageObject {

	@MessageField(id = "CMSCode", name = "CMSCode")
	private String cmsCode;
	
	@MessageField(id = "DeptPersonName", name = "예금주")
	private String deptPersonName;
	
	@MessageField(id = "FDate", name = "조회시작일")
	private String fDate;
	
	@MessageField(id = "TDate", name = "조회종료일")
	private String tDate;
	
	@MessageField(id="chargeList", name = "수수료 납부 취소 대상 목록")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<ChargeInfo> chargeList;

}