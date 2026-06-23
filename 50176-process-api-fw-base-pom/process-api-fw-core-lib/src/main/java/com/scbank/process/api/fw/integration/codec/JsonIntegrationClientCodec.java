package com.scbank.process.api.fw.integration.codec;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonIntegrationClientCodec implements IntegrationClientCodec {

	private final JacksonMessageMapper jacksonMessageMapper;
	
	@Override
	public <T extends IMessageObject> byte[] encode(MessageContext context, T request) throws Exception {
		return this.jacksonMessageMapper.serialize(request, context);
	}

	@Override
	public <T extends IMessageObject> T decode(MessageContext context, byte[] response, Class<T> responseType)
			throws Exception {
		return this.jacksonMessageMapper.deserialize(response, responseType, context);
	}
}
