package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListPushNotificationResult {

    private String msgSeq;

    private String msgType;

    private String bnkingId;

    private String vectorId;

    private String sendDate;

    private String campSeq;

    private String title;

    private String cnts;

    private String contentsMsg;

    private String webUrl;

    private String imgUrl;

}
