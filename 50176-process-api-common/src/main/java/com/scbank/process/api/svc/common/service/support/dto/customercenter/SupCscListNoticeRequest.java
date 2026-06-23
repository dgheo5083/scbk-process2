package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 요청 정보 클래스
 * 공지사항 목록 조회
 */
@Data
@IntegrationMessage(id = "SupCscListNoticeRequest", type = Type.REQUEST)
public class SupCscListNoticeRequest implements IMessageObject {

    @MessageField(id = "language", name = "언어코드")
    private String language;

    @MessageField(id = "pageSize", name = "페이지사이즈")
    private String pageSize;

    @MessageField(id = "paging", name = "페이징")
    private String paging;

    @MessageField(id = "pageCount", name = "페이지수")
    private String pageCount;

}
