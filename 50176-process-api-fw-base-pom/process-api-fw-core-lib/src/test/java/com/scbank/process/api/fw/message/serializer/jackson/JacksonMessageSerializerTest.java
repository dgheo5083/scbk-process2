package com.scbank.process.api.fw.message.serializer.jackson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("JacksonMessageSerializer Tests")
class JacksonMessageSerializerTest {

    private JacksonMessageSerializer serializer;
    private ObjectMapper objectMapper;

    @Mock
    private MessageContext messageContext;

    @Mock
    private IIntegrationMessageMetadata integrationMessageMetadata;

    @Mock
    private IMessageFieldMetadata fieldMetadata;

    @Mock
    private IMessageFieldConverterRegistry converterRegistry;

    @Mock
    private IIntegrationMessageMetadataRegistrar metadataRegistrar;

    @Mock
    private SerializationOptions serializationOptions;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        serializer = new JacksonMessageSerializer(objectMapper);
    }

    // Test message object for testing
    public static class TestMessageObject implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private String name;
        private Integer age;
        private List<String> items = new ArrayList<>();
        private NestedObject nested;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public List<String> getItems() { return items; }
        public void setItems(List<String> items) { this.items = items; }
        public NestedObject getNested() { return nested; }
        public void setNested(NestedObject nested) { this.nested = nested; }
    }

    public static class NestedObject implements IMessageObject {
        private static final long serialVersionUID = 1L;
        private String value;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    @Nested
    @DisplayName("serialize() method tests")
    class SerializeTests {

        @Test
        @DisplayName("Should serialize object to JSON bytes with metadata")
        void testSerializeWithMetadata() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setName("test");
            source.setAge(25);

            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(serializationOptions.enabled(MessageFormatOption.PRETTY_FORMAT)).thenReturn(false);

            byte[] result = serializer.serialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
            String jsonStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("{}", jsonStr);
        }

        @Test
        @DisplayName("Should serialize object to pretty JSON bytes")
        void testSerializeWithPrettyFormat() throws Exception {
            TestMessageObject source = new TestMessageObject();

            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(serializationOptions.enabled(MessageFormatOption.PRETTY_FORMAT)).thenReturn(true);

            byte[] result = serializer.serialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should serialize using source class metadata lookup")
        void testSerializeWithSourceClassMetadata() throws Exception {
            TestMessageObject source = new TestMessageObject();

            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMessageMetadata);
                when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());
                when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
                when(serializationOptions.enabled(MessageFormatOption.PRETTY_FORMAT)).thenReturn(false);

                byte[] result = serializer.serialize(source, messageContext);

                assertNotNull(result);
            }
        }
    }

    @Nested
    @DisplayName("writeField() method tests")
    class WriteFieldTests {

        @Test
        @DisplayName("Should write field with empty children list")
        void testWriteFieldWithEmptyChildren() throws Exception {
            TestMessageObject source = new TestMessageObject();
            List<IMessageMetadata> emptyChildren = new ArrayList<>();

            ObjectNode result = serializer.writeField(source, emptyChildren, messageContext);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should write field with null children list")
        void testWriteFieldWithNullChildren() throws Exception {
            TestMessageObject source = new TestMessageObject();
            List<IMessageMetadata> nullChildren = null;

            ObjectNode result = serializer.writeField(source, nullChildren, messageContext);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("writePrimitiveField() tests")
    class WritePrimitiveFieldTests {

        @Test
        @DisplayName("Should throw exception for unsupported type")
        void testWritePrimitiveFieldWithUnsupportedType() {
            TestMessageObject source = new TestMessageObject();
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> {
                serializer.writePrimitiveField(source, parentNode, fieldMetadata, messageContext);
            });
        }
    }

    @Nested
    @DisplayName("writeRepeatedField() tests")
    class WriteRepeatedFieldTests {

        @Test
        @DisplayName("Should write empty list as empty array")
        void testWriteRepeatedFieldWithEmptyList() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setItems(new ArrayList<>());
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getFieldName()).thenReturn("items");
            when(fieldMetadata.getId()).thenReturn("items");
            when(fieldMetadata.getRepeatedGenericType()).thenReturn((Class) String.class);
            when(fieldMetadata.getChildren()).thenReturn(new ArrayList<>());

            serializer.writeRepeatedField(source, parentNode, fieldMetadata, messageContext);

            assertTrue(parentNode.has("items"));
            assertTrue(parentNode.get("items").isArray());
            assertEquals(0, parentNode.get("items").size());
        }

        @Test
        @DisplayName("Should write null list as empty array")
        void testWriteRepeatedFieldWithNullList() throws Exception {
            TestMessageObject source = new TestMessageObject();
            source.setItems(null);
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getFieldName()).thenReturn("items");
            when(fieldMetadata.getId()).thenReturn("items");
            when(fieldMetadata.getRepeatedGenericType()).thenReturn((Class) String.class);
            when(fieldMetadata.getChildren()).thenReturn(new ArrayList<>());

            serializer.writeRepeatedField(source, parentNode, fieldMetadata, messageContext);

            assertTrue(parentNode.has("items"));
            assertTrue(parentNode.get("items").isArray());
        }

        @Test
        @DisplayName("Should throw exception for unsupported primitive type in list")
        void testWriteRepeatedFieldWithUnsupportedType() {
            TestMessageObject source = new TestMessageObject();
            List<String> items = new ArrayList<>();
            items.add("item1");
            source.setItems(items);
            ObjectNode parentNode = objectMapper.createObjectNode();

            IMessageFieldMetadata childMetadata = mock(IMessageFieldMetadata.class);
            List<IMessageMetadata> children = new ArrayList<>();
            children.add(childMetadata);

            when(fieldMetadata.getFieldName()).thenReturn("items");
            when(fieldMetadata.getId()).thenReturn("items");
            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(fieldMetadata.getRepeatedGenericType()).thenReturn((Class) String.class);
            when(fieldMetadata.getChildren()).thenReturn(children);
            when(childMetadata.getType()).thenReturn(MessageType.STRING);
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> {
                serializer.writeRepeatedField(source, parentNode, fieldMetadata, messageContext);
            });
        }
    }

    @Nested
    @DisplayName("resolveOutputKey() tests")
    class ResolveOutputKeyTests {

        @Test
        @DisplayName("Should return id when id is not blank")
        void testResolveOutputKeyWithId() {
            when(fieldMetadata.getId()).thenReturn("fieldId");
            when(fieldMetadata.getName()).thenReturn("fieldName");

            String result = serializer.resolveOutputKey(fieldMetadata);

            assertEquals("fieldId", result);
        }

        @Test
        @DisplayName("Should return name when id is null")
        void testResolveOutputKeyWithNullId() {
            when(fieldMetadata.getId()).thenReturn(null);
            when(fieldMetadata.getName()).thenReturn("fieldName");

            String result = serializer.resolveOutputKey(fieldMetadata);

            assertEquals("fieldName", result);
        }

        @Test
        @DisplayName("Should return name when id is blank")
        void testResolveOutputKeyWithBlankId() {
            when(fieldMetadata.getId()).thenReturn("   ");
            when(fieldMetadata.getName()).thenReturn("fieldName");

            String result = serializer.resolveOutputKey(fieldMetadata);

            assertEquals("fieldName", result);
        }

        @Test
        @DisplayName("Should return name when id is empty")
        void testResolveOutputKeyWithEmptyId() {
            when(fieldMetadata.getId()).thenReturn("");
            when(fieldMetadata.getName()).thenReturn("fieldName");

            String result = serializer.resolveOutputKey(fieldMetadata);

            assertEquals("fieldName", result);
        }
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create serializer with ObjectMapper")
        void testConstructorWithObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            JacksonMessageSerializer newSerializer = new JacksonMessageSerializer(mapper);

            assertNotNull(newSerializer);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should serialize empty object to empty JSON")
        void testSerializeEmptyObject() throws Exception {
            TestMessageObject source = new TestMessageObject();

            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(serializationOptions.enabled(MessageFormatOption.PRETTY_FORMAT)).thenReturn(false);

            byte[] result = serializer.serialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
            String jsonStr = new String(result, StandardCharsets.UTF_8);
            assertEquals("{}", jsonStr);
        }

        @Test
        @DisplayName("Should serialize with pretty format")
        void testSerializeWithPrettyFormatIntegration() throws Exception {
            TestMessageObject source = new TestMessageObject();

            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());
            when(messageContext.getSerializationOptions()).thenReturn(serializationOptions);
            when(serializationOptions.enabled(MessageFormatOption.PRETTY_FORMAT)).thenReturn(true);

            byte[] result = serializer.serialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
            String jsonStr = new String(result, StandardCharsets.UTF_8);
            assertTrue(jsonStr.contains("{"));
            assertTrue(jsonStr.contains("}"));
        }
    }
}
