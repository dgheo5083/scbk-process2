package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FidoBackupInfoParameter {

    private String userId;

    private String trId;

    private String hpNum;

}
