package com.scbank.process.api.svc.common.service.sample.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class TestTransferInfo implements IMessageObject {

    // @MessageField(id = "IMS_TRAN_CODE", name = "")
    // private String IMS_TRAN_CODE;

    // @MessageField(id = "IN_CLASS_CODE", name = "")
    // private String IN_CLASS_CODE;

    // @MessageField(id = "SERVICE_CODE", name = "")
    // private String SERVICE_CODE;

    @MessageField(id = "JbCode", name = "")
    private String jbCode;

    @MessageField(id = "JYCode", name = "")
    private String jYCode;

    @MessageField(id = "CgAcctNum", name = "")
    private String cgAcctNum;

    @MessageField(id = "TotFeeAmt", name = "")
    private String totFeeAmt;

    @MessageField(id = "HgSName", name = "")
    private String hgSName;

    @MessageField(id = "GrNum", name = "")
    private String grNum;

    @MessageField(id = "YyDate", name = "")
    private String yyDate;

    @MessageField(id = "FeeCode", name = "")
    private String feeCode;

    @MessageField(id = "CMSCode", name = "")
    private String cMSCode;

    @MessageField(id = "TotSgAmt", name = "")
    private String totSgAmt;

    @MessageField(id = "SgAmt", name = "")
    private String sgAmt;

    @MessageField(id = "JsNum", name = "")
    private String jsNum;

    @MessageField(id = "IgBankCode", name = "")
    private String igBankCode;

    @MessageField(id = "YyTime", name = "")
    private String yyTime;

    @MessageField(id = "HgRName", name = "")
    private String hgRName;

    @MessageField(id = "FeeAmt", name = "")
    private String feeAmt;

    @MessageField(id = "JgCode", name = "")
    private String jgCode;

}
