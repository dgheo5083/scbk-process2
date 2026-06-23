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
@IntegrationMessage(id = "CbIbk01D88000Req", type = Type.REQUEST, captureSystem = "OLTP", description = "고객목표설정금액조회 요청부")
public class CbIbk01D88000Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "조회시작일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIJGJNO", name = "변경계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJGJNO;

    @MessageField(id = "YIGRGBN", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGBN;

    @MessageField(id = "YISJAMT", name = "설정금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISJAMT;

    @MessageField(id = "YICHNL", name = "채널구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHNL;

    @MessageField(id = "YIDUMMY", name = "DUMMY", length = 76, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;
}