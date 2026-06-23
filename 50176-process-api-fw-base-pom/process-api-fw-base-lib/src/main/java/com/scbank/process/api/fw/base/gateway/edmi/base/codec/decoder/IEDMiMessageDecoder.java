package com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder;

import java.lang.reflect.Type;

import feign.Response;
import feign.codec.Decoder;

/**
 * 
 */
public interface IEDMiMessageDecoder extends Decoder {

	/**
	 * 
	 * @param response
	 * @param type
	 * @return
	 */
	boolean supported(Response response, Type type);
}
