package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03D93500Res", type = Type.RESPONSE, description = "입금지정계좌")
public class CbTbs03D93500Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGUBN", name = "서비스구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBN;

    @MessageField(id = "YOMSSU", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMSSU;

    @MessageField(id = "YOTOHAN", name = "미지정이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOTOHAN;

    @MessageField(id = "REC_01", name = "이체명세부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbTbs03D93500Res/YOMSSU")
    private List<REC_01> REC_01;

    @Data
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "YOIBANK", name = "입금은행", length = 3, align = AlignType.LEFT)
        private String YOIBANK;

        @MessageField(id = "YOIGEJA", name = "계좌번호", length = 14, align = AlignType.LEFT)
        private String YOIGEJA;

        @MessageField(id = "YORESULT", name = "정상처리여부", length = 1, align = AlignType.LEFT)
        private String YORESULT;

    }
}