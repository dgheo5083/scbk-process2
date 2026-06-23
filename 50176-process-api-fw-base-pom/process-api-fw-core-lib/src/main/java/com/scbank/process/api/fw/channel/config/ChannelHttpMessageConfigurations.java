package com.scbank.process.api.fw.channel.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
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
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;
import com.scbank.process.api.fw.message.mapper.form.FormMessageMapper;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.mapper.multipart.MultipartMessageMapper;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageSerializer;
import com.scbank.process.api.fw.message.serializer.form.FormMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.form.FormMessageSerializer;
import com.scbank.process.api.fw.message.serializer.multipart.MultipartMessageDeserializer;

/**
 * <pre>
 * 프레임워크 채널 모듈의 HTTP 메시지 변환기 및 응답 렌더러 구성을 담당하는 설정 클래스입니다.
 *
 * 이 클래스는 'framework.channel.enabled=true' 설정 시 자동 활성화되며,
 * 다양한 메시지 포맷(JSON, XML, FixedLength, Form, Multipart)에 대한 메시지 매퍼,
 * HttpMessageConverter, 응답 렌더러를 빈으로 제공합니다.
 *
 * 각 Bean은 @ConditionalOnMissingBean으로 감싸져 있어, 필요 시 사용자가 커스터마이징할 수 있습니다.
 * </pre>
 *
 * @author
 * @since 2025.04.26
 */
@Configuration
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ChannelHttpMessageConfigurations {

    // ************************************************************************
    // 메시지 포맷별 MessageMapper Bean 등록
    // ************************************************************************

    /**
     * Jackson(JSON) 기반 메시지 매퍼 등록.
     *
     * @return {@link JacksonMessageMapper}
     */
    @Bean("jacksonHttpMessageMapper")
    @ConditionalOnMissingBean(name = "jacksonHttpMessageMapper")
    JacksonMessageMapper jacksonHttpMessageMapper(ObjectMapper objectMapper) {
        return new JacksonMessageMapper(objectMapper);
    }

    /**
     * XML 기반 메시지 매퍼 등록.
     *
     * @return {@link XmlMessageMapper}
     */
    @Bean("xmlhttpMessageMapper")
    @ConditionalOnMissingBean(name = "xmlhttpMessageMapper")
    XmlMessageMapper xmlhttpMessageMapper(XmlMapper xmlMapper) {
        return new XmlMessageMapper(xmlMapper);
    }

    /**
     * 고정길이 포맷 메시지 매퍼 등록.
     *
     * @return {@link FixedLengthMessageMapper}
     */
    @Bean("fixedLengthHttpMessageMapper")
    @ConditionalOnMissingBean(name = "fixedLengthHttpMessageMapper")
    FixedLengthMessageMapper fixedLengthHttpMessageMapper() {
        return new FixedLengthMessageMapper(
                new FixedLengthMessageSerializer(),
                new FixedLengthMessageDeserializer());
    }

    /**
     * Form(application/x-www-form-urlencoded) 포맷 메시지 매퍼 등록.
     *
     * @return {@link FormMessageMapper}
     */
    @Bean(name = "formMessageMapper")
    @ConditionalOnMissingBean(name = "formMessageMapper")
    FormMessageMapper formMessageMapper() {
        return new FormMessageMapper(
                new FormMessageSerializer(),
                new FormMessageDeserializer());
    }

    /**
     * Multipart/form-data 메시지 매퍼 등록.
     *
     * @return {@link MultipartMessageMapper}
     */
    @Bean(name = "multipartMessageMapper")
    @ConditionalOnMissingBean(name = "multipartMessageMapper")
    MultipartMessageMapper multipartMessageMapper() {
        return new MultipartMessageMapper(new MultipartMessageDeserializer());
    }

    /**
     * 채널 메시지 컨텍스트 생성기 등록.
     *
     * @param properties 채널 설정 정보
     * @return {@link ChannelMessageContextCreator}
     */
    @Bean
    @ConditionalOnMissingBean(ChannelMessageContextCreator.class)
    @DependsOn({ "channelProperties" })
    ChannelMessageContextCreator channelMessageContextCreator(ChannelProperties properties,
            MessageContextFactory messageContextFactory) {
        return new ChannelMessageContextCreator(properties, messageContextFactory);
    }

    // ************************************************************************
    // HttpMessageConverter 등록
    // ************************************************************************

    /**
     * Jackson 기반 HttpMessageConverter 등록 (JSON 처리용).
     */
    @Bean
    @ConditionalOnMissingBean(JacksonMessageHttpConverter.class)
    @DependsOn({ "runtimeContextInitializer", "jacksonHttpMessageMapper" })
    HttpMessageConverter<?> jacksonMessageHttpConverter(
            @Qualifier("jacksonHttpMessageMapper") JacksonMessageMapper jacksonHttpMessageMapper,
            @Qualifier("jacksonMessageFieldConverterRegistry") IMessageFieldConverterRegistry jacksonMessageFieldConverterRegistry) {
        return new JacksonMessageHttpConverter<>(jacksonHttpMessageMapper);
    }

    /**
     * XML 기반 HttpMessageConverter 등록.
     */
    @Bean
    @ConditionalOnMissingBean(XmlMessageHttpConverter.class)
    @DependsOn({ "runtimeContextInitializer", "xmlhttpMessageMapper" })
    <T extends IMessageObject> XmlMessageHttpConverter<T> xmlMessageHttpConverter(
            XmlMessageMapper xmlhttpMessageMapper) {
        return new XmlMessageHttpConverter<>(xmlhttpMessageMapper);
    }

    /**
     * FixedLength 기반 HttpMessageConverter 등록.
     */
    @Bean("fixedLengthMessageHttpConverter")
    @ConditionalOnMissingBean(name = "fixedLengthMessageHttpConverter")
    @DependsOn({ "runtimeContextInitializer", "fixedLengthHttpMessageMapper" })
    <T extends IMessageObject> HttpMessageConverter<T> fixedlengthMessageHttpConverter(
            FixedLengthMessageMapper fixedLengthHttpMessageMapper) {
        return new FixedLengthMessageHttpConverter<>(fixedLengthHttpMessageMapper);
    }

    /**
     * Form 기반 HttpMessageConverter 등록.
     */
    @Bean("formMessageHttpConverter")
    @ConditionalOnMissingBean(name = "formMessageHttpConverter")
    @DependsOn({ "runtimeContextInitializer", "formMessageMapper" })
    <T extends IMessageObject> HttpMessageConverter<T> formMessageHttpConverter(
            FormMessageMapper formMessageMapper) {
        return new FormMessageHttpConverter<>(formMessageMapper);
    }

    /**
     * Multipart 기반 HttpMessageConverter 등록.
     */
    @Bean(name = "multipartMessageHttpConverter")
    @ConditionalOnMissingBean(name = "multipartMessageHttpConverter")
    @DependsOn({ "runtimeContextInitializer", "multipartMessageMapper" })
    <T extends IMessageObject> HttpMessageConverter<T> multipartMessageHttpConverter(
            MultipartMessageMapper multipartMessageMapper) {
        return new MultipartMessageHttpConverter<>(multipartMessageMapper);
    }

    // ************************************************************************
    // HttpMessageConverterComposite 등록
    // ************************************************************************

    /**
     * 등록된 모든 HttpMessageConverter를 통합하는 Composite 변환기 등록.
     *
     * @return {@link HttpMessageConverterComposite}
     */
    @Bean
    @ConditionalOnMissingBean(HttpMessageConverterComposite.class)
    @DependsOn({
            "jacksonMessageHttpConverter",
            "xmlMessageHttpConverter",
            "fixedLengthMessageHttpConverter",
            "formMessageHttpConverter",
            "multipartMessageHttpConverter"
    })
    <T extends IMessageObject> HttpMessageConverterComposite<T> httpMessageConverterComposite(
            JacksonMessageHttpConverter<T> jacksonMessageHttpConverter,
            XmlMessageHttpConverter<T> xmlMessageHttpConverter,
            FixedLengthMessageHttpConverter<T> fixedLengthMessageHttpConverter,
            FormMessageHttpConverter<T> formMessageHttpConverter,
            MultipartMessageHttpConverter<T> multipartMessageHttpConverter) {
        return new HttpMessageConverterComposite<>(List.of(
                jacksonMessageHttpConverter,
                xmlMessageHttpConverter,
                fixedLengthMessageHttpConverter,
                formMessageHttpConverter,
                multipartMessageHttpConverter));
    }

    // ************************************************************************
    // 응답 렌더러 구성
    // ************************************************************************

    /**
     * 일반 JSON/XML/text 응답용 렌더러 등록.
     *
     * @return {@link GenericResponseRenderer}
     */
    @Bean
    @ConditionalOnMissingBean(GenericResponseRenderer.class)
    GenericResponseRenderer genericResponseRenderer() {
        return new GenericResponseRenderer();
    }

    /**
     * 파일 다운로드 전용 렌더러 등록.
     *
     * @return {@link FileDownloadResponseRenderer}
     */
    @Bean
    @ConditionalOnMissingBean
    FileDownloadResponseRenderer fileDownloadResponseRenderer() {
        return new FileDownloadResponseRenderer();
    }

    /**
     * 다중 응답 렌더러를 조합해 처리하는 Composite 렌더러 등록.
     *
     * @param renderers 등록된 렌더러 목록
     * @return {@link ResponseRendererComposite}
     */
    @Bean
    @ConditionalOnMissingBean(ResponseRendererComposite.class)
    ResponseRendererComposite responseRendererComposite(List<IResponseRenderer<?, ?>> renderers) {
        return new ResponseRendererComposite(renderers);
    }
}
