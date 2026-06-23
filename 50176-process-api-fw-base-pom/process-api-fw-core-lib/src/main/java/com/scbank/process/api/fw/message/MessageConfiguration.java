package com.scbank.process.api.fw.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverter;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.converter.MessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.converter.format.common.BigDecimalFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.ByteFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.DoubleFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.FloatFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.NumberFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.StringFieldConverter;
import com.scbank.process.api.fw.message.converter.format.common.VariableLengthFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonBigDecimalFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonBooleanFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonDoubleFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonFloatFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonNumberFieldConverter;
import com.scbank.process.api.fw.message.converter.format.jackson.JacksonStringFieldConverter;
import com.scbank.process.api.fw.message.evaluate.ConditionEvaluatorComposite;
import com.scbank.process.api.fw.message.introspector.MessageFieldJacksonAnnotationIntrospector;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;
import com.scbank.process.api.fw.message.metadata.registry.impl.IntegrationMessageMetadataRegistrar;

/**
 * 전문 메시지 처리 관련 Bean들을 등록하는 Configuration 클래스.
 * 
 * 'csl.message.enabled=true'인 경우에만 활성화됩니다.
 * 메시지 메타데이터 등록자 및 메시지 필드 변환기 레지스트리 구성.
 * 
 * @author sungdon.choi
 */
@Configuration
@ConditionalOnProperty(prefix = "csl.message", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(MessageProperties.class)
public class MessageConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        AnnotationIntrospector jackson = new JacksonAnnotationIntrospector();
        AnnotationIntrospector custom = new MessageFieldJacksonAnnotationIntrospector();

        ObjectMapper objectMapper = new ObjectMapper();

        // configure
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 단일 ARRAY 리스트로 무조건 판단하도록
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        objectMapper.setAnnotationIntrospector(AnnotationIntrospectorPair.create(custom, jackson));
        return objectMapper;
    }

    @Bean
    XmlMapper xmlMapper() {
        AnnotationIntrospector jackson = new JacksonAnnotationIntrospector();
        AnnotationIntrospector custom = new MessageFieldJacksonAnnotationIntrospector();

        XmlMapper xmlMapper = new XmlMapper();

        // configure
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 단일 ARRAY 리스트로 무조건 판단하도록
        xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        xmlMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        xmlMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        xmlMapper.setAnnotationIntrospector(AnnotationIntrospectorPair.create(custom, jackson));
        return xmlMapper;
    }

    /**
     * 메시지 메타데이터 등록자 Bean을 생성합니다.
     *
     * @param properties 메시지 설정 프로퍼티
     * @return 메시지 메타데이터 등록자 구현체
     */
    @Bean
    @ConditionalOnMissingBean(IIntegrationMessageMetadataRegistrar.class)
    IIntegrationMessageMetadataRegistrar messageMetadataRegistrar(MessageProperties properties) {
        return new IntegrationMessageMetadataRegistrar(properties);
    }

    /**
     * FixedLength 메시지를 위한 필드 변환기 매핑을 생성합니다.
     *
     * @return 메시지 타입별 변환기 Map
     */
    @Bean("fixedLengthMessageFieldConverters")
    @ConditionalOnMissingBean(name = "fixedLengthMessageFieldConverters")
    Map<MessageType, IMessageFieldConverter<?, ?>> fixedLengthMessageFieldConverters() {
        return Map.of(
                // 2025.10.17 ByteFieldConverter 추가
                MessageType.BYTE, new ByteFieldConverter(),
                MessageType.STRING, new StringFieldConverter(),
                MessageType.SHORT, new NumberFieldConverter<>(Short.class),
                MessageType.INTEGER, new NumberFieldConverter<>(Integer.class),
                MessageType.LONG, new NumberFieldConverter<>(Long.class),
                MessageType.BIGDECIMAL, new BigDecimalFieldConverter(),
                MessageType.DOUBLE, new DoubleFieldConverter(),
                MessageType.FLOAT, new FloatFieldConverter(),
                MessageType.VARIABLE_LENGTH, new VariableLengthFieldConverter());
    }

    /**
     * Jackson(JSON) 메시지를 위한 필드 변환기 매핑을 생성합니다.
     *
     * @return 메시지 타입별 변환기 Map
     */
    @Bean("jacksonMessageFieldConverters")
    @ConditionalOnMissingBean(name = "jacksonMessageFieldConverters")
    Map<MessageType, IMessageFieldConverter<?, ?>> jsonMessageFieldConverters() {
        return Map.of(
        		MessageType.BOOLEAN, new JacksonBooleanFieldConverter(), //[2026.02.13 최성돈] 신규 추가 
                MessageType.STRING, new JacksonStringFieldConverter(new StringFieldConverter()),
                MessageType.SHORT, new JacksonNumberFieldConverter<>(new NumberFieldConverter<>(Short.class)),
                MessageType.INTEGER, new JacksonNumberFieldConverter<>(new NumberFieldConverter<>(Integer.class)),
                MessageType.LONG, new JacksonNumberFieldConverter<>(new NumberFieldConverter<>(Long.class)),
                MessageType.BIGDECIMAL, new JacksonBigDecimalFieldConverter(new BigDecimalFieldConverter()),
                MessageType.DOUBLE, new JacksonDoubleFieldConverter(new DoubleFieldConverter()),
                MessageType.FLOAT, new JacksonFloatFieldConverter(new FloatFieldConverter()));
    }

    /**
     * FixedLength 변환기 레지스트리를 생성합니다.
     *
     * @param fixedLengthMessageFieldConverters 타입별 변환기 Map
     * @return FixedLength 메시지 필드 변환기 레지스트리
     */
    @Bean("fixedLengthMessageFieldConverterRegistry")
    @ConditionalOnMissingBean(name = "fixedLengthMessageFieldConverterRegistry")
    @DependsOn({ "fixedLengthMessageFieldConverters" })
    IMessageFieldConverterRegistry fixedLengthMessageFieldConverterRegistry(
            @Qualifier("fixedLengthMessageFieldConverters") Map<MessageType, IMessageFieldConverter<?, ?>> fixedLengthMessageFieldConverters) {
        return new MessageFieldConverterRegistry(fixedLengthMessageFieldConverters);
    }

    /**
     * Jackson(JSON) 메시지 변환기 레지스트리를 생성합니다.
     *
     * @param jacksonMessageFieldConverters 타입별 변환기 Map
     * @return Jackson 메시지 필드 변환기 레지스트리
     */
    @Bean("jacksonMessageFieldConverterRegistry")
    @ConditionalOnMissingBean(name = "jacksonMessageFieldConverterRegistry")
    @DependsOn({ "jacksonMessageFieldConverters" })
    IMessageFieldConverterRegistry jacksonMessageFieldConverterRegistry(
            @Qualifier("jacksonMessageFieldConverters") Map<MessageType, IMessageFieldConverter<?, ?>> jacksonMessageFieldConverters) {
        return new MessageFieldConverterRegistry(jacksonMessageFieldConverters);
    }

    @Bean
    MessageContextFactory messageContextFactory() {
        return new MessageContextFactory();
    }

    @Bean
    ConditionEvaluatorComposite conditionOperatorComposite() {
        return new ConditionEvaluatorComposite();
    }
}
