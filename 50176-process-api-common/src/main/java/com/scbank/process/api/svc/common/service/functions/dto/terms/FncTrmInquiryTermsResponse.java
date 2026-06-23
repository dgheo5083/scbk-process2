package com.scbank.process.api.svc.common.service.functions.dto.terms;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncTrmInquiryTermsResponse", type = Type.RESPONSE)
public class FncTrmInquiryTermsResponse implements IMessageObject {

    @MessageField(id = "resCode", name = "응답코드")
    private String resCode;

    @MessageField(id = "resourceRoot", name = "PDF 루트경로")
    private String resourceRoot;

    @MessageField(id = "termsInfoList", name = "약관정보", example = "")
    @RepeatedField
    private List<TermsInfo> termsInfoList;

}
