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

import lombok.Data;

@Data
@IntegrationMessage(id = "MciIbAddr0101Res", type = Type.RESPONSE, description = "주소정제  응답부")
public class MciIbAddr0101Res implements IMessageObject {

    @MessageField(id = "TONEWJSOGB2", name = "신주소구분2", length = 1, multiBytes = true)
    private String TONEWJSOGB2;

    @MessageField(id = "TONEWJSOGB", name = "신주소구분", length = 1, multiBytes = true)
    private String TONEWJSOGB;

    @MessageField(id = "TONEWJSO1", name = "신주소1", length = 70, multiBytes = true)
    private String TONEWJSO1;

    @MessageField(id = "TONEWJSO4", name = "신주소4", length = 100, multiBytes = true)
    private String TONEWJSO4;

    @MessageField(id = "TOZIPCD1", name = "우편번호1", length = 6, multiBytes = true)
    private String TOZIPCD1;

    @MessageField(id = "TOZIPCD2", name = "우편번호2", length = 6, multiBytes = true)
    private String TOZIPCD2;

    @MessageField(id = "TONEWJSO2", name = "신주소2", length = 70, multiBytes = true)
    private String TONEWJSO2;

    @MessageField(id = "TONEWJSO5", name = "신주소5", length = 100, multiBytes = true)
    private String TONEWJSO5;

    @MessageField(id = "TOROADNACOD", name = "도로명코드", length = 12, multiBytes = true)
    private String TOROADNACOD;

    @MessageField(id = "TOROADSEQ3", name = "기타도로일련번호", length = 2, multiBytes = true)
    private String TOROADSEQ3;

    @MessageField(id = "TOJIHAYB", name = "지하여부", length = 1, multiBytes = true)
    private String TOJIHAYB;

    @MessageField(id = "TOBLDNO", name = "건물번호", length = 10, multiBytes = true)
    private String TOBLDNO;

    @MessageField(id = "TOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer TOJRPGSU;

    @MessageField(id = "TOJHNYKINF", name = "조회내역정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciIbAddr0101Res/TOJRPGSU")
    private List<TOJHNYKINF> TOJHNYKINF;

    @Data
    public static class TOJHNYKINF implements IMessageObject {

        @MessageField(id = "TOZIPCD1", name = "우편번호1", length = 6, multiBytes = true)
        private String TOZIPCD1;

        @MessageField(id = "TOZIPCD2", name = "우편번호2", length = 6, multiBytes = true)
        private String TOZIPCD2;

        @MessageField(id = "TONEWJSO3", name = "신주소3", length = 70, multiBytes = true)
        private String TONEWJSO3;

        @MessageField(id = "TONEWJSO5", name = "신주소5", length = 100, multiBytes = true)
        private String TONEWJSO5;

        @MessageField(id = "TOROADNACOD", name = "도로명코드", length = 12, multiBytes = true)
        private String TOROADNACOD;

        @MessageField(id = "TOROADSEQ3", name = "기타도로일련번호", length = 2, multiBytes = true)
        private String TOROADSEQ3;

        @MessageField(id = "TOJIHAYB", name = "지하여부", length = 1, multiBytes = true)
        private String TOJIHAYB;

        @MessageField(id = "TOBLDNO", name = "건물번호", length = 10, multiBytes = true)
        private String TOBLDNO;

    }
}