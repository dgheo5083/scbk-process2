package com.scbank.process.api.edmi.dto.mci;

import java.math.BigDecimal;
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
@IntegrationMessage(id = "MciFund051Res", type = Type.RESPONSE, captureSystem = "MCI", description = "목표도달 대상가능계좌 조회 입력 응답부")
public class MciFund051Res implements IMessageObject {

    @MessageField(id = "AOOUTMACNA", name = "출력마크로명", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOOUTMACNA;

    @MessageField(id = "AOPRODCOD", name = "상품코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOPRODCOD;

    @MessageField(id = "AOJPNO", name = "전표번호", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOJPNO;

    @MessageField(id = "AOSMLTL8", name = "제목", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AOSMLTL8;

    @MessageField(id = "AOJUMIN", name = "주민등록번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "01")
    private String AOJUMIN;

    @MessageField(id = "AOCMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE, masking = true, maskingType = "04")
    private String AOCMFNA;

    @MessageField(id = "AOJHIL", name = "조회일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHIL;

    @MessageField(id = "AOJHTM", name = "조회시간", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJHTM;

    @MessageField(id = "AOJRPGSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer AOJRPGSU;

    @MessageField(id = "AOJHINF", name = "조회정보")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "MciFund051Res/AOJRPGSU")
    private List<AOJHINF> AOJHINF;

    @Data
    public static class AOJHINF implements IMessageObject {

        @MessageField(id = "REACH_ACCTNUM", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String REACH_ACCTNUM;

        @MessageField(id = "AOJBGB1", name = "종별구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOJBGB1;

        @MessageField(id = "AOSINIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSINIL;

        @MessageField(id = "AOFNDKNM1", name = "판매펀드명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOFNDKNM1;

        @MessageField(id = "AOPGAAK", name = "평가금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AOPGAAK;

        @MessageField(id = "AONIPAK", name = "납입금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal AONIPAK;

        @MessageField(id = "AONUJSURT", name = "누적수익율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AONUJSURT;

        @MessageField(id = "AOHMESSRYB", name = "환매수수료여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOHMESSRYB;

        @MessageField(id = "AORSLLFEEN", name = "환매수수료명", length = 18, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AORSLLFEEN;

        @MessageField(id = "AODRIL", name = "서비스등록일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AODRIL;

        @MessageField(id = "AOSERSTAIL", name = "서비스시작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSERSTAIL;

        @MessageField(id = "AOMPOSURT", name = "목표수익율", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOMPOSURT;

        @MessageField(id = "AOHMEYJGIL", name = "환매예정일자", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AOHMEYJGIL;

        @MessageField(id = "AOSERHJIL", name = "서비스해지일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOSERHJIL;

        @MessageField(id = "AODCGEYN", name = "대출가능여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String AODCGEYN;

        @MessageField(id = "AODCBY52", name = "대출비율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AODCBY52;

        @MessageField(id = "AOGJWMJN", name = "계좌관리점번호", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private Integer AOGJWMJN;
    }
}
