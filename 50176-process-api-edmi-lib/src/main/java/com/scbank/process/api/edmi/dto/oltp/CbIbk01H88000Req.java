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
@IntegrationMessage(id = "CbIbk01H88000Req", type = Type.REQUEST, captureSystem = "OLTP", description = "고객목표설정금액 조회 요청부")
public class CbIbk01H88000Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIJGJNO", name = "조회계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJGJNO;

    @MessageField(id = "YIGRGBN", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGBN;

    @MessageField(id = "YIJHGB", name = "조회구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJHGB;

    @MessageField(id = "YICHNL", name = "채널구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHNL;

    @MessageField(id = "YIJHSTA", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHSTA;

    @MessageField(id = "YIJHEND", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJHEND;

    @MessageField(id = "YIDUMMY", name = "DUMMY", length = 52, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}