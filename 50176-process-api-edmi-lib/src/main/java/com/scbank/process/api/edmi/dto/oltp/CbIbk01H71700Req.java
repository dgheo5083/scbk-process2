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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H71700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "예금이율조회 요청 전문")
public class CbIbk01H71700Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자ID", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIYGJB", name = "예금종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYGJB;
}
