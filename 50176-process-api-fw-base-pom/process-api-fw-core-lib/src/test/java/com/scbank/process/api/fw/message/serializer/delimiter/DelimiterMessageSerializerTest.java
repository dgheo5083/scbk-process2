package com.scbank.process.api.fw.message.serializer.delimiter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.context.MessageContext;

class DelimiterMessageSerializerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		DelimiterMessageSerializer serializer = new DelimiterMessageSerializer();
		
		assertThrows(UnsupportedOperationException.class, () -> serializer.serialize(null, (MessageContext)null));
		
		assertThrows(UnsupportedOperationException.class, () -> serializer.serialize(null, null, null));
	}
}
