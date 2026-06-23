package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IPInsideAboardParameter {

    private String ipStartAddr;

    private String ipEndAddr;
}
