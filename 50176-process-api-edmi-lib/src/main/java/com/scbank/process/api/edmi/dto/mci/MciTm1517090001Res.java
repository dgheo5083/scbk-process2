package com.scbank.process.api.edmi.dto.mci;

import java.util.List;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.AlignType;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "MciTm1517090001Res", type = Type.RESPONSE, description = "직장정보 조회 응답부")
public class MciTm1517090001Res implements IMessageObject {

    @MessageField(id = "SORESERVE5", name = "APIHEADOUT", length = 5)
    private String SORESERVE5;

    @MessageField(id = "SOERRCOD", name = "에러코드", length = 4)
    private String SOERRCOD;

    @MessageField(id = "SOERRNAME", name = "ERROR한글명30", length = 30)
    private String SOERRNAME;

    @MessageField(id = "SOTOTCNT", name = "전체건수", length = 7)
    private Integer SOTOTCNT;

    @MessageField(id = "SONXTTBL", name = "연속거래", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String SONXTTBL;

    @MessageField(id = "SOJRPGSU", name = "조립건수", length = 3)
    private Integer SOJRPGSU;

    @MessageField(id = "SOJHINF", name = "조회정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciTm1517090001Res/SOJRPGSU")
    private List<SOJHINF> SOJHINF;

    @Getter
    @Setter
    public static class SOJHINF implements IMessageObject {
        @MessageField(id = "SOEBRMUPCCOD", name = "EB업체코드", length = 6)
        private String SOEBRMUPCCOD;

        @MessageField(id = "SOUPCHNM", name = "업체명", length = 40)
        private String SOUPCHNM;

        @MessageField(id = "SOTAXID", name = "사업자번호", length = 10)
        private String SOTAXID;
    }
}