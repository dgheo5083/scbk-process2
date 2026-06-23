package com.scbank.process.api.svc.common.dao;

import java.util.List;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgCampParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushMsgCampResult;
import com.scbank.process.api.svc.common.dao.dto.ListPushNotificationParameter;
import com.scbank.process.api.svc.common.dao.dto.ListPushNotificationResult;
import com.scbank.process.api.svc.common.dao.dto.SelectPushMsgCampResult;

@DaoComponent(id = "TmbPushMsgCampDao", database = "smsdb", description = "PUSH 발송", author = "김기주")
public interface TmbPushMsgCampDao {

    @ComponentOperation(name = "푸시 알림 목록 조회", description = "푸시 알림 목록 조회")
    List<ListPushNotificationResult> listPushNotification(ListPushNotificationParameter parameter);

    @ComponentOperation(name = "PUSH 발송 메세지 조회", description = "PUSH 발송 메세지 조회")
    SelectPushMsgCampResult selectPushMsgCamp(String msgSeq);

    @ComponentOperation(name = "PUSH 발송 메세지 목록 조회", description = "PUSH 발송 메세지 목록 조회")
    List<ListPushMsgCampResult> listPushMsgCamp(ListPushMsgCampParameter parameter);

}
