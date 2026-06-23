package com.scbank.process.api.fw.integration;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.scbank.process.api.fw.message.mapper.fixedlength.FixedLengthMessageMapper;
import com.scbank.process.api.fw.message.mapper.jackson.JacksonMessageMapper;
import com.scbank.process.api.fw.message.mapper.xml.XmlMessageMapper;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageDeserializer;
import com.scbank.process.api.fw.message.serializer.fixedlength.FixedLengthMessageSerializer;

/**
 * <pre>
 * 연동 프레임워크의 핵심 컴포넌트를 구성하는 Spring Boot 자동 구성 클래스입니다.
 *
 * 'framework.integration.enabled=true' 속성이 활성화된 경우에만 적용되며,
 * 다음과 같은 Bean을 자동 등록합니다:
 *
 * 2. FixedLength 포맷용 클라이언트 Codec
 * 3. Codec 레지스트리 (MessageFormat 기반)
 * 4. 연동 인터셉터 레지스트리
 * 5. 메시지 컨텍스트 생성기
 *
 * 각 Bean은 {@code @ConditionalOnMissingBean} 조건으로 선언되어 있어,
 * 외부에서 동일 타입의 Bean을 정의하면 자동 구성되지 않습니다.
 * </pre>
 *
 * @author sungdon.choi
 * @since 2025.04
 */
@Configuration
@ConditionalOnProperty(prefix = "csl.integration", name = "enabled", havingValue = "true")
public class IntegrationConfiguration {

    /**
     * FixedLength 포맷 전용 클라이언트 Codec 생성.
     *
     * @return {@link FixedLengthIntegrationClientCodec}
     */
    @Bean
    @ConditionalOnMissingBean(FixedLengthIntegrationClientCodec.class)
    @DependsOn({ "fixedLengthMessageFieldConverterRegistry" })
    FixedLengthIntegrationClientCodec fixedlengthIntegrationClientCodec() {
        return new FixedLengthIntegrationClientCodec(
                new FixedLengthMessageMapper(
                        new FixedLengthMessageSerializer(),
                        new FixedLengthMessageDeserializer()));
    }
    
    @Bean
    @ConditionalOnMissingBean(XmlIntegrationClientCodec.class)
    @DependsOn({ "jacksonMessageFieldConverterRegistry" })
    JsonIntegrationClientCodec jsonIntegrationClientCodec(ObjectMapper objectMapper) {
        return new JsonIntegrationClientCodec(new JacksonMessageMapper(objectMapper));
    }

    @Bean
    @ConditionalOnMissingBean(XmlIntegrationClientCodec.class)
    @DependsOn({ "jacksonMessageFieldConverterRegistry" })
    XmlIntegrationClientCodec xmlIntegrationClientCodec(XmlMapper xmlMapper) {
        return new XmlIntegrationClientCodec(new XmlMessageMapper(xmlMapper));
    }

    /**
     * {@link MessageFormat} 기반의 클라이언트 Codec 레지스트리를 구성합니다.
     *
     * @param fixedLengthCodec FIXEDLENGTH 포맷 처리용 Codec
     * @return {@link IntegrationClientCodecRegistry} 구현체
     */
    @Bean
    @ConditionalOnMissingBean(IntegrationClientCodecRegistry.class)
    IntegrationClientCodecRegistry integrationClientCodecRegistry(
            FixedLengthIntegrationClientCodec fixedLengthCodec,
            JsonIntegrationClientCodec jsonCodec,
            XmlIntegrationClientCodec xmlCodec) {
        DefaultIntegrationClientCodecRegistry registry = new DefaultIntegrationClientCodecRegistry();
        registry.register(MessageFormat.FIXEDLENGTH, fixedLengthCodec);
        registry.register(MessageFormat.XML, xmlCodec);
        registry.register(MessageFormat.JSON, jsonCodec);
        return registry;
    }

    /**
     * 연동 인터셉터 레지스트리를 구성합니다.
     *
     * @param interceptors {@link IntegrationInterceptor} 구현체 목록 (Spring 자동 주입)
     * @return {@link IntegrationInterceptorRegistry} 구현체
     */
    @Bean
    @ConditionalOnMissingBean(IntegrationInterceptorRegistry.class)
    IntegrationInterceptorRegistry integrationInterceptorRegistry(
            Map<String, IntegrationInterceptor> interceptors) {
        return new DefaultIntegrationInterceptorRegistry(interceptors);
    }

    /**
     * 연동 메시지 컨텍스트 생성기 등록.
     *
     * @return {@link IntegrationMessageContextCreator}
     */
    @Bean
    @ConditionalOnMissingBean(IntegrationMessageContextCreator.class)
    IntegrationMessageContextCreator integrationMessageContextCreator(MessageContextFactory messageContextFactory) {
        return new IntegrationMessageContextCreator(messageContextFactory);
    }
}
