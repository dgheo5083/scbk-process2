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
@IntegrationMessage(id = "CbTbs07H41600Req", type = Type.REQUEST, captureSystem = "OLTP", description = "현대카드 e-그린세이브 M포인트 전환/해제 요청 전문")
public class CbTbs07H41600Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", encoding = "cp500", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encryptType = "cp500")
    private String TSPassword;

    @MessageField(id = "YIYGGB", name = "예금구분 (1 : 예금 , 2: 미정)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIYGGB;

    @MessageField(id = "YIGJNO", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO;

    @MessageField(id = "YIGPASS", name = "계좌비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String YIGPASS;

    @MessageField(id = "YIPNTGB", name = "Ｍ포인트전환여부(1:전환, 2:해제)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPNTGB;

    @MessageField(id = "DUMMY", name = "DUMMY", length = 35, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String DUMMY;
}
