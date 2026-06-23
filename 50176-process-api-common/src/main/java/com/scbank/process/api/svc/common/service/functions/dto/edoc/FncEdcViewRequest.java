package com.scbank.process.api.svc.common.service.functions.dto.edoc;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncEdcViewRequest", type = Type.REQUEST)
public class FncEdcViewRequest implements IMessageObject {

    @MessageField(id = "tranType", name = "")
    private String tranType;

    @MessageField(id = "custNo", name = "고객 번호", example = "9999")
    private String custNo;

    @MessageField(id = "tradNo", name = "거래 번호", example = "9999")
    private String tradNo;

    @MessageField(id = "bizType", name = "업무 구분", example = "HELS")
    private String bizType;

    @MessageField(id = "clipDocTitle", name = "전자문서 제목", example = "전자문서 뷰어 테스트 제목")
    private String clipDocTitle;

    @MessageField(id = "prdctId", name = "상품 아이디", example = "9979")
    private String prdctId;

    @MessageField(id = "edocList", name = "")
    @RepeatedField
    private List<EdocData> edocList;

}
