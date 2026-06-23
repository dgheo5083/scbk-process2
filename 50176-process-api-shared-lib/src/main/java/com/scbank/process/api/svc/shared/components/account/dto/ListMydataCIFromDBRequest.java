package com.scbank.process.api.svc.shared.components.account.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 마이데이터 CI 조회
 */
@Data
@IntegrationMessage(id = "ListMydataCIFromDBRequest", type = Type.REQUEST)
public class ListMydataCIFromDBRequest implements IMessageObject {

}
