package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.ArrayList;
import java.util.HashMap;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardLoanResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 장기카드대출 거래내역조회 API")
public class ListCardLoanResponse implements IMessageObject {

    @MessageField(id = "nextKey", name = "다음 페이지 KEY")
    private String nextKey;

    @MessageField(id = "limitCnt", name = "페이지건수")
    private String limitCnt;

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgName", name = "기관명")
    private String orgName;

    @MessageField(id = "orgImg", name = "기관이미지")
    private String orgImg;

    @MessageField(id = "blingYn", name = "수신 구분")
    private String blingYn;

    @MessageField(id = "reqNo", name = "")
    private String reqNo;

    @MessageField(id = "cardProdNm", name = "카드상품명")
    private String cardProdNm;

    @MessageField(id = "cardNo", name = "카드번호")
    private String cardNo;

    @MessageField(id = "cardType", name = "카드타입")
    private String cardType;

    @MessageField(id = "loanLongAmt", name = "장기카드대출 결제예정금액")
    private String loanLongAmt;

    @MessageField(id = "eCode", name = "에러 코드")
    private String eCode;

    @MessageField(id = "errMsg", name = "에러 메시지")
    private String errMsg;

    @MessageField(id = "tranListJson", name = "tranListJson")
    private ArrayList<HashMap<String, Object>> tranListJson;

    @MessageField(id = "longTrmList", name = "longTrmList")
    private ArrayList<HashMap<String, Object>> longTrmList;

}
