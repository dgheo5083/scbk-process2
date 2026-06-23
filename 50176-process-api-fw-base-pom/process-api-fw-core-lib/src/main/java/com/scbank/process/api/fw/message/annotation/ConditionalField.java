package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 조건에 따라 특정 필드의 직렬화 또는 역직렬화 여부를 결정하는 어노테이션입니다.
 *
 * 주로 메시지 변환 시, 조건에 따라 포함 여부가 달라지는 필드에 적용됩니다.
 * 내부적으로는 SpEL(Spring Expression Language) 표현식을 사용하여 조건을 해석합니다.
 *
 * 예시:
 * 
 * <pre>{@code
 * @ConditionalField(condition = "#useYn == 'Y'")
 * private String optionalData;
 * }</pre>
 *
 * 위의 예에서는 `useYn` 값이 "Y"일 때만 `optionalData` 필드가 메시지에 포함됩니다.
 *
 * @author sungdon.choi
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalField {

    /**
     * 필드 포함 여부를 결정하는 조건식입니다.
     *
     * <p>
     * SpEL(Spring Expression Language) 기반 표현식이며,
     * 현재 메시지 변환 컨텍스트 내의 값에 접근 가능합니다.
     * </p>
     *
     * @return 필드 활성화 조건 표현식
     */
    String condition();

    /**
     * 필드 조건에 대한 설명입니다. (Swagger 문서화 등에서 활용 가능)
     *
     * @return 설명 문자열
     */
    String description() default "";
}
