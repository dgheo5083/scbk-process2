package com.scbank.process.api.svc.common.service.securities.dto.blocking;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * PRC 서비스 요청 정보 클래스
 * 해외IP차단서비스 조회
 */
@Data
@IntegrationMessage(id = "SecBlkGetBlockingForeignIpRequest", description = "해외IP차단서비스 조회 요청 DTO", type = Type.REQUEST)
public class SecBlkGetBlockingForeignIpRequest implements IMessageObject {

}
