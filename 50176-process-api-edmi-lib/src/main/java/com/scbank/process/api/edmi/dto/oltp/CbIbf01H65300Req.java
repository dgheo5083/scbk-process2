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
@IntegrationMessage(id = "CbIbf01H65300Req", type = Type.REQUEST, captureSystem = "OLTP", description = "영업점별 Fx고객번호")
public class CbIbf01H65300Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "PerBusNo", name = "주민/사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String PerBusNo;

    @MessageField(id = "continueFlag", name = "연속ID구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String continueFlag;

    @MessageField(id = "continueBranchNum", name = "연속출금계좌번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer continueBranchNum;

    @MessageField(id = "continueCustNum", name = "연속조회계좌번호", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer continueCustNum;
}
