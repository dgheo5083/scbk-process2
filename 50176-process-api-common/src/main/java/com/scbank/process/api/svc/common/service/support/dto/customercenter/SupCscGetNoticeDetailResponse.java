package com.scbank.process.api.svc.common.service.support.dto.customercenter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * PROCESS 서비스 응답 정보 클래스
 * 공지사항 상세 조회
 */
@Data
@IntegrationMessage(id = "SupCscGetNoticeDetailResponse", type = Type.RESPONSE)
public class SupCscGetNoticeDetailResponse implements IMessageObject {

    @MessageField(id = "contents", name = "내용")
    private String contents;

    @MessageField(id = "title", name = "제목")
    private String title;

    @MessageField(id = "wdate", name = "작성일")
    private String wdate;

    @MessageField(id = "seqno", name = "공지사항 번호")
    private int seqno;

    @MessageField(id = "fxdBulltnFlg", name = "상단고정여부")
    private String fxdBulltnFlg;

    @MessageField(id = "afterSeqno", name = "다음글 번호")
    private String afterSeqno;

    @MessageField(id = "afterTitle", name = "다음글 제목")
    private String afterTitle;

    @MessageField(id = "afterFxdBulltnFlg", name = "다음글 상단고정여부")
    private String afterFxdBulltnFlg;

    @MessageField(id = "beforSeqno", name = "이전글 번호")
    private String beforSeqno;

    @MessageField(id = "beforTitle", name = "이전글 제목")
    private String beforTitle;

}
