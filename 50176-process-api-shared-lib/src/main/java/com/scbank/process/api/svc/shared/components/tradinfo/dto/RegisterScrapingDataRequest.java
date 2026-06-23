package com.scbank.process.api.svc.shared.components.tradinfo.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterScrapingDataRequest", type = Type.REQUEST)
public class RegisterScrapingDataRequest implements IMessageObject {
	@MessageField(id = "custNo", name = "고객번호", example = "9999")
	private String custNo;
	@MessageField(id = "tradNo", name = "거래번호", example = "")
	private Integer tradNo;
	@MessageField(id = "objInst", name = "대상기관", example = "")
    private String objInst;
	@MessageField(id = "objDocCd", name = "대상문서코드", example = "")
    private String objDocCd;
	@MessageField(id = "scrppngData", name = "스크래핑데이터(JSON Text)", example = "")
    private String scrppngData;
	@MessageField(id = "errCd", name = "에러코드", example = "")
    private String errCd;
	@MessageField(id = "errMsg", name = "에러메시지", example = "")
    private String errMsg;
	@MessageField(id = "scrType", name = "스크래핑타입 G:공공데이터, S:스크래핑", example = "S")
    private String scrType;
	@MessageField(id = "certType", name = "서명타입 1:공동, B:금융 pin, C:금융생체, 9:디지털", example = "1")
    private String certType;
}
