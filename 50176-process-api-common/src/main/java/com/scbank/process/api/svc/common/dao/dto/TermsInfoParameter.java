package com.scbank.process.api.svc.common.dao.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermsInfoParameter {

    private String actionType;

    private List<String> loctnCdList;

    private String prvsnCd;

    private List<String> prvsnCdList;

}
