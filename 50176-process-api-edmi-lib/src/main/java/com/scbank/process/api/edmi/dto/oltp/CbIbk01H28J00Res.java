package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H28J00Res", type = Type.RESPONSE, description = "자행 착오송금 내역 조회 응답부", captureSystem = "OLTP")
public class CbIbk01H28J00Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자 ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOJHGB", name = "조회구분-1:반환대상,2:반환완료", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJHGB;

    @MessageField(id = "YOJUMIN", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMIN;

    @MessageField(id = "YOJHSTIL", name = "조회시작일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJHSTIL;

    @MessageField(id = "YOJHEDIL", name = "조회종료일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJHEDIL;

    @MessageField(id = "YOYCONT", name = "1:연속데이터있음", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYCONT;

    @MessageField(id = "YOYPAGE", name = "출력총페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOYPAGE;

    @MessageField(id = "YOYCNT", name = "출력요청페이지", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOYCNT;

    @MessageField(id = "YOJRPSU", name = "조립건수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOJRPSU;

    @MessageField(id = "YOGRINF", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "CbIbk01H28J00Res/YOJRPSU")
    private List<YOGRINF> YOGRINF;

    @Data
    public static class YOGRINF implements IMessageObject {

        @MessageField(id = "YOSEQN", name = "명세번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOSEQN;

        @MessageField(id = "YOWGRIL", name = "원거래일자(착오송금입금일)", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOWGRIL;

        @MessageField(id = "YOCGIL", name = "청구일자(반환요청일)", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOCGIL;

        @MessageField(id = "YOBHIL", name = "반환일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private String YOBHIL;

        @MessageField(id = "YOCSAYU", name = "청구사유코드", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCSAYU;

        @MessageField(id = "YOCGSYN", name = "청구기타사유-05(기타일경우 수기입력내용)", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOCGSYN;

        @MessageField(id = "YOWSUGJ", name = "원거래수취계좌(출금계좌번호)", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
        private String YOWSUGJ;

        @MessageField(id = "YOCGAK", name = "청구금액(착오입금액)", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal YOCGAK;

        @MessageField(id = "YOBHAK", name = "반환금액", length = 14, align = AlignType.RIGHT, padding = StringUtils.ZERO)
        private BigDecimal YOBHAK;

    }

    @MessageField(id = "YIDUMMY", name = "예비", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}
