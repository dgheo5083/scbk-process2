package com.scbank.process.api.svc.common.service.functions.dto.edoc;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncEdcViewResponse", type = Type.RESPONSE)
public class FncEdcViewResponse implements IMessageObject {

    @MessageField(id = "clipReportUrl", name = "")
    private String clipReportUrl;

    @MessageField(id = "custNo", name = "고객 번호")
    private String custNo;

    @MessageField(id = "tradNo", name = "거래 번호")
    private String tradNo;

    @MessageField(id = "bizType", name = "업무 구분")
    private String bizType;

    @MessageField(id = "clipDocTitle", name = "전자문서 제목")
    private String clipDocTitle;

    @MessageField(id = "prdctId", name = "")
    private String prdctId;

    @MessageField(id = "edocList", name = "")
    @RepeatedField
    private List<EdocData> edocDataList;

}
