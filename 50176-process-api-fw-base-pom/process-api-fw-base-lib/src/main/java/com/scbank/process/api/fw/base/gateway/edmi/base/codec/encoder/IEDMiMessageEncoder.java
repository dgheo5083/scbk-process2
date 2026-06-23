package com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder;

import java.lang.reflect.Type;

import feign.RequestTemplate;
import feign.codec.Encoder;

public interface IEDMiMessageEncoder extends Encoder {
	/**
	 * 
	 * @param object
	 * @param bodyType
	 * @param template
	 * @return
	 */
	boolean supported(Object object, Type bodyType, RequestTemplate template);
}
