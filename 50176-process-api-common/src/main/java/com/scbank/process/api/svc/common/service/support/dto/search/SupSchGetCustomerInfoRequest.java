package com.scbank.process.api.svc.common.service.support.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 전체메뉴 고객정보 조회
 */
@Data
@IntegrationMessage(id = "SupSchGetCustomerInfoRequest", type = Type.REQUEST)
public class SupSchGetCustomerInfoRequest implements IMessageObject {

}