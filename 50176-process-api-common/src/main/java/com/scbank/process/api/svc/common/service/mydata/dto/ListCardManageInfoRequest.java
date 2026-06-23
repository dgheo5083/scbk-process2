package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "ListCardManageInfoRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 관리(상세, 리볼빙, 단기대출, 장기대출) API")
public class ListCardManageInfoRequest implements IMessageObject {

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

    @MessageField(id = "blingYn", name = "수신구분")
    private String blingYn;

    @MessageField(id = "reqNo", name = "")
    private String reqNo;

    @MessageField(id = "searchMonth", name = "조회년월")
    private String searchMonth;

    @MessageField(id = "tabGubun", name = "탭 구분")
    private String tabGubun;

    @MessageField(id = "loanNo", name = "대출번호")
    private String loanNo;

}
