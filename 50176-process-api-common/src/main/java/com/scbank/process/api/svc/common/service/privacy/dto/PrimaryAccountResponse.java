package com.scbank.process.api.svc.common.service.privacy.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "InquiryExposedPersonResponse", type = Type.RESPONSE)
public class PrimaryAccountResponse implements IMessageObject {

    @MessageField(id = "yoGUNSU", name = "yoGUNSU")
    private int yoGUNSU;
    
    @MessageField(id = "yoIBID", name = "yoIBID")
	private String yoIBID;
    
    @MessageField(id = "yoNAME", name = "yoNAME")
	private String yoNAME;

    @MessageField(id = "ConnectType", name = "ConnectType")
	private String ConnectType;
    
    @MessageField(id = "noLogin", name = "noLogin")
	private String noLogin;
    
    @MessageField(id = "yocdcok", name = "yocdcok")
	private String yocdcok;
    
    @MessageField(id = "custNo", name = "custNo")
	private String custNo;
	
	@MessageField(id = "yoMYINF_REC", name = "계좌번호목록")
	@RepeatedField(repeatType = RepeatType.REFERENCE)
	private List<myinfRec> yoMYINF_REC;
	
	@Data
    public static class myinfRec implements IMessageObject {
		@MessageField(id = "mygj", name = "mygj")
        private String mygj;
		
		@MessageField(id = "mygjFmt", name = "mygjFmt")
		private String mygjFmt;
		
		@MessageField(id = "mygjNm", name = "mygjNm")
		private String mygjNm;
	}
}