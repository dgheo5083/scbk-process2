package com.scbank.process.api.fw.message.converter.format.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.format.common.DoubleFieldConverter;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

@ExtendWith(MockitoExtension.class)
class JacksonDoubleFieldConverterTest {

	@Mock
	DoubleFieldConverter fieldConverter;
	
	@Mock
	IMessageFieldMetadata fieldMetadata;
	
	@Mock
	MessageContext messageContext;
	
	JacksonDoubleFieldConverter converter;
	
	@BeforeEach
	void setup() {
		converter = new JacksonDoubleFieldConverter(fieldConverter);
	}
	
	@Test
	void read_whenSource_returnNull() throws Exception {
		Double result = converter.read(null, fieldMetadata, messageContext);
		
		assertNull(result);
		verifyNoInteractions(fieldConverter);
	}
	
	void read_whenSourceIsnullNode_returnsNull() throws Exception {
		JsonNode source = NullNode.getInstance();
		
		Double result = converter.read(source, fieldMetadata, messageContext);
		
		assertNull(result);
		verifyNoInteractions(fieldConverter);
	}
	
	@Test
	void read_convertsTextToBytesAndDelegates() throws Exception {
		
		JsonNode source = TextNode.valueOf("123.45");
		
		when(messageContext.getDefaultEncoding()).thenReturn("utf-8");
		when(fieldConverter.read(eq("123.45".getBytes("utf-8")), eq(fieldMetadata), eq(messageContext))).thenReturn(123.45);
		
		Double result = converter.read(source, fieldMetadata, messageContext);
		
		assertEquals(123.45, result);
		verify(fieldConverter).read(eq("123.45".getBytes("utf-8")), eq(fieldMetadata), eq(messageContext));
	}
	
	@Test
	void write_whenSource_returnNull() throws Exception {
		JsonNode result = converter.write(null, fieldMetadata, messageContext);
		
		assertNull(result);
		verifyNoInteractions(fieldConverter);
	}
	
	@Test
	void write_convertsTextToBytesAndDelegates() throws Exception {
		when(messageContext.getDefaultEncoding()).thenReturn("utf-8");
		when(fieldConverter.write(123.45, fieldMetadata, messageContext)).thenReturn("123.45".getBytes("utf-8"));
		
		JsonNode result = converter.write(123.45, fieldMetadata, messageContext);
		
		assertNotNull(result);
		assertTrue(result.isNumber());
		verify(fieldConverter).write(123.45, fieldMetadata, messageContext);
	}

}
