package com.scbank.process.api.edmi.dto.mci;

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
@IntegrationMessage(id = "MciTm1517090001Req", type = Type.REQUEST, description = "직장정보 조회 요청부")
public class MciTm1517090001Req implements IMessageObject {

    @MessageField(id = "SIDUMMY15", name = "APIHEADIN", length = 15)
    private String SIDUMMY15;

    @MessageField(id = "SIRESEVE", name = "RESERVE", length = 9)
    private String SIRESEVE;

    @MessageField(id = "SIBRNO", name = "점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SIBRNO;

    @MessageField(id = "SILTERM", name = "LTERM번호", length = 5)
    private String SILTERM;

    @MessageField(id = "SIDEBUG", name = "디버그구분", length = 1)
    private String SIDEBUG;

    @MessageField(id = "SINXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SINXTTBL;

    @MessageField(id = "SITAXID", name = "사업자번호", length = 10)
    private String SITAXID;

    @MessageField(id = "SIUPCHNM", name = "업체명", length = 40)
    private String SIUPCHNM;

}