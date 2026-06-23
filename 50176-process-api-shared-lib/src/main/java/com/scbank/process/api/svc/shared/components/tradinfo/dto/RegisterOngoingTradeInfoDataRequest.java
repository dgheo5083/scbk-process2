package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import java.util.HashMap;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterOngoingTradeInfoDataRequest", type = Type.REQUEST)
public class RegisterOngoingTradeInfoDataRequest implements IMessageObject {
	@MessageField(id = "custNo", name = "고객번호[I]", example = "9999")
	private String custNo;
	@MessageField(id = "tradNo", name = "거래번호", example = "")
    private String tradNo;
	@MessageField(id = "bizType", name = "업무구분", example = "HELS")
	private String bizType;
	@MessageField(id = "callCntrStsCd", name = "콜센터상태코드", example = "")
    private String callCntrStsCd;
	@MessageField(id = "prgrssStsCd", name = "진행상태코드", example = "")
    private String prgrssStsCd;
	
	@MessageField(id = "cnnctnTradNo", name = "제휴처거래번호", example = "")
    private String cnnctnTradNo;
	@MessageField(id = "cnnctnWay", name = "제휴처", example = "")
	private String cnnctnWay;
    @MessageField(id = "scrnInfoOnly", name = "화면데이터정보만 저장여부")
    private String scrnInfoOnly;
    @MessageField(id = "skipSendLms", name = "LMS 발설 안하도록 설정", example = "")
    private String skipSendLms;
    @MessageField(id = "newPrdctNum", name = "newPrdctNum", example = "")
    private String newPrdctNum;
    @MessageField(id = "newPrdctCurrency", name = "newPrdctCurrency", example = "")
    private String newPrdctCurrency;
    @MessageField(id = "reacdn", name = "REA코드", example = "")
    private String reacdn;
    @MessageField(id = "preLoanMoveYn", name = "대출이동여부 (상담)", example = "")
    private String preLoanMoveYn;
    @MessageField(id = "realLoanMoveYn", name = "대출이동여부 (실행)", example = "")
    private String realLoanMoveYn;
    @MessageField(id = "hqPrimeRateApplyYn", name = "본부우대금리적용여부", example = "")
    private String hqPrimeRateApplyYn;
    @MessageField(id = "hqPrimeRate", name = "본부우대금리", example = "")
    private String hqPrimeRate;
    @MessageField(id = "loanPurpose", name = "대출목적", example = "")
    private String loanPurpose;
    @MessageField(id = "collateralType", name = "담보유형", example = "")
    private String collateralType;
    @MessageField(id = "oplCnsltYn", name = "'one product 상담 여부", example = "")
    private String oplCnsltYn;
    @MessageField(id = "prdctNm", name = "prdctNm", example = "")
    private String prdctNm;
    
    @MessageField(id = "tradInfo", name = "거래정보 JSON 문자열(대문자 키 사용)", example = "{\"BIZ_TYPE\":\"CASA\"}")
    private String tradInfo;
    @MessageField(id = "scrnDataInfo", name = "화면데이터정보 JSON 문자열(대문자 키 사용)", example="{\"BIZ_TYPE\":\"CASA\"}")
    private String scrnDataInfo;
    @MessageField(id = "rcvryData", name = "CDD전자문서정보 JSON 문자열(대문자 키 사용)", example="{\"BIZ_TYPE\":\"CASA\"}")
    private String rcvryData;
}
