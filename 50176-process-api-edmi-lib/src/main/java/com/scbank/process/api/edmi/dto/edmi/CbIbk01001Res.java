package com.scbank.process.api.edmi.dto.edmi;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

@IntegrationMessage(
		id = "CbIbk01001Res", 
		type = Type.RESPONSE, 
		description = "FATCA등록/조회 응답부[AS-IS EDMI_CB_IBK01_001]")
public class CbIbk01001Res implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "YOPBGTB", name = "응답 업무헤더")
	private YoPBGTBHeader YOPBGTB;
	
	@MessageField(id = "YOPUPMU", name = "업무DATA")
	private YOPUPMU_REC YIPUPMU;
	
	public static class YOPUPMU_REC implements IMessageObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@MessageField(id = "YOUSID", name = "이용자번호", length = 10, masking = true, maskingType = "01")
		private String YOUSID; 
		
		@MessageField(id = "YOFAFLG", name = "거래전 평가 FLAG", length = 2)
		private String YOFAFLG;
		
		@MessageField(id = "YOGEORE", name = "5개사, 6개인, 7당행", length = 2)
		private String YOGEORE;
		
		@MessageField(id = "YO303YN", name = "미국국적포함여부", length = 1)
		private String YO303YN;
		
		@MessageField(id = "YOFATYN", name = "온라인FATCA가능", length = 1)
		private String YOFATYN;
		
		@MessageField(id = "YOUSPP", name = "비미국인", length = 1)
		private String YOUSPP;
		
		@MessageField(id = "YODUMMY", name = "DUMMY", length = 50)
		private String YODUMMY;
	}
}
