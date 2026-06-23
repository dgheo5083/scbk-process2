package com.scbank.process.api.svc.common.dao.dto;

// import com.scbank.process.api.fw.message.IMessageObject;
// import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class AddCntrctDocumentResult {
	private String addCntrctFileNm; // 공통약관파일명
	private String addCntrctNm; // 공통약관명
	private String addCntrctOfferCode; // 공통약관아이디계약서류제공여부
}