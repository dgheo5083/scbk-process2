package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H38400Req", type = Type.REQUEST, captureSystem = "OLTP", description = "금융소비자보호법 BC카드 계약 카드조회 서비스")
public class CbIbk01H38400Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIGUBUN", name = "(1:카드번호, 2:주민+일자+고유번호)", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGUBUN;

    @MessageField(id = "YIHWNO", name = "카드번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIHWNO;

    @MessageField(id = "YIJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIILJA", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIILJA;

    @MessageField(id = "YIGOYU", name = "거래고유번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGOYU;

    @MessageField(id = "YIGNBIGB", name = "개인 법인 구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGNBIGB;

}