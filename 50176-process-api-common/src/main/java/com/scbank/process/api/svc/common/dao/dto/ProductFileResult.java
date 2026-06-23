package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class ProductFileResult implements IMessageObject {

    @MessageField(id = "prdctCd", name = "상품코드")
    private String prdctCd;

    @MessageField(id = "fundClsCd", name = "클래스펀드코드")
    private String fundClsCd;

    @MessageField(id = "assctnFundNm", name = "협회(클래스)펀드명")
    private String assctnFundNm;

    @MessageField(id = "tranWay", name = "거래방식")
    private String tranWay;

    @MessageField(id = "cntrctPrdStrt", name = "계약시작일")
    private String cntrctPrdStrt;

    @MessageField(id = "cntrctPrdEnd", name = "계약종료일")
    private String cntrctPrdEnd;

    @MessageField(id = "prvsnMk1Filenm", name = "집합투자규약")
    private String prvsnMk1Filenm;

    @MessageField(id = "prvsnMk2Filenm", name = "투자설명서")
    private String prvsnMk2Filenm;

    @MessageField(id = "prvsnMk3Filenm", name = "간이투자설명서")
    private String prvsnMk3Filenm;

    @MessageField(id = "prvsnMk4Filenm", name = "외국집합투자증권")
    private String prvsnMk4Filenm;

    @MessageField(id = "prvsnMk5Filenm", name = "사모집합투자증권 핵심요약상품설명서")
    private String prvsnMk5Filenm;

    @MessageField(id = "prvsnMk6Filenm", name = "사모집합투자증권 핵심상품설명서")
    private String prvsnMk6Filenm;

    @MessageField(id = "fundCls1Nm", name = "fundCls1Nm")
    private String fundCls1Nm;

    @MessageField(id = "fundCls2Nm", name = "fundCls2Nm")
    private String fundCls2Nm;

    @MessageField(id = "fundCls3Nm", name = "fundCls3Nm")
    private String fundCls3Nm;

}
