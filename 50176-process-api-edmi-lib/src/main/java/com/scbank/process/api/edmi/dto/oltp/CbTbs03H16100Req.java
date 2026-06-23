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
@IntegrationMessage(id = "CbTbs03H16100Req", type = Type.REQUEST, captureSystem = "OLTP", description = "한도대출 감액 요청부")
public class CbTbs03H16100Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YICHGU", name = "처리구분　　　", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHGU;

    @MessageField(id = "YIACCT", name = "대출계좌번호　", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String YIACCT;

    @MessageField(id = "YIBGJKM", name = "한도변경전금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIBGJKM;

    @MessageField(id = "YIBGHKM", name = "한도변경금액　", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIBGHKM;

    @MessageField(id = "YIJAMT", name = "이자금액　　　", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIJAMT;

    @MessageField(id = "Dummy", name = "dummy", length = 327, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "")
    private String TeleThree;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "Dummy1", name = "dummy1", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy1;

    @MessageField(id = "YIIPN", name = "IP정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "Dummy2", name = "dummy2", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy2;

}