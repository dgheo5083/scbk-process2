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
@IntegrationMessage(id = "CbIbk01H76800Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "만기자동입금,재예치 조회.신청.해제 응답부")
public class CbIbk01H76800Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGJNO;

    @MessageField(id = "YOSINIL", name = "계좌신규일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSINIL;

    @MessageField(id = "YOGISIL", name = "계좌신규기산일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGISIL;

    @MessageField(id = "YOMANIL", name = "계좌만기일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOMANIL;

    @MessageField(id = "YOGIGAN", name = "계약기간", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGIGAN;

    @MessageField(id = "YOMANICH", name = "만기자동입금신청구분 Y:신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANICH;

    @MessageField(id = "YOMGJNO", name = "만기자동입금계좌", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMGJNO;

    @MessageField(id = "YOJYCGB", name = "재예치신청구분 1:신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJYCGB;

    @MessageField(id = "YODUMMY", name = "dummy", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}