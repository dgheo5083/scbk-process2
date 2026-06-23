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
@IntegrationMessage(id = "CbIbk01H92300Req", type = Type.REQUEST, description = "출금계좌 등록 변경")
public class CbIbk01H92300Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "TSPassword2", name = "통신비밀번호2", length = 8, masking = true, maskingType = "03", align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword2;

    @MessageField(id = "YISO1", name = "한글시작", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YISO1;

    @MessageField(id = "YIDNAME", name = "담당자성명", length = 14, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YIDNAME;

    @MessageField(id = "YISI1", name = "한글끝", length = 1, align = AlignType.LEFT, padding = StringUtils.ZERO)
    private String YISI1;

    @MessageField(id = "YOHICHD", name = "1회이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHICHD;

    @MessageField(id = "YOLICHD", name = "1일이체한도", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOLICHD;

    @MessageField(id = "YICHGJ", name = "출금계좌번호", length = 11, masking = true, maskingType = "02", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHGJ;

    @MessageField(id = "YISGBUB", name = "송금인명사용법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISGBUB;

    @MessageField(id = "YICHINTYN", name = "출금계좌추가허용거래여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHINTYN;

    @MessageField(id = "YICHINTDA", name = "허용거래여부값", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHINTDA;
}
