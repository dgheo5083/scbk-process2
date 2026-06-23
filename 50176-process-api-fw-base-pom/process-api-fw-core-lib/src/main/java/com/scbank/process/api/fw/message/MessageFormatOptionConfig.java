package com.scbank.process.api.fw.message;

import java.util.Map;

import lombok.Data;

@Data
public class MessageFormatOptionConfig {

	private Map<String, Object> serialization;

	private Map<String, Object> deserialization;

}