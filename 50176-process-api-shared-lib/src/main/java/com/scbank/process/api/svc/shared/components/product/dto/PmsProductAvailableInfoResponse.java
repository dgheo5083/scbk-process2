package com.scbank.process.api.svc.shared.components.product.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "PmsProductAvailableInfoResponse", type = Type.RESPONSE, description = "상품이용가능정보조회 응답")
public class PmsProductAvailableInfoResponse {
	@MessageField(id = "availableDay", name = "이용가능날짜")
    private String availableDay;
	
	@MessageField(id = "availableTime", name = "이용가능 시간")
    private String availableTime;
	
	@MessageField(id = "availableAge", name = "이용가능 나이")
    private String availableAge;
	
	@MessageField(id = "availableAbroadIp", name = "해외IP 가능여부")
    private String availableAbroadIp;
	
	@MessageField(id = "checkDay", name = "이용가능 날짜 체크 결과")
    private String checkDay;
	
	@MessageField(id = "checkTime", name = "이용가능 시간 체크 결과")
    private String checkTime;
	
	@MessageField(id = "checkAge", name = "이용가능 나이 체크 결과")
    private String checkAge;
	
	@MessageField(id = "checkAbroadIP", name = "해외IP 허용여부 체크 결과")
    private String checkAbroadIP;
	
	@MessageField(id = "flagIsAbroadYn", name = "해외IP여부")
    private String flagIsAbroadYn;
	
	@MessageField(id = "isLowAge", name = "나이제한 여부")
    private String isLowAge;
	
	@MessageField(id = "isForeigner", name = "외국인 여부")
    private String isForeigner;
	
	@MessageField(id = "dayErrorMessage", name = "이용가능 날짜 체크 exception 메시지")
    private String dayErrorMessage;
	
	@MessageField(id = "timeErrorMessage", name = "이용가능 시간 체크 exception 메시지")
    private String timeErrorMessage;
	
	@MessageField(id = "ageErrorMessage", name = "이용가능 나이 체크 exception 메시지")
    private String ageErrorMessage;
	
	@MessageField(id = "abroadErrorMessage", name = "해외IP 허용여부 체크 exception 메시지")
    private String abroadErrorMessage;
	
	@MessageField(id = "userAge", name = "사용자 나이")
    private String userAge;
}
