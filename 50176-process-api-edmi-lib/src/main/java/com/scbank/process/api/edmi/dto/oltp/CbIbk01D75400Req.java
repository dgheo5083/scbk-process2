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
@IntegrationMessage(id = "CbIbk01D75400Req", type = Type.REQUEST, description = "마케팅 동의/변경 요청부", captureSystem = "OLTP")
public class CbIbk01D75400Req implements IMessageObject {

    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPASS;

    @MessageField(id = "YIGRGBN", name = "거래구분(1:조회/2:등록)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGBN;

    @MessageField(id = "YIPHONE01", name = "자택전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE01;

    @MessageField(id = "YIPHONE02", name = "직장전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE02;

    @MessageField(id = "YIPHONE03", name = "기타전화", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE03;

    @MessageField(id = "YIPHONE04", name = "휴대전화(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE04;

    @MessageField(id = "YIPHONE05", name = "SMS(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE05;

    @MessageField(id = "YIPHONE06", name = "제휴신용카드(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE06;

    @MessageField(id = "YIPHONE07", name = "당행제휴(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE07;

    @MessageField(id = "YIPHONE08", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE08;

    @MessageField(id = "YIPHONE09", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE09;

    @MessageField(id = "YIPHONE10", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPHONE10;

    @MessageField(id = "YIMAIL01", name = "자택우편(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL01;

    @MessageField(id = "YIMAIL02", name = "직장우편(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL02;

    @MessageField(id = "YIMAIL03", name = "기타우편", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL03;

    @MessageField(id = "YIMAIL04", name = "이메일(Y/N)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL04;

    @MessageField(id = "YIMAIL05", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL05;

    @MessageField(id = "YIMAIL06", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL06;

    @MessageField(id = "YIMAIL07", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL07;

    @MessageField(id = "YIMAIL08", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL08;

    @MessageField(id = "YIMAIL09", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL09;

    @MessageField(id = "YIMAIL10", name = "Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAIL10;

    @MessageField(id = "YIMUSE3", name = "개인정보수집이용동의 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMUSE3;

    @MessageField(id = "YIMUSEM3", name = "광고성정보수신동의 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMUSEM3;

    @MessageField(id = "YILUSE22S", name = "개인(민감)정보분리 Y/N/SPACE", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YILUSE22S;

    @MessageField(id = "YIFIRST", name = "CIF최초생성일자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIFIRST;

    @MessageField(id = "YIJUMIN", name = "주민번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIDUMMY", name = "더미", length = 94, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}
