package com.scbank.process.api.fw.message.converter.format.jackson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.StringFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

class JacksonStringFieldConverterTest {

	@Test
	@DisplayName("read - NullNode ━")
	void readNullNode() throws Exception {

		StringFieldConverter delegate = mock(StringFieldConverter.class);

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext context = mock(MessageContext.class);

		when(metadata.getEncoding()).thenReturn("UTF-8");

		when(context.getDefaultEncoding()).thenReturn("UTF-8");

		when(delegate.read(any(byte[].class), eq(metadata), eq(context))).thenReturn(null);

		JacksonStringFieldConverter converter = new JacksonStringFieldConverter(delegate);

		String result = converter.read(NullNode.getInstance(), metadata, context);

		assertNull(result);

		verify(delegate).read(any(byte[].class), eq(metadata), eq(context));
	}

	@Test
	@DisplayName("read - TextNode ━")
	void readTextNode() throws Exception {

		StringFieldConverter delegate = mock(StringFieldConverter.class);

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext context = mock(MessageContext.class);

		when(metadata.getEncoding()).thenReturn("UTF-8");

		when(context.getDefaultEncoding()).thenReturn("UTF-8");

		when(delegate.read(any(byte[].class), eq(metadata), eq(context))).thenReturn("SCBANK");

		JacksonStringFieldConverter converter = new JacksonStringFieldConverter(delegate);

		String result = converter.read(new TextNode("SCBANK"), metadata, context);

		assertEquals("SCBANK", result);

		verify(delegate).read(any(byte[].class), eq(metadata), eq(context));
	}

	@Test
	@DisplayName("write - String -> TextNode")
	void write() throws Exception {

		StringFieldConverter delegate = mock(StringFieldConverter.class);

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext context = mock(MessageContext.class);

		when(metadata.getEncoding()).thenReturn("UTF-8");

		when(context.getDefaultEncoding()).thenReturn("UTF-8");

		when(delegate.write(eq("SCBANK"), eq(metadata), eq(context)))
				.thenReturn("SCBANK".getBytes(StandardCharsets.UTF_8));

		JacksonStringFieldConverter converter = new JacksonStringFieldConverter(delegate);

		JsonNode result = converter.write("SCBANK", metadata, context);

		assertNotNull(result);
		assertTrue(result.isTextual());
		assertEquals("SCBANK", result.asText());

		verify(delegate).write(eq("SCBANK"), eq(metadata), eq(context));
	}

}
