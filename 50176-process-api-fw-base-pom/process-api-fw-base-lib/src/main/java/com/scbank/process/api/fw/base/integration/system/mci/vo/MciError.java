package com.scbank.process.api.fw.base.integration.system.mci.vo;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;

import lombok.Data;

/**
 * MCI 에러 공통부
 * 
 * @author sungdon.choi
 */
@Data
@IntegrationMessage(id = "mciError")
public class MciError implements IMessageObject {

	private static final long serialVersionUID = 1L;

}
