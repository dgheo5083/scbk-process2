package com.scbank.process.api.svc.common.dao.dto;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "AddCntrctDocument", type = Type.RESPONSE, description = "계약서류 응답")
public class AddCntrctDocument implements IMessageObject {
	@MessageField(id = "addCntrctFileNm", name = "공통약관파일명")
	private String addCntrctFileNm; // 공통약관파일명
	@MessageField(id = "addCntrctNm", name = "공통약관명")
	private String addCntrctNm; // 공통약관명
	@MessageField(id = "addCntrctOfferCode", name = "공통약관아이디계약서류제공여부")
	private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
}