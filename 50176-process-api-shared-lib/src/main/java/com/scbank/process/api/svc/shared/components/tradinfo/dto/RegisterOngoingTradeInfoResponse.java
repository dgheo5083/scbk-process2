package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterOngoingTradeInfoResponse", type = Type.RESPONSE)
public class RegisterOngoingTradeInfoResponse implements IMessageObject {
	@MessageField(id = "tradNo", name = "거래번호", example = "")
    private String tradNo;
	
	/*
     * smsResponse값이 'O'가 아닐 경우 데이타값을 다시한번 확인 바람
     * szStatus='P'; 이통사번호오류
     * szStatus='N'; 전화번호오류
     * szStatus='I'; 사용자ID오류
     * szStatus='D'; 회사코드오류
     * szStatus='M'; 메시지가 NULL인 경우
     * SzStatus='T'; 예약일자 이상
     * szStatus='I'; 등록된 회사가 아니거나 회원이 아니다.
     * szStatus='C'; 잔액초과
     * szStatus='O'; 호스트 성공 리턴코드
     * szStatus='X'; 호스트 실패 리턴코드
     */
	@MessageField(id = "sendLmsFlag", name = "LMS전송결과('O'가 아닐 경우 데이타값을 다시한번 확인 바람)", example = "")
	private String sendLmsFlag;
}
