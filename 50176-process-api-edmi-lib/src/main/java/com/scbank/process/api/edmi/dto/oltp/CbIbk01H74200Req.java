package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H74200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "증명서>연말정산증명서-주택자금상환증명서")
public class CbIbk01H74200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "YYDateSta", name = "최초조회일자", length = 8)
    private String YYDateSta;

    @MessageField(id = "YYDateEnd", name = "최종조회일자", length = 8)
    private String YYDateEnd;
}
