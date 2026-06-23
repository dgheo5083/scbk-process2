package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@IntegrationMessage(id = "CbIbk01H89400Res", type = Type.RESPONSE, description = "파기대상고객 해제거래 및 국가코드등록 응답부 응답부")

public class CbIbk01H89400Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGB;

    @MessageField(id = "YOKUK", name = "국가코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKUK;

}
