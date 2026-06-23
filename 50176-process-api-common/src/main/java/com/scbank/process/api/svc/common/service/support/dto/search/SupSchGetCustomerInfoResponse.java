package com.scbank.process.api.svc.common.service.support.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 전체메뉴 고객정보 조회
 */
@Data
@IntegrationMessage(id = "SupSchGetCustomerInfoResponse", type = Type.RESPONSE)
public class SupSchGetCustomerInfoResponse implements IMessageObject {

	@MessageField(id = "lstCnnCtnDt", name = "최근 접속시각")
	private String lstCnnCtnDt;

	@MessageField(id = "loginType", name = "로그인 타입")
	private String loginType;

	@MessageField(id = "serno", name = "고객일련번호")
	private String serno;

	@MessageField(id = "cnfrmNo", name = "디바이스번호")
	private String cnfrmNo;

	@MessageField(id = "breezePushJoinYN", name = "PUSH 가입 여부 체크")
	private String breezePushJoinYN;

	@MessageField(id = "hdCardYn", name = "현대카드 보유여부")
	private String hdCardYn;

	@MessageField(id = "bcCardYn", name = "BC카드 보유여부")
	private String bcCardYn;

}