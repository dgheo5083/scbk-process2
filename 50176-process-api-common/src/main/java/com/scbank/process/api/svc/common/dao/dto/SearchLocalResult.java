package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class SearchLocalResult implements IMessageObject {

    @MessageField(id = "country", name = "국가")
    private String country;

    @MessageField(id = "region", name = "도시")
    private String region;

    @MessageField(id = "area", name = "지역")
    private String area;

}
