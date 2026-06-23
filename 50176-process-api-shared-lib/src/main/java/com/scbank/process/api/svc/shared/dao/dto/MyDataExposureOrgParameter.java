package com.scbank.process.api.svc.shared.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MYDATA 업권노출 기관코드 Result
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyDataExposureOrgParameter {

    private String serverType;
    private String authType;

}
