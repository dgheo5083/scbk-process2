
package com.scbank.process.api.svc.common.service.mydata.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetCardBasicInfoRequest", type = Type.REQUEST, description = "마이데이터 > 현대카드 카드기본정보조회 API")
public class GetCardBasicInfoRequest implements IMessageObject {

    @MessageField(id = "orgCd", name = "현대카드 기관코드(D1AAFO0000)")
    private String orgCd;

    @MessageField(id = "hdFlag", name = "현대카드 새로고침 플래그(Y: 새로고침, H: 새로고침 아님)")
    private String hdFlag;

    @MessageField(id = "stlmtExpctdAmt", name = "결제예정금액")
    private String stlmtExpctdAmt;

    @MessageField(id = "stlmtExpctdDt", name = "결제예정일")
    private String stlmtExpctdDt;

    @MessageField(id = "reqNo", name = "req_no")
    private String reqNo;

    @MessageField(id = "ppId", name = "선불카드 식별자")
    private String ppId;

    @MessageField(id = "cardId", name = "카드 id")
    private String cardId;

    @MessageField(id = "isHCD", name = "전체 > 현대카드 진입 플래그")
    private String isHCD;

}
