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
@IntegrationMessage(id = "CbIbk01H38500Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "계약서류제공 수신(입출금, 예금, 적금) 신청내역조회")
public class CbIbk01H38500Res implements IMessageObject {

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

    @MessageField(id = "YOGRGB", name = "거래구분 10-신규, 11-신규취소, 20-해약, 21-해약취소, 30-전출입, 31-전출입취소", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGRGB;

    @MessageField(id = "YOLASIL", name = "최종갱신일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOLASIL;

    @MessageField(id = "YODOCGB", name = "계약서류제공방법 1-서면(계좌조회서비스), 2-이메일, 3-문자메세지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODOCGB;

    @MessageField(id = "YOHPNO", name = "발송휴대폰번호", length = 12, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHPNO;

    @MessageField(id = "YOEMAIL", name = "발송이메일주소", length = 60, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOEMAIL;

    @MessageField(id = "YOZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOZONG;

    @MessageField(id = "YOPRDNM", name = "상품명", length = 42, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOPRDNM;

    @MessageField(id = "YOCUSNM", name = "고객명", length = 32, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCUSNM;

    @MessageField(id = "YOSGJAN", name = "신규금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSGJAN;

    @MessageField(id = "YOGAIPJA", name = "가입자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOGAIPJA;

    @MessageField(id = "YOSWHAN", name = "비과세한도", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOSWHAN;

    @MessageField(id = "YOTBGJL", name = "예금잔액조회서", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOTBGJL;

    @MessageField(id = "YOBSJUM", name = "계좌관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOBSJUM;

    @MessageField(id = "YOMANIL", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOMANIL;

    @MessageField(id = "YOIYUL", name = "약정이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YOIYUL;

    @MessageField(id = "YOMANGJ", name = "만기자동입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANGJ;

    @MessageField(id = "YOMANTJ", name = "만기통지SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOMANTJ;

    @MessageField(id = "YOIJAGB", name = "이자지급방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOIJAGB;

    @MessageField(id = "YOJYCGB", name = "만기재예치신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJYCGB;

    @MessageField(id = "YOGAYAK", name = "계약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOGAYAK;

    @MessageField(id = "YOSAVGB", name = "적립방법", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOSAVGB;

    @MessageField(id = "YOJYTJ", name = "납입지연SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOJYTJ;

    @MessageField(id = "YOITDC", name = "IB담보대출신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOITDC;

    @MessageField(id = "YOURL", name = "URL고유번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOURL;

    @MessageField(id = "YOBUGUM", name = "월부금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YOBUGUM;

    @MessageField(id = "YOBPYN", name = "방문판매여부", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOBPYN;

    @MessageField(id = "YODUMMY", name = "더미", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YODUMMY;
}