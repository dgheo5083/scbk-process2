package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H93800Req", type = Type.REQUEST, captureSystem = "OLTP", description = "이체기능재사용 요청부")
public class CbTbs03H93800Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "주민사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "SafeCardKind", name = "보안카드종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardKind;

    @MessageField(id = "SafeCardINDEX", name = "안전카드 INDEX", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardINDEX;

    @MessageField(id = "SecurityValue", name = "안전카드값", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SecurityValue; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "SafeCardIssueNum", name = "안전카드 발급번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardIssueNum;

    @MessageField(id = "SafeCardBranchNum", name = "안전카드 점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SafeCardBranchNum;

    @MessageField(id = "SafeCardINDEX2", name = "안전카드 INDEX 2", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String SafeCardINDEX2;

    @MessageField(id = "SecurityValue2", name = "안전카드값2", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SecurityValue2; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "Dummy", name = "dummy", length = 399, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "PerBusNo1", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo1;

    @MessageField(id = "ChipNo", name = "칩번", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String TeleThree;
}
