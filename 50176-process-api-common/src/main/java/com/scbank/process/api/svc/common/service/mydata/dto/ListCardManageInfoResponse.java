package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardManageInfoResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 관리(상세, 리볼빙, 단기대출, 장기대출) API")
public class ListCardManageInfoResponse implements IMessageObject {

    @MessageField(id = "orgCd", name = "기관코드")
    private String orgCd;

    @MessageField(id = "orgName", name = "기관명")
    private String orgName;

    @MessageField(id = "orgImg", name = "기관이미지")
    private String orgImg;

    @MessageField(id = "blingYn", name = "수신 구분")
    private String blingYn;

    @MessageField(id = "loanShortAmt", name = "단기카드대출 결제예정금액")
    private String loanShortAmt;

    @MessageField(id = "loanLongAmt", name = "장기카드대출 결제예정금액")
    private String loanLongAmt;

    @MessageField(id = "rvlvngAmt", name = "리볼빙 결제예정금액")
    private String rvlvngAmt;

    @MessageField(id = "eCode", name = "에러 코드")
    private String eCode;

    @MessageField(id = "errMsg", name = "에러 메시지")
    private String errMsg;

    @MessageField(id = "bllngAmt", name = "청구금액")
    private String bllngAmt;

    @MessageField(id = "annualFee", name = "연회비")
    private String annualFee;

    @MessageField(id = "rvlvngList", name = "rvlvngList")
    @RepeatedField
    private List<RevolvingInfoDto> rvlvngList;

    @MessageField(id = "resultJsonArray", name = "resultJsonArray")
    private String resultJsonArray;

    @MessageField(id = "shortTrmList", name = "shortTrmList")
    @RepeatedField
    private List<ShortTranInfoDto> shortTrmList;

    @MessageField(id = "longTrmList", name = "longTrmList")
    @RepeatedField
    private List<LongTranInfoDto> longTrmList;

    @MessageField(id = "cardBrndCd", name = "cardBrndCd")
    private String cardBrndCd;

    @MessageField(id = "cardBrndOd", name = "cardBrndOd")
    private Integer cardBrndOd;

    @MessageField(id = "prodType", name = "상품타입")
    private List<Map<String, Object>> prodType;

    @MessageField(id = "resultRecordList", name = "resultRecordList")
    private List<HashMap<String, Object>> resultRecordList;

    @MessageField(id = "tranListJson", name = "tranListJson")
    private ArrayList<HashMap<String, Object>> tranListJson;

    @MessageField(id = "cardId", name = "카드 id")
    private String cardId;

    // public class CardType {
    // private String rdmptnMthdCd;
    // private int rdmptnMthdOd;

    // public CardType(String rdmptnMthdCd, int rdmptnMthdOd) {
    // this.rdmptnMthdCd = rdmptnMthdCd;
    // this.rdmptnMthdOd = rdmptnMthdOd;
    // }

    // public String getRdmptnMthdCd() {
    // return rdmptnMthdCd;
    // }

    // public int getRdmptnMthdOd() {
    // return rdmptnMthdOd;
    // }
    // }

}
