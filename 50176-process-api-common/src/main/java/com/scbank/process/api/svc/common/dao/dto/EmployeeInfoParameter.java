package com.scbank.process.api.svc.common.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeInfoParameter {

    private String emplyNm;

    private String clerkNo;

    private String loanTypeProduct;

}
