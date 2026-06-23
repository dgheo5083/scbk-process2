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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H88000Res", type = Type.REQUEST, captureSystem = "OLTP", description = "고객목표설정금액 조회 응답부")
public class CbIbk01H88000Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOSJAMT1", name = "설정금액당월", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSJAMT1;

    @MessageField(id = "YOSJAMT2", name = "설정금액익월", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSJAMT2;

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 34, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
