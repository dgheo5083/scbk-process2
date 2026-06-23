package com.scbank.process.api.edmi.dto.oltp;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H44N00Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드정보조회 (실질투자수익)")
public class CbIbk01H44N00Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOGJNO", name = "계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGJNO;

    @MessageField(id = "YOYWSU", name = "연월건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOYWSU;

    @MessageField(id = "YOYW", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H44N00Res/YOYWSU")
    private List<YOYW> YOYW;

    @Getter
    @Setter
    public static class YOYW implements IMessageObject {

        @MessageField(id = "YOYYYYMM", name = "연월", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOYYYYMM;

        @MessageField(id = "YOSJSUYUL", name = "실질투자수익율", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer YOSJSUYUL;

    }
}