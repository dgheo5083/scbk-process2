package com.scbank.process.api.svc.common.service.settings.dto.emergency;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetEmgListNoticeForPopupRequest", description = "팝업용 긴급공지 요청 DTO", type = Type.REQUEST)
public class SetEmgListNoticeForPopupRequest implements IMessageObject {

}
