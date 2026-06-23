package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbTbs03H90200Res", type = Type.RESPONSE, description = "온라인OTP 분실신고해제 거래 응답부")
public class CbTbs03H90200Res implements IMessageObject {
    @MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUSID;

    @MessageField(id = "YOKNAME", name = "국문성명", length = 32, masking = true, maskingType = "04", align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKNAME;

    @MessageField(id = "YOREVIL", name = "사고신고일시", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOREVIL;

    @MessageField(id = "YOHDDD1", name = "휴대지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHDDD1;

    @MessageField(id = "YOHGUK1", name = "휴대국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHGUK1;

    @MessageField(id = "YOHTEL1", name = "휴대전화번호", length = 4, masking = true, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHTEL1;

    @MessageField(id = "YOPSUPGB", name = "업체구분 001", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPSUPGB;

    @MessageField(id = "YOPSCNOD", name = "보안카드번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPSCNOD;

    @MessageField(id = "YOERRGB", name = "분실Y거래불가A분실N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOERRGB;

    @MessageField(id = "YODUMMY", name = "DUMMY", length = 86, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;

}