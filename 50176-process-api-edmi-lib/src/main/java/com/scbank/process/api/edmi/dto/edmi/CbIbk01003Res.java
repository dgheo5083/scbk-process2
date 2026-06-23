package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "CbIbk01003Res", type = Type.RESPONSE, description = "파기대상신규등록 응답부[AS-IS EDMI_CB_IBK01_003]")
public class CbIbk01003Res implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YOPBGTB", name = "응답 업무헤더")
	private YoPBGTBHeader YOPBGTB;
	
	@MessageField(id = "YOPUPMU", name = "업무DATA")
	private YOPUPMU_REC YOPUPMU;
	
	public static class YOPUPMU_REC implements IMessageObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@MessageField(id = "YOUSID", name = "이용자번호", length = 10)
		private String YOUSID; 
		
		@MessageField(id = "YOGRGB", name = "거래구분", length = 1)
		private String YOGRGB; 
		
		@MessageField(id = "YOKUK", name = "국가코드", length = 3)
		private String YOKUK; 
	}
	
}
