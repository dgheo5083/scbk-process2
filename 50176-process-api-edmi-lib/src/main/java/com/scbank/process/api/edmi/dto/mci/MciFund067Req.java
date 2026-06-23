package com.scbank.process.api.edmi.dto.mci;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "MciFund067Req", type = Type.REQUEST, captureSystem = "MCI", description = "펀드 실질투자수익률보고서조회 요청부")
public class MciFund067Req implements IMessageObject {
    @MessageField(id = "AIGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIGRGB;

    @MessageField(id = "AIJUMNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIJUMNO;

    @MessageField(id = "AIKMCOD", name = "과목코드", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIKMCOD;

    @MessageField(id = "AIGJWNO", name = "계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AIGJWNO;

    @MessageField(id = "AIOUTGB", name = "출력구분 (1:보유계좌, 2:해지계좌, UI에서 사용)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIOUTGB;

    @MessageField(id = "AIOUTGB1", name = "출력구분 1(2:APP, 3:LMS)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIOUTGB1;

    @MessageField(id = "AIOUTGB2", name = "출력구분 2", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIOUTGB2;

    @MessageField(id = "AICMFNO", name = "고객번호", length = 7, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AICMFNO;

    @MessageField(id = "AIJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIJMNO;

    @MessageField(id = "AIYYMM1", name = "기준년월", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AIYYMM1;

    @MessageField(id = "AILMSURL", name = "LMS URL", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AILMSURL;

}
