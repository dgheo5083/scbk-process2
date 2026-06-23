
package com.scbank.process.api.svc.common.service.mydata.dto;

import java.util.List;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Data;

@Data
@IntegrationMessage(id = "GetCardListDataResponse", type = Type.RESPONSE, description = "마이데이터 > 현대카드 상세 정보 조회 API")
public class GetCardListDataResponse implements IMessageObject {

    @MessageField(id = "orgCardList", name = "현대카드목록")
    @RepeatedField
    private List<OrgCardInfoDto> orgCardList;

    @MessageField(id = "rspCode", name = "응답코드")
    private String rspCode;

    @MessageField(id = "rspMsg", name = "응답메시지")
    private String rspMsg;

    @MessageField(id = "inTimeStamp", name = "inTimeStamp")
    private String inTimeStamp;

    @MessageField(id = "linkedOrgCnt", name = "linkedOrgCnt")
    private String linkedOrgCnt;
}
