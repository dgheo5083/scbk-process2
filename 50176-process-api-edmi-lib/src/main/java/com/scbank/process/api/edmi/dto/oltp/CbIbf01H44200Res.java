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
@IntegrationMessage(id = "CbIbf01H44200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "펀드 외화 추가납입")
public class CbIbf01H44200Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String YOUSID;

    @MessageField(id = "YOJGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
    private String YOJGJNO;

    @MessageField(id = "YOIGJNO", name = "입금계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO, masking = true, maskingType = "02")
    private String YOIGJNO;

    @MessageField(id = "YOFCODE", name = "펀드코드", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOFCODE;

    @MessageField(id = "YOTONM", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTONM;

    @MessageField(id = "YOGMAK", name = "납입금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGMAK;

    @MessageField(id = "YOCYAK", name = "청약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOCYAK;

    @MessageField(id = "YOPMSSR", name = "선취수수료", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOPMSSR;

    @MessageField(id = "YOSINIL", name = "신규일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSINIL;

    @MessageField(id = "YOMANIL", name = "만기일", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANIL;

    @MessageField(id = "YOYEIL", name = "입금예정일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYEIL;

    @MessageField(id = "YOJSNO", name = "접수번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJSNO;

    @MessageField(id = "YODUMMY", name = "dummy", length = 181, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}