package com.scbank.process.api.edmi.dto.oltp;

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
@IntegrationMessage(id = "CbIbk01H38600Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "계약서류조회(요구불)")
public class CbIbk01H38600Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOBRNO", name = "점", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBRNO;

    @MessageField(id = "YOKMCD", name = "과목", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOKMCD;

    @MessageField(id = "YOBUNHO", name = "계좌번호", length = 6, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBUNHO;

    @MessageField(id = "YOSINIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOSINIL;

    @MessageField(id = "YOJUMIN", name = "주민등록번호/사업자번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUMIN;

    @MessageField(id = "YOGRGB", name = "거래구분 10-신규, 11-신규취소, 20-고객변경, 30-전출입, 40-해약, 41-해약취소, 90-기타", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGB;

    @MessageField(id = "YOLASIL", name = "최종갱신일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOLASIL;

    @MessageField(id = "YODOCGB", name = "계약서류제공방법 1-서면(계좌조회서비스), 2-이메일, 3-문자메세지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODOCGB;

    @MessageField(id = "YOHPNO", name = "발송휴대폰번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHPNO;

    @MessageField(id = "YOEMAIL", name = "발송이메일주소", length = 62, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEMAIL;

    @MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZONG;

    @MessageField(id = "YOPRDNM", name = "상품명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPRDNM;

    @MessageField(id = "YOCUSNM", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCUSNM;

    @MessageField(id = "YOSGJAN", name = "신규금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSGJAN;

    @MessageField(id = "YOGAIPJA", name = "가입자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGAIPJA;

    @MessageField(id = "YOTBGJL", name = "예금잔액조회서", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTBGJL;

    @MessageField(id = "YOBSJUM", name = "계좌관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOBSJUM;

    @MessageField(id = "YOMANIL", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMANIL;

    @MessageField(id = "YOIYUL", name = "약정이율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOIYUL;

    @MessageField(id = "YOMANGJ", name = "만기자동입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANGJ;

    @MessageField(id = "YOMANTJ", name = "만기통지SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANTJ;

    @MessageField(id = "YOCURCOD", name = "통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCURCOD;

    @MessageField(id = "YOCURNM", name = "통화명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCURNM;

    @MessageField(id = "YOTJAGB", name = "투자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTJAGB;

    @MessageField(id = "YOITNET", name = "신규채널", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOITNET;

    @MessageField(id = "YOGRUSE", name = "거래목적", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRUSE;

    @MessageField(id = "YOJAGJR", name = "자금원천", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJAGJR;

    @MessageField(id = "YODEALJ", name = "딜 정보", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YODEALJ;

    @MessageField(id = "YOJUNGSO", name = "중소기업", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJUNGSO;

    @MessageField(id = "YOJSSGB", name = "지급지시서구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJSSGB;

    @MessageField(id = "YOURL", name = "URL", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOURL;

    @MessageField(id = "YOMNJGB", name = "방문판매여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMNJGB;
}