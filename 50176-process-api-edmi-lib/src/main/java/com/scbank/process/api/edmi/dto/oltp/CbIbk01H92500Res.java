package com.scbank.process.api.edmi.dto.oltp;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.enums.AlignType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IntegrationMessage(id = "CbIbk01H92500Res", type = Type.RESPONSE, captureSystem = "OLTP", description = "기본화면변경 응답부")
public class CbIbk01H92500Res implements IMessageObject {

    @MessageField(id = "UserID", name = "이용자번호", length = 10, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String UserID;

    @MessageField(id = "MenuType", name = "메뉴유형", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String MenuType;

    @MessageField(id = "AcctNumType", name = "계좌유형", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String AcctNumType;

    @MessageField(id = "RemitType", name = "송금확인증유형", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String RemitType;

    @MessageField(id = "CardType", name = "카드종류", length = 1, align = AlignType.LEFT, padding = StringUtils.SPACE)
    private String CardType;

}
