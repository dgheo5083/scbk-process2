package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class FaqResult implements IMessageObject {

	@MessageField(id = "seqNo", name = "seqNo")
    private String seqNo;

	@MessageField(id = "title", name = "title")
    private String title;

	@MessageField(id = "wDate", name = "wDate")
    private String wDate;

}
