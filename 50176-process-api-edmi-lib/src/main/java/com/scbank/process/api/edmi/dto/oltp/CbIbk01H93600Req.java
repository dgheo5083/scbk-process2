package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbIbk01H93600Req", type = Type.REQUEST, description = "지연이체 서비스 신청 및 해지 요청 전문", captureSystem = "OLTP")
public class CbIbk01H93600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TransPassword;

    @MessageField(id = "YIGUBN", name = "처리구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBN;

    @MessageField(id = "YIUSEHAN", name = "미지정이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIUSEHAN;

    @MessageField(id = "YIMSSU", name = "처리건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMSSU;

    @MessageField(id = "YIDUMMY", name = "더미", length = 50, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

    @MessageField(id = "YIIBANK0", name = "입금은행코드0", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK0;

    @MessageField(id = "YIIGEJA0", name = "인터넷입금계좌0", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA0;

    @MessageField(id = "YIIDUMY0", name = "더미0", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY0;

    @MessageField(id = "YIIBANK1", name = "입금은행코드1", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK1;

    @MessageField(id = "YIIGEJA1", name = "인터넷입금계좌1", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA1;

    @MessageField(id = "YIIDUMY1", name = "더미1", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY1;

    @MessageField(id = "YIIBANK2", name = "입금은행코드2", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK2;

    @MessageField(id = "YIIGEJA2", name = "인터넷입금계좌2", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA2;

    @MessageField(id = "YIIDUMY2", name = "더미2", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY2;

    @MessageField(id = "YIIBANK3", name = "입금은행코드3", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK3;

    @MessageField(id = "YIIGEJA3", name = "인터넷입금계좌3", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA3;

    @MessageField(id = "YIIDUMY3", name = "더미3", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY3;

    @MessageField(id = "YIIBANK4", name = "입금은행코드4", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK4;

    @MessageField(id = "YIIGEJA4", name = "인터넷입금계좌4", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA4;

    @MessageField(id = "YIIDUMY4", name = "더미4", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY4;

    @MessageField(id = "YIIBANK5", name = "입금은행코드5", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK5;

    @MessageField(id = "YIIGEJA5", name = "인터넷입금계좌5", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA5;

    @MessageField(id = "YIIDUMY5", name = "더미5", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMY5;
}
