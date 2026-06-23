package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "CbIbf01H44200Req", type = Type.REQUEST, captureSystem = "OLTP", description = "펀드 외화 추가납입")
public class CbIbf01H44200Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TransPassword", name = "이체비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String TransPassword;

    @MessageField(id = "CgAcctNum", name = "출금계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
    private String CgAcctNum;

    @MessageField(id = "CgAcctPassword", name = "출금계좌 비밀번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "03")
    private String CgAcctPassword;

    @MessageField(id = "IgAcctNumFund", name = "입금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String IgAcctNumFund;

    @MessageField(id = "YIFCODE", name = "펀드코드", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIFCODE;

    @MessageField(id = "YIFZONG", name = "펀드종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIFZONG;

    @MessageField(id = "YITONM", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM;

    @MessageField(id = "YIGMAK", name = "납입금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIGMAK;

    @MessageField(id = "YICYAK", name = "청약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YICYAK;

    @MessageField(id = "YIPMSSR", name = "선취수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIPMSSR;

    @MessageField(id = "YIJSNO", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJSNO;

    @MessageField(id = "Dummy", name = "dummy", length = 113, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String Dummy;

}