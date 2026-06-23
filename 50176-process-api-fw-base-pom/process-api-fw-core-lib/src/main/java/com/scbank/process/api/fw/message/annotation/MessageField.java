package com.scbank.process.api.fw.message.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.enums.AlignType;

/**
 * 메시지 필드를 정의하기 위한 어노테이션 고정 길이, 암호화, 마스킹, 인코딩, 정렬, SOSI 처리 등 다양한 옵션을 제공
 *
 * @author sungdon.choi
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageField {

    /**
     * 필드 식별자 (ID) 주로 메시지 정의 XML 메타데이터와 매핑되는 키
     */
    String id();

    /**
     * 필드 이름 또는 설명 (문서화 용도)
     */
    String name() default "";

    /**
     * 필드 길이 (바이트 기준) 고정 길이 메시지 직렬화 시 필수
     */
    int length() default 0;

    /**
     * 소수점 자리수 (scale) BigDecimal 등 숫자형 필드에서 사용
     */
    int scale() default 0;

    /**
     * 필수 여부 (true = 값이 없으면 오류 발생)
     */
    boolean required() default false;

    /**
     * 정렬 방식 (LEFT, RIGHT, NONE) 기본값은 NONE (정렬 안함)
     */
    AlignType align() default AlignType.NONE;

    /**
     * 정렬 시 사용할 패딩 문자 예: "0" 또는 " "
     */
    String padding() default StringUtils.EMPTY;

    /**
     * 값이 null 또는 빈값일 때 대체할 기본값
     */
    String defaultValue() default "";

    /**
     * 로그 출력 시 마스킹 처리 여부 예: 주민번호, 계좌번호 등 민감 정보
     */
    boolean masking() default false;

    /**
     * 로그 출력 시 마스킹 처리 타입
     * 
     * @return
     */
    String maskingType() default "";

    /**
     * 암호화 방식 지정 (예: "AES", "SHA256") 암호화가 필요한 필드에 명시
     */
    String encryptType() default "";

    /**
     * 중첩 객체 타입 복합 필드 처리 시 내부 객체 지정
     */
    Class<?> targetClass() default Object.class;

    /**
     * 필드 전용 문자 인코딩 지정 (예: "UTF-8", "MS949") 전역 설정과 다를 경우에만 사용
     */
    String encoding() default "";

    /**
     * SOSI(SO=0x0E, SI=0x0F) 래핑 여부 한글이 포함된 필드에서 전각 문자 구간을 명시할 때 사용
     */
    boolean sosi() default false;

    /**
     * 멀티 바이트 문자 여부 (2바이트 한글 등) 고정 길이 정렬 시 정확한 바이트 계산을 위한 플래그
     */
    boolean multiBytes() default false;

    /**
     * 테스트 샘플 값 설정
     * 
     * @return
     */
    String example() default "";
}
