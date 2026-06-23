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
@IntegrationMessage(id = "CbIbk01H11400Req", type = Type.REQUEST, captureSystem = "OLTP", description = "계좌상세조회 요청부")
public class CbIbk01H11400Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "AcctNum", name = "계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNum;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String AcctPassword;

    @MessageField(id = "PasswordVerifiYorN", name = "암호검증여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PasswordVerifiYorN;

    @MessageField(id = "PerNo", name = "주민등록번호", length = 13, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerNo;

    @MessageField(id = "Dummy", name = "Dummy", length = 423, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

    @MessageField(id = "InputJumin", name = "for Mobile", length = 13, masking = true, maskingType = "01", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String InputJumin;

    @MessageField(id = "ChipNo", name = "칩번호", length = 16, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String ChipNo;

    @MessageField(id = "CardIssueDate", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String CardIssueDate;

    @MessageField(id = "TeleOne", name = "지역번호", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TeleOne;

    @MessageField(id = "TeleTwo", name = "국번", length = 4, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TeleTwo;

    @MessageField(id = "TeleThree", name = "전화번호", length = 4, masking = true, maskingType = "03", align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String TeleThree;

}
