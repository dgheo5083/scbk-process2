package com.scbank.process.api.svc.common.service.functions.dto.terms;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncTrmInquiryTermsRequest", type = Type.REQUEST)
public class FncTrmInquiryTermsRequest implements IMessageObject {

    @MessageField(id = "actionType", name = "약관뷰어타입", example = "pdfViewer,htmlExplain")
    private String actionType;

    @MessageField(id = "loctnCdList", name = "언어코드", example = "LN1001,LN1002") // LN1001 : 한국어 / LN1002 : 영어
    @RepeatedField
    private List<String> loctnCdList;

    @MessageField(id = "prvsnCdList", name = "약관코드목록", example = "PRVCMM000225,PRVCMM000226,PRVFND012053,PRVFND004445")
    @RepeatedField
    private List<String> prvsnCdList;

}
