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
@IntegrationMessage(id = "CbIbk01H97700Req", type = Type.REQUEST, captureSystem = "OLTP", description = "영업일 조회 요청부")
public class CbIbk01H97700Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIYOGUHs", name = "요구일수BIT", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYOGUHs;

    @MessageField(id = "YIYOGU", name = "요구일수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIYOGU;

    @MessageField(id = "YISILJA", name = "시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISILJA;

}
