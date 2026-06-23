package com.scbank.process.api.svc.common.service.authenticator.dto.management;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 간편로그인 가입 본인확인
 */
@Data
@IntegrationMessage(id = "AthMgtValidateSimpLognPreRegistRequest", type = Type.REQUEST)
public class AthMgtValidateSimpLognPreRegistRequest implements IMessageObject {

}