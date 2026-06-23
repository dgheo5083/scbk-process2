package com.scbank.process.api.fw.message.serializer.delimiter;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.serializer.AbstractMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.IMessageDeserializer;

public class DelimiterMessageDeserializer extends AbstractMessageDeserializer implements IMessageDeserializer {

	@Override
	public <T extends IMessageObject> T deserialize(byte[] source, Class<T> targetType, MessageContext ctx)
			throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends IMessageObject> T deserialize(byte[] source,
			IIntegrationMessageMetadata integrationMessageMetadata, MessageContext ctx) throws Exception {
		throw new UnsupportedOperationException();
	}
}
