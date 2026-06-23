package com.scbank.process.api.svc.common.dao.dto;

import lombok.Data;

@Data
public class InsertUserPushAgreeParameter {

    private String msgTypeSubCd;

    private String useYn;

    private String userId;

}
