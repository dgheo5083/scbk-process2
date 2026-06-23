package com.scbank.process.api.fw.message.converter;

import java.util.Map;

import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;

import lombok.RequiredArgsConstructor;

/**
 * 프레임워크 전문필드 Converter Registry 구현 클래스
 *
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public class MessageFieldConverterRegistry implements IMessageFieldConverterRegistry {

    private final Map<MessageType, IMessageFieldConverter<?, ?>> converters;

    @Override
    public IMessageFieldConverter<?, ?> get(MessageType type) {
        return this.converters.getOrDefault(type, null);
    }
}
