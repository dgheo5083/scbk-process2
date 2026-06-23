package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class EventResult implements IMessageObject {

	@MessageField(id = "seqNo", name = "seqNo")
    private String seqNo;

	@MessageField(id = "title", name = "title")
    private String title;

	@MessageField(id = "wDate", name = "wDate")
    private String wDate;

	@MessageField(id = "evtSDate", name = "evtSDate")
    private String evtSDate;

	@MessageField(id = "evtSTime", name = "evtSTime")
    private String evtSTime;

	@MessageField(id = "evtEDate", name = "evtEDate")
    private String evtEDate;

	@MessageField(id = "evtETime", name = "evtETime")
    private String evtETime;

	@MessageField(id = "attfileDtlImg", name = "attfileDtlImg")
    private String attfileDtlImg;

	@MessageField(id = "attfileDtlUrl", name = "attfileDtlUrl")
    private String attfileDtlUrl;

	@MessageField(id = "attfileSnlImg", name = "attfileSnlImg")
    private String attfileSnlImg;

	@MessageField(id = "attfileSnlUrl", name = "attfileSnlUrl")
    private String attfileSnlUrl;

	@MessageField(id = "ctgryCd", name = "ctgryCd")
    private String ctgryCd;

}
