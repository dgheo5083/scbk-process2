package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * EDMI 오류 메시지부
 */
@Data
@IntegrationMessage(id = "edmiError")
public class EdmiError implements IMessageObject  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MessageField(id = "YOPUPMU", name = "업무DATA")
	private YOPUPMU YOPUPMU;
	
	@Data
	public static class YOPUPMU implements IMessageObject {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@MessageField(id = "YOEUSID", name = "이용자번호", length = 10)
		private String YOEUSID;
		
		@MessageField(id = "YOMODN", name = "", length = 8)
		private String YOMODN;
		
		@MessageField(id = "YOCONN", name = "접속해제여부", length = 10)
		private String YOCONN;
		
		@MessageField(id = "YOERMSG1", name = "", length = 74)
		private String YOERMSG1;
		
		@MessageField(id = "YOERMSG2", name = "", length = 78)
		private String YOERMSG2;
		
		@MessageField(id = "YOMSG1", name = "", length = 74)
		private String YOMSG1;
		
		@MessageField(id = "YOMSG2", name = "", length = 74)
		private String YOMSG2;
		
		@MessageField(id = "YOMSG3", name = "", length = 74)
		private String YOMSG3;
		
		@MessageField(id = "YOGOYU", name = "타행이체고유번호", length = 12)
		private String YOGOYU;
		
		@MessageField(id = "YOYDCD", name = "타행응답코드", length = 4)
		private String YOYDCD;
		
		@MessageField(id = "YOKEY", name = "업무Key", length = 30)
		private String YOKEY;
	}

}
