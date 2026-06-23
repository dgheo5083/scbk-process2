package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "MciIbEdms002Req", type = Type.REQUEST, captureSystem = "MCI", description = "EDMS 계약서류조회 조회수/다운로드수 증가")
public class MciIbEdms002Req implements IMessageObject {

    @MessageField(id = "PROC_MK", name = "처리구분", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PROC_MK;

    @MessageField(id = "ELEMENT_ID", name = "엘리먼트 ID", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ELEMENT_ID;

    @MessageField(id = "ACCT_NO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ACCT_NO;

    @MessageField(id = "DOC_CODE", name = "문서코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DOC_CODE;

}