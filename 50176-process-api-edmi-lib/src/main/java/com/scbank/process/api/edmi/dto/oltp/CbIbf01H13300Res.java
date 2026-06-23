package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbf01H13300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "본인정보 이용 및 제공 조회")
public class CbIbf01H13300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOJMNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJMNO;

    @MessageField(id = "YOMILJA", name = "마케팅최종동의일자", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMILJA;

    @MessageField(id = "YOMCBR", name = "마케팅최종동의장소", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMCBR;

    @MessageField(id = "YOPHONE1", name = "마케팅동의여부TM(자택)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE1;

    @MessageField(id = "YOPHONE2", name = "마케팅동의여부TM(직장)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE2;

    @MessageField(id = "YOPHONE3", name = "마케팅동의여부TM(기타)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE3;

    @MessageField(id = "YOPHONE4", name = "마케팅동의여부TM(휴대폰)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE4;

    @MessageField(id = "YOPHONE5", name = "마케팅동의여부TM(SMS)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE5;

    @MessageField(id = "YOPHONE_DUMMY", name = "마케팅동의여부TM(더미)", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPHONE_DUMMY;

    @MessageField(id = "YOMAIL1", name = "마케팅동의여부DM(자택)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL1;

    @MessageField(id = "YOMAIL2", name = "마케팅동의여부DM(직장)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL2;

    @MessageField(id = "YOMAIL3", name = "마케팅동의여부DM(기타)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL3;

    @MessageField(id = "YOMAIL4", name = "마케팅동의여부DM(이메일)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL4;

    @MessageField(id = "YOMAIL_DUMMY", name = "마케팅동의여부DM(더미)", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMAIL_DUMMY;

    @MessageField(id = "YODWTEL", name = "권유동의전화", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODWTEL;

    @MessageField(id = "YODWSMS", name = "권유동의SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODWSMS;

    @MessageField(id = "YODWSEM", name = "권유동의서면", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODWSEM;

    @MessageField(id = "YODWEML", name = "권유동의이메일", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODWEML;

    @MessageField(id = "YOIBDWIL", name = "권유동의일", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOIBDWIL;

    @MessageField(id = "YOIBDWCN", name = "동의장소", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIBDWCN;

    @MessageField(id = "YOCA111", name = "고객신규동의여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCA111;

    @MessageField(id = "YOKNAME", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKNAME;

    @MessageField(id = "YODUMMY", name = "더미", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}
