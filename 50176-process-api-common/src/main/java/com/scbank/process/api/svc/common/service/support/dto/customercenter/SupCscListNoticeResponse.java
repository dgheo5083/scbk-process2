package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.ListNoticeRecordResult;

import lombok.Data;

/**
 * PROCESS 서비스 응답 정보 클래스
 * 공지사항 목록 조회
 */
@Data
@IntegrationMessage(id = "SupCscListNoticeResponse", type = Type.RESPONSE)
public class SupCscListNoticeResponse implements IMessageObject {

    @MessageField(id = "countRecord", name = "레코드갯수")
    private int countRecord;

    @MessageField(id = "noticeSelect", name = "공지사항 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<ListNoticeRecordResult> noticeSelect;

    @MessageField(id = "moreData", name = "moreData")
    private String moreData;

    @MessageField(id = "pageCount", name = "pageCount")
    private int pageCount;

}
