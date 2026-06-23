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
@IntegrationMessage(id = "CbIbk01H38500Req", type = Type.REQUEST, captureSystem = "OLTP", description = "계약서류제공 수신(입출금, 예금, 적금) 신청내역조회")
public class CbIbk01H38500Req implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자ID", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "로그인비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIUPGB", name = "업무구분 1-등록, 2-변경, 3-삭제, 9-계약정보조회, U-URL Link 조회", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIUPGB;

    @MessageField(id = "YIGRGB", name = "거래구분 10-신규, 11-신규취소, 20-해약, 21-해약취소, 30-전출입, 31-전출입취소", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGRGB;

    @MessageField(id = "YIBRNO", name = "점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBRNO;

    @MessageField(id = "YIKMCD", name = "과목", length = 2, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIKMCD;

    @MessageField(id = "YIBUNHO", name = "계좌번호", length = 6, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBUNHO;

    @MessageField(id = "YISINIL", name = "신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YISINIL;

    @MessageField(id = "YIDOCGB", name = "계약서류제공방법 1-서면(계좌조회서비스), 2-이메일, 3-문자메세지", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIDOCGB;

    @MessageField(id = "YIZONG", name = "종별", length = 2, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIZONG;

    @MessageField(id = "YIPRDNM", name = "상품명", length = 40, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIPRDNM;

    @MessageField(id = "YICUSNM", name = "고객성명", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YICUSNM;

    @MessageField(id = "YISGJAN", name = "신규금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YISGJAN;

    @MessageField(id = "YIGAIPJA", name = "가입자구분", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGAIPJA;

    @MessageField(id = "YISWHAN", name = "비과세한도", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YISWHAN;

    @MessageField(id = "YITBGJL", name = "예금잔액조회서", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITBGJL;

    @MessageField(id = "YIBSJUM", name = "계좌관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBSJUM;

    @MessageField(id = "YIMANIL", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMANIL;

    @MessageField(id = "YIIYUL", name = "약정이율", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIIYUL;

    @MessageField(id = "YIMANGJ", name = "만기자동입금계좌", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMANGJ;

    @MessageField(id = "YIMANTJ", name = "만기통지SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIMANTJ;

    @MessageField(id = "YIIJAGB", name = "이자지급방법 1-월이자지급식, 2-만기일시지급", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIIJAGB;

    @MessageField(id = "YIJYCGB", name = "만기재예치신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJYCGB;

    @MessageField(id = "YIGAYAK", name = "계약금액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIGAYAK;

    @MessageField(id = "YISAVGB", name = "적립방법 1-자유적립, 2-정기적립", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YISAVGB;

    @MessageField(id = "YIJYTJ", name = "납입지연SMS", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIJYTJ;

    @MessageField(id = "YIITDC", name = "IB담보대출신청", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIITDC;

    @MessageField(id = "YICMOIJ", name = "조작일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMOIJ;

    @MessageField(id = "YICMMIJ", name = "모드일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMMIJ;

    @MessageField(id = "YICMCBR", name = "취급점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YICMCBR;

    @MessageField(id = "YIAFGJN", name = "변경 후 계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIAFGJN;

    @MessageField(id = "YIURL", name = "URL고유번호", length = 30, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIURL;

    @MessageField(id = "YIJUMIN", name = "월부금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private BigDecimal YIJUMIN;

    @MessageField(id = "YIITNET", name = "주민번호", length = 13, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIITNET;

}