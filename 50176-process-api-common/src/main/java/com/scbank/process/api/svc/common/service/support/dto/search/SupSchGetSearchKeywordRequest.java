package com.scbank.process.api.svc.common.service.support.dto.search;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 메뉴검색 검색어 검색
 */
@Data
@IntegrationMessage(id = "SupSchGetSearchKeywordRequest", type = Type.REQUEST)
public class SupSchGetSearchKeywordRequest implements IMessageObject {

	@MessageField(id = "searchWord", name = "검색어")
	private String searchWord;

}