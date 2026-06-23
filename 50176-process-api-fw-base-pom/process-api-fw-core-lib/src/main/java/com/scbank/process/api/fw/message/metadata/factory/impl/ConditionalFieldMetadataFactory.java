package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;

import com.scbank.process.api.fw.message.annotation.ConditionalField;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.metadata.DefaultConditionalFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * {@link ConditionalField} 어노테이션이 붙은 필드를 처리하는 팩토리입니다.
 * <p>
 * 조건부 필드 처리를 위한 메타데이터를 생성한다.
 * </p>
 * 
 * @author sungdon.choi
 */
public class ConditionalFieldMetadataFactory implements IMessageFieldMetadataFactory {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ConditionalField.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField mf = field.getAnnotation(MessageField.class);
        ConditionalField ext = field.getAnnotation(ConditionalField.class);

        return DefaultConditionalFieldMetadata.builder()
                .type(MessageUtils.fromJavaType(field.getType()))
                .fieldType(field.getType())
                .fieldName(field.getName())
                .id(mf.id())
                .name(mf.name())
                .length(mf.length())
                .scale(mf.scale())
                .required(mf.required())
                .padding(mf.padding())
                .align(mf.align())
                .masking(mf.masking())
                .defaultValue(mf.defaultValue())
                .encoding(mf.encoding())
                .encryptType(mf.encryptType())
                .order(order)
                .path(path)
                .condition(ext.condition())
                .build();
    }
}
