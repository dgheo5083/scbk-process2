package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scbank.process.api.fw.message.enums.VariableFieldType;

/**
 * <pre>
 * 가변 길이 필드를 정의하기 위한 어노테이션입니다.
 * 메시지 변환기에서 필드의 길이를 동적으로 처리할 수 있도록 메타 정보를 제공합니다.
 *
 * 예: 길이 prefix 방식, 고정 길이, 참조 필드 기반 등 다양한 길이 처리 방식 지원
 *
 * 사용 대상: 필드 (FIELD)
 * </pre>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariableLengthField {

    /**
     * <pre>
     * 가변 길이 필드의 타입을 정의합니다.
     * - FIXED: 고정 길이
     * - LENGTH_PREFIX: 길이 프리픽스를 앞에 둠
     * - REFERENCE: 다른 필드 값을 길이로 참조
     * </pre>
     */
    VariableFieldType lengthType() default VariableFieldType.FIXED;

    /**
     * <pre>
     * lengthType이 REFERENCE일 경우,
     * 길이를 참조할 다른 필드의 이름을 지정합니다.
     * </pre>
     */
    String referenceField() default "";

    /**
     * <pre>
     * lengthType이 LENGTH_PREFIX일 경우,
     * 앞부분에 위치한 길이 정보의 바이트 수를 정의합니다.
     * 예: 2 -> 앞 2바이트가 길이를 나타냄
     * </pre>
     */
    int lengthPrefixSize() default 0;

    /**
     * <pre>
     * lengthType이 FIXED일 경우,
     * 해당 필드의 고정 길이를 명시합니다.
     * -1은 설정되지 않음을 의미합니다.
     * </pre>
     */
    int fixedLength() default -1;

    /**
     * <pre>
     * delimiter 기반 필드일 경우,
     * 필드의 끝을 구분하는 구분자 바이트 값을 지정합니다.
     * 예: 0x1F, 0x0A 등
     * </pre>
     */
    byte delimiter() default 0x00;

    /**
     * <pre>
     * 해당 필드의 문자열 인코딩 방식을 정의합니다.
     * 예: "UTF-8", "EUC-KR", "ISO-8859-1" 등
     * </pre>
     */
    String encoding() default "";
}
