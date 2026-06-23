package com.scbank.process.api.fw.core.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 서비스 컴포넌트를 나타내는 커스텀 어노테이션입니다.
 * <p>
 * 비즈니스 로직을 담당하는 클래스에 부여되며, 컴포넌트 식별자(id)와 함께 설명, 작성자 등의 메타데이터를 함께 정의할 수 있습니다. 주로
 * 트레이스 로깅, 문서화, 동적 서비스 등록 등에 활용됩니다.
 * </p>
 *
 * <pre>{@code
 * &#64;ServiceComponent(
 *     id = "USER_LOGIN_SVC",
 *     name = "사용자 로그인 서비스",
 *     description = "로그인 인증 및 세션 처리 서비스",
 *     author = "홍길동",
 *     version = "1.0"
 * )
 * public class UserLoginService {
 *     ...
 * }
 * }</pre>
 *
 * @author sungdon.choi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceComponent {

    String url() default "";

    /**
     * 컴포넌트 고유 식별자 (필수) 예: "USER_LOGIN_SVC", "ACCT_BALANCE_SVC"
     */
    String id() default "";

    /**
     * 컴포넌트 이름 또는 제목 (선택) 예: "사용자 로그인 서비스"
     */
    String name() default "";

    /**
     * 컴포넌트 상세 설명 (선택)
     */
    String description() default "";

    /**
     * 작성자 또는 담당자 정보 (선택)
     */
    String author() default "";

    /**
     * 컴포넌트 버전 정보 (기본값: 1.0)
     */
    String version() default "1.0";
}
