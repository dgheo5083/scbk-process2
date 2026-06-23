package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;

import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.VariableLengthField;
import com.scbank.process.api.fw.message.metadata.DefaultVariableLengthMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;

/**
 * {@link VariableLengthField} 어노테이션이 붙은 가변 길이 필드를 처리하는 팩토리입니다.
 * <p>
 * 길이 타입, 참조 필드, 구분자 등 정보를 포함한
 * {@link DefaultVariableLengthMessageFieldMetadata}를 생성합니다.
 * </p>
 * 
 * @author sungdon.choi
 */
public class VariableLengthFieldMetadataFactory implements IMessageFieldMetadataFactory {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(VariableLengthField.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField mf = field.getAnnotation(MessageField.class);
        VariableLengthField vlf = field.getAnnotation(VariableLengthField.class);

        return DefaultVariableLengthMessageFieldMetadata.builder()
                .type(MessageType.VARIABLE_LENGTH)
                .fieldType(field.getType())
                .fieldName(field.getName())
                .id(mf.id())
                .name(mf.name())
                .required(mf.required())
                .masking(mf.masking())
                .maskingType(mf.maskingType())
                .defaultValue(mf.defaultValue())
                .encoding(mf.encoding())
                .sosi(mf.sosi())
                .multiBytes(mf.multiBytes())
                .scale(mf.scale())
                .align(mf.align())
                .order(order)
                .path(path)
                .variableFieldType(vlf.lengthType())
                .referenceField(vlf.referenceField())
                .delimiter(vlf.delimiter())
                .lengthPrefixSize(vlf.lengthPrefixSize())
                .fixedLength(vlf.fixedLength())
                .encoding(vlf.encoding())
                .build();
    }
}
