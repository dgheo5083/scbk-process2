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
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "CbTbs03H21600Req", type = Type.REQUEST, description = "키보드,전화번호이체 통합전문 요청 전문", captureSystem = "OLTP")
public class CbTbs03H21600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIICJR", name = "이체종류", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIICJR;

    @MessageField(id = "YICRGB", name = " 처리구분 1:키보드뱅킹 2:전화번호이체 ", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICRGB;

    @MessageField(id = "YIOGJNO", name = "출금계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIOGJNO;

    @MessageField(id = "YITICGM", name = "이체금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YITICGM;

    @MessageField(id = "YIIBKCD", name = "입금계좌은행코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIBKCD;

    @MessageField(id = "YIIGJNO", name = "입금계좌번호 ( 실행 )", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGJNO;

    @MessageField(id = "YIICILJA", name = "거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIICILJA;

    @MessageField(id = "YIJSNO", name = "거래접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJSNO;

    @MessageField(id = "YICHDATE", name = "원거래일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YICHDATE;

    @MessageField(id = "YIJSNOWON", name = "원거래접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJSNOWON;

    @MessageField(id = "AcctPassword", name = "계좌비밀번호", length = 4, masking = true, maskingType = "03")
    private String AcctPassword; // _DNFE2E_ _/DNFE2E_ _DVKEY_ _/DVKEY_

    @MessageField(id = "YITSSR", name = "총수수료금액", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YITSSR;

    @MessageField(id = "YISSRGB", name = "수수료구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISSRGB;

    @MessageField(id = "YIIGHPNO", name = "받는사람휴대폰번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIGHPNO;

    @MessageField(id = "YIONAME", name = "보내는사람", length = 26, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String YIONAME;

    @MessageField(id = "YISCNM", name = "수취인명－받는사람", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE, sosi = true)
    private String YISCNM;

    @MessageField(id = "YITEL", name = "연락전화번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITEL;

    @MessageField(id = "YICRGB2", name = "처리구분2 1:발신인이체 예비처리", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICRGB2;

    @MessageField(id = "YINEWKEY", name = "PAYKEY 예비거래 PW스킵 : Y", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YINEWKEY;

    @MessageField(id = "YIIDUMMY", name = "더미", length = 224, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIDUMMY;

    @MessageField(id = "YIJNO", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJNO;

    @MessageField(id = "YICHI", name = "칩번호", length = 16, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICHI;

    @MessageField(id = "YIBIL", name = "카드발급일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBIL;

    @MessageField(id = "YITE1", name = "지역번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE1;

    @MessageField(id = "YITE2", name = "국", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE2;

    @MessageField(id = "YITE3", name = "번호", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITE3;

    @MessageField(id = "YIJIL", name = "주문일", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIJIL;

    @MessageField(id = "YIJUMUN", name = "주문번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMUN;

    @MessageField(id = "YIDM1", name = "DUMY", length = 33, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDM1;

    @MessageField(id = "YIIPN", name = "IP 정보", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIPN;

    @MessageField(id = "YIMAC", name = "MAC 정보", length = 20, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMAC;

    @MessageField(id = "YIDM2", name = "DUMY", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDM2;
}
