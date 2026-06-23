package com.scbank.process.api.fw.message.converter.format.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;

/**
 * Jackson 기반 JSON 포맷에서 Boolean 타입 필드를 읽고 쓰기 위한 변환기 클래스.
 */
public class JacksonBooleanFieldConverter extends AbstractJacksonMessageFieldConverter<Boolean>{

	@Override
	public Boolean read(JsonNode source, IMessageFieldMetadata fieldMetadata, MessageContext messageContext) throws Exception {
		if (source == null || source.isNull()) {
			return false;
		}
		return source.asBoolean();
	}

	@Override
	public JsonNode write(Boolean value, IMessageFieldMetadata metadata, MessageContext messageContext) throws Exception {
		if (value == null) {
			return JsonNodeFactory.instance.booleanNode(Boolean.FALSE);
		}
		return JsonNodeFactory.instance.booleanNode(value);
	}
}
