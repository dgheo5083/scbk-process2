package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 푸시 알림 서비스 설정화면 화면을 호출 및 푸시 가입여부를 조회
 */
@Data
@IntegrationMessage(id = "SupPntConfirmPushJoinYnRequest", type = Type.REQUEST)
public class SupPntConfirmPushJoinYnRequest implements IMessageObject {

    @MessageField(id = "callPageType", name = "")
    private String callPageType;

    @MessageField(id = "eventNo", name = "")
    private String eventNo;

    @MessageField(id = "cnfrmNoNew", name = "")
    private String cnfrmNoNew;

    @MessageField(id = "operType", name = "")
    private String operType;

    @MessageField(id = "pushSrvcApprvlFlg", name = "")
    private String pushSrvcApprvlFlg;

    @MessageField(id = "appInfo", name = "")
    private String appInfo;

    @MessageField(id = "deviceMd", name = "")
    private String deviceMd;

    @MessageField(id = "deviceVis", name = "")
    private String deviceVis;

    @MessageField(id = "agrmntMk", name = "")
    private String agrmntMk;

}
