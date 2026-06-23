package com.scbank.process.api.fw.message.converter.format.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.BooleanNode;

class JacksonBooleanFieldConverterTest {

	private final JacksonBooleanFieldConverter converter = new JacksonBooleanFieldConverter();
	
	@Test
	void read_returns_false_when_source_is_null() throws Exception {
		Boolean result = converter.read(null, null, null);
		assertFalse(result);
	}
	
	@Test
	void read_returns_false_when_soirce_is_json_null() throws Exception {
		Boolean result = converter.read(BooleanNode.TRUE, null, null);
		assertTrue(result);
	}

	@Test
	void write_returns_false_node_when_value_is_null() throws Exception {
		assertEquals(BooleanNode.TRUE, converter.write(Boolean.TRUE, null, null));
	}
	
	@Test
	void write_returns_boolean_node() throws Exception {
		assertEquals(BooleanNode.TRUE, converter.write(Boolean.TRUE, null, null));
	}

}
