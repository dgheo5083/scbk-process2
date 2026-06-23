package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class SearchNationResult implements IMessageObject {

    @MessageField(id = "country", name = "국가")
    private String country;

}
