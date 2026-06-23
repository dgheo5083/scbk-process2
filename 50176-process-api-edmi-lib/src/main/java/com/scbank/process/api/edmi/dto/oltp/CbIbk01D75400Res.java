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
@IntegrationMessage(id = "CbIbk01D75400Res", type = Type.RESPONSE, description = "마케팅 동의/변경 응답부", captureSystem = "OLTP")
public class CbIbk01D75400Res implements IMessageObject {

    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOGRGJIL", name = "거래기준일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRGJIL;

    @MessageField(id = "YOGRJN", name = "거래점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOGRJN;

    @MessageField(id = "YOPHONE01", name = "자택전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE01;

    @MessageField(id = "YOPHONE02", name = "직장전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE02;

    @MessageField(id = "YOPHONE03", name = "기타전화", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE03;

    @MessageField(id = "YOPHONE04", name = "휴대전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE04;

    @MessageField(id = "YOPHONE05", name = "SMS(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE05;

    @MessageField(id = "YOPHONE06", name = "제휴신용카드(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE06;

    @MessageField(id = "YOPHONE07", name = "당행제휴(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE07;

    @MessageField(id = "YOPHONE08", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE08;

    @MessageField(id = "YOPHONE09", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE09;

    @MessageField(id = "YOPHONE10", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE10;

    @MessageField(id = "YOMAIL01", name = "자택우편", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL01;

    @MessageField(id = "YOMAIL02", name = "직장전화", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL02;

    @MessageField(id = "YOMAIL03", name = "기타우편", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL03;

    @MessageField(id = "YOMAIL04", name = "이메일", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL04;

    @MessageField(id = "YOMAIL05", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL05;

    @MessageField(id = "YOMAIL06", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL06;

    @MessageField(id = "YOMAIL07", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL07;

    @MessageField(id = "YOMAIL08", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL08;

    @MessageField(id = "YOMAIL09", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL09;

    @MessageField(id = "YOMAIL10", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL10;

    @MessageField(id = "YOMUSE3", name = "개인정보수집이용동의 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMUSE3;

    @MessageField(id = "YOMUSEM3", name = "광고성정보수신동의 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMUSEM3;

    @MessageField(id = "YOLUSE22S", name = "개인(민감)정보분리 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOLUSE22S;

    @MessageField(id = "YODUMMY", name = "더미", length = 56, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
