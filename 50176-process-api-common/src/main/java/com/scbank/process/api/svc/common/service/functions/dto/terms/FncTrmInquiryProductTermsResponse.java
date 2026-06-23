package com.scbank.process.api.svc.common.service.functions.dto.terms;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncTrmInquiryProductTermsResponse", type = Type.RESPONSE)
public class FncTrmInquiryProductTermsResponse implements IMessageObject {

    @MessageField(id = "resCode", name = "응답코드")
    private String resCode;

    @MessageField(id = "resourceRoot", name = "PDF 루트경로")
    private String resourceRoot;

    @MessageField(id = "productTermsInfo", name = "약관정보", example = "")
    private ProductTermsInfo productTermsInfo;

}
