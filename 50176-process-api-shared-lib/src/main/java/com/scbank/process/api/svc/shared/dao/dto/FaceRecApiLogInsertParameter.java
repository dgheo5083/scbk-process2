package com.scbank.process.api.svc.shared.dao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaceRecApiLogInsertParameter {
	private String userCifNo;
	private String faceTransactionId;
	private String custNo;
	private String tradNo;
	private String tradCls;
	private String chnlGb;
	private String apiUrl;
	private String data;
	private String resCode;
}
