package com.scbank.process.api.svc.common.service.support.dto.contractdocument;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 계약서류제공 계좌목록 조회
 */
@Data
@IntegrationMessage(id = "SupDocListDocProvisionAccountRequest", type = Type.REQUEST)
public class SupDocListDocProvisionAccountRequest implements IMessageObject {

}