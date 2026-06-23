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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("JacksonMessageDeserializer Tests")
class JacksonMessageDeserializerTest {

    private JacksonMessageDeserializer deserializer;
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

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        deserializer = new JacksonMessageDeserializer(objectMapper);
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
    @DisplayName("deserialize() method tests")
    class DeserializeTests {

        @Test
        @DisplayName("Should deserialize JSON bytes to object with metadata")
        void testDeserializeWithMetadata() throws Exception {
            String json = "{\"name\":\"test\",\"age\":25}";
            byte[] source = json.getBytes(StandardCharsets.UTF_8);

            when(integrationMessageMetadata.getTargetClass()).thenReturn((Class) TestMessageObject.class);
            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());

            TestMessageObject result = deserializer.deserialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should deserialize JSON bytes using target type")
        void testDeserializeWithTargetType() throws Exception {
            String json = "{\"name\":\"test\"}";
            byte[] source = json.getBytes(StandardCharsets.UTF_8);

            try (MockedStatic<RuntimeContext> mockedRuntime = mockStatic(RuntimeContext.class)) {
                mockedRuntime.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestMessageObject.class)).thenReturn(integrationMessageMetadata);
                when(integrationMessageMetadata.getTargetClass()).thenReturn((Class) TestMessageObject.class);
                when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());

                TestMessageObject result = deserializer.deserialize(source, TestMessageObject.class, messageContext);

                assertNotNull(result);
            }
        }
    }

    @Nested
    @DisplayName("asArrayNode() method tests")
    class AsArrayNodeTests {

        @Test
        @DisplayName("Should return empty array for null node")
        void testAsArrayNodeWithNull() {
            ArrayNode result = deserializer.asArrayNode(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return empty array for null JSON node")
        void testAsArrayNodeWithNullJsonNode() {
            JsonNode nullNode = objectMapper.nullNode();

            ArrayNode result = deserializer.asArrayNode(nullNode);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return same node for array node")
        void testAsArrayNodeWithArrayNode() {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            arrayNode.add("item1");
            arrayNode.add("item2");

            ArrayNode result = deserializer.asArrayNode(arrayNode);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertSame(arrayNode, result);
        }

        @Test
        @DisplayName("Should wrap object node in array")
        void testAsArrayNodeWithObjectNode() {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("key", "value");

            ArrayNode result = deserializer.asArrayNode(objectNode);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("value", result.get(0).get("key").asText());
        }

        @Test
        @DisplayName("Should wrap text node in array")
        void testAsArrayNodeWithTextNode() {
            JsonNode textNode = objectMapper.valueToTree("text");

            ArrayNode result = deserializer.asArrayNode(textNode);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("text", result.get(0).asText());
        }
    }

    @Nested
    @DisplayName("readPrimitiveTypeFromNode() tests")
    class ReadPrimitiveTypeFromNodeTests {

        @Test
        @DisplayName("Should handle null parent node")
        void testReadPrimitiveTypeFromNodeWithNullParent() {
            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            // Should not throw exception for null parent - returns early
            assertDoesNotThrow(() -> {
                deserializer.readPrimitiveTypeFromNode(null, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }

        @Test
        @DisplayName("Should handle missing value node")
        void testReadPrimitiveTypeFromNodeWithMissingValue() {
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(fieldMetadata.getId()).thenReturn("nonexistent");
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(mock());

            // Should not throw exception for missing value - returns early
            assertDoesNotThrow(() -> {
                deserializer.readPrimitiveTypeFromNode(parentNode, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }

        @Test
        @DisplayName("Should throw exception for unsupported type")
        void testReadPrimitiveTypeFromNodeWithUnsupportedType() {
            ObjectNode parentNode = objectMapper.createObjectNode();
            parentNode.put("testField", "value");

            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(fieldMetadata.getId()).thenReturn("testField");
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            assertThrows(Exception.class, () -> {
                deserializer.readPrimitiveTypeFromNode(parentNode, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }
    }

    @Nested
    @DisplayName("readPrimitiveTypeWithIndexFromNode() tests")
    class ReadPrimitiveTypeWithIndexFromNodeTests {

        @Test
        @DisplayName("Should handle null parent node")
        void testReadPrimitiveTypeWithIndexFromNodeWithNullParent() throws Exception {
            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(mock());

            // Should not throw exception for null parent - returns early
            assertDoesNotThrow(() -> {
                deserializer.readPrimitiveTypeWithIndexFromNode(null, fieldMetadata, new TestMessageObject(), 0, messageContext);
            });
        }

        @Test
        @DisplayName("Should handle missing value node")
        void testReadPrimitiveTypeWithIndexFromNodeWithMissingValue() throws Exception {
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(fieldMetadata.getId()).thenReturn("nonexistent");
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(mock());

            // Should not throw exception for missing value - returns early
            assertDoesNotThrow(() -> {
                deserializer.readPrimitiveTypeWithIndexFromNode(parentNode, fieldMetadata, new TestMessageObject(), 0, messageContext);
            });
        }

        @Test
        @DisplayName("Should throw exception for null converter")
        void testReadPrimitiveTypeWithIndexFromNodeWithNullConverter() {
            ObjectNode parentNode = objectMapper.createObjectNode();
            parentNode.put("testField", "value");

            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(null);

            assertThrows(IllegalStateException.class, () -> {
                deserializer.readPrimitiveTypeWithIndexFromNode(parentNode, fieldMetadata, new TestMessageObject(), 0, messageContext);
            });
        }
    }

    @Nested
    @DisplayName("readObjectFromJsonNode() tests")
    class ReadObjectFromJsonNodeTests {

        @Test
        @DisplayName("Should handle null parent node")
        void testReadObjectFromJsonNodeWithNullParent() throws Exception {
            // Should not throw exception for null parent - returns early
            assertDoesNotThrow(() -> {
                deserializer.readObjectFromJsonNode(null, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }
    }

    @Nested
    @DisplayName("readRepeatedFieldFromJsonNode() tests")
    class ReadRepeatedFieldFromJsonNodeTests {

        @Test
        @DisplayName("Should handle null parent node")
        void testReadRepeatedFieldFromJsonNodeWithNullParent() throws Exception {
            // Should not throw exception for null parent - returns early
            assertDoesNotThrow(() -> {
                deserializer.readRepeatedFieldFromJsonNode(null, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }

        @Test
        @DisplayName("Should handle missing node")
        void testReadRepeatedFieldFromJsonNodeWithMissingNode() throws Exception {
            ObjectNode parentNode = objectMapper.createObjectNode();

            when(fieldMetadata.getId()).thenReturn("nonexistent");

            // Should not throw exception for missing node - returns early
            assertDoesNotThrow(() -> {
                deserializer.readRepeatedFieldFromJsonNode(parentNode, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }

        @Test
        @DisplayName("Should handle non-array node")
        void testReadRepeatedFieldFromJsonNodeWithNonArrayNode() throws Exception {
            ObjectNode parentNode = objectMapper.createObjectNode();
            parentNode.put("items", "not-an-array");

            when(fieldMetadata.getId()).thenReturn("items");

            // Should not throw exception for non-array node - returns early
            assertDoesNotThrow(() -> {
                deserializer.readRepeatedFieldFromJsonNode(parentNode, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }

        @Test
        @DisplayName("Should handle empty children list")
        void testReadRepeatedFieldFromJsonNodeWithEmptyChildren() throws Exception {
            ObjectNode parentNode = objectMapper.createObjectNode();
            ArrayNode arrayNode = objectMapper.createArrayNode();
            arrayNode.add("item1");
            parentNode.set("items", arrayNode);

            when(fieldMetadata.getId()).thenReturn("items");
            when(fieldMetadata.getChildren()).thenReturn(new ArrayList<>());

            // Should not throw exception for empty children - returns early
            assertDoesNotThrow(() -> {
                deserializer.readRepeatedFieldFromJsonNode(parentNode, fieldMetadata, new TestMessageObject(), messageContext);
            });
        }
    }

    @Nested
    @DisplayName("constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create deserializer with ObjectMapper")
        void testConstructorWithObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            JacksonMessageDeserializer newDeserializer = new JacksonMessageDeserializer(mapper);

            assertNotNull(newDeserializer);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should deserialize empty JSON object")
        void testDeserializeEmptyObject() throws Exception {
            String json = "{}";
            byte[] source = json.getBytes(StandardCharsets.UTF_8);

            when(integrationMessageMetadata.getTargetClass()).thenReturn((Class) TestMessageObject.class);
            when(integrationMessageMetadata.getChildren()).thenReturn(new ArrayList<>());

            TestMessageObject result = deserializer.deserialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
            assertNull(result.getName());
            assertNull(result.getAge());
        }

        @Test
        @DisplayName("Should deserialize with children metadata")
        void testDeserializeWithChildrenMetadata() throws Exception {
            String json = "{\"name\":\"test\"}";
            byte[] source = json.getBytes(StandardCharsets.UTF_8);

            List<IMessageMetadata> children = new ArrayList<>();
            children.add(fieldMetadata);

            when(integrationMessageMetadata.getTargetClass()).thenReturn((Class) TestMessageObject.class);
            when(integrationMessageMetadata.getChildren()).thenReturn(children);
            when(fieldMetadata.getType()).thenReturn(MessageType.STRING);
            when(fieldMetadata.getId()).thenReturn("name");
            when(fieldMetadata.getFieldName()).thenReturn("name");
            when(messageContext.getMessageFieldConverterRegistry()).thenReturn(converterRegistry);
            when(converterRegistry.get(MessageType.STRING)).thenReturn(mock());

            TestMessageObject result = deserializer.deserialize(source, integrationMessageMetadata, messageContext);

            assertNotNull(result);
        }
    }
}
