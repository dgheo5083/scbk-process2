package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class BusinessHourResult implements IMessageObject {

    @MessageField(id = "cgtryCd", name = "카테고리코드")
    private String cgtryCd;

    @MessageField(id = "ctgryNm", name = "카테고리명")
    private String ctgryNm;

    @MessageField(id = "contents", name = "내용")
    private String contents;
}
