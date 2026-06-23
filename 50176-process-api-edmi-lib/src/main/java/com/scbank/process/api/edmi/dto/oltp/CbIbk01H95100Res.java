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
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbIbk01H95100Res", type = Type.RESPONSE, description = "스피드계좌 조회")
public class CbIbk01H95100Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJNO;

    @MessageField(id = "YOKMNM", name = "과목명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKMNM;

    @MessageField(id = "YOUPGBN", name = "등록해지여부(1:등록, 2:해지)", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOUPGBN;
}
