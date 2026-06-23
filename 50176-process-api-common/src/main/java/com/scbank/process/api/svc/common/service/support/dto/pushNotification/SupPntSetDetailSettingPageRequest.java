package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 상세 조회 및 상세설정 화면
 */
@Data
@IntegrationMessage(id = "SupPntSetDetailSettingPageRequest", type = Type.REQUEST)
public class SupPntSetDetailSettingPageRequest implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "drawAccNum", name = "통장번호")
    private String drawAccNum;

    @MessageField(id = "drawAccName", name = "통장명")
    private String drawAccName;

    @MessageField(id = "workType", name = "업무구분값 (1:신규, 2:추가, 3:변경, 4:삭제)")
    private String workType;

    @MessageField(id = "benefitFlag", name = "")
    private String benefitFlag;

    @MessageField(id = "financeFlag", name = "")
    private String financeFlag;

    @MessageField(id = "financeVal", name = "")
    private String financeVal;

    @MessageField(id = "iotranlistFlag", name = "")
    private String iotranlistFlag;

    @MessageField(id = "notyExrateFlg", name = "")
    private String notyExrateFlg;

    @MessageField(id = "wmloungeFlag", name = "")
    private String wmloungeFlag;

}
