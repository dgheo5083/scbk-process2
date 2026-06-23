package com.scbank.process.api.fw.integration.support;

import com.scbank.process.api.fw.core.encrypt.IEncryptProcessorRegistrar;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.integration.IntegrationProperties.IntegrationSystemConfig;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.RequiredArgsConstructor;

/**
 * 연계(Integration) 모듈 전용 MessageContext 생성기
 * <p>
 * 시스템별 설정(SystemConfig)을 기반으로 동적으로 {@link MessageContext}를 생성합니다.
 * 변환 옵션, 포맷, 인코딩은 SystemConfig에서 가져오고,
 * 필드 변환기 및 암복호화 레지스트리는 Creator 주입 시 주입합니다.
 * </p>
 *
 * 주요 기능:
 * <ul>
 * <li>SystemConfig 기반 포맷/인코딩/직렬화/역직렬화 옵션 적용</li>
 * <li>공통 필드 변환기 및 암복호화 프로세서 적용</li>
 * </ul>
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class IntegrationMessageContextCreator {

    /** MessageContext 생성용 팩토리 */
    private final MessageContextFactory messageContextFactory;

    /**
     * 기본 포맷(format)과 인코딩(encoding)만 이용하여 MessageContext를 생성합니다.
     * (확장 옵션 없이 기본 옵션만 적용)
     *
     * @param format   메시지 포맷
     * @param encoding 문자 인코딩
     * @return 생성된 MessageContext
     */
    public MessageContext create(MessageFormat format, String encoding) {
        MessageContext messageContext = messageContextFactory.create(
                format,
                encoding,
                SerializationOptions.getDefaultOptions(format).get(),
                DeserializationOptions.getDefaultOptions(format).get());

        messageContext.setMessageFieldConverterRegistry(this.getMessageFieldConverterRegistryWithMessageFormat(format));
        messageContext.setEncryptProcessorRegistrar(this.getEncryptProcessorRegistrar());

        return messageContext;
    }

    /**
     * SystemConfig를 기반으로 MessageContext를 생성합니다.
     *
     * @param systemConfig 시스템별 설정 객체
     * @return 생성된 MessageContext
     */
    public MessageContext create(IntegrationSystemConfig systemConfig) {
        MessageContext messageContext = messageContextFactory.create(
                systemConfig.format(),
                systemConfig.charset(),
                this.mergeSerializationOptions(systemConfig),
                this.mergeDeserializationOptions(systemConfig));

        messageContext.setMessageFieldConverterRegistry(
                this.getMessageFieldConverterRegistryWithMessageFormat(systemConfig.format()));
        messageContext.setEncryptProcessorRegistrar(this.getEncryptProcessorRegistrar());

        return messageContext;
    }

    /**
     * 기본 직렬화 옵션에 채널 확장 옵션을 머지합니다.
     *
     * @param format 메시지 포맷
     * @return 병합된 직렬화 옵션
     */
    private SerializationOptions mergeSerializationOptions(IntegrationSystemConfig systemConfig) {
        MessageFormat format = systemConfig.format();
        SerializationOptions defaultOptions = SerializationOptions.getDefaultOptions(format)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported MessageFormat: " + format));

        MessageFormatOptionConfig formatOptionConfig = systemConfig.messageFormatOptions();
        if (formatOptionConfig != null && formatOptionConfig.getSerialization() != null) {
            return SerializationOptions.merge(defaultOptions, formatOptionConfig.getSerialization());
        }
        return defaultOptions;
    }

    /**
     * 기본 역직렬화 옵션에 채널 확장 옵션을 머지합니다.
     *
     * @param format 메시지 포맷
     * @return 병합된 역직렬화 옵션
     */
    private DeserializationOptions mergeDeserializationOptions(IntegrationSystemConfig systemConfig) {
        MessageFormat format = systemConfig.format();
        DeserializationOptions defaultOptions = DeserializationOptions.getDefaultOptions(format)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported MessageFormat: " + format));

        MessageFormatOptionConfig formatOptionConfig = systemConfig.messageFormatOptions();
        if (formatOptionConfig != null && formatOptionConfig.getDeserialization() != null) {
            return DeserializationOptions.merge(defaultOptions, formatOptionConfig.getDeserialization());
        }
        return defaultOptions;
    }

    /**
     * 메시지 포맷에 따라 적절한 필드 변환기 레지스트리를 반환합니다.
     *
     * @param format 메시지 포맷
     * @return 필드 변환기 레지스트리 인스턴스
     */
    private IMessageFieldConverterRegistry getMessageFieldConverterRegistryWithMessageFormat(MessageFormat format) {
        switch (format) {
            case FIXEDLENGTH -> {
                return RuntimeContext.getBean("fixedLengthMessageFieldConverterRegistry",
                        IMessageFieldConverterRegistry.class);
            }
            case JSON, XML -> {
                return RuntimeContext.getBean("jacksonMessageFieldConverterRegistry",
                        IMessageFieldConverterRegistry.class);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + format);
        }
    }

    /**
     * 암복호화 프로세서 레지스트리를 조회합니다.
     * 없으면 null 반환 (선택적 적용)
     *
     * @return 암복호화 레지스트리 또는 null
     */
    private IEncryptProcessorRegistrar getEncryptProcessorRegistrar() {
        try {
            return RuntimeContext.getBean(IEncryptProcessorRegistrar.class);
        } catch (Exception e) {
            return null;
        }
    }
}
