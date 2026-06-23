package com.scbank.process.api.fw.base.gateway.edmi.base.codec.decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EDMiMessageDecoderComposite implements Decoder {

	private final List<IEDMiMessageDecoder> decoders;
	
	@Override
	public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
		for (IEDMiMessageDecoder decoder : decoders) {
			if (!decoder.supported(response, type)) {
				continue;
			}
			
			try {
				return decoder.decode(response, type);
			} catch (Exception e) {
				throw new DecodeException(response.status(), "EDMi Message Decode failed: " + decoder.getClass().getSimpleName(), response.request(), e);
			}
		}
		
		throw new DecodeException(response.status(), "EDMi Message Decoder Not Found ", response.request());
	}
}
