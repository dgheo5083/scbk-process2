package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

public class FindBranchAtmResult implements IMessageObject {
    @MessageField(id = "branchNo", name = "branchNo")
    private String branchNo;

    @MessageField(id = "branchNm", name = "branchNm")
    private String branchNm;

    @MessageField(id = "jibun", name = "jibun")
    private String jibun;

    @MessageField(id = "addr", name = "addr")
    private String addr;

    @MessageField(id = "latitude", name = "latitude")
    private String latitude;

    @MessageField(id = "longitude", name = "longitude")
    private String longitude;

    @MessageField(id = "subway", name = "subway")
    private String subway;

    @MessageField(id = "addressDesc", name = "addressDesc")
    private String addressDesc;

    @MessageField(id = "tel", name = "tel")
    private String tel;

    @MessageField(id = "businessHours", name = "businessHours")
    private String businessHours;

    @MessageField(id = "type", name = "type")
    private String type;
}
