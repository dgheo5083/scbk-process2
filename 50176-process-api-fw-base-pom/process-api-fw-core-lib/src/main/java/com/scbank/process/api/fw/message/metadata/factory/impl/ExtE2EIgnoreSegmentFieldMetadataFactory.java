package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;

import com.scbank.process.api.fw.message.annotation.ExtE2EIgnoreSegment;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.metadata.DefaultExtE2EIgnoreSegmentFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * {@link ExtE2EIgnoreSegment} 어노테이션이 붙은 필드를 처리하는 팩토리입니다.
 * <p>
 * 시작/종료 문자열 기반의 무시 구간 처리를 위한 메타데이터를 생성합니다.
 * </p>
 * 
 * @author sungdon.choi
 */
public class ExtE2EIgnoreSegmentFieldMetadataFactory implements IMessageFieldMetadataFactory {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ExtE2EIgnoreSegment.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField mf = field.getAnnotation(MessageField.class);
        ExtE2EIgnoreSegment ext = field.getAnnotation(ExtE2EIgnoreSegment.class);

        return DefaultExtE2EIgnoreSegmentFieldMetadata.builder()
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
                .start(ext.start())
                .end(ext.end())
                .build();
    }
}
