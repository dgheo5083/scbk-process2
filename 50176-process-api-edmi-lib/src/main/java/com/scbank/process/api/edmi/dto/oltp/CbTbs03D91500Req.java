package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbTbs03D91500Req", type = Type.REQUEST, captureSystem = "OLTP", description = "인터넷뱅킹해지 요청부")
public class CbTbs03D91500Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "입력주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "USERNAME", name = "USERNAME")
    private String USERNAME;

    @MessageField(id = "GUBUNCODE", name = "GUBUNCODE")
    private String GUBUNCODE;

    @MessageField(id = "TXCODE", name = "TXCODE")
    private String TXCODE;

    @MessageField(id = "PAGELANG", name = "PAGELANG")
    private String PAGELANG;

    @MessageField(id = "REGSITE", name = "REGSITE")
    private String REGSITE;

    @MessageField(id = "TRAN_CHECK", name = "TRAN_CHECK")
    private String TRAN_CHECK;

    @MessageField(id = "JobId", name = "JobId")
    private String JobId;

    @MessageField(id = "IS_E2E", name = "IS_E2E")
    private String IS_E2E;

    @MessageField(id = "DealPart", name = "DealPart")
    private String DealPart;

    @MessageField(id = "redirectURL", name = "redirectURL")
    private String redirectURL;

    @MessageField(id = "CustName", name = "CustName")
    private String CustName;

}
