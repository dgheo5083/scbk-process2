package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DuplicationLoginCheckInfoParameter {

	private String userId;

	private long loginDt;

	private String chnlMk;

}