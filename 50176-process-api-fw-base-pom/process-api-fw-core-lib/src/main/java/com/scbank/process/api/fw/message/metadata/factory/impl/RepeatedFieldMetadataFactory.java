package com.scbank.process.api.fw.message.metadata.factory.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.BiFunction;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.metadata.DefaultMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.DefaultRepeatFieldMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageMetadata.MessageType;
import com.scbank.process.api.fw.message.metadata.factory.IMessageFieldMetadataFactory;
import com.scbank.process.api.fw.message.utils.MessageUtils;

/**
 * {@link RepeatedField}가 붙은 반복 필드를 처리하는 메타데이터 팩토리입니다.
 * <p>
 * 반복 항목이 {@link IMessageObject} 타입이면 재귀적으로 자식 필드 메타데이터를 생성합니다.
 * </p>
 */
public class RepeatedFieldMetadataFactory implements IMessageFieldMetadataFactory {

    private final BiFunction<Class<?>, String, List<IMessageMetadata>> recursiveExtractor;

    /**
     * 생성자
     *
     * @param recursiveExtractor 재귀 메타데이터 추출 함수 (targetClass, parentPath) →
     *                           List&lt;IMessageMetadata&gt;
     */
    public RepeatedFieldMetadataFactory(BiFunction<Class<?>, String, List<IMessageMetadata>> recursiveExtractor) {
        this.recursiveExtractor = recursiveExtractor;
    }

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(RepeatedField.class);
    }

    @Override
    public IMessageMetadata create(Field field, int order, String path) {
        MessageField mf = field.getAnnotation(MessageField.class);
        RepeatedField rf = field.getAnnotation(RepeatedField.class);

        Class<?> itemType = resolveRepeatedItemType(field);
        DefaultRepeatFieldMetadata metadata = DefaultRepeatFieldMetadata.builder()
                .type(MessageType.REPEATED)
                .fieldType(field.getType())
                .fieldName(field.getName())
                .id(mf.id())
                .name(mf.name())
                .order(order)
                .path(path)
                .repeatType(rf.repeatType())
                .repeatCount(rf.repeatCount())
                .wrapperName(rf.wrapperName())
                .elementName(rf.elementName())
                .repeatedGenericType(itemType)
                .build();

        if (IMessageObject.class.isAssignableFrom(itemType)) {
            List<IMessageMetadata> children = recursiveExtractor.apply(itemType, path + "[*]");
            metadata.setChildren(children.stream().sorted().toList());
        } else {
            DefaultMessageFieldMetadata itemMeta = DefaultMessageFieldMetadata.builder()
                    .type(MessageUtils.fromJavaType(itemType))
                    .fieldType(itemType)
                    .id(mf.id() + "[*]")
                    .name("item")
                    .order(0)
                    .path(path + "[*]")
                    .build();
            metadata.setChildren(List.of(itemMeta));
        }

        return metadata;
    }

    /**
     * 반복 항목의 제네릭 타입을 추출합니다.
     *
     * @param field 반복 필드
     * @return 제네릭 항목 클래스
     */
    private Class<?> resolveRepeatedItemType(Field field) {
        if (field.getGenericType() instanceof ParameterizedType pt &&
                pt.getActualTypeArguments()[0] instanceof Class<?> clazz) {
            return clazz;
        }
        throw new IllegalStateException("반복 필드 제네릭 타입을 추출할 수 없습니다: " + field.getName());
    }
}
