package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListJoinOrganInfoRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 이용내역조회 API")
public class ListJoinOrganInfoRequest implements IMessageObject {

    @MessageField(id = "entrncFlg", name = "마이데이터 가입여부")
    private String entrncFlg;

    @MessageField(id = "hycardFlg", name = "현대카드 연결여부")
    private String hycardFlg;

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

    @MessageField(id = "cardNo", name = "카드번호")
    private String cardNo;

    @MessageField(id = "cardType", name = "카드타입")
    private String cardType;

    @MessageField(id = "cardArray", name = "카드타입")
    private String cardArray;

    @MessageField(id = "cardPpId", name = "선불카드식별자")
    private String cardPpId;

    @MessageField(id = "hdFlag", name = "현대카드 플래그")
    private String hdFlag;

    @MessageField(id = "cardId", name = "카드 id")
    private String cardId;

    @MessageField(id = "searchMonth", name = "조회년월")
    private String searchMonth;

}
