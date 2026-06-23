package com.scbank.process.api.svc.common.service.certification.dto.financial;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 금융인증서 생체인증 등록허용 장치 목록 조회
 */
@Data
@IntegrationMessage(id = "CrtFctListFidoAvailableRequest", type = Type.REQUEST)
public class CrtFctListFidoAvailableRequest implements IMessageObject {

}