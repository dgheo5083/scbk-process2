package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationMessage(id = "CbCir08N08000Req", type = Type.REQUEST, description = "스피드조회 예금잔액증명서 조회", captureSystem = "OLTP")
public class CbCir08N08000Req implements IMessageObject {
    @MessageField(id = "YIUSID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUSID;

    @MessageField(id = "YIPASS", name = "이용자비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String YIPASS;

    @MessageField(id = "E2ERegNum", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String E2ERegNum;

    @MessageField(id = "YICMFNA", name = "고객명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICMFNA;

    @MessageField(id = "YITELRGNNO1", name = "전화지역번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITELRGNNO1;

    @MessageField(id = "YITELKNO1", name = "전화국번1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITELKNO1;

    @MessageField(id = "YITELNO1", name = "전화번호1", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITELNO1;

    @MessageField(id = "YIMBRGNNO", name = "핸드폰지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMBRGNNO;

    @MessageField(id = "YIMBKNO", name = "핸드폰국번", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMBKNO;

    @MessageField(id = "YIMBTELNO", name = "핸드폰전화번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMBTELNO;

    @MessageField(id = "YISEQNO2", name = "잔액증명발급번", length = 17, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISEQNO2;

    @MessageField(id = "YIGRIL", name = "거래일자", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRIL;

    @MessageField(id = "YIGJJN_01", name = "결제점번호_01", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_01;

    @MessageField(id = "YIGJWKM_01", name = "계좌과목_01", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_01;

    @MessageField(id = "YIGJWNO_01", name = "계좌번호_01", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_01;

    @MessageField(id = "YIWJJAN_01", name = "원화원장잔액_01", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_01;

    @MessageField(id = "YIFXWJJAN_01", name = "외화원장잔액_01", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_01;

    @MessageField(id = "YIGRCURCOD_01", name = "거래통화코드_01", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_01;

    @MessageField(id = "YIGJJN_02", name = "결제점번호_02", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_02;

    @MessageField(id = "YIGJWKM_02", name = "계좌과목_02", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_02;

    @MessageField(id = "YIGJWNO_02", name = "계좌번호_02", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_02;

    @MessageField(id = "YIWJJAN_02", name = "원화원장잔액_02", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_02;

    @MessageField(id = "YIFXWJJAN_02", name = "외화원장잔액_02", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_02;

    @MessageField(id = "YIGRCURCOD_02", name = "거래통화코드_02", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_02;

    @MessageField(id = "YIGJJN_03", name = "결제점번호_03", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_03;

    @MessageField(id = "YIGJWKM_03", name = "계좌과목_03", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_03;

    @MessageField(id = "YIGJWNO_03", name = "계좌번호_03", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_03;

    @MessageField(id = "YIWJJAN_03", name = "원화원장잔액_03", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_03;

    @MessageField(id = "YIFXWJJAN_03", name = "외화원장잔액_03", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_03;

    @MessageField(id = "YIGRCURCOD_03", name = "거래통화코드_03", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_03;

    @MessageField(id = "YIGJJN_04", name = "결제점번호_04", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_04;

    @MessageField(id = "YIGJWKM_04", name = "계좌과목_04", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_04;

    @MessageField(id = "YIGJWNO_04", name = "계좌번호_04", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_04;

    @MessageField(id = "YIWJJAN_04", name = "원화원장잔액_04", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_04;

    @MessageField(id = "YIFXWJJAN_04", name = "외화원장잔액_04", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_04;

    @MessageField(id = "YIGRCURCOD_04", name = "거래통화코드_04", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_04;

    @MessageField(id = "YIGJJN_05", name = "결제점번호_05", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_05;

    @MessageField(id = "YIGJWKM_05", name = "계좌과목_05", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_05;

    @MessageField(id = "YIGJWNO_05", name = "계좌번호_05", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_05;

    @MessageField(id = "YIWJJAN_05", name = "원화원장잔액_05", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_05;

    @MessageField(id = "YIFXWJJAN_05", name = "외화원장잔액_05", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_05;

    @MessageField(id = "YIGRCURCOD_05", name = "거래통화코드_05", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_05;

    @MessageField(id = "YIGJJN_06", name = "결제점번호_06", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_06;

    @MessageField(id = "YIGJWKM_06", name = "계좌과목_06", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_06;

    @MessageField(id = "YIGJWNO_06", name = "계좌번호_06", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_06;

    @MessageField(id = "YIWJJAN_06", name = "원화원장잔액_06", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_06;

    @MessageField(id = "YIFXWJJAN_06", name = "외화원장잔액_06", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_06;

    @MessageField(id = "YIGRCURCOD_06", name = "거래통화코드_06", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_06;

    @MessageField(id = "YIGJJN_07", name = "결제점번호_07", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJJN_07;

    @MessageField(id = "YIGJWKM_07", name = "계좌과목_07", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJWKM_07;

    @MessageField(id = "YIGJWNO_07", name = "계좌번호_07", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGJWNO_07;

    @MessageField(id = "YIWJJAN_07", name = "원화원장잔액_07", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIWJJAN_07;

    @MessageField(id = "YIFXWJJAN_07", name = "외화원장잔액_07", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIFXWJJAN_07;

    @MessageField(id = "YIGRCURCOD_07", name = "거래통화코드_07", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIGRCURCOD_07;

    @MessageField(id = "YIKNAME", name = "예금주명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKNAME;

}
