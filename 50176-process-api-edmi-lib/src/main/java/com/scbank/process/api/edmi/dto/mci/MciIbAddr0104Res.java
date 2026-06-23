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
@IntegrationMessage(id = "MciIbAddr0104Res", type = Type.RESPONSE, description = "주소찾기 응답부")
public class MciIbAddr0104Res implements IMessageObject {

    @MessageField(id = "TOTOTGSU", name = "총건수", length = 6)
    private Integer TOTOTGSU;

    @MessageField(id = "TOJRPGSU", name = "조립건수", length = 3)
    private Integer TOJRPGSU;

    @MessageField(id = "TOJHNYKINF", name = "조회내역정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbAddr0104Res/TOJRPGSU")
    private List<TOJHNYKINF> TOJHNYKINF;

    @Getter
    @Setter
    public static class TOJHNYKINF implements IMessageObject {

        @MessageField(id = "TOZIPCD3", name = "새우편번호", length = 5, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TOZIPCD3;

        @MessageField(id = "TOOLDJSO1", name = "구주소1", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TOOLDJSO1;

        @MessageField(id = "TOOLDJSO2", name = "구주소2", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TOOLDJSO2;

        @MessageField(id = "TONEWJSO18", name = "신주소18", length = 80, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TONEWJSO18;

        @MessageField(id = "TONEWJSO20", name = "신주소20", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TONEWJSO20;

        @MessageField(id = "TONENEWJSO1", name = "영문신주소1", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE, multiBytes = true)
        private String TONENEWJSO1;

    }
}