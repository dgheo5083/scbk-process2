package com.scbank.process.api.fw.channel.converter.xml;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextHolder;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

/**
 * XmlMessageHttpConverter Test Class
 *
 * Tests for {@link XmlMessageHttpConverter} ensuring 100% Jacoco coverage.
 */
@ExtendWith(MockitoExtension.class)
class XmlMessageHttpConverterTest {

    @Mock
    private XmlMessageMapper xmlMessageMapper;

    @Mock
    private HttpInputMessage inputMessage;

    @Mock
    private HttpOutputMessage outputMessage;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private XmlMapper xmlMapper;

    @Mock
    private ObjectWriter objectWriter;

    @Mock
    private IIntegrationMessageMetadataRegistrar metadataRegistrar;

    @Mock
    private IIntegrationMessageMetadata metadata;

    @Mock
    private MessageContext messageContext;

    @Mock
    private ChannelMessageContextCreator messageContextCreator;

    @AfterEach
    void tearDown() {
        MessageContextHolder.clear();
    }

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create converter with custom root node name and XmlMessageMapper")
        void shouldCreateConverterWithCustomRootNodeName() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>("customRoot", xmlMessageMapper);

                assertNotNull(converter);
                assertTrue(converter.getSupportedMediaTypes().contains(MediaType.APPLICATION_XML));
                assertTrue(converter.getSupportedMediaTypes().contains(MediaType.TEXT_XML));
                assertTrue(converter.getSupportedMediaTypes().contains(MediaType.APPLICATION_ATOM_XML));
                assertEquals(3, converter.getSupportedMediaTypes().size());
            }
        }

        @Test
        @DisplayName("Should create converter with default root node name")
        void shouldCreateConverterWithDefaultRootNodeName() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                assertNotNull(converter);
                assertEquals(3, converter.getSupportedMediaTypes().size());
            }
        }

        @Test
        @DisplayName("Should create converter with null root node and mapper")
        void shouldCreateConverterSuccessfullyWithNullRootNode() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(null, xmlMessageMapper);

                assertNotNull(converter);
                assertEquals(3, converter.getSupportedMediaTypes().size());
            }
        }
    }

    @Nested
    @DisplayName("supports tests")
    class SupportsTests {

        @Test
        @DisplayName("Should support IMessageObject class")
        void shouldSupportIMessageObjectClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                boolean result = converter.supports(IMessageObject.class);

                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should support concrete IMessageObject implementation")
        void shouldSupportConcreteImplementation() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                boolean result = converter.supports(TestXmlMessageObject.class);

                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should not support String class")
        void shouldNotSupportStringClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                boolean result = converter.supports(String.class);

                assertFalse(result);
            }
        }

        @Test
        @DisplayName("Should not support Object class")
        void shouldNotSupportObjectClass() {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                boolean result = converter.supports(Object.class);

                assertFalse(result);
            }
        }
    }

    @Nested
    @DisplayName("readInternal tests")
    class ReadInternalTests {

        @Test
        @DisplayName("Should read XML and deserialize to object")
        void shouldReadXmlAndDeserializeToObject() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                String xmlContent = "<message><field>value</field></message>";
                byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);

                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentLength()).thenReturn((long) xmlBytes.length);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(inputMessage.getBody()).thenReturn(new ByteArrayInputStream(xmlBytes));

                TestXmlMessageObject expectedObject = new TestXmlMessageObject();
                expectedObject.setFieldName("testValue");

                when(xmlMessageMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestXmlMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
                assertTrue(result instanceof TestXmlMessageObject);
                assertEquals("testValue", ((TestXmlMessageObject) result).getFieldName());
                verify(xmlMessageMapper).deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any(MessageContext.class));
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotReadableException on deserialization error")
        void shouldThrowExceptionOnDeserializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                String xmlContent = "<message><field>value</field></message>";
                byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);

                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentLength()).thenReturn((long) xmlBytes.length);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(inputMessage.getBody()).thenReturn(new ByteArrayInputStream(xmlBytes));

                when(xmlMessageMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Deserialization error"));

                // When/Then
                HttpMessageNotReadableException exception = assertThrows(
                        HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestXmlMessageObject.class, inputMessage)
                );

                assertTrue(exception.getMessage().contains("XML 데이터 역직렬화 오류"));
                assertNotNull(exception.getCause());
                assertEquals("Deserialization error", exception.getCause().getMessage());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful read")
        void shouldClearMessageContextHolderAfterSuccessfulRead() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                String xmlContent = "<message><field>value</field></message>";
                byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);

                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentLength()).thenReturn((long) xmlBytes.length);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(inputMessage.getBody()).thenReturn(new ByteArrayInputStream(xmlBytes));

                TestXmlMessageObject expectedObject = new TestXmlMessageObject();
                when(xmlMessageMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                converter.readInternal(TestXmlMessageObject.class, inputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception")
        void shouldClearMessageContextHolderOnException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                String xmlContent = "<message><field>value</field></message>";
                byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);

                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentLength()).thenReturn((long) xmlBytes.length);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(inputMessage.getBody()).thenReturn(new ByteArrayInputStream(xmlBytes));

                when(xmlMessageMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any()))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotReadableException.class,
                        () -> converter.readInternal(TestXmlMessageObject.class, inputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should use charset from content type when available")
        void shouldUseCharsetFromContentType() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                String xmlContent = "<message><field>value</field></message>";
                byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.ISO_8859_1);

                MediaType contentTypeWithCharset = new MediaType("application", "xml", StandardCharsets.ISO_8859_1);
                when(inputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentLength()).thenReturn((long) xmlBytes.length);
                when(httpHeaders.getContentType()).thenReturn(contentTypeWithCharset);
                when(inputMessage.getBody()).thenReturn(new ByteArrayInputStream(xmlBytes));

                TestXmlMessageObject expectedObject = new TestXmlMessageObject();
                when(xmlMessageMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any()))
                        .thenReturn(expectedObject);

                // When
                IMessageObject result = converter.readInternal(TestXmlMessageObject.class, inputMessage);

                // Then
                assertNotNull(result);
            }
        }
    }

    @Nested
    @DisplayName("writeInternal tests")
    class WriteInternalTests {

        @Test
        @DisplayName("Should serialize object with IMessageObject child fields to XML")
        void shouldSerializeObjectWithMessageObjectChildFields() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestParentXmlMessageObject parentObject = new TestParentXmlMessageObject();
                TestChildXmlMessageObject childObject = new TestChildXmlMessageObject();
                childObject.setChildName("childValue");
                parentObject.setChild(childObject);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestChildXmlMessageObject.class)).thenReturn(metadata);

                byte[] serializedXml = "<child><childName>childValue</childName></child>".getBytes(StandardCharsets.UTF_8);
                when(xmlMessageMapper.serialize(eq(childObject), eq(metadata), any(MessageContext.class)))
                        .thenReturn(serializedXml);
                when(xmlMapper.readTree(serializedXml)).thenReturn(com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode());

                byte[] responseBytes = "<message><child><childName>childValue</childName></child></message>".getBytes(StandardCharsets.UTF_8);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then
                verify(xmlMessageMapper).serialize(eq(childObject), eq(metadata), any(MessageContext.class));
                verify(httpHeaders).setContentLength(responseBytes.length);
            }
        }

        @Test
        @DisplayName("Should skip non-IMessageObject fields")
        void shouldSkipNonMessageObjectFields() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestParentXmlMessageObject parentObject = new TestParentXmlMessageObject();
                parentObject.setSimpleField("simpleValue");
                // child is null, so it won't be processed

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);

                byte[] responseBytes = "<message></message>".getBytes(StandardCharsets.UTF_8);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - serialize should not be called for non-IMessageObject fields
                verify(xmlMessageMapper, never()).serialize(any(IMessageObject.class), any(), any());
            }
        }

        @Test
        @DisplayName("Should skip fields with null metadata")
        void shouldSkipFieldsWithNullMetadata() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestParentXmlMessageObject parentObject = new TestParentXmlMessageObject();
                TestChildXmlMessageObject childObject = new TestChildXmlMessageObject();
                parentObject.setChild(childObject);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestChildXmlMessageObject.class)).thenReturn(null);

                byte[] responseBytes = "<message></message>".getBytes(StandardCharsets.UTF_8);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(parentObject, outputMessage);

                // Then - serialize should not be called when metadata is null
                verify(xmlMessageMapper, never()).serialize(any(IMessageObject.class), any(), any());
            }
        }

        @Test
        @DisplayName("Should throw HttpMessageNotWritableException on serialization error")
        void shouldThrowExceptionOnSerializationError() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestParentXmlMessageObject parentObject = new TestParentXmlMessageObject();
                TestChildXmlMessageObject childObject = new TestChildXmlMessageObject();
                parentObject.setChild(childObject);

                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestChildXmlMessageObject.class)).thenReturn(metadata);

                when(xmlMessageMapper.serialize(any(IMessageObject.class), eq(metadata), any(MessageContext.class)))
                        .thenThrow(new RuntimeException("Serialization error"));

                // When/Then
                HttpMessageNotWritableException exception = assertThrows(
                        HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage)
                );

                assertTrue(exception.getMessage().contains("XML 데이터 역직렬화 오류"));
                assertNotNull(exception.getCause());
                assertEquals("Serialization error", exception.getCause().getMessage());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder after successful write")
        void shouldClearMessageContextHolderAfterSuccessfulWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestXmlMessageObject messageObject = new TestXmlMessageObject();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);

                byte[] responseBytes = "<message></message>".getBytes(StandardCharsets.UTF_8);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then - MessageContextHolder should be cleared
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should clear MessageContextHolder even on exception during write")
        void shouldClearMessageContextHolderOnWriteException() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestParentXmlMessageObject parentObject = new TestParentXmlMessageObject();
                TestChildXmlMessageObject childObject = new TestChildXmlMessageObject();
                parentObject.setChild(childObject);

                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);
                when(metadataRegistrar.getMetadata(TestChildXmlMessageObject.class)).thenReturn(metadata);

                when(xmlMessageMapper.serialize(any(IMessageObject.class), eq(metadata), any(MessageContext.class)))
                        .thenThrow(new RuntimeException("Error"));

                // When
                assertThrows(HttpMessageNotWritableException.class,
                        () -> converter.writeInternal(parentObject, outputMessage));

                // Then - MessageContextHolder should be cleared even after exception
                assertNull(MessageContextHolder.get());
            }
        }

        @Test
        @DisplayName("Should use custom root node name in XML output")
        void shouldUseCustomRootNodeName() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                String customRootName = "customRoot";
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(customRootName, xmlMessageMapper);

                TestXmlMessageObject messageObject = new TestXmlMessageObject();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(MediaType.APPLICATION_XML);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(customRootName)).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);

                byte[] responseBytes = "<customRoot></customRoot>".getBytes(StandardCharsets.UTF_8);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then
                verify(objectWriter).withRootName(customRootName);
            }
        }

        @Test
        @DisplayName("Should use charset from content type for write")
        void shouldUseCharsetFromContentTypeForWrite() throws Exception {
            try (MockedStatic<RuntimeContext> runtimeContextMock = mockStatic(RuntimeContext.class)) {
                // Given
                runtimeContextMock.when(RuntimeContext::getDefaultEncoding).thenReturn("UTF-8");
                runtimeContextMock.when(() -> RuntimeContext.getBean(ChannelMessageContextCreator.class))
                        .thenReturn(messageContextCreator);
                when(messageContextCreator.create(any(), anyString())).thenReturn(messageContext);

                XmlMessageHttpConverter<IMessageObject> converter =
                        new XmlMessageHttpConverter<>(xmlMessageMapper);

                TestXmlMessageObject messageObject = new TestXmlMessageObject();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                MediaType contentTypeWithCharset = new MediaType("application", "xml", StandardCharsets.ISO_8859_1);
                when(outputMessage.getHeaders()).thenReturn(httpHeaders);
                when(httpHeaders.getContentType()).thenReturn(contentTypeWithCharset);
                when(outputMessage.getBody()).thenReturn(outputStream);

                when(xmlMessageMapper.getXmlMapper()).thenReturn(xmlMapper);
                when(xmlMapper.writer()).thenReturn(objectWriter);
                when(objectWriter.withRootName(anyString())).thenReturn(objectWriter);

                runtimeContextMock.when(() -> RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class))
                        .thenReturn(metadataRegistrar);

                byte[] responseBytes = "<message></message>".getBytes(StandardCharsets.ISO_8859_1);
                when(objectWriter.writeValueAsBytes(any())).thenReturn(responseBytes);

                // When
                converter.writeInternal(messageObject, outputMessage);

                // Then - verify data was written to output stream
                assertArrayEquals(responseBytes, outputStream.toByteArray());
                verify(httpHeaders).setContentLength(responseBytes.length);
            }
        }
    }

    /**
     * Test IMessageObject implementation for XML data
     */
    public static class TestXmlMessageObject implements IMessageObject {
        private String fieldName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    /**
     * Test parent message object with child IMessageObject field
     */
    public static class TestParentXmlMessageObject implements IMessageObject {
        private String simpleField;
        private TestChildXmlMessageObject child;

        public String getSimpleField() {
            return simpleField;
        }

        public void setSimpleField(String simpleField) {
            this.simpleField = simpleField;
        }

        public TestChildXmlMessageObject getChild() {
            return child;
        }

        public void setChild(TestChildXmlMessageObject child) {
            this.child = child;
        }
    }

    /**
     * Test child message object
     */
    public static class TestChildXmlMessageObject implements IMessageObject {
        private String childName;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }
}
