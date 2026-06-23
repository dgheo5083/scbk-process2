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
@IntegrationMessage(id = "CbIbk01H15200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "대출이자납부 요청부")
public class CbIbk01H15200Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "LoanAcctNum", name = "대출계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String LoanAcctNum;

    @MessageField(id = "NapBuYeiJungDate", name = "납부예정일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer NapBuYeiJungDate;

    @MessageField(id = "Dummy", name = "dummy", length = 433, align = AlignType.LEFT, padding = StringUtils.SPACE)
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

}