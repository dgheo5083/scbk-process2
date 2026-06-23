package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinTechCertUserInfoParameter {

    private String userId;

    private String connectType;

    private String finCertSeqNum;

}
