package com.scbank.process.api.svc.shared.components.product.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "PmsProductAvailableInfoRequest", type = Type.REQUEST)
public class PmsProductAvailableInfoRequest {
	@MessageField(id = "targetPrdctCd", name = "상품코드")
	private String targetPrdctCd;
	
	@MessageField(id = "flagIsAbroadYn", name = "해외IP여부")
	private String flagIsAbroadYn;
	
	@MessageField(id = "errorCode", name = "에러코드")
	private String errorCode;
	
	@MessageField(id = "nextPage", name = "에러발생시 이동시킬 페이지")
	private String nextPage;
	
	@MessageField(id = "nextParam", name = "이동할 페이지로 보내줄 parameter")
	private String nextParam;
	
	@MessageField(id = "throwExceptionYn", name = "에러발생여부")
	private String throwExceptionYn;
	
	@MessageField(id = "ipinsideAx", name = "ipinsideAx")
	private String ipinsideAx;
	
	@MessageField(id = "perBusNo", name = "주민번호")
	private String perBusNo;
	
	@MessageField(id = "forceCheckCode", name = "강제체크코드")
	private String forceCheckCode;
}
