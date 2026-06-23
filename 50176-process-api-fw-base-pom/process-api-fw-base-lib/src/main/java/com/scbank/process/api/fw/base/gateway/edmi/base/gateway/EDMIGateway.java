package com.scbank.process.api.fw.base.gateway.edmi.base.gateway;

import java.util.List;

import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiRequestMessage;
import com.scbank.process.api.fw.base.gateway.edmi.base.dto.EDMiResponseMessage;

/**
 * 
 */
public interface EDMIGateway {

	/**
	 * 
	 * @return
	 */
	List<String> getSupportedTypeNames();
	/**
	 * 
	 * @param request
	 * @return
	 */
	EDMiResponseMessage send(EDMiRequestMessage request);
}
