package com.scbank.process.api.fw.base.gateway.edmi.base.codec.encoder;

import java.lang.reflect.Type;
import java.util.List;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RequiredArgsConstructor
public class EDMiMessageEncoderCompoiste implements Encoder {

	private final List<IEDMiMessageEncoder> encoders;
	
	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
		for (IEDMiMessageEncoder encoder : encoders) {
			if (encoder.supported(object, bodyType, template)) {
				encoder.encode(object, bodyType, template);
				return;
			}
		}
	}
}
