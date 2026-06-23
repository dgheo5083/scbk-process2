package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class ListNoticeRecordResult implements IMessageObject {

    @MessageField(id = "seqno", name = "seqno")
    private int seqno;

    @MessageField(id = "wdate", name = "wdate")
    private String wdate;

    @MessageField(id = "title", name = "title")
    private String title;

    @MessageField(id = "rn", name = "rn")
    private String rn;

    @MessageField(id = "fxdBulltnFlg", name = "fxdBulltnFlg")
    private String fxdBulltnFlg;
}
