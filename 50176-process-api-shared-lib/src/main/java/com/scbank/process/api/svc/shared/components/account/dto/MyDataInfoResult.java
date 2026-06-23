package com.scbank.process.api.svc.shared.components.account.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class MyDataInfoResult implements IMessageObject {
    @MessageField(id = "orgCd", name = "orgCd")
    private String orgCd;

    @MessageField(id = "orgMkCd", name = "orgMkCd")
    private String orgMkCd;

    @MessageField(id = "orgName", name = "orgName")
    private String orgName;

    @MessageField(id = "orgImg", name = "orgImg")
    private String orgImg;

    @MessageField(id = "orgImgName", name = "orgCd")
    private String orgImgName;

    @MessageField(id = "orgFlag", name = "orgFlag")
    private String orgFlag;

    @MessageField(id = "orgOrder", name = "orgOrder")
    private String orgOrder;

    @MessageField(id = "openBnkFlag", name = "openBnkFlag")
    private String openBnkFlag;

    @MessageField(id = "openBnkCd", name = "openBnkCd")
    private String openBnkCd;
}
