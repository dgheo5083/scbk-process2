package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTermsInfoParameter {

    final String prdctId;

    final String prdctCd;

    final String prdctType;

}
