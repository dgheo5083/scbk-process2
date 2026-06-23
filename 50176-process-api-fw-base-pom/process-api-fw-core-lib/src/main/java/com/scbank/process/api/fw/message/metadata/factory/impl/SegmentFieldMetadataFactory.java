package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;
import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.SegmentField;
import com.scbank.process.api.fw.message.metadata.DefaultSegmentMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;

/**
 * {@link SegmentField} 어노테이션이 붙은 세그먼트 객체 필드를 처리하는 팩토리입니다.
 * <p>
 * 구분자와 위치 정보를 포함하는 {@link DefaultSegmentMessageFieldMetadata}를 생성합니다.
 * </p>
 * 
 * @author sungdon.choi
 */
public class SegmentFieldMetadataFactory implements IMessageFieldMetadataFactory {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(SegmentField.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField mf = field.getAnnotation(MessageField.class);
        SegmentField sf = field.getAnnotation(SegmentField.class);

        DefaultSegmentMessageFieldMetadata metadata = DefaultSegmentMessageFieldMetadata.builder()
                .type(MessageType.OBJECT)
                .fieldType(field.getType())
                .fieldName(field.getName())
                .id(mf.id())
                .name(mf.name())
                .required(mf.required())
                .masking(mf.masking())
                .order(order)
                .path(path)
                .delimiter(sf.delimiter())
                .delimiterPosition(sf.position())
                .delimiterType(sf.type())
                .build();

        if (IMessageObject.class.isAssignableFrom(field.getType())) {
            metadata.setChildren(List.of()); // 추후 재귀 처리 시 외부에서 주입
        }

        return metadata;
    }
}
