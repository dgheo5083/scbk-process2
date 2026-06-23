package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scbank.process.api.fw.message.enums.RepeatType;

/**
 * <pre>
 * 반복부 필드를 정의하기 위한 어노테이션
 *
 * 예: 리스트 형태로 반복되는 메시지 필드 처리 시 사용
 *
 * 사용 대상: 필드 (FIELD)
 * </pre>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatedField {

    /**
     * <pre>
     * 반복 횟수를 나타내는 값 또는 참조할 필드명입니다.
     * 예: "3" 또는 "itemCount" 등
     * </pre>
     */
    String repeatCount() default "0";

    /**
     * <pre>
     * 반복 횟수를 어떤 방식으로 해석할지 지정합니다.
     * - NONE: 반복 아님
     * - FIXED: 고정 횟수 반복
     * - REFERENCE: 다른 필드 값 참조
     * </pre>
     */
    RepeatType repeatType() default RepeatType.NONE;

    /**
     * <pre>
     * XML 등 구조 기반 포맷에서 반복 필드를 감싸는 래퍼(wrapper) 요소명
     * 예: &lt;items&gt;...&lt;/items&gt; 구조에서 "items"
     * </pre>
     */
    String wrapperName() default "";

    /**
     * <pre>
     * 반복되는 개별 요소의 이름입니다.
     * 예: &lt;item&gt;...&lt;/item&gt; 반복 시 "item"
     * </pre>
     */
    String elementName() default "";

    /**
     * 반복 항목 간 구분자 (예: ";" → item1;item2;item3)
     */
    String delimiter() default "";
}
