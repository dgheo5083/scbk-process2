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
@IntegrationMessage(id = "CbIbk03H03900Req", type = Type.REQUEST, captureSystem = "OLTP", description = "전계좌조회 원장 Refresh 확인 거래 요청부")
public class CbIbk03H03900Req implements IMessageObject {
    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "TSPassword", name = "통신비밀번호", length = 8, align = AlignType.LEFT, padding = StringUtils.SPACE, encoding = "cp500")
    private String TSPassword;

    @MessageField(id = "YIMSSU", name = "명세수", length = 3, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIMSSU;

    @MessageField(id = "YIGJNO0", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO0;

    @MessageField(id = "YITONM0", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM0;

    @MessageField(id = "YIKIND0", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND0;

    @MessageField(id = "BalanceSign0", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign0;

    @MessageField(id = "YIBALANCE0", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE0;

    @MessageField(id = "YIGJNO1", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO1;

    @MessageField(id = "YITONM1", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM1;

    @MessageField(id = "YIKIND1", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND1;

    @MessageField(id = "BalanceSign1", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign1;

    @MessageField(id = "YIBALANCE1", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE1;

    @MessageField(id = "YIGJNO2", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO2;

    @MessageField(id = "YITONM2", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM2;

    @MessageField(id = "YIKIND2", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND2;

    @MessageField(id = "BalanceSign2", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign2;

    @MessageField(id = "YIBALANCE2", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE2;

    @MessageField(id = "YIGJNO3", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO3;

    @MessageField(id = "YITONM3", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM3;

    @MessageField(id = "YIKIND3", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND3;

    @MessageField(id = "BalanceSign3", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign3;

    @MessageField(id = "YIBALANCE3", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE3;

    @MessageField(id = "YIGJNO4", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO4;

    @MessageField(id = "YITONM4", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM4;

    @MessageField(id = "YIKIND4", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND4;

    @MessageField(id = "BalanceSign4", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign4;

    @MessageField(id = "YIBALANCE4", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE4;

    @MessageField(id = "YIGJNO5", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO5;

    @MessageField(id = "YITONM5", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM5;

    @MessageField(id = "YIKIND5", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND5;

    @MessageField(id = "BalanceSign5", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign5;

    @MessageField(id = "YIBALANCE5", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE5;

    @MessageField(id = "YIGJNO6", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO6;

    @MessageField(id = "YITONM6", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM6;

    @MessageField(id = "YIKIND6", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND6;

    @MessageField(id = "BalanceSign6", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign6;

    @MessageField(id = "YIBALANCE6", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE6;

    @MessageField(id = "YIGJNO7", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO7;

    @MessageField(id = "YITONM7", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM7;

    @MessageField(id = "YIKIND7", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND7;

    @MessageField(id = "BalanceSign7", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign7;

    @MessageField(id = "YIBALANCE7", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE7;

    @MessageField(id = "YIGJNO8", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO8;

    @MessageField(id = "YITONM8", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM8;

    @MessageField(id = "YIKIND8", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND8;

    @MessageField(id = "BalanceSign8", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign8;

    @MessageField(id = "YIBALANCE8", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE8;

    @MessageField(id = "YIGJNO9", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO9;

    @MessageField(id = "YITONM9", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM9;

    @MessageField(id = "YIKIND9", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND9;

    @MessageField(id = "BalanceSign9", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign9;

    @MessageField(id = "YIBALANCE9", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE9;

    @MessageField(id = "YIGJNO10", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO10;

    @MessageField(id = "YITONM10", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM10;

    @MessageField(id = "YIKIND10", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND10;

    @MessageField(id = "BalanceSign10", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign10;

    @MessageField(id = "YIBALANCE10", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE10;

    @MessageField(id = "YIGJNO11", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO11;

    @MessageField(id = "YITONM11", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM11;

    @MessageField(id = "YIKIND11", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND11;

    @MessageField(id = "BalanceSign11", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign11;

    @MessageField(id = "YIBALANCE11", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE11;

    @MessageField(id = "YIGJNO12", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO12;

    @MessageField(id = "YITONM12", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM12;

    @MessageField(id = "YIKIND12", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND12;

    @MessageField(id = "BalanceSign12", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign12;

    @MessageField(id = "YIBALANCE12", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE12;

    @MessageField(id = "YIGJNO13", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO13;

    @MessageField(id = "YITONM13", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM13;

    @MessageField(id = "YIKIND13", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND13;

    @MessageField(id = "BalanceSign13", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign13;

    @MessageField(id = "YIBALANCE13", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE13;

    @MessageField(id = "YIGJNO14", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO14;

    @MessageField(id = "YITONM14", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM14;

    @MessageField(id = "YIKIND14", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND14;

    @MessageField(id = "BalanceSign14", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign14;

    @MessageField(id = "YIBALANCE14", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE14;

    @MessageField(id = "YIGJNO15", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO15;

    @MessageField(id = "YITONM15", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM15;

    @MessageField(id = "YIKIND15", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND15;

    @MessageField(id = "BalanceSign15", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign15;

    @MessageField(id = "YIBALANCE15", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE15;

    @MessageField(id = "YIGJNO16", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO16;

    @MessageField(id = "YITONM16", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM16;

    @MessageField(id = "YIKIND16", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND16;

    @MessageField(id = "BalanceSign16", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign16;

    @MessageField(id = "YIBALANCE16", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE16;

    @MessageField(id = "YIGJNO17", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO17;

    @MessageField(id = "YITONM17", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM17;

    @MessageField(id = "YIKIND17", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND17;

    @MessageField(id = "BalanceSign17", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign17;

    @MessageField(id = "YIBALANCE17", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE17;

    @MessageField(id = "YIGJNO18", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO18;

    @MessageField(id = "YITONM18", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM18;

    @MessageField(id = "YIKIND18", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND18;

    @MessageField(id = "BalanceSign18", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign18;

    @MessageField(id = "YIBALANCE18", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE18;

    @MessageField(id = "YIGJNO19", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO19;

    @MessageField(id = "YITONM19", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM19;

    @MessageField(id = "YIKIND19", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND19;

    @MessageField(id = "BalanceSign19", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign19;

    @MessageField(id = "YIBALANCE19", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE19;

    @MessageField(id = "YIGJNO20", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO20;

    @MessageField(id = "YITONM20", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM20;

    @MessageField(id = "YIKIND20", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND20;

    @MessageField(id = "BalanceSign20", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign20;

    @MessageField(id = "YIBALANCE20", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE20;

    @MessageField(id = "YIGJNO21", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO21;

    @MessageField(id = "YITONM21", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM21;

    @MessageField(id = "YIKIND21", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND21;

    @MessageField(id = "BalanceSign21", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign21;

    @MessageField(id = "YIBALANCE21", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE21;

    @MessageField(id = "YIGJNO22", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO22;

    @MessageField(id = "YITONM22", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM22;

    @MessageField(id = "YIKIND22", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND22;

    @MessageField(id = "BalanceSign22", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign22;

    @MessageField(id = "YIBALANCE22", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE22;

    @MessageField(id = "YIGJNO23", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO23;

    @MessageField(id = "YITONM23", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM23;

    @MessageField(id = "YIKIND23", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND23;

    @MessageField(id = "BalanceSign23", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign23;

    @MessageField(id = "YIBALANCE23", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE23;

    @MessageField(id = "YIGJNO24", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO24;

    @MessageField(id = "YITONM24", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM24;

    @MessageField(id = "YIKIND24", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND24;

    @MessageField(id = "BalanceSign24", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign24;

    @MessageField(id = "YIBALANCE24", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE24;

    @MessageField(id = "YIGJNO25", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO25;

    @MessageField(id = "YITONM25", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM25;

    @MessageField(id = "YIKIND25", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND25;

    @MessageField(id = "BalanceSign25", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign25;

    @MessageField(id = "YIBALANCE25", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE25;

    @MessageField(id = "YIGJNO26", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO26;

    @MessageField(id = "YITONM26", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM26;

    @MessageField(id = "YIKIND26", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND26;

    @MessageField(id = "BalanceSign26", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign26;

    @MessageField(id = "YIBALANCE26", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE26;

    @MessageField(id = "YIGJNO27", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO27;

    @MessageField(id = "YITONM27", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM27;

    @MessageField(id = "YIKIND27", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND27;

    @MessageField(id = "BalanceSign27", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign27;

    @MessageField(id = "YIBALANCE27", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE27;

    @MessageField(id = "YIGJNO28", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO28;

    @MessageField(id = "YITONM28", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM28;

    @MessageField(id = "YIKIND28", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND28;

    @MessageField(id = "BalanceSign28", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign28;

    @MessageField(id = "YIBALANCE28", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE28;

    @MessageField(id = "YIGJNO29", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO29;

    @MessageField(id = "YITONM29", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM29;

    @MessageField(id = "YIKIND29", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND29;

    @MessageField(id = "BalanceSign29", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign29;

    @MessageField(id = "YIBALANCE29", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE29;

    @MessageField(id = "YIGJNO30", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO30;

    @MessageField(id = "YITONM30", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM30;

    @MessageField(id = "YIKIND30", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND30;

    @MessageField(id = "BalanceSign30", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign30;

    @MessageField(id = "YIBALANCE30", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE30;

    @MessageField(id = "YIGJNO31", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO31;

    @MessageField(id = "YITONM31", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM31;

    @MessageField(id = "YIKIND31", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND31;

    @MessageField(id = "BalanceSign31", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign31;

    @MessageField(id = "YIBALANCE31", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE31;

    @MessageField(id = "YIGJNO32", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO32;

    @MessageField(id = "YITONM32", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM32;

    @MessageField(id = "YIKIND32", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND32;

    @MessageField(id = "BalanceSign32", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign32;

    @MessageField(id = "YIBALANCE32", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE32;

    @MessageField(id = "YIGJNO33", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO33;

    @MessageField(id = "YITONM33", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM33;

    @MessageField(id = "YIKIND33", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND33;

    @MessageField(id = "BalanceSign33", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign33;

    @MessageField(id = "YIBALANCE33", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE33;

    @MessageField(id = "YIGJNO34", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO34;

    @MessageField(id = "YITONM34", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM34;

    @MessageField(id = "YIKIND34", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND34;

    @MessageField(id = "BalanceSign34", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign34;

    @MessageField(id = "YIBALANCE34", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE34;

    @MessageField(id = "YIGJNO35", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO35;

    @MessageField(id = "YITONM35", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM35;

    @MessageField(id = "YIKIND35", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND35;

    @MessageField(id = "BalanceSign35", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign35;

    @MessageField(id = "YIBALANCE35", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE35;

    @MessageField(id = "YIGJNO36", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO36;

    @MessageField(id = "YITONM36", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM36;

    @MessageField(id = "YIKIND36", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND36;

    @MessageField(id = "BalanceSign36", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign36;

    @MessageField(id = "YIBALANCE36", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE36;

    @MessageField(id = "YIGJNO37", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO37;

    @MessageField(id = "YITONM37", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM37;

    @MessageField(id = "YIKIND37", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND37;

    @MessageField(id = "BalanceSign37", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign37;

    @MessageField(id = "YIBALANCE37", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE37;

    @MessageField(id = "YIGJNO38", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO38;

    @MessageField(id = "YITONM38", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM38;

    @MessageField(id = "YIKIND38", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND38;

    @MessageField(id = "BalanceSign38", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign38;

    @MessageField(id = "YIBALANCE38", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE38;

    @MessageField(id = "YIGJNO39", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO39;

    @MessageField(id = "YITONM39", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM39;

    @MessageField(id = "YIKIND39", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND39;

    @MessageField(id = "BalanceSign39", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign39;

    @MessageField(id = "YIBALANCE39", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE39;

    @MessageField(id = "YIGJNO40", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO40;

    @MessageField(id = "YITONM40", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM40;

    @MessageField(id = "YIKIND40", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND40;

    @MessageField(id = "BalanceSign40", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign40;

    @MessageField(id = "YIBALANCE40", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE40;

    @MessageField(id = "YIGJNO41", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO41;

    @MessageField(id = "YITONM41", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM41;

    @MessageField(id = "YIKIND41", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND41;

    @MessageField(id = "BalanceSign41", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign41;

    @MessageField(id = "YIBALANCE41", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE41;

    @MessageField(id = "YIGJNO42", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO42;

    @MessageField(id = "YITONM42", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM42;

    @MessageField(id = "YIKIND42", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND42;

    @MessageField(id = "BalanceSign42", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign42;

    @MessageField(id = "YIBALANCE42", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE42;

    @MessageField(id = "YIGJNO43", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO43;

    @MessageField(id = "YITONM43", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM43;

    @MessageField(id = "YIKIND43", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND43;

    @MessageField(id = "BalanceSign43", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign43;

    @MessageField(id = "YIBALANCE43", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE43;

    @MessageField(id = "YIGJNO44", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO44;

    @MessageField(id = "YITONM44", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM44;

    @MessageField(id = "YIKIND44", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND44;

    @MessageField(id = "BalanceSign44", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign44;

    @MessageField(id = "YIBALANCE44", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE44;

    @MessageField(id = "YIGJNO45", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO45;

    @MessageField(id = "YITONM45", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM45;

    @MessageField(id = "YIKIND45", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND45;

    @MessageField(id = "BalanceSign45", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign45;

    @MessageField(id = "YIBALANCE45", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE45;

    @MessageField(id = "YIGJNO46", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO46;

    @MessageField(id = "YITONM46", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM46;

    @MessageField(id = "YIKIND46", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND46;

    @MessageField(id = "BalanceSign46", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign46;

    @MessageField(id = "YIBALANCE46", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE46;

    @MessageField(id = "YIGJNO47", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO47;

    @MessageField(id = "YITONM47", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM47;

    @MessageField(id = "YIKIND47", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND47;

    @MessageField(id = "BalanceSign47", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign47;

    @MessageField(id = "YIBALANCE47", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE47;

    @MessageField(id = "YIGJNO48", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO48;

    @MessageField(id = "YITONM48", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM48;

    @MessageField(id = "YIKIND48", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND48;

    @MessageField(id = "BalanceSign48", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign48;

    @MessageField(id = "YIBALANCE48", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE48;

    @MessageField(id = "YIGJNO49", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO49;

    @MessageField(id = "YITONM49", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM49;

    @MessageField(id = "YIKIND49", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND49;

    @MessageField(id = "BalanceSign49", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign49;

    @MessageField(id = "YIBALANCE49", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE49;

    @MessageField(id = "YIGJNO50", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO50;

    @MessageField(id = "YITONM50", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM50;

    @MessageField(id = "YIKIND50", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND50;

    @MessageField(id = "BalanceSign50", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign50;

    @MessageField(id = "YIBALANCE50", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE50;

    @MessageField(id = "YIGJNO51", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO51;

    @MessageField(id = "YITONM51", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM51;

    @MessageField(id = "YIKIND51", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND51;

    @MessageField(id = "BalanceSign51", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign51;

    @MessageField(id = "YIBALANCE51", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE51;

    @MessageField(id = "YIGJNO52", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO52;

    @MessageField(id = "YITONM52", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM52;

    @MessageField(id = "YIKIND52", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND52;

    @MessageField(id = "BalanceSign52", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign52;

    @MessageField(id = "YIBALANCE52", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE52;

    @MessageField(id = "YIGJNO53", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO53;

    @MessageField(id = "YITONM53", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM53;

    @MessageField(id = "YIKIND53", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND53;

    @MessageField(id = "BalanceSign53", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign53;

    @MessageField(id = "YIBALANCE53", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE53;

    @MessageField(id = "YIGJNO54", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO54;

    @MessageField(id = "YITONM54", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM54;

    @MessageField(id = "YIKIND54", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND54;

    @MessageField(id = "BalanceSign54", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign54;

    @MessageField(id = "YIBALANCE54", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE54;

    @MessageField(id = "YIGJNO55", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO55;

    @MessageField(id = "YITONM55", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM55;

    @MessageField(id = "YIKIND55", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND55;

    @MessageField(id = "BalanceSign55", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign55;

    @MessageField(id = "YIBALANCE55", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE55;

    @MessageField(id = "YIGJNO56", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO56;

    @MessageField(id = "YITONM56", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM56;

    @MessageField(id = "YIKIND56", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND56;

    @MessageField(id = "BalanceSign56", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign56;

    @MessageField(id = "YIBALANCE56", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE56;

    @MessageField(id = "YIGJNO57", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO57;

    @MessageField(id = "YITONM57", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM57;

    @MessageField(id = "YIKIND57", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND57;

    @MessageField(id = "BalanceSign57", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign57;

    @MessageField(id = "YIBALANCE57", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE57;

    @MessageField(id = "YIGJNO58", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO58;

    @MessageField(id = "YITONM58", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM58;

    @MessageField(id = "YIKIND58", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND58;

    @MessageField(id = "BalanceSign58", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign58;

    @MessageField(id = "YIBALANCE58", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE58;

    @MessageField(id = "YIGJNO59", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO59;

    @MessageField(id = "YITONM59", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM59;

    @MessageField(id = "YIKIND59", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND59;

    @MessageField(id = "BalanceSign59", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign59;

    @MessageField(id = "YIBALANCE59", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE59;

    @MessageField(id = "YIGJNO60", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO60;

    @MessageField(id = "YITONM60", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM60;

    @MessageField(id = "YIKIND60", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND60;

    @MessageField(id = "BalanceSign60", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign60;

    @MessageField(id = "YIBALANCE60", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE60;

    @MessageField(id = "YIGJNO61", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO61;

    @MessageField(id = "YITONM61", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM61;

    @MessageField(id = "YIKIND61", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND61;

    @MessageField(id = "BalanceSign61", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign61;

    @MessageField(id = "YIBALANCE61", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE61;

    @MessageField(id = "YIGJNO62", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO62;

    @MessageField(id = "YITONM62", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM62;

    @MessageField(id = "YIKIND62", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND62;

    @MessageField(id = "BalanceSign62", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign62;

    @MessageField(id = "YIBALANCE62", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE62;

    @MessageField(id = "YIGJNO63", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO63;

    @MessageField(id = "YITONM63", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM63;

    @MessageField(id = "YIKIND63", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND63;

    @MessageField(id = "BalanceSign63", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign63;

    @MessageField(id = "YIBALANCE63", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE63;

    @MessageField(id = "YIGJNO64", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO64;

    @MessageField(id = "YITONM64", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM64;

    @MessageField(id = "YIKIND64", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND64;

    @MessageField(id = "BalanceSign64", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign64;

    @MessageField(id = "YIBALANCE64", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE64;

    @MessageField(id = "YIGJNO65", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO65;

    @MessageField(id = "YITONM65", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM65;

    @MessageField(id = "YIKIND65", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND65;

    @MessageField(id = "BalanceSign65", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign65;

    @MessageField(id = "YIBALANCE65", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE65;

    @MessageField(id = "YIGJNO66", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO66;

    @MessageField(id = "YITONM66", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM66;

    @MessageField(id = "YIKIND66", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND66;

    @MessageField(id = "BalanceSign66", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign66;

    @MessageField(id = "YIBALANCE66", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE66;

    @MessageField(id = "YIGJNO67", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO67;

    @MessageField(id = "YITONM67", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM67;

    @MessageField(id = "YIKIND67", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND67;

    @MessageField(id = "BalanceSign67", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign67;

    @MessageField(id = "YIBALANCE67", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE67;

    @MessageField(id = "YIGJNO68", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO68;

    @MessageField(id = "YITONM68", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM68;

    @MessageField(id = "YIKIND68", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND68;

    @MessageField(id = "BalanceSign68", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign68;

    @MessageField(id = "YIBALANCE68", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE68;

    @MessageField(id = "YIGJNO69", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO69;

    @MessageField(id = "YITONM69", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM69;

    @MessageField(id = "YIKIND69", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND69;

    @MessageField(id = "BalanceSign69", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign69;

    @MessageField(id = "YIBALANCE69", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE69;

    @MessageField(id = "YIGJNO70", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO70;

    @MessageField(id = "YITONM70", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM70;

    @MessageField(id = "YIKIND70", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND70;

    @MessageField(id = "BalanceSign70", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign70;

    @MessageField(id = "YIBALANCE70", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE70;

    @MessageField(id = "YIGJNO71", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO71;

    @MessageField(id = "YITONM71", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM71;

    @MessageField(id = "YIKIND71", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND71;

    @MessageField(id = "BalanceSign71", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign71;

    @MessageField(id = "YIBALANCE71", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE71;

    @MessageField(id = "YIGJNO72", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO72;

    @MessageField(id = "YITONM72", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM72;

    @MessageField(id = "YIKIND72", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND72;

    @MessageField(id = "BalanceSign72", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign72;

    @MessageField(id = "YIBALANCE72", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE72;

    @MessageField(id = "YIGJNO73", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO73;

    @MessageField(id = "YITONM73", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM73;

    @MessageField(id = "YIKIND73", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND73;

    @MessageField(id = "BalanceSign73", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign73;

    @MessageField(id = "YIBALANCE73", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE73;

    @MessageField(id = "YIGJNO74", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO74;

    @MessageField(id = "YITONM74", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM74;

    @MessageField(id = "YIKIND74", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND74;

    @MessageField(id = "BalanceSign74", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign74;

    @MessageField(id = "YIBALANCE74", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE74;

    @MessageField(id = "YIGJNO75", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO75;

    @MessageField(id = "YITONM75", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM75;

    @MessageField(id = "YIKIND75", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND75;

    @MessageField(id = "BalanceSign75", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign75;

    @MessageField(id = "YIBALANCE75", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE75;

    @MessageField(id = "YIGJNO76", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO76;

    @MessageField(id = "YITONM76", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM76;

    @MessageField(id = "YIKIND76", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND76;

    @MessageField(id = "BalanceSign76", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign76;

    @MessageField(id = "YIBALANCE76", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE76;

    @MessageField(id = "YIGJNO77", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO77;

    @MessageField(id = "YITONM77", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM77;

    @MessageField(id = "YIKIND77", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND77;

    @MessageField(id = "BalanceSign77", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign77;

    @MessageField(id = "YIBALANCE77", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE77;

    @MessageField(id = "YIGJNO78", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO78;

    @MessageField(id = "YITONM78", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM78;

    @MessageField(id = "YIKIND78", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND78;

    @MessageField(id = "BalanceSign78", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign78;

    @MessageField(id = "YIBALANCE78", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE78;

    @MessageField(id = "YIGJNO79", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO79;

    @MessageField(id = "YITONM79", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM79;

    @MessageField(id = "YIKIND79", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND79;

    @MessageField(id = "BalanceSign79", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign79;

    @MessageField(id = "YIBALANCE79", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE79;

    @MessageField(id = "YIGJNO80", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO80;

    @MessageField(id = "YITONM80", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM80;

    @MessageField(id = "YIKIND80", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND80;

    @MessageField(id = "BalanceSign80", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign80;

    @MessageField(id = "YIBALANCE80", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE80;

    @MessageField(id = "YIGJNO81", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO81;

    @MessageField(id = "YITONM81", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM81;

    @MessageField(id = "YIKIND81", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND81;

    @MessageField(id = "BalanceSign81", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign81;

    @MessageField(id = "YIBALANCE81", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE81;

    @MessageField(id = "YIGJNO82", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO82;

    @MessageField(id = "YITONM82", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM82;

    @MessageField(id = "YIKIND82", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND82;

    @MessageField(id = "BalanceSign82", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign82;

    @MessageField(id = "YIBALANCE82", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE82;

    @MessageField(id = "YIGJNO83", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO83;

    @MessageField(id = "YITONM83", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM83;

    @MessageField(id = "YIKIND83", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND83;

    @MessageField(id = "BalanceSign83", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign83;

    @MessageField(id = "YIBALANCE83", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE83;

    @MessageField(id = "YIGJNO84", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO84;

    @MessageField(id = "YITONM84", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM84;

    @MessageField(id = "YIKIND84", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND84;

    @MessageField(id = "BalanceSign84", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign84;

    @MessageField(id = "YIBALANCE84", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE84;

    @MessageField(id = "YIGJNO85", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO85;

    @MessageField(id = "YITONM85", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM85;

    @MessageField(id = "YIKIND85", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND85;

    @MessageField(id = "BalanceSign85", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign85;

    @MessageField(id = "YIBALANCE85", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE85;

    @MessageField(id = "YIGJNO86", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO86;

    @MessageField(id = "YITONM86", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM86;

    @MessageField(id = "YIKIND86", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND86;

    @MessageField(id = "BalanceSign86", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign86;

    @MessageField(id = "YIBALANCE86", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE86;

    @MessageField(id = "YIGJNO87", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO87;

    @MessageField(id = "YITONM87", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM87;

    @MessageField(id = "YIKIND87", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND87;

    @MessageField(id = "BalanceSign87", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign87;

    @MessageField(id = "YIBALANCE87", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE87;

    @MessageField(id = "YIGJNO88", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO88;

    @MessageField(id = "YITONM88", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM88;

    @MessageField(id = "YIKIND88", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND88;

    @MessageField(id = "BalanceSign88", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign88;

    @MessageField(id = "YIBALANCE88", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE88;

    @MessageField(id = "YIGJNO89", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO89;

    @MessageField(id = "YITONM89", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM89;

    @MessageField(id = "YIKIND89", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND89;

    @MessageField(id = "BalanceSign89", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign89;

    @MessageField(id = "YIBALANCE89", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE89;

    @MessageField(id = "YIGJNO90", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO90;

    @MessageField(id = "YITONM90", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM90;

    @MessageField(id = "YIKIND90", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND90;

    @MessageField(id = "BalanceSign90", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign90;

    @MessageField(id = "YIBALANCE90", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE90;

    @MessageField(id = "YIGJNO91", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO91;

    @MessageField(id = "YITONM91", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM91;

    @MessageField(id = "YIKIND91", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND91;

    @MessageField(id = "BalanceSign91", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign91;

    @MessageField(id = "YIBALANCE91", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE91;

    @MessageField(id = "YIGJNO92", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO92;

    @MessageField(id = "YITONM92", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM92;

    @MessageField(id = "YIKIND92", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND92;

    @MessageField(id = "BalanceSign92", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign92;

    @MessageField(id = "YIBALANCE92", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE92;

    @MessageField(id = "YIGJNO93", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO93;

    @MessageField(id = "YITONM93", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM93;

    @MessageField(id = "YIKIND93", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND93;

    @MessageField(id = "BalanceSign93", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign93;

    @MessageField(id = "YIBALANCE93", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE93;

    @MessageField(id = "YIGJNO94", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO94;

    @MessageField(id = "YITONM94", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM94;

    @MessageField(id = "YIKIND94", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND94;

    @MessageField(id = "BalanceSign94", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign94;

    @MessageField(id = "YIBALANCE94", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE94;

    @MessageField(id = "YIGJNO95", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO95;

    @MessageField(id = "YITONM95", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM95;

    @MessageField(id = "YIKIND95", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND95;

    @MessageField(id = "BalanceSign95", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign95;

    @MessageField(id = "YIBALANCE95", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE95;

    @MessageField(id = "YIGJNO96", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO96;

    @MessageField(id = "YITONM96", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM96;

    @MessageField(id = "YIKIND96", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND96;

    @MessageField(id = "BalanceSign96", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign96;

    @MessageField(id = "YIBALANCE96", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE96;

    @MessageField(id = "YIGJNO97", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO97;

    @MessageField(id = "YITONM97", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM97;

    @MessageField(id = "YIKIND97", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND97;

    @MessageField(id = "BalanceSign97", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign97;

    @MessageField(id = "YIBALANCE97", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE97;

    @MessageField(id = "YIGJNO98", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO98;

    @MessageField(id = "YITONM98", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM98;

    @MessageField(id = "YIKIND98", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND98;

    @MessageField(id = "BalanceSign98", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign98;

    @MessageField(id = "YIBALANCE98", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE98;

    @MessageField(id = "YIGJNO99", name = "계좌번호", length = 11, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIGJNO99;

    @MessageField(id = "YITONM99", name = "통화약명", length = 3, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YITONM99;

    @MessageField(id = "YIKIND99", name = "계좌종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String YIKIND99;

    @MessageField(id = "BalanceSign99", name = "잔액Sign", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String BalanceSign99;

    @MessageField(id = "YIBALANCE99", name = "원장잔액", length = 13, align = AlignType.RIGHT, padding = StringUtils.ZERO)
    private Integer YIBALANCE99;

}