package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbIbk01D88000Res", type = Type.REQUEST, captureSystem = "OLTP", description = "고객목표설정금액조회 응답부")
public class CbIbk01D88000Res implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YOBFSJAMT", name = "변경전설정금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBFSJAMT;

    @MessageField(id = "YOAFSJAMT", name = "변경후설정금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOAFSJAMT;

    @MessageField(id = "YIDUMMY", name = "DUMMY", length = 34, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}
