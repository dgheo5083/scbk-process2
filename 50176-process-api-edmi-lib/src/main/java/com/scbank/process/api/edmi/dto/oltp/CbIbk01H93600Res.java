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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbIbk01H93600Res", type = Type.RESPONSE, description = "지연이체 서비스 신청 및 해지 응답 전문")
public class CbIbk01H93600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGUBN", name = "업무구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGUBN;

    @MessageField(id = "YODELAYYES", name = "가입구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODELAYYES;

    @MessageField(id = "YOMSSU", name = "처리건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMSSU;

    @MessageField(id = "YOEXGYES", name = "예외계좌입금허용", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEXGYES;

    @MessageField(id = "YOTOHAN", name = "미지정이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private BigDecimal YOTOHAN;

    @MessageField(id = "YOREMAINTOHAN", name = "당일잔여한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private BigDecimal YOREMAINTOHAN;

    @MessageField(id = "YODummy", name = "더미", length = 38, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODummy;

    @MessageField(id = "REC_01", name = "출력계좌반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H93600Res/YOMSSU")
    private List<REC_01> REC_01;

    @Data
    public static class REC_01 implements IMessageObject {
        @MessageField(id = "YOIBANK", name = "지연이체 예외계좌은행코드", length = 3, align = AlignType.LEFT)
        private String YOIBANK;

        @MessageField(id = "YOIGEJA", name = "지연이체 예외계좌", length = 14, align = AlignType.LEFT)
        private String YOIGEJA;

        @MessageField(id = "YORESULT", name = "지연이체 예외계좌등록 결과", length = 1, align = AlignType.LEFT)
        private String YORESULT;

        @MessageField(id = "YOADUMMY", name = "지연이체 예외계좌 더미", length = 12, align = AlignType.LEFT)
        private String YOADUMMY;
    }

}
