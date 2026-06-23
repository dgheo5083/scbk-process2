package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01H92300Res", type = Type.RESPONSE, description = "출금계좌 등록 변경")
public class CbIbk01H92300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YISO1", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YISO1;

    @MessageField(id = "YIDNAME", name = "담당자성명", length = 14, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YIDNAME;

    @MessageField(id = "YISI1", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YISI1;

    @MessageField(id = "YOSGB1", name = "송금인코드", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSGB1;

    @MessageField(id = "YOSO3", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSO3;

    @MessageField(id = "YOSGBUB1", name = "송금인명사용법", length = 20, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSGBUB1;

    @MessageField(id = "YOSI3", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSI3;

    @MessageField(id = "YOHICHD1", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHICHD1;

    @MessageField(id = "YOLICHD1", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOLICHD1;

    @MessageField(id = "YOSO2", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSO2;

    @MessageField(id = "YODNAME2", name = "담당자성명", length = 14, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YODNAME2;

    @MessageField(id = "YOSI2", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSI2;

    @MessageField(id = "YOSGB2", name = "송금인코드", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSGB2;

    @MessageField(id = "YOSO4", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSO4;

    @MessageField(id = "YOSGBUB2", name = "송금인명사용법", length = 20, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSGBUB2;

    @MessageField(id = "YOSI4", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSI4;

    @MessageField(id = "YOHICHD2", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHICHD2;

    @MessageField(id = "YOLICHD2", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOLICHD2;

    @MessageField(id = "YOCHGJ", name = "해당출금계좌", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCHGJ;

    @MessageField(id = "YOSO5", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSO5;

    @MessageField(id = "YOMSG", name = "결과메세지", length = 36, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOMSG;

    @MessageField(id = "YOSI5", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOSI5;

    @MessageField(id = "YOCHINTDA", name = "인터넷출금허용", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YOCHINTDA;
}
