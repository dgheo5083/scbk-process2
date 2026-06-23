package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 마케팅 혜택 안내 팝업
 */
@Data
@IntegrationMessage(id = "SupPntSetMarketingNoticePopRequest", type = Type.REQUEST)
public class SupPntSetMarketingNoticePopRequest implements IMessageObject {

    @MessageField(id = "prvsnCd", name = "")
    private String prvsnCd;

    @MessageField(id = "actionType", name = "")
    private String actionType;

    @MessageField(id = "viewDepth", name = "")
    private String viewDepth;

    @MessageField(id = "benefitAgreeFlag", name = "")
    private String benefitAgreeFlag;

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

    @MessageField(id = "iotranListFlag", name = "")
    private String iotranListFlag;

    @MessageField(id = "notyExrateFlag", name = "")
    private String notyExrateFlag;

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

    @MessageField(id = "agreeFlag", name = "")
    private String agreeFlag;

}
