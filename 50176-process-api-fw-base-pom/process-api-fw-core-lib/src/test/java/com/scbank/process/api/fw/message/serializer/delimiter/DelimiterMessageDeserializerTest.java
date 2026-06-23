package com.scbank.process.api.fw.message.serializer.delimiter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.DefaultIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;

class DelimiterMessageDeserializerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void deserialize_with_targetType_should_throw_unsupported_operation() {
		DelimiterMessageDeserializer des = new DelimiterMessageDeserializer();
		
		assertThrows(UnsupportedOperationException.class, () -> des.deserialize(null, IMessageObject.class, (MessageContext)null));
	}
	
	@Test
	void deserialize_with_metadata_should_throw_unsupported_operation() {
		DelimiterMessageDeserializer des = new DelimiterMessageDeserializer();
		
		IIntegrationMessageMetadata messageMetadata = DefaultIntegrationMessageMetadata.builder().build();
		
		assertThrows(UnsupportedOperationException.class, () -> des.deserialize(null, messageMetadata, null));
	}

}
