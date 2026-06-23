package com.scbank.process.api.fw.core.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컴포넌트 메서드에 비즈니스 의미를 부여하기 위한 어노테이션입니다.
 * <p>
 * 주로 AOP 트레이스 로깅과 연계되어, 트랜잭션 흐름의 구간별 식별자 및 설명을 제공하며, 메서드 실행 구간을 추적하거나 문서화 목적으로
 * 사용됩니다.
 * </p>
 * <p>
 * 예: 사용자 로그인, 계좌 조회, 외부 시스템 호출 등
 * </p>
 *
 * <pre>{@code
 * &#64;ComponentOperation(name = "사용자 로그인", description = "사용자 인증 및 세션 처리", author = "홍길동")
 * public UserLoginOutput login(UserLoginInput input) {
 *     ...
 * }
 * }</pre>
 *
 * @author sungdon.choi
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentOperation {

    /**
     * 업무 메서드의 이름 또는 기능 설명 (필수) 예: "사용자 로그인", "계좌 조회"
     */
    String name() default "";

    /**
     * 업무 메서드에 대한 상세 설명 (선택)
     */
    String description() default "";

    /**
     * 업무 메서드 작성자 또는 담당자 명 (선택)
     */
    String author() default "";

    /**
     * 트레이스 대상여부
     * 
     * @return
     */
    boolean trace() default true;
}
