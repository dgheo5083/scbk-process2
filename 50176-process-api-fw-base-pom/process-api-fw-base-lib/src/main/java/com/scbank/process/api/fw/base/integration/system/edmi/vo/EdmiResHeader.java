package com.scbank.process.api.fw.base.integration.system.edmi.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

/**
 * 호스트 응답 헤더
 * 
 * @author sungdon.choi
 */
@Data
@IntegrationMessage(id = "edmiResHeader")
public class EdmiResHeader implements IMessageObject  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@MessageField(id = "SYSTEMHEADER", name = "채널 입/출력 전문 시스템 헤더부")
	private EdmiSystemHeader systemHeader;
	
	@MessageField(id = "TRANCOMMONHEADER", name = "채널 입/출력 전문 거래공통부")
	private EdmiTranCommonHeader tranCommonHeader;
	
	@MessageField(id = "MSGINFO", name = "채널 출력전문 메시지부")
	private EdmiMsgInfo msgInfo;
	
	@MessageField(id = "CHANNELDATA", name = "채널 출력전문 채널 데이터부")
	private EdmiChannelData channelData;
	
	@MessageField(id = "CUSTOMINFO", name = "채널 출력전문 고객정보부 출력 데이터")
	private EdmiCustomInfo customInfo;
	
	@MessageField(id = "CONTTRAN", name = "연속거래 데이터")
	private EdmiContTran contTran;
}
