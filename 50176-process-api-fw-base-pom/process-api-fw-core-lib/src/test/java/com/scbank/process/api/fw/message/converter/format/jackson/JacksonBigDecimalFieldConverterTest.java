package com.scbank.process.api.fw.message.converter.format.jackson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.BigDecimalFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

class JacksonBigDecimalFieldConverterTest {

	@Test
	@DisplayName("read - NullNode이면 null 반환")
	void readNullNode() throws Exception {

		BigDecimalFieldConverter delegate = mock(BigDecimalFieldConverter.class);

		JacksonBigDecimalFieldConverter converter = new JacksonBigDecimalFieldConverter(delegate);

		BigDecimal result = converter.read(NullNode.getInstance(), mock(IMessageFieldMetadata.class),
				mock(MessageContext.class));

		assertNull(result);

		verifyNoInteractions(delegate);
	}

	@Test
	@DisplayName("read - JsonNode -> BigDecimal 변환")
	void read() throws Exception {

		BigDecimalFieldConverter delegate = mock(BigDecimalFieldConverter.class);

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext context = mock(MessageContext.class);

		when(metadata.getEncoding()).thenReturn("UTF-8");

		when(context.getDefaultEncoding()).thenReturn("UTF-8");

		when(delegate.read(any(byte[].class), eq(metadata), eq(context))).thenReturn(new BigDecimal("123.45"));

		JacksonBigDecimalFieldConverter converter = new JacksonBigDecimalFieldConverter(delegate);

		BigDecimal result = converter.read(new TextNode("123.45"), metadata, context);

		assertEquals(new BigDecimal("123.45"), result);

		verify(delegate).read(any(byte[].class), eq(metadata), eq(context));
	}

	@Test
	@DisplayName("write - source null이면 null 반환")
	void writeNull() throws Exception {

		BigDecimalFieldConverter delegate = mock(BigDecimalFieldConverter.class);

		JacksonBigDecimalFieldConverter converter = new JacksonBigDecimalFieldConverter(delegate);

		JsonNode result = converter.write(null, mock(IMessageFieldMetadata.class), mock(MessageContext.class));

		assertNull(result);

		verifyNoInteractions(delegate);
	}

	@Test
	@DisplayName("write - BigDecimal -> JsonNode 변환")
	void write() throws Exception {

		BigDecimalFieldConverter delegate = mock(BigDecimalFieldConverter.class);

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext context = mock(MessageContext.class);

		when(metadata.getEncoding()).thenReturn("UTF-8");

		when(context.getDefaultEncoding()).thenReturn("UTF-8");

		when(delegate.write(any(BigDecimal.class), eq(metadata), eq(context)))
				.thenReturn("123.45".getBytes(StandardCharsets.UTF_8));

		JacksonBigDecimalFieldConverter converter = new JacksonBigDecimalFieldConverter(delegate);

		JsonNode result = converter.write(new BigDecimal("123.45"), metadata, context);

		assertNotNull(result);
		assertTrue(result.isNumber());
		assertEquals(new BigDecimal("123.45"), result.decimalValue());

		verify(delegate).write(eq(new BigDecimal("123.45")), eq(metadata), eq(context));
	}

}
