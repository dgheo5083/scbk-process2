package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

/**
 * 채널 출력전문 메시지부
 */
@Data
public class EdmiMsgInfo implements IMessageObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MessageField(id = "STD_TMSG_DAT_KIND_CD", name = "데이타종류", length = 1)
	private String stdTmsgDatKindCd;
	
	@MessageField(id = "STD_TMSG_DAT_LEN", name = "데이타길이", length = 5)
	private Integer stdTmsgDatLen;
	
	@MessageField(id = "OUTPUT_DRV_MSG", name = "화면 출력유도 메시지", length = 350)
	private String outputDrvMsg;
	
	@MessageField(id = "MSG_INFO_REPT_TCNT", name = "메시지정보반복횟수", length = 2)
	private Integer msgInfoReptTcnt;
	
	@MessageField(id = "HOST_MSG", name = "메시지부")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "msgInfo/msgInfoReptTcnt")
	private List<HostMsg> hostMsg;
	
	@MessageField(id = "ADTN_MSG_REPT_TCNT", name = "부가메시지반복횟수", length = 2)
	private Integer adtnMsgReptTcnt;
	
	@MessageField(id = "MCA_MSG", name = "부가메시지부")
	@RepeatedField(repeatType = RepeatType.REFERENCE, repeatCount = "msgInfo/adtnMsgReptTcnt")
	private List<McaMsg> mcaMsg;
	
	@MessageField(id = "NEW_ERR_MSG_CD", name = "신에러코드", length = 10)
	private String newErrMsgCd;
	
	@MessageField(id = "NEW_ERR_MSG_CTT", name = "신에러메시지", length = 400)
	private String newErrMsgCtt;
	
	/**
	 * 호스트 메시지부
	 */
	@Data
	public static class HostMsg implements IMessageObject {
		
		private static final long serialVersionUID = 1L;
		
		@MessageField(id = "MSG_CD", name = "메시지코드", length = 4)
		private String msgCd;
		
		@MessageField(id = "MSG_CTT", name = "메시지내용", length = 400)
		private String msgCtt;
	}
	
	/**
	 * 부가메시지부
	 */
	@Data
	public static class McaMsg implements IMessageObject {
		
		private static final long serialVersionUID = 1L;

		@MessageField(id = "ADTN_MSG_CD", name = "부가메시지코드", length = 9)
		private String adtnMsgCd;
		
		@MessageField(id = "ADTN_MSG", name = "부가메시지", length = 200)
		private String adtnMsg;
	}
}
