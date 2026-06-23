package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03D28900Req", type = Type.REQUEST, description = "착오송금 본처리 요청부")
public class CbTbs03D28900Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AcctPassword;

    @MessageField(id = "YIICGB", name = "즉시/예약구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICGB;

    @MessageField(id = "YIICJR", name = "이체종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICJR;

    @MessageField(id = "YICGIL", name = "청구일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YICGIL;

    @MessageField(id = "YISEQN", name = "명세번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YISEQN;

    @MessageField(id = "YIIBKCD", name = "입금은행", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBKCD;

    @MessageField(id = "YITICGM", name = "총이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YITICGM;

    @MessageField(id = "YITSSR", name = "총수수료금액", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YITSSR;

    @MessageField(id = "YISSRGB", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISSRGB;

    @MessageField(id = "YIIDUMMY", name = "DUMMY", length = 200, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMMY;

    @MessageField(id = "YIJNO", name = "전문번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJNO;

    @MessageField(id = "YICHI", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHI;

    @MessageField(id = "YIBIL", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBIL;

    @MessageField(id = "YITE1", name = "번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE1;

    @MessageField(id = "YITE2", name = "번호2", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE2;

    @MessageField(id = "YITE3", name = "번호3", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE3;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "YIDM1", name = "DUMY1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDM1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "YIDM2", name = "DUMY2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDM2;

}
