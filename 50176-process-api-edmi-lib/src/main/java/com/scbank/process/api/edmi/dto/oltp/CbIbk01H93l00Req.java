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
@IntegrationMessage(id = "CbIbk01H93l00Req", type = Type.REQUEST, captureSystem = "OLTP", description = "키보드뱅킹 공통 관리 전문 요청부")
public class CbIbk01H93l00Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGUBN", name = "처리구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBN;

    @MessageField(id = "YIUSEHAN", name = "이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIUSEHAN;

    @MessageField(id = "YICGEJA", name = "출금지정게좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICGEJA;

    @MessageField(id = "YIPSSCH", name = "키보드뱅킹 조회 계좌비밀번호 사용", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPSSCH;

    @MessageField(id = "YISVCD", name = "키보드뱅킹 서비스코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISVCD;

    @MessageField(id = "YIDUMMY", name = "Dummy", length = 96, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

    @MessageField(id = "YIIBANK0", name = "입금은행코드0", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBANK0;

    @MessageField(id = "YIIGEJA0", name = "인터넷입금계좌0", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGEJA0;

    @MessageField(id = "YISO10", name = "한글시작0", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISO10;

    @MessageField(id = "YIIJUKYO0", name = "입금계좌적요0", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIJUKYO0;

    @MessageField(id = "YISI10", name = "입금계좌적요0", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISI10;

    @MessageField(id = "YIIDUMMY0", name = "입력더미", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMMY0;
}
