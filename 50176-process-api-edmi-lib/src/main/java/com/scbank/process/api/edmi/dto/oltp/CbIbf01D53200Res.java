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
@IntegrationMessage(id = "CbIbf01D53200Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "외화정기예금조회해지 본거래 응답 전문")
public class CbIbf01D53200Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "YOJSNO", name = "접수번호", length = 5, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJSNO;

    @MessageField(id = "YONAME", name = "고객명", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YONAME;

    @MessageField(id = "YOJGJNO", name = "해약계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJGJNO;

    @MessageField(id = "YOSINIL", name = "해약계좌 신규일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSINIL;

    @MessageField(id = "YOYGJRH", name = "예금종류", length = 22, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOYGJRH;

    @MessageField(id = "YOIGJNO", name = "입금계좌번호", length = 11, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOIGJNO;

    @MessageField(id = "YOMANIL", name = "만기일자", length = 8, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOMANIL;

    @MessageField(id = "YOHJGB", name = "해지구분", length = 52, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHJGB;

    @MessageField(id = "YOCRATE", name = "적용환율", length = 7, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCRATE;

    @MessageField(id = "YOHTONG", name = "해약계좌：통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHTONG;

    @MessageField(id = "YOJGMAK", name = "해약원금", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJGMAK;

    @MessageField(id = "YOYGIJA", name = "예금이자", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOYGIJA;

    @MessageField(id = "YOSOSE", name = "소득세(원)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOSOSE;

    @MessageField(id = "YOJUSE", name = "주민세(원)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOJUSE;

    @MessageField(id = "YONTSE", name = "농특세(원)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YONTSE;

    @MessageField(id = "YOTAXTOT", name = "세액합계(원)", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOTAXTOT;

    @MessageField(id = "YOCTONG", name = "차감지급액：통화코드", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOCTONG;

    @MessageField(id = "YOCGJIK", name = "차감지급액", length = 15, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOCGJIK;

    @MessageField(id = "YOTJBR", name = "통장관리점", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOTJBR;

    @MessageField(id = "YOHBRNO", name = "통장관리점(한)", length = 14, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YOHBRNO;

    @MessageField(id = "YOPOINT", name = "캐쉬백 포인트", length = 9, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private String YOPOINT;
}
