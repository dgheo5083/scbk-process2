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
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;

/**
 * FixedLengthIntegrationClientCodec Test Class
 */
@ExtendWith(MockitoExtension.class)
class FixedLengthIntegrationClientCodecTest {

    @Mock
    private FixedLengthMessageMapper mockMapper;

    @Mock
    private MessageContext mockContext;

    @Mock
    private IMessageObject mockRequest;

    private FixedLengthIntegrationClientCodec codec;

    @BeforeEach
    void setUp() {
        codec = new FixedLengthIntegrationClientCodec(mockMapper);
    }

    @Nested
    @DisplayName("encode tests")
    class EncodeTests {

        @Test
        @DisplayName("Should encode request to byte array")
        void shouldEncodeRequestToByteArray() throws Exception {
            byte[] expectedBytes = "test data".getBytes();
            when(mockMapper.serialize(mockRequest, mockContext)).thenReturn(expectedBytes);

            byte[] result = codec.encode(mockContext, mockRequest);

            assertArrayEquals(expectedBytes, result);
            verify(mockMapper).serialize(mockRequest, mockContext);
        }

        @Test
        @DisplayName("Should delegate to mapper for encoding")
        void shouldDelegateToMapperForEncoding() throws Exception {
            byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
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
                    .thenThrow(new RuntimeException("Serialization failed"));

            assertThrows(RuntimeException.class, () -> codec.encode(mockContext, mockRequest));
        }
    }

    @Nested
    @DisplayName("decode tests")
    class DecodeTests {

        @Test
        @DisplayName("Should decode byte array to response")
        void shouldDecodeByteArrayToResponse() throws Exception {
            byte[] responseBytes = "response data".getBytes();
            when(mockMapper.deserialize(eq(responseBytes), eq(TestMessageObject.class), eq(mockContext)))
                    .thenReturn(new TestMessageObject());

            TestMessageObject result = codec.decode(mockContext, responseBytes, TestMessageObject.class);

            assertNotNull(result);
            verify(mockMapper).deserialize(responseBytes, TestMessageObject.class, mockContext);
        }

        @Test
        @DisplayName("Should delegate to mapper for decoding")
        void shouldDelegateToMapperForDecoding() throws Exception {
            byte[] responseBytes = new byte[]{5, 4, 3, 2, 1};
            TestMessageObject expected = new TestMessageObject();
            when(mockMapper.deserialize(any(byte[].class), eq(TestMessageObject.class), any(MessageContext.class)))
                    .thenReturn(expected);

            TestMessageObject result = codec.decode(mockContext, responseBytes, TestMessageObject.class);

            assertSame(expected, result);
        }

        @Test
        @DisplayName("Should throw exception when mapper fails during decode")
        void shouldThrowExceptionWhenMapperFailsDuringDecode() throws Exception {
            byte[] responseBytes = new byte[]{1, 2, 3};
            when(mockMapper.deserialize(any(byte[].class), any(Class.class), any(MessageContext.class)))
                    .thenThrow(new RuntimeException("Deserialization failed"));

            assertThrows(RuntimeException.class, () ->
                    codec.decode(mockContext, responseBytes, TestMessageObject.class));
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
    static class TestMessageObject implements IMessageObject {
    }
}
