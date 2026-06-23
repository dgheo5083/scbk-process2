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

import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * DefaultIntegrationClientCodecRegistry Test Class
 */
@ExtendWith(MockitoExtension.class)
class DefaultIntegrationClientCodecRegistryTest {

    @Mock
    private IntegrationClientCodec mockJsonCodec;

    @Mock
    private IntegrationClientCodec mockXmlCodec;

    @Mock
    private IntegrationClientCodec mockFixedLengthCodec;

    private DefaultIntegrationClientCodecRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultIntegrationClientCodecRegistry();
    }

    @Nested
    @DisplayName("register tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register codec for format")
        void shouldRegisterCodecForFormat() {
            registry.register(MessageFormat.JSON, mockJsonCodec);

            assertDoesNotThrow(() -> registry.getCodec(MessageFormat.JSON));
        }

        @Test
        @DisplayName("Should register multiple codecs")
        void shouldRegisterMultipleCodecs() {
            registry.register(MessageFormat.JSON, mockJsonCodec);
            registry.register(MessageFormat.XML, mockXmlCodec);
            registry.register(MessageFormat.FIXEDLENGTH, mockFixedLengthCodec);

            assertSame(mockJsonCodec, registry.getCodec(MessageFormat.JSON));
            assertSame(mockXmlCodec, registry.getCodec(MessageFormat.XML));
            assertSame(mockFixedLengthCodec, registry.getCodec(MessageFormat.FIXEDLENGTH));
        }

        @Test
        @DisplayName("Should replace existing codec for same format")
        void shouldReplaceExistingCodecForSameFormat() {
            IntegrationClientCodec anotherJsonCodec = mock(IntegrationClientCodec.class);

            registry.register(MessageFormat.JSON, mockJsonCodec);
            registry.register(MessageFormat.JSON, anotherJsonCodec);

            assertSame(anotherJsonCodec, registry.getCodec(MessageFormat.JSON));
        }
    }

    @Nested
    @DisplayName("getCodec tests")
    class GetCodecTests {

        @Test
        @DisplayName("Should return registered codec")
        void shouldReturnRegisteredCodec() {
            registry.register(MessageFormat.XML, mockXmlCodec);

            IntegrationClientCodec result = registry.getCodec(MessageFormat.XML);

            assertSame(mockXmlCodec, result);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for unregistered format")
        void shouldThrowIllegalArgumentExceptionForUnregisteredFormat() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> registry.getCodec(MessageFormat.JSON));

            assertTrue(exception.getMessage().contains("Codec not found for format"));
            assertTrue(exception.getMessage().contains("JSON"));
        }

        @Test
        @DisplayName("Should throw for different unregistered formats")
        void shouldThrowForDifferentUnregisteredFormats() {
            registry.register(MessageFormat.JSON, mockJsonCodec);

            assertThrows(IllegalArgumentException.class, () -> registry.getCodec(MessageFormat.XML));
            assertThrows(IllegalArgumentException.class, () -> registry.getCodec(MessageFormat.FIXEDLENGTH));
        }
    }

    @Nested
    @DisplayName("EnumMap behavior tests")
    class EnumMapBehaviorTests {

        @Test
        @DisplayName("Should handle all MessageFormat enum values")
        void shouldHandleAllMessageFormatEnumValues() {
            for (MessageFormat format : MessageFormat.values()) {
                IntegrationClientCodec codec = mock(IntegrationClientCodec.class);
                registry.register(format, codec);
                assertSame(codec, registry.getCodec(format));
            }
        }
    }
}
