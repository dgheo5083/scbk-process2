package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data

public class SignInfoParameter implements IMessageObject {
    @MessageField(id = "userId", name = "", example = "JTEST017")
    private String userId;

    @MessageField(id = "userName", name = "", example = "")
    private String userName;

    @MessageField(id = "sysIp", name = "", example = "")
    private String sysIp;

    @MessageField(id = "sysName", name = "", example = "MA30")
    private String sysName;

    @MessageField(id = "gubunCode", name = "", example = "MA")
    private String gubunCode;

    @MessageField(id = "txCode", name = "", example = "D437")
    private String txCode;

    @MessageField(id = "regSite", name = "", example = "MA3ACTCLS001_80V")
    private String regSite;

    @MessageField(id = "regDay", name = "", example = "20260317")
    private String regDay;
}