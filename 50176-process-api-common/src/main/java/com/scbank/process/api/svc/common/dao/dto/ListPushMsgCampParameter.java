package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class ListPushMsgCampParameter {

    private String bnkingId;

    private String inqStrDate;

    private String inqEndDate;

    private String inquirySetKind;

}
