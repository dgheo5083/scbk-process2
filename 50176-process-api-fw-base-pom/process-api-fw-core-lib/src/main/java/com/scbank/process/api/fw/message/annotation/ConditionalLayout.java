package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 조건에 따라 필드의 레이아웃(타입)을 동적으로 결정할 수 있도록 지원하는 어노테이션입니다.
 *
 * <p>
 * 이 어노테이션은 하나의 필드에 여러 조건부 레이아웃을 설정할 수 있으며,
 * 주로 고정 길이 메시지, 전문 메시지의 동적 구조 처리에 사용됩니다.
 * </p>
 *
 * 예시:
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;ConditionalLayout(condition = "#type == 'A'", type = LayoutA.class)
 *     @ConditionalLayout(condition = "#type == 'B'", type = LayoutB.class)
 *     private Object detail;
 * }
 * </pre>
 *
 * <p>
 * 표현식 언어(SpEL)를 기반으로 조건식을 정의하며,
 * 조건이 참이면 해당 타입으로 해당 필드가 직렬화 또는 역직렬화됩니다.
 * </p>
 *
 * @author sungdon.choi
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConditionalLayout.List.class)
public @interface ConditionalLayout {

    /**
     * 해당 레이아웃이 적용될 조건을 정의하는 SpEL(Spring Expression Language) 표현식입니다.
     * 
     * <p>
     * 예: {@code #type == 'A'}
     * </p>
     *
     * @return 조건 표현식
     */
    String condition();

    /**
     * 조건이 만족될 경우 적용될 DTO 타입 클래스입니다.
     *
     * @return 레이아웃 타입 클래스
     */
    Class<?> type();

    /**
     * 조건부 레이아웃에 대한 설명입니다. (문서화 목적)
     *
     * @return 설명 문자열
     */
    String description() default "";

    /**
     * 다중 {@link ConditionalLayout} 어노테이션 지원을 위한 컨테이너 어노테이션입니다.
     */
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        /**
         * 다중 조건 레이아웃 설정
         *
         * @return 조건부 레이아웃 배열
         */
        ConditionalLayout[] value();
    }
}
