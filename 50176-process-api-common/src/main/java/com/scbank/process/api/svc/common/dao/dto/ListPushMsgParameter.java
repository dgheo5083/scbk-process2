package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListPushMsgParameter {

    private String bnkingId;

    private String inqStrDate;

    private String inqEndDate;

}
