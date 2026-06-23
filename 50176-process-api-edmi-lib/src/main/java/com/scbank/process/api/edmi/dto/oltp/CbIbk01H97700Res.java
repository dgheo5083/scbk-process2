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
@IntegrationMessage(id = "CbIbk01H97700Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "영업일조회 응답부")

public class CbIbk01H97700Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOYOGUHs", name = "요구일수BIT", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYOGUHs;

    @MessageField(id = "YOYOGU", name = "요구일수", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYOGU;

    @MessageField(id = "YOSILJA", name = "시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSILJA;

    @MessageField(id = "YOEILJA", name = "결과일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOEILJA;

    @MessageField(id = "YOPHGB", name = "평/휴일구분(1:평일,2:휴일(평일),3:휴일(휴일))", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHGB;

}
