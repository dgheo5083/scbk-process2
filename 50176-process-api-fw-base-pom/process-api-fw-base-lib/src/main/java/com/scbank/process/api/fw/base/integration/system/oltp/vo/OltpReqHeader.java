package com.scbank.process.api.fw.base.integration.system.oltp.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Builder;
import lombok.Data;

/**
 * 호스트 요청 헤더
 * 
 * @author sungdon.choi
 */
@Data
@Builder
@IntegrationMessage(id = "oltpReqHeader")
public class OltpReqHeader implements IMessageObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 호스트 공통부
	 */
	@MessageField(id = "oltpCommon")
	private OltpCommon oltpCommon;
}
