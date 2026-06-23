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
@IntegrationMessage(id = "CbIbk01H38600Req", type = Type.REQUEST, captureSystem = "OLTP", description = "계약서류조회(요구불)")
public class CbIbk01H38600Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIUPGB", name = "업무구분 1-등록, 2-변경, 3-삭제, 9-조회", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGB;

    @MessageField(id = "YIGRGB", name = "거래구분 10-신규, 11-신규취소, 20-고객변경, 30-전출입, 40-해약, 41-해약취소, 90-기타", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGB;

    @MessageField(id = "YIBRNO", name = "점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO;

    @MessageField(id = "YIKMCD", name = "과목", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIKMCD;

    @MessageField(id = "YIBUNHO", name = "계좌번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBUNHO;

    @MessageField(id = "YISINIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YISINIL;

    @MessageField(id = "YIJUMIN", name = "주민등록번호/사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUMIN;

    @MessageField(id = "YIDOCGB", name = "계약서류제공방법 1-서면(계좌조회서비스), 2-이메일, 3-문자메세지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDOCGB;

    @MessageField(id = "YIZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIZONG;

    @MessageField(id = "YIPRDNM", name = "상품명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPRDNM;

    @MessageField(id = "YICUSNM", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICUSNM;

    @MessageField(id = "YISGJAN", name = "신규금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YISGJAN;

    @MessageField(id = "YIGAIPJA", name = "가입자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGAIPJA;

    @MessageField(id = "YITBGJL", name = "예금잔액조회서", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITBGJL;

    @MessageField(id = "YIBSJUM", name = "계좌관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YIBSJUM;

    @MessageField(id = "YIMANIL", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMANIL;

    @MessageField(id = "YIIYUL", name = "약정이율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIYUL;

    @MessageField(id = "YIMANGJ", name = "만기자동입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMANGJ;

    @MessageField(id = "YIMANTJ", name = "만기통지SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMANTJ;

    @MessageField(id = "YICMOIJ", name = "조작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMOIJ;

    @MessageField(id = "YICMMIJ", name = "모드일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMMIJ;

    @MessageField(id = "YICMCBR", name = "취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMCBR;

    @MessageField(id = "YICURCOD", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICURCOD;

    @MessageField(id = "YICURNM", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICURNM;

    @MessageField(id = "YITJAGB", name = "투자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITJAGB;

    @MessageField(id = "YIITNET", name = "신규채널", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIITNET;

    @MessageField(id = "YIGRUSE", name = "거래목적", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRUSE;

    @MessageField(id = "YIJAGJR", name = "자금원천", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJAGJR;

    @MessageField(id = "YIDEALJ", name = "딜 정보", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIDEALJ;

    @MessageField(id = "YIJUNGSO", name = "중소기업", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJUNGSO;

    @MessageField(id = "YIJSSGB", name = "지급지시서구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJSSGB;

    @MessageField(id = "YIURL", name = "URL", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIURL;

    @MessageField(id = "YIDUMMY", name = "더미", length = 100, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDUMMY;

}