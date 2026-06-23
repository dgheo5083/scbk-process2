package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 요청 정보 클래스
 * 공지사항 상세 조회
 */
@Data
@IntegrationMessage(id = "SupCscGetNoticeDetailRequest", type = Type.REQUEST)
public class SupCscGetNoticeDetailRequest implements IMessageObject {

    @MessageField(id = "seqno", name = "공지글번호")
    private int seqno;

    @MessageField(id = "language", name = "언어코드")
    private String language;

}
