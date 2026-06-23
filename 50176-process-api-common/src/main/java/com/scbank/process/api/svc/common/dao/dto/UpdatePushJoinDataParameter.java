package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class UpdatePushJoinDataParameter {

    private String operType;

    private String userNm;

    private String telno;

    private String cnfrmNo;

    private String pushSrvcApprvlFlg;

    private String appInfo;

    private String deviceMd;

    private String deviceVis;

    private String bnkingId;

    private String serno;

}
