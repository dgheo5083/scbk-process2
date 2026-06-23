package com.scbank.process.api.fw.message.metadata;

import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DefaultRepeatFieldMetadata extends DefaultMessageFieldMetadata implements IRepeatFieldMetadata {

    private static final long serialVersionUID = 1L;

    /** 반복부 개수 (고정값 또는 참조 필드명) */
    private String repeatCount;

    /** 반복부 개수 해석 방식 ({@link RepeatType}) */
    private RepeatType repeatType;

    /** 반복부 제네릭 타입 (예: List&lt;타입&gt;) */
    private Class<?> repeatedGenericType;

    /** 반복부 래퍼(wrapper) 이름 (XML/JSON 전용) */
    private String wrapperName;

    /** 반복부 요소(element) 이름 */
    private String elementName;

    private String delimiter;
}
