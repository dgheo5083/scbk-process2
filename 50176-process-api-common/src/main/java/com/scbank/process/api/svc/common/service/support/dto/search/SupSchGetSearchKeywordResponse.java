package com.scbank.process.api.svc.common.service.support.dto.search;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.EventResult;
import com.scbank.process.api.svc.common.dao.dto.FaqResult;
import com.scbank.process.api.svc.common.dao.dto.NoticeResult;
import com.scbank.process.api.svc.common.dao.dto.ProductResult;

import lombok.Data;

/**
 * CSL 서비스 응답 정보 클래스
 * 메뉴검색 검색어 검색
 */
@Data
@IntegrationMessage(id = "SupSchGetSearchKeywordResponse", type = Type.RESPONSE)
public class SupSchGetSearchKeywordResponse implements IMessageObject {

	@MessageField(id = "productOut", name = "상품 조회 결과")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<ProductResult> productOut;

	@MessageField(id = "faqOut", name = "FAQ 조회 결과")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<FaqResult> faqOut;

	@MessageField(id = "noticeOut", name = "공지사항 조회 결과")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<NoticeResult> noticeOut;

	@MessageField(id = "eventOut", name = "이벤트 조회 결과")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<EventResult> eventOut;

}