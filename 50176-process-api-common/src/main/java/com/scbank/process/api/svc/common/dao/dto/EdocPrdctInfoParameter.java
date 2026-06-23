package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdocPrdctInfoParameter {

    private String prdctId;

    private String edocCd;

}
