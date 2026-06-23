package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * 기본 필드를 처리하는 메타데이터 팩토리 구현입니다.
 * <p>
 * 반복, 세그먼트, 가변길이 등 특별한 조건이 없는 일반 단일 필드를 처리합니다.
 * </p>
 * 
 * @author sungdon.choi
 */
public class DefaultFieldMetadataFactory implements IMessageFieldMetadataFactory {

    @Override
    public boolean supports(Field field) {
        // 기본 필드는 다른 특수 필드 조건을 모두 만족하지 않는 경우로 판단
        return field.isAnnotationPresent(MessageField.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField annotation = field.getAnnotation(MessageField.class);

        return DefaultMessageFieldMetadata.builder()
                .type(MessageUtils.fromJavaType(field.getType()))
                .fieldType(field.getType())
                .fieldName(field.getName())
                .id(annotation.id())
                .name(annotation.name())
                .length(annotation.length())
                .scale(annotation.scale())
                .required(annotation.required())
                .padding(annotation.padding())
                .align(annotation.align())
                .masking(annotation.masking())
                .maskingType(annotation.maskingType())
                .defaultValue(annotation.defaultValue())
                .encoding(annotation.encoding())
                .encryptType(annotation.encryptType())
                .order(order)
                .path(path)
                .sosi(annotation.sosi())
                .multiBytes(annotation.multiBytes())
                .example(annotation.example())
                .build();
    }
}
