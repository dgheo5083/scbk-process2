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
@IntegrationMessage(id = "MciFund052Res", type = Type.RESPONSE, captureSystem = "MCI", description = "목표 도달 자동환매 응답부")
public class MciFund052Res implements IMessageObject {
    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJPNO;

    @MessageField(id = "AOSMLTL8", name = "소제목", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL8;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String AOCMFNA;

    @MessageField(id = "AOGRIL", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOGRIL;

    @MessageField(id = "AOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU;

    @MessageField(id = "AOGBLDRINF", name = "개별등록정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciFund052Res/AOJRPGSU")
    private List<AOGBLDRINF> AOGBLDRINF;

    @Data
    public static class AOGBLDRINF implements IMessageObject {

        @MessageField(id = "REACH_ACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "02")
        private String REACH_ACCTNUM;

        @MessageField(id = "AOINTFNDFNA1", name = "펀드FULL명", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOINTFNDFNA1;

        @MessageField(id = "AOMPOSURTA", name = "목표수익율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOMPOSURTA;

        @MessageField(id = "AOMPOSURT2", name = "목표수익율2", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOMPOSURT2;

        @MessageField(id = "AONUJSURT", name = "누적수익율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AONUJSURT;

        @MessageField(id = "AOSERSTAIL", name = "서비스시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSERSTAIL;

        @MessageField(id = "AOGIJIL", name = "기준일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOGIJIL;

    }
}
