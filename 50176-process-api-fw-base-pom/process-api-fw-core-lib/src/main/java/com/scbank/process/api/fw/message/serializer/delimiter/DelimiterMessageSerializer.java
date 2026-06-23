package com.scbank.process.api.fw.message.serializer.delimiter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.serializer.AbstractMessageSerializer;
import com.scbank.process.api.fw.message.serializer.IMessageSerializer;

public class DelimiterMessageSerializer extends AbstractMessageSerializer implements IMessageSerializer {

	@Override
	public <T extends IMessageObject> byte[] serialize(T source, MessageContext ctx) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends IMessageObject> byte[] serialize(T source, IIntegrationMessageMetadata metadata,
			MessageContext ctx) throws Exception {
		throw new UnsupportedOperationException();
	}
}
