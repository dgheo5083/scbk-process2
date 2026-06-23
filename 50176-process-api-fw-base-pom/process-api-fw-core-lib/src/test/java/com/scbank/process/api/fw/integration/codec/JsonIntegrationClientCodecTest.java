package com.scbank.process.api.fw.integration.codec;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;

class JsonIntegrationClientCodecTest {

	@Test
	void encode_mapper_serialize_test() throws Exception {

		JacksonMessageMapper mapper = mock(JacksonMessageMapper.class);
		JsonIntegrationClientCodec codec = new JsonIntegrationClientCodec(mapper);

		MessageContext context = mock(MessageContext.class);
		IMessageObject request = mock(IMessageObject.class);

		byte[] expected = new byte[] { 1, 2, 3 };

		when(mapper.serialize(request, context)).thenReturn(expected);

		byte[] result = codec.encode(context, request);

		assertArrayEquals(expected, result);
		verify(mapper).serialize(request, context);
	}

	@Test
	void decode_mapper_deserialize_test() throws Exception {

		JacksonMessageMapper mapper = mock(JacksonMessageMapper.class);
		JsonIntegrationClientCodec codec = new JsonIntegrationClientCodec(mapper);

		MessageContext context = mock(MessageContext.class);
		byte[] response = new byte[] { 9, 9 };
		
		Class<IMessageObject> responseType = IMessageObject.class;
		
		IMessageObject expected = mock(IMessageObject.class);

		when(mapper.deserialize(response, responseType, context)).thenReturn(expected);

		IMessageObject result = codec.decode(context, response, responseType);

		assertSame(expected, result);
		verify(mapper).deserialize(response, responseType, context);
	}
}
