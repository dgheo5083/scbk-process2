package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.FindBranchAtmResult;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindBranchAtmResponse", type = Type.RESPONSE)
public class SupLocFindBranchAtmResponse implements IMessageObject {

    @MessageField(id = "findBranchAtmResultList", name = "영업점 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<FindBranchAtmResult> findBranchAtmResultList;

    @MessageField(id = "moreInfoYn", name = "moreInfoYn")
    private String moreInfoYn;

}
