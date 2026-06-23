package com.scbank.process.api.fw.base.integration.system.oltp.vo;

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
@IntegrationMessage(id = "oltpResHeader")
public class OltpResHeader implements IMessageObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 호스트 공통부
	 */
	@MessageField(id = "oltpCommon")
	private OltpCommon oltpCommon;
}
