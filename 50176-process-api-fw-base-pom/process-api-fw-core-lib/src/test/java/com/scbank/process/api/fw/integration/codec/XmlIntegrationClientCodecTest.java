package com.scbank.process.api.fw.integration.codec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;

/**
 * XmlIntegrationClientCodec Test Class
 */
@ExtendWith(MockitoExtension.class)
class XmlIntegrationClientCodecTest {

    @Mock
    private XmlMessageMapper mockMapper;

    @Mock
    private MessageContext mockContext;

    @Mock
    private IMessageObject mockRequest;

    private XmlIntegrationClientCodec codec;

    @BeforeEach
    void setUp() {
        codec = new XmlIntegrationClientCodec(mockMapper);
    }

    @Nested
    @DisplayName("encode tests")
    class EncodeTests {

        @Test
        @DisplayName("Should encode request to byte array")
        void shouldEncodeRequestToByteArray() throws Exception {
            byte[] expectedBytes = "<xml>test</xml>".getBytes();
            when(mockMapper.serialize(mockRequest, mockContext)).thenReturn(expectedBytes);

            byte[] result = codec.encode(mockContext, mockRequest);

            assertArrayEquals(expectedBytes, result);
            verify(mockMapper).serialize(mockRequest, mockContext);
        }

        @Test
        @DisplayName("Should delegate to mapper for XML encoding")
        void shouldDelegateToMapperForXmlEncoding() throws Exception {
            byte[] expectedBytes = "<?xml version=\"1.0\"?>".getBytes();
            when(mockMapper.serialize(any(IMessageObject.class), any(MessageContext.class)))
                    .thenReturn(expectedBytes);

            byte[] result = codec.encode(mockContext, mockRequest);

            assertNotNull(result);
            verify(mockMapper, times(1)).serialize(mockRequest, mockContext);
        }

        @Test
        @DisplayName("Should throw exception when mapper fails")
        void shouldThrowExceptionWhenMapperFails() throws Exception {
            when(mockMapper.serialize(any(IMessageObject.class), any(MessageContext.class)))
                    .thenThrow(new RuntimeException("XML serialization failed"));

            assertThrows(RuntimeException.class, () -> codec.encode(mockContext, mockRequest));
        }
    }

    @Nested
    @DisplayName("decode tests")
    class DecodeTests {

        @Test
        @DisplayName("Should decode byte array to response")
        void shouldDecodeByteArrayToResponse() throws Exception {
            byte[] responseBytes = "<xml>response</xml>".getBytes();
            when(mockMapper.deserialize(eq(responseBytes), eq(TestXmlMessageObject.class), eq(mockContext)))
                    .thenReturn(new TestXmlMessageObject());

            TestXmlMessageObject result = codec.decode(mockContext, responseBytes, TestXmlMessageObject.class);

            assertNotNull(result);
            verify(mockMapper).deserialize(responseBytes, TestXmlMessageObject.class, mockContext);
        }

        @Test
        @DisplayName("Should delegate to mapper for XML decoding")
        void shouldDelegateToMapperForXmlDecoding() throws Exception {
            byte[] responseBytes = "<root><data>value</data></root>".getBytes();
            TestXmlMessageObject expected = new TestXmlMessageObject();
            when(mockMapper.deserialize(any(byte[].class), eq(TestXmlMessageObject.class), any(MessageContext.class)))
                    .thenReturn(expected);

            TestXmlMessageObject result = codec.decode(mockContext, responseBytes, TestXmlMessageObject.class);

            assertSame(expected, result);
        }

        @Test
        @DisplayName("Should throw exception when mapper fails during decode")
        void shouldThrowExceptionWhenMapperFailsDuringDecode() throws Exception {
            byte[] responseBytes = "<invalid>".getBytes();
            when(mockMapper.deserialize(any(byte[].class), any(Class.class), any(MessageContext.class)))
                    .thenThrow(new RuntimeException("XML deserialization failed"));

            assertThrows(RuntimeException.class, () ->
                    codec.decode(mockContext, responseBytes, TestXmlMessageObject.class));
        }
    }

    @Nested
    @DisplayName("interface implementation tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement IntegrationClientCodec")
        void shouldImplementIntegrationClientCodec() {
            assertTrue(codec instanceof IntegrationClientCodec);
        }
    }

    // Test helper class
    static class TestXmlMessageObject implements IMessageObject {
    }
}
