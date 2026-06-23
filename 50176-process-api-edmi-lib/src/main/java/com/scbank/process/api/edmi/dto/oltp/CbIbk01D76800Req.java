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
@IntegrationMessage(id = "CbIbk01D76800Req", type = Type.REQUEST, captureSystem = "OLTP", description = "만기자동입금,재예치 조회.신청.해제 요청부")
public class CbIbk01D76800Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIGPASS", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGPASS;

    @MessageField(id = "YIGRGB", name = "거래구분 1:만기자동입금, 2:재예치", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGB;

    @MessageField(id = "YIDRGB", name = "업무구분 1:신청, 2:해지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDRGB;

    @MessageField(id = "YIMGJNO", name = "만기자동입금계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMGJNO;

    @MessageField(id = "YIDUMMY", name = "dummy", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;
}
