package com.scbank.process.api.fw.core.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * 공통 기능을 제공하는 공유 컴포넌트 클래스에 사용하는 어노테이션입니다.
 * <p>
 * 주로 여러 서비스 컴포넌트에서 함께 사용하는 유틸성 클래스, 공통 설정 제공자, 외부 연계 설정 빌더 등에 적용됩니다.
 * <p>
 * 내부적으로 {@link Component}를 포함하고 있어 Spring Bean 으로 자동 등록됩니다.
 * </p>
 *
 * <pre>{@code
 * &#64;SharedComponent(
 *     name = "MCI 설정 공유 컴포넌트",
 *     description = "MCI 연계 설정 정보를 구성하는 공유 클래스",
 *     author = "홍길동"
 * )
 * public class MciIntegrationConfigProvider {
 *     ...
 * }
 * }</pre>
 *
 * @author sungdon.choi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SharedComponent {

    /**
     * 공유 컴포넌트 이름 (예: "코드 관리", "연계 설정 제공자")
     */
    String name() default "";

    /**
     * 상세 설명 (예: 어떤 역할을 하는 공유 클래스인지 설명)
     */
    String description() default "";

    /**
     * 작성자 또는 책임자 이름
     */
    String author() default "";
}
