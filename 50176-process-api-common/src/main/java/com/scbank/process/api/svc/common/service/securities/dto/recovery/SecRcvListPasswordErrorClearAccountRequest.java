package com.scbank.process.api.svc.common.service.securities.dto.recovery;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계좌 비밀번호 오류건수 해제용 보유계좌조회
 */
@Data
@IntegrationMessage(id = "SecRcvListPasswordErrorClearAccountRequest", type = Type.REQUEST)
public class SecRcvListPasswordErrorClearAccountRequest implements IMessageObject {

}
