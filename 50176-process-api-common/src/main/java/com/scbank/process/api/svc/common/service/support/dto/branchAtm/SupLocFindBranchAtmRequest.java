package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindBranchAtmRequest", type = Type.REQUEST)
public class SupLocFindBranchAtmRequest implements IMessageObject {

    @MessageField(id = "keyword", name = "키워드")
    private String keyword;

    @MessageField(id = "type", name = "타입")
    private String type;

    @MessageField(id = "currPage", name = "현재페이지")
    private String currPage;

    @MessageField(id = "lng", name = "경도")
    private String lng;

    @MessageField(id = "lat", name = "위도")
    private String lat;

    @MessageField(id = "listCountPerPage", name = "listCountPerPage")
    private String listCountPerPage;

    @MessageField(id = "distance", name = "거리")
    private String distance;
}
