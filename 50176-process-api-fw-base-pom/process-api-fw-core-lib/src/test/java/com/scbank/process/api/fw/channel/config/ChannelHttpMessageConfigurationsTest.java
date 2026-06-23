package com.scbank.process.api.fw.channel.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.converter.fixedlength.FixedLengthMessageHttpConverter;
import com.scbank.process.api.fw.channel.converter.form.FormMessageHttpConverter;
import com.scbank.process.api.fw.channel.converter.json.JacksonMessageHttpConverter;
import com.scbank.process.api.fw.channel.converter.multipart.MultipartMessageHttpConverter;
import com.scbank.process.api.fw.channel.converter.xml.XmlMessageHttpConverter;
import com.scbank.process.api.fw.channel.response.IResponseRenderer;
import com.scbank.process.api.fw.channel.response.ResponseRendererComposite;
import com.scbank.process.api.fw.channel.response.impl.FileDownloadResponseRenderer;
import com.scbank.process.api.fw.channel.response.impl.GenericResponseRenderer;
import com.scbank.process.api.fw.channel.support.ChannelMessageContextCreator;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;
import com.scbank.process.api.fw.message.mapper.form.FormMessageMapper;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.mapper.multipart.MultipartMessageMapper;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;

/**
 * ChannelHttpMessageConfigurations Test Class
 * Comprehensive tests for 100% JaCoCo coverage
 */
@ExtendWith(MockitoExtension.class)
class ChannelHttpMessageConfigurationsTest {

    private ChannelHttpMessageConfigurations configurations;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private XmlMapper xmlMapper;

    @Mock
    private ChannelProperties channelProperties;

    @Mock
    private MessageContextFactory messageContextFactory;

    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() {
        configurations = new ChannelHttpMessageConfigurations();
        // Initialize RuntimeContext with mock Environment using lenient mocking
        lenient().when(environment.getProperty(anyString())).thenReturn("UTF-8");
        lenient().when(environment.getProperty(anyString(), anyString())).thenReturn("UTF-8");
        RuntimeContext.setEnvironment(environment);
    }

    @AfterEach
    void tearDown() {
        // Clean up RuntimeContext
        RuntimeContext.setEnvironment(null);
    }

    @Nested
    @DisplayName("Message Mapper Bean tests")
    class MessageMapperTests {

        @Test
        @DisplayName("Should create JacksonMessageMapper")
        void shouldCreateJacksonMessageMapper() {
            JacksonMessageMapper mapper = configurations.jacksonHttpMessageMapper(objectMapper);

            assertNotNull(mapper);
        }

        @Test
        @DisplayName("Should create XmlMessageMapper")
        void shouldCreateXmlMessageMapper() {
            XmlMessageMapper mapper = configurations.xmlhttpMessageMapper(xmlMapper);

            assertNotNull(mapper);
        }

        @Test
        @DisplayName("Should create FixedLengthMessageMapper")
        void shouldCreateFixedLengthMessageMapper() {
            FixedLengthMessageMapper mapper = configurations.fixedLengthHttpMessageMapper();

            assertNotNull(mapper);
        }

        @Test
        @DisplayName("Should create FormMessageMapper")
        void shouldCreateFormMessageMapper() {
            FormMessageMapper mapper = configurations.formMessageMapper();

            assertNotNull(mapper);
        }

        @Test
        @DisplayName("Should create MultipartMessageMapper")
        void shouldCreateMultipartMessageMapper() {
            MultipartMessageMapper mapper = configurations.multipartMessageMapper();

            assertNotNull(mapper);
        }
    }

    @Nested
    @DisplayName("ChannelMessageContextCreator tests")
    class ChannelMessageContextCreatorTests {

        @Test
        @DisplayName("Should create ChannelMessageContextCreator")
        void shouldCreateChannelMessageContextCreator() {
            ChannelMessageContextCreator creator = configurations.channelMessageContextCreator(
                    channelProperties, messageContextFactory);

            assertNotNull(creator);
        }
    }

    @Nested
    @DisplayName("HttpMessageConverter Bean tests")
    class HttpMessageConverterTests {

        @Test
        @DisplayName("Should create JacksonMessageHttpConverter")
        void shouldCreateJacksonMessageHttpConverter() {
            JacksonMessageMapper mapper = configurations.jacksonHttpMessageMapper(objectMapper);

            HttpMessageConverter<?> converter = configurations.jacksonMessageHttpConverter(mapper, null);

            assertNotNull(converter);
            assertTrue(converter instanceof JacksonMessageHttpConverter);
        }

        @Test
        @DisplayName("Should create XmlMessageHttpConverter")
        void shouldCreateXmlMessageHttpConverter() {
            XmlMessageMapper mapper = configurations.xmlhttpMessageMapper(xmlMapper);

            XmlMessageHttpConverter<?> converter = configurations.xmlMessageHttpConverter(mapper);

            assertNotNull(converter);
        }

        @Test
        @DisplayName("Should create FixedLengthMessageHttpConverter")
        void shouldCreateFixedLengthMessageHttpConverter() {
            FixedLengthMessageMapper mapper = configurations.fixedLengthHttpMessageMapper();

            HttpMessageConverter<?> converter = configurations.fixedlengthMessageHttpConverter(mapper);

            assertNotNull(converter);
            assertTrue(converter instanceof FixedLengthMessageHttpConverter);
        }

        @Test
        @DisplayName("Should create FormMessageHttpConverter")
        void shouldCreateFormMessageHttpConverter() {
            FormMessageMapper mapper = configurations.formMessageMapper();

            HttpMessageConverter<?> converter = configurations.formMessageHttpConverter(mapper);

            assertNotNull(converter);
            assertTrue(converter instanceof FormMessageHttpConverter);
        }

        @Test
        @DisplayName("Should create MultipartMessageHttpConverter")
        void shouldCreateMultipartMessageHttpConverter() {
            MultipartMessageMapper mapper = configurations.multipartMessageMapper();

            HttpMessageConverter<?> converter = configurations.multipartMessageHttpConverter(mapper);

            assertNotNull(converter);
            assertTrue(converter instanceof MultipartMessageHttpConverter);
        }
    }

    @Nested
    @DisplayName("HttpMessageConverterComposite tests")
    class HttpMessageConverterCompositeTests {

        @Test
        @DisplayName("Should create HttpMessageConverterComposite with all converters")
        @SuppressWarnings("unchecked")
        void shouldCreateHttpMessageConverterComposite() {
            JacksonMessageMapper jacksonMapper = configurations.jacksonHttpMessageMapper(objectMapper);
            XmlMessageMapper xmlMessageMapper = configurations.xmlhttpMessageMapper(xmlMapper);
            FixedLengthMessageMapper fixedLengthMapper = configurations.fixedLengthHttpMessageMapper();
            FormMessageMapper formMapper = configurations.formMessageMapper();
            MultipartMessageMapper multipartMapper = configurations.multipartMessageMapper();

            JacksonMessageHttpConverter jacksonConverter = new JacksonMessageHttpConverter<>(jacksonMapper);
            XmlMessageHttpConverter xmlConverter = new XmlMessageHttpConverter<>(xmlMessageMapper);
            FixedLengthMessageHttpConverter fixedLengthConverter = new FixedLengthMessageHttpConverter<>(fixedLengthMapper);
            FormMessageHttpConverter formConverter = new FormMessageHttpConverter<>(formMapper);
            MultipartMessageHttpConverter multipartConverter = new MultipartMessageHttpConverter<>(multipartMapper);

            HttpMessageConverterComposite<?> composite = configurations.httpMessageConverterComposite(
                    jacksonConverter, xmlConverter, fixedLengthConverter, formConverter, multipartConverter);

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("Response Renderer Bean tests")
    class ResponseRendererTests {

        @Test
        @DisplayName("Should create GenericResponseRenderer")
        void shouldCreateGenericResponseRenderer() {
            GenericResponseRenderer renderer = configurations.genericResponseRenderer();

            assertNotNull(renderer);
        }

        @Test
        @DisplayName("Should create FileDownloadResponseRenderer")
        void shouldCreateFileDownloadResponseRenderer() {
            FileDownloadResponseRenderer renderer = configurations.fileDownloadResponseRenderer();

            assertNotNull(renderer);
        }

        @Test
        @DisplayName("Should create ResponseRendererComposite")
        void shouldCreateResponseRendererComposite() {
            List<IResponseRenderer<?, ?>> renderers = List.of(
                    configurations.genericResponseRenderer(),
                    configurations.fileDownloadResponseRenderer());

            ResponseRendererComposite composite = configurations.responseRendererComposite(renderers);

            assertNotNull(composite);
        }

        @Test
        @DisplayName("Should create ResponseRendererComposite with empty list")
        void shouldCreateResponseRendererCompositeWithEmptyList() {
            ResponseRendererComposite composite = configurations.responseRendererComposite(List.of());

            assertNotNull(composite);
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should create all beans successfully")
        void shouldCreateAllBeansSuccessfully() {
            // Message Mappers
            JacksonMessageMapper jacksonMapper = configurations.jacksonHttpMessageMapper(objectMapper);
            XmlMessageMapper xmlMessageMapper = configurations.xmlhttpMessageMapper(xmlMapper);
            FixedLengthMessageMapper fixedLengthMapper = configurations.fixedLengthHttpMessageMapper();
            FormMessageMapper formMapper = configurations.formMessageMapper();
            MultipartMessageMapper multipartMapper = configurations.multipartMessageMapper();

            assertNotNull(jacksonMapper);
            assertNotNull(xmlMessageMapper);
            assertNotNull(fixedLengthMapper);
            assertNotNull(formMapper);
            assertNotNull(multipartMapper);

            // Response Renderers
            GenericResponseRenderer genericRenderer = configurations.genericResponseRenderer();
            FileDownloadResponseRenderer fileRenderer = configurations.fileDownloadResponseRenderer();

            assertNotNull(genericRenderer);
            assertNotNull(fileRenderer);
        }
    }
}
