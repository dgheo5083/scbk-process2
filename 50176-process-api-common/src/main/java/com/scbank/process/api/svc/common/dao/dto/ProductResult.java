package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class ProductResult implements IMessageObject {

	@MessageField(id = "prdctMkCd", name = "prdctMkCd")
    private String prdctMkCd;

	@MessageField(id = "prdctCd", name = "prdctCd")
    private String prdctCd;

	@MessageField(id = "bztyCd", name = "bztyCd")
    private String bztyCd;

	@MessageField(id = "ctgrySysCd", name = "ctgrySysCd")
    private String ctgrySysCd;

	@MessageField(id = "ctgrySysNm", name = "ctgrySysNm")
    private String ctgrySysNm;

	@MessageField(id = "fundClsCd", name = "fundClsCd")
    private String fundClsCd;

	@MessageField(id = "prdctSubCd", name = "prdctSubCd")
    private String prdctSubCd;

	@MessageField(id = "prdctNm", name = "prdctNm")
    private String prdctNm;

	@MessageField(id = "fundCls1Nm", name = "fundCls1Nm")
    private String fundCls1Nm;

	@MessageField(id = "fundCls2Nm", name = "fundCls2Nm")
    private String fundCls2Nm;

	@MessageField(id = "fundCls3Nm", name = "fundCls3Nm")
    private String fundCls3Nm;

	@MessageField(id = "prdctFturKr", name = "prdctFturKr")
    private String prdctFturKr;

	@MessageField(id = "prdctMkNm", name = "prdctMkNm")
    private String prdctMkNm;

}
