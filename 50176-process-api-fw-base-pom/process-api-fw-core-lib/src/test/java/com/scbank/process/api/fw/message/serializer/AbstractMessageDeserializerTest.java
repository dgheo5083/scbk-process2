package com.scbank.process.api.fw.message.serializer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.evaluate.ConditionEvaluatorComposite;
import com.scbank.process.api.fw.message.metadata.IConditionalFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

class AbstractMessageDeserializerTest {

	private final TestDeserializer deserializer = new TestDeserializer();

	@Test
	@DisplayName("getEncoding - field encoding 우선")
	void getEncoding() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		when(metadata.getEncoding()).thenReturn("EUC-KR");

		String result = deserializer.callGetEncoding(metadata, "UTF-8");

		assertEquals("EUC-KR", result);
	}

	@Test
	@DisplayName("getEncoding - default encoding")
	void getEncodingDefault() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		when(metadata.getEncoding()).thenReturn("");

		String result = deserializer.callGetEncoding(metadata, "UTF-8");

		assertEquals("UTF-8", result);
	}

	@Test
	@DisplayName("getFieldLength - metadata null")
	void getFieldLengthNull() {

		int[] result = deserializer.callGetFieldLength(null);

		assertArrayEquals(new int[] { 0, 0 }, result);
	}

	@Test
	@DisplayName("getFieldLength")
	void getFieldLength() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		when(metadata.getLength()).thenReturn(20);
		when(metadata.getScale()).thenReturn(2);

		int[] result = deserializer.callGetFieldLength(metadata);

		assertArrayEquals(new int[] { 20, 2 }, result);
	}

	@Test
	@DisplayName("FIXED repeat count")
	void getRepeatCountFixed() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		when(metadata.getRepeatType()).thenReturn(RepeatType.FIXED);

		when(metadata.getRepeatCount()).thenReturn("5");

		int result = deserializer.callGetRepeatCount(metadata, mock(IMessageObject.class), mock(MessageContext.class));

		assertEquals(5, result);
	}

	@Test
	@DisplayName("FIXED repeat count invalid")
	void getRepeatCountFixedInvalid() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		when(metadata.getRepeatType()).thenReturn(RepeatType.FIXED);

		when(metadata.getRepeatCount()).thenReturn("ABC");

		when(metadata.getId()).thenReturn("ARR");

		int result = deserializer.callGetRepeatCount(metadata, mock(IMessageObject.class), mock(MessageContext.class));

		assertEquals(0, result);
	}

	@Test
	@DisplayName("REFERENCE repeat count")
	void getRepeatCountReference() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext ctx = mock(MessageContext.class);

		when(metadata.getRepeatType()).thenReturn(RepeatType.REFERENCE);

		when(metadata.getRepeatCount()).thenReturn("CNT");

		when(ctx.getPathValue("CNT")).thenReturn("3");

		int result = deserializer.callGetRepeatCount(metadata, mock(IMessageObject.class), ctx);

		assertEquals(3, result);
	}

	@Test
	@DisplayName("REFERENCE repeat count invalid")
	void getRepeatCountReferenceInvalid() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		MessageContext ctx = mock(MessageContext.class);

		when(metadata.getRepeatType()).thenReturn(RepeatType.REFERENCE);

		when(metadata.getRepeatCount()).thenReturn("CNT");

		when(metadata.getId()).thenReturn("ARR");

		when(ctx.getPathValue("CNT")).thenReturn("ABC");

		int result = deserializer.callGetRepeatCount(metadata, mock(IMessageObject.class), ctx);

		assertEquals(0, result);
	}

	@Test
	@DisplayName("조건식 true")
	void conditionalFieldTrue() {

		IConditionalFieldMetadata metadata = mock(IConditionalFieldMetadata.class);

		MessageContext ctx = mock(MessageContext.class);

		ConditionEvaluatorComposite evaluator = mock(ConditionEvaluatorComposite.class);

		when(metadata.getCondition()).thenReturn("A == '1'");

		when(ctx.getConditionEvaluator()).thenReturn(evaluator);

		when(evaluator.evaluate(anyString(), eq(ctx))).thenReturn(true);

		assertTrue(deserializer.callCheckConditionalField(metadata, ctx));
	}

	@Test
	@DisplayName("조건식 false")
	void conditionalFieldFalse() {

		IConditionalFieldMetadata metadata = mock(IConditionalFieldMetadata.class);

		MessageContext ctx = mock(MessageContext.class);

		ConditionEvaluatorComposite evaluator = mock(ConditionEvaluatorComposite.class);

		when(metadata.getCondition()).thenReturn("A == '1'");

		when(ctx.getConditionEvaluator()).thenReturn(evaluator);

		when(evaluator.evaluate(anyString(), eq(ctx))).thenReturn(false);

		assertFalse(deserializer.callCheckConditionalField(metadata, ctx));
	}

	@Test
	@DisplayName("조건 evaluator null")
	void conditionalFieldEvaluatorNull() {

		IConditionalFieldMetadata metadata = mock(IConditionalFieldMetadata.class);

		MessageContext ctx = mock(MessageContext.class);

		when(ctx.getConditionEvaluator()).thenReturn(null);

		assertFalse(deserializer.callCheckConditionalField(metadata, ctx));
	}

	@Test
	@DisplayName("일반 필드는 true")
	void normalField() {

		IMessageFieldMetadata metadata = mock(IMessageFieldMetadata.class);

		assertTrue(deserializer.callCheckConditionalField(metadata, mock(MessageContext.class)));
	}

	@Test
	@DisplayName("findIntegrationMessageMetadata")
	void findMetadata() {

		IIntegrationMessageMetadataRegistrar registrar = mock(IIntegrationMessageMetadataRegistrar.class);

		IIntegrationMessageMetadata metadata = mock(IIntegrationMessageMetadata.class);

		when(registrar.getMetadata(String.class)).thenReturn(metadata);

		deserializer.integrationMessageMetadataRegistrar = registrar;

		assertSame(metadata, deserializer.callFindMetadata(String.class));
	}

	static class TestDeserializer extends AbstractMessageDeserializer {

		String callGetEncoding(IMessageFieldMetadata metadata, String encoding) {
			return getEncoding(metadata, encoding);
		}

		int[] callGetFieldLength(IMessageFieldMetadata metadata) {
			return getFieldLength(metadata);
		}

		int callGetRepeatCount(IMessageFieldMetadata metadata, IMessageObject object, MessageContext ctx) {
			return getRepeatCount(metadata, object, ctx);
		}

		boolean callCheckConditionalField(IMessageFieldMetadata metadata, MessageContext ctx) {
			return checkConditionalField(metadata, ctx);
		}

		IIntegrationMessageMetadata callFindMetadata(Class<?> clazz) {
			return findIntegrationMessageMetadata(clazz);
		}
	}

}
