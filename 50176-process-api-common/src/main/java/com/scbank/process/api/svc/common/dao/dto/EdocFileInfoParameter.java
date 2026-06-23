package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdocFileInfoParameter {

    private String custNo;

    private String tradNo;

    private String edocCd;

}
