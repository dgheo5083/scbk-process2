package com.scbank.process.api.edmi.dto.oltp;

import java.math.BigDecimal;

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
@IntegrationMessage(id = "CbIbk01H38300Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "계약서류제공 신탁 계약서조회")
public class CbIbk01H38300Res implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOGRGB", name = "거래구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGB;

    @MessageField(id = "YOBRNO", name = "계좌점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBRNO;

    @MessageField(id = "YOKMCD", name = "과목번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKMCD;

    @MessageField(id = "YOBUNHO", name = "계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBUNHO;

    @MessageField(id = "YOCIFNO", name = "CIF번호", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOCIFNO;

    @MessageField(id = "YOCNAME", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCNAME;

    @MessageField(id = "YOOPEIL", name = "계좌개설일자", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOOPEIL;

    @MessageField(id = "YOTOCD", name = "통화코드", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOTOCD;

    @MessageField(id = "YOSINAK", name = "계좌개설금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSINAK;

    @MessageField(id = "YOPRID", name = "상품번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPRID;

    @MessageField(id = "YOPNAME", name = "상품명", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPNAME;

    @MessageField(id = "YOCRISK", name = "고객투자등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCRISK;

    @MessageField(id = "YOPRISK", name = "상품위험등급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPRISK;

    @MessageField(id = "YOJAJUM", name = "연결계좌점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJAJUM;

    @MessageField(id = "YOJAKM", name = "연결계좌과목번호", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJAKM;

    @MessageField(id = "YOJABUN", name = "연결계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJABUN;

    @MessageField(id = "YOWBIL", name = "만기일자", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWBIL;

    @MessageField(id = "YOUNGB", name = "운용보고서통보", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOUNGB;

    @MessageField(id = "YOSUGB", name = "수익률／계산서통보", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSUGB;

    @MessageField(id = "YOALGB", name = "알림／이벤트통보", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOALGB;

    @MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZONG;

    @MessageField(id = "YOYSPCD", name = "세제유형", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYSPCD;

    @MessageField(id = "YOHANDO", name = "세제한도금액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOHANDO;

    @MessageField(id = "YOGRBB", name = "거래방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRBB;

    @MessageField(id = "YOBSJUM", name = "계좌관리점", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBSJUM;

    @MessageField(id = "YOTOGB", name = "계약서류통보방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTOGB;

    @MessageField(id = "YOGIGAN", name = "계약기간", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGIGAN;

    @MessageField(id = "YOPBOSU", name = "선취보수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOPBOSU;

    @MessageField(id = "YOGBOSU", name = "후취보수", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOGBOSU;

    @MessageField(id = "YOJDSSR", name = "중도해지수수료율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOJDSSR;

    @MessageField(id = "YOBSGB", name = "발송구분（신탁용)", length = 4, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBSGB;

    @MessageField(id = "YOURL", name = "URL정보", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOURL;

    @MessageField(id = "YOTONM", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTONM;

    @MessageField(id = "YOWJAGB", name = "자동이체등록여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWJAGB;

    @MessageField(id = "YOWJAJUM", name = "자동이체계좌점번호", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWJAJUM;

    @MessageField(id = "YOWJAKM", name = "자동이체계좌과목", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWJAKM;

    @MessageField(id = "YOWJABUN", name = "자동이체계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWJABUN;

    @MessageField(id = "YOWJAIL", name = "자동이체일자", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWJAIL;

    @MessageField(id = "YOWJAAMT", name = "자동이체금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.SPACE)
    private BigDecimal YOWJAAMT;

    @MessageField(id = "YOWSTIL", name = "자동이체시작일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWSTIL;

    @MessageField(id = "YOWLTIL", name = "자동이체종료일자", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOWLTIL;

    @MessageField(id = "YOFDSPYH", name = "펀드상품유형", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOFDSPYH;

    @MessageField(id = "YOJHYB", name = "31일간 조회 가능 여부 Y/N", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJHYB;

    @MessageField(id = "YOBCODE", name = "바코드생성정보", length = 700, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBCODE;

    @MessageField(id = "YOBD2DC", name = "방문판매관리번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBD2DC;

}