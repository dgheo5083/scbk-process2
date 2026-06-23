package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@IntegrationMessage(id = "SelectMydataOrgMgtParameter", type = Type.REQUEST, description = "MYDATA 업권 기관코드 리스트")

public class SelectMydataOrgMgtParameter implements IMessageObject {

}
