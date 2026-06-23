package com.scbank.process.api.fw.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.integration.codec.DefaultIntegrationClientCodecRegistry;
import com.scbank.process.api.fw.integration.codec.FixedLengthIntegrationClientCodec;
import com.scbank.process.api.fw.integration.codec.IntegrationClientCodecRegistry;
import com.scbank.process.api.fw.integration.codec.JsonIntegrationClientCodec;
import com.scbank.process.api.fw.integration.codec.XmlIntegrationClientCodec;
import com.scbank.process.api.fw.integration.interceptor.DefaultIntegrationInterceptorRegistry;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptorRegistry;
import com.scbank.process.api.fw.integration.support.IntegrationMessageContextCreator;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;

/**
 * IntegrationConfiguration Test Class
 */
@ExtendWith(MockitoExtension.class)
class IntegrationConfigurationTest {

    private IntegrationConfiguration configuration;

    @Mock
    private XmlMapper mockXmlMapper;

    @Mock
    private FixedLengthIntegrationClientCodec mockFixedLengthCodec;

    @Mock
    private XmlIntegrationClientCodec mockXmlCodec;
    
    @Mock
    private JsonIntegrationClientCodec mockJsonCodec;

    @Mock
    private IntegrationInterceptor mockInterceptor;

    @Mock
    private MessageContextFactory mockMessageContextFactory;

    @BeforeEach
    void setUp() {
        configuration = new IntegrationConfiguration();
    }

    @Nested
    @DisplayName("fixedlengthIntegrationClientCodec tests")
    class FixedLengthCodecTests {

        @Test
        @DisplayName("Should create FixedLengthIntegrationClientCodec bean")
        void shouldCreateFixedLengthCodecBean() {
            FixedLengthIntegrationClientCodec codec = configuration.fixedlengthIntegrationClientCodec();

            assertNotNull(codec);
            assertTrue(codec instanceof FixedLengthIntegrationClientCodec);
        }
    }

    @Nested
    @DisplayName("xmlIntegrationClientCodec tests")
    class XmlCodecTests {

        @Test
        @DisplayName("Should create XmlIntegrationClientCodec bean")
        void shouldCreateXmlCodecBean() {
            XmlIntegrationClientCodec codec = configuration.xmlIntegrationClientCodec(mockXmlMapper);

            assertNotNull(codec);
            assertTrue(codec instanceof XmlIntegrationClientCodec);
        }
    }

    @Nested
    @DisplayName("integrationClientCodecRegistry tests")
    class CodecRegistryTests {

        @Test
        @DisplayName("Should create registry with codecs registered")
        void shouldCreateRegistryWithCodecsRegistered() {
            IntegrationClientCodecRegistry registry = configuration.integrationClientCodecRegistry(
                    mockFixedLengthCodec, mockJsonCodec, mockXmlCodec);

            assertNotNull(registry);
            assertTrue(registry instanceof DefaultIntegrationClientCodecRegistry);
        }

        @Test
        @DisplayName("Should register FIXEDLENGTH codec")
        void shouldRegisterFixedLengthCodec() {
            IntegrationClientCodecRegistry registry = configuration.integrationClientCodecRegistry(
                    mockFixedLengthCodec, mockJsonCodec, mockXmlCodec);

            assertEquals(mockFixedLengthCodec, registry.getCodec(MessageFormat.FIXEDLENGTH));
        }

        @Test
        @DisplayName("Should register XML codec")
        void shouldRegisterXmlCodec() {
            IntegrationClientCodecRegistry registry = configuration.integrationClientCodecRegistry(
                    mockFixedLengthCodec, mockJsonCodec, mockXmlCodec);

            assertEquals(mockXmlCodec, registry.getCodec(MessageFormat.XML));
        }
        
        @Test
        @DisplayName("Should register Json codec")
        void shouldRegisterJsonCodec() {
            IntegrationClientCodecRegistry registry = configuration.integrationClientCodecRegistry(
                    mockFixedLengthCodec, mockJsonCodec, mockXmlCodec);

            assertEquals(mockJsonCodec, registry.getCodec(MessageFormat.JSON));
        }
    }

    @Nested
    @DisplayName("integrationInterceptorRegistry tests")
    class InterceptorRegistryTests {

        @Test
        @DisplayName("Should create interceptor registry with empty map")
        void shouldCreateInterceptorRegistryWithEmptyMap() {
            Map<String, IntegrationInterceptor> interceptors = new HashMap<>();

            IntegrationInterceptorRegistry registry = configuration.integrationInterceptorRegistry(interceptors);

            assertNotNull(registry);
            assertTrue(registry instanceof DefaultIntegrationInterceptorRegistry);
        }

        @Test
        @DisplayName("Should create interceptor registry with interceptors")
        void shouldCreateInterceptorRegistryWithInterceptors() {
            Map<String, IntegrationInterceptor> interceptors = new HashMap<>();
            interceptors.put("testInterceptor", mockInterceptor);

            IntegrationInterceptorRegistry registry = configuration.integrationInterceptorRegistry(interceptors);

            assertNotNull(registry);
        }
    }

    @Nested
    @DisplayName("integrationMessageContextCreator tests")
    class MessageContextCreatorTests {

        @Test
        @DisplayName("Should create message context creator bean")
        void shouldCreateMessageContextCreatorBean() {
            IntegrationMessageContextCreator creator = configuration.integrationMessageContextCreator(
                    mockMessageContextFactory);

            assertNotNull(creator);
            assertTrue(creator instanceof IntegrationMessageContextCreator);
        }
    }
}
