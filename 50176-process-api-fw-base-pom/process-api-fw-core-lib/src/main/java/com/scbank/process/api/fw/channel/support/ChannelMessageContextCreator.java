package com.scbank.process.api.fw.channel.support;

import java.util.Map;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.core.encrypt.IEncryptProcessorRegistrar;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.message.MessageFormatOptionConfig;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.converter.IMessageFieldConverterRegistry;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.DeserializationOptions;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.RequiredArgsConstructor;

/**
 * 채널 모듈 전용 MessageContext 생성기
 * 
 * 채널 프로퍼티(ChannelProperties) 기반으로 메시지 포맷별 기본 옵션 + 확장 옵션을 적용하여
 * {@link MessageContext}를 생성합니다.
 * 변환 옵션은 Serialization/Deserialization 모두 채널 설정값에 따라 기본 옵션에 덮어씌워집니다.
 * 
 * 이 클래스는 채널 모듈 전용으로 사용되며, 서비스 흐름에 따라 MessageContext를 동적으로 생성할 때 활용됩니다.
 *
 * 주요 기능:
 * <ul>
 * <li>메시지 포맷별 직렬화/역직렬화 옵션 병합</li>
 * <li>포맷에 따라 다른 필드 변환기 레지스트리 적용</li>
 * <li>암복호화 프로세서 레지스트리 설정</li>
 * </ul>
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class ChannelMessageContextCreator {

    /** 채널 프로퍼티 설정 */
    private final ChannelProperties channelProperties;
    /** MessageContext 생성용 팩토리 */
    private final MessageContextFactory messageContextFactory;

    /**
     * 주어진 메시지 포맷과 인코딩을 기준으로 MessageContext를 생성합니다.
     *
     * @param format   메시지 포맷 (예: FIXEDLENGTH, JSON, XML 등)
     * @param encoding 문자 인코딩 (예: UTF-8, EUC-KR 등)
     * @return 생성된 MessageContext 인스턴스
     */
    public MessageContext create(MessageFormat format, String encoding) {
        SerializationOptions serializationOptions = mergeSerializationOptions(format);
        DeserializationOptions deserializationOptions = mergeDeserializationOptions(format);

        MessageContext messageContext = messageContextFactory.create(format, encoding, serializationOptions,
                deserializationOptions);
        messageContext.setMessageFieldConverterRegistry(this.getMessageFieldConverterRegistryWithMessageFormat(format));
        messageContext.setEncryptProcessorRegistrar(this.getEncryptProcessorRegistrar());
        return messageContext;
    }

    /**
     * 기본 직렬화 옵션에 채널 확장 옵션을 머지합니다.
     *
     * @param format 메시지 포맷
     * @return 병합된 직렬화 옵션
     */
    private SerializationOptions mergeSerializationOptions(MessageFormat format) {
        SerializationOptions defaultOptions = SerializationOptions.getDefaultOptions(format)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported MessageFormat: " + format));

        Map<MessageFormat, MessageFormatOptionConfig> messageFormatOptionMap = channelProperties
                .getMessageFormatOptions();
        if (messageFormatOptionMap == null) {
            return defaultOptions;
        }

        MessageFormatOptionConfig formatOptionConfig = messageFormatOptionMap.get(format);
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
    private DeserializationOptions mergeDeserializationOptions(MessageFormat format) {
        DeserializationOptions defaultOptions = DeserializationOptions.getDefaultOptions(format)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported MessageFormat: " + format));

        Map<MessageFormat, MessageFormatOptionConfig> messageFormatOptionMap = channelProperties
                .getMessageFormatOptions();
        if (messageFormatOptionMap == null) {
            return defaultOptions;
        }

        MessageFormatOptionConfig formatOptionConfig = messageFormatOptionMap.get(format);
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
