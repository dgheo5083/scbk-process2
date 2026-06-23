package com.scbank.process.api.svc.common.service.support.dto.pushNotification;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 푸시알림 서비스 가입
 */
@Data
@IntegrationMessage(id = "SupPntApplyPushAlarmServiceRequest", type = Type.REQUEST)
public class SupPntApplyPushAlarmServiceRequest implements IMessageObject {

    @MessageField(id = "serno", name = "고객일련번호")
    private String serno;

    @MessageField(id = "operType", name = "단말 운영체제 정보")
    private String operType;

    @MessageField(id = "cnfrmNo", name = "Push기기확인번호")
    private String cnfrmNo;

    @MessageField(id = "pushSrvcApprvlFlg", name = "Push서비스 가입여부")
    private String pushSrvcApprvlFlg;

    @MessageField(id = "appInfo", name = "APP정보 - APP버전정보")
    private String appInfo;

    @MessageField(id = "deviceMd", name = "기기모델명")
    private String deviceMd;

    @MessageField(id = "deviceVis", name = "단말OS버전")
    private String deviceVis;

}
